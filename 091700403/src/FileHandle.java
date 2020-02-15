import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

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

    public StringBuilder fileRead(String fileName) throws Exception {
        File file = new File(fileName);// 定义一个file对象，用来初始化FileReader
        FileReader reader = new FileReader(file);// 定义一个fileReader对象，用来初始化BufferedReader
        BufferedReader bReader = new BufferedReader(reader);// new一个BufferedReader对象，将文件内容读取到缓存
        sb = new StringBuilder();// 定义一个字符串缓存，将字符串存放缓存中
        String s = "";
        while ((s = bReader.readLine()) != null) {// 逐行读取文件内容，不读取换行符和末尾的空格
            sb.append(s + "\n");// 将读取的字符串添加换行符后累加存放在缓存中
            System.out.println(s);
        }
        bReader.close();

        return sb;
    }

}
