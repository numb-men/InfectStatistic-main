/**
 * InfectStatistic
 * TODO
 *
 * @author 爱学习的水先生
 * @version 1.0
 * @since 2020/2/11
 */


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 抽象命令接口，用于命令模式的设计
 */
interface Command
{
	public void execute();
}

/*
 * 请求接收者类,类方法用于执行各种命令
 */
class ReceiveCommand
{
	public void listDate()
	{
		System.out.println("接受到listDate命令，正在执行中......");
	}
}

/*
 * 命令发送者控制器类，用于封装命令
 */
class SendCommandController
{
	private Command list;
	
	public SendCommandController(Command list) 
	{
		this.list = list;
	}
	
	public void list()
	{
		list.execute();
	}
}

/*
 * List命令实体类，调用ReceiveCommand命令接收者类的命令执行方法
 */
class List implements Command
{
	private ReceiveCommand rc;
	
	public List() 
	{
		this.rc = new ReceiveCommand();
	}
	
	/*********************
	 * 执行不同选项的list命令，switch选择
	 ********************/
	public void execute()
	{
		rc.listDate();//////////////////测试用
	}
}



public class InfectStatistic 
{	
	
	public static void main(String[] args) 
	{
		
		Command list;//创建命令
		list = new List();//实例化
		CommandAnalyze commandAnalyze = new CommandAnalyze(args);//创建命令解析对象
		
		SendCommandController sendCommandController = new SendCommandController(list);//创建并初始化控制器
		
		commandAnalyze.commandAnalysis(sendCommandController);//命令解析
		
	}

}
