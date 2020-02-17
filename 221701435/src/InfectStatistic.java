import java.io.*;
import java.text.Collator;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * InfectStatistic TODO
 *
 * @author moli
 * @version 1.0.0
 * @since 2020-02-17
 */

public class infectStatistic {

	/**
	 * CommandLine TODO
	 * 
	 * @description 通过内部Command变量存放list命令，Record变量存放命令后跟着的一些数据
	 * @author moli
	 * @version m 1.0.0
	 * @since 2020-02-17
	 */

	static class CommandLine {

		private Command command;
		
		private Record record;

		public Command getCommand() {
			return command;
		}

		public void setCommand(Command command) {
			this.command = command;
		}

		public Record getRecord() {
			return record;
		}

		public void setRecord(Record record) {
			this.record = record;
		}

		public String toString() {
			return command + " -" + record.isLog() + " " + record.getLog_content() + " -" + record.isOut() + " "
					+ record.getOut_content() + " -" + record.isDate() + " " + record.getDate_content() + " -"
					+ record.isType() + " " + record.getType_content() + " -" + record.isProvince() + " "
					+ record.getProvince_content();
		}

		/**
		 * Command TODO
		 * 
		 * @description 定义布尔型变量，判断命令是否为list
		 * @author moli
		 * @version m 1.0.0
		 * @since 2020-02-17
		 */

		static class Command {
			private boolean list;

			public boolean isList() {
				return list;
			}

			public void setList(boolean list) {
				this.list = list;
			}
		}

		/**
		 * Record TODO
		 * 
		 * @description 定义变量存放log，out,date,type,province数据
		 * @author moli
		 * @version m 1.0.0
		 * @since 2020-02-17
		 */

		static class Record {
			private boolean log;

			private String log_content;

			private boolean out;

			private String out_content;

			private boolean date;

			private String date_content;

			private boolean type;

			private ArrayList<TypeOption> type_content;

			private boolean province;

			private ArrayList<String> province_content;

			public boolean isLog() {
				return log;
			}

			public void setLog(boolean log) {
				this.log = log;
			}

			public String getLog_content() {
				return log_content;
			}

			public void setLog_content(String log_content) {
				this.log_content = log_content;
			}

			public boolean isOut() {
				return out;
			}

			public void setOut(boolean out) {
				this.out = out;
			}

			public String getOut_content() {
				return out_content;
			}

			public void setOut_content(String out_content) {
				this.out_content = out_content;
			}

			public boolean isDate() {
				return date;
			}

			public void setDate(boolean date) {
				this.date = date;
			}

			public String getDate_content() {
				return date_content;
			}

			public void setDate_content(String date_content) {
				this.date_content = date_content;
			}

			public boolean isType() {
				return type;
			}

			public void setType(boolean type) {
				this.type = type;
			}

			public ArrayList<TypeOption> getType_content() {
				return type_content;
			}

			public void setType_content(ArrayList<TypeOption> type_content) {
				this.type_content = type_content;
			}

			public boolean isProvince() {
				return province;
			}

			public void setProvince(boolean province) {
				this.province = province;
			}

			public ArrayList<String> getProvince_content() {
				return province_content;
			}

			public void setProvince_content(ArrayList<String> province_content) {
				this.province_content = province_content;
			}
		}

		/**
		 * TypeOptionEnum TODO
		 * 
		 * @description 定义type的四种情况IP,SP,CURE,DEAD
		 * @author moli
		 * @version m 1.0.0
		 * @since 2020-02-17
		 */

		enum TypeOptionEnum {
			IP("ip", 1, false), SP("sp", 2, false), CURE("cure", 3, false), DEAD("dead", 4, false);

			private String name;

			private int index;

			private boolean status;

			private TypeOptionEnum(String name, int index, boolean status) {
				this.name = name;
				this.index = index;
				this.status = status;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public int getIndex() {
				return index;
			}

			public void setIndex(int index) {
				this.index = index;
			}

			public boolean isStatus() {
				return status;
			}

			public void setStatus(boolean status) {
				this.status = status;
			}
		}

		/**
		 * TypeOption TODO
		 * 
		 * @description 将枚举定义的四种类型封装到类中存储
		 * @author moli
		 * @version m 1.0.0
		 * @since 2020-02-17
		 */

		static class TypeOption {
			private String name;

			private int index;

			private boolean status;

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public int getIndex() {
				return index;
			}

			public void setIndex(int index) {
				this.index = index;
			}

			public boolean isStatus() {
				return status;
			}

			public void setStatus(boolean status) {
				this.status = status;
			}

			public TypeOption setTypeOptionIp() {
				CommandLine.TypeOption typeoption = new CommandLine.TypeOption();

				typeoption.setName(TypeOptionEnum.IP.getName());
				typeoption.setIndex(TypeOptionEnum.IP.getIndex());
				TypeOptionEnum.IP.setStatus(true);
				typeoption.setStatus(TypeOptionEnum.IP.isStatus());

				return typeoption;
			}

			public TypeOption setTypeOptionSp() {
				CommandLine.TypeOption typeoption = new CommandLine.TypeOption();

				typeoption.setName(TypeOptionEnum.SP.getName());
				typeoption.setIndex(TypeOptionEnum.SP.getIndex());
				TypeOptionEnum.SP.setStatus(true);
				typeoption.setStatus(TypeOptionEnum.SP.isStatus());

				return typeoption;
			}

			public TypeOption setTypeOptionCure() {
				CommandLine.TypeOption typeoption = new CommandLine.TypeOption();

				typeoption.setName(TypeOptionEnum.CURE.getName());
				typeoption.setIndex(TypeOptionEnum.CURE.getIndex());
				TypeOptionEnum.CURE.setStatus(true);
				typeoption.setStatus(TypeOptionEnum.CURE.isStatus());

				return typeoption;
			}

			public TypeOption setTypeOptionDead() {
				CommandLine.TypeOption typeoption = new CommandLine.TypeOption();

				typeoption.setName(TypeOptionEnum.DEAD.getName());
				typeoption.setIndex(TypeOptionEnum.DEAD.getIndex());
				TypeOptionEnum.DEAD.setStatus(true);
				typeoption.setStatus(TypeOptionEnum.DEAD.isStatus());

				return typeoption;
			}
		}
	}

	/**
	 * CommandLineAnalytic TODO
	 * 
	 * @description 对由main方法得到的list命令行数据进行解析，并存储在CommandLine中
	 * @author moli
	 * @version m 1.0.0
	 * @since 2020-02-17
	 */

	static class CommandLineAnalytic {
		
		/**
		 * @description 解析数据，并存放在CommandLine中
		 * @param list
		 * @return CommandLine
		 */
		public CommandLine Analytic(List<String> list) {
			CommandLine commandline = new CommandLine();
			CommandLine.Command command = new CommandLine.Command();
			CommandLine.Record record = new CommandLine.Record();

			for (int i = 0; i < list.size(); i++) {
				String temp = list.get(i);
				switch (temp) {
				case "list":
					command.setList(true);
					break;
				case "-log":
					record.setLog(true);
					record.setLog_content(list.get(i + 1));
					break;
				case "-out":
					record.setOut(true);
					record.setOut_content(list.get(i + 1));
					break;
				case "-date":
					record.setDate(true);
					record.setDate_content(list.get(i + 1));
					break;
				case "-type":
					record.setType(true);
					ArrayList<CommandLine.TypeOption> arraylist = new ArrayList<CommandLine.TypeOption>();
					for (int j = i + 1; j < list.size(); j++) {
						if (!list.get(j).substring(0, 1).equals("-")) {
							if (list.get(j).equals("ip")) {
								CommandLine.TypeOption typeoption = new CommandLine.TypeOption();
								arraylist.add(typeoption.setTypeOptionIp());
							}
							if (list.get(j).equals("sp")) {
								CommandLine.TypeOption typeoption = new CommandLine.TypeOption();
								arraylist.add(typeoption.setTypeOptionSp());
							}
							if (list.get(j).equals("cure")) {
								CommandLine.TypeOption typeoption = new CommandLine.TypeOption();
								arraylist.add(typeoption.setTypeOptionCure());
							}
							if (list.get(j).equals("dead")) {
								CommandLine.TypeOption typeoption = new CommandLine.TypeOption();
								arraylist.add(typeoption.setTypeOptionDead());
							}
						}
					}
					record.setType_content(arraylist);
					break;
				case "-province":
					record.setProvince(true);
					ArrayList<String> list1 = new ArrayList<String>();
					for (int j = i + 1; j < list.size(); j++) {
						if (!list.get(j).substring(0, 1).equals("-")) {
							list1.add(list.get(j));
						}
					}
					record.setProvince_content(list1);
					break;
				}
			}
			commandline.setCommand(command);
			commandline.setRecord(record);

			return commandline;
		}
	}

	/**
	 * RegexUtil TODO
	 *
	 * @description 定义8个string对应着8种数据的类型，使用正则表达式，分别得到对应的数据，在存入RegexParameter中
	 * @author moli
	 * @version m 1.0.0
	 * @since 2020-02-17
	 */
	
	public static class RegexUtil {
		String type1 = "\\W+ 新增 感染患者 \\d+人";
		String type2 = "\\W+ 新增 疑似患者 \\d+人";
		String type3 = "\\W+ 感染患者 流入 \\W+ \\d+人";
		String type4 = "\\W+ 疑似患者 流入 \\W+ \\d+人";
		String type5 = "\\W+ 死亡 \\d+人";
		String type6 = "\\W+ 治愈 \\d+人";
		String type7 = "\\W+ 疑似患者 确诊感染 \\d+人";
		String type8 = "\\W+ 排除 疑似患者 \\d+人";

		public String getType1() {
			return type1;

		}

		public void setType1(String type1) {
			this.type1 = type1;
		}

		public String getType2() {
			return type2;
		}

		public void setType2(String type2) {
			this.type2 = type2;
		}

		public String getType3() {
			return type3;
		}

		public void setType3(String type3) {
			this.type3 = type3;
		}

		public String getType4() {
			return type4;
		}

		public void setType4(String type4) {
			this.type4 = type4;
		}

		public String getType5() {
			return type5;
		}

		public void setType5(String type5) {
			this.type5 = type5;
		}

		public String getType6() {
			return type6;
		}

		public void setType6(String type6) {
			this.type6 = type6;
		}

		public String getType7() {
			return type7;
		}

		public void setType7(String type7) {
			this.type7 = type7;
		}

		public String getType8() {
			return type8;
		}

		public void setType8(String type8) {
			this.type8 = type8;
		}

		/**
		 * RegexParameter TODO
		 * 
		 * @description 存放通过正则表达式匹配后得到的数据
		 * @author moli
		 * @version m 1.0.0
		 * @since 2020.02.17
		 */
		
		static class RegexParameter {
			String pattern1;
			String pattern2;
			int pattern3;

			public String getPattern1() {
				return pattern1;
			}

			public void setPattern1(String pattern1) {
				this.pattern1 = pattern1;
			}

			public String getPattern2() {
				return pattern2;
			}

			public void setPattern2(String pattern2) {
				this.pattern2 = pattern2;
			}

			public int getPattern3() {
				return pattern3;
			}

			public void setPattern3(int pattern3) {
				this.pattern3 = pattern3;
			}
		}

		public static RegexParameter getAddIp(String text) {
			Pattern pattern1 = Pattern.compile("(.*) 新增");
			Pattern pattern2 = Pattern.compile("感染患者 (.*)人");
			
			return getString(text, pattern1, pattern2);
		}

		public static RegexParameter getAddSp(String text) {
			Pattern pattern1 = Pattern.compile("(.*) 新增");
			Pattern pattern2 = Pattern.compile("疑似患者 (.*)人");
			
			return getString(text, pattern1, pattern2);
		}

		public static RegexParameter getEmptiesIp(String text) {
			Pattern pattern1 = Pattern.compile("(.*) 感染患者");
			Pattern pattern2 = Pattern.compile("流入 (.*) \\d+人");
			Pattern pattern3 = Pattern.compile("\\W+ (.*)人");
			
			return getString(text, pattern1, pattern2, pattern3);
		}

		public static RegexParameter getEmptiesSp(String text) {
			Pattern pattern1 = Pattern.compile("(.*) 疑似患者");
			Pattern pattern2 = Pattern.compile("流入 (.*) \\d+人");
			Pattern pattern3 = Pattern.compile("\\W+ (.*)人");
			
			return getString(text, pattern1, pattern2, pattern3);
		}

		public static RegexParameter getDead(String text) {
			Pattern pattern1 = Pattern.compile("(.*) 死亡");
			Pattern pattern2 = Pattern.compile("死亡 (.*)人");
			
			return getString(text, pattern1, pattern2);
		}

		public static RegexParameter getCure(String text) {
			Pattern pattern1 = Pattern.compile("(.*) 治愈");
			Pattern pattern2 = Pattern.compile("治愈 (.*)人");
			
			return getString(text, pattern1, pattern2);
		}

		public static RegexParameter getSpToIp(String text) {
			Pattern pattern1 = Pattern.compile("(.*) 疑似患者");
			Pattern pattern2 = Pattern.compile("确诊感染 (.*)人");
			
			return getString(text, pattern1, pattern2);
		}

		public static RegexParameter getRemoveSp(String text) {
			Pattern pattern1 = Pattern.compile("(.*) 排除");
			Pattern pattern2 = Pattern.compile("疑似患者 (.*)人");
			
			return getString(text, pattern1, pattern2);
		}

		/**
		 * 
		 * @description 将正则表达式匹配的语句用matcher处理，然后获得第一个子部分，存入RegexParameter
		 * @param text pattern1 pattern2
		 * @return RegexParameter
		 */
		
		private static RegexParameter getString(String text, Pattern pattern1, Pattern pattern2) {
			RegexParameter regexParameter = new RegexParameter();
			Matcher matcher1 = pattern1.matcher(text);
			Matcher matcher2 = pattern2.matcher(text);
			
			if (matcher1.find()) {
				regexParameter.setPattern1(matcher1.group(1));
			}
			if (matcher2.find()) {
				regexParameter.setPattern3(Integer.parseInt(matcher2.group(1)));
			}
			
			return regexParameter;
		}

		/**
		 * 
		 * @description 将正则表达式匹配的语句用matcher处理，然后获得第一个子部分，存入RegexParameter
		 * @param text pattern1 pattern2 pattern3
		 * @return RegexParameter
		 */
		
		private static RegexParameter getString(String text, Pattern pattern1, Pattern pattern2, Pattern pattern3) {
			RegexParameter regexParameter = new RegexParameter();
			Matcher matcher1 = pattern1.matcher(text);
			Matcher matcher2 = pattern2.matcher(text);
			Matcher matcher3 = pattern3.matcher(text);
		
			if (matcher1.find()) {
				regexParameter.setPattern1(matcher1.group(1));
			}
			if (matcher2.find()) {
				regexParameter.setPattern2(matcher2.group(1));
			}
			if (matcher3.find()) {
				regexParameter.setPattern3(Integer.parseInt(matcher3.group(1)));
			}
			
			return regexParameter;
		}
	}
	
        /**
         * LogUtil TODO
         * 
         * @description 对文件中每行处理后的数据进行一个存储
         * @author moli
         * @version m 1.0.0
         * @since 2020-02-17
         */
	
		static class LogUtil {
			
			/**
			 * InfectResult TODO
			 * 
			 * @description 定义一个对象用于存放正则表达式处理后的数据
			 * @author moli
			 * @version m 1.0.0
			 * @since 2020.02.17
			 */
			
			static class InfectResult {
				String province;
				int ip;
				int sp;
				int cure;
				int dead;

				public InfectResult() {
					super();
				}

				public InfectResult(String province, int ip, int sp, int cure, int dead) {
					super();
					this.province = province;
					this.ip = ip;
					this.sp = sp;
					this.cure = cure;
					this.dead = dead;
				}

				public String getProvince() {
					return province;
				}

				public void setProvince(String province) {
					this.province = province;
				}

				public int getIp() {
					return ip;
				}

				public void setIp(int ip) {
					this.ip = ip;
				}

				public int getSp() {
					return sp;
				}

				public void setSp(int sp) {
					this.sp = sp;
				}

				public int getCure() {
					return cure;
				}

				public void setCure(int cure) {
					this.cure = cure;
				}

				public int getDead() {
					return dead;
				}

				public void setDead(int dead) {
					this.dead = dead;
				}

				public String toResultString() {
					return province + " " + "感染患者" + ip + "人" + " " + "疑似患者" + sp + "人" + " " + "治愈" + cure + "人" + " "
							+ "死亡" + dead + "人";
				}

				public String toString() {
					return province + " " + ip + " " + sp + " " + cure + " " + dead;
				}
			}

			/**
			 * ResultUtil TODO
			 * 
			 * @description 将正则表达式得到的RegexParameter对象转换存储于InfectResult中
			 * @author moli
			 * @version m 1.0.0
			 * @since 2020.02.17
			 */
			
			static class ResultUtil {
				public static InfectResult getIpResult(String text) {
					InfectResult infectResult = new InfectResult();
					RegexUtil.RegexParameter regexParameter = new RegexUtil.RegexParameter();
					
					regexParameter = RegexUtil.getAddIp(text);
					infectResult.setProvince(regexParameter.getPattern1());
					infectResult.setIp(regexParameter.getPattern3());
					
					return infectResult;
				}

				public static InfectResult getSpResult(String text) {
					InfectResult infectResult = new InfectResult();
					RegexUtil.RegexParameter regexParameter = new RegexUtil.RegexParameter();
					
					regexParameter = RegexUtil.getAddSp(text);
					infectResult.setProvince(regexParameter.getPattern1());
					infectResult.setSp(regexParameter.getPattern3());
					
					return infectResult;
				}

				public static List<InfectResult> getEmptiesIpResult(String text) {
					List<InfectResult> list = new ArrayList<InfectResult>();
					RegexUtil.RegexParameter regexParameter = new RegexUtil.RegexParameter();
					LogUtil.InfectResult infectResult1 = new LogUtil.InfectResult();
					LogUtil.InfectResult infectResult2 = new LogUtil.InfectResult();
					
					regexParameter = RegexUtil.getEmptiesIp(text);
					infectResult1.setProvince(regexParameter.getPattern1());
					infectResult1.setIp(-regexParameter.getPattern3());
					infectResult2.setProvince(regexParameter.getPattern2());
					infectResult2.setIp(regexParameter.getPattern3());
					list.add(infectResult1);
					list.add(infectResult2);
					
					return list;
				}

				public static List<InfectResult> getEmptiesSpResult(String text) {
					List<InfectResult> list = new ArrayList<InfectResult>();
					RegexUtil.RegexParameter regexParameter = new RegexUtil.RegexParameter();
					LogUtil.InfectResult infectResult1 = new LogUtil.InfectResult();
					LogUtil.InfectResult infectResult2 = new LogUtil.InfectResult();
					
					regexParameter = RegexUtil.getEmptiesSp(text);
					infectResult1.setProvince(regexParameter.getPattern1());
					infectResult1.setSp(-regexParameter.getPattern3());
					infectResult2.setProvince(regexParameter.getPattern2());
					infectResult2.setSp(regexParameter.getPattern3());
					list.add(infectResult1);
					list.add(infectResult2);
					
					return list;
				}

				public static InfectResult getDeadResult(String text) {
					InfectResult infectResult = new InfectResult();
					RegexUtil.RegexParameter regexParameter = new RegexUtil.RegexParameter();
					
					regexParameter = RegexUtil.getDead(text);
					infectResult.setProvince(regexParameter.getPattern1());
					infectResult.setIp(-regexParameter.getPattern3());
					infectResult.setDead(regexParameter.getPattern3());
					
					return infectResult;
				}

				public static InfectResult getCureResult(String text) {
					InfectResult infectResult = new InfectResult();
					RegexUtil.RegexParameter regexParameter = new RegexUtil.RegexParameter();
					
					regexParameter = RegexUtil.getCure(text);
					infectResult.setProvince(regexParameter.getPattern1());
					infectResult.setIp(-regexParameter.getPattern3());
					infectResult.setCure(regexParameter.getPattern3());
					
					return infectResult;
				}

				public static InfectResult getSpToIpResult(String text) {
					InfectResult infectResult = new InfectResult();
					RegexUtil.RegexParameter regexParameter = new RegexUtil.RegexParameter();
					
					regexParameter = RegexUtil.getSpToIp(text);
					infectResult.setProvince(regexParameter.getPattern1());
					infectResult.setSp(-regexParameter.getPattern3());
					infectResult.setIp(regexParameter.getPattern3());
					
					return infectResult;
				}

				public static InfectResult getRemoveSpResult(String text) {
					InfectResult infectResult = new InfectResult();
					RegexUtil.RegexParameter regexParameter = new RegexUtil.RegexParameter();
					
					regexParameter = RegexUtil.getRemoveSp(text);
					infectResult.setProvince(regexParameter.getPattern1());
					infectResult.setSp(-regexParameter.getPattern3());
					
					return infectResult;
				}
			}
			
			/**
			 *ListUtil TODO
			 *
			 * @description 将得到的InfectResult数组，相同省事的合为一个InfectResult,并重写compareTor接口，使其按照省会拼音进行排序
			 * @author ASUS
			 * @version m 1.0.0
			 * @since 2020-02-17
			 */
			
			static class ListUtil {
				
				/**
				 * 
				 * @description 将得到的InfectResult数组，相同省事的进行一个合并，运用flag来处理目录与文件之间合并的不同。
				 * @param a flag
				 * @return List<InfectResult>
				 */
				
				public static List<InfectResult> mergeList(List<InfectResult> a, boolean flag) {
					List<InfectResult> list = new ArrayList<InfectResult>();
					InfectResult r1 = new InfectResult();
					InfectResult r2 = new InfectResult();
					InfectResult r3 = new InfectResult();
					
					r3.setProvince("全国");
					if (a.size() > 0) {
						r1 = a.get(0);
						for (InfectResult aa : a) {
							r3.setIp(r3.getIp() + aa.getIp());
							r3.setSp(r3.getSp() + aa.getSp());
							r3.setCure(r3.getCure() + aa.getCure());
							r3.setDead(r3.getDead() + aa.getDead());
							if (compare(aa, r1)) {
								if (r2.getProvince() != null) {
									r2.setIp(r2.getIp() + aa.getIp());
									r2.setSp(r2.getSp() + aa.getSp());
									r2.setCure(r2.getCure() + aa.getCure());
									r2.setDead(r2.getDead() + aa.getDead());
								} else {
									r2 = r1;
								}
							} else {
								list.add(r2);
								r1 = aa;
								r2 = r1;
							}
						}
						if (!list.contains(r2)) {
							list.add(r2);
						}
						if (flag != true) {
							r3.setIp(r3.getIp() / 2);
							r3.setSp(r3.getSp() / 2);
							r3.setDead(r3.getDead() / 2);
							r3.setCure(r3.getCure() / 2);
						}
						list.add(r3);
					} else {
						r3.setIp(0);
						r3.setSp(0);
						r3.setCure(0);
						r3.setDead(0);
						list.add(r3);
					}
					
					return list;
				}

				public static boolean compare(Object o1, Object o2) {
					InfectResult a = (InfectResult) o1;
					InfectResult a2 = (InfectResult) o2;
					
					if (a.getProvince().equals(a2.getProvince())) {
						return true;
					}
					
					return false;
				}

				public static enum EnumTest {
					全国(1), 安徽(2), 北京(3), 重庆(4), 福建(5), 甘肃(6), 广东(7), 广西(8), 贵州(9), 海南(10), 河北(11), 河南(12), 黑龙江(13), 湖北(14),
					湖南(15), 吉林(16), 江苏(17), 江西(18), 辽宁(19), 内蒙古(20), 宁夏(21), 青海(22), 山东(23), 山西(24), 陕西(25), 上海(26), 四川(27),
					天津(28), 西藏(29), 新疆(30), 云南(31), 浙江(32);
					
					private int value;

					private EnumTest(int value) {
						this.value = value;
					}

					public int getValue() {
						return value;
					}

					public void setValue(int value) {
						this.value = value;
					}
				}

				@SuppressWarnings("rawtypes")
				static class comparator implements Comparator {
					public int compare(Object object, Object object2) {
						InfectResult a = (InfectResult) object;
						InfectResult a2 = (InfectResult) object2;
						
						return ((Integer) EnumTest.valueOf(a.getProvince()).getValue())
								.compareTo(EnumTest.valueOf(a2.getProvince()).getValue());
					}
				}
			}
		}
	
	public static void main(String[] args) throws IOException {
		List<String> list = new ArrayList<String>();

		for (String temp : args) {
			list.add(temp);
		}

		if (list.get(list.size() - 1).equals("-date")) {
			list.add("default");
		} else {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).equals("-date")) {
					if (list.get(i + 1).substring(0, 1).equals("-")) {
						list.add(i + 1, "default");
						break;
					}
				}
			}
		}
	}
}
