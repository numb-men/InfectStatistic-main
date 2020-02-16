import junit.framework.TestCase;

public class Test extends TestCase {
    public void test1(){
        String str[]={
                "list",
                "-log", "E:\\github\\InfectStatistic-main\\221701434\\log\\",
                "-out", "E:\\github\\InfectStatistic-main\\221701434\\result\\ListOut1.txt",
        };
        InfectStatistic.main(str);
    }
    public void test2(){
        String str[]={
                "list",
                "-date", "2020-01-22",
                "-log", "E:\\github\\InfectStatistic-main\\221701434\\log\\",
                "-out", "E:\\github\\InfectStatistic-main\\221701434\\result\\ListOut2.txt",
        };
        InfectStatistic.main(str);
    }
    public void test3(){
        String str[]={
                "list",
                "-date",
                "-log", "E:\\github\\InfectStatistic-main\\221701434\\log\\",
                "-out", "E:\\github\\InfectStatistic-main\\221701434\\result\\ListOut3.txt",
        };
        InfectStatistic.main(str);
    }
    public void test4(){
        String str[]={
                "list",
                "-date", "2020-1-1",
                "-log", "E:\\github\\InfectStatistic-main\\221701434\\log\\",
                "-out", "E:\\github\\InfectStatistic-main\\221701434\\result\\ListOut4.txt",
        };
        InfectStatistic.main(str);
    }
    public void test5(){
        String str[]={
                "list",
                "-date", "2020-01-27",
                "-type", "ip",
                "-log", "E:\\github\\InfectStatistic-main\\221701434\\log\\",
                "-out", "E:\\github\\InfectStatistic-main\\221701434\\result\\ListOut5.txt",
        };
        InfectStatistic.main(str);
    }
    public void test6(){
        String str[]={
                "list",
                "-date", "2020-01-27",
                "-type", "ip", "dead", "cure", "sp",
                "-log", "E:\\github\\InfectStatistic-main\\221701434\\log\\",
                "-out", "E:\\github\\InfectStatistic-main\\221701434\\result\\ListOut6.txt",
        };
        InfectStatistic.main(str);
    }
    public void test7(){
        String str[]={
                "list",
                "-date", "2020-01-27",
                "-type", "sp", "cure", "dead", "ip",
                "-log", "E:\\github\\InfectStatistic-main\\221701434\\log\\",
                "-out", "E:\\github\\InfectStatistic-main\\221701434\\result\\ListOut7.txt",
        };
        InfectStatistic.main(str);
    }
    public void test8(){
        String str[]={
                "list",
                "-date", "2020-01-27",
                "-type", "ip", "dead", "cure", "sp",
                "-province","湖北",
                "-log", "E:\\github\\InfectStatistic-main\\221701434\\log\\",
                "-out", "E:\\github\\InfectStatistic-main\\221701434\\result\\ListOut8.txt",
        };
        InfectStatistic.main(str);
    }
    public void test9(){
        String str[]={
                "list",
                "-date", "2020-01-27",
                "-type", "ip", "dead", "sp",
                "-province","湖北","福建","全国",
                "-log", "E:\\github\\InfectStatistic-main\\221701434\\log\\",
                "-out", "E:\\github\\InfectStatistic-main\\221701434\\result\\ListOut9.txt",
        };
        InfectStatistic.main(str);
    }
    public void test10(){
        String str[]={
                "list",
                "-Date", "2020-01-27",
                "-type", "ip", "dead", "cure", "sp",
                "-province","湖北","福建","浙江",
                "-log", "E:\\github\\InfectStatistic-main\\221701434\\log\\",
                "-out", "E:\\github\\InfectStatistic-main\\221701434\\result\\ListOut10.txt",
        };
        InfectStatistic.main(str);
    }
    public void test11(){
        String str[]={
                "list",
                "-Date", "2020-01-25",
                "-type", "ip",  "cure", "sp",
                "-province","湖北","福建","浙江","湖南",
                "-log", "E:\\github\\InfectStatistic-main\\221701434\\log\\",
                "-out", "E:\\github\\InfectStatistic-main\\221701434\\result\\ListOut11.txt",
        };
        InfectStatistic.main(str);
    }
    public void test12(){
        String str[]={
                "list",
                "-Date", "2020-01-25",
                "-type", "ip",  "cure",
                "-province","全国",
                "-log", "E:\\github\\InfectStatistic-main\\221701434\\log\\",
                "-out", "E:\\github\\InfectStatistic-main\\221701434\\result\\ListOut12.txt",
        };
        InfectStatistic.main(str);
    }
    public void test13(){
        String str[]={
                "list",
                "-Date", "2020-01-25",
                "-type", "ip",  "cure",
                "-province","全国","河南",
                "-log", "E:\\github\\InfectStatistic-main\\221701434\\log\\",
                "-out", "E:\\github\\InfectStatistic-main\\221701434\\result\\ListOut13.txt",
        };
        InfectStatistic.main(str);
    }
}
