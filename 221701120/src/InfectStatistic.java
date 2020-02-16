import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map.Entry;
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
class InfectStatistic {
    public static void main(String[] args){
        
        CommandParser cmParser = new CommandParser(args);
        InfectedMap map = new InfectedMap();
        FileUtils reader = new FileUtils();
        try {
            reader.parseFile(cmParser.getSrcPath(), map);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        for (HashMap.Entry<String, InfectedArea> entry : map.map.entrySet()) {
            System.out.println("Key = " + entry.getKey() + 
                    ", Value = infect:" + entry.getValue().infectedNum +
                    ",potential:" + entry.getValue().potentialNum +
                    ",cure:" + entry.getValue().curedNum +
                    ",dead:" + entry.getValue().deadNum);
        }
       
        //System.out.println(numString);      
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

    public String getSrcPath() {
        return srcPath;
    }
    
    public String getDstPath() {
        return dstPath;
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
    public static void parseFile(String srcPath, InfectedMap map) throws IOException {
        InputStream inStream = new FileInputStream(srcPath);
        String line; 
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
        line = reader.readLine(); 
        InfoParser infoParser = new InfoParser();
        while (line != null) { 
            if (!line.matches("//(.*)")) {
                infoParser.parseInfo(line, map);
                //System.out.println(line);
                
            }     
            line = reader.readLine(); 
        }
        reader.close();
        inStream.close();
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

