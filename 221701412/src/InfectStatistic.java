import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * InfectStatistic
 * TODO
 * 疫情统计
 * @author 221701412_theTuring
 * @version v 1.0.0
 * @since 2020.2.4
 */
class InfectStatistic {

    /**
     * CommandLine
     * TODO
     *
     * @author 221701412_theTuring
     * @version v 1.0.0
     * @description 整条命令行对应的实体类
     * eg $ java InfectStatistic list -date 2020-01-22 -log D:/log/ -out D:/output.txt
     * 命令行分为 命令+命令行参数
     * 命令 list
     * 命令行参数 -log -out
     * @since 2020.2.7
     */
    static class CommandLine {

        private Command command;

        private Arguments arguments;

        public Command getCommand() {
            return command;
        }


        public void setCommand(Command command) {
            this.command = command;
        }

        public Arguments getArguments() {
            return arguments;
        }

        public void setArguments(Arguments arguments) {
            this.arguments = arguments;
        }


        /**
         * Command
         * TODO
         *
         * @author 221701412_theTuring
         * @version v 1.0.0
         * @description 命令
         * 现有命令 list
         * @since 2020.2.7
         */
        static class Command {

            //List （命令）
            private boolean list;

            public boolean isList() {
                return list;
            }

            public void setList(boolean list) {
                this.list = list;
            }
        }

        /**
         * Arguments
         * TODO
         *
         * @author 221701412_theTuring
         * @version v 1.0.0
         * @description 命令行参数对应的实体类
         * 现有命令行参数 log out date type province
         * -log 指定日志目录的位置，该项必会附带，请直接使用传入的路径，而不是自己设置路径
         * -out 指定输出文件路径和文件名，该项必会附带，请直接使用传入的路径，而不是自己设置路径
         * -date 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期之前的所有log文件
         * -type 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，
         * 使用缩写选择，如 -type ip 表示只列出感染患者的情况，-type sp cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。
         * -province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江
         * @since 2020.2.7
         */
        static class Arguments {

            private boolean log;

            private String log_content;

            private boolean out;

            private String out_content;

            private boolean date;

            private String date_content;

            private boolean type;

            private ArrayList<TypeOptionDto> typeOption;

            private boolean province;

            private ArrayList<String> province_content;

            public boolean isLog() {
                return log;
            }

            public void setLog(boolean log) {
                this.log = log;
            }

            public String getLog_content() {
                return log_content;
            }

            public void setLog_content(String log_content) {
                this.log_content = log_content;
            }

            public boolean isOut() {
                return out;
            }

            public void setOut(boolean out) {
                this.out = out;
            }

            public String getOut_content() {
                return out_content;
            }

            public void setOut_content(String out_content) {
                this.out_content = out_content;
            }

            public boolean isDate() {
                return date;
            }

            public void setDate(boolean date) {
                this.date = date;
            }

            public String getDate_content() {
                return date_content;
            }

            public void setDate_content(String date_content) {
                this.date_content = date_content;
            }

            public boolean isType() {
                return type;
            }

            public void setType(boolean type) {
                this.type = type;
            }

            public List<TypeOptionDto> getTypeOption() {
                return typeOption;
            }

            public void setTypeOption(ArrayList<TypeOptionDto> typeOption) {
                this.typeOption = typeOption;
            }

            public boolean isProvince() {
                return province;
            }

            public void setProvince(boolean province) {
                this.province = province;
            }

            public List<String> getProvince_content() {
                return province_content;
            }

            public void setProvince_content(ArrayList<String> province_content) {
                this.province_content = province_content;
            }
        }

        /**
         * TypeOption
         * TODO
         *
         * @author 221701412_theTuring
         * @version v 1.0.0
         * @description 命令行参数中 type 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]
         * 使用的枚举类型
         * @since 2020.2.7
         */
        enum TypeOptionEnum {

            IP("infection_patients", 1, false), SP("suspected patients", 2, false),
            CURE("cure_patients", 3, false), DEAD("dead__patient", 4, false);

            //类型名
            private String name;
            //索引
            private int index;
            //状态 false：未选中 true：选中
            private boolean status;

            private TypeOptionEnum(String name, int index, boolean status) {

                this.name = name;
                this.index = index;
                this.status = status;

            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
            }

            public boolean isStatus() {
                return status;
            }

            public void setStatus(boolean status) {
                this.status = status;
            }

            public void setTypeOptionIP() {

            }
        }

        /**
         * TypeOptionDto
         * TODO
         *
         * @author 221701412_theTuring
         * @version v 1.0.0
         * @description 封装枚举类型的类
         * @since 2020.2.7
         */
        static class TypeOptionDto {

            //类型名
            private String name;
            //索引
            private int index;
            //状态 false：未选中 true：选中
            private boolean status;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
            }

            public boolean isStatus() {
                return status;
            }

            public void setStatus(boolean status) {
                this.status = status;
            }

            /**
             * setTypeOptionIp()
             * TODO
             * @description 创建ip选项的实例化
             * @author 221701412_theTuring
             * @version v 1.0.0
             * @since 2020.2.7
             * @return TypeOptionDto
             */
            public TypeOptionDto setTypeOptionIp(){

                CommandLine.TypeOptionDto typeOptionDto = new CommandLine.TypeOptionDto();
                typeOptionDto.setName(TypeOptionEnum.IP.getName());
                typeOptionDto.setIndex(TypeOptionEnum.IP.getIndex());
                TypeOptionEnum.IP.setStatus(true);
                typeOptionDto.setStatus(TypeOptionEnum.IP.isStatus());

                return typeOptionDto;
            }

            /**
             * setTypeOptionSp()
             * TODO
             * @description 创建sp选项的实例化
             * @author 221701412_theTuring
             * @version v 1.0.0
             * @since 2020.2.7
             * @return TypeOptionDto
             */
            public TypeOptionDto setTypeOptionSp(){

                CommandLine.TypeOptionDto typeOptionDto = new CommandLine.TypeOptionDto();
                typeOptionDto.setName(TypeOptionEnum.SP.getName());
                typeOptionDto.setIndex(TypeOptionEnum.SP.getIndex());
                TypeOptionEnum.SP.setStatus(true);
                typeOptionDto.setStatus(TypeOptionEnum.SP.isStatus());

                return typeOptionDto;
            }

            /**
             * setTypeOptionCure()
             * TODO
             * @description 创建cure选项的实例化
             * @author 221701412_theTuring
             * @version v 1.0.0
             * @since 2020.2.7
             * @return TypeOptionDto
             */
            public TypeOptionDto setTypeOptionCure(){

                CommandLine.TypeOptionDto typeOptionDto = new CommandLine.TypeOptionDto();
                typeOptionDto.setName(TypeOptionEnum.CURE.getName());
                typeOptionDto.setIndex(TypeOptionEnum.CURE.getIndex());
                TypeOptionEnum.CURE.setStatus(true);
                typeOptionDto.setStatus(TypeOptionEnum.CURE.isStatus());

                return typeOptionDto;
            }

            /**
             * setTypeOptionDead()
             * TODO
             * @description 创建dead选项的实例化
             * @author 221701412_theTuring
             * @version v 1.0.0
             * @since 2020.2.7
             * @return TypeOptionDto
             */
            public TypeOptionDto setTypeOptionDead(){

                CommandLine.TypeOptionDto typeOptionDto = new CommandLine.TypeOptionDto();
                typeOptionDto.setName(TypeOptionEnum.DEAD.getName());
                typeOptionDto.setIndex(TypeOptionEnum.DEAD.getIndex());
                TypeOptionEnum.DEAD.setStatus(true);
                typeOptionDto.setStatus(TypeOptionEnum.DEAD.isStatus());

                return typeOptionDto;
            }
        }


    }

    /**
     * CommandLineAnalytic
     * TODO
     *
     * @author 221701412_theTuring
     * @version v 1.0.0
     * @description 命令行解析类
     * @since 2020.2.7
     */
    static class CommandLineAnalytic {

        /**
         * Analytic()
         * TODO
         *
         * @return CommandLine
         * @description 命令行解析方法
         * @author 221701412_theTuring
         * @version v 1.0.0
         * @since 2020.2.7
         */
        public CommandLine Analytic(List<String> list) {

            //实例化命令行
            CommandLine CommandLine = new CommandLine();

            //实例化命令
            CommandLine.Command command = new CommandLine.Command();

            //实例化命令行参数
            CommandLine.Arguments arguments = new CommandLine.Arguments();

            for (int i = 0; i < list.size(); i++) {

                String temp = list.get(i);

                switch (temp) {
                    case "list":
                        command.setList(true);
                        break;
                    case "-log":
                        arguments.setLog(true);
                        arguments.setLog_content(list.get(i + 1));
                        break;
                    case "-out":
                        arguments.setOut(true);
                        arguments.setOut_content(list.get(i + 1));
                        break;
                    case "-date":
                        arguments.setDate(true);
                        arguments.setDate_content(list.get(i + 1));
                        break;
                    case "-type":
                        arguments.setType(true);
                        ArrayList<CommandLine.TypeOptionDto> arrayList = new ArrayList<>();
                        for (int j = i; j < list.size(); j++) {
                            String type = list.get(j);
                            if (!type.substring(0, 1).equals('-')) {

                                if (type.equals("ip")) {
                                    InfectStatistic.CommandLine.TypeOptionDto typeOptionDto = new CommandLine.TypeOptionDto();
                                    arrayList.add(typeOptionDto.setTypeOptionIp());
                                }

                                if (type.equals("sp")) {
                                    InfectStatistic.CommandLine.TypeOptionDto typeOptionDto = new CommandLine.TypeOptionDto();
                                    arrayList.add(typeOptionDto.setTypeOptionSp());
                                }

                                if (type.equals("cure")) {
                                    InfectStatistic.CommandLine.TypeOptionDto typeOptionDto = new CommandLine.TypeOptionDto();
                                    arrayList.add(typeOptionDto.setTypeOptionCure());
                                }

                                if (type.equals("dead")) {
                                    InfectStatistic.CommandLine.TypeOptionDto typeOptionDto = new CommandLine.TypeOptionDto();
                                    arrayList.add(typeOptionDto.setTypeOptionDead());
                                }
                            }
                        }
                        arguments.setTypeOption(arrayList);
                        break;
                    case "-province":
                        arguments.setProvince(true);
                        ArrayList<String> p_list = new ArrayList<>();
                        for (int j = i+1; j < list.size(); j++) {
                            String province = list.get(j);
                            if (!province.substring(0, 1).equals('-')) {
                                p_list.add(province);
                            }
                        }
                        arguments.setProvince_content(p_list);
                        break;
                }
            }
            CommandLine.setCommand(command);
            CommandLine.setArguments(arguments);
            return CommandLine;
        }
    }

        /**
         * RegexUtil
         * TODO
         *
         * @author 221701412_theTuring
         * @version v 1.0.0
         * @description 正则工具类 用正则表达式用于提取文本
         * 提取类型共八种
         * 1、<省> 新增 感染患者 n人
         * 2、<省> 新增 疑似患者 n人
         * 3、<省1> 感染患者 流入 <省2> n人
         * 4、<省1> 疑似患者 流入 <省2> n人
         * 5、<省> 死亡 n人
         * 6、<省> 治愈 n人
         * 7、<省> 疑似患者 确诊感染 n人
         * 8、<省> 排除 疑似患者 n人
         * @since 2020.2.7
         */
        public static class RegexUtil {

            /**
             * pattern
             * TODO
             * @description 用于封装正则提取的参数的类
             * @author 221701412_theTuring
             * @version v 1.0.0
             * @since 2020.2.7
             */
            static class RegexParameter{

                String pattern1;

                String pattern2;

                int pattern3;

                public String getPattern1() {
                    return pattern1;
                }

                public void setPattern1(String pattern1) {
                    this.pattern1 = pattern1;
                }

                public String getPattern2() {
                    return pattern2;
                }

                public void setPattern2(String pattern2) {
                    this.pattern2 = pattern2;
                }

                public int getPattern3() {
                    return pattern3;
                }

                public void setPattern3(int pattern3) {
                    this.pattern3 = pattern3;
                }
            }

            /**
             * getAddIp(String text)
             * TODO
             * @description 获得类型 1.<省> 新增 感染患者 n人
             * @author 221701412_theTuring
             * @version v 1.0.0
             * @since 2020.2.7
             * @return RegexParameter <省>+n
             */
            public static RegexParameter getAddIp(String text) {
                Pattern pattern1 = Pattern.compile("(.*) 新增");
                Pattern pattern2 = Pattern.compile("感染患者 (.*)人");
                return getString(text, pattern1, pattern2);
            }

            /**
             * getAddSp(String text)
             * TODO
             * @description 获得类型 2、<省> 新增 疑似患者 n人
             * @author 221701412_theTuring
             * @version v 1.0.0
             * @since 2020.2.7
             * @return RegexParameter <省>+n
             */
            public static RegexParameter getAddSp(String text) {
                Pattern pattern1 = Pattern.compile("(.*) 新增");
                Pattern pattern2 = Pattern.compile("疑似患者 (.*)人");
                return getString(text, pattern1, pattern2);
            }

            /**
             * getAddSp(String text)
             * TODO
             * @description 获得类型 3、<省1> 感染患者 流入 <省2> n人
             * @author 221701412_theTuring
             * @version v 1.0.0
             * @since 2020.2.7
             * @return RegexParameter <省1>+n+<省2>
             */
            public static RegexParameter getEmptiesIp(String text) {
                Pattern pattern1 = Pattern.compile("(.*) 感染患者");
                Pattern pattern2 = Pattern.compile("流入 (.*) \\d+人");
                Pattern pattern3 = Pattern.compile("\\W+ (.*)人");
                return getString(text, pattern1, pattern2, pattern3);
            }

            /**
             * getEmptiesSp(String text)
             * TODO
             * @description 获得类型 4、<省1> 疑似患者 流入 <省2> n人
             * @author 221701412_theTuring
             * @version v 1.0.0
             * @since 2020.2.7
             * @return RegexParameter <省1>+n+<省2>
             */
            public static RegexParameter getEmptiesSp(String text) {
                Pattern pattern1 = Pattern.compile("(.*) 疑似患者");
                Pattern pattern2 = Pattern.compile("流入 (.*) \\d+人");
                Pattern pattern3 = Pattern.compile("\\W+ (.*)人");
                return getString(text, pattern1, pattern2, pattern3);
            }


            /**
             * getDead(String text)
             * TODO
             * @description 获得类型 5、<省> 死亡 n人
             * @author 221701412_theTuring
             * @version v 1.0.0
             * @since 2020.2.7
             * @return RegexParameter <省>+n
             */
            public static RegexParameter getDead(String text) {
                Pattern pattern1 = Pattern.compile("(.*) 死亡");
                Pattern pattern2 = Pattern.compile("死亡 (.*)人");
                return getString(text, pattern1, pattern2);
            }

            /**
             * getCure(String text)
             * TODO
             * @description 获得类型 6、<省> 治愈 n人
             * @author 221701412_theTuring
             * @version v 1.0.0
             * @since 2020.2.7
             * @return RegexParameter <省>+n
             */
            public static RegexParameter getCure(String text) {
                Pattern pattern1 = Pattern.compile("(.*) 治愈");
                Pattern pattern2 = Pattern.compile("治愈 (.*)人");
                return getString(text, pattern1, pattern2);
            }

            /**
             * getSpToIp(String text)
             * TODO
             * @description 获得类型 7、<省> 疑似患者 确诊感染 n人
             * @author 221701412_theTuring
             * @version v 1.0.0
             * @since 2020.2.7
             * @return RegexParameter <省>+n
             */
            public static RegexParameter getSpToIp(String text) {
                Pattern pattern1 = Pattern.compile("(.*) 疑似患者");
                Pattern pattern2 = Pattern.compile("确诊感染 (.*)人");
                return getString(text, pattern1, pattern2);
            }

            /**
             * getRemoveSp(String text)
             * TODO
             * @description 获得类型 8、<省> 排除 疑似患者 n人
             * @author 221701412_theTuring
             * @version v 1.0.0
             * @since 2020.2.7
             * @return RegexParameter <省>+n
             */
            public static RegexParameter getRemoveSp(String text) {
                Pattern pattern1 = Pattern.compile("(.*) 排除");
                Pattern pattern2 = Pattern.compile("疑似患者 (.*)人");
                return getString(text, pattern1, pattern2);
            }

            private static RegexParameter getString(String text, Pattern pattern1, Pattern pattern2) {

                //实例封装参数的类
                RegexParameter regexParameter =new RegexParameter();

                Matcher matcher1 = pattern1.matcher(text);
                Matcher matcher2 = pattern2.matcher(text);
                //String str = null;
                if (matcher1.find()) {
                    ///str = matcher1.group(1);
                    regexParameter.setPattern1(matcher1.group(1));
                }
                if (matcher2.find()) {
                    //插入空格方便后续使用
                    //str += " ";
                    ///str += matcher2.group(1);
                    regexParameter.setPattern3(Integer.parseInt(matcher2.group(1)));
                }
                return regexParameter;
            }

            private static RegexParameter getString(String text, Pattern pattern1, Pattern pattern2, Pattern pattern3) {

                //实例封装参数的类
                RegexParameter regexParameter =new RegexParameter();

                Matcher matcher1 = pattern1.matcher(text);
                Matcher matcher2 = pattern2.matcher(text);
                Matcher matcher3 = pattern3.matcher(text);
                //String str = null;
                if (matcher1.find()) {
                    //str = matcher1.group(1);
                    regexParameter.setPattern1(matcher1.group(1));
                }
                if (matcher2.find()) {
                    //插入空格方便后续使用
                    //str += " ";
                    ///str += matcher2.group(1);
                    regexParameter.setPattern2(matcher2.group(1));
                }
                if (matcher3.find()) {
                    //插入空格方便后续使用
                    //str += " ";
                    //str += matcher3.group(1);
                    regexParameter.setPattern3(Integer.parseInt(matcher3.group(1)));
                }
                return regexParameter;
            }


        }

        /**
         * LogUtil
         * TODO
         *
         * @author 221701412_theTuring
         * @version v 1.0.0
         * @description Log文件连接初始化类
         * @since
         */
        static class LogUtil {

            /**
             * readFileByLines(String fileName)
             * TODO
             * @description 以行为单位读取文件内容，一次读一整行以匹配正则
             * @author 221701412_theTuring
             * @version v 1.0.0
             * @since 2020.2.7
             */
            public static void readFileByLines(String fileName) {
                File file = new File(fileName);
                BufferedReader reader = null;
                try {
                    System.out.println("以行为单位读取文件内容，一次读一整行：");
                    reader = new BufferedReader(new FileReader(file));
                    String tempString = null;
                    int line = 1;
                    // 一次读入一行，直到读入null为文件结束
                    while ((tempString = reader.readLine()) != null) {
                        // 显示行号
                        System.out.println("line " + line + ": " + tempString);
                        line++;
                    }
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e1) {
                        }
                    }
                }
            }

        }

        /**
         * LogDao
         * TODO
         *
         * @author 221701412_theTuring
         * @version v 1.0.0
         * @description Log文件控制类
         * @since
         */
        class LogDao {
        }


        /**
         * CommandLine
         * TODO
         *
         * @author 221701412_theTuring
         * @version v 1.0.0
         * @description 命令行启动类
         * @since
         */
        class CommandLineApplication {
        }

        public static void main(String[] args) {

//         CommandLine commandLine = new CommandLine();
//
//         CommandLine.Command command = new CommandLine.Command();
//
//         CommandLine.Arguments arguments = new CommandLine.Arguments();
//
//         command.setList(true);
//
//         arguments.setLog(true);
//         arguments.setLog_content("C");
//         arguments.setOut(true);
//         arguments.setOut_content("D");
//         arguments.setType(true);
//
//         ArrayList<CommandLine.TypeOptionDto> arrayList = new ArrayList<>();
//         CommandLine.TypeOptionDto typeOptionDto = new CommandLine.TypeOptionDto();
//         typeOptionDto.setName(CommandLine.TypeOption.CURE.getName());
//         typeOptionDto.setIndex(CommandLine.TypeOption.CURE.getIndex());
//         typeOptionDto.setStatus(CommandLine.TypeOption.CURE.isStatus());
//
//         arrayList.add(typeOptionDto);
//
//         arguments.setTypeOption(arrayList);
//
//
//         commandLine.setCommand(command);
//
//
//         commandLine.setArguments(arguments);
//
//
//         System.out.println(commandLine.getArguments().getTypeOption().get(0).getName());

            List<String> list = new ArrayList<String>();

            for (String temp : args) {
                list.add(temp);
            }
//
//        for(String temp:list){
//            System.out.print(temp+"\n");
//        }
//
//        for(int i=0; i<list.size(); i++) {
//            String temp = list.get(i);
//            System.out.print(temp+"\n");
//        }
//

//            CommandLineAnalytic commandLineAnalytic = new CommandLineAnalytic();
//            CommandLine commandLine = new CommandLine();
//
//            commandLine = commandLineAnalytic.Analytic(list);
//
//            commandLine.getArguments().isLog();
//            commandLine.getArguments().isOut();
//            commandLine.getArguments().isDate();
//            commandLine.getArguments().isType();
//            commandLine.getArguments().getTypeOption();
//            commandLine.getArguments().isProvince();
//            commandLine.getArguments().getProvince_content();
//
//            System.out.println(commandLine.getArguments().isLog());
//            System.out.println(commandLine.getArguments().isOut());
//            System.out.println(commandLine.getArguments().isDate());
//            System.out.println(commandLine.getArguments().isType());
//            System.out.println(commandLine.getArguments().getTypeOption().get(0).getName());
//            System.out.println(commandLine.getArguments().getTypeOption().get(1).getName());
//            System.out.println(commandLine.getArguments().isProvince());
//            System.out.println(commandLine.getArguments().getProvince_content().get(0));
//            System.out.println(commandLine.getArguments().getProvince_content().get(1));
//            System.out.println(commandLine.getArguments().getProvince_content().get(2));


            LogUtil logUtil = new LogUtil();

            logUtil.readFileByLines(".\\log\\2020-01-22.log.txt");

            RegexUtil regexUtil = new RegexUtil();

            regexUtil.getAddIp("福建 新增 感染患者 2人");

            regexUtil.getEmptiesIp("湖北 感染患者 流入 福建 2人");

            regexUtil.getDead("湖北 死亡 1人");

            regexUtil.getSpToIp("福建 疑似患者 确诊感染 1人");

            regexUtil.getRemoveSp("湖北 排除 疑似患者 2人");

            System.out.println(regexUtil.getAddIp("福建 新增 感染患者 2人").getPattern1());
            System.out.println(regexUtil.getAddIp("福建 新增 感染患者 2人").getPattern3());

            System.out.println(regexUtil.getEmptiesIp("湖北 感染患者 流入 福建 2人").getPattern1());
            System.out.println(regexUtil.getEmptiesIp("湖北 感染患者 流入 福建 2人").getPattern2());
            System.out.println(regexUtil.getEmptiesIp("湖北 感染患者 流入 福建 2人").getPattern3());

            System.out.println(regexUtil.getDead("湖北 死亡 1人").getPattern1());
            System.out.println(regexUtil.getDead("湖北 死亡 1人").getPattern3());

            System.out.println(regexUtil.getRemoveSp("湖北 排除 疑似患者 2人").getPattern1());
            System.out.println(regexUtil.getRemoveSp("湖北 排除 疑似患者 2人").getPattern3());

            System.out.println(regexUtil.getSpToIp("福建 疑似患者 确诊感染 1人").getPattern1());
            System.out.println(regexUtil.getSpToIp("福建 疑似患者 确诊感染 1人").getPattern3());

            System.out.println("hello world");
        }

}