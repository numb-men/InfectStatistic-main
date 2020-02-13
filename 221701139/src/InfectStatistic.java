import argument.ArgParser;
import reg.AllInformation;

import java.io.*;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * 这个是调用者
 */
public class InfectStatistic {
    public static void main(String[] args) throws IOException {
        String REGEX = "(\\S+) 新增 感染患者 (\\d+)人";
        String INPUT = "福建 新增 感染患者 2";

        // 返回一个正则表达式对象
        Pattern pattern = Pattern.compile(REGEX);
        // 放回一个匹配器对象
        Matcher matcher = pattern.matcher(INPUT);
        boolean b = matcher.matches();

        System.out.println(b);
        for (String arg:args) {
            System.out.println(arg);
        }
        Map<String, String> arg = ArgParser.argumentParser(args);

        // 解析根据参数执行命令
        String path = arg.get("-log");
        // 得到输出文件目录
        String outFileName = arg.get("-out");
        File dir = new File(path);

        File[] files = dir.listFiles();
        for (File file:files) {
            String filePath = path+"/"+file.getName();

            AllInformation allInformation = new AllInformation();
            allInformation.processInfo(filePath);
            allInformation.writeIntoLog("D:/out.txt");

            allInformation.printInfo();
            // 只测试一个文件
            break;
        }
    }
}
