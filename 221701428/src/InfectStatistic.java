/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version 1.0
 */
class InfectStatistic {

    /** 保存args的值 */
    public static String[] paramenterStrings;
    /** index为参数名在哈希表中的位置，值为参数名在paramenterStrings中的下标，不存在参数名则为-1 */
    public static int[]  indexOfParamenterStrings = {-1, -1, -1, -1, -1, -1};
    /** log 日志文件目录,项目必会附带 */
    public static String inputDir = "";
    /** 统计到哪一天 */
    public static String toDateString = "";
    /** 输出路径&文件名 */
    public static String outputFileNameString = "";
    /** type的参数值 */
    public static String[] paramentersOfType = new String[10];
    /** province的参数值 */
    public static String[] paramentersOfProvince = new String[25];
    /** 用来存储省份的哈希表 */
    public static Hashtable<String, Province> hashtable = new Hashtable<String, Province>(40);

    public static void main(String[] args) {
        //...
    }
}
