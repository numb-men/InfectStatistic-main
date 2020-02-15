import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * InfectStatistic
 * TODO
 *
 * @author 221701120_hxy
 * @version 1.0
 * @since 2020.2.15
 */
class InfectStatistic {
    public static void main(String[] args) {
        
        CommandParser cmParser = new CommandParser(args);
        
        System.out.println("helloworld");
    }
}

/**
 * 
 * 文件读取的工具类
 * TODO
 *
 * @author 221701120_hxy
 * @version 1.0
 * @since 2020.2.15
 */
class FileUtils{
    
    public static void readToBuffer(StringBuffer buffer, String filePath) throws IOException {
        InputStream inStream = new FileInputStream(filePath);
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            buffer.append(line); // 将读到的内容添加到 buffer 中
            buffer.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        inStream.close();
    }
}

/**
 * 
 * 用来解析命令行传入的参数
 * TODO
 * 
 * @author 221701120_hxy
 * @version 1.0
 * @since 2020.2.15
 */
class CommandParser{ 
    //存放-log
    private String srcPath;
    //存放-out
    private String dstPath;
    //存放-date
    private String dateString;
    //存放-type
    private String typeString;
    //存放-province
    private String provinceString;
    
    public CommandParser(String[] args) {
        for (int i = 1; i < args.length; i++){
            if ( args[i].equals("-log")) {
                i++;
                this.srcPath = args[i];
            }else if (args[i].equals("-out")){
                i++;
                this.dstPath = args[i];
            }else if (args[i].equals("-date")){
                i++;
                this.dateString = args[i];
            } else if (args[i].equals("-type")){
                i++;
                this.typeString = args[i];
            } else if (args[i].equals("-province")){
                i++;
                this.provinceString = args[i];
            }                 
        }
    }
    
    static String getContent() {
        String srcPath = "";
        
        return srcPath;
    }
    
}

