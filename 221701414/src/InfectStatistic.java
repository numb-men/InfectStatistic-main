import java.io.FileNotFoundException;

/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
class InfectStatistic {
    public static void main(String[] args){
        String cmdLine = "list -log D:\\log\\ -out D:\\ListOut1.txt -date 2020-01-22";
        args = cmdLine.split(" ");
        Lib lib=new Lib(args);
        try{
            lib.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
//        for(String key:lib.getArgMap().keySet()){
//            System.out.println(key);
//            for(String value:lib.getArgMap().get(key)){
//                System.out.println(value);
//            }
//        }
    }
}
