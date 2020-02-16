import java.util.ArrayList;

/**
 * InfectStatistic
 * TODO
 *
 * @author 221701312 张庭博
 * @version 1.1
 * @since 2020-02-11
 */
class InfectStatistic {
    public static void main(String[] args)
    {
        CommandIdentity cmdit=new CommandIdentity(args);
        //cmdit.PrintCommand();
        cmdit.Identify();
    }
}
class FileManager{
    public FileManager(){

    }
}
class CommandIdentity{
    public String[] command;
    public CommandIdentity(String[] x){
        command = x;
    }
    public void PrintCommand(){
        /*for(String s:command){
            System.out.print(s);
        }*/
        for(int i=0;i< command.length;i++)
            System.out.print(command[i]);
        System.out.println();
    }
    public void Identify(){
        int logpos=0,outpos=0,datepos=0,typepos=0,provincepos=0;
        ArrayList<String > typeparam=new ArrayList<String>(50);
        ArrayList<String > provinceparam = new ArrayList<String>(50);
        //String[] typeparam=new String[50],provinceparam=new String[50];
        String logparam,outparam,dateparam;
        if(!command[0].equals("list"))
            System.out.println("命令格式错误！");
        else{
            //log命令位置获取和参数
            for(int i=0;i<command.length;i++) {
                if (command[i].equals("-log")) {
                    logpos = i;
                    break;
                }
            }
            logparam=command[logpos+1];
            //out命令位置获取和参数
            for(int i=0;i<command.length;i++) {
                if(command[i].equals("-out")) {
                    outpos = i;
                    break;
                }
            }
            outparam=command[outpos+1];
            //date命令位置获取和参数
            for(int i=0;i<command.length;i++) {
                if (command[i].equals("-date")) {
                    datepos = i;
                    break;
                }
            }
            if(command[datepos+1].startsWith("-"))
                dateparam="";
            else
                dateparam=command[datepos+1];
            //type命令位置获取和参数
            for(int i=0;i<command.length;i++) {
                if (command[i].equals("-type")) {
                    typepos = i;
                    break;
                }
            }
            for(int i=typepos+1;i<command.length;i++){
                if(command[i].startsWith("-"))
                    break;
                else
                    typeparam.add(command[i]);
            }
            //province命令位置获取和参数
            for(int i=0;i<command.length;i++) {
                if (command[i].equals("-province")) {
                    provincepos = i;
                    break;
                }
            }
            for(int i=provincepos+1;i<command.length;i++){
                if(command[i].startsWith("-"))
                    break;
                else
                    provinceparam.add(command[i]);
            }
            System.out.printf("-log的位置为：%d  参数为：%s\n",logpos,logparam);
            System.out.printf("-out的位置为：%d  参数为：%s\n",outpos,outparam);
            System.out.printf("-date的位置为：%d  参数为：%s\n",datepos,dateparam);
            System.out.printf("-type的位置为：%d  参数为：",typepos);
            for(String s:typeparam)
                System.out.print(s + " ");
            System.out.println();
            System.out.printf("-province的位置为：%d  参数为：",provincepos);
            for(String s:provinceparam)
                System.out.print(s + " ");
        }
    }
}
