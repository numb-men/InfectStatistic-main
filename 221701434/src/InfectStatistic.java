/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
class InfectStatistic {
    public static void main(String[] args) {
        //People a=new People();
        //  Read b=new Read("E:\\github\\InfectStatistic-main\\221701434\\log\\",a);
        // Write c=new Write("E:\\github\\InfectStatistic-main\\221701434\\123.txt",a);
        //b.getFiles();
        //c.Writealltxt();
       Cmdlist a=new Cmdlist(args);
       boolean go=true;
       go=a.isright();
       if(go==false)
           return;
       go=a.isrighttype();
        if(go==false)
            return;
        go=a.isrightprovince();
        if(go==false)
            return;
        Read b=new Read(a.main_Path,a.here,a.date);


    }
}
