import java.io.File;
import java.util.ArrayList;

/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
class InfectStatistic {
	
	public static ArrayList<String> getText(File file) {
		ArrayList<String> list = new ArrayList<String>();
		try {
			BufferedReader bReader = new BufferedReader(new FileReader(file));
			String s = null;
			while ((s = bReader.readLine()) != null) {
				if (s.substring(0, 2).equals("//")) {
					continue;
				} else {
					if (!s.equals("")) {
						list.add(s);
					}
				}
			}
			bReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static ArrayList<String> getFiles(String path) {
		ArrayList<String> files = new ArrayList<String>();
		File file = new File(path);
		File[] tempList = file.listFiles();

		for (int i = 0; i < tempList.length; i++) {
			if (tempList[i].isFile()) {

				files.add(tempList[i].toString());
			}
			if (tempList[i].isDirectory()) {
				System.out.println("文件夹：" + tempList[i]);
			}
		}
		return files;
	}

	
	public static void main(String[] args) {
		System.out.println("helloworld");
	}
} 