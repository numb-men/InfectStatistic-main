import java.util.ArrayList;

/**
 * InfectStatistic
 * TODO
 *
 * @author massizhi
 * @version 1.0
 */
class InfectStatistic
{
    public static void main(String[] args)
    {
    	//建立省份名数组（包括全国）
    	String province[]={"全国","安徽","北京","重庆","福建","甘肃","广东","广西",
    			"贵州","海南","河北","河南","黑龙江","湖北","湖南","吉林","江苏",
    			"江西","辽宁","内蒙古","宁夏","青海","山东","山西","陕西","上海",
    			"四川","天津","西藏","新疆","云南","浙江"};
    	String type[]={"ip","sp","cure","dead"};
    	//记录各省（包括全国）各种类型的人的数量
    	int[][] number=new int[32][4];
    	//建立存放各类型命令行参数的ArrayList
    	String logList=new String();
		String outList=new String();
		String dateList=new String();
		ArrayList<String> typeList=new ArrayList<String>();
		ArrayList<String> provinceList=new ArrayList<String>();
		
		//初始化二维数组number
    	for (int i=0;i<32;i++)
    	{
    		for (int j=0;j<4;j++)
    		{
    			number[i][j]=0;
    		}
    	}
    	//得到存放各类型命令行参数的ArrayList具体值
    	for (int i=0;i<args.length;i++)
    	{
    		if (args[i].equals("-log")) {
    			logList=args[++i];
    		}
    		else if (args[i].equals("-out")) {
    			outList=args[++i];
    		}
    		else if (args[i].equals("-date")) {
    			dateList=args[++i];
    		}
    		else if (args[i].equals("-type")) {
    			for (int j=i+1;j<args.length;j++) {
    				if (!args[j].equals("-out")&&!args[j].equals("-date")&&
    					!args[j].equals("-log")&&!args[j].equals("-province"))
    				{
    					typeList.add(args[j]);
    				}
    				else 
    				{
    					i=j-1;
    					break;
    				}
    			}
    		}
    		else if (args[i].equals("-province")) {
    			for (int j=i+1;j<args.length;j++) {
    				if (!args[j].equals("-out")&&!args[j].equals("-date")&&
    					!args[j].equals("-type")&&!args[j].equals("-log"))
    				{
    					provinceList.add(args[j]);
    				}
    				else 
    				{
    					i=j-1;
    					break;
    				}
    			}
    		}
		}//获取ArrayList循环结束
    	
    	//test
    	System.out.println(logList);
    	System.out.println(outList);
    	System.out.println(dateList);
    	for (int i=0;i<typeList.size();i++)
    		System.out.print(typeList.get(i));
    	System.out.println();
    	for (int i=0;i<provinceList.size();i++)
    		System.out.print(provinceList.get(i));
    	System.out.println();
    	//list -date 2020-01-22 -province jingsu fujian -log D:/log/ -out D:/output.txt -type a b c
    }
}