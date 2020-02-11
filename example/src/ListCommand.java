
public class ListCommand implements Command{
	public String[] args;
	
	//使用字符串构造ListCommand命令
	public ListCommand(String argsStr) {
		
	}
	
	//获取某个参数的具体值
	public String getKeyVal(String key) {
		return "";
	}
	
	//判断是否为合法的命令格式
	public boolean isTrueCommand() {
		return false;
	}
	
	//执行命令
	public void execute() {
		
	}
}
