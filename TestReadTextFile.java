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
 * @author Samreng 2016-05-21 15:11
 */
public class TestReadTextFile {

    public static void main(String[] args) {

        String path = "D:\\Training\\Java\\SourceCode\\pgw\\PGWr8.csv";
        File file = new File(path);

        try {
            try (BufferedReader bufferRead = new BufferedReader(new FileReader(file))) {
                String line;
                String tagHeader = "";
                String fieldConfig = "";
                String pathConfig = "";

                while ((line = bufferRead.readLine()) != null) {
                    System.out.println(line);
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
                System.out.println("fieldConfig==> " + fieldConfig);
                System.out.println("pathConfig==> " + pathConfig);
            }
        } catch (IOException e) {
            System.out.println("Can't read file:" + file);    // TODO Auto-generated catch block
        }
    }
}
