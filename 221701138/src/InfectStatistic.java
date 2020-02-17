/**
 * InfectStatistic
 * TODO
 *
 * @author _LMG
 * @version xxx
 * @since xxx
 */
package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;

class InfectStatistic {
	
	//命令接口
    public interface Command{
        public int excute();
    }

    //命令具象化
    public static class CmdCommand implements Command{
    	private final String s;
    	
    	public CmdCommand(String s) {
    		this.s = s;
    	}
    	@Override
    	public int excute() {
    		return Analyze(s);
    	}
    }
    
    //分析命令
    public static int Analyze(String s) {
    	int x = 0;
    	if(s.equals("-log")) x = 1;
    	else if(s.equals("-out")) x = 2;
    	else if(s.equals("-date")) x =3;
    	else if(s.equals("-type")) x = 4;
    	else if(s.equals("-province")) x = 5;
    	return x;
    }  
    
    //ִ运行器
    public static class Runner{
    	private Command command;
    	
    	public Runner(Command command) {
    		this.command = command;
    	}
    	public int Run() {
    		return command.excute();
    	}
    }
    
    //读取文件内容
    public static ArrayList<String> ReadTxt(File file) {
    	ArrayList<String> as = new ArrayList<String>();
    	StringBuilder result = new StringBuilder();
    	try {
    		BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
    		String s = null;
    		while ((s = br.readLine()) != null) {
    			as.add(s);
    		}
    		br.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return as;
    }
    
    public static void main(String[] args) {
    	String cm[] = new String[] {"1","2","3","4","5","6","7","8"};
    	int x = 0;
    	//存取参数
        for(int i=0;i<args.length;i++){
            Command command = new CmdCommand(args[i]);
            Runner runner = new Runner(command);
            x = runner.Run();
            //log
            if(x == 1) {
            	cm[0] = args[i+1];
            }
            //out
            else if(x == 2) {
            	cm[1] = args[i+1];
            }
            //date
            else if(x == 3) {
            	cm[2] = args[i+1];
            }
            //type
            else if(x == 4) {
            	for(int a = i+1;;a++) {
            		if(args[a].equals("ip"))
            			cm[3] = args[a];
            		else if(args[a].equals("sp"))
            			cm[4] = args[a];
            		else if(args[a].equals("cure"))
            			cm[5] = args[a];
            		else if(args[a].equals("dead"))
            			cm[6] = args[a];
            		else break;
            	}
            }
            else if(x == 5) {
            	int c = 7;
            	for(int a = i+1;args[a].isEmpty();a++) {
            		cm[c] = args[a];
            		c++;
            	}
            }
        }
        //ִ开始读写
        ArrayList<String> ls = new ArrayList<String>();
        ArrayList<String> lt = new ArrayList<String>();
        String path = cm[0];
        File file1 = new File(path);
        File templist[] = file1.listFiles();
        
        for(int i = 0 ; i < templist.length;i++){
			if(templist[i].isFile()) {
				ls.add(templist[i].toString());
			}
        }
        /*for(int j = 0 ; j < ls.size();j++){
			System.out.print(ls.get(j)+"\n");
		}*/
        for(String s : ls) {
        	File file2 = new File(s);
        	lt=ReadTxt(file2);
        	for(int j = 0 ; j < lt.size();j++){
    			System.out.print(lt.get(j)+"\n");
    		}
        }
    }
}
