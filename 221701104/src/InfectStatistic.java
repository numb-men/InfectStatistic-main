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
    static Pattern ADD_DOUBTED_PATIENT = Pattern.compile("(\\S+) 新增 疑似患者 (\\d+)人");
    static Pattern MOVE_EXACT_PATIENT = Pattern.compile("(\\S+) 感染患者 流入 (\\S+) (\\d+)人");
    static Pattern MOVE_DOUBTED_PATIENT = Pattern.compile("(\\S+) 疑似患者 流入 (\\S+) (\\d+)人");
    static Pattern CURED_PATIENT = Pattern.compile("(\\S+) 治愈 (\\d+)人");
    static Pattern DEAD_PATIENT = Pattern.compile("(\\S+) 死亡 (\\d+)人");
    static Pattern DOUBTED_TO_EXACT = Pattern.compile("(\\S+) 疑似患者 确诊感染 (\\d+)人");
    static Pattern DOUBTED_TO_NONE = Pattern.compile("(\\S+) 排除 疑似患者 (\\d+)人");
    static int PROVINCE_NUM = 35;
    static String[] PROVINCE = {"全国", "安徽", "澳门" ,"北京", "重庆", "福建","甘肃",
            "广东", "广西", "贵州", "海南", "河北", "河南", "黑龙江", "湖北", "湖南", "吉林",
            "江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海", "山东", "山西", "陕西", "上海",
            "四川", "台湾", "天津", "西藏", "香港", "新疆", "云南", "浙江"};

    /*
    定义各个省的情况类
     */
    class ProvinceStat {
        public  String name;
        private int infected;
        private int doubted;
        private int cured;
        private int dead;

        ProvinceStat(String name)
        {
            this.name = name;
            infected = 0;
            doubted = 0;
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
            return doubted;
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
            doubted += n;
        }

        /*
        用以减少疑似感染人数
         */
        public void decreaseDoubletd(int n)
        {
            doubted -=n;
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
    定义全国情况列表
     */
    class ProvinceList{
        ProvinceStat[] provinceList = new ProvinceStat[PROVINCE_NUM];

        ProvinceList() {
            //初始化全国情况
            for(int i = 0; i < PROVINCE_NUM; i++) {
                provinceList[i] = new ProvinceStat(PROVINCE[i]);
            }
        }

        public void showStat() {
            for(int i = 0; i < PROVINCE_NUM; i++) {
                System.out.println(provinceList[i].name + "感染患者" + provinceList[i].getInfected() + "人 "
                        + "疑似患者" + provinceList[i].getDoublted() + "人 "
                        + "治愈" + provinceList[i].getCured() + "人 "
                        + "死亡" + provinceList[i].getDead() + "人");
            }
        }
    }

    /*
    定义处理文档的抽象类
     */
    public abstract class LogHandler {
        protected LogHandler successor;

        public LogHandler getSuccessor() {
            return successor;
        }

        public void setSuccessor(LogHandler successor){
            this.successor = successor;
        }

        /*
        抽象处理类
         */
        public abstract void handleRequest(String line, ProvinceList provinceList);
    }

    /*
    处理感染病人类
     */
    public class AddExactHandler extends LogHandler {
        @Override
        public void handleRequest(String line, ProvinceList provinceList) {
            Matcher m1 = ADD_EXACT_PATIENT.matcher(line);
            if (m1.matches()) {
                dealAddExact(line, provinceList);
            } else {
                getSuccessor().handleRequest(line, provinceList);
            }
        }
    }

    /*
    处理疑似病人类
     */
    public class AddDoubtedHandler extends LogHandler {
        @Override
        public void handleRequest(String line, ProvinceList provinceList) {
            Matcher m1 = ADD_DOUBTED_PATIENT.matcher(line);
            if (m1.matches()) {
                dealAddDoubted(line, provinceList);
            } else {
                getSuccessor().handleRequest(line, provinceList);
            }
        }
    }

    /*
    处理感染患者流入类
     */
    public class MoveExactHandler extends LogHandler {
        @Override
        public void handleRequest(String line, ProvinceList provinceList) {
            Matcher m1 = MOVE_EXACT_PATIENT.matcher(line);
            if (m1.matches()) {
                dealMoveExact(line, provinceList);
            } else {
                getSuccessor().handleRequest(line, provinceList);
            }
        }
    }

    /*
    处理疑似患者流入类
     */
    public class MoveDoubtedHandler extends LogHandler {
        @Override
        public void handleRequest(String line, ProvinceList provinceList) {
            Matcher m1 = MOVE_DOUBTED_PATIENT.matcher(line);
            if (m1.matches()) {
                dealMoveDoubted(line, provinceList);
            } else {
                getSuccessor().handleRequest(line, provinceList);
            }
        }
    }

    /*
    处理治愈病人类
     */
    public class CureHandler extends LogHandler {
        @Override
        public void handleRequest(String line, ProvinceList provinceList) {
            Matcher m1 = CURED_PATIENT.matcher(line);
            if (m1.matches()) {
                dealCure(line, provinceList);
            } else {
                getSuccessor().handleRequest(line, provinceList);
            }
        }
    }

    /*
    处理死亡病人类
     */
    public class DeadHandler extends LogHandler {
        @Override
        public void handleRequest(String line, ProvinceList provinceList) {
            Matcher m1 = DEAD_PATIENT.matcher(line);
            if (m1.matches()) {
                dealDead(line, provinceList);
            } else {
                getSuccessor().handleRequest(line, provinceList);
            }
        }
    }

    /*
    处理疑似患者确诊类
     */
    public class Doubted2ExactHandler extends LogHandler {
        @Override
        public void handleRequest(String line, ProvinceList provinceList) {
            Matcher m1 = DOUBTED_TO_EXACT.matcher(line);
            if (m1.matches()) {
                dealDoubted2Exact(line, provinceList);
            } else {
                getSuccessor().handleRequest(line, provinceList);
            }
        }
    }

    /*
    处理疑似患者排除类
     */
    public class Doubted2NoneHandler extends LogHandler {
        @Override
        public void handleRequest(String line, ProvinceList provinceList) {
            Matcher m1 = DOUBTED_TO_NONE.matcher(line);
            if (m1.matches()) {
                dealDoubted2None(line, provinceList);
            } else {
                getSuccessor().handleRequest(line, provinceList);
            }
        }
    }

    /*
    处理文档类，将责任链包括其中。
     */
    public class DealLog {
        public void execute(String line, ProvinceList provinceList) {
            LogHandler addExactPatient = new AddExactHandler();
            LogHandler addDoubtedPatient = new AddDoubtedHandler();
            LogHandler moveExactPatient = new MoveExactHandler();
            LogHandler moveDoubtedPatient = new MoveDoubtedHandler();
            LogHandler curePatient = new CureHandler();
            LogHandler deadPatient = new DeadHandler();
            LogHandler doubted2Exact = new Doubted2ExactHandler();
            LogHandler doubted2None = new Doubted2NoneHandler();

            //串联责任链
            addExactPatient.setSuccessor(addDoubtedPatient);
            addDoubtedPatient.setSuccessor(moveExactPatient);
            moveExactPatient.setSuccessor(moveDoubtedPatient);
            moveDoubtedPatient.setSuccessor(curePatient);
            curePatient.setSuccessor(deadPatient);
            deadPatient.setSuccessor(doubted2Exact);
            doubted2Exact.setSuccessor(doubted2None);

            //自动调用责任链
            addExactPatient.handleRequest(line, provinceList);
        }
    }

    /*
    对符合增加感染人数的行作数据处理
    @param 日志行
     */
    public void dealAddExact(String line, ProvinceList pl) {
        //将字符串以空格分割为多个字符串
        String[] strArray = line.split(" ");
        int n = Integer.parseInt(strArray[3].replace("人", ""));

        for(int i = 0; i < PROVINCE_NUM; i++)
        {
            if(strArray[0].equals(PROVINCE[i]))
            {
                //全国感染人数增加
                pl.provinceList[0].addInfected(n);
                //当地区感染人数增加
                pl.provinceList[i].addInfected(n);
                break;
            }
        }
    }

    /*
   对符合增加疑似感染人数的行作数据处理
   @param line:日志行
   @param pl:省份列表
    */
    public void dealAddDoubted(String line, ProvinceList pl) {
        //将字符串以空格分割为多个字符串
        String[] strArray = line.split(" ");
        int n = Integer.parseInt(strArray[3].replace("人", ""));

        for(int i = 0; i < PROVINCE_NUM; i++)
        {
            if(strArray[0].equals(PROVINCE[i]))
            {
                //全国疑似感染人数增加
                pl.provinceList[0].addDoublted(n);
                //当地区疑似感染人数增加
                pl.provinceList[i].addDoublted(n);
                break;
            }
        }
    }

    /*
   对符合流动感染人数的行作数据处理
   @param 日志行
   @param pl:省份列表
    */
    public void dealMoveExact(String line, ProvinceList pl) {
        //将字符串以空格分割为多个字符串
        String[] strArray = line.split(" ");
        int n = Integer.parseInt(strArray[4].replace("人", ""));

        for(int i = 0; i < PROVINCE_NUM; i++)
        {
            if(strArray[0].equals(PROVINCE[i]))
            {
                //流出地感染人数减少
                pl.provinceList[i].decreaseInfected(n);
                break;
            }
        }
        for(int i = 0; i < PROVINCE_NUM; i++)
        {
            if(strArray[3].equals(PROVINCE[i]))
            {
                //流入地感染人数增加
                pl.provinceList[i].addInfected(n);
                break;
            }
        }
    }

    /*
  对符合流动疑似感染人数的行作数据处理
  @param 日志行
  @param pl:省份列表
   */
    public void dealMoveDoubted(String line, ProvinceList pl) {
        //将字符串以空格分割为多个字符串
        String[] strArray = line.split(" ");
        int n = Integer.parseInt(strArray[4].replace("人", ""));

        for(int i = 0; i < PROVINCE_NUM; i++)
        {
            if(strArray[0].equals(PROVINCE[i]))
            {
                //流出地疑似感染人数减少
                pl.provinceList[i].decreaseDoubletd(n);
                break;
            }
        }
        for(int i = 0; i < PROVINCE_NUM; i++)
        {
            if(strArray[3].equals(PROVINCE[i]))
            {
                //流入地疑似感染人数增加
                pl.provinceList[i].addDoublted(n);
                break;
            }
        }
    }

    /*
  对符合治愈人数的行作数据处理
  @param line:日志行
  @param pl:省份列表
   */
    public void dealCure(String line, ProvinceList pl) {
        //将字符串以空格分割为多个字符串
        String[] strArray = line.split(" ");
        int n = Integer.parseInt(strArray[2].replace("人", ""));

        for(int i = 0; i < PROVINCE_NUM; i++)
        {
            if(strArray[0].equals(PROVINCE[i]))
            {
                //全国治愈人数增加
                pl.provinceList[0].addCured(n);
                //全国感染人数减少
                pl.provinceList[0].decreaseInfected(n);
                //当地区治愈人数增加
                pl.provinceList[i].addCured(n);
                //当地区感染人数减少
                pl.provinceList[i].decreaseInfected(n);
                break;
            }
        }
    }

    /*
  对符合死亡人数的行作数据处理
  @param line:日志行
  @param pl:省份列表
   */
    public void dealDead(String line, ProvinceList pl) {
        //将字符串以空格分割为多个字符串
        String[] strArray = line.split(" ");
        int n = Integer.parseInt(strArray[2].replace("人", ""));

        for(int i = 0; i < PROVINCE_NUM; i++)
        {
            if(strArray[0].equals(PROVINCE[i]))
            {
                //全国死亡人数增加
                pl.provinceList[0].addDead(n);
                //全国感染人数减少
                pl.provinceList[0].decreaseInfected(n);
                //当地区死亡人数增加
                pl.provinceList[i].addDead(n);
                //当地区感染人数减少
                pl.provinceList[i].decreaseInfected(n);
                break;
            }
        }
    }

    /*
  对符合确诊人数的行作数据处理
  @param line:日志行
  @param pl:省份列表
   */
    public void dealDoubted2Exact(String line, ProvinceList pl) {
        //将字符串以空格分割为多个字符串
        String[] strArray = line.split(" ");
        int n = Integer.parseInt(strArray[3].replace("人", ""));

        for(int i = 0; i < PROVINCE_NUM; i++)
        {
            if(strArray[0].equals(PROVINCE[i]))
            {
                //全国感染人数增加
                pl.provinceList[0].addInfected(n);
                //全国疑似感染人数减少
                pl.provinceList[0].decreaseDoubletd(n);
                //当地区感染人数增加
                pl.provinceList[i].addInfected(n);
                //当地区疑似感染人数减少
                pl.provinceList[i].decreaseDoubletd(n);
                break;
            }
        }
    }

    /*
  对符合排除人数的行作数据处理
  @param line:日志行
  @param pl:省份列表
   */
    public void dealDoubted2None(String line, ProvinceList pl) {
        //将字符串以空格分割为多个字符串
        String[] strArray = line.split(" ");
        int n = Integer.parseInt(strArray[3].replace("人", ""));

        for(int i = 0; i < PROVINCE_NUM; i++)
        {
            if(strArray[0].equals(PROVINCE[i]))
            {
                //全国疑似感染人数减少
                pl.provinceList[0].decreaseDoubletd(n);
                //当地区疑似感染人数减少
                pl.provinceList[i].decreaseDoubletd(n);
                break;
            }
        }
    }

    public static void main(String[] args) {
        String line = "福建 新增 感染患者 2人";
        InfectStatistic infectStatistic = new InfectStatistic();
        InfectStatistic.DealLog dealLog = infectStatistic.new DealLog();
        InfectStatistic.ProvinceList provinceList = infectStatistic.new ProvinceList();

        dealLog.execute(line, provinceList);
        provinceList.showStat();

        //handleLine(line);
    }
}
