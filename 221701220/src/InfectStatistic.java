/**
 * InfectStatistic
 * TODO
 *
 * @author WeiNan Zhao
 * @version 1.0.0
 * @since 2020.01.15
 */


import java.util.*;
import java.lang.String;
import java.util.regex.*;


enum PatientType {

    INFECTION("感染患者","ip"),
    SUSPECTED("疑似患者","sp"),
    CURE("治愈","cure"),
    DEAD("死亡", "dead");

    private String typeName;
    private String abbr;

    PatientType(String typename, String abbr) {
        this.typeName = typename;
        this.abbr = abbr;
    }
    public String getTypeName() { return typeName; }
    public String getAbbr() { return abbr; }

    public static PatientType getTypeByAbbr(String abbr) {
        for(PatientType patientType : PatientType.values()) {
            if(abbr.equals(patientType.getAbbr())) {
                return patientType;
            }
        }
        return null;
    }

}


enum Option {

    LOG("-log"), OUT("-out"),
    DATE("-date"), TYPE("-type"),
    PROVINCE("-province");

    private String optionString;

    Option(String optionString) { this.optionString = optionString; }

    public String getOptionString() { return optionString; }

    public static Option getOptionByString(String optionString) {
        for(Option option : Option.values()) {
            if(optionString.equals(option.getOptionString())) {
                return option;
            }
        }
        return null;
    }
}


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


class Province {

    private static int[] nationalNumbers;
    private int[] localNumbers;

    Province() { localNumbers = new int[PatientType.values().length]; }

    public static int[] getNationalNumbers() { return nationalNumbers; }

    public int[] getLocalNumbers() { return localNumbers; }

    public static void setNationalNumbers(int[] nationalNumbers) {
        Province.nationalNumbers = nationalNumbers;
    }

    public void alterLocalNum(PatientType patientType, int changedNum) {
        localNumbers[patientType.ordinal()] += changedNum;
        nationalNumbers[patientType.ordinal()] += changedNum;
    }

}


class CommandArgsProcessor {

    private String[] args;
    private int[] optionsIndex;
    private String sourceDirectoryPath;
    private String outputFilePath;
    private String dateString;
    private List<PatientType> patientTypes;
    private TreeSet<String> provinceSet;

    CommandArgsProcessor(String[] args) {
        this.args = args;
        optionsIndex = new int[Option.values().length];
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

    public void setNextProcessor(AbstractLogLineProcessor nextProcessor) {
        this.nextProcessor = nextProcessor;
    }

    abstract protected void processLogLine(String logLine);

    public void processorChoose(String logLine) {
        if(pattern.matcher(logLine).find()) {
            processLogLine(logLine);
        }
        else if(nextProcessor != null) {
            nextProcessor.processorChoose(logLine);
        }
    }

}


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


class LogLineProcessor {

    private AbstractLogLineProcessor processorChains;

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
        processorChains.processorChoose(logLine);
    }

}


class InfectStatistic {
    public static void main(String[] args) {
        
    }
}
