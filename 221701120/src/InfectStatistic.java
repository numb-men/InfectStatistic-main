
import java.awt.RenderingHints.Key;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * InfectStatistic
 * TODO
 *
 * @author 221701120_hxy
 * @version 1.0
 * @since 2020.2.15
 */
public class InfectStatistic {
    public static void main(String[] args){
        
        CommandParser cmParser = new CommandParser(args);
        CommandRun commandRun = new CommandRun(cmParser);
        commandRun.runCommand();


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
    public String srcPath = "";
    //存放-out
    public String dstPath = "";
    //存放-date
    public String dateString = "";
    //存放-type
    public boolean hasType = false;
    public List<String> typeList = new ArrayList<String>();
    //存放-province
    public boolean hasProvince = false;
    public List<String> provinceList = new ArrayList<String>();
    
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
                this.hasType = true;
                int index = i;
                for (int j = 0; j < args.length - index ; j++) {
                    this.typeList.add(args[i]);
                    if (args[i].equals("-province")) {
                        i --;
                        break;
                    }
                    i++;
                }
                
                if (typeList.size() == 4) {
                    hasType = false;
                }
            } else if (args[i].equals("-province")){
                //若含有province参数，则要对其后参数进行记录
                this.hasProvince = true;
                i++;
                int index = i;
                for (int j = 0; j < args.length - index; j++) {
                    this.provinceList.add(args[i]);
                    i++;
                }
            }                 
        }
    }   
}

/**
 * 
 * 命令执行类
 * TODO
 *
 * @author 221701120_hxy
 * @version 1.0
 * @since 2020.2.15
 */
class CommandRun{
    CommandParser parser;
    boolean hasDate = false;
    boolean hasType = false;
    boolean hasProvince = false;
    
    public CommandRun(CommandParser parser) {
        if (!parser.dateString.equals("")) {
            this.hasDate = true;
        }
        if (parser.hasType) {
            this.hasType = true;
        }
        if (parser.hasProvince) {
            this.hasProvince = true;
        }
        this.parser = parser;
    }
    
    public void runCommand() {
        InfectedMap map = new InfectedMap();
        FileInputUtils reader = new FileInputUtils();
        FileOutputUtils writer = new FileOutputUtils();
        
        try {
            
            reader.parseFile(parser.srcPath, map, hasDate, parser.dateString);
            map.sortByProvince();
            writer.writeFile(parser.dstPath, map, hasType, parser.typeList, hasProvince, parser.provinceList);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
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
class FileInputUtils{
    /*
     * 对传入log文件夹目录下的所有文件进行处理
     */
    public void parseFile(String dirPath, InfectedMap map, boolean hasDate, String dateString) throws IOException{
        //获取文件目录
        File dirFile = new File(dirPath);
        //获取目录下所有*.log.txt文件
        File[] logFiles = dirFile.listFiles();
        //存放所有的文件名
        List<String> filesName = new ArrayList<String>();
        List<File> filesList = Arrays.asList(logFiles);

        //对filesList根据文件名按照时间进行排序
        //覆写compare方法
        Collections.sort(filesList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        for (int i = 0; i < filesList.size() ;i++) {
                filesName.add(filesList.get(i).toString());
            }
        
        String srcPath = "";
        
        //改成绝对路径
        String absolutePath = "";
        if (hasDate) {
            absolutePath = dirPath + dateString + ".log.txt";

        }
        for (int i = 0; i<filesName.size(); i++) {
            srcPath = filesName.get(i);
            inputFile(srcPath, map);
            //若命令中有 -date参数
            if(hasDate) {
                if (srcPath.equals(absolutePath)) {
                    break;
                }
            }
        }
        
    }
    
    /*
     * 解析每个*.log.txt文件的内容
     */
    public void inputFile(String srcPath, InfectedMap map) throws IOException {
        InputStream inStream = new FileInputStream(srcPath);
        String line; 
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
        line = reader.readLine(); 
        InfoParser infoParser = new InfoParser();
        while (line != null) { 
            if (!line.matches("//(.*)") && !line.equals("")) {
                infoParser.parseInfo(line, map);
            }     
            line = reader.readLine(); 
        }
        reader.close();
        inStream.close();
    }
}

/**
 * 
 * 文件输出的工具类
 * TODO
 *
 * @author 221701120_hxy
 * @version 1.0
 * @since 2020.2.15
 */
class FileOutputUtils{
    public void writeFile(String dstPath, InfectedMap map,
                boolean hasType, List<String> typeList, boolean hasProvince ,
                List<String> provinceList) throws IOException {    
        OutputStream outStream = new FileOutputStream(dstPath);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outStream));

        if (hasProvince && !hasType) {
            String provinceName = "";
            for (int i = 0; i < provinceList.size(); i++) {
                provinceName = provinceList.get(i);
                InfectedArea province = map.map.get(provinceName);
                writeAll(provinceName, bufferedWriter, province);
            }
           
        }else if (!hasProvince && hasType) {
            for (HashMap.Entry<String, InfectedArea> entry : map.map.entrySet()) {
                String keyString = entry.getKey();
                bufferedWriter.write(keyString);
                if(typeList.contains("ip")) {
                    bufferedWriter.write(" 感染患者" + map.map.get(keyString).infectedNum + "人");
                }
                if(typeList.contains("sp")) {
                    bufferedWriter.write(" 疑似患者" + map.map.get(keyString).potentialNum + "人");
                }
                if(typeList.contains("cure")) {
                    bufferedWriter.write(" 治愈" + map.map.get(keyString).curedNum + "人");
                }
                if(typeList.contains("dead")) {
                    bufferedWriter.write(" 死亡" + map.map.get(keyString).deadNum + "人");
                }
                bufferedWriter.write("\n");  
               }
        }else if (hasProvince && hasType) {
            String provinceName = "";
            for (int i = 0; i < provinceList.size(); i++) {
                provinceName = provinceList.get(i);
                InfectedArea province = map.map.get(provinceName);
                String keyString = provinceName;
                bufferedWriter.write(keyString);
                if(typeList.contains("ip")) {
                    bufferedWriter.write(" 感染患者" + province.infectedNum + "人");
                }
                if(typeList.contains("sp")) {
                    bufferedWriter.write(" 疑似患者" + province.potentialNum + "人");
                }
                if(typeList.contains("cure")) {
                    bufferedWriter.write(" 治愈" + province.curedNum + "人");
                }
                if(typeList.contains("dead")) {
                    bufferedWriter.write(" 死亡" + province.deadNum + "人");
                }
                bufferedWriter.write("\n");  
            }
        }else {
            for (HashMap.Entry<String, InfectedArea> entry : map.map.entrySet()) {
                String keyString = entry.getKey();
                writeAll(keyString, bufferedWriter, map.map.get(keyString));
               }
        }
        
            
        bufferedWriter.write("//该数据并非真实数据，仅供测试程序使用\n"); 
        bufferedWriter.close();
        outStream.close();
    }
    
    /*
     * 来向文件写信息
     */
    public void writeAll(String keyString, BufferedWriter bufferedWriter, InfectedArea infectedArea) {
        try {
            bufferedWriter.write(keyString);
            if(infectedArea.infectedNum > 0) {
                bufferedWriter.write(" 感染患者" + infectedArea.infectedNum + "人");
            }
            if(infectedArea.potentialNum > 0) {
                bufferedWriter.write(" 疑似患者" + infectedArea.potentialNum + "人");
            }
            if(infectedArea.curedNum > 0) {
                bufferedWriter.write(" 治愈" + infectedArea.curedNum + "人");
            }
            if(infectedArea.deadNum > 0) {
                bufferedWriter.write(" 死亡" + infectedArea.deadNum + "人");
            }
            bufferedWriter.write("\n");  
        } catch (IOException e) {
            e.printStackTrace();
        }            
          
    }
}

/**
 * 
 * 存储被感染省份地区的相关信息
 * TODO
 *
 * @author 221701120_hxy
 * @version 1.0
 * @since 2020.2.15
 */
class InfectedArea{
    public int infectedNum = 0;
    public int potentialNum = 0;
    public int curedNum = 0;
    public int deadNum = 0;  
}

class InfectedMap{
    //Key存储感染地区的名字，Value存储感染地区的人数信息
    HashMap<String, InfectedArea> map;
    
    public InfectedMap() {
        map = new HashMap<String, InfectedArea>();
        String wholeCountry = "全国";
        InfectedArea countryArea = new InfectedArea();
        map.put(wholeCountry, countryArea);
    }
    
    /*
     * 将map表中的数据根据key值进行排序
     */
    public void sortByProvince() {
        List<String> regulationOrder = Arrays.asList(
                "全国", "安徽", "北京",
                "重庆", "福建", "甘肃", "广东", "广西", "贵州",
                "海南", "河北", "河南", "黑龙江","湖北", "湖南",
                "吉林", "江苏", "江西", "辽宁", "内蒙古","宁夏",
                "青海", "山东", "山西", "陕西", "上海",  "四川", 
                "天津", "西藏", "新疆", "云南", "浙江" );
     
        Set<Entry<String,InfectedArea>> set = map.entrySet();
        List<Entry<String,InfectedArea>> list = new ArrayList<>();
        
        //把set加到list中去
        Iterator<Entry<String,InfectedArea>> it = set.iterator(); 
        while(it.hasNext()) {
            Entry<String,InfectedArea> entry = it.next();
            list.add(entry);
        }
        
        Collections.sort(list, new Comparator<Entry<String,InfectedArea>>() {
 
            @Override
            public int compare(Entry<String, InfectedArea> o1, Entry<String, InfectedArea> o2) {
                String value1 = o1.getKey();
                String value2 = o2.getKey();
                int index1 = regulationOrder.indexOf(value1);
                int index2 = regulationOrder.indexOf(value2);
                return index1-index2;
            }
        });
        //对list排序完成后放入LinkedHashMap即可。
        HashMap<String, InfectedArea> map2 = new LinkedHashMap<>();
        for (Entry<String, InfectedArea> entry : list) {
            map2.put(entry.getKey(), entry.getValue());
            map = map2;
        }
    }
}
/**
 * 
 * 对文件信息的处理
 * TODO
 *
 * @author 221701120_hxy
 * @version 1.0
 * @since 2020.2.15
 */
class InfoParser{
    
    public void parseInfo(String line, InfectedMap infectedMap){
        //将传入的信息按空格进行切割
        String cutString = " ";
        String[] newLine = line.split(cutString);
        
        //获取人数信息
        int len = newLine.length;
        String regEx="[^0-9]";  
        Pattern p = Pattern.compile(regEx);  
        Matcher m = p.matcher(newLine[len-1]); 
        String numString= m.replaceAll("").trim();
        int num = Integer.valueOf(numString);
        
        //查找infectedMap中是否含有该地区
        boolean existed = infectedMap.map.containsKey(newLine[0]);
        
        //若无该地区，则先创建
        if (!existed){
            InfectedArea thisArea = new InfectedArea();
            infectedMap.map.put(newLine[0], thisArea);
        }
        
        InfectedArea countryTmp = infectedMap.map.get("全国");
        InfectedArea provinceTmp = infectedMap.map.get(newLine[0]); 
       
        //用正则表达式进行处理
        if (line.matches("(.*)新增 感染患者(.*)")){
            countryTmp.infectedNum += num;
            provinceTmp.infectedNum += num;           
        }else if (line.matches("(.*)新增 疑似患者(.*)")){
            countryTmp.potentialNum += num;
            provinceTmp.potentialNum += num;
        }else if (line.matches("(.*)死亡(.*)")){
            countryTmp.deadNum += num;
            provinceTmp.deadNum += num;
            countryTmp.infectedNum -= num;
            provinceTmp.infectedNum -= num;
        }else if (line.matches("(.*)治愈(.*)")){
            countryTmp.curedNum += num;
            provinceTmp.curedNum += num;
            countryTmp.infectedNum -= num;
            provinceTmp.infectedNum -= num;
        }else if (line.matches("(.*)疑似患者 确诊感染(.*)")){
            countryTmp.infectedNum += num;
            provinceTmp.infectedNum += num;
            countryTmp.potentialNum -= num;
            provinceTmp.potentialNum -= num;
        }else if (line.matches("(.*)排除 疑似患者(.*)")){
            countryTmp.potentialNum -= num;
            provinceTmp.potentialNum -= num;
        }else if (line.matches("(.*)流入(.*)")){
            boolean existed2 = infectedMap.map.containsKey(newLine[3]);
            if(!existed2){
                InfectedArea thisArea = new InfectedArea();
                infectedMap.map.put(newLine[3], thisArea);
            }
            InfectedArea province2Tmp = infectedMap.map.get(newLine[3]); 
            if (line.matches("(.*)疑似患者 流入(.*)")){
                provinceTmp.potentialNum -= num;
                province2Tmp.potentialNum += num;
            }else if (line.matches("(.*)感染患者 流入(.*)")) {
                provinceTmp.infectedNum -= num;
                province2Tmp.infectedNum += num;
            }
            infectedMap.map.replace(newLine[3], province2Tmp);
        }
        
        infectedMap.map.replace(newLine[0], countryTmp);
        infectedMap.map.replace(newLine[0], provinceTmp);
    }
    
  
}


