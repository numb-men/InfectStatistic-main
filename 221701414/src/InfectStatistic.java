
import java.io.FileNotFoundException;

/**
 * InfectStatistic
 *
 *说明：这个类仅作为运行类，所有的相关实现在其他地方
 *
 * @author 黎家泽
 * @version 1.0
 * @since 2020年2月6日15:28:23
 */
class InfectStatistic {
    public static void main(String[] args) {
//        String cmdLine = "list -log D:\\log\\ -out D:\\ListOut7.txt -date 2020-01-23 -type cure dead ip -province 全国 浙江 福建";
//        args = cmdLine.split(" ");
        Lib lib = new Lib(args);
        try {
            lib.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
