/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pgw;

import java.util.List;

/**
 *
 * @author Samreng
 */
//public class TestCreateFileFolder {
//    /*
//  Determine if file or directory exists
//  This Java example shows how to determine if a particular file or directory
//  exists in the filesystem using exists method of Java File class.
//*/
// 
//import java.io.*;
// 
//public class DetermineIfFileExists {
// 
//  public static void main(String[] args) {
//   
//    //create file object
//    File file = new File("C://FileIO/ExistsDemo.txt");
//   
//    /*
//     * To determine whether specified file or directory exists, use
//     * boolean exists() method of Java File class.
//     *
//     * This method returns true if a particular file or directory exists
//     * at specified path in the filesystem, false otherwise.
//     */
//   
//     boolean blnExists = file.exists();
//   
//     System.out.println("Does file " + file.getPath() + " exist ?: " + blnExists);
//  }
//}
// 
///*
//Output would be
//Does file C:\FileIO\ExistsDemo.txt exist ?: true
//*/
//   
//    
//    private void createDirectoryIfNeeded(String directoryName)
//{
//  File theDir = new File(directoryName);
// 
//  // if the directory does not exist, create it
//  if (!theDir.exists())
//  {
//    System.out.println("creating directory: " + directoryName);
//    theDir.mkdir();
//  }
//}
//    
//    
//    
// new File("/Path/To/File/or/Directory").exists();   
//    File f = new File("/Path/To/File/or/Directory");
//if (f.exists() && f.isDirectory()) {
//   ...
//}
//    
//}
//
//
//public static void main(String[] args) {
////        ("D:\\Training\\Java\\SourceCode\\cdr\\cdr_raw_PS_R8\\20140403221949_sample.cdr");
//    FileIO FileIO =new FileIO();
//    
//    String pathName="D:/Training/Java/SourceCode/cdr/cdr_raw_PS_R8/";
//    String directoryName="DirectoryTest2";
//    String fileName="FileTest1";
//    
//    FileIO.createDirectory(pathName+directoryName);
//    
//    
//}
//    
//    
//    public static void main(String[]args)
//    {
//        File curDir = new File(".");
//        getAllFiles(curDir);
//    }
//    private static void getAllFiles(File curDir) {
//
//        File[] filesList = curDir.listFiles();
//        for(File f : filesList){
//            if(f.isDirectory())
//                getAllFiles(f);
//            if(f.isFile()){
//                System.out.println(f.getName());
//            }
//        }
//
//    }
//    
//    public static void main(String[]args)
//    {
//        File curDir = new File(".");
//        getAllFiles(curDir);
//    }
//    private static void getAllFiles(File curDir) {
//
//        File[] filesList = curDir.listFiles();
//        for(File f : filesList){
//            if(f.isDirectory())
//                System.out.println("Directory: "+f.getName());
//            if(f.isFile()){
//                System.out.println("File: "+f.getName());
//            }
//        }
//
//    }
//    
//    
//    
//    package test;
//import java.io.File;
//import java.io.IOException;
//
//public class DirectoryContents {
//
//	public static void main(String[] args) throws IOException {
//
//		File f = new File("."); // current directory
//
//		File[] files = f.listFiles();
//		for (File file : files) {
//			if (file.isDirectory()) {
//				System.out.print("directory:");
//			} else {
//				System.out.print("     file:");
//			}
//			System.out.println(file.getCanonicalPath());
//		}
//
//	}
//
//        
// 
//    import java.io.File;
//public class ListFiles
//{
//    public static void main(String[] args)
//    {
//        // Directory path here
//        String path = ".";
//        String files_name;
//        File folder = new File(path);
//        File[] listOfFiles = folder.listFiles();
//        for (int i = 0; i < listOfFiles.length; i++)
//        {
//            if (listOfFiles[i].isFile())
//            {
//                files_name = listOfFiles[i].getName();
//                if (files_name.endsWith(".txt") || files_name.endsWith(".TXT"))
//                {
//                    System.out.println(files_name);
//                }
//            }
//        }
//    }
//    package demo.file;
// 
//import java.io.*;
// 
//}
//
//public class TestListFileByExtension {
// 
//    private static final String FILE_DIR = "D:/Training/Java/SourceCode/cdr/cdr_raw_PS_R8";
//    private static final String FILE_TEXT_EXT = ".cdr";
// 
////    /**
////     * @param args
////     */
//    public static void main(String args[]) {
//        new TestListFileByExtension().listFile(FILE_DIR, FILE_TEXT_EXT);
//    }
// 
//    public void listFile(String folder, String ext) {
// 
//        GenericExtFilter filter = new GenericExtFilter(ext);
// 
//        File dir = new File(folder);
// 
//        if (dir.isDirectory() == false) {
//            System.out.println("Directory does not exists : " + FILE_DIR);
//            return;
//        }
// 
//        // list out all the file name and filter by the extension
//        String[] list = dir.list(filter) == null ? new String[0] : dir
//                .list(filter);
// 
//        if (list.length == 0) {
//            System.out.println("no files end with : " + ext);
//            return;
//        }
// 
//        for (String file : list) {
//            String temp = new StringBuffer(FILE_DIR).append(File.separator)
//                    .append(file).toString();
//            System.out.println("file : " + temp);
//        }
//    }
// 
//    // inner class, generic extension filter
//    public class GenericExtFilter implements FilenameFilter {
// 
//        private String ext;
// 
//        public GenericExtFilter(String ext) {
//            this.ext = ext;
//        }
// 
//        public boolean accept(File dir, String name) {
//            return (name.endsWith(ext));
//        }
//    }
//}
// 
//    
//    
//    
////    package com.java.myapp;
//
//import java.io.File;
//public class ListFile {
//
//    public static void main(String[] args) {
//
//        String path = "D:/Training/Java/SourceCode/cdr/cdr_raw_PS_R8";
//
//        File directory = new File(path);
//
//        //get all the files from a directory
//        File[] fList = directory.listFiles();
//
//        for (File file : fList) {
//            System.out.println(file.getName());
//        }
//
//    }
//
//}




public class TestListFileByExtension {
 
    private static final String FILE_DIR = "D:/Training/Java/SourceCode/cdr/cdr_raw_PS_R8";
    private static final String FILE_EXT = ".cdr";
 
//    /**
//     * @param args
//     */
    public static void main(String args[]) {
//        new TestListFileByExtension().listFile(FILE_DIR, FILE_TEXT_EXT);
        
        FileIO FileIO = new FileIO();
        List<String> arrayListFile=FileIO.ListFileByExtension(FILE_DIR, FILE_EXT);
        
//        System.out.println("arrayListFile0: "+arrayListFile<0>);
//        for (String data1 : arrayListFile) {
//               System.out.println(data1 + System.getProperty("line.separator"));
//            }
        
        System.out.println("arrayListFile Size: "+arrayListFile.size());
        System.out.println("arrayListFile(0): "+arrayListFile.get(0));
        
        
    }
}
 