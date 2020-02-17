import org.junit.Test;

public class InfectStatisticTest {

	@Test  //测试1只有-log和-out两个命令，其他命令未指定
	public void test1() {
		String[] testStr = {"list", "-log", "D:\\InfectStatistic-test\\log\\", 
				"-out", "D:\\InfectStatistic-test\\result\\testOut1.txt"};
		InfectStatistic.main(testStr);
	}
	
	@Test  //测试2在测试1的基础上添加-date命令,日期为第一篇日志发布的时间之前
	public void test2() {
		String[] testStr = {"list", "-log", "D:\\InfectStatistic-test\\log\\", 
				"-out", "D:\\InfectStatistic-test\\result\\testOut2.txt", "-date", "2020-01-01"} ;
		InfectStatistic.main(testStr);
	}
	
	@Test  //测试3在测试2的基础上修改了日期的值，日期为2020-01-24，即没有对应当天的日志文件但是有之前日期的对应日志文件
	public void test3() {
		String[] testStr = {"list", "-log", "D:\\InfectStatistic-test\\log\\", 
				"-out", "D:\\InfectStatistic-test\\result\\testOut3.txt", "-date", "2020-01-24"} ;
		InfectStatistic.main(testStr);
	}
	
	@Test   //测试4在测试
	public void test4() {
		String[] testStr = {"list", "-log", "D:\\InfectStatistic-test\\log\\", 
				"-out", "D:\\InfectStatistic-test\\result\\testOut4.txt", "-date", "2020-01-24"} ;
		InfectStatistic.main(testStr);
	}
	
	@Test
	public void test5() {
		String[] testStr = {"list", "-log", "D:\\InfectStatistic-test\\log\\", 
				"-out", "D:\\InfectStatistic-test\\result\\testOut5.txt", "-date", "2020-01-01"} ;
		InfectStatistic.main(testStr);
	}
	
	@Test
	public void test6() {
		String[] testStr = {"list", "-log", "D:\\InfectStatistic-test\\log\\", 
				"-out", "D:\\InfectStatistic-test\\result\\testOut6.txt", "-date", "2020-01-01"} ;
		InfectStatistic.main(testStr);
	}
	
	@Test
	public void test7() {
		String[] testStr = {"list", "-log", "D:\\InfectStatistic-test\\log\\", 
				"-out", "D:\\InfectStatistic-test\\result\\testOut7.txt", "-date", "2020-01-01"} ;
		InfectStatistic.main(testStr);
	}
	
	@Test
	public void test8() {
		String[] testStr = {"list", "-log", "D:\\InfectStatistic-test\\log\\", 
				"-out", "D:\\InfectStatistic-test\\result\\testOut8.txt", "-date", "2020-01-01"} ;
		InfectStatistic.main(testStr);
	}
	
	@Test
	public void test9() {
		String[] testStr = {"list", "-log", "D:\\InfectStatistic-test\\log\\", 
				"-out", "D:\\InfectStatistic-test\\result\\testOut9.txt", "-date", "2020-01-01"} ;
		InfectStatistic.main(testStr);
	}

	@Test
	public void test10() {
		String[] testStr = {"list", "-log", "D:\\InfectStatistic-test\\log\\", 
				"-out", "D:\\InfectStatistic-test\\result\\testOut10.txt", "-date", "2020-01-01"} ;
		InfectStatistic.main(testStr);
	}
}
