/**
 * InfectStatistic TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
class InfectStatistic {

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
			infectstatistic.runNing();
			return;
		} else {
			return;
		}
	}

	private void beginAnalysis(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("www");
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

	void runNing() {

	}

}
