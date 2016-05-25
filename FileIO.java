/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pgw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Samreng 2016-5-21 20:18
 */
public class FileIO {

    String[] ReadFileConfig(String path) {
        File file = new File(path);

        String line;
        String tagHeader = "";
        String fieldConfig = "";
        String pathConfig = "";
        int tagCount = 0;
        String[] fieldList = new String[300];   //array buffer for store field list

//        String[] config = new String[3];    //[0]= pathConfig, [1]=fieldConfig, [2]=tagCount
        try {
            try (BufferedReader bufferRead = new BufferedReader(new FileReader(file))) {
//                String line;
//                String tagHeader = "";
//                String fieldConfig = "";
//                String pathConfig = "";
//                int tagCount=0;
//                String[] fieldList = new String[300];   //array buffer for store field list

                while ((line = bufferRead.readLine()) != null) {
                    line = line.trim();
                    if (line.length() > 1) {
                        if ("[".equals(line.substring(0, 1))) {   //Check line start with "["
                            tagHeader = line;
                        } else {

                            switch (tagHeader) {

                                case "[FieldConfig]":
                                    fieldConfig = fieldConfig + line + "|";
                                    fieldList[tagCount] = line.substring(0, (line.indexOf(",")));
                                    tagCount++;     //Count Tag Field config
                                    break;

                                case "[Path]":
                                    pathConfig = pathConfig + line + "|";
                                    break;

                                default:
                                    break;
                            }
                        }
                    }
                }
//                String[] config = new String[3+tagCount];    //[0]= pathConfig, [1]=fieldConfig, [2]=tagCount, [4](tagList) {5...
//                config[0] = pathConfig;
//                config[1] = fieldConfig;
//                config[2] = Integer.toString(tagCount);
//                for(int i=0; i<tagCount;i++){
//                    config[i+3]=fieldList[i];
//                }
            }
        } catch (IOException e) {
            System.out.println("Can't read file:" + file);    // TODO Auto-generated catch block
        }
        String[] config = new String[3 + tagCount];    //[0]= pathConfig, [1]=fieldConfig, [2]=tagCount, [4](tagList) {5...
        config[0] = pathConfig;
        config[1] = fieldConfig;
        config[2] = Integer.toString(tagCount);
        for (int i = 0; i < tagCount; i++) {
            config[i + 3] = fieldList[i];
        }

        return config;
    }

}