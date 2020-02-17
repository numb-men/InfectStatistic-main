import java.io.*;
import java.text.Collator;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
public class infectStatistic {
	
	/**
	 * CommandLine TODO
	 * 
	 * @description 通过内部Command变量存放list命令，Record变量存放命令后跟着的一些数据
	 * @author moli
	 * @version m 1.0.0
	 * @see Command,Record,toString
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
		 * @see
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
		 * @see TypeOption TypeOptionEnum
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
		 * @descreption 定义type的四种情况IP,SP,CURE,DEAD
		 * @author moli
		 * @version m 1.0.0
		 * @see 
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
		 * @see setTypeOptionIp,setTypeOptionSp,setTypeOptionCure,setTypeOptionDead
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

