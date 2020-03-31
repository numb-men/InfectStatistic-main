import org.junit.Test;

public class InfectStatisticTest {
	
	@Test
	public void MainTest1() {
		String args[]= {"list","-log","D:\\log","-out","D:\\out1.txt"};
		InfectStatistic.main(args);
	}
	
	
	@Test
	public void MainTest2() {
		String args[]= {"list","-log","D:\\log","-out","D:\\out2.txt","-type","ip"};
		InfectStatistic.main(args);
	}
	
	
	@Test
	public void MainTest3() {
		String args[]= {"list","-log","D:\\log","-out","D:\\out3.txt","-date","1998-1-1"};
		InfectStatistic.main(args);
	}
	
	
	@Test
	public void MainTest4() {
		String args[]= {"list","-log","D:\\","-out","D:\\out4.txt"};
		InfectStatistic.main(args);
	}
	
	
	@Test
	public void MainTest5() {
		String args[]= {"list","-log","D:\\log","-out","D:\\out5.txt","-date","2040-01-23"};
		InfectStatistic.main(args);
	}
	
	
	@Test
	public void MainTest6() {
		String args[]= {"list","-log","D:\\log","-out","D:\\out6.txt","-province","全国","河南","福建"};
		InfectStatistic.main(args);
	}
	
	
	@Test
	public void MainTest7() {
		String args[]= {"list","-log","D:\\log","-out","D:\\out7.txt","-province","内蒙古"};
		InfectStatistic.main(args);
	}
	
	
	@Test
	public void MainTest8() {
		String args[]= {"list","-log","D:\\log","-out","D:\\out8.txt","-date","2020-01-22"};
		InfectStatistic.main(args);
	}
	
	
	@Test
	public void MainTest9() {
		String args[]= {"list","-log","D:\\log","-out","D:\\out9.txt","-date","2020-01-22","-province","全国"};
		InfectStatistic.main(args);
	}
	
	
	@Test
	public void MainTest10() {
		String args[]= {"list","-log","D:\\log","-out","D:\\out10.txt","-date","2020-01-22","-province","全国","-type","sp"};
		InfectStatistic.main(args);
	}
}
