/**
 * InfectStatistic
 * TODO
 *
 * @author 221701312 张庭博
 * @version 1.0
 * @since 2020-02-11
 */
class InfectStatistic {
    public static void main(String[] args)
    {
        CommandIdentity cmdit=new CommandIdentity(args);
        cmdit.PrintCommand();
    }
}
class FileManager{
    public FileManager(){

    }
}
class CommandIdentity{
    public String[] command;
    public CommandIdentify(String[] x){
        command = x;
    }
    public void PrintCommand(){
        for(String s:command){
            System.out.print(s);
        }
    }
}
