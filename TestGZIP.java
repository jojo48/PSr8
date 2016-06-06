/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pgw;


/**
 *
 * @author Samreng
 */

//ackage demo.compress;
 
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;
 
//public class TestGZIP{   //CompressFileExample {
 
//    /**
//     * @param args
//     */
//    public static void main(String[] args) {
//        byte[] buffer = new byte[1024];
// 
//        try {
// 
//            FileOutputStream fos = new FileOutputStream("D:\\Training\\Java\\SourceCode\\cdr\\cdr_raw_PS_R8\\testRaw\\TestGzip\\20140403221949_sample_fieldErr.zip");
//            ZipOutputStream zos = new ZipOutputStream(fos);
//            ZipEntry ze = new ZipEntry("20140403221949_sample_fieldErr.cdr");
//            zos.putNextEntry(ze);
//            FileInputStream in = new FileInputStream ("D:\\Training\\Java\\SourceCode\\cdr\\cdr_raw_PS_R8\\testRaw\\20140403221949_sample_fieldErr.cdr"); //("C:\\users\\nopphanan7\\Directory1\\photo.jpg");
// 
//            int len;
//            while ((len = in.read(buffer)) > 0) {
//                zos.write(buffer, 0, len);
//            }
//            in.close();
//            
//            
//            zos.closeEntry();
//            // remember close it
//            zos.flush();
//            zos.close();
//            System.out.println("Compress file is done");
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
//}
//-------------------------------------------------------------------------------------------------------------
//
//compressedOutput.flush() 
//compressedOutput.close()








//-------------------------------------------------------------------------------------------------------------
public class TestGZIP {
    private static final String OUTPUT_GZIP_FILE = "D:\\Training\\Java\\SourceCode\\cdr\\cdr_raw_PS_R8\\testRaw\\TestGzip\\20140403221949_sample_fieldErr_1.cdr.gz";
    private static final String SOURCE_FILE = "D:\\Training\\Java\\SourceCode\\cdr\\cdr_raw_PS_R8\\testRaw\\20140403221949_sample_fieldErr.cdr";
 
    /**
     * @param args
     */
    public static void main(String[] args) {
        FileIO FileIO = new FileIO();
        if(FileIO.gZIP(SOURCE_FILE, OUTPUT_GZIP_FILE)){     //Test with create object from class FileIO
            System.out.println("gZIP Complete");
        }else{
            System.out.println("gZIP Error");
        }
        
//        TestGZIP gZip = new TestGZIP();
//        gZip.gzipIt();
    }
 
    /**
     * GZip it
     * 
     * @param zipFile output GZip file location
     */
    public void gzipIt() {
        byte[] buffer = new byte[1024];
 
        try {
            GZIPOutputStream gzos = new GZIPOutputStream(new FileOutputStream(OUTPUT_GZIP_FILE));
            FileInputStream in = new FileInputStream(SOURCE_FILE);
 
            int len;
            while ((len = in.read(buffer)) > 0) {
                gzos.write(buffer, 0, len);
            }
            in.close();
            gzos.finish();
            gzos.close();
 
            System.out.println("Compress file with GZip is Done");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
//    
//    
//    
//    
//package demo.compress;
// 
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.zip.GZIPOutputStream;
// 
//public class CompressGZipFileExample {
// 
//    private static final String OUTPUT_GZIP_FILE = "C:\\users\\nopphanan7\\MyGZFile.gz";
//    private static final String SOURCE_FILE = "C:\\users\\nopphanan7\\Directory1\\photo.jpg";
// 
//    /**
//     * @param args
//     */
//    public static void main(String[] args) {
//        CompressGZipFileExample gZip = new CompressGZipFileExample();
//        gZip.gzipIt();
//    }
// 
//    /**
//     * GZip it
//     * 
//     * @param zipFile output GZip file location
//     */
//    public void gzipIt() {
//        byte[] buffer = new byte[1024];
// 
//        try {
//            GZIPOutputStream gzos = new GZIPOutputStream(new FileOutputStream(OUTPUT_GZIP_FILE));
//            FileInputStream in = new FileInputStream(SOURCE_FILE);
// 
//            int len;
//            while ((len = in.read(buffer)) &gt; 0) {
//                gzos.write(buffer, 0, len);
//            }
//            in.close();
//            gzos.finish();
//            gzos.close();
// 
//            System.out.println("Compress file with GZip is Done");
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
//}
