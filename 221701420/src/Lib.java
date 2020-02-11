import java.io.*;
import java.util.*;

//储存省份患者信息
class province_info{
    String province;//省份
    int infected;//感染患者
    int suspected;//疑似患者
    int cure;//治愈
    int dead;//死亡
}

public class Lib {
    public Lib() throws IOException {
        //初始化数据数组
        ArrayList<province_info> infos = new ArrayList<province_info>();
        sort_file();
    }
    //对指定目录下的文件按照日期进行排序
    private void sort_file() throws IOException {
        File file=new File("C:\\Users\\58215\\Desktop\\log");//创建指定目录的File对象
        File[] files = file.listFiles();
        List fileList = Arrays.asList(files);

        //按照日期升序对file对象进行排序
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile())
                    return -1;
                if (o1.isFile() && o2.isDirectory())
                    return 1;
                return o1.getName().compareTo(o2.getName());
            }
        });

        //读取排序好的文件对象并进行处理
        for (File file_temp : files) {
            System.out.println(file_temp.getName());
            read_file(file_temp);//读取指定的文件
        }
    }

    //读取指定的文件(传入File对象)
    private void read_file(File file) throws IOException {

        InputStreamReader reader=new InputStreamReader(new FileInputStream(file),"UTF-8");
        BufferedReader bfreader=new BufferedReader(reader);
        String line;
        while((line=bfreader.readLine())!=null) {//包含该行内容的字符串，不包含任何行终止符，如果已到达流末尾，则返回 null
            System.out.println(line);
            adjust_arry(line);
        }
    }

    //对某行文字进行判断并对数据队列进行调整
    private void adjust_arry(String line){

    }

}



