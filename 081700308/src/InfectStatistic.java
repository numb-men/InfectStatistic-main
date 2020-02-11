import java.util.regex.Pattern;

/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
class InfectStatistic {
		
		static String[] provinces= {
				"全国",
				"安徽",
				"北京",
				"重庆",
				"福建",
				"甘肃",
				"广东",
				"广西",
				"贵州",
				"海南",
				"河北",
				"河南",
				"黑龙江",
				"湖北",
				"湖南",
				"吉林",
				"江苏",
				"江西",
				"辽宁",
				"内蒙古",
				"宁夏",
				"青海",
				"山东",
				"山西",
				"陕西",
				"上海",
				"四川",
				"天津",
				"西藏",
				"新疆",
				"云南",
				"浙江"};
		
		int[] ip=new int[32];
		int[] sp=new int[32];
		int[] cure=new int[32];
		int[] dead=new int[32];
		
		//省份对应下标
		public int proToInt(String p)
		{
			
			return 0;
		}
		
	
    	
	 static String type1="\\w+ 新增 感染患者 \\d+人";
		String type2="\\w+ 新增 疑似患者 \\d+人";
		String type3="\\w+ 感染患者 流入 \\w+ \\d+人";
		String type4="\\w+ 疑似患者 流入 \\w+ \\d+人";
		String type5="\\w+ 死亡 \\d+人";
		String type6="\\w+ 治愈 \\d+人";
		String type7="\\w+ 疑似患者 确诊感染 \\d+人";
		String type8="\\w+ 排除 疑似患者 \\d+人";
		
		public static String[] getAddIp(String text)
		{
			Pattern pattern1=Pattern.compile("(.*) 新增");
			Pattern pattern2=Pattern.compile("新");
			return pattern2.split(text);//getString(pattern1,pattern2);
		}
    
		 public static void main(String args[ ])
		    {
			 
			 String x="全国 新增 感染患者 1人";
			 getAddIp(x);
					
		       System.out.println(getAddIp(x)[0]);
		    }// 方法main结束
		
		
	

}
