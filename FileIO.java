/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pgw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

/**
 *
 * @author Samreng 2016-5-21 20:18
 */
public class FileIO {

    void createDirectory(String directoryName) {
        File theDir = new File(directoryName);

        // if the directory does not exist, create it
        if (!theDir.exists()) {
//            System.out.println("creating directory: " + directoryName);     // Debug
            theDir.mkdir();
        } else {
//            System.out.println("Directory " + directoryName + " is exists");    //Debug
        }
    }

    public List<String> ListFileByExtension(String folder, String ext) {

        ArrayList<String> arrayListFile = new ArrayList<>();      //array for store fileName
//        arrayRecordData.add(recordFieldData);

        GenericExtFilter filter = new GenericExtFilter(ext);
        File dir = new File(folder);
        if (dir.isDirectory() == false) {
            System.out.println("Directory does not exists : " + folder);    //FILE_DIR);    // Debug
            return arrayListFile;
        }

        // list out all the file name and filter by the extension
        String[] list = dir.list(filter) == null ? new String[0] : dir
                .list(filter);

        if (list.length == 0) {
            System.out.println("no files end with : " + ext);           //Debug
            return arrayListFile;
        }

        for (String file : list) {
            String temp = new StringBuffer() //(folder)  //.append(File.separator)
                    .append(file).toString();
            arrayListFile.add(temp);
//            System.out.println("file : " + temp);             // Debug
        }
        return arrayListFile;
    }

    // inner class, generic extension filter
    public class GenericExtFilter implements FilenameFilter {

        private String ext;

        public GenericExtFilter(String ext) {
            this.ext = ext;
        }

        public boolean accept(File dir, String name) {
            return (name.endsWith(ext));
        }
    }
//
//    
//    
//    
//    
//    
//    

    String[] ReadFileConfig(String path) {
        File file = new File(path);

        String line;
        String tagHeader = "";
        String fieldConfig = "";
        String pathConfig = "";
        int tagCount = 0;
        String[] fieldList = new String[300];   //array buffer for store field list ==> estimate not more than 300 field

//        String[] config = new String[3];    //[0]= pathConfig, [1]=fieldConfig, [2]=tagCount
        try {
            try (BufferedReader bufferRead = new BufferedReader(new FileReader(file))) {

                while ((line = bufferRead.readLine()) != null) {
                    line = line.trim();
                    if (line.length() > 1) {
                        if ("[".equals(line.substring(0, 1))&&("]".equals(line.substring(line.length()-1,line.length())))) {   //Check line start with "[" and end with "]"
                            tagHeader = line;
                        } else {

                            switch (tagHeader) {

                                case "[FieldConfig]":
                                    fieldConfig = fieldConfig + line + "|";
                                    fieldList[tagCount] = line.substring(0, (line.indexOf(",")));
                                    tagCount++;     //Count Tag Field config
                                    break;

                                case "[RunConfig]":
                                    pathConfig = pathConfig + line + "|";
                                    break;

                                default:
                                    break;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Can't read file:" + file);    // TODO Auto-generated catch block
        }
        String[] config = new String[3 + tagCount];    //[0]= pathConfig, [1]=fieldConfig, [2]=tagCount, [4](tagList) {5...
        config[0] = pathConfig;
        config[1] = fieldConfig;
        config[2] = Integer.toString(tagCount);

//         for (int i = 0; i < tagCount; i++) {
//            config[i + 3] = fieldList[i];
//        }
        System.arraycopy(fieldList, 0, config, 3, tagCount);

        return config;
    }

    boolean FileWriter(String fileName, boolean writeMode, String data) {
        File file = new File(fileName);
        FileWriter fileWrite;
        try {
            fileWrite = new FileWriter(file, writeMode);  // writeMode ==> true=append , false=Overide
            fileWrite.write(data);
            fileWrite.close();
        } catch (IOException e) {
            System.out.println("Error write to file: " + fileName);   //Debug
            return false;
        }
        return true;
    }

    boolean bufferWriter(String fileName, List<String> data) {

        File file = new File(fileName);
        FileWriter fileWrite;
        BufferedWriter bufferWrite;
        try {
            fileWrite = new FileWriter(file);
            bufferWrite = new BufferedWriter(fileWrite);
            for (String data1 : data) {
                bufferWrite.write(data1 + System.getProperty("line.separator"));
            }
            bufferWrite.close();
            fileWrite.close();
        } catch (IOException e) {
            System.out.println("Error write to file: " + fileName);   //Debug
            return false;
        }
        return true;
    }
//}

    public boolean gZIP(String sourceFile, String destFile) {
        byte[] buffer = new byte[1024];

        try {
            try (GZIPOutputStream gzos = new GZIPOutputStream(new FileOutputStream(destFile))) {
                try (FileInputStream in = new FileInputStream(sourceFile)) {
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        gzos.write(buffer, 0, len);
                    }
                }
                gzos.finish();
            }
            return true;    // zip file and write complete
        } catch (IOException ex) {
        }
        return false;       // zip file or write error
    }
}
