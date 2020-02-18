import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * InfectStatistic
 * TODO
 *
 * @author massizhi
 * @version 2.0
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
    	//记录各省（包括全国）各种类型的人的数量,最后一位记录该省是否被涉及到
    	int[][] number=new int[32][5];
    	//建立存放各类型命令行参数的ArrayList
    	String logList=new String();
		String outList=new String();
		String dateList=new String();
		ArrayList<String> typeList=new ArrayList<String>();
		ArrayList<Integer> typeInt=new ArrayList<Integer>();
		ArrayList<String> provinceList=new ArrayList<String>();
		ArrayList<Integer> provinceInt=new ArrayList<Integer>();
		
		//初始化二维数组number
    	for (int i=0;i<32;i++)
    	{
    		for (int j=0;j<5;j++)
    		{
    			number[i][j]=0;
    		}
    	}
    	//得到存放各类型命令行参数的ArrayList具体值
    	for (int i=0;i<args.length;i++)
    	{
    		if (args[i].equals("-log")) {
    			logList=args[++i];
    			//System.out.println(logList);
    			logList=logList.replace('/', '\\');
    			//System.out.println(logList);
    		}
    		else if (args[i].equals("-out")) {
    			outList=args[++i];
    			outList=outList.replace('/', '\\');
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
    	//获取省份、类型对应的下标
    	for (int i=0;i<typeList.size();i++)
    	{
    		for (int j=0;j<type.length;j++)
    		{
    			if (typeList.get(i).equals(type[j])) 
    			{
    				typeInt.add(j);
    			}
    		}
    	}
    	for (int i=0;i<provinceList.size();i++)
    	{
    		for (int j=0;j<province.length;j++)
    		{
    			if (provinceList.get(i).equals(province[j])) 
    			{
    				provinceInt.add(j);
    			}
    		}
    	}
    	
    	File file=new File(logList);
        //获取该路径下的文件和文件夹
        String[] arr=file.list();
        //处理日志中的数据
        for(String s:arr)
        {
        	if (s.compareTo(dateList+".log")>0)
        	{
        		continue;
        	}
			try 
			{			    
				File afile = new File(logList+s);
		        Scanner sc = new Scanner(afile);
		        while (sc.hasNext()) 
		        {
		            String first=sc.next();
		            if (first.equals("//")) 
		            {
		            	sc.nextLine();
		            } 
		            else 
		            {
		            	int index=0;
		            	for (int i=0;i<32;i++)
		            	{
		            		if (first.equals(province[i]))
		            		{
		            			index=i;
		            			break;
		            		}
		            	}
		            	//System.out.println(index);
		            	number[0][4]=1;
		            	number[index][4]=1;
		            	String second=sc.next();
		                if (second.equals("新增")) 
		                {
		                	String third=sc.next();
		                	String four=sc.next();
		                	four=four.replace("人", "");
		                	int member=Integer.parseInt(four);	                	
		                	if (third.equals("感染患者"))
		                	{
		                		number[0][0]+=member;
		                		number[index][0]+=member;
		                	}
		                	else
		                	{
		                		number[0][1]+=member;
		                		number[index][1]+=member;
		                	}
		                }
		                else if (second.equals("感染患者"))
		                {
		                	sc.next();
		                	String four=sc.next();
		                	String five=sc.next();
		                	five=five.replace("人", "");
		                	int member=Integer.parseInt(five);
		                	int index1=0;
		                	for (int i=0;i<32;i++)
			            	{
			            		if (four.equals(province[i]))
			            		{
			            			index1=i;
			            			break;
			            		}
			            	}
		                	number[index][0]-=member;
		                	number[index1][0]+=member;
		                	number[index1][4]=1;
		                }
		                else if (second.equals("疑似患者"))
		                {
		                	String three=sc.next();
		                	if (three.equals("流入"))
		                	{
		                		String four=sc.next();
			                	String five=sc.next();
			                	five=five.replace("人", "");
			                	int member=Integer.parseInt(five);
			                	int index1=0;
			                	for (int i=0;i<32;i++)
				            	{
				            		if (four.equals(province[i]))
				            		{
				            			index1=i;
				            			break;
				            		}
				            	}
			                	number[index][1]-=member;
			                	number[index1][1]+=member;
			                	number[index1][4]=1;
		                	}
		                	else
		                	{
		                		String four=sc.next();
		                		four=four.replace("人", "");
			                	int member=Integer.parseInt(four);
			                	number[index][0]+=member;
			                	number[index][1]-=member;
			                	number[0][0]+=member;
			                	number[0][1]-=member;
		                	}
		                }
		                else if (second.equals("死亡"))
		                {
		                	String three=sc.next();
		                	three=three.replace("人", "");
		                	int member=Integer.parseInt(three);
		                	number[index][3]+=member;
		                	number[index][0]-=member;
		                	number[0][3]+=member;
		                	number[0][0]-=member;
		                }
		                else if (second.equals("治愈"))
		                {
		                	String three=sc.next();
		                	three=three.replace("人", "");
		                	int member=Integer.parseInt(three);
		                	number[index][2]+=member;
		                	number[index][0]-=member;
		                	number[0][2]+=member;
		                	number[0][0]-=member;
		                }
		                else if (second.equals("排除"))
		                {
		                	sc.next();
		                	String four=sc.next();
	                		four=four.replace("人", "");
		                	int member=Integer.parseInt(four);
		                	number[index][1]-=member;
		                	number[0][1]-=member;
		                }
		            }	                	
		        }
		        sc.close();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}           
        }
        
    	//得到输出结果
        ArrayList<String> result=new ArrayList<String>();
        if (provinceInt.size()==0)
        {
        	if (typeInt.size()==0)
        	{
        		for (int i=0;i<32;i++)
            	{
        			if (number[i][4]==0)
        			{
        				continue;
        			}
        			String mid=province[i]+" 感染患者"+""+number[i][0]+""+"人"+" 疑似患者"+""+number[i][1]+""+"人"
        			+" 治愈"+""+number[i][2]+""+"人"+" 死亡"+""+number[i][3]+""+"人\n";
        			result.add(mid);
            	}
        	}
        	else
        	{
        		for (int i=0;i<32;i++)
            	{
        			if (number[i][4]==0)
        			{
        				continue;
        			}
        			String mid=province[i];
        			for (int j=0;j<typeInt.size();j++)
        			{
        				if (typeInt.get(j)==0)
        				{
        					mid=mid+" 感染患者"+""+number[i][0]+""+"人";
        				}
        				else if (typeInt.get(j)==1)
        				{
        					mid=mid+" 疑似患者"+""+number[i][1]+""+"人";
        				}
        				else if (typeInt.get(j)==2)
        				{
        					mid=mid+" 治愈"+""+number[i][2]+""+"人";
        				}
        				else if (typeInt.get(j)==3)
        				{
        					mid=mid+" 死亡"+""+number[i][3]+""+"人";
        				}
        			}
        			mid+="\n";
        			result.add(mid);
            	}
        	}
        }
        else
        {
        	for (int i=0;i<32;i++)
        	{
        		number[i][4]=0;
        	}
        	for (int i=0;i<provinceInt.size();i++)
        	{
        		number[provinceInt.get(i)][4]=1;
        	}
        	if(typeInt.size()==0)
        	{
        		for (int i=0;i<32;i++)
            	{
        			if (number[i][4]==0)
        			{
        				continue;
        			}
        			String mid=province[i]+" 感染患者"+""+number[i][0]+""+"人"+" 疑似患者"+""+number[i][1]+""+"人"
        			+" 治愈"+""+number[i][2]+""+"人"+" 死亡"+""+number[i][3]+""+"人\n";
        			result.add(mid);
            	}
        	}
        	else
        	{
        		for (int i=0;i<32;i++)
            	{
        			if (number[i][4]==0)
        			{
        				continue;
        			}
        			String mid=province[i];
        			for (int j=0;j<typeInt.size();j++)
        			{
        				if (typeInt.get(j)==0)
        				{
        					mid=mid+" 感染患者"+""+number[i][0]+""+"人";
        				}
        				else if (typeInt.get(j)==1)
        				{
        					mid=mid+" 疑似患者"+""+number[i][1]+""+"人";
        				}
        				else if (typeInt.get(j)==2)
        				{
        					mid=mid+" 治愈"+""+number[i][2]+""+"人";
        				}
        				else if (typeInt.get(j)==3)
        				{
        					mid=mid+" 死亡"+""+number[i][3]+""+"人";
        				}
        			}
        			mid+="\n";
        			result.add(mid);
            	}
        	}
        }
        
        //输出结果到指定文件中
        try {
	        FileOutputStream out = new FileOutputStream(outList);
	        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
	        for (int i=0;i<result.size();i++) {
	        	bw.write(result.get(i));
	        }
	        bw.write("// 该文档并非真实数据，仅供测试使用\n");
	        bw.close();
        }
        catch (Exception e) 
		{
			e.printStackTrace();
		}   
        
    	/*//test
    	System.out.println(logList);
    	System.out.println(outList);
    	System.out.println(dateList);
    	for (int i=0;i<typeInt.size();i++)
    		System.out.print(typeInt.get(i));
    	System.out.println();
    	for (int i=0;i<provinceInt.size();i++)
    		System.out.print(provinceInt.get(i));
    	System.out.println();
    	
    	for (int i=0;i<32;i++)
    	{
    		for (int j=0;j<4;j++)
    		{
    			if(number[i][j]!=0) {
    			System.out.print(i);
    			System.out.print(" ");
    			System.out.print(j);
    			System.out.print(" ");
    			System.out.print(number[i][j]);
    			System.out.println();
    			} 			
    		}
    	}
    	*/
    }
}