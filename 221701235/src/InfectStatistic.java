/**
 * InfectStatistic
 * TODO
 *
 * @author DDDDy
 * @version 1.0
 * @since 2020-02-16
 */
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class InfectStatistic {
    public static void main(String[] args) {
        //java InfectStatistic list -date 2020-01-22 -log D:/log/ -out D:/output.txt
        //按照上述命令（已知）测试文件读写
        System.out.println("命令测试：");
        for (int i = 0;i < args.length;i++) {
            System.out.println(args[i]);
        }
        String dateInput = args[2];
        String logPath = args[4];
        String outPath = args[6];
        String filePath = logPath + "/" +dateInput + ".log.txt";
        String fileContent = "";
        
        //读取
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream,"UTF-8");
            BufferedReader buffReader = new BufferedReader(inputStreamReader);
            String line = null;   
            while ((line = buffReader.readLine()) != null) {
                fileContent += line;
                fileContent +="\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //写入
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outPath);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream,"UTF-8");
            outputStreamWriter.write(fileContent);
            outputStreamWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
