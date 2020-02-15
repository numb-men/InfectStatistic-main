/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
class InfectStatistic {
	public static void main(String args[]) {
		try {
			@SuppressWarnings("resource")
			BufferedReader in = new BufferedReader(new FileReader("D:\\log.txt"));
            String str=null;
            int i=0;
            while ((str = in.readLine())!= null) {
                i++;
                byte[] bytes=str.getBytes();
                System.out.print("第"+i+"行："+str+"    ");
                System.out.println("第"+i+"行有"+bytes.length+"个字节"+str.length()+"个字符");//输出每一行的字符和字节个数
            }
            System.out.println("该文本共有"+i+"行");
        } catch (IOException e) {
            e.printStackTrace();
        }
		try {
				String content = "a dog will be write in file";
				File file = new File("D:\\result.txt");
				if(!file.exists()){
					file.createNewFile();
				}
				FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fileWriter);
				bw.write(content);
				bw.close();
				System.out.println("finish");
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
}
    }
}
