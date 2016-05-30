/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pgw;

import java.util.HashMap;

/**
 *
 * @author Samreng
 */
public class TestSplitText {

    public static void main(String args[]) {
////        String text = "1,2,3,4,5,6";
////        String text = "1x|2x|3x|4x|5x|6";
//        String text="RawData=D:\\Training\\Java\\SourceCode\\pgw\\PGWr8.csv,DecodeData=D:\\Training\\Java\\SourceCode\\pgw\\PGWr8.csv,ZipData=D:\\Training\\Java\\SourceCode\\pgw\\PGWr8.csv|";
//        
//                String[] splits1 = text.split(",", 0);  // same as text.split(",");
//        String[] splits2 = text.split(",", -1);
//        String[] splits3 = text.split(",", 5);
//
//        System.out.println(text);
//        System.out.println("text.split(\",\", 0)");
//        for (int i = 0; i < splits1.length; i++) {
//            System.out.println("index:" + i + " word:" + splits1[i]);
//        }
//
//        System.out.println("\ntext.split(\",\", -1)");
//        for (int i = 0; i < splits2.length; i++) {
//            System.out.println("index:" + i + " word:" + splits2[i]);
//        }
//
//        System.out.println("\ntext.split(\",\", 5)");
//        for (int i = 0; i < splits3.length; i++) {
//            System.out.println("index:" + i + " word:" + splits3[i]);
//        }

        String pathConfig = "RawData=D:\\Training\\Java\\SourceCode\\pgw\\RawData\\PGWr8.csv|DecodeData=D:\\Training\\Java\\SourceCode\\pgw\\DecodeData\\PGWr8.csv|ZipData=D:\\Training\\Java\\SourceCode\\pgw\\ZipData\\PGWr8.csv|";

        int pathConfig_indxStart;
        int pathConfig_indxEnd = 0;
        int offsetStart = 0;            // use for check and Skip "|"

        HashMap<String, String> listPathConfig = new HashMap<>();  //Create HashMap for store field data decoded

        do {
            pathConfig_indxStart = pathConfig_indxEnd + offsetStart;
            pathConfig_indxEnd = pathConfig.indexOf("|", pathConfig_indxEnd + 1);
            String pathItem = pathConfig.substring(pathConfig_indxStart, pathConfig.indexOf("|", pathConfig_indxEnd));
            System.out.println(pathItem);                                               // Debug
            String key = pathItem.substring(0, pathItem.indexOf("="));
            String val = pathItem.substring(pathItem.indexOf("=") + 1, pathItem.length());
            listPathConfig.put(key, val);  // push to HashMapArray
            System.out.println("key:" + key + "    " + "val:" + val);                   // Debug
            System.out.println("");                                                     // Debug
            offsetStart = 1;      // Skip "|"
        } while (pathConfig_indxEnd + 1 < pathConfig.length());
        
        System.out.println("GetRawPath:"+listPathConfig.get("RawData"));                //Debug
        System.out.println("GetDecodeDataPath:"+listPathConfig.get("DecodeData"));      //Debug
        System.out.println("GetPath(no-key):"+listPathConfig.get("no-key"));            //Debug
    }
}
