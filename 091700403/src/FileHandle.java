import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Project Name:InfectStatistic-main2
 * File Name:FileHandle.java
 * Package Name:
 * Date:上午9:43:46
 * Copyright (c) 2020, bluemobi All Rights Reserved.
 *
*/

/**
 * Description: <br/>
 * Date: 上午9:43:46 <br/>
 * 
 * @author 丁鹏
 * @version
 * @see
 */
public class FileHandle {

    StringBuilder sb;

    // 读取一个文件内容
    public StringBuilder fileRead(String fileName) {
        File file = new File(fileName);// 定义一个file对象，用来初始化FileReader
        FileReader reader = null;
        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {

            // Auto-generated catch block
            e.printStackTrace();

        } // 定义一个fileReader对象，用来初始化BufferedReader
        BufferedReader bReader = new BufferedReader(reader);// new一个BufferedReader对象，将文件内容读取到缓存
        sb = new StringBuilder();// 定义一个字符串缓存，将字符串存放缓存中
        String s = "";
        try {
            while ((s = bReader.readLine()) != null) {// 逐行读取文件内容，不读取换行符和末尾的空格
                sb.append(s + "\n");// 将读取的字符串添加换行符后累加存放在缓存中

            }
        } catch (IOException e) {

            // Auto-generated catch block
            e.printStackTrace();

        }
        try {
            bReader.close();
        } catch (IOException e) {

            // Auto-generated catch block
            e.printStackTrace();

        }

        return sb;
    }

    public ArrayList<String> getFiles(String path, String sdate) {
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");

        File file = new File(path);
        String[] nameStrings = file.list(); // 取得path目录下所有文件名称
        ArrayList<String> fileName = new ArrayList<>();
        String FileDateStr = "";
        Date fileData = new Date();
        Date argDate = null;

        try {
            argDate = dFormat.parse(sdate);// 将参数转为日期形式

            for (int i = 0; i < nameStrings.length; i++) {
                FileDateStr = nameStrings[i].substring(0, nameStrings[i].indexOf('.'));// 截取文件名中的日期

                fileData = dFormat.parse(FileDateStr);

                // argDate = dFormat.parse(sdate);// 指定日期

                if (fileData.getTime() <= argDate.getTime()) {

                    fileName.add(nameStrings[i]);
                }

            }
        } catch (ParseException e) {

            e.printStackTrace();

        }

        return fileName;
    }

    // 读取多个文件内容
    public StringBuilder filesRead(String path, String sdate) {
        ArrayList<String> fileName = getFiles(path, sdate);
        StringBuilder ab = new StringBuilder();
        Iterator<String> it = fileName.iterator();

        StringBuilder abtmp = new StringBuilder();
        while (it.hasNext()) {
            abtmp = fileRead(path + "\\" + it.next());
            ab.append(abtmp);
        }
        return ab;
    }

}
