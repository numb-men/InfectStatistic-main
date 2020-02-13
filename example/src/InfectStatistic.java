import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
//省份
class Province{
	String name;
	public int ip,sp,cure,dead;
	//感染患者，疑似患者，治愈，死亡患者
}


class InfectStatistic{
    public static void main(String[] args) throws FileNotFoundException{
    	String strLine;
    	Province[] province = new Province[31];
    	String result = "F:\\Users\\lenovo\\Documents\\GitHub\\InfectStatistic-main\\example\\log\\省份.txt";
    	//省份名文件地址
    	FileInputStream fstream = new FileInputStream(new File(result));
		InputStreamReader isr = null;
		
		try{
			isr = new InputStreamReader(fstream,"UTF-8");
		}
		catch(UnsupportedEncodingException e1){
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		} //指定以UTF-8编码读入
    	BufferedReader br = new BufferedReader(isr);//构造一个BufferedReader，里面存放在控制台输入的字节转换后成的字符。
    	try{
    		int i = 0; 
			while((strLine = br.readLine()) != null){
				province[i] = new Province();
				province[i].name = strLine;
				i++;
			}
		}
    	catch(IOException e){
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
    	
    	result = "F:\\Users\\lenovo\\Documents\\GitHub\\InfectStatistic-main\\example\\log\\2020-01-22.log.txt";
    	//log文件地址
    	fstream = new FileInputStream(new File(result));
		try{
			isr = new InputStreamReader(fstream,"UTF-8");
		}
		catch(UnsupportedEncodingException e1){
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		} //指定以UTF-8编码读入
		br = new BufferedReader(isr);//构造一个BufferedReader，里面存放在控制台输入的字节转换后成的字符。
    	try{
			while((strLine = br.readLine()) != null){
				String str[] = strLine.split(" ");//用空格分隔每一行中的记录
				if(!str[0].substring(0,2).equals("//")){
					Update(str,province,str.length);
				}
			}
		}
    	catch(IOException e){
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
    	
    	for(int i = 0;i<31;i++){
    		System.out.println(province[i].name + " "
    				 + "感染患者" + province[i].ip + " "
    				 + "疑似患者" + province[i].sp + " "
    				 + "治愈" + province[i].cure + " "
    				 + "死亡" + province[i].dead);
    	}
    }

    
	private static void Update(String str[],Province province[],int length){//读取Log文件后更新省份信息变动
		int num = 0;//记录变动人数
		int i = 0;//记录变动省份
		str[length - 1] = str[length - 1].trim();
		if(str[length - 1] != null&&!"".equals(str[length - 1])){
			for(int k = 0;k<str[length - 1].length();k++){
				if(str[length - 1].charAt(k) >= 48 && str[length - 1].charAt(k) <= 57){
					num = 10*num+str[length - 1].charAt(k) - 48;
				}
			}
		}
		
		while(!str[0].equals(province[i].name)&&i<31){
			i++;
		}
		
		if(length == 3){
			if(str[1].equals("治愈")){//xx省份患者治愈
				province[i].ip -= num;
				province[i].cure += num;
			}
			else if(str[1].equals("死亡")){//xx省份患者死亡
				province[i].ip -= num;
				province[i].dead += num;
			}	
		}
		else if(length == 4){
			if(str[1].equals("新增")){
				if(str[2].equals("感染患者")){//xx省份新增感染患者
					province[i].ip += num;
				}
				else if(str[2].equals("疑似患者")){//xx省份新增疑似患者
					province[i].sp += num;
				}
			}
			else if(str[1].equals("疑似患者")){////xx省份疑似患者确诊
				province[i].sp -= num;
				province[i].ip += num;
			}
			else if(str[1].equals("排除")){//xx省份疑似患者排除
				province[i].sp -= num;
			}
		}
		else if(length == 5){
			int j = 0;
			while(!str[3].equals(province[j].name)&&j<31){
				j++;
			}
			if(str[1].equals("疑似患者")){//a省疑似患者流入b省
				province[i].sp -= num;
				province[j].sp += num;
			}
			else if(str[1].equals("感染患者")){//a省感染患者流入b省
				province[i].ip -= num;
				province[j].ip += num;
			}
		}
	}
	

}