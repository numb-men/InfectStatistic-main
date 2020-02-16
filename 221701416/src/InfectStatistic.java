import java.io.IOException;
import java.util.*;
/**
 * InfectStatistic
 *
 * @author Hanani
 * @version v1.0.0
 * @since xxx
 */
class InfectStatistic {
	public String path=new String();
    public static void main(String[] args) throws IOException {
    	CoronavirusDetail CD=new CoronavirusDetail();
    	CD.Init();
    	AnalysisCommand(args);
    	CD.ReadAll();
    }
    public static void AnalysisCommand(String[] command) {
    	String $list= new String("list");
    	for(int i=0;i<command.length;i++) {
    		if((i==0) && !(command[i].equals($list))) {
    			System.out.println("请使用list命令进行操作");
    			System.exit(0);
    		}
    	}
    }
}
/**
 * CoronavirusDetail
 *
 * @author Hanani
 * @version v1.0.0
 * @since xxx
 */
class CoronavirusDetail{
	Map ProvinceMap= new HashMap();	
	public void Init() {
		//填入省份信息
		String[] ProvinceStr= {
				"安徽"  ,"北京"  ,"重庆"  ,"福建"  ,"甘肃"  ,
				"广东"  ,"广西"  ,"贵州"  ,"海南"  ,"河北"  ,
				"河南"  ,"黑龙江","湖北"  ,"湖南"  ,"吉林"  ,
				"江苏"  ,"江西"  ,"辽宁"  ,"内蒙古","宁夏"  ,
				"青海"  ,"山东"  ,"山西"  ,"陕西"  ,"上海"  ,
				"四川"  ,"天津"  ,"西藏"  ,"新疆"  ,"云南"  ,"浙江"  
		};
		//将省份注入Map中方便后面使用
		for(int i=0;i<ProvinceStr.length;i++) {
			ProvinceMap.put(ProvinceStr[i], Integer.valueOf(i));
		}
	}
	public void ReadAll() throws IOException{
		
	}
}