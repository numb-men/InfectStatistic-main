import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * InfectStatistic TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
class InfectStatistic {

	public static ArrayList<String> getText(File file) {
		ArrayList<String> list = new ArrayList<String>();
		try {
			BufferedReader bReader = new BufferedReader(new FileReader(file));
			String s = null;
			while ((s = bReader.readLine()) != null) {
				if (s.substring(0, 2).equals("//")) {
					continue;
				} else {
					if (!s.equals("")) {
						list.add(s);
					}
				}
			}
			bReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static ArrayList<String> getFiles(String path) {
		ArrayList<String> files = new ArrayList<String>();
		File file = new File(path);
		File[] tempList = file.listFiles();

		for (int i = 0; i < tempList.length; i++) {
			if (tempList[i].isFile()) {

				files.add(tempList[i].toString());
			}
			if (tempList[i].isDirectory()) {
				System.out.println("文件夹：" + tempList[i]);
			}
		}
		return files;
	}

//	写出文件
	public static void writeTxt(String txtPath,String content){    
	       FileOutputStream fileOutputStream = null;
	       File file = new File(txtPath);
	       try {
	           if(file.exists()){
	               //判断文件是否存在，如果不存在就新建一个txt
	               file.createNewFile();
	           }
	           fileOutputStream = new FileOutputStream(file);
	           fileOutputStream.write(content.getBytes());
	           fileOutputStream.flush();
	           fileOutputStream.close();
	       } catch (Exception e) {
	           e.printStackTrace();
	       }
	    }
	
	public static void main(String[] args) {

		String list = "";
		String log = "";
		String out = "";
		String date = "";
		String type = "";
		List<String> province = new ArrayList<String>();

		String cmd = "//命令：list";
		
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("list")) {
				list = "ok";
			} else if (args[i].equals("-log")) {
				log = args[i + 1];
				cmd += " -log "+log;
			} else if (args[i].equals("-out")) {
				out = args[i + 1];
				cmd += " -out "+out;
			} else if (args[i].equals("-date")) {
				date = args[i + 1];
				cmd += " -date "+date;
			} else if (args[i].equals("-type")) {
				type = args[i + 1];
				cmd += " -type "+type;
			} else if (args[i].equals("-province")) {
				
				for (int j = args.length - 1; j > i; j--) {
					province.add(args[j]);
				}
				
				String data = "";
				cmd += " -province ";
				for (int j = 0; j < province.size(); j++) {
					data += province.get(j).toString()+" ";
				}
				cmd += data;
			}
		}

		if (list.equals("") || log.equals("") || out.equals("")) {
			System.out.println("Error,eg:java InfectStatistic list -log D:/log/ -out D:/output.txt");
		} else {
			ArrayList<String> fileName = getFiles(log);
			List<String> allList = new ArrayList<String>();
			for (int i = 0; i < fileName.size(); i++) {
				try {
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					String fileNamaDate = fileName.get(i).toString().split("\\\\")[2].split("\\.")[0];
					if(date.equals("")) {
						date = df.format(new Date());
					}
					long date1 = df.parse(date).getTime();
					long date2 = df.parse(fileNamaDate).getTime();
					if (date1 >= date2) {
						ArrayList<String> inf = getText(new File("./log/"+fileNamaDate+".log.txt"));
						for (int j = 0; j < inf.size(); j++) {
							allList.add(inf.get(j));
						}
					}else {
						continue;
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			List<String> siteList = new ArrayList<String>();
			for (int i = 0; i < allList.size(); i++) {
				boolean isExistSite = true;
				if(siteList.size() == 0) {
					siteList.add(allList.get(i).split(" ")[0]);
				}else {
					for (int j = 0; j < siteList.size(); j++) {
						if(allList.get(i).split(" ")[0].toString().equals(siteList.get(j).toString())) {
							isExistSite = true;
							break;
						}else {
							isExistSite = false;
						}
					}
				}
				if (!isExistSite) {
					siteList.add(allList.get(i).split(" ")[0]);
				}
			}
			
			Integer allGanran = 0;
			Integer allYisi = 0;
			Integer allZhiyu = 0;
			Integer allSiwang = 0;
			
			ArrayList<String> endResult = new ArrayList<String>();
			
			for (int i = 0; i < siteList.size(); i++) {
				
				Integer ganran = 0;
				Integer yisi = 0;
				Integer zhiyu = 0;
				Integer siwang = 0;
				
				String site = siteList.get(i).toString();
				
				for (int j = 0; j < allList.size(); j++) {
					if(site.equals(allList.get(j).split(" ")[0])) {
						if(allList.get(j).split(" ").length == 3) {
							if(allList.get(j).split(" ")[1].equals("死亡")) {
								String countString = allList.get(j).split(" ")[2];
								Integer count = Integer.parseInt(countString.substring(0, countString.length()-1));
								siwang += count;
								ganran -= count;
							}else {
								String countString = allList.get(j).split(" ")[2];
								Integer count = Integer.parseInt(countString.substring(0, countString.length()-1));
								zhiyu += count;
								ganran -= count;
							}
						}else if (allList.get(j).split(" ").length == 4) {
							if(allList.get(j).split(" ")[1].equals("新增") && allList.get(j).split(" ")[2].equals("感染患者")) {
								String countString = allList.get(j).split(" ")[3];
								Integer count = Integer.parseInt(countString.substring(0, countString.length()-1));
								ganran += count;
							}else if (allList.get(j).split(" ")[1].equals("新增") && allList.get(j).split(" ")[2].equals("疑似患者")) {
								String countString = allList.get(j).split(" ")[3];
								Integer count = Integer.parseInt(countString.substring(0, countString.length()-1));
								yisi += count;
							}else if (allList.get(j).split(" ")[1].equals("疑似患者") && allList.get(j).split(" ")[2].equals("确诊感染")) {
								String countString = allList.get(j).split(" ")[3];
								Integer count = Integer.parseInt(countString.substring(0, countString.length()-1));
								ganran += count;
								yisi -= count;
							}else if (allList.get(j).split(" ")[1].equals("排除") && allList.get(j).split(" ")[2].equals("疑似患者")) {
								String countString = allList.get(j).split(" ")[3];
								Integer count = Integer.parseInt(countString.substring(0, countString.length()-1));
								yisi -= count;
							}
						}else if (allList.get(j).split(" ").length == 5) {
							if(allList.get(j).split(" ")[1].equals("感染患者")) {
								String countString = allList.get(j).split(" ")[4];
								Integer count = Integer.parseInt(countString.substring(0, countString.length()-1));
								ganran -= count;
							}else if (allList.get(j).split(" ")[1].equals("疑似患者")) {
								String countString = allList.get(j).split(" ")[4];
								Integer count = Integer.parseInt(countString.substring(0, countString.length()-1));
								yisi -= count;
							}
						}
					}else {
						if(allList.get(j).split(" ")[1].equals("感染患者") && site.equals(allList.get(j).split(" ")[3])) {
							String countString = allList.get(j).split(" ")[4];
							Integer count = Integer.parseInt(countString.substring(0, countString.length()-1));
							ganran += count;
						}else if(allList.get(j).split(" ")[1].equals("疑似患者") && site.equals(allList.get(j).split(" ")[3])){
							String countString = allList.get(j).split(" ")[4];
							Integer count = Integer.parseInt(countString.substring(0, countString.length()-1));
							yisi += count;
						}
					}
					
				}
				
				endResult.add(site + " 感染患者" + ganran.toString() + " 疑似患者" + yisi.toString() + " 治愈" + zhiyu.toString() + " 死亡" + siwang.toString() + "人");
				
				allGanran += ganran;
				allYisi += yisi;
				allZhiyu += zhiyu;
				allSiwang += siwang;
				
			}
		
			
			endResult.add(0,"全国 感染患者" + allGanran.toString() + " 疑似患者" + allYisi.toString() + " 治愈" + allZhiyu + " 死亡" + allSiwang + "人");
			
			String outText = "";
			for (int i = 0; i < endResult.size(); i++) {
				if(province.size() <= 0) {
					outText += endResult.get(i).toString() + System.lineSeparator();
				}else {
					for (int k = 0; k < province.size(); k++) {
						if(endResult.get(i).toString().split(" ")[0].equals(province.get(k).toString())) {
							outText += endResult.get(i).toString() + System.lineSeparator();
						}
					}
				}
			}

			outText += "// 该文档并非真实数据，仅供测试使用" + System.lineSeparator();
			outText += cmd;
			System.out.println(outText);
			writeTxt(out, outText);
			System.out.println("文件写出成功，保存路径为" + out);
			
		}
		
		

	}
}
