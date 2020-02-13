package test;

import argument.ArgParser;
import com.sun.org.apache.xpath.internal.Arg;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;
import org.junit.Test;
import reg.AllInformation;

public class ArgParerTest  {
    @Test
    public void testMain() {
//        String[] arg = {"list","-log","D:/InfectStatistic-main/221701139/log","-out", "D:/output.txt"};
        String[] arguments = {"list","-log","D:/InfectStatistic-main/221701139/log", "-out", "D:/output.txt", "-date", "2020-01-23",
                "-province","福建","湖北","四川","-type","sp","cure"};
        ArgParser argParser = new ArgParser(arguments);
        argParser.printArg();

//        String date = argParser.getVals("-date");
//        System.out.println(date);
//        File dir = new File(path);
//        File[] files = dir.listFiles();
//        for (File file:files) {
//            if (file.getName().contains(date)) {
//                String filePath = path+"/"+file.getName();
//
//                AllInformation allInformation = new AllInformation();
//                allInformation.processInfo(filePath);
//                allInformation.writeIntoLog(outFileName);
//
//                allInformation.printInfo();
//            }
            // 只测试一个文件
//            break;
        }
//        Map<String, String> map = ArgParser.argumentParser(arg);

//        String root = map.get("-log");
//        String outFile = map.get("-out");
//        // 执行list命令
//        String REGEX = "(\\S+) 疑似患者 确认感染 (\\d+)人";
//        String INPUT = "福建 疑似患者 确诊感染 1人";
//        Pattern pattern = Pattern.compile(REGEX);
//        Matcher matcher = pattern.matcher(INPUT);
//        System.out.println(matcher.find());
//
//        Integer[] integers1 = {1,0,0,0};
//        Integer[] integers2 = {-1,3,4,5};
//        for (int i = 0;i<integers1.length;i++) {
//
//                System.out.print(integers1[i]+" ");
//                integers1[i] = integers1[i]+integers2[i];
//                System.out.println(integers1[i]);
//
//        }
//        Map<String,String> tmp = new LinkedHashMap<>();
//        tmp.put("1","2");
//        tmp.put("1","3");
//        System.out.println(tmp.get("1"));

}
