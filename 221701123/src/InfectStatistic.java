import java.util.Scanner;



/**
 * InfectStatistic
 * TODO
 *
 * @author Hkb
 * @version xxx
 * @since 2020.2.10
 */
class InfectStatistic {
    public static void main(String[] args) {
    	new InfectStatistic().run();
    }
    
    public void run() {
    	Scanner fin=new Scanner(System.in);
    	String argsStr=fin.nextLine();
    	Lib.Invoker invoker=new Lib.Invoker();
    	
    	Lib.ListCommand list=new Lib.ListCommand(argsStr);
    	
    	//Invoker invoker=new Invoker();
    	invoker.takeCommand(list);
    	
    }
}
