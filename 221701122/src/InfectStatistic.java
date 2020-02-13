import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import java.io.File;

/**
 * InfectStatistic TODO
 *
 * @author hy
 * @version 1.0
 * @since 2020.2.8
 */
class InfectStatistic {
	public static final int PROVINCE_CONSTANT = 31;
	boolean[] valid; // 默认false +1全国
	int[][] c;// 整体数据结构 +1全国
	private String c1;
	private String c2;
	private String c3;
	private String[] c4;
	private String[] c5;
	private int cindex_4;
	private int cindex_5;

	InfectStatistic() {
		valid = new boolean[PROVINCE_CONSTANT + 1];// 默认false +1全国
		c = new int[PROVINCE_CONSTANT + 1][4];// 整体数据结构 +1全国
		c1 = null;
		c2 = null;
		c3 = null;
		c4 = new String[4];
		c5 = new String[PROVINCE_CONSTANT];
		cindex_4 = 0;
		cindex_5 = 0;
	}

	public static void main(String[] args) {
		InfectStatistic infectstatistic = new InfectStatistic();
		/*
		 * for(int i=0;i<args.length;i++){
		 * System.out.println("args["+i+"]="+args[i]); }
		 */
		if (args.length == 0) {
			System.out.println("主函数参数不为空");
			return;
		}
		if (infectstatistic.isList(args[0])) {
			infectstatistic.analysisCommand(args);// 命令分析并存储
			infectstatistic.beforeDate(infectstatistic.c3);// 读取log
			infectstatistic.writeDown(args);// 写文件
			return;
		} else {
			System.out
				.println("只接受list命令 相关参数：-log -out -date -type -province");
			System.out.println("暂不支持其他命令");
			return;
		}
	}

	private void analysisCommand(String[] args) {
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
					File file = new File(args[i]);
					if (!file.exists()) {
						System.out.println("-log文件名错误，'" + args[i] + "'文件不存在");
						System.exit(0);
					}
					if (!file.isDirectory()) {
						System.out
							.println("-log文件名错误，'" + args[i] + "'不是一个目录文件");
						System.exit(0);
					}
					c1 = args[i];
					c1 = c1.replace("\\", "/");// 路径符号修改
					// 路径末自动添加'/'
					if (!c1.substring(c1.length() - 1).equals("/")) {
						c1 = c1.concat("/");
					}
				} else if (c == 2) {
					File file = new File(args[i]);
					if (!file.exists()) {
						try {
							file.createNewFile();// 创建日志文件
						} catch (IOException e) {
							// TODO Auto-generated catch block
							System.out.println(
								"-out错误，'" + args[i] + "'不存在并且无法创建该文件");
							e.printStackTrace();
						}
					}
					c2 = args[i];
				} else if (c == 3) {
					c3 = args[i];
				} else if (c == 4) {
					if (args[i].equals("cure") || args[i].equals("dead")
						|| args[i].equals("ip") || args[i].equals("sp")) {
						c4[cindex_4++] = args[i];
					} else {
						System.out.println(
							"输入的-type类型错误，-type不具有'" + args[i] + "'类型");
						System.exit(0);
					}
				} else if (c == 5) {
					try {
						valid[Province.valueOf(args[i]).ordinal()] = true;
					} catch (IllegalArgumentException e) {
						System.out.println(
							"输入的-province类型错误,-province不具有'" + args[i] + "'类型");
						System.exit(0);
					}
					c5[cindex_5++] = args[i];
				}
			}
		}
	}

	private void printParameter() {
		System.out
			.print("// 命令：list -log " + c1 + " -out " + c2 + " -date " + c3);
		if (cindex_4 != 0) {
			System.out.print(" -type");
			for (int i = 0; i < cindex_4; i++) {
				System.out.print(" " + c4[i]);
			}
		}
		if (cindex_5 != 0) {
			System.out.print(" -province");
			for (int i = 0; i < cindex_5; i++) {
				System.out.print(" " + c5[i]);
			}
		}

	}

	void readFile(String filename) throws FileNotFoundException {
		File file = new File(filename);
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
			// 如果是注释则跳过
			if (province.equals("//")) {
				province = s.nextLine();
			} else {
				try {
					pr = Province.valueOf(province);
				} catch (Exception ex) {
					System.out.println("文件格式错误（省份读入异常）");
					System.exit(0);
				}
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
						c[0][1] -= i;
						c[0][0] += i;
					} else {
						System.out
							.println("文件格式错误（省份 疑似患者 后必为\"流入\"或\"确诊感染\"）");
						System.exit(0);
					}
				} else if (second.equals("死亡")) {
					String data = s.next();// 行末
					int i = stringToInt(data);
					c[pr.ordinal()][0] -= i;// 感染患者减少
					c[pr.ordinal()][3] += i;// 死亡人数增加
					c[0][0] -= i;
					c[0][3] += i;
				} else if (second.equals("治愈")) {
					String data = s.next();// 行末
					int i = stringToInt(data);
					c[pr.ordinal()][0] -= i;// 感染患者减少
					c[pr.ordinal()][2] += i;// 治愈人数增加
					c[0][0] -= i;
					c[0][2] += i;
				} else if (second.equals("排除")) {
					String third = s.next();
					if (third.equals("疑似患者")) {
						String data = s.next();// 行末
						int i = stringToInt(data);
						c[pr.ordinal()][1] -= i;// 疑似患者减少
						c[0][1] -= i;
					} else {
						System.out.println("文件格式错误（省份 排除 后必为\"疑似患者\"）");
						System.exit(0);
					}
				} else {
					System.out.println("文件格式错误（省份后第一个参数错误）");
					System.exit(0);
				}
			}
		}
		s.close();
	}

	void beforeDate(String date) {
		File file = new File(c1);
		File[] filelist = file.listFiles(pathname -> {
			if (pathname.isFile())
				return true;
			else
				return false;
		});
		if (filelist.length == 0) {
			System.out.println("'" + c1 + "'目录不具有文件");
			System.exit(0);
		}
		boolean dir_have_before_file = false;
		for (File i : filelist) {
			String filename = i.getName();
			String suffix = filename.substring(filename.indexOf(".") + 1);
			if (suffix.equals("log.txt")) {
				// 确认为日志文件
				String logfilename = filename.substring(0,
					filename.indexOf("."));
				// 处理时间早于date的文件
				if (date.compareTo(logfilename) >= 0) {
					dir_have_before_file = true;
					// 文件名转为绝对地址
					String result = c1.concat(filename);
					// 对于日志文件的处理
					try {
						readFile(result);// 打开一个文件并处理相关数据
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("can't find " + result);
					}
				}
			}
		}
		if (!dir_have_before_file) {
			System.out.println("'" + c1 + "'" + "目录下没有时间早于'" + date + "'的日志文件");
			System.exit(0);
		}
	}

	void writeDown(String[] args) {
		/*
		 * try { FileWriter filewriter = new FileWriter(file.getName()); } catch
		 * (IOException e1) { // TODO Auto-generated catch block
		 * e1.printStackTrace(); }
		 */
		/*
		 * 使用true，即进行append file try { FileWriter filewritter = new
		 * FileWriter(file.getName(),true); } catch (IOException e1) { // TODO
		 * Auto-generated catch block e1.printStackTrace(); }
		 */
		/*
		 * System.out .println(c[0][0] + "," + c[0][1] + "," + c[0][2] + "," +
		 * c[0][3]); Province pr = Province.valueOf("福建");
		 * System.out.println(c[pr.ordinal()][0] + "," + c[pr.ordinal()][1] +
		 * "," + c[pr.ordinal()][2] + "," + c[pr.ordinal()][3]); pr =
		 * Province.valueOf("湖北"); System.out.println(c[pr.ordinal()][0] + "," +
		 * c[pr.ordinal()][1] + "," + c[pr.ordinal()][2] + "," +
		 * c[pr.ordinal()][3]);
		 */
		/*
		 * for (int i = 0; i < c4.length; i++) { if (c4[i] != null)
		 * System.out.println(i); } /* for(int i=0;i<c5.length;i++) {
		 * if(c5[i]!=null)System.out.println(i); }
		 */
		try {
			PrintStream print = new PrintStream(c2);// 这里偷懒了，可以使用上面的输出流
			System.setOut(print);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // 写好输出位置文件；
		for (Province e : Province.values()) {
			if (cindex_5 != 0) {
				// 给定province，输出合法省份
				if (valid[e.ordinal()]) {
					System.out.print(e);
					if (cindex_4 != 0) {
						// 给定类型
						for (int i = 0; i < cindex_4; i++) {
							if (c4[i].equals("ip")) {
								System.out.print(
									" " + "感染患者" + c[e.ordinal()][0] + "人");
							} else if (c4[i].equals("sp")) {
								System.out.print(
									" " + "疑似患者" + c[e.ordinal()][1] + "人");
							} else if (c4[i].equals("cure")) {
								System.out.print(
									" " + "治愈" + c[e.ordinal()][2] + "人");
							} else if (c4[i].equals("dead")) {
								System.out.print(
									" " + "死亡" + c[e.ordinal()][3] + "人");
							} else {
								System.out.println("在输出时遇到type类型值的错误问题，type不具有'"
									+ c4[i] + "'类型");
								System.exit(0);
							}
						}
					} else {
						// 没有给定类型，默认输出方式
						System.out.print(" " + "感染患者" + c[e.ordinal()][0]
							+ "人" + " " + "疑似患者" + c[e.ordinal()][1] + "人" + " "
							+ "治愈" + c[e.ordinal()][2] + "人" + " " + "死亡"
							+ c[e.ordinal()][3] + "人");
					}
					System.out.print("\n");
				}
			} else {
				// 没有给定province,默认输出含有信息的省份
				if (c[e.ordinal()][0] != 0 || c[e.ordinal()][1] != 0
					|| c[e.ordinal()][2] != 0 || c[e.ordinal()][3] != 0) {
					System.out.print(e);
					if (cindex_4 != 0) {
						// 没有给定
						for (int i = 0; i < cindex_4; i++) {
							if (c4[i].equals("ip")) {
								System.out.print(
									" " + "感染患者" + c[e.ordinal()][0] + "人");
							} else if (c4[i].equals("sp")) {
								System.out.print(
									" " + "疑似患者" + c[e.ordinal()][1] + "人");
							} else if (c4[i].equals("cure")) {
								System.out.print(
									" " + "治愈" + c[e.ordinal()][2] + "人");
							} else if (c4[i].equals("dead")) {
								System.out.print(
									" " + "死亡" + c[e.ordinal()][3] + "人");
							} else {
								System.out.println("在输出时遇到type类型值的错误问题，type不具有'"
									+ c4[i] + "'类型");
								System.exit(0);
							}
						}
					} else {
						// 没有给定类型，默认输出方式
						System.out.print(" " + "感染患者" + c[e.ordinal()][0]
							+ "人" + " " + "疑似患者" + c[e.ordinal()][1] + "人" + " "
							+ "治愈" + c[e.ordinal()][2] + "人" + " " + "死亡"
							+ c[e.ordinal()][3] + "人");
					}
					System.out.print("\n");
				}
			}
		}
		System.out.println("// 该文档并非真实数据，仅供测试使用");
		printParameter();
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
	全国, 安徽, 北京, 重庆, 福建, 甘肃, 广东, 广西, 贵州, 海南, 河北, 河南, 黑龙江, 湖北, 湖南, 吉林, 江苏, 江西, 辽宁,
	内蒙古, 宁夏, 青海, 山东, 山西, 陕西, 上海, 四川, 天津, 西藏, 新疆, 云南, 浙江;
}
