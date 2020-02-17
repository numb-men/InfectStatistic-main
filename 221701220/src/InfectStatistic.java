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


class InfectStatistic {
    public static void main(String[] args) {
        
    }
}
