import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * InfectStatistic TODO
 *
 * @author 181700141_呼叫哆啦A梦
 * @version xxx
 * @since xxx
 */
class InfectStatistic {
    public static void main(String[] args) {
        int length = args.length;
        if (length == 0)
            return;
        else if (args[0].equals("list")) {
            ListCommand command = new ListCommand();
            try {
                command.dealParameter(args);
                command.carryOutActions();
            } catch (IllegalException e) {
                System.out.println(e);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
            }

        } else {
            System.out.println("不存在" + args[0] + "指令");
        }

    }
}

//当参数或参数值非法时将抛出该异常类
class IllegalException extends Exception {
    // 记录错误原因
    private String message;

    public IllegalException(String tMessage) {
        message = tMessage;
    }

    public String toString() {
        return message;
    }
}

/**
 * 
 * @author 181700141_呼叫哆啦A梦
 * 
 *         处理list命令
 * 
 *         -log 指定日志目录的位置，该项必会附带，请直接使用传入的路径，而不是自己设置路径 。
 * 
 *         -out 指定输出文件路径和文件名，该项必会附带，请直接使用传入的路径，而不是自己设置路径 。
 * 
 *         -date 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期之前的所有log文件。
 * 
 *         -type 可选择[ip： infection patients 感染患者，sp： suspected patients
 *         疑似患者，cure：治愈 ，dead：死亡患者]， 使用缩写选择，如 -type ip 表示只列出感染患者的情况，-type sp
 *         cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况， 不指定该项默认会列出所有情况。
 * 
 *         -province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江。
 */

class ListCommand {
    // 存储各省及全国感染患者人数
    private LinkedHashMap<String, Integer> ipMap = new LinkedHashMap<>();
    // 存储各省及全国疑似患者人数
    private LinkedHashMap<String, Integer> spMap = new LinkedHashMap<>();
    // 存储各省及全国治愈患者人数
    private LinkedHashMap<String, Integer> cureMap = new LinkedHashMap<>();
    // 存储各省及全国死亡人数
    private LinkedHashMap<String, Integer> deadMap = new LinkedHashMap<>();

    // 记录日志目录路径
    private String inDirectory = null;
    // 记录输出目录路径
    private String outDirectory = null;
    // 记录日期：date的参数值
    private String date = null;
    // 记录type的值，如果集合为空，默认列出全部情况
    private List<String> types = new ArrayList<String>();
    // 记录要列出的省
    private List<String> provinces = new ArrayList<String>();

    // 记录是否有输入相应的参数
    private boolean dateIsExist = false;
    private boolean typeIsExist = false;
    private boolean provinceIsExist = false;

    // 定义正则表达式，表达式内的空格不可随意修该，否则会影响读取处理
    String s1 = "\\s*\\S+ 新增 感染患者 \\d+人\\s*";
    String s2 = "\\s*\\S+ 新增 疑似患者 \\d+人\\s*";
    String s3 = "\\s*\\S+ 感染患者 流入 \\S+ \\d+人\\s*";
    String s4 = "\\s*\\S+ 疑似患者 流入 \\S+ \\d+人\\s*";
    String s5 = "\\s*\\S+ 死亡 \\d+人\\s*";
    String s6 = "\\s*\\S+ 治愈 \\d+人\\s*";
    String s7 = "\\s*\\S+ 疑似患者 确诊感染 \\d+人\\s*";
    String s8 = "\\s*\\S+ 排除 疑似患者 \\d+人\\s*";

    private String[] provincesArray = { "全国", "安徽", "北京", "重庆", "福建", "甘肃", "广东", "广西", "贵州", "海南", "河北", "河南", "黑龙江",
            "湖北", "湖南", "吉林", "江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海", "山东", "山西", "陕西", "上海", "四川", "天津", "西藏", "新疆", "云南",
            "浙江" };

    public ListCommand() {
        // 有顺序插入
        for (int i = 0; i < provincesArray.length; i++) {
            ipMap.put(provincesArray[i], 0);
            spMap.put(provincesArray[i], 0);
            cureMap.put(provincesArray[i], 0);
            deadMap.put(provincesArray[i], 0);
        }
    }

    /**
     * 处理list命令的各参数 。
     * 
     * 要求-log和-out参数都必须存在且参数值有且仅有一个 。
     * -date参数值可以没有但如果有提供需满足XXXX-XX-XX(X=0-9)格式，且仅有一个。
     * -type参数值可以没有但如果提供则应为（ip,sp,cure,dead）中一个或多个但不能重复，如果没有提供最后会将成员变量types修改含全部即ip，sp，cure，dead
     * -province参数如果提供则要求一定提供参数值，参数值要求合法即存在该省份（可以为全国），同样不允许重复输入相同参数值
     * 
     * @param 用户输入的命令，含list
     * @throws 如果初步解析list命令如：list命令未提供该参数的执行方法 同一参数出现多次，必须存在的参数不存在时将抛出IllegalException
     */
    public void dealParameter(String[] args) throws IllegalException {
        int l = args.length;
        // 存储合法的省份，用于检测用户输入的省份是否合法
        List<String> legalProvinces = Arrays.asList(provincesArray);
        // 存储合法的type值
        List<String> legalTypes = new ArrayList<String>();
        legalTypes.add("ip");
        legalTypes.add("sp");
        legalTypes.add("cure");
        legalTypes.add("dead");

        for (int i = 1; i < l; i++) {
            switch (args[i]) {
            case "-log":
                if (inDirectory != null)
                    throw new IllegalException("错误，重复出现 -log参数");
                if (i == l - 1 || args[i + 1].charAt(0) == '-')
                    throw new IllegalException("错误，未提供-log参数值");
                // 给日志目录路径赋值
                inDirectory = args[++i];
                break;
            case "-out":
                if (outDirectory != null)
                    throw new IllegalException("错误，重复出现-out参数");
                if (i == l - 1 || args[i + 1].charAt(0) == '-')
                    throw new IllegalException("错误，未提供-out参数值");
                // 给输出目录路径赋值
                outDirectory = args[++i];
                break;
            case "-date":
                if (dateIsExist)
                    throw new IllegalException("错误，重复出现-date参数");
                if (i < l - 1 && args[i + 1].charAt(0) != '-')
                    date = args[++i];
                if (date != null && !date.matches("\\d{4}-\\d{2}-\\d{2}"))
                    throw new IllegalException("错误，-date参数值非法，须符合XXXX-XX-XX格式（X为0-9）");
                dateIsExist = true;
                break;
            case "-type":
                if (typeIsExist)
                    throw new IllegalException("错误，重复出现-type参数");
                for (int j = i + 1; j < l; j++) {
                    if (args[j].charAt(0) == '-')
                        break;
                    if (types.contains(args[j]))
                        throw new IllegalException("错误，参数-type出现重复参数值");
                    if (!legalTypes.contains(args[j]))
                        throw new IllegalException("错误，参数-type的参数值非法，应为（ip,sp,cure,dead）");
                    i = j;
                    types.add(args[j]);
                }
                typeIsExist = true;
                break;
            case "-province":
                if (provinceIsExist)
                    throw new IllegalException("错误，重复出现-province参数");
                for (int j = i + 1; j < l; j++) {
                    if (args[j].charAt(0) == '-')
                        break;
                    if (provinces.contains(args[j]))
                        throw new IllegalException("错误，参数-province出现重复参数值");
                    if (!legalProvinces.contains(args[j]))
                        throw new IllegalException("错误，参数-province出现的参数值非法（省份非法）");
                    i = j;
                    provinces.add(args[j]);
                }
                provinceIsExist = true;
                break;
            default:
                if (args[i].charAt(0) == '-')
                    throw new IllegalException("错误，list命令不支持" + args[i] + "参数");
                throw new IllegalException("错误，参数值" + args[i] + "非法");
            }// end of switch
        } // end of for
        if (outDirectory == null || inDirectory == null)
            throw new IllegalException("错误，参数-log及-out要求必须存在");
        if (provinceIsExist && provinces.isEmpty())
            throw new IllegalException("错误，未提供参数-province的参数值");
        // 如果有输入province参数则排序
        if (provinceIsExist)
            provinces = sort();
        // 如果有输入-type参数，但未输入参数值
        if (types.isEmpty()) {
            types.add("ip");
            types.add("sp");
            types.add("cure");
            types.add("dead");
        }
    }

    /**
     * 对用户输入的省份进行排序，如果有全国则其在首位，其余根据拼音排序。要求要有输入省份本函数不进行相应检查
     * 
     * @return 排好序的list
     */
    private List<String> sort() {
        List<String> list = new ArrayList<String>();
        int size = provincesArray.length;
        for (int i = 0; i < size; i++)
            if (provinces.contains(provincesArray[i]))
                list.add(provincesArray[i]);
        return list;
    }

    public List<String> getInputProvinces() {
        return provinces;
    }

    // 执行各参数所要求的操作
    public void carryOutActions() throws Exception {

        File file = new File(inDirectory);
        if (!file.exists() || !file.isDirectory())
            throw new IllegalException("错误，日志目录" + inDirectory + "不存在");
        handleFiles(file);

    }

    // 读取目录下的日志文件
    public void handleFiles(File file) throws Exception {
        // 获得目录下全部文件列表
        String[] logFiles = file.list();
        int l = logFiles.length;
        // 提取出符合XXXX-XX-XX.log.txt命名要求的日志文件
        List<String> legalFiles = new ArrayList<String>();
        for (int i = 0; i < l; i++) {
            if (logFiles[i].matches("\\d{4}-\\d{2}-\\d{2}\\.log\\.txt"))
                legalFiles.add(logFiles[i]);
        }

        // 将符合要求的日志文件列表存入logFiles数组中
        l = legalFiles.size();
        if (l == 0)
            throw new IllegalException("错误，该日志目录下不存在合法的日志文件");
        logFiles = new String[l];
        legalFiles.toArray(logFiles);
        Arrays.sort(logFiles);

        if (date != null && date.compareTo(logFiles[l - 1]) > 0)
            throw new IllegalException("错误，日期非法，超出日志最晚时间");
        if (date == null)
            date = logFiles[l - 1];
        else
            date = date + ".log.txt";
        // 读取满足要求的日志文件并进行数据更新
        for (int i = 0; i < l; i++) {
            if (date.compareTo(logFiles[i]) >= 0) {
                handleFile(inDirectory + "/" + logFiles[i]);
            }
        }

        // 统计全国数据
        int ipAmount = 0;
        int spAmount = 0;
        int cureAmount = 0;
        int deadAmount = 0;
        for (Integer i : ipMap.values())
            ipAmount += i;
        ipMap.put("全国", ipAmount);
        for (Integer i : spMap.values())
            spAmount += i;
        spMap.put("全国", spAmount);
        for (Integer i : cureMap.values())
            cureAmount += i;
        cureMap.put("全国", cureAmount);
        for (Integer i : deadMap.values())
            deadAmount += i;
        deadMap.put("全国", deadAmount);

        File outFile = new File(outDirectory);
        FileWriter writer = new FileWriter(outFile, false);
        if (typeIsExist) {
            if (!provinceIsExist) {
                // 未提供要输出的省份，则输出日志文件有提供的省份的数据，
                List<String> list = new ArrayList<String>();
                int size = provincesArray.length;
                for (int i = 0; i < size; i++) {
                    // 全是0则不输出其数据
                    if (ipMap.get(provincesArray[i]) == 0 && spMap.get(provincesArray[i]) == 0
                            && cureMap.get(provincesArray[i]) == 0 && deadMap.get(provincesArray[i]) == 0)
                        continue;
                    else
                        list.add(provincesArray[i]);
                }
                out(writer, list);
            } else {
                out(writer, provinces);
            }
        } else {
            if (provinceIsExist) {
                for (String province : provinces) {
                    writer.write(province + " 感染患者" + ipMap.get(province) + "人 疑似患者" + spMap.get(province) + "人 治愈"
                            + cureMap.get(province) + "人 死亡" + deadMap.get(province) + "人\n");
                }

            } else {

                // 将各省数据填入相应状态数组内，由于前面集合是有顺序插入所以下面的数据也是有顺序
                Integer[] ipProvincesAmount = new Integer[ipMap.size()];
                ipMap.values().toArray(ipProvincesAmount);
                Integer[] spProvincesAmount = new Integer[spMap.size()];
                spMap.values().toArray(spProvincesAmount);
                Integer[] cureProvincesAmount = new Integer[cureMap.size()];
                cureMap.values().toArray(cureProvincesAmount);
                Integer[] deadProvincesAmount = new Integer[deadMap.size()];
                deadMap.values().toArray(deadProvincesAmount);

                // 将数据填入文件(由于未提供要输出的省故全部为0将不输入)
                int size = provincesArray.length;
                for (int i = 0; i < size; i++) {
                    if (ipProvincesAmount[i] == 0 && spProvincesAmount[i] == 0 && cureProvincesAmount[i] == 0
                            && deadProvincesAmount[i] == 0)
                        continue;
                    else
                        writer.write(
                                provincesArray[i] + " 感染患者" + ipProvincesAmount[i] + "人 疑似患者" + spProvincesAmount[i]
                                        + "人 治愈" + cureProvincesAmount[i] + "人 死亡" + deadProvincesAmount[i] + "人\n");
                }

            }
        }
        writer.write("// 该文档并非真实数据，仅供测试使用");
        writer.close();

    }

    /**
     * 读取日志文件，更新相应人数
     * 
     * @param route 待读取日志文件路径
     * @throws Exception
     */
    public void handleFile(String route) throws Exception {
        FileInputStream fstream = new FileInputStream(new File(route));
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String strLine;
        while ((strLine = br.readLine()) != null) {
            if (strLine.matches(s1)) {
                int index = strLine.indexOf(" 新增 感染患者");
                // 取出省份，省份前面可能有空格
                String province = strLine.substring(0, index);
                // 去掉全部空格
                province.replace(" ", "");

                // 取出感染人数
                int sum = getAmount(strLine);
                // 修改人数
                sum += ipMap.get(province);
                ipMap.put(province, sum);
            } else if (strLine.matches(s2)) {
                int index = strLine.indexOf(" 新增 疑似患者");
                // 取出省份，省份前面可能有空格
                String province = strLine.substring(0, index);
                // 去掉全部空格
                province.replace(" ", "");

                // 取出疑似患者人数
                int sum = getAmount(strLine);
                // 修改人数
                sum += spMap.get(province);
                spMap.put(province, sum);
            } else if (strLine.matches(s3)) {
                // String s3 = "\\s*\\S+ 感染患者 流入 \\S+ \\d+人\\s*";
                int index = strLine.indexOf(" 感染患者 流入");
                // 取出流出省份，省份前面可能有空格
                String outProvince = strLine.substring(0, index);
                // 去掉全部空格
                outProvince.replace(" ", "");

                // 取出流出人数
                int sum = getAmount(strLine);
                index = strLine.indexOf(Integer.toString(sum));
                // 取出流入省份
                String inProvince = strLine.substring(strLine.lastIndexOf("流入") + 3, index - 1);

                ipMap.put(outProvince, ipMap.get(outProvince) - sum);
                ipMap.put(inProvince, ipMap.get(inProvince) + sum);
            } else if (strLine.matches(s4)) {
                // String s4 = "\\s*\\S+ 疑似患者 流入 \\S+ \\d+人\\s*";
                int index = strLine.indexOf(" 疑似患者 流入");
                // 取出流出省份，省份前面可能有空格
                String outProvince = strLine.substring(0, index);
                // 去掉全部空格
                outProvince.replace(" ", "");

                // 取出流出人数
                int sum = getAmount(strLine);
                index = strLine.indexOf(Integer.toString(sum));
                // 取出流出省份
                String inProvince = strLine.substring(strLine.lastIndexOf("流入") + 3, index - 1);

                spMap.put(outProvince, spMap.get(outProvince) - sum);
                spMap.put(inProvince, spMap.get(inProvince) + sum);
            } else if (strLine.matches(s5)) {
                int index = strLine.indexOf(" 死亡");
                // 取出省份，省份前面可能有空格
                String province = strLine.substring(0, index);
                // 去掉全部空格
                province.replace(" ", "");

                // 取出死亡人数
                int deadSum = getAmount(strLine);
                // 获得感染人数
                int ipSum = ipMap.get(province);
                // 更新感染人数
                ipMap.put(province, ipSum - deadSum);

                deadSum += deadMap.get(province);
                deadMap.put(province, deadSum);
            } else if (strLine.matches(s6)) {
                // s6 = "\\s*\\S+ 治愈 \\d+人\\s*";
                int index = strLine.indexOf(" 治愈");
                // 取出省份，省份前面可能有空格
                String province = strLine.substring(0, index);
                // 去掉全部空格
                province.replace(" ", "");

                // 取出治愈人数
                int cureSum = getAmount(strLine);
                // 获得感染人数
                int ipSum = ipMap.get(province);
                // 更新感染人数
                ipMap.put(province, ipSum - cureSum);
                cureSum += cureMap.get(province);
                cureMap.put(province, cureSum);

            } else if (strLine.matches(s7)) {
                // String s7 = "\\s*\\S+ 疑似患者 确诊感染 \\d+人\\s*";
                int index = strLine.indexOf(" 疑似患者 确诊感染");
                // 取出省份，省份前面可能有空格
                String province = strLine.substring(0, index);
                // 去掉全部空格
                province.replace(" ", "");
                int ipSum = getAmount(strLine);
                spMap.put(province, spMap.get(province) - ipSum);
                ipMap.put(province, ipMap.get(province) + ipSum);
            } else if (strLine.matches(s8)) {
                // String s8 = "\\s*\\S+ 排除 疑似患者 \\d+人\\s*";
                int index = strLine.indexOf(" 排除 疑似患者");
                // 取出省份，省份前面可能有空格
                String province = strLine.substring(0, index);
                // 去掉全部空格
                province.replace(" ", "");

                int excludeSum = getAmount(strLine);
                spMap.put(province, spMap.get(province) - excludeSum);
            }
        } // end of while
        br.close();
        fstream.close();
    }

    /**
     * 取出s里面的数字，s里面仅有一处地方有数字且是整数
     * 
     * @param s：满足handleFile函数中正则表达式s1-s8的字符串
     * @return 返回s里面的整数
     */
    public int getAmount(String s) {
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(s);
        m.find();
        return Integer.parseInt(s.substring(m.start(), m.end()));
    }

    public void out(FileWriter writer, List<String> provinces) throws Exception {
        for (String province : provinces) {
            writer.write(province);
            int size = types.size();
            String[] needTypes = new String[size];
            types.toArray(needTypes);
            if (size == 1) {
                if (needTypes[0].equals("ip"))
                    writer.write(" 感染患者" + ipMap.get(province) + "人\n");
                else if (needTypes[0].equals("sp"))
                    writer.write(" 疑似患者" + spMap.get(province) + "人\n");
                else if (needTypes[0].equals("cure"))
                    writer.write(" 治愈" + cureMap.get(province) + "人\n");
                else
                    writer.write(" 死亡" + deadMap.get(province) + "人\n");
                continue;
            }
            if (needTypes[0].equals("ip"))
                writer.write(" 感染患者" + ipMap.get(province));
            else if (needTypes[0].equals("sp"))
                writer.write(" 疑似患者" + spMap.get(province));
            else if (needTypes[0].equals("cure"))
                writer.write(" 治愈" + cureMap.get(province));
            else
                writer.write(" 死亡" + deadMap.get(province));

            for (int i = 1; i < size - 1; i++) {
                if (needTypes[i].equals("ip"))
                    writer.write("人 感染患者" + ipMap.get(province));
                else if (needTypes[i].equals("sp"))
                    writer.write("人 疑似患者" + spMap.get(province));
                else if (needTypes[i].equals("cure"))
                    writer.write("人 治愈" + cureMap.get(province));
                else
                    writer.write("人 死亡" + deadMap.get(province));
            }
            if (needTypes[size - 1].equals("ip"))
                writer.write("人 感染患者" + ipMap.get(province) + "人\n");
            else if (needTypes[size - 1].equals("sp"))
                writer.write("人 疑似患者" + spMap.get(province) + "人\n");
            else if (needTypes[size - 1].equals("cure"))
                writer.write("人 治愈" + cureMap.get(province) + "人\n");
            else
                writer.write("人 死亡" + deadMap.get(province) + "人\n");
        }
    }

}
