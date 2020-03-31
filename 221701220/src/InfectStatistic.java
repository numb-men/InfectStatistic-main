/**
 * InfectStatistic
 * TODO
 *
 * @author WeiNan Zhao
 * @version 1.0.0
 * @since 2020.02.15
 */


import java.nio.charset.StandardCharsets;
import java.util.*;
import java.lang.String;
import java.util.regex.*;
import java.io.*;


/**
 * 将患者类型用枚举类表示，有利于拓展和数据处理
 * 例如将感染患者细化为轻症患者和重症患者，只需修改枚举类，并修改AbstartLogLineProcessor责任链的处理即可
 * typename用于输出，abbr用于提取-type选项参数值
 */
enum PatientType {

    INFECTION("感染患者","ip"),
    SUSPECTED("疑似患者","sp"),
    CURE("治愈","cure"),
    DEAD("死亡", "dead");

    public static final int size = PatientType.values().length;
    private String typeName;
    private String abbr;

    PatientType(String typename, String abbr) {
        this.typeName = typename;
        this.abbr = abbr;
    }
    public String getTypeName() { return typeName; }
    public String getAbbr() { return abbr; }

    /**
     * @param abbr 选项的缩写
     * @return abbr匹配返回缩写对应枚举变量，否则返回null
     */
    public static PatientType getTypeByAbbr(String abbr) {
        for(PatientType patientType : PatientType.values()) {
            if(abbr.equals(patientType.getAbbr())) {
                return patientType;
            }
        }
        return null;
    }

}


/**
 * 将list命令的选项用枚举类表示，便于拓展和数据处理
 */
enum Option {

    LOG("-log"), OUT("-out"),
    DATE("-date"), TYPE("-type"),
    PROVINCE("-province");

	public static final int size = Option.values().length;
    private String optionString;

    Option(String optionString) { this.optionString = optionString; }

    public String getOptionString() { return optionString; }

    /**
     * @param optionString 选项的文本内容
     * @return optionString匹配返回缩写对应枚举变量，否则返回null
     */
    public static Option getOptionByString(String optionString) {
        for(Option option : Option.values()) {
            if(optionString.equals(option.getOptionString())) {
                return option;
            }
        }
        return null;
    }
}


/**
 * 省份名称比较器
 * 以常量字符串中的省份顺序来对省份名称进行排序
 */
class ProvinceComparator implements Comparator<String> {

    private static final String PROVINCE_STRING = "全国 安徽 北京 重庆 福建 "
            + "甘肃 广东 广西 贵州 海南 河北 河南 黑龙江 湖北 湖南 吉林 江苏 江西 "
            + "辽宁 内蒙古 宁夏 青海 山东 山西 陕西 上海 四川 天津 西藏 新疆 云南 浙江";

    @Override
    public int compare(String s1, String s2) {
        return PROVINCE_STRING.indexOf(s1) - PROVINCE_STRING.indexOf(s2);
    }

    public static boolean isExist(String provinceName) {
        return PROVINCE_STRING.contains(provinceName);
    }

}


/**
 * 省份的实体类
 * 类静态变量nationalNumbers数组用来记录全国人数变化
 * 类私有成员localNumbers数组用来记录当地人数变化
 */
class Province {

    private static int[] nationalNumbers;
    private int[] localNumbers;

    Province() { localNumbers = new int[PatientType.size]; }

    public static int[] getNationalNumbers() { return nationalNumbers; }

    public int[] getLocalNumbers() { return localNumbers; }

    public static void setNationalNumbers(int[] nationalNumbers) {
        Province.nationalNumbers = nationalNumbers;
    }

    /**
     * @param patientType 患者类型，修改传入患者类型的数量
     * @param changedNum 日志文件提取的变化人数，总为正
     * 同步修改本省和全国的数据
     */
    public void alterLocalNum(PatientType patientType, int changedNum) {
        localNumbers[patientType.ordinal()] += changedNum;
        nationalNumbers[patientType.ordinal()] += changedNum;
    }

}


/**
 * 以省份名称为键，以Privince类实体为键组成的映射类
 * 使用TreeMap来实现省份名称的排序，便于有序输出
 */
class ProvinceTreeMap {

    private TreeMap<String, Province> treeMap;

    ProvinceTreeMap() {
        treeMap = new TreeMap<>(new ProvinceComparator());
    }

    public boolean isExistedProvince(String provinceName) {
        return treeMap.containsKey(provinceName);
    }

    public void createNewProvince(String provinceName) {
        treeMap.put(provinceName, new Province());
    }

    public Province getProvinceByName(String provinceName) {
        return treeMap.get(provinceName);
    }

    public Set<String> getKeySet() {
        return treeMap.keySet();
    }
}


/**
 * 命令行处理类
 * 根据传入的参数处理相应的数据
 * 使用Getter将配置好的参数传入Province
 */
class CommandArgsProcessor {

    private String[] args;//传入命令行的参数列表
    private int[] optionsIndex;//每个选项在命令行参数字符串数组中的索引
    private String sourceDirectoryPath;//源文件夹路径
    private String outputFilePath;//输出文件夹路径
    private String dateString;//date选项指定的日期
    private List<PatientType> patientTypes;//type选项指定的显示类型
    private TreeSet<String> provinceSet;//province指定的输出省份

    CommandArgsProcessor(String[] args) {
        this.args = args;
        optionsIndex = new int[Option.size];
    }

    public String getSourceDirectoryPath() {
        return sourceDirectoryPath;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public String getDateString() {
        return dateString;
    }

    public List<PatientType> getPatientTypes() {
        return patientTypes;
    }

    public TreeSet<String> getProvinceSet() {
        return provinceSet;
    }

    private void getOptionIndex() {
        Option option;
        for (int i = 0; i < args.length; i++) {
            if( (option = Option.getOptionByString(args[i])) != null) {
                optionsIndex[option.ordinal()] = i;
            }
        }
    }

    private void logOptionProcess() {
        int index = optionsIndex[Option.LOG.ordinal()];
        sourceDirectoryPath = args[index + 1];
    }

    private void outOptionProcess() {
        int index = optionsIndex[Option.OUT.ordinal()];
        outputFilePath = args[index + 1];
    }

    private void dateOptionProcess() {
        dateString = null;
        int index = optionsIndex[Option.DATE.ordinal()];
        if(index != 0) {
            dateString = args[index + 1];
        }
    }

    private void typeOptionProcess() {
        patientTypes = new LinkedList<>();
        int index = optionsIndex[Option.TYPE.ordinal()];
        //-type参数中指令的类型按照枚举变量的声明顺序存入types数组中，未指定的类型则为null
        if(index != 0) {
            PatientType patientType;
            for(int i = index + 1; i < args.length; i++) {
                if((patientType = PatientType.getTypeByAbbr(args[i])) != null) {
                    patientTypes.add(patientType);
                }
                else {
                    break;
                }
            }
        }
        //未指定-type命令，则获得所有种类
        else {
            patientTypes = Arrays.asList(PatientType.values());
        }
    }

    private void provinceOptionProcess() {

        provinceSet = new TreeSet<>(new ProvinceComparator());
        int index = optionsIndex[Option.PROVINCE.ordinal()];

        if(index != 0) {
            for(int i = index + 1; i < args.length; i++) {
                if(ProvinceComparator.isExist(args[i])) {
                    provinceSet.add(args[i]);
                }
                else {
                    break;
                }
            }
        }

    }

    public void processAllOptions() {

        getOptionIndex();
        logOptionProcess();
        outOptionProcess();
        dateOptionProcess();
        typeOptionProcess();
        provinceOptionProcess();
    }

}


/**
 * 用责任链模式来处理单行文本
 * 该类分别由IncreaseProcessor、MoveProcessor、DecreaseProcessor、ChangeProcessor继承
 * 获取到的需求单行文本分别由以上类依次处理
 * 若正则表达式匹配，则处理该需求文本，且不再继续向后传递需求文本
 * 否则继续向后传递需求文本
 */
abstract class AbstractLogLineProcessor {

    public static String INCREASE = "新增.*人";
    public static String MOVE = "流入.*人";
    public static String DECREASE = "[死亡|治愈].*人";
    public static String CHANGE = "[确诊|排除].*人";

    protected static ProvinceTreeMap provinceTreeMap;
    protected Pattern pattern;
    protected AbstractLogLineProcessor nextProcessor;

    public static void setProvinceTreeMap(ProvinceTreeMap provinceTreeMap) {
        AbstractLogLineProcessor.provinceTreeMap = provinceTreeMap;
    }

    //将继承本抽象类的所有处理类链接成责任链
    public void setNextProcessor(AbstractLogLineProcessor nextProcessor) {
        this.nextProcessor = nextProcessor;
    }

    abstract protected void processLogLine(String logLine);

    //需求在责任链上的选择和传递
    public void processorChoose(String logLine) {
        if(pattern.matcher(logLine).find()) {
            processLogLine(logLine);
        }
        else if(nextProcessor != null) {
            nextProcessor.processorChoose(logLine);
        }
    }

}


/**
 * 新增患者情况的处理类
 */
class IncreaseProcessor extends AbstractLogLineProcessor {

    IncreaseProcessor(String regex) {
        pattern = Pattern.compile(regex);
    }

    @Override
    protected void processLogLine(String logLine) {

        String[] lineStrings = logLine.split(" ");
        String provinceName = lineStrings[0];
        PatientType patientType =
                lineStrings[2].equals(PatientType.INFECTION.getTypeName()) ? PatientType.INFECTION : PatientType.SUSPECTED;
        int increasedNum = Integer.parseInt( lineStrings[3].replace("人", "") );

        if(!provinceTreeMap.isExistedProvince(provinceName)) {
            provinceTreeMap.createNewProvince(provinceName);
        }
        Province province = provinceTreeMap.getProvinceByName(provinceName);
        province.alterLocalNum(patientType, increasedNum);
    }

}


/**
 * 患者流动情况的处理类
 */
class MoveProcessor extends AbstractLogLineProcessor {

    MoveProcessor(String regex) {
        pattern = Pattern.compile(regex);
    }

    @Override
    protected void processLogLine(String logLine) {

        String[] lineStrings = logLine.split(" ");
        String provinceName1 = lineStrings[0];
        PatientType patientType =
                lineStrings[1].equals(PatientType.INFECTION.getTypeName()) ? PatientType.INFECTION : PatientType.SUSPECTED;
        String provinceName2 = lineStrings[3];
        int movedNum = Integer.parseInt( lineStrings[4].replace("人", "") );

        if(!provinceTreeMap.isExistedProvince(provinceName2)) {
            provinceTreeMap.createNewProvince(provinceName2);
        }
        Province province1 = provinceTreeMap.getProvinceByName(provinceName1);
        Province province2 = provinceTreeMap.getProvinceByName(provinceName2);
        province1.alterLocalNum(patientType, (-1) * movedNum);
        province2.alterLocalNum(patientType, movedNum);
    }

}


/**
 * 患者人数减少(被治愈或不幸死亡)的处理类
 */
class DecreaseProcessor extends AbstractLogLineProcessor {

    DecreaseProcessor(String regex) {
        pattern = Pattern.compile(regex);
    }

    @Override
    protected void processLogLine(String logLine) {

        String[] lineStrings = logLine.split(" ");
        String provinceName = lineStrings[0];
        PatientType patientType =
                lineStrings[1].equals(PatientType.DEAD.getTypeName()) ? PatientType.DEAD : PatientType.CURE;
        int decreasedNum = Integer.parseInt( lineStrings[2].replace("人", "") );

        if(!provinceTreeMap.isExistedProvince(provinceName)) {
            provinceTreeMap.createNewProvince(provinceName);
        }
        Province province = provinceTreeMap.getProvinceByName(provinceName);
        province.alterLocalNum(PatientType.INFECTION, (-1) * decreasedNum);
        province.alterLocalNum(patientType, decreasedNum);
    }

}


/**
 * 患者类型变化(疑似患者确诊或者被排除)的处理类
 */
class ChangeProcessor extends AbstractLogLineProcessor {

    ChangeProcessor(String regex) {
        pattern = Pattern.compile(regex);
    }

    @Override
    protected void processLogLine(String logLine) {

        boolean isConfirmed = logLine.contains("确诊");
        String[] lineStrings = logLine.split(" ");
        String provinceName = lineStrings[0];
        int changedNum = Integer.parseInt( lineStrings[3].replace("人", "") );

        if(!provinceTreeMap.isExistedProvince(provinceName)) {
            provinceTreeMap.createNewProvince(provinceName);
        }
        Province province = provinceTreeMap.getProvinceByName(provinceName);
        province.alterLocalNum(PatientType.SUSPECTED, (-1) * changedNum);
        if(isConfirmed) {
            province.alterLocalNum(PatientType.INFECTION, changedNum);
        }
    }

}


/**
 * 用来处理单行需求文本的总类
 * 使用以AbstractLogLineProcessor类实现的责任链来匹配和处理单行文本
 * 从而避免了冗长的if else或者switch语句
 */
class LogLineProcessor {

    private AbstractLogLineProcessor processorChains;

    /**
     * @param provinceTreeMap 传入的构造参数
     * 生成四个需求处理类，将他们链接成责任链，用于处理单行文本
     */
    LogLineProcessor(ProvinceTreeMap provinceTreeMap) {

        AbstractLogLineProcessor.setProvinceTreeMap(provinceTreeMap);
        AbstractLogLineProcessor increaseProcessor = new IncreaseProcessor(AbstractLogLineProcessor.INCREASE);
        AbstractLogLineProcessor moveProcessor = new MoveProcessor(AbstractLogLineProcessor.MOVE);
        AbstractLogLineProcessor decreaseProcessor = new DecreaseProcessor(AbstractLogLineProcessor.DECREASE);
        AbstractLogLineProcessor changeProcessor = new ChangeProcessor(AbstractLogLineProcessor.CHANGE);

        increaseProcessor.setNextProcessor(moveProcessor);
        moveProcessor.setNextProcessor(decreaseProcessor);
        decreaseProcessor.setNextProcessor(changeProcessor);

        processorChains = increaseProcessor;
    }

    public void processLogLine(String logLine) {
        if(!logLine.startsWith("//")) {
            processorChains.processorChoose(logLine);
        }
    }

}


/**
 * 文件比较器
 * 以文件名称升序排序，以防日志文件处理出错
 */
class FileComparator implements Comparator<File> {

    @Override
    public int compare(File file1, File file2) {
        return file1.getName().compareTo(file2.getName());
    }

}


/**
 * 文件处理类
 * 根据指定的-date参数来选择要处理的日志文件
 * 同时进行日期有效性的判断
 * 根据命令行的选项和参数配置文件路径和格式化输出
 */
class FileProcessor {

    private static final String SUFFIX = ".log.txt";//日志文件后缀
    private TreeSet<File> fileTreeSet;
    private ProvinceTreeMap provinceTreeMap;
    private LogLineProcessor logLineProcessor;//处理单行文本的类

    private File sourceDirectory;//命令行传入的源文件夹参数
    private FileOutputStream fileOutputStream;//命令行传入的目标文件路径的写入流
    private BufferedWriter bufferedWriter;//文件输出的高级流
    private String dateString;//命令行传入的date参数
    private List<PatientType> patientTypes;//命令行指定的患者类型
    private Set<String> provinceSet;//命令行指定的省份列表
    private boolean allowNation;//是否允许输出全国的数据

    public FileProcessor(ProvinceTreeMap provinceTreeMap, CommandArgsProcessor commandArgsProcessor) {

        fileTreeSet = new TreeSet<>(new FileComparator());
        this.provinceTreeMap = provinceTreeMap;
        logLineProcessor = new LogLineProcessor(provinceTreeMap);
        patientTypes = new LinkedList<>();
        argsUse(commandArgsProcessor);
    }

    /**
     * @param processor 命令行处理类
     * 使用命令行处理类的处理数据来配置路径和格式化输出
     */
    private void argsUse(CommandArgsProcessor processor) {

        sourceDirectory = new File(processor.getSourceDirectoryPath());
        try {
            fileOutputStream = new FileOutputStream(processor.getOutputFilePath());
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream,StandardCharsets.UTF_8));
        }
        catch (IOException exc) {
            System.out.println(Arrays.toString(exc.getStackTrace()));
            System.exit(1);
        }
        this.dateString = processor.getDateString();

        TreeSet<String> provinceSet = processor.getProvinceSet();
        allowNation = ( provinceSet.isEmpty() || provinceSet.contains("全国") );
        this.provinceSet = provinceSet.isEmpty() ? provinceTreeMap.getKeySet() : provinceSet;

        patientTypes = processor.getPatientTypes();

    }

    /**
     * 根据-date的日期来选择需要处理那些文件
     */
    private void setProcessedFiles() {

        if(dateString == null) {
            fileTreeSet.addAll(Arrays.asList(sourceDirectory.listFiles()));
        }
        else {
            for(File file : sourceDirectory.listFiles()) {
                if(file.getName().compareTo(dateString + SUFFIX) <= 0)
                    fileTreeSet.add(file);
            }
            if(fileTreeSet.size() == sourceDirectory.listFiles().length &&
                    fileTreeSet.last().getName().compareTo(dateString + SUFFIX) < 0) {
                System.out.println("日期超出范围");
                System.exit(1);
            }
        }

    }

    /**
     * 处理所有文件，将日志文件的每一行字符串都交给LogLineProcessor责任链来处理
     */
    public void processFiles() {

        setProcessedFiles();

        for (File file:fileTreeSet) {
            String logLine;
            try {
                InputStream inputStream  = new FileInputStream(file.getAbsolutePath());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                while ((logLine = bufferedReader.readLine()) != null) {
                    logLineProcessor.processLogLine(logLine);
                }
                bufferedReader.close();
                inputStream.close();
            }
            catch (IOException exc) {
                System.out.println(Arrays.toString(exc.getStackTrace()));
                System.exit(1);
            }
        }

    }

    /**
     * 根据布尔变量allowNation的值来决定是否输出全国情况
     */
    private void outputNationData() {

        if(allowNation) {
            provinceSet.remove("全国");
            try {
                bufferedWriter.write("全国");
                for (PatientType patientType : patientTypes) {
                    bufferedWriter.write(" " + patientType.getTypeName());
                    bufferedWriter.write(Province.getNationalNumbers()[patientType.ordinal()] + "人");
                }
                bufferedWriter.newLine();
            }
            catch (IOException exc) {
                System.out.println(Arrays.toString(exc.getStackTrace()));
                System.exit(1);
            }
        }
    }

    /**
     * 将结果输出到指定的文件中
     * 允许输出全国数据则先输出全国数据
     * 然后继续输出各省数据
     */
    public void outputResult() {

        outputNationData();

        Iterator<String> iterator = provinceSet.iterator();
        try {
            while (iterator.hasNext()) {
                String provinceName = iterator.next();
                if (!provinceTreeMap.isExistedProvince(provinceName)) {
                    provinceTreeMap.createNewProvince(provinceName);
                }
                Province province = provinceTreeMap.getProvinceByName(provinceName);

                bufferedWriter.write(provinceName);
                for (PatientType patientType : patientTypes) {
                    bufferedWriter.write(" " + patientType.getTypeName());
                    bufferedWriter.write(province.getLocalNumbers()[patientType.ordinal()] + "人");
                }
                bufferedWriter.newLine();
            }
            bufferedWriter.write("// 该文档并非真实数据，仅供测试使用\n");
            bufferedWriter.close();
            fileOutputStream.close();
        }
        catch (IOException exc) {
            System.out.println(Arrays.toString(exc.getStackTrace()));
            System.exit(1);
        }

    }

}


/**
 * 文件统计处理类
 * 用于main函数中生成、配置处理类
 * 并进行结果的输出
 */
class StatisticProcessor {

    private String[] args;
    private FileProcessor fileProcessor;

    StatisticProcessor(String[] args) { this.args = args; }

    public void initialize() {
        Province.setNationalNumbers(new int[PatientType.size]);
        CommandArgsProcessor commandArgsProcessor = new CommandArgsProcessor(args);
        commandArgsProcessor.processAllOptions();
        ProvinceTreeMap provinceTreeMap = new ProvinceTreeMap();
        fileProcessor = new FileProcessor(provinceTreeMap, commandArgsProcessor);
    }

    public void process() {
        fileProcessor.processFiles();
    }

    public void output() {
        fileProcessor.outputResult();
    }
}

/**
 * main函数，程序运行的入口
 */
class InfectStatistic {
    public static void main(String[] args) {

        StatisticProcessor statisticProcessor = new StatisticProcessor(args);

        statisticProcessor.initialize();
        statisticProcessor.process();
        statisticProcessor.output();

    }
}
