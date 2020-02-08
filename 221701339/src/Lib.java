import javafx.util.Pair;

import java.io.*;
import java.text.Collator;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Lib
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
public class Lib {
}

class Parameter {
    private String name;
    private Object value;

    public Parameter() {
    }

    public Parameter(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}

class ParameterRule {
    private final boolean valueRequired;
    private final boolean multivalued;

    public ParameterRule(boolean valueRequired, boolean multivalued) {
        this.valueRequired = valueRequired;
        this.multivalued = multivalued;
    }

    public boolean isValueRequired() {
        return valueRequired;
    }

    public boolean isMultivalued() {
        return multivalued;
    }
}

class ParameterHelper {
    public static List<Parameter> resolve(final String[] args, final Map<String, ParameterRule> ruleMap) {
        List<Parameter> parameters = new LinkedList<>();
        int i = 0;

        while (i < args.length) {
            String arg = args[i];
            if (ruleMap.containsKey(arg)) {
                ParameterRule rule = ruleMap.get(arg);
                if (rule.isValueRequired()) {
                    if (rule.isMultivalued()) {
                        List<String> values = new LinkedList<>();
                        while (++i < args.length) {
                            if (ruleMap.containsKey(args[i])) {
                                break;
                            } else {
                                values.add(args[i]);
                            }
                        }
                        if (values.isEmpty()) {
                            throw new IllegalArgumentException(arg + " 选项需要至少一个参数，但并未提供");
                        }
                        parameters.add(new Parameter(arg, values));
                    } else {
                        if (++i < args.length && !ruleMap.containsKey(args[i])) {
                            parameters.add(new Parameter(arg, args[i++]));
                        } else {
                            throw new IllegalArgumentException(arg + " 选项需要一个参数，但并未提供");
                        }
                    }
                } else {
                    parameters.add(new Parameter(arg, null));
                    i++;
                }
            } else {
                parameters.add(new Parameter(null, arg));
                i++;
            }
        }
        return parameters;
    }
}

class CommandInvokeErrorException extends Exception {
    public CommandInvokeErrorException() {
    }

    public CommandInvokeErrorException(String message) {
        super(message);
    }

    public CommandInvokeErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}

interface Command {
    /**
     * 使用指定的参数执行命令
     *
     * @param args 命名需要的参数
     * @throws CommandInvokeErrorException 当命令执行遇到错误时产生，比如参数设置不正确，命令内部执行出错
     */
    void invoke(String[] args) throws CommandInvokeErrorException;
}

class CommandManager {
    private Map<String, Command> map;

    public CommandManager() {
        this.map = new HashMap<>();
    }

    public Command register(String name, Command command) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        if (command == null) {
            throw new NullPointerException("command");
        }
        return map.put(name, command);
    }

    public Command unregister(String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        return map.remove(name);
    }

    public void invoke(String[] args) throws CommandInvokeErrorException {
        if (args == null) {
            throw new NullPointerException("args");
        }
        if (args.length < 1) {
            throw new IllegalArgumentException("给定参数列表arg必须至少含有一个参数");
        }
        String[] paramList = Arrays.copyOfRange(args, 1, args.length);
        if (map.containsKey(args[0])) {
            map.get(args[0]).invoke(paramList);
        } else {
            throw new IllegalArgumentException("未注册的命令:" + args[0]);
        }
    }
}

class ListCommand implements Command {
    private Parameter log;
    private Parameter out;
    private Parameter date;
    private Parameter type;
    private Parameter province;
    protected final Map<String, ParameterRule> RULES;
    protected Collection<Parameter> parameters;

    @Override
    public void invoke(String[] args) throws CommandInvokeErrorException {
        try {
            clear();
            parameters = ParameterHelper.resolve(args, RULES);
            prepare();
            if (canInvoke()) {
                invoke();
            }
        } catch (InfectStatisticException | IllegalArgumentException e) {
            throw new CommandInvokeErrorException("list:" + e.getMessage(), e);
        }
    }

    protected void invoke() throws InfectStatisticException {
        InfectStatistician statistician = new InfectStatistician();
        List<String> provinces = null;
        List<String> types = null;
        LocalDate endDate = null;
        if (province != null) {
            provinces = (List<String>) province.getValue();
        }
        if (type != null) {
            types = (List<String>) type.getValue();
        }
        if (date != null) {
            endDate = (LocalDate) date.getValue();
        }
        statistician.readDataFrom(log.getValue().toString(), endDate);
        statistician.formatAndSave(provinces, types, out.getValue().toString(), "UTF-8");
    }

    public ListCommand() {
        RULES = new HashMap<>();
        RULES.put("-log", new ParameterRule(true, false));
        RULES.put("-out", new ParameterRule(true, false));
        RULES.put("-type", new ParameterRule(true, true));
        RULES.put("-province", new ParameterRule(true, true));
        RULES.put("-date", new ParameterRule(true, false));
    }

    protected void clear() {
        log = out = date = type = province = null;
        parameters = null;
    }

    /**
     * 处理未知参数。当prepare()方法遇到未能处理<code>RULES</code>指定选项时候调用。
     *
     * @param parameter 要处理的未知参数
     */
    protected void handleUnknownOption(Parameter parameter) {
        throw new IllegalArgumentException("意外的输入 " + parameter.getValue());
    }

    protected void prepare() {
        for (Parameter parameter : parameters) {
            if (parameter.getName() == null) {
                handleUnknownOption(parameter);
            } else {
                switch (parameter.getName()) {
                    case "-log":
                        this.log = parameter;
                        break;
                    case "-out":
                        this.out = parameter;
                        break;
                    case "-type":
                        this.type = parameter;
                        break;
                    case "-province":
                        this.province = parameter;
                        break;
                    case "-date":
                        try {
                            String dateString = parameter.getValue().toString().trim();
                            parameter.setValue(LocalDate.parse(dateString));
                            this.date = parameter;
                        } catch (DateTimeParseException e) {
                            throw new IllegalArgumentException("-date 选项指定日期无法完成转换", e);
                        }
                        break;
                    default:
                        throw new IllegalArgumentException(parameter.getName() + " 选项不存在");
                }
            }
        }
    }

    protected boolean canInvoke() {
        if (log == null || out == null) {
            throw new IllegalArgumentException("-log 和 -out 选项必须指定");
        }
        return true;
    }
}


class InfectFileReader {
    private final String ENCODING;

    public InfectFileReader() {
        this("UTF-8");
    }

    public InfectFileReader(String encoding) {
        ENCODING = encoding;
    }

    public String[] read(File file) throws IOException {
        long length = file.length();
        byte[] content = new byte[(int) length];
        try (FileInputStream in = new FileInputStream(file)) {
            in.read(content);
        }
        return new String(content, ENCODING).split("\r?\n");
    }
}

class InfectionItem {
    public String name;
    public int patient;
    public int survivor;
    public int suspect;
    public int dead;

    @Override
    public String toString() {
        return "InfectItem{"
            + "name='" + name
            + ", 感染=" + patient
            + ", 疑似=" + suspect
            + ", 治愈=" + survivor
            + ", 死亡=" + dead
            + '}';
    }
}

class InfectionItemHelper {
    public static InfectionItem getOrCreateItem(Map<String, InfectionItem> map, String itemName) {
        if (map.containsKey(itemName)) {
            return map.get(itemName);
        }
        InfectionItem item = new InfectionItem();
        item.name = itemName;
        item.patient = 0;
        item.suspect = 0;
        item.survivor = 0;
        item.dead = 0;
        map.put(itemName, item);
        return item;
    }
}

class InfectDataParseException extends RuntimeException {
    public InfectDataParseException() {
    }

    public InfectDataParseException(String message) {
        super(message);
    }

    public InfectDataParseException(String message, Throwable cause) {
        super(message, cause);
    }
}

class InfectDataParser {
    private static final String CHINESE_CHAR = "[\\u4e00-\\u9fa5]";
    private static final String ANNOTATION = "\\s*//.*";
    private static final String NEW_PATIENT = CHINESE_CHAR + "+\\s+新增\\s+感染患者\\s+\\d+人\\s*";
    private static final String NEW_SUSPECT = CHINESE_CHAR + "+\\s+新增\\s+疑似患者\\s+\\d+人\\s*";
    private static final String SURE_PATIENT = CHINESE_CHAR + "+\\s+疑似患者\\s+确诊感染\\s+\\d+人\\s*";
    private static final String EXCLUDE_SUSPECT = CHINESE_CHAR + "+\\s+排除\\s+疑似患者\\s+\\d+人\\s*";
    private static final String NEW_DEAD = CHINESE_CHAR + "+\\s+死亡\\s+\\d+人\\s*";
    private static final String NEW_SURVIVOR = CHINESE_CHAR + "+\\s+治愈\\s+\\d+人\\s*";
    private static final String PATIENT_INFLOW =
        CHINESE_CHAR + "+\\s+感染患者\\s+流入\\s+" + CHINESE_CHAR + "+\\s+\\d+人\\s*";
    private final String SUSPECT_INFLOW =
        CHINESE_CHAR + "+\\s+疑似患者\\s+流入\\s+" + CHINESE_CHAR + "+\\s+\\d+人\\s*";

    protected void handleUnknownRow(String row) {
        if (row.matches(ANNOTATION)) {
            return;
        }
        throw new InfectDataParseException("不能识别的内容:" + row);
    }

    public Collection<InfectionItem> parse(String[] rows) {
        Map<String, InfectionItem> map = new HashMap<>(256);
        for (String row : rows) {
            String[] attrs = row.split("\\s+");
            if (row.matches(NEW_PATIENT)) {
                InfectionItem item = InfectionItemHelper.getOrCreateItem(map, attrs[0]);
                item.patient = item.patient + getNumberByAttr(attrs[3]);
            } else if (row.matches(NEW_SUSPECT)) {
                InfectionItem item = InfectionItemHelper.getOrCreateItem(map, attrs[0]);
                item.suspect = item.suspect + getNumberByAttr(attrs[3]);
            } else if (row.matches(SURE_PATIENT)) {
                InfectionItem item = InfectionItemHelper.getOrCreateItem(map, attrs[0]);
                int newPatient = getNumberByAttr(attrs[3]);
                item.suspect = item.suspect - newPatient;
                item.patient = item.patient + newPatient;
            } else if (row.matches(EXCLUDE_SUSPECT)) {
                InfectionItem item = InfectionItemHelper.getOrCreateItem(map, attrs[0]);
                item.suspect = item.suspect - getNumberByAttr(attrs[3]);
            } else if (row.matches(NEW_DEAD)) {
                InfectionItem item = InfectionItemHelper.getOrCreateItem(map, attrs[0]);
                int dead = getNumberByAttr(attrs[2]);
                item.patient = item.patient - dead;
                item.dead = item.dead + dead;
            } else if (row.matches(NEW_SURVIVOR)) {
                InfectionItem item = InfectionItemHelper.getOrCreateItem(map, attrs[0]);
                int survivor = getNumberByAttr(attrs[2]);
                item.patient = item.patient - survivor;
                item.survivor = item.survivor + survivor;
            } else if (row.matches(PATIENT_INFLOW)) {
                InfectionItem from = InfectionItemHelper.getOrCreateItem(map, attrs[0]);
                InfectionItem to = InfectionItemHelper.getOrCreateItem(map, attrs[3]);
                int num = getNumberByAttr(attrs[4]);
                from.patient = from.patient - num;
                to.patient = to.patient + num;
            } else if (row.matches(SUSPECT_INFLOW)) {
                InfectionItem from = InfectionItemHelper.getOrCreateItem(map, attrs[0]);
                InfectionItem to = InfectionItemHelper.getOrCreateItem(map, attrs[3]);
                int num = getNumberByAttr(attrs[4]);
                from.suspect = from.suspect - num;
                to.suspect = to.suspect + num;
            } else {
                handleUnknownRow(row);
            }
        }
        return map.values();
    }

    protected int getNumberByAttr(String attr) {
        try {
            return Integer.parseInt(attr.substring(0, attr.length() - 1));
        } catch (NumberFormatException e) {
            throw new InfectDataParseException("数据转换发生异常:" + e.getMessage(), e);
        }
    }
}

class InfectStatisticException extends Exception {
    public InfectStatisticException() {
    }

    public InfectStatisticException(String message) {
        super(message);
    }

    public InfectStatisticException(String message, Throwable cause) {
        super(message, cause);
    }
}

class InfectStatistician {

    private static final String FILE_NAME_PATTERN = "\\d{4}-\\d{2}-\\d{2}.log.txt";
    private static final String ANNOTATION = "// 该文档并非真实数据，仅供测试使用";
    private static final Collection<String> APPROVED_TYPES = Arrays.asList("ip", "sp", "cure", "dead");
    private final static Comparator<Object> DEFAULT_COMPARE = Collator.getInstance(java.util.Locale.CHINA);
    private boolean ready = false;
    private LocalDate endDate;
    private LocalDate minDate;
    private LocalDate maxDate;
    protected Vector<Pair<LocalDate, Collection<InfectionItem>>> data;

    public void readDataFrom(String path, LocalDate endDate) throws InfectStatisticException {
        ready = false;
        File targetDir = new File(path);
        if (!(targetDir.exists() && targetDir.isDirectory())) {
            throw new InfectStatisticException(path + ":不存在或者不是一个目录");
        }
        this.minDate = this.maxDate = null;
        this.endDate = endDate;

        File[] files = targetDir.listFiles((dir, name) -> name.matches(FILE_NAME_PATTERN));
        if (files == null) {
            return;
        }
        this.data = new Vector<>(files.length);
        List<Pair<LocalDate, File>> dateFilePairs = new LinkedList<>();
        for (int i = 0; i < files.length; i++) {
            try {
                String fileName = files[i].getName();
                LocalDate date = LocalDate.parse(fileName.substring(0, fileName.indexOf('.')));
                maintainDateBound(date);
                if (this.endDate != null && this.endDate.isBefore(date)) {
                    continue;
                }
                dateFilePairs.add(new Pair<>(date, files[i]));
            } catch (DateTimeParseException e) {
                System.out.println(files[i].getAbsolutePath() + ":文件名中的日期无效, " + e.getMessage());
            }
        }
        if (endDate != null && maxDate != null) {
            if (endDate.isAfter(maxDate) || endDate.isBefore(minDate)) {
                throw new InfectStatisticException("日期超出范围,已知范围:" + minDate + "至" + maxDate);
            }
        }
        dateFilePairs.parallelStream().forEach((dateFile) -> {
            try {
                InfectFileReader reader = new InfectFileReader();
                InfectDataParser parser = new InfectDataParser();
                Collection<InfectionItem> items = parser.parse(reader.read(dateFile.getValue()));
                data.add(new Pair<>(dateFile.getKey(), items));
            } catch (IOException | InfectDataParseException e) {
                System.out.println("无法处理文件" + dateFile.getValue().getAbsolutePath() + "," + e.getMessage());
            }
        });
        ready = true;
    }

    private void maintainDateBound(LocalDate date) {
        if (minDate != null) {
            if (minDate.isAfter(date)) {
                minDate = date;
            }
        } else {
            minDate = date;
        }
        if (maxDate != null) {
            if (maxDate.isBefore(date)) {
                maxDate = date;
            }
        } else {
            maxDate = date;
        }
    }

    protected String getFormatString(Collection<String> types) throws InfectStatisticException {
        if (types == null) {
            types = APPROVED_TYPES;
        }
        StringBuffer buffer = new StringBuffer(64);
        buffer.append("%s");
        for (String type : types) {
            switch (type) {
                case "ip":
                    buffer.append(" 感染患者%2$d人");
                    break;
                case "sp":
                    buffer.append(" 疑似患者%3$d人");
                    break;
                case "cure":
                    buffer.append(" 治愈%4$d人");
                    break;
                case "dead":
                    buffer.append(" 死亡%5$d人");
                    break;
                default:
                    throw new InfectStatisticException("类型参数中中含有不支持的类型");
            }
        }
        return buffer.toString();
    }

    protected String format(String format, InfectionItem item) {
        return String.format(format,
            item.name,
            item.patient,
            item.suspect,
            item.survivor,
            item.dead);
    }

    public void formatAndSave(Collection<String> provinces, Collection<String> types, String fileName, String encoding)
        throws InfectStatisticException {
        if (!ready) {
            throw new InfectStatisticException("无法执行操作，请重新取数据");
        }
        if (types != null && !APPROVED_TYPES.containsAll(types)) {
            throw new InfectStatisticException("类型参数中中含有不支持的类型");
        }
        if (endDate == null) {
            endDate = maxDate;
        }

        Map<String, InfectionItem> map = new HashMap<>(256);
        InfectionItem all = InfectionItemHelper.getOrCreateItem(map, "全国");
        map.remove(all.name);
        for (int i = 0; i < data.size(); i++) {
            Collection<InfectionItem> infectionItems = data.get(i).getValue();
            for (InfectionItem infectionItem : infectionItems) {
                InfectionItem item = InfectionItemHelper.getOrCreateItem(map, infectionItem.name);
                item.patient += infectionItem.patient;
                item.survivor += infectionItem.survivor;
                item.suspect += infectionItem.suspect;
                item.dead += infectionItem.dead;
                all.patient += infectionItem.patient;
                all.survivor += infectionItem.survivor;
                all.suspect += infectionItem.suspect;
                all.dead += infectionItem.dead;
            }
        }

        String formatString = getFormatString(types) + System.lineSeparator();
        try (BufferedWriter writer =
                 new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(fileName)), encoding))) {
            ArrayList<String> provinceList;
            boolean allRequired = false;


            if (provinces != null) {
                if (provinces.contains(all.name)) {
                    provinces.remove(all.name);
                    allRequired = true;
                }
                provinceList = new ArrayList<>(provinces);
            } else {
                provinceList = new ArrayList<>(map.keySet());
            }
            provinceList.sort(DEFAULT_COMPARE);
            if (provinces == null) {
                writer.write(format(formatString, all));
            }

            InfectionItem emptyItem = new InfectionItem();
            emptyItem.patient = 0;
            emptyItem.survivor = 0;
            emptyItem.suspect = 0;
            emptyItem.dead = 0;
            if (allRequired) {
                writer.write(format(formatString, all));
            }
            for (String province : provinceList) {
                if (map.containsKey(province)) {
                    InfectionItem item = map.get(province);
                    writer.write(format(formatString, item));
                } else {
                    emptyItem.name = province;
                    writer.write(format(formatString, emptyItem));
                }
            }
            writer.write(ANNOTATION);
        } catch (IOException e) {
            throw new InfectStatisticException("输出到文件时发生错误:" + e.getMessage(), e);
        }
    }


}