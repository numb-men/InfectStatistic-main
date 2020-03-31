

import java.io.*;

/**
 *
 * 这个是调用者
 */
public class InfectStatistic {
    public static void main(String[] args) throws IOException {
//        String[] arguments = {"list","-log","221701139/log","-out","D:/ListOut1.txt","-date","2020-01-22"};
//        String[] arguments2 = {"list","-log","221701139/log","-out","D:/ListOut2.txt","-date","2020-01-22",
//                                "-province","福建","河北"};
//        String[] arguments3 = {"list","-log","221701139/log","-out","D:/ListOut3.txt","-date","2020-01-27",
//                "-province","全国","福建","湖北"};
        Lib lib = new Lib(args);
        lib.execute();
    }
}
