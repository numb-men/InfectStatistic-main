import java.util.ArrayList;
import java.util.List;


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
     * @description 整条命令行对应的实体类
     * eg $ java InfectStatistic list -date 2020-01-22 -log D:/log/ -out D:/output.txt
     * 命令行分为 命令+命令行参数
     * 命令 list
     * 命令行参数 -log -out
     * @author 221701412_theTuring
     * @version v 1.0.0
     * @since 2020.2.7
     */
    static class CommandLine{

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
         * @description 命令
         * 现有命令 list
         * @author 221701412_theTuring
         * @version v 1.0.0
         * @since 2020.2.7
         */
        static class Command{

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
         * @description 命令行参数对应的实体类
         * 现有命令行参数 log out date type province
         * -log 指定日志目录的位置，该项必会附带，请直接使用传入的路径，而不是自己设置路径
         * -out 指定输出文件路径和文件名，该项必会附带，请直接使用传入的路径，而不是自己设置路径
         * -date 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期之前的所有log文件
         * -type 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，
         * 使用缩写选择，如 -type ip 表示只列出感染患者的情况，-type sp cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。
         * -province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江
         * @author 221701412_theTuring
         * @version v 1.0.0
         * @since 2020.2.7
         */
        static class Arguments{

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
         * @description 命令行参数中 type 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]
         * 使用的枚举类型
         * @author 221701412_theTuring
         * @version v 1.0.0
         * @since 2020.2.7
         */
        enum TypeOption{

            IP("infection_patients",1,false),SP("suspected patients",2,false),
            CURE("cure_patients",3,false),DEAD("dead__patient",4,false);

            //类型名
            private String name;
            //索引
            private int index;
            //状态 false：未选中 true：选中
            private boolean status;

            private TypeOption(String name, int index, boolean status){

                this.name = name ;
                this.index = index ;
                this.status = status ;

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
        }

        /**
         * TypeOptionDto
         * TODO
         * @description 封装枚举类型的类
         * @author 221701412_theTuring
         * @version v 1.0.0
         * @since 2020.2.7
         */
        static class TypeOptionDto{

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
        }


    }

    /**
     * CommandLineAnalytic
     * TODO
     * @description 命令行解析类
     * @author 221701412_theTuring
     * @version v 1.0.0
     * @since
     */
    class CommandLineAnalytic{
    }


    /**
     * LogUtil
     * TODO
     * @description Log文件连接初始化类
     * @author 221701412_theTuring
     * @version v 1.0.0
     * @since
     */
    class LogUtil{
    }

    /**
     * LogDao
     * TODO
     * @description Log文件控制类
     * @author 221701412_theTuring
     * @version v 1.0.0
     * @since
     */
    class LogDao{
    }


    /**
     * CommandLine
     * TODO
     * @description 命令行启动类
     * @author 221701412_theTuring
     * @version v 1.0.0
     * @since
     */
    class CommandLineApplication{
    }

    public static void main(String[] args) {

         CommandLine commandLine = new CommandLine();


         CommandLine.Command command = new CommandLine.Command();


         CommandLine.Arguments arguments = new CommandLine.Arguments();


         command.setList(true);


         arguments.setLog(true);
         arguments.setLog_content("C");
         arguments.setOut(true);
         arguments.setOut_content("D");
         arguments.setType(true);

         ArrayList<CommandLine.TypeOptionDto> arrayList = new ArrayList<>();
         CommandLine.TypeOptionDto typeOptionDto = new CommandLine.TypeOptionDto();
         typeOptionDto.setName(CommandLine.TypeOption.CURE.getName());
         typeOptionDto.setIndex(CommandLine.TypeOption.CURE.getIndex());
         typeOptionDto.setStatus(CommandLine.TypeOption.CURE.isStatus());

         arrayList.add(typeOptionDto);

         arguments.setTypeOption(arrayList);


         commandLine.setCommand(command);


         commandLine.setArguments(arguments);


         System.out.println(commandLine.getArguments().getTypeOption().get(0).getName());

         System.out.println("hello world");
    }
}
