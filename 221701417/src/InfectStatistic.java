import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * InfectStatistic TODO
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

//	写出文件
	public static void writeTxt(String txtPath,String content){    
	       FileOutputStream fileOutputStream = null;
	       File file = new File(txtPath);
	       try {
	           if(file.exists()){
	               //判断文件是否存在，如果不存在就新建一个txt
	               file.createNewFile();
	           }
	           fileOutputStream = new FileOutputStream(file);
	           fileOutputStream.write(content.getBytes());
	           fileOutputStream.flush();
	           fileOutputStream.close();
	       } catch (Exception e) {
	           e.printStackTrace();
	       }
	    }
	
	
}
