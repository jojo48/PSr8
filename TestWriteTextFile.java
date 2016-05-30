/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pgw;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Samreng
 */
public class TestWriteTextFile {

    public static void main(String[] args) {
        FileIO FileIO =new FileIO();
        
//		("D:\\Training\\Java\\SourceCode\\cdr\\cdr_raw_PS_R8\\20140403221949_sample4.cdr");
        String folderName = "D:\\Training\\Java\\SourceCode\\cdr\\cdr_raw_PS_R8\\";
        String fileName = "20140403221949_sample4.cdr" + ".txt";
        String path;
        path = folderName + fileName;
        File file = new File(path);

        FileWriter writer;
        try {

            writer = new FileWriter(file, true);  //True = Append to file, false = Overwrite
            writer.write("Welcome thaicreate.com 1\r\n");
            writer.write("Welcome thaicreate.com 2\r\n");
            writer.write("Welcome thaicreate.com 3\r\n");
            writer.write("Welcome thaicreate.com 4\r\n");

            writer.close();     // Close file

            System.out.println("Write success!");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
//        } catch (Exception e) {
//            System.out.println("Error: " + e.getMessage());
        }
        
// #######################################################################
        String pathName="D:/Training/Java/SourceCode/cdr/cdr_raw_ps_R8/";
        fileName="Test1.cdr" + ".txt";
        String writeFileName=pathName+fileName;
        
        String [] data =new String[]{"Text1","Text2","Text3","Text4","Text5"};
        
//        FileIO.bufferWriter(writeFileName,data);
        
        
        
        
        
        

    }

}
