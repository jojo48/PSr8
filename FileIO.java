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
        String[] config = new String[2];
        try {
            try (BufferedReader bufferRead = new BufferedReader(new FileReader(file))) {
                String line;
                String tagHeader = "";
                String fieldConfig = "";
                String pathConfig = "";

                while ((line = bufferRead.readLine()) != null) {
                    if (line.length() > 1) {
                        if ("[".equals(line.substring(0, 1))) {   //Check line start with "["
                            tagHeader = line;
                        } else {

                            switch (tagHeader) {

                                case "[FieldConfig]":
                                    fieldConfig = fieldConfig + line + "|";
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
                config[0] = pathConfig;
                config[1] = fieldConfig;
            }
        } catch (IOException e) {
            System.out.println("Can't read file:" + file);    // TODO Auto-generated catch block
        }
        return config;
    }

}
