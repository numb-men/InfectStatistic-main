import javafx.util.Pair;

import java.io.*;
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

class ListCommand {
    private Parameter log;
    private Parameter out;
    private Parameter date;
    private Parameter type;
    private Parameter province;
    protected final Map<String, ParameterRule> RULES;
    protected Collection<Parameter> parameters;

    public void execute(String[] args) {
        clear();
        parameters = ParameterHelper.resolve(args, RULES);
        prepare();
        if (canInvoke()) {
            execute();
        }
    }

    protected void execute() {

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
        Long length = file.length();
        byte[] content = new byte[length.intValue()];
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
    protected static final String CHINESE_CHAR = "[\\u4e00-\\u9fa5]";
    protected static final String ANNOTATION = "\\s*//.*";
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
                InfectionItem item = getOrCreateItem(map, attrs[0]);
                item.patient = item.patient + getNumberByAttr(attrs[3]);
            } else if (row.matches(NEW_SUSPECT)) {
                InfectionItem item = getOrCreateItem(map, attrs[0]);
                item.suspect = item.suspect + getNumberByAttr(attrs[3]);
            } else if (row.matches(SURE_PATIENT)) {
                InfectionItem item = getOrCreateItem(map, attrs[0]);
                int newPatient = getNumberByAttr(attrs[3]);
                item.suspect = item.suspect - newPatient;
                item.patient = item.patient + newPatient;
            } else if (row.matches(EXCLUDE_SUSPECT)) {
                InfectionItem item = getOrCreateItem(map, attrs[0]);
                item.suspect = item.suspect - getNumberByAttr(attrs[3]);
            } else if (row.matches(NEW_DEAD)) {
                InfectionItem item = getOrCreateItem(map, attrs[0]);
                int dead = getNumberByAttr(attrs[2]);
                item.patient = item.patient - dead;
                item.dead = item.dead + dead;
            } else if (row.matches(NEW_SURVIVOR)) {
                InfectionItem item = getOrCreateItem(map, attrs[0]);
                int survivor = getNumberByAttr(attrs[2]);
                item.patient = item.patient - survivor;
                item.survivor = item.survivor + survivor;
            } else if (row.matches(PATIENT_INFLOW)) {
                InfectionItem from = getOrCreateItem(map, attrs[0]);
                InfectionItem to = getOrCreateItem(map, attrs[3]);
                int num = getNumberByAttr(attrs[4]);
                from.patient = from.patient - num;
                to.patient = to.patient + num;
            } else if (row.matches(SUSPECT_INFLOW)) {
                InfectionItem from = getOrCreateItem(map, attrs[0]);
                InfectionItem to = getOrCreateItem(map, attrs[3]);
                int num = getNumberByAttr(attrs[4]);
                from.suspect = from.suspect - num;
                to.suspect = to.suspect + num;
            } else {
                handleUnknownRow(row);
            }
        }
        return map.values();
    }

    protected Integer getNumberByAttr(String attr) {
        return Integer.parseInt(attr.substring(0, attr.length() - 1));
    }

    protected InfectionItem getOrCreateItem(Map<String, InfectionItem> map, String itemName) {
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

class InfectStatisticException extends RuntimeException {
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
    private LocalDate minDate;
    private LocalDate maxDate;
    public Vector<Pair<LocalDate, Collection<InfectionItem>>> data;

    public void readDataFrom(String path) {
        File targetDir = new File(path);
        if (!(targetDir.exists() && targetDir.isDirectory())) {
            throw new InfectStatisticException(path + ":不存在或者不是一个目录");
        }
        File[] files = targetDir.listFiles((dir, name) -> name.matches(FILE_NAME_PATTERN));
        data = new Vector<>(files.length);
        List<File> fileList = Arrays.asList(files);
        fileList.parallelStream().forEach((file) -> {
            try {
                String fileName = file.getName();
                LocalDate date = LocalDate.parse(fileName.substring(0, fileName.indexOf('.')));
                InfectFileReader reader = new InfectFileReader();
                InfectDataParser parser = new InfectDataParser();
                Collection<InfectionItem> items = parser.parse(reader.read(file));
                data.add(new Pair<>(date, items));
            } catch (DateTimeParseException e) {
                System.out.println(file.getAbsolutePath() + ":文件名中的日期无效, " + e.getMessage());
            } catch (IOException | InfectDataParseException e) {
                System.out.println("无法处理文件"+file.getAbsolutePath()+","+ e.getMessage());
            }
        });
    }
}