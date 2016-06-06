/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pgw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Samreng 2016-05-25 19:58
 */
public class PGW2TXT {

    public static final String lineSeparator = System.getProperty("line.separator");
    public static final String pathSeparator = File.separator;
    public static int sumDataUplink_file = 0;
    public static int sumDataDownlink_file = 0;
    public static int sumDataUplink_record = 0;
    public static int sumDataDownlink_record = 0;
    public static double sumDataUplink_allFile=0;
    public static double sumDataDownlink_allFile=0;
    public static double sumRecord_allFile=0;
    public static double sumRecordError_allFile=0;

    public void DataUplink(int dataUplink) {
        sumDataUplink_allFile=sumDataUplink_allFile+dataUplink;
        sumDataUplink_file = sumDataUplink_file + dataUplink;
        sumDataUplink_record = sumDataUplink_record + dataUplink;
    }

    public void DataDownlink(int dataDownlink) {
        sumDataDownlink_allFile=sumDataDownlink_allFile+dataDownlink;
        sumDataDownlink_file = sumDataDownlink_file + dataDownlink;
        sumDataDownlink_record = sumDataDownlink_record + dataDownlink;
    }
// ########################### Count Number of Record ###############################
//    int CountRecord(int[] rawFile) {
//        DataConverter DataConverter = new DataConverter();
//        int recordCount = 0;
//        int rawFile_indx = 0;
//        int rawFile_len = rawFile.length;
//        String tagRawFile;
//        do {
//            String record_len_str = "0x";
//            if ("BF4F".equals(tagRawFile = String.format("%02X", rawFile[rawFile_indx]) + String.format("%02X", rawFile[rawFile_indx + 1]))) {
//                recordCount++;
//                rawFile_indx += 2;
//                int record_len_byte = rawFile[rawFile_indx] - 0x80;
//                rawFile_indx++;
//                for (int j = 0; j < record_len_byte; j++) {
//                    record_len_str = record_len_str + String.format("%02X", rawFile[rawFile_indx]);
//                    rawFile_indx++;
//                }
//            }
//            rawFile_indx = rawFile_indx + DataConverter.hexString2int(record_len_str);
//        } while (rawFile_indx < rawFile_len - 1);
//        return recordCount;
//    }

    public static void main(String[] args) throws FileNotFoundException, IOException {

        Locale.setDefault(Locale.US);
        
        FileIO FileIO = new FileIO();
        String pathFileConfig = "D:\\Training\\Java\\SourceCode\\pgw\\PGWr8.csv";
        String[] readConfig = FileIO.ReadFileConfig(pathFileConfig);    //Return with array of header group
        String pathConfig = readConfig[0];  // String of Path Configuration
        String fieldConfig = readConfig[1]; // String of Tag Field list
        String record_decode_data; // Recodr data buffer
        
//formatting numbers upto 2 decimal places in Java  // DecimalFormat DecimalFormat = new DecimalFormat("#,###,##0.00");
        DecimalFormat DecimalFormat = new DecimalFormat("#,###,##0");  
//        System.out.println(DecimalFormat.format(364565.14));          // Example use

        
//#################################### Read Configuration and Store ArrayList ###############################################  
//        
        
int pathConfig_indxStart;
        int pathConfig_indxEnd = 0;
        int offsetStart = 0;            // use for check and Skip "|"

        HashMap<String, String> listPathConfig = new HashMap<>();  //Create HashMap for store field data decoded

        do {
            pathConfig_indxStart = pathConfig_indxEnd + offsetStart;
            pathConfig_indxEnd = pathConfig.indexOf("|", pathConfig_indxEnd + 1);
            String pathItem = pathConfig.substring(pathConfig_indxStart, pathConfig.indexOf("|", pathConfig_indxEnd));
            String key = pathItem.substring(0, pathItem.indexOf("="));
            String val = pathItem.substring(pathItem.indexOf("=") + 1, pathItem.length());
            listPathConfig.put(key, val);                           // push to HashMapArray
            offsetStart = 1;                                        // Skip "|"
        } while (pathConfig_indxEnd + 1 < pathConfig.length());
        
        String pathRawData=listPathConfig.get("RawFile");
        String pathDecodeData =listPathConfig.get("DecodeFile");
        String pathZipData =listPathConfig.get("BackupRawFile");
        String pathLogData =listPathConfig.get("LogFile");
        String pathFileError =listPathConfig.get("BackupErrorFile");
        String rawFileExtension =listPathConfig.get("RawFileExtension");
        
        
        
//        String timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date());
        
        FileIO.createDirectory(pathDecodeData);
        FileIO.createDirectory(pathZipData);
        FileIO.createDirectory(pathLogData);
        FileIO.createDirectory(pathFileError);
        
        List<String> arrayListFile=FileIO.ListFileByExtension(pathRawData, rawFileExtension);   // List RAW File from folder pathRawData
        String writeLogFileName = pathLogData+"LogDecoder.txt";
        
// ArrayList<String> arrayLogData = new ArrayList<>();         //array for store log data before write to text file 
        
        
// FileIO.FileWriter(writeLogFileName,true,"##########################################################################################################################"+lineSeparator);
// FileIO.FileWriter(writeLogFileName,true,"Start Time "+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))+lineSeparator);      // Start of log file
        
FileIO.FileWriter(writeLogFileName,true,lineSeparator+"====================================== Start Time "
                +new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+" ======================================"+lineSeparator);        
 FileIO.FileWriter(writeLogFileName,true,"\r\n------------------------------------ Path Configuration ------------------------------------"+lineSeparator);
 FileIO.FileWriter(writeLogFileName,true,"PathRawFile:         = "+pathRawData+lineSeparator);
 FileIO.FileWriter(writeLogFileName,true,"PathDecodeFile:      = "+pathDecodeData+lineSeparator);
 FileIO.FileWriter(writeLogFileName,true,"PathBackupRawFile:   = "+pathZipData+lineSeparator);
 FileIO.FileWriter(writeLogFileName,true,"PathLogFile:         = "+pathLogData+lineSeparator);
 FileIO.FileWriter(writeLogFileName,true,"PathBackupErrorFile: = "+pathFileError+lineSeparator);
 FileIO.FileWriter(writeLogFileName,true,"RawFileExtension:    = "+"\""+rawFileExtension+"\""+lineSeparator);
 
// 
// 
// arrayLogData.add("Start Time "+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));      // Start of log file
// arrayLogData.add("------------------------------------ Path Configuration ------------------------------------");
// arrayLogData.add("pathRawData:      "+pathRawData);
// arrayLogData.add("pathDecodeData:   "+pathDecodeData);
// arrayLogData.add("pathZipData:      "+pathZipData);
// arrayLogData.add("pathLogData:      "+pathLogData);
// arrayLogData.add("pathFileError:    "+pathFileError);
// arrayLogData.add("rawFileExtension: "+rawFileExtension);
// arrayLogData.add("");                                      // add space new line
// 
// 
 
        for (String data1 : arrayListFile) {                                                        //Debug
               System.out.println(data1);    //Debug   // System.getProperty("line.separator")      // Debug
        }                                                                                           // Debug
         
        
        
        
//        System.out.println("arrayListFile0: "+arrayListFile<0>);
//        for (String data1 : arrayListFile) {
//               System.out.println(data1 + System.getProperty("line.separator"));
//            }
        
        System.out.println("arrayListFile Size: "+arrayListFile.size());
        System.out.println("arrayListFile(0): "+arrayListFile.get(0));
        
        
//############################################# Start loop for run all RAW file ####################################################        
//     
       int totalRawFile= arrayListFile.size();
       int rawFileErrorCount=0;
       int errCountLimit=3;
       String listRawFileError="";
       String rawFileErrorList="";
       
       for(int rawFileNo=0;rawFileNo<totalRawFile;rawFileNo++){
           String fileName=arrayListFile.get(rawFileNo);
           String rawFilePathName=pathRawData+fileName;
           int notPGWCount=0;       // Counter for Count Not PGW Record If Over Limit Skip To Next Raw File
                      
           
           System.out.println("=================================================================================");     // Debug
           System.out.println("Decoding file: "+rawFilePathName);                                                           // Debug
           System.out.println("=================================================================================");     // Debug
       
         
        ArrayList<String> arrayRecordData = new ArrayList<>();      //array for store record decode data before write to text file
        int fieldCount = Integer.parseInt(readConfig[2]);
        String[] arrayFieldList = new String[fieldCount + 2];       // array for store field list  //+2 for sumUplink, sumDownlink

        int fieldConfig_len = fieldConfig.length();
        int fieldConfig_indx = 0;
        int tagIndex;

        arrayFieldList[0] = "sumUplink";
        arrayFieldList[1] = "sumDownlink";
        for (int i = 0; i < fieldCount; i++) {
            arrayFieldList[i + 2] = readConfig[i + 3];
        }

//        System.out.println(arrayFieldList[0] + " " + arrayFieldList[1]);     //Debug

        int fileIndex = 0;
        String tag_1hex_str, tag_2hex_str, tag_3hex_str;

        int record_indx = 0;
        int recordCount = 1;
        int recordErrorCount = 0;
        String addressErrorList="";
        String recordErrorList = "";    //use for add to integer for convert integer to string
        int sumRecordLength = 0;
       
//        String rawFileName="20140403221949_sample3.cdr";     // Test and Debug
        
//        String pathRawFile=listPathConfig.get("pathRawData")+rawFileName;
        
       
        
//        arrayLogData.add("------------------------------------ ("+DecimalFormat.format((rawFileNo+1))+"/"+DecimalFormat.format(totalRawFile)+") "+ fileName+" ------------------------------------");
//        arrayLogData.add("TimeBegin "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        FileIO.FileWriter(writeLogFileName,true,lineSeparator+"------------------------------------ (SeqNo:"+DecimalFormat.format((rawFileNo+1))+"/"+DecimalFormat.format(totalRawFile)+") "
                + fileName+" ------------------------------------"+lineSeparator);
        FileIO.FileWriter(writeLogFileName,true,"Start Time "+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))+lineSeparator);      // Start of log file
        
        
        RawFile readFile = new RawFile(); // Create Object from class readRaw
        int[] rawFileInt = readFile.readBinaryFile(rawFilePathName);
//           ("C:\\Users\\Samreng\\Documents\\HSPA\\CDR Project\\CDR_Description PS_R8\\raw_PS_R8\\PGW\\006295981_20160428091436.cdr");
//           ("D:\\Training\\Java\\SourceCode\\cdr\\cdr_raw_PS_R8\\0000204700_20140403221949.cdr");
//           ("D:\\Training\\Java\\SourceCode\\cdr\\cdr_raw_PS_R8\\20140403221949_sample.cdr");
//           ("D:\\Training\\Java\\SourceCode\\cdr\\cdr_raw_PS_R8\\20140403221949_sample3.cdr");
//           ("D:\\Training\\Java\\SourceCode\\cdr\\cdr_raw_PS_R8\\20140403221949_sample4.cdr");
//           ("D:\\Training\\Java\\SourceCode\\cdr\\cdr_raw_PS_R8\\20140403221949_sample_fieldErr.cdr");
//           ("D:\\Training\\Java\\SourceCode\\cdr\\cdr_raw_PS_R8\\006295981_20160428091436_sample.cdr");
//           ("D:\\Training\\Java\\SourceCode\\cdr\\cdr_raw_PS_R8\\20140403221949_sample2.cdr");
//           ("C:\\Users\\Samreng\\Documents\\HSPA\\CDR Project\\CDR_Description PS_R8\\pscdr2text\\0000211585_20140920201238.cdr");
//           ("D:\\Training\\Java\\SourceCode\\cdr\\cdr_raw_PS_R8\\20140920201238_sample_x30x80.cdr");
//           ("D:\\Training\\Java\\SourceCode\\cdr\\cdr_raw_PS_R8\\b00000001.dat");
        int fileLength = rawFileInt.length;
//        int rawFileRemain=fileLength;
        
//        arrayLogData.add("FileSize "+DecimalFormat.format(fileLength)+" Byte");
        FileIO.FileWriter(writeLogFileName,true,"FileSize "+DecimalFormat.format(fileLength)+" Bytes"+lineSeparator);
        
        System.out.println("File length = " + fileLength + "|0x" + String.format("%02X", fileLength) + " Bytes"); // Debug
        
        FieldDecoder decode = new FieldDecoder();       // Create Object from class FieldDecoder
        RawFile getRaw = new RawFile();                 // Create Object from class RawFile
        DataConverter data_conv = new DataConverter();  // Create Object from class DataConverter

//---------------- Start of loop file process ------------------------
        do {
            record_decode_data = "";    // Reset Recodr data buffer
            String field_hex_str = "";  // Reset old record length
            sumDataUplink_file = 0;     // Reset Counter
            sumDataDownlink_file = 0;   // Reset Counter
            HashMap<String,String> mapFieldData = new HashMap<>();  //Create HashMap for store field data decoded
//            mapFieldData = new HashMap<>();
             

//---------------- Start of loop record process ------------------------
            String record_type_hex_str = String.format("%02X", rawFileInt[fileIndex]) + String.format("%02X", rawFileInt[fileIndex + 1]);
            if ("BF4F".equals(record_type_hex_str)) {
                notPGWCount=0;          // Reset Record Error Counter
                fileIndex = fileIndex + 2;
                sumRecordLength = sumRecordLength + 2;
                System.out.println("############################## Record: " + recordCount + " ##############################");
                System.out.println("Addr:" + fileIndex + " is PGW Record"); // Debug Print out
                int record_len_byte = rawFileInt[fileIndex] - 0x80; //Check number of Byte is define Record length (1 or 2 Byte)
                fileIndex++;
                sumRecordLength = sumRecordLength + record_len_byte + 1;  // +1 = LengthByte
                for (int i = 0; i < record_len_byte; i++) {
                    field_hex_str = field_hex_str + String.format("%02X", rawFileInt[fileIndex]);
                    fileIndex++;
                }
                field_hex_str = "0x" + field_hex_str; // Add prefix Hex String format (0x)

                System.out.println("fileLength:" + "0x" + String.format("%02X", fileLength) + " fileIndx:" + "0x" + String.format("%02X", +fileIndex)); //Debug
                System.out.println("field_hex_str:" + field_hex_str);     //Debug

                int record_length = data_conv.hexString2int(field_hex_str); // Calculate Record length ( xxx Byte)
                sumRecordLength = sumRecordLength + record_length;
                System.out.println("Record length:" + record_length + " Byte");    // Debug print
//
//---------------- Start of loop record process ------------------------     
                try {
                    do {
//---------- Start of Check Tag field length 1,2,3Byte ---------------------
                        tag_1hex_str = "x" + String.format("%02X", rawFileInt[fileIndex]);    // Convert Integer to Hex String for check Tag field 1Byte
//
//----------------------------- Start of tag_1hex_str ---------------------------
                        System.out.println("------------------- Start of field (Record:" + recordCount + ") --------------------");
                        if (fieldConfig.contains(tag_1hex_str + ",")) {     // Check 1Byte Tag field is in Tag list table
                            System.out.print("Field Tag: " + tag_1hex_str + "   Address: 0x" + String.format("%02X", fileIndex) + "  "); // Debug print
                            fileIndex++;
                            record_indx++;
                            int tag_list_length = fieldConfig.indexOf("|", fieldConfig.indexOf(tag_1hex_str));
                            String field_conf = fieldConfig.substring(fieldConfig.indexOf(tag_1hex_str), tag_list_length);
                            String field_decode = field_conf.substring(field_conf.indexOf(",") + 1, field_conf.indexOf(",") + 2);
                            int field_length = rawFileInt[fileIndex];
                            if ("1".equals(field_decode)) {
//---------- Special check for Tag [xAC x81 (x80...x83)] not found in manual----------                    
                                if (("xAC".equals(tag_1hex_str)) && (rawFileInt[fileIndex] > 0x80) && (rawFileInt[fileIndex + 2] == 0x30)) {
                                    fileIndex += 2;       //Skip 2Byte
                                    record_indx += 2;    //Skip 2Byte
                                    // ------- Check all sub field x30 Length -------
                                    int x30_indx = fileIndex;
                                    int x30_len = rawFileInt[x30_indx + 1] + 2;    //+1Byte for Skip tag x30; +2Byte(Tag+Length Byte)

                                    while (rawFileInt[(x30_indx + x30_len)] == 0x30) {
                                        x30_len = x30_len + 2 + rawFileInt[(x30_indx + x30_len + 1)];    //+2Byte for Skip Tag and Length Byte
                                    }

                                    int[] field_raw_int = new int[x30_len];
                                    for (int i = 0; i < x30_len; i++) {
                                        field_raw_int[i] = rawFileInt[fileIndex];
                                        fileIndex++;
                                    }

                                    //Example data for call method decoder==> xAC,[120,12,2, ...],xAC,1,listOfTrafficVolumes,SEQUENCE OF ChangeOfCharCondition
                                    String field_decode_data = decode.decoder(tag_1hex_str, field_raw_int, field_conf);
                                    record_decode_data = record_decode_data + "|" + field_decode_data;

                                    System.out.println("length: " + field_length + "|0x" + String.format("%02X", field_length) + " Byte");    // Debug print
                                    System.out.println("Field Decode Data:" + field_decode_data);     // Debug print
//                        System.out.println("File Index:"+String.format("%02X", file_indx)); // Debug print
//                        record_indx < record_length
                                    System.out.println("file_indx:" + "0x" + String.format("%02X", fileIndex) + " record_length:" + "0x" + String.format("%02X", +record_length) + " record_indx:" + "0x" + String.format("%02X", +record_indx) + " field_length:" + "0x" + String.format("%02X", field_length));   //Debug
                                    System.out.println("fileLength:" + "0x" + String.format("%02X", fileLength) + " fileIndx:" + "0x" + String.format("%02X", +fileIndex)); //Debug

                                } //---------- End of Special check for Tag [xAC x81 (x80...x83)] not found in manual----------
                                else {

                                    int[] field_raw_int = new int[field_length]; // Array for field raw data interger
                                    fileIndex++;
                                    record_indx++;
                                    for (int i = 0; i < field_length; i++) {
                                        field_raw_int[i] = rawFileInt[fileIndex];
                                        fileIndex++;
                                        record_indx++;
                                    }
                                    //Example data for call method decoder==> xAC,[120,12,2, ...],xAC,1,listOfTrafficVolumes,SEQUENCE OF ChangeOfCharCondition
                                    String field_decode_data = decode.decoder(tag_1hex_str, field_raw_int, field_conf);
                                    mapFieldData.put(tag_1hex_str, field_decode_data);  // push to HashMapArray
                                    
                                    record_decode_data = record_decode_data + tag_1hex_str + ":" + field_decode_data + "|";     //Test , Debug

                                    System.out.println("length: " + field_length + "|0x" + String.format("%02X", field_length) + " Byte");    // Debug print
                                    System.out.println("Field Decode Data:" + field_decode_data);     // Debug print
//                        System.out.println("File Index:"+String.format("%02X", file_indx)); // Debug print
//                        record_indx < record_length
                                    System.out.println("file_indx:" + "0x" + String.format("%02X", fileIndex) + " record_length:" + "0x" + String.format("%02X", +record_length) + " record_indx:" + "0x" + String.format("%02X", +record_indx) + " field_length:" + "0x" + String.format("%02X", field_length));   //Debug
                                    System.out.println("fileLength:" + "0x" + String.format("%02X", fileLength) + " fileIndx:" + "0x" + String.format("%02X", +fileIndex)); //Debug
                                }
                            } else {
                                fileIndex = fileIndex + field_length + 1;   // Skip file index to next field
                                record_indx = record_indx + field_length + 1;     // Add field_length to field_indx for correct field_indx position
                            }
//                    System.out.println("Tag end:" + tag_list_length);   // Debug print
                            System.out.println("Field config: " + fieldConfig.substring(fieldConfig.indexOf(tag_1hex_str), tag_list_length));   // Debug print
//----------------------------- End of tag_1hex_str ---------------------------

//----------------------------- Start of tag_2hex_str ---------------------------
                        } else {
                            tag_2hex_str = tag_1hex_str.substring(0, 3) + String.format("%02X", rawFileInt[fileIndex + 1]);    // Convert Integer to Hex String for check Tag field 2Byte
//                    System.out.println("tag_2hex_str:" + tag_2hex_str);   //Debug
                            if (fieldConfig.contains(tag_2hex_str + ",")) {     // Check 2Byte Tag field is in Tag list table
                                System.out.print("Field Tag: " + tag_2hex_str + "   Address: 0x" + String.format("%02X", fileIndex) + "  "); // Debug print
                                fileIndex += 2;
                                record_indx += 2;
//                        System.out.println("File Index2Hex:" + String.format("%02X", file_indx)); // Debug print

                                int tag_list_length = fieldConfig.indexOf("|", fieldConfig.indexOf(tag_2hex_str));
                                String field_conf = fieldConfig.substring(fieldConfig.indexOf(tag_2hex_str), tag_list_length);
                                String field_decode = field_conf.substring(field_conf.indexOf(",") + 1, field_conf.indexOf(",") + 2);

//                        System.out.println("Field Skip:" + field_skip); // Debug
                                int field_length = rawFileInt[fileIndex];
                                if ("1".equals(field_decode)) {
                                    int[] field_raw_int = new int[field_length]; // Array for field raw data interger
                                    fileIndex++;
                                    record_indx++;
                                    for (int i = 0; i < field_length; i++) {
                                        field_raw_int[i] = rawFileInt[fileIndex];
                                        fileIndex++;
                                        record_indx++;
                                    }
                                    String field_decode_data = decode.decoder(tag_2hex_str, field_raw_int, field_conf);
                                    mapFieldData.put(tag_2hex_str, field_decode_data);  // push to HashMapArray
                                    
                                    
                                    record_decode_data = record_decode_data + tag_1hex_str + ":" + field_decode_data + "|";     //Test , Debug

                                    System.out.println("  length:" + field_length + "|0x" + String.format("%02X", field_length) + " Byte");    // Debug print
                                    System.out.println("Field Decode Data:" + field_decode_data);     // Debug print
//                        System.out.println("File Index:"+String.format("%02X", file_indx)); // Debug print
                                    System.out.println("file_indx:" + "0x" + String.format("%02X", fileIndex) + " record_length:" + "0x" + String.format("%02X", +record_length) + " record_indx:" + "0x" + String.format("%02X", +record_indx) + " field_length:" + "0x" + String.format("%02X", field_length));   //Debug
                                    System.out.println("fileLength:" + "0x" + String.format("%02X", fileLength) + " fileIndx:" + "0x" + String.format("%02X", +fileIndex)); //Debug                            

                                } else {
                                    fileIndex = fileIndex + field_length + 1;   // Skip file index to next field
                                    record_indx = record_indx + field_length + 1;     // Add field_length to field_indx for correct field_indx position
                                }
                                System.out.println("Field config: " + fieldConfig.substring(fieldConfig.indexOf(tag_2hex_str), tag_list_length));   // Debug print
//----------------------------- End of tag_2hex_str ---------------------------

//----------------------------- Start of tag_3hex_str ---------------------------
                            } else {
                                tag_3hex_str = tag_2hex_str.substring(0, 5) + String.format("%02X", rawFileInt[fileIndex + 2]);    // Convert Integer to Hex String for check Tag field 2Byte
                                System.out.println("tag_3hex_str:" + tag_3hex_str + ",");   //Debug
                                if (fieldConfig.contains(tag_3hex_str)) {     // Check 3Byte Tag field is in Tag list table
                                    System.out.print("Field Tag: " + tag_3hex_str + "   Address: 0x" + String.format("%02X", fileIndex) + "  "); // Debug print
                                    fileIndex += 3;
                                    record_indx += 3;
//                        System.out.println("File Index3Hex:" + String.format("%02X", file_indx)); // Debug print

                                    int tag_list_length = fieldConfig.indexOf("|", fieldConfig.indexOf(tag_3hex_str));
                                    String field_conf = fieldConfig.substring(fieldConfig.indexOf(tag_3hex_str), tag_list_length);
                                    String field_decode = field_conf.substring(field_conf.indexOf(",") + 1, field_conf.indexOf(",") + 2);

//                        System.out.println("Field Skip:" + field_skip); // Debug
                                    int field_length = rawFileInt[fileIndex];
                                    if ("1".equals(field_decode)) {
//                            System.out.println("File Index(StartValue):" + String.format("%02X", file_indx + 1)); // Debug print
                                        int[] field_raw_int = new int[field_length]; // Array for field raw data interger
                                        fileIndex++;
                                        record_indx++;
                                        for (int i = 0; i < field_length; i++) {
                                            field_raw_int[i] = rawFileInt[fileIndex];
                                            fileIndex++;
                                            record_indx++;
                                        }
                                        String field_decode_data = decode.decoder(tag_3hex_str, field_raw_int, field_conf);
                                        mapFieldData.put(tag_3hex_str, field_decode_data);  // push to HashMapArray
                                        record_decode_data = record_decode_data + tag_1hex_str + ":" + field_decode_data + "|";     //  Test , Debug

                                        System.out.println("  length:" + field_length + "|0x" + String.format("%02X", field_length) + " Byte");    // Debug print
                                        System.out.println("Field Decode Data:" + field_decode_data);     // Debug print
//                        System.out.println("File Index:"+String.format("%02X", file_indx)); // Debug print

                                    } else {
                                        fileIndex = fileIndex + field_length + 1;   // Skip file index to next field
                                        record_indx = record_indx + field_length + 1;     // Add field_length to field_indx for correct field_indx position
                                    }
//----------------------------- Debug ---------------------------
//                        System.out.println("Tag end:" + tag_list_length);   // Debug print
                                    System.out.println("Field config: " + fieldConfig.substring(fieldConfig.indexOf(tag_3hex_str), tag_list_length));   // Debug print
//----------------------------- End of tag_3hex_str ---------------------------

                                } else {
                                    System.out.println("Unknow Tag!!! File address: " + "0x" + String.format("%02X", fileIndex));       // Debug
                                    System.out.println("Raw data remaining in process: " + (record_indx - record_length) + " Byte");    // Debug
                                    
//                                    addressErrorList=addressErrorList+"0x" + String.format("%02X", fileIndex)+",";
                                    
                                    
//                                    FileIO.FileWriter(writeLogFileName,true,"Unknow Tag!!! address: " + "0x" + String.format("%02X", fileIndex)+lineSeparator);
//                                    FileIO.FileWriter(writeLogFileName,true,"Raw data remaining in process: " + (record_length-record_indx) + " Bytes"+lineSeparator);
                                    
//                                break;  // Break or Return
//*************  Skip to next Record and count error **********
                                    recordErrorCount++;
                                    recordErrorList = recordErrorList + recordCount + ",";
                                    addressErrorList=addressErrorList+"0x" + String.format("%02X", fileIndex)+",";
                                    fileIndex = sumRecordLength;     //Skip Record error

                                }
                            }
                        }
                        mapFieldData.put("sumDataUplink_record", Integer.toString(sumDataUplink_record));  // push to HashMapArray
                        mapFieldData.put("sumDataDownlink_record", Integer.toString(sumDataDownlink_record));  // push to HashMapArray
                        
                        System.out.println("SumDataVolumeUplink: " + sumDataUplink_record + " ;  SumDataVolumeDownlink: " + sumDataDownlink_record);
                    } //            while (record_indx < record_length);    
                    while (fileIndex < sumRecordLength);

                } catch (Exception ex) {
                    System.out.println("Record Error Raw data invalid format!!!");

                    recordErrorCount++;
                    recordErrorList = recordErrorList + recordCount + ",";
                    addressErrorList=addressErrorList+"0x" + String.format("%02X", fileIndex)+",";
                    fileIndex = sumRecordLength;     //Skip Record error
                }

            } else {
                if(notPGWCount<errCountLimit){      // Set Limit of Record Error Count = 5
                System.out.println("fileIndex:0x" + String.format("%02X", fileIndex) + " record_indx:0x" + String.format("%02X", record_indx) + " Not PGW-Record");
                FileIO.FileWriter(writeLogFileName,true,"*** Error *** ==> fileIndex:0x" + String.format("%02X", fileIndex) + " record_indx:0x" + String.format("%02X", record_indx) 
                        + " Not PGW-Record"+lineSeparator);
                recordErrorCount++;
                notPGWCount++;
                fileIndex++;    //Check next byte
                }else{
                    System.out.println("Skip File \""+fileName+"\" Because Record Error Count is Over Limit(>"+errCountLimit+")");
                    FileIO.FileWriter(writeLogFileName,true,"Skip File \""+fileName+"\" Because Record Error Count is Over Limit(>"+notPGWCount+") and RAW Data Remaining:"
                            +((fileLength)-fileIndex)+" Bytes"+lineSeparator);
//                    rawFileRemain=(fileLength-1)-fileIndex;
                    fileIndex=fileLength-1;       // Set fileIndex For Skip This File
                    recordErrorCount=recordErrorCount -errCountLimit;
                }
            }
            recordCount++;
            
            
            System.out.println("record_decode_data=> " + record_decode_data);     //Debux print text data record
            
            
//    ***** Arrange dataDecode order by field list before write to array buffer (recordDataBuffer)  after that write from array (recordDataBuffer) to Text File
            
//            mapFieldData
            
            Iterator<String> Vmap = mapFieldData.keySet().iterator();       // Debug print (Tag) = (DecodeData)
            while(Vmap.hasNext()){                                          // Debug print (Tag) = (DecodeData)
			String key = (String)(Vmap.next());  // Key         // Debug print (Tag) = (DecodeData)
			String val = mapFieldData.get(key); // Value        // Debug print (Tag) = (DecodeData)
			System.out.println(key + " = " + val);              // Debug print (Tag) = (DecodeData)
		}
            
//		mapFieldData.clear();
            
                int fieldTotal=arrayFieldList.length;
                String keyTag,fieldValue;
                String recordFieldData=sumDataUplink_record+"|"+sumDataDownlink_record+"|";
                String getFieldData;
                for(int i=0;i<fieldTotal;i++){
                    getFieldData=mapFieldData.get(arrayFieldList[i]);
                    if(null!=(getFieldData)){
                        recordFieldData=recordFieldData+getFieldData+"|";
                    }else{
                        recordFieldData=recordFieldData+"|";
                    }
                }
                
            System.out.println("recordFieldData:"+recordFieldData);     //Debug
            
            arrayRecordData.add(recordFieldData);   //write to array buffer before write to text file
            
            mapFieldData.clear();   // Clear array buffer
            
            
            sumDataUplink_record = 0;       //Reset value
            sumDataDownlink_record = 0;     //Reset value
             
        } while ((fileIndex + 1) < fileLength);     // +1Byet for adjust length (protect array out of bound)
//        rawFileRemain=(fileLength-1)-fileIndex;
//        catch(Exception ex){
//                
//                }

//----------------- End of File Summarry Report -------------------  
        sumRecord_allFile=sumRecord_allFile+(recordCount - 1);
        sumRecordError_allFile=sumRecordError_allFile+recordErrorCount;
        
        
        
        
        if (recordErrorList.length() > 0) {
            recordErrorList = " {[Record:"+recordErrorList.substring(0, (recordErrorList.length() - 1))+"],";
        }
        if (addressErrorList.length() > 0) {
            addressErrorList =  "[Address:"+addressErrorList.substring(0, (addressErrorList.length() - 1))+"]}";    
             }
        
        
        System.out.println("");
        System.out.println("***************************************** End of file *****************************************");
//        System.out.println("Record Total:" + (recordCount - 1) + " ;  Records Error:" + recordErrorCount + "{["+recordErrorList+"]" +"["+addressErrorList+"]}");
        System.out.println("Record Total:" + DecimalFormat.format(recordCount - 1) + " ;  Records Error:" + DecimalFormat.format(recordErrorCount) 
                +recordErrorList +addressErrorList);
        
        
        System.out.println("Raw Data Remaining(can't process):" + ((fileLength-1)-fileIndex) + " Byte");
        System.out.println("SumDataVolumeUplink: " + sumDataUplink_file + " ;  SumDataVolumeDownlink: " + sumDataDownlink_file);

        FileIO.FileWriter(writeLogFileName,true,"Record Total:" + DecimalFormat.format(recordCount - 1) + " ;  Records Error:" + DecimalFormat.format(recordErrorCount) 
                +recordErrorList +addressErrorList+ lineSeparator 
                + "Raw Data Remaining(can't process):" + DecimalFormat.format((fileLength-1)-fileIndex) + " Bytes"+lineSeparator);
       
        FileIO.FileWriter(writeLogFileName,true,"SumDataVolumeUplink: " + DecimalFormat.format(sumDataUplink_file) 
                + " ;  SumDataVolumeDownlink: " + DecimalFormat.format(sumDataDownlink_file)+lineSeparator);
        
        if(recordErrorCount>0|notPGWCount>0){                                 // if have record error increment file error counter
        rawFileErrorList=rawFileErrorList+(rawFileNo+1)+",";
        rawFileErrorCount++;
        listRawFileError=listRawFileError+DecimalFormat.format(rawFileErrorCount)+". (SeqNo:"+DecimalFormat.format((rawFileNo+1))+") "+fileName+lineSeparator;
        }
        
        
        
//         
//        System.out.println("");                         // Debug
//        for (String temp : arrayFieldList){             // Debug
//            System.out.print(temp+"|");                 // Debug
//            }                                           // Debug
//        System.out.println("");                         // Debug

//        String writeFileName=pathDecodeData+rawFileName+".txt";
        String writeFileName=pathDecodeData+fileName+".txt";
//
//------------ Check Folder Existing Before Write File ------------------------
//        
        File pathTextData = new File(pathDecodeData);
         if (pathTextData.exists()) {
             FileIO.bufferWriter(writeFileName,arrayRecordData);     // Write output to text file
        } else {
            System.out.println("Directory " + pathDecodeData + " is not exists !!!");
        }
        
        File textData = new File(writeFileName);
         if (textData.exists()) {
             System.out.println("Decode File: "+fileName+" FileSize "+DecimalFormat.format(fileLength)+" Bytes ==> "+DecimalFormat.format((recordCount-1))+"/"+DecimalFormat.format(recordErrorCount)+" Records(Total/Error)");

        } else {
            System.out.println("Directory " + pathDecodeData + " is not exists !!!");
        }
        
//        FileIO.bufferWriter(writeFileName,arrayRecordData);     // Write output to text file
        
         
// ################################################# Write log file with bufferWrite #################################################
//    writeFileName=pathLogData+"LogDecoder.txt";
//    
//         File pathLog = new File(pathLogData);
//         if (pathLog.exists()) {
//             FileIO.bufferWriter(writeFileName,arrayLogData);     // Write output to text file
//        } else {
//            System.out.println("Directory " + pathLogData + " does not exists !!!");
//        }
//        
//        File logData = new File(writeFileName);
//         if (logData.exists()) {
//             System.out.println("Decode File: "+fileName+" FileSize "+DecimalFormat.format(fileLength)+" Byte ==> Decoded "+DecimalFormat.format((recordCount-1))+"/"+DecimalFormat.format(recordErrorCount)+" Records(Total/Error)");
//
//        } else {
//            System.out.println("Directory " + pathDecodeData + " does not exists !!!");
//        }
// ###################################################################################################################################     

         
        
        
//        arrayRecordData.stream().forEach((temp) -> {    // Debug
//            System.out.println(temp);                   // Debug
//        });                                             // Debug
//        System.out.println("");                         // Debug
//        System.out.println("arrayRecordData size (Before clear):"+arrayRecordData.size());  // Debug
//        System.out.println("arrayRecordData0 (Before clear):"+arrayRecordData.get(0));      // Debug
        
        arrayRecordData.clear();
        
        
//        System.out.println("arrayRecordData size (After clear):"+arrayRecordData.size());   // Debug
        
//                
                  
    }
   
       
         
         if (rawFileErrorCount > 0) {
            rawFileErrorList = " {SeqNo:" + rawFileErrorList.substring(0, (rawFileErrorList.length() - 1)) + "}";
        }
        FileIO.FileWriter(writeLogFileName,true,lineSeparator+"\r\n============================================== Decode Summary =============================================="+lineSeparator);
//FileIO.FileWriter(writeLogFileName,true,"sumDataUplink_allFile: "+DecimalFormat.format(sumDataUplink_allFile)+" Bytes"+lineSeparator);
        
        
        FileIO.FileWriter(writeLogFileName,true,"Total File: " + DecimalFormat.format(totalRawFile) + " ;  File Error:" + DecimalFormat.format(rawFileErrorCount) 
                +rawFileErrorList+lineSeparator);
        FileIO.FileWriter(writeLogFileName,true,"List of Error File:\r\n");
        FileIO.FileWriter(writeLogFileName,true,listRawFileError);
        
        
        FileIO.FileWriter(writeLogFileName,true,"Total Record: " + DecimalFormat.format(sumRecord_allFile) + " ;  Total Record Error: " 
                + DecimalFormat.format(sumRecordError_allFile)+" (Exclude File Error)"+lineSeparator);
        
        FileIO.FileWriter(writeLogFileName,true,"Total DataVolumeUplink: " + DecimalFormat.format(sumDataUplink_allFile) + " ;  Total DataVolumeDownlink: " 
                + DecimalFormat.format(sumDataDownlink_allFile)+lineSeparator);
        
//        FileIO.FileWriter(writeLogFileName,true,"Finish Time "+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))+lineSeparator);      // Start of log file
        FileIO.FileWriter(writeLogFileName,true,lineSeparator+"====================================== Decode End "
                +new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+" ======================================"+lineSeparator);
       
       
        System.out.print(lineSeparator+"\r\n============================================== Decode Summary =============================================="+lineSeparator);
        System.out.print("Total File:" + DecimalFormat.format(totalRawFile) + " ;  File Error:" + DecimalFormat.format(rawFileErrorCount) 
                +rawFileErrorList+lineSeparator);
        System.out.print("List of Error File:\r\n");
        System.out.print(listRawFileError);
        System.out.print("Total Record: " + DecimalFormat.format(sumRecord_allFile) + " ;  Total Record Error: " 
                + DecimalFormat.format(sumRecordError_allFile)+" (Exclude File Error)"+lineSeparator);
        System.out.print("Total DataVolumeUplink: " + DecimalFormat.format(sumDataUplink_allFile) + " ;  Total DataVolumeDownlink: " 
                + DecimalFormat.format(sumDataDownlink_allFile)+lineSeparator);
        System.out.print(lineSeparator+"====================================== Decode End "
                +new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+" ======================================"+lineSeparator);
        
        
       
        
        
        
        sumDataUplink_allFile=0;
        sumDataDownlink_allFile=0;
        sumRecord_allFile=0;
        sumRecordError_allFile=0;
                
}
}
