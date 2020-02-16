

import java.io.*;

/**
 *
 * 这个是调用者
 */
public class InfectStatistic {
    public static void main(String[] args) throws IOException {

        // 在静态方法创建非静态内部类
        Lib lib = new Lib(args);
        lib.execute();

    }
}
