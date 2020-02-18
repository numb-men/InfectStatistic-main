import java.text.SimpleDateFormat;
import java.util.Date;

public class Lib {

	public static void main(String argsp[]) throws Exception{
	    String time="2010-11-20";
	    Date date=null;
	    SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
	    date=formatter.parse(time);
	    System.out.println(date);
	}
}
