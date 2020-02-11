import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.File;

/**
 * InfectStatistic TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
class InfectStatistic {

	int[][] c = new int[23][4];// 整体数据结构
	private String c1 = null;
	private String c2 = null;
	private String c3 = null;
	private String[] c4 = new String[4];
	private String[] c5 = new String[23];
	private int cindex_4 = 0;
	private int cindex_5 = 0;

	public static void main(String[] args) {
		InfectStatistic infectstatistic = new InfectStatistic();
		/*
		 * for(int i=0;i<args.length;i++){ System.out.println("args["+i+"]="+args[i]); }
		 */
		if (infectstatistic.isList(args[0])) {
			infectstatistic.beginAnalysis(args);
			try {
				infectstatistic.readFile();// 打开一个文件并处理相关数据
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("can't find log file");
			}
			return;
		} else {
			return;
		}
	}

	private void beginAnalysis(String[] args) {
		// TODO Auto-generated method stub
		int c = 0;
		for (int i = 1; i < args.length; i++) {
			if (isCommand(args[i])) {
				c = 0;
				if (isLog(args[i])) {
					c = 1;
				} else if (isOut(args[i])) {
					c = 2;
				} else if (isDate(args[i])) {
					c = 3;
				} else if (isType(args[i])) {
					c = 4;
				} else if (isProvince(args[i])) {
					c = 5;
				}
			} else {
				if (c == 1) {
					c1 = args[i];
				} else if (c == 2) {
					c2 = args[i];
				} else if (c == 3) {
					c3 = args[i];
				} else if (c == 4) {
					c4[cindex_4++] = args[i];
				} else if (c == 5) {
					c5[cindex_5++] = args[i];
				}
			}
		}
		printParameter();

	}

	private void printParameter() {
		System.out.println(c1);
		System.out.println(c2);
		System.out.println(c3);
		for (int i = 0; i < cindex_4; i++) {
			System.out.println(c4[i]);
		}
		for (int i = 0; i < cindex_5; i++) {
			System.out.println(c5[i]);
		}
	}

	void readFile() throws FileNotFoundException {
		File file = new File("C:/Users/hc201804/Desktop/git工作目录/InfectStatistic-main/221701122/log/2020-01-22.log.txt");
		if (!file.exists()) {
			System.out.println("节点基本信息文件未找到");
			System.exit(0);
		}
		Scanner s = new Scanner(file);
		while (s.hasNext()) {
			String province = s.next();
			// int num=s.nextInt();
			// String de = s.next();
			Province pr = null;
			try {
				pr = Province.valueOf(province);
			} catch (Exception ex) {
				System.out.println("文件格式错误（省份读入异常）");
				System.exit(0);
			}
			System.out.println(pr);// 获取当前的省份
			/*
			 * c[pr.ordinal()][1]++; System.out.println(c[1][1]);
			 */
			String second = s.next();
			if (second.equals("新增")) {
				String third = s.next();
				if (third.equals("感染患者")) {
					String data = s.next();
					int i = stringToInt(data);
					c[pr.ordinal()][0] += i;// 省份新增感染患者
					c[0][0] += i;// 全国新增感染患者
				} else if (third.equals("疑似患者")) {
					String data = s.next();// 行末
					int i = stringToInt(data);
					c[pr.ordinal()][1] += i;// 省份新增疑似患者
					c[0][1] += i;// 全国新增疑似患者
				} else {
					System.out.println("文件格式错误（新增患者仅有感染患者与疑似患者两种）");
					System.exit(0);
				}
			} else if (second.equals("感染患者")) {
				String third = s.next();
				if (third.equals("流入")) {
					String next_province = s.next();
					Province next_pr = null;
					try {
						next_pr = Province.valueOf(next_province);
					} catch (Exception ex) {
						System.out.println("文件格式错误（感染患者流入省份读入异常）");
						System.exit(0);
					}
					String data = s.next();// 行末
					int i = stringToInt(data);
					c[pr.ordinal()][0] -= i; // 流出的省份減少
					c[next_pr.ordinal()][0] += i;// 流入的省份增加
				} else {
					System.out.println("文件格式错误（省份 感染患者 后必为\"流入\"）");
					System.exit(0);
				}
			} else if (second.equals("疑似患者")) {
				String third = s.next();
				if (third.equals("流入")) {
					String next_province = s.next();
					Province next_pr = null;
					try {
						next_pr = Province.valueOf(next_province);
					} catch (Exception ex) {
						System.out.println("文件格式错误（疑似患者流入省份读入异常）");
						System.exit(0);
					}
					String data = s.next();// 行末
					int i = stringToInt(data);
					c[pr.ordinal()][1] -= i; // 流出的省份減少
					c[next_pr.ordinal()][1] += i;// 流入的省份增加
				} else if (third.equals("确诊感染")) {
					String data = s.next();// 行末
					int i = stringToInt(data);
					c[pr.ordinal()][1] -= i;// 疑似患者减少
					c[pr.ordinal()][0] += i;// 感染患者增加
				} else {
					System.out.println("文件格式错误（省份 疑似患者 后必为\"流入\"或\"确诊感染\"）");
					System.exit(0);
				}
			} else if (second.equals("死亡")) {
				String data = s.next();// 行末
				int i = stringToInt(data);
				c[pr.ordinal()][0] -= i;// 感染患者减少
				c[pr.ordinal()][3] += i;// 死亡人数增加
			} else if (second.equals("治愈")) {
				String data = s.next();// 行末
				int i = stringToInt(data);
				c[pr.ordinal()][0] -= i;// 感染患者减少
				c[pr.ordinal()][2] += i;// 治愈人数增加
			} else if (second.equals("排除")) {
				String third = s.next();
				if (third.equals("疑似患者")) {
					String data = s.next();// 行末
					int i = stringToInt(data);
					c[pr.ordinal()][1] -= i;// 疑似患者减少
				} else {
					System.out.println("文件格式错误（省份 排除 后必为\"疑似患者\"）");
					System.exit(0);
				}
			} else {
				System.out.println("文件格式错误（省份后第一个参数错误）");
				System.exit(0);
			}
		}
		s.close();
	}

	
	boolean isCommand(String args) {
		if (args.charAt(0) == '-') {
			return true;
		}
		return false;
	}

	boolean isList(String args) {
		if (args.equals("list")) {
			return true;
		}
		return false;
	}

	boolean isLog(String args) {
		if (args.equals("-log")) {
			return true;
		}
		return false;
	}

	boolean isOut(String args) {
		if (args.equals("-out")) {
			return true;
		}
		return false;
	}

	boolean isDate(String args) {
		if (args.equals("-date")) {
			return true;
		}
		return false;
	}

	boolean isType(String args) {
		if (args.equals("-type")) {
			return true;
		}
		return false;
	}

	boolean isProvince(String args) {
		if (args.equals("-province")) {
			return true;
		}
		return false;
	}

	// 将例如String 2人转为int 2
	int stringToInt(String data) {
		int i = 0;
		try {
			data = data.substring(0, data.length() - 1);
			i = Integer.parseInt(data);
		} catch (NumberFormatException ex) {
			System.out.println("人数格式错误");
			System.exit(0);
		}
		return i;
	}

}

enum Province {
	全国, 福建, 河北, 山西, 辽宁, 吉林, 黑龙江, 浙江, 江苏, 安徽, 江西, 山东, 河南, 湖北, 湖南, 广东, 海南, 四川, 贵州, 云南, 陕西, 甘肃, 青海, 台湾;
}
