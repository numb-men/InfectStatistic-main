import org.junit.Test;

/**
 * Project Name:InfectStatistic-main2
 * File Name:FileHandleTest.java
 * Package Name:
 * Date:上午11:46:53
 * Copyright (c) 2020, Doris All Rights Reserved.
 *
*/

/**
 * Description: <br/>
 * Date: 上午11:46:53 <br/>
 * 
 * @author Doris
 * @version
 * @see
 */
public class FileHandleTest {
    @Test
    public void testFileRead() {
        FileHandle fh = new FileHandle();
        StringBuilder sb = new StringBuilder();
        sb = fh.fileRead("D:\\temp\\log\\2020-01-22.log.txt");
        System.out.println(sb);
    }

    // @Test
    // public void testGetFiles() {
    // FileHandle fh = new FileHandle();
    // ArrayList<String> a = fh.getFiles("‪D:\\temp\\log", "2020-01-27");
    // System.out.println(a);
    // }
    //
    // @Test
    // public void testFilesRead() {
    // FileHandle fh = new FileHandle();
    // StringBuilder sb = new StringBuilder();
    // sb = fh.filesRead("‪D:\\temp\\log", "2020-01-27");
    // System.out.println(sb);
    // }
}
