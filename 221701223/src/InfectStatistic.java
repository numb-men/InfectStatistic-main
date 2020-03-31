/**
 * InfectStatistic
 *
 * @author ybn
 */
public class InfectStatistic {
    /**
     * Main
     * 程序主入口
     *
     * @param args args
     */
    public static void main(String[] args) {
        try {
            ArgumentContainer argumentContainer = ArgumentHandler.getArgumentContainer(args);
            RecordContainer recordContainer = new RecordContainer(argumentContainer) {{
                init();
            }};
            FileTools fileTools = new FileTools(argumentContainer);
            fileTools.readFile(recordContainer);
            fileTools.createOutputFile(recordContainer.newOutputDataSet());
        } catch (ArgumentException ae) {
            ae.printStackTrace();
        }
    }

    /**
     * Test main
     */
//    @Test
//    public void testMain() {
//        ArrayList<String[]> argList = new ArrayList<>() {{
//            //指定日期
//            add(new String[]{"list", "-date", "2020-01-27", "-log", "/Users/ybn/IdeaProjects/InfectStatistic-main" +
//                "/221701223/log/", "-out", "/Users/ybn/Downloads/out1.txt"});
//            //指定省份
//            add(new String[]{"list", "-log", "/Users/ybn/IdeaProjects/InfectStatistic-main/221701223/log/", "-province",
//                "福建", "湖北", "江西", "-out", "/Users/ybn/Downloads/out2.txt"});
//            //指定类型
//            add(new String[]{"list", "-log", "/Users/ybn/IdeaProjects/InfectStatistic-main/221701223/log/", "-type",
//                "ip", "dead", "-out", "/Users/ybn/Downloads/out3.txt"});
//            //指定省份和类型
//            add(new String[]{"list", "-log", "/Users/ybn/IdeaProjects/InfectStatistic-main/221701223/log/", "-type",
//                "ip", "dead", "-province", "湖北", "-out", "/Users/ybn/Downloads/out4.txt"});
//            //指定日期、省份和类型
//            add(new String[]{"list", "-log", "/Users/ybn/IdeaProjects/InfectStatistic-main/221701223/log/", "-date",
//                "2020-01-27", "-province", "福建", "-type", "sp", "-out", "/Users/ybn/Downloads/out5.txt"});
//            //缺少list
//            add(new String[]{"-log", "~/IdeaProjects/InfectStatistic-main/221701223/log/", "out",
//                "/Users/ybn/Downloads/out6.txt"});
//            //日期超出范围
//            add(new String[]{"list", "-log", "/Users/ybn/IdeaProjects/InfectStatistic-main/221701223/log/", "-date",
//                "2020-01-28", "-out", "/Users/ybn/Downloads/out7.txt"});
//            //带-type却未指定type
//            add(new String[]{"list", "-log", "/Users/ybn/IdeaProjects/InfectStatistic-main/221701223/log/", "-type",
//                "-out", "/Users/ybn/Downloads/out8.txt"});
//            //带-province却未指定province
//            add(new String[]{"list", "-log", "/Users/ybn/IdeaProjects/InfectStatistic-main/221701223/log/",
//                "-province", "-out", "/Users/ybn/Downloads/out8.txt"});
//            //log文件格式有误
//            add(new String[]{"list", "-log", "/Users/ybn/Downloads/log/", "-out", "/Users/ybn/Downloads/out9.txt"});
//            //缺少-log
//            add(new String[]{"list", "-province", "福建", "湖北", "江西", "-out", "/Users/ybn/Downloads/out2.txt"});
//            //缺少-out
//            add(new String[]{"list", "-log", "/Users/ybn/IdeaProjects/InfectStatistic-main/221701223/log/", "-province",
//                "福建", "湖北"});
//        }};
//        for (String[] args : argList) {
//            InfectStatistic.main(args);
//        }
//    }
}
