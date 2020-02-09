/**
 * InfectStatistic TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
class InfectStatistic {
	public static void main(String[] args) {
		/*
		 * for(int i=0;i<args.length;i++){ System.out.println("args["+i+"]="+args[i]); }
		 */
		if (args[0].equals("list")) {
			begin_analysis(args);
			return;
		} else {
			return;
		}

	}

	private static void begin_analysis(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("www");
		
	}

	boolean is_date(String args) {
		if (args == "-date")
			return true;
		return false;
	}

	boolean is_log(String args) {
		if (args == "-log")
			return true;
		return false;
	}

	boolean is_out(String args) {
		if (args == "-out")
			return true;
		return false;
	}

	boolean is_type(String args) {
		if (args == "-type")
			return true;
		return false;
	}

	boolean is_province(String args) {
		if (args == "-province")
			return true;
		return false;
	}

}
