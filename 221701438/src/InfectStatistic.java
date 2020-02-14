import java.io.*;
import java.io.File;
import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
/**
 * InfectStatistic
 * TODO
 *
 * @author hmx1
 * @version 1.0.0
 * @since 2020.2.10
 */
class InfectStatistic {
    /**
     * TODO
     * 建立Hashmap解析命令行，将命令行的参数与参数值分开
     * @author hmx1
     * @version 1.0.0
     * @since 2020.2.10
     */
    public static HashMap<String, String[]> ParsingCommandLine(String[] args) {
        HashMap<String, String[]> Arguments = new HashMap<String, String[]>();
        ArrayList<String> ParsingValue = new ArrayList<>();
        for (int i = 1; i<args.length; i++) {
        }
    }
    /**
     * TODO
     * 清空文件内容
     * @author hmx1
     * @version 1.0.0
     * @since 2020.2.10
     */
    public static void clearInfoForFile(String fileName) {
        File file =new File(fileName);
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter =new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * TODO
     * 将字符串写入文件
     * @author hmx1
     * @version 1.0.0
     * @since 2020.2.11
     */
    public static void WriteStringToFile(String fileName, String str) {
        try {
            File file = new File(fileName);
            PrintStream ps = new PrintStream(new FileOutputStream(file));
            ps.println(str);
            ps.append("\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     * TODO
     * 读取目录下的所有日志
     * @author hmx1
     * @version 1.0.0
     * @since 2020.2.11
     */
    public static String[] readFile(String path,String date) throws ParseException {
        List allFilePath = getFileName(path,date);
        String[] allContent = new String[allPath.size()];
        for (int i = 0; i < allPath.size(); i++){
            File file = new File(String.valueOf(allPath.get(i)));
            StringBuilder result = new StringBuilder();
            // 构造一个BufferedReader类来读取文件
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
            String s = null;
            // 使用readLine方法，一次读一行，并忽略注释行
            while ((s = br.readLine()) != null && !s.startsWith("//")) {
                result.append(System.lineSeparator() + s);
            }
            br.close();
            allContent[i] = result.toString();
        }
        return allContent;
    }
    /**
     * TODO
     * 读取文件夹下所有文件的文件名
     * @author hmx1
     * @version 1.0.0
     * @since 2020.2.11
     */
    public static List getFileName(String path, String date) throws ParseException {
        List listFileName = new ArrayList<>();
        File file = new File(path);
        if (file.isDirectory()) {
            // 获取路径下的所有文件
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                // 如果还是文件夹 递归获取里面的文件与文件夹
                if (files[i].isDirectory()) {
                    //System.out.println("目录：" + files[i].getPath());
                    getFiles(files[i].getPath());
                } else {
                    listFileName.add(files[i].getPath());
                    //System.out.println("文件：" + files[i].getPath());
                }
            }
        } else {
            listFileName.add(file.getPath());
            //System.out.println("文件：" + file.getPath());
        }
        return listFileName;
    }
    /**
     * TODO
     * 判断字符串是否为日期格式yyyy-mm-dd
     * @author hmx1
     * @version 1.0.0
     * @since 2020.2.10
     */
    private static boolean isValidDate(String str) {
        boolean flag = true;
        // 日期格式为yyyy-mm-dd；
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            format.setLenient(false);
            format.parse(str);
        } catch (Exception e) {
            flag = false;
        }
        return convertSuccess;
    }















    public static void main(String[] args) {
        System.out.println("helloworld");
    }
}
