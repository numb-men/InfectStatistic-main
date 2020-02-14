/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
class InfectStatistic {
    public static void main(String[] args)
    {
        People a=new People();
        Read b=new Read("E:\\github\\InfectStatistic-main\\221701434\\log\\",a);
        Write c=new Write("E:\\github\\InfectStatistic-main\\221701434\\123.txt",a);
        b.getFiles();
        c.Writealltxt();
    }
}
