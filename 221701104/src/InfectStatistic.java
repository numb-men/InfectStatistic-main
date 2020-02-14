import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * InfectStatistic
 * TODO
 *
 * @author pcy
 * @version 1.0
 * @since xxx
 */
class InfectStatistic {
    /*定义全局变量文本匹配模式*/
    static Pattern ADD_EXACT_PATIENT = Pattern.compile("(\\S+) 新增 感染患者 (\\d+)人");
    static Pattern ADD_DOUBLTED_PATIENT = Pattern.compile("(\\S+) 新增 疑似患者 (\\d+)人");
    static Pattern MOVE_EXACT_PATIENT = Pattern.compile("(\\S+) 感染患者 流入 (\\S+) (\\d+)人");
    static Pattern MOVE_DOUBLTED_PATIENT = Pattern.compile("(\\S+) 疑似患者 流入 (\\S+) (\\d+)人");
    static Pattern CURED_PATIENT = Pattern.compile("(\\S+) 治愈 (\\d+)人");
    static Pattern DEAD_PATIENT = Pattern.compile("(\\S+) 死亡 (\\d+)人");
    static Pattern DOUBLTED_TO_EXACT = Pattern.compile("(\\S+) 疑似患者 确诊感染 (\\d+)人");
    static Pattern DOUBLTED_TO_NONE = Pattern.compile("(\\S+) 排除 疑似患者 (\\d+)人");

    class Statistic{
        private int infected;
        private int doublted;
        private int cured;
        private int dead;

        Statistic()
        {
            infected = 0;
            doublted = 0;
            cured = 0;
            dead = 0;
        }

        /*
        用以获取受感染人数
         */
        public int getInfected()
        {
            return infected;
        }

        /*
        用以获得疑似感染人数
         */
        public int getDoublted()
        {
            return doublted;
        }

        /*
        用以获得治愈人数
         */
        public int getCured()
        {
            return cured;
        }

        /*
        用以获得死亡人数
         */
        public int getDead()
        {
            return dead;
        }

        /*
        用以增加感染人数
         */
        public void addInfected(int n)
        {
            infected += n;
        }

        /*
        用以减少感染人数
         */
        public void decreaseInfected(int n)
        {
            infected -= n;
        }

        /*
        用以增加疑似感染人数
         */
        public void addDoublted(int n)
        {
            doublted += n;
        }

        /*
        用以减少疑似感染人数
         */
        public void decreaseDoubletd(int n)
        {
            doublted -=n;
        }

        /*
        用以增加治愈人数
         */
        public void addCured(int n)
        {
            cured += n;
        }

        /*
        用以增加死亡人数
         */
        public void addDead(int n)
        {
            dead += n;
        }
    }

    /*
    用以进行每行文本的处理。
    接受参数：String的一行文本
    返回值：空
     */
    public static void handleLine(String line)
    {
        Matcher m1 = ADD_EXACT_PATIENT.matcher(line);
        if(m1.matches())
        {
            System.out.println("匹配成功");
        }
    }

    public static void main(String[] args) {
        String line = "福建 新增 感染患者 2人";
        handleLine(line);
    }
}
