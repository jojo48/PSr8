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
import java.util.List;
import java.util.Locale;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Samreng 2016-05-25 19:58
 */
public class PGW2TXT {

    public static final String lineSeparator = System.getProperty("line.separator");
    public static final String pathSeparator = File.separator;
    
    public static long sumDataUplink_record = 0;
    public static long sumDataDownlink_record = 0;
    public static long sumDataUplink_file = 0;
    public static long sumDataDownlink_file = 0;
    public static long sumDataUplink_allFile = 0;
    public static long sumDataDownlink_allFile = 0;
    public static long sumRecord_allFile = 0;
    public static long sumRecordError_allFile = 0;

    public void DataUplink(long dataUplink) {
        sumDataUplink_allFile = sumDataUplink_allFile + dataUplink;
        sumDataUplink_file = sumDataUplink_file + dataUplink;
        sumDataUplink_record = sumDataUplink_record + dataUplink;
    }

    public void DataDownlink(long dataDownlink) {
        sumDataDownlink_allFile = sumDataDownlink_allFile + dataDownlink;
        sumDataDownlink_file = sumDataDownlink_file + dataDownlink;
        sumDataDownlink_record = sumDataDownlink_record + dataDownlink;
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {

        Locale.setDefault(Locale.US);
        DecimalFormat DecimalFormat = new DecimalFormat("#,###,##0");

        FileIO FileIO = new FileIO();
        String pathFileConfig = "D:\\Training\\Java\\SourceCode\\pgw\\PGWr8.csv";
        String fieldListFileName = "FieldList.txt";
        String[] readConfig = FileIO.ReadFileConfig(pathFileConfig);    // Return with array of header group
        String pathConfig = readConfig[0];                                  // Array readConfig address 0 = String of Path Configuration
        String fieldConfig = readConfig[1];                                 // Array readConfig address 1 = String of Tag Field list
        String record_decode_data;                                      // Recodr data buffer
        
//        System.out.println("fieldConfig:"+fieldConfig);     // Debug Display fieldConfig

//#################################### Read Configuration and Store to ArrayList ###############################################
//
        int pathConfig_indxStart;
        int pathConfig_indxEnd = 0;
        int offsetStart = 0;                                            // use for check and Skip "|"

//------------------------- Create Array List Path and Config -------------------
//        
        HashMap<String, String> listPathConfig = new HashMap<>();       //Create HashMap for store field data decoded
        do {
            pathConfig_indxStart = pathConfig_indxEnd + offsetStart;
            pathConfig_indxEnd = pathConfig.indexOf("|", pathConfig_indxEnd + 1);
            String pathItem = pathConfig.substring(pathConfig_indxStart, pathConfig.indexOf("|", pathConfig_indxEnd));
            String key = pathItem.substring(0, pathItem.indexOf("="));
            String val = pathItem.substring(pathItem.indexOf("=") + 1, pathItem.length());
            listPathConfig.put(key, val);                               // push to HashMapArray
            offsetStart = 1;                                            // Skip "|"
        } while (pathConfig_indxEnd + 1 < pathConfig.length());

        String pathRawData = listPathConfig.get("RawFile");
        String pathDecodeFile = listPathConfig.get("DecodeFile");
        String pathDecodeFileError = listPathConfig.get("DecodeFileError");
        String pathBackupRawFile = listPathConfig.get("BackupRawFile");
        String pathLogData = listPathConfig.get("LogFile");
        String pathBackupRawFileError = listPathConfig.get("BackupRawFileError");
        String pathDecodeDetailFile = listPathConfig.get("DecodeDetailFile");
        String rawFileExtension = listPathConfig.get("RawFileExtension");
        String copyRawToBackup = (listPathConfig.get("CopyRawToBackup")).toUpperCase();
        String backupWithGzip = (listPathConfig.get("BackupWithGzip")).toUpperCase();
        String deleteOriginalRawFile = (listPathConfig.get("DeleteOriginalRawFile")).toUpperCase();
        String createDetailTextFile = (listPathConfig.get("CreateDetailTextFile")).toUpperCase();
        String createOutputSelectedFieldOnly = (listPathConfig.get("CreateOutputSelectedFieldOnly")).toUpperCase();
        int recordErrorCountLimit = Integer.parseInt(listPathConfig.get("RecordErrorCountLimit"));

        System.out.println(lineSeparator + "====================================== Start Time "
                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " ======================================" + lineSeparator);
        System.out.println("------------------------------------ Path Configuration ------------------------------------");
        System.out.println("PathRawFile                   = " + pathRawData);
        System.out.println("PathDecodeFile                = " + pathDecodeFile);
        System.out.println("PathDecodeFileError           = " + pathDecodeFileError);
        System.out.println("PathDecodeDetailFile          = " + pathDecodeDetailFile);
        System.out.println("PathBackupRawFile             = " + pathBackupRawFile);
        System.out.println("PathLogFile                   = " + pathLogData);
        System.out.println("PathBackupErrorFile           = " + pathBackupRawFileError);
        System.out.println("FieldListFileName             = " + fieldListFileName);
        System.out.println("RawFileExtension              = " + rawFileExtension);
        System.out.println("CopyRawToBackup               = " + copyRawToBackup);
        System.out.println("BackupWithGzip                = " + backupWithGzip);
        System.out.println("DeleteOriginalRawFile         = " + deleteOriginalRawFile);
        System.out.println("RecordErrorCountLimit         = " + recordErrorCountLimit);
        System.out.println("CreateDetailTextFile          = " + createDetailTextFile);
        System.out.println("CreateOutputSelectedFieldOnly = " + createOutputSelectedFieldOnly);

        FileIO.createDirectory(pathDecodeFile);
        FileIO.createDirectory(pathDecodeFileError);
        FileIO.createDirectory(pathDecodeDetailFile);
        FileIO.createDirectory(pathBackupRawFile);
        FileIO.createDirectory(pathLogData);
        FileIO.createDirectory(pathBackupRawFileError);

        List<String> arrayListFile = FileIO.ListFileByExtension(pathRawData, rawFileExtension);   // List RAW File from folder pathRawData only define file extenion
        
        String writeLogFileName = pathLogData + "LogDecoder.txt";
        String writeFieldConfigFileName = pathLogData + "FieldConfig.txt";

        FileIO.FileWriter(writeLogFileName, true, lineSeparator + "====================================== Start Time "
                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " ======================================" + lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, lineSeparator + "------------------------------------ Path Configuration ------------------------------------" + lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, "PathRawFile                    = " + pathRawData + lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, "PathDecodeFile                 = " + pathDecodeFile + lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, "PathDecodeFileError            = " + pathDecodeFileError + lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, "PathDecodeDetailFile           = " + pathDecodeDetailFile + lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, "PathBackupRawFile              = " + pathBackupRawFile + lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, "PathLogFile                    = " + pathLogData + lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, "PathBackupErrorFile            = " + pathBackupRawFileError + lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, "FieldListFileName              = " + fieldListFileName + lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, "RawFileExtension               = " + rawFileExtension + lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, "CopyRawToBackup                = " + copyRawToBackup + lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, "BackupWithGzip                 = " + backupWithGzip + lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, "DeleteOriginalRawFile          = " + deleteOriginalRawFile + lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, "RecordErrorCountLimit          = " + recordErrorCountLimit + lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, "CreateOutputSelectedFieldOnly  = " + createOutputSelectedFieldOnly + lineSeparator);        
        FileIO.FileWriter(writeLogFileName, true, lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, "Total RAW File = " + arrayListFile.size() + lineSeparator);

        System.out.println(lineSeparator + "Total RAW File = " + arrayListFile.size());

//################################################# Create Array Filed List ########################################################            
//                 
            int fieldCount = Integer.parseInt(readConfig[2]);
            String[] arrayFieldList = new String[fieldCount + 2];       // array for store field list  //+2 for sumUplink_record, sumDownlink_record
            arrayFieldList[0] = "sumDataUplink_record";                            // add Sum of record Data Uplink
            arrayFieldList[1] = "sumDataDownlink_record";                          // add Sum of record Data Downlink
            for (int i = 0; i < fieldCount; i++) {
                arrayFieldList[i + 2] = readConfig[i + 3];
//                System.out.println("arrayFieldList["+(i+2)+"]:"+arrayFieldList[i + 2]);     // Debug dump array field list
            }
 
//######################################## Create Array Filed List (Only Selected Field) ###########################################            
//         
//            *********************************
////          **** map key with field name ****
//            *********************************
//            HashMap<String, String> mapFieldData = new HashMap<>();       //Create HashMap for store field data decoded
//            mapFieldData.put(tag_1hex_str, field_decode_data);            // push to HashMapArray
//            
//            Iterator<String> Vmap = mapFieldData.keySet().iterator();         // Debug print (Tag) = (DecodeData)    
//            while(Vmap.hasNext()){                                            // Debug print (Tag) = (DecodeData)
//			String key = (String)(Vmap.next());  // Key             // Debug print (Tag) = (DecodeData)
//			String val = mapFieldData.get(key); // Value            // Debug print (Tag) = (DecodeData)
//			System.out.println(key + " = " + val);                  // Debug print (Tag) = (DecodeData)
//		}             
            
//################ Create Array Field List (Only Enable Field & mapArray Filed List (FieldHexString = FieldName) ###################
//         
//            *********************************
////          **** map key with field name ****
//            *********************************
//                
            HashMap<String, String> mapHexString2FieldName = new HashMap<>();       //Create HashMap for store FieldString = FieldName
            ArrayList<String> arrayListFieldEnableOnly = new ArrayList<>();       //Create Array for Store List of Field is not Skip (0=Skip, 1=Decode)
            
            mapHexString2FieldName.put("sumDataUplink_record","sumDataUplink_record");  //add mapField by manual (key="sumDataUplink_record",value="sumDataUplink_record")
            mapHexString2FieldName.put("sumDataDownlink_record","sumDataDownlink_record");  //add mapField by manual (key="sumDataDownlink_record",value="sumDataDownlink_record")
            arrayListFieldEnableOnly.add(0,"sumDataUplink_record");          //add field by manual (address 0)
            arrayListFieldEnableOnly.add(1,"sumDataDownlink_record");        //add field by manual (address 1)
            
////############################ Create Field List Text File (For Map With Text File Output Decode Data) #############################
////            
//            String outputFieldList="";          // Field list for create text file (for map with decode data field)
//            int fieldTotal=arrayFieldList.length;
//            
//            outputFieldList=outputFieldList+("========================="+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
//                    +"========================="+lineSeparator;
//            outputFieldList=outputFieldList+"[ListAllField="+fieldTotal+"]"+lineSeparator;
//            
//            for(int i=0;i<fieldTotal;i++){
//                        outputFieldList=outputFieldList+arrayFieldList[i]+"|";
//                    }
//            outputFieldList=outputFieldList+lineSeparator;
//            
//            
//            
//            fieldTotal=arrayListFieldEnableOnly.size();
//            outputFieldList=outputFieldList+lineSeparator+"[ListEnableField="+fieldTotal+"]"+lineSeparator;
//            for(int i=0;i<fieldTotal;i++){
//                        outputFieldList=outputFieldList+arrayFieldList[i]+"|";
//                    }
//            outputFieldList=outputFieldList+lineSeparator+lineSeparator;
//            
//            String writeFieldListFileName=pathLogData+fieldListFileName;
//            FileIO.FileWriter(writeFieldListFileName, true,outputFieldList);        // Write Text File "FieldList.txt"
//            
            
            
            
//********************** Check may be not use this for loop *********************************************
            
            for(int i=2;i<(arrayFieldList.length-1);i++){             // 2=Skip(sumUpLink,sumDownLink) // -1=array is count from 0
//            String fieldHexStr = arrayFieldList[i].substring(0,(arrayFieldList[i].indexOf(","))-1);
                              
            }
//*******************************************************************************************************       
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
        int offsetPipeNext=fieldConfig.indexOf("|")+1;             //=fieldConfig.indexOf("|",0)+1;
        int offsetPipeStart=0;
        int fieldConfLen=fieldConfig.length();
        int countField=1;                           // Debug Test
        
        for(int i=0;i<fieldConfLen;i++){
        
        String fieldConf = fieldConfig.substring(offsetPipeStart,offsetPipeNext-1);  //,(fieldConfig.indexOf("|",fieldConfig.indexOf("|")+1)));
//        System.out.println("fieldConf"+countField+":"+fieldConf);                   //Debug Test
        
        int tagStart=0;
        int tagEnd=fieldConf.indexOf(",");
        String  tagHexStr = fieldConf.substring(tagStart,tagEnd);
        tagStart=tagEnd+1;
        tagEnd=fieldConf.indexOf(",",tagStart);
        String  skipField = fieldConf.substring(tagStart,tagEnd);
        tagStart=tagEnd+1;
        tagEnd=fieldConf.indexOf(",",tagStart);
        String  fieldName = fieldConf.substring(tagStart,tagEnd);
       
//  System.out.println(tagHexStr+"   "+skipField+"   "+fieldName);                                            // Debug
       
        mapHexString2FieldName.put(tagHexStr,fieldName);                    // push to HashMapArray
        if(skipField.equals("1")){                                          // Check Skip or NotSkip (0=Skip,1=NotSkip)
        arrayListFieldEnableOnly.add(tagHexStr);                                  // Add List of Field to Decode (Enable)
        }
        
        offsetPipeStart=offsetPipeNext;
        offsetPipeNext=fieldConfig.indexOf("|",offsetPipeStart)+1;
        i=offsetPipeStart;
//        countField++;                                                   // Debug Test
        }
        
//        for(int i=0;i<arrayListFieldEnableOnly.size();i++){                   // Debug
//            System.out.println(i+":"+arrayListFieldEnableOnly.get(i));        // Debug
//        }                                                               // Debug
        
        
//############################ Create Field List Text File (For Map With Text File Output Decode Data) #############################
//          
            String outputFieldList="";
            String outputFieldListHex="";          // Field list for create text file (for map with decode data field)
            String outputFieldListName="";
            int fieldTotal=arrayFieldList.length;
            
            outputFieldList=outputFieldList+("======================="+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))+"======================="+lineSeparator;
            outputFieldList=lineSeparator+outputFieldList+"[ListAllField="+fieldTotal+"]"+lineSeparator+lineSeparator;
            
            for(int i=0;i<fieldTotal;i++){
                        outputFieldListHex=outputFieldListHex+arrayFieldList[i]+"|";
                        outputFieldListName=outputFieldListName+mapHexString2FieldName.get(arrayFieldList[i])+"|";
                    }
            outputFieldList=outputFieldList+outputFieldListHex+lineSeparator;
            outputFieldList=lineSeparator+outputFieldList+lineSeparator+outputFieldListName+lineSeparator+lineSeparator;
            
            
            outputFieldListHex="";      // Clear Buffer
            outputFieldListName="";     // Clear Buffer
            fieldTotal=arrayListFieldEnableOnly.size();
            
            outputFieldList=outputFieldList+"----------------------------------------------------------------"+lineSeparator;
            outputFieldList=outputFieldList+"[ListEnableField="+fieldTotal+"]"+lineSeparator+lineSeparator;
            for(int i=0;i<fieldTotal;i++){
                        outputFieldListHex=outputFieldListHex+arrayListFieldEnableOnly.get(i)+"|";
                        outputFieldListName=outputFieldListName+mapHexString2FieldName.get(arrayListFieldEnableOnly.get(i))+"|";
                    }
            outputFieldList=outputFieldList+outputFieldListHex+lineSeparator+lineSeparator;
            outputFieldList=outputFieldList+outputFieldListName+lineSeparator;
            
            outputFieldList=outputFieldList+"================================================================"+lineSeparator;
            
            String writeFieldListFileName=pathLogData+fieldListFileName;
            FileIO.FileWriter(writeFieldListFileName, true,outputFieldList);        // Write Text File "FieldList.txt"
  
        
        
        
        
        
        
        
        
        
        
        
           
            
//            Iterator<String> Vmap = mapHexString2FieldName.keySet().iterator();         // Debug print (Tag) = (DecodeData)
//            int l=0;                                                                    // Debug print (Tag) = (DecodeData)
//            while(Vmap.hasNext()){                                                      // Debug print (Tag) = (DecodeData)
//			String key = (String)(Vmap.next());  // Key                     // Debug print (Tag) = (DecodeData)
//			String val = mapHexString2FieldName.get(key); // Value          // Debug print (Tag) = (DecodeData)
//			System.out.println(l+":"+key + " = " + val);                    // Debug print (Tag) = (DecodeData)
//                        l++;                                                            // Debug print (Tag) = (DecodeData)
//		}                                                                       // Debug print (Tag) = (DecodeData)
            
            
//################################################# Create Global Variable #########################################################            
// 
        int totalRawFile = arrayListFile.size();
        int rawFileErrorCount = 0;
        String listRawFileError = "";
        String rawFileErrorList = "";
        String backupRawDestination;
        
//############################################# Start loop for run all RAW file ####################################################
//
        for (int rawFileNo = 0; rawFileNo < totalRawFile; rawFileNo++) {

//---------------- Create Variable & array use in file decoder ------------------
            String fileName = arrayListFile.get(rawFileNo);
            String rawFilePathName = pathRawData + fileName;
            int notPGWCount = 0;                                        // Counter for Count Not PGW Record If Over Limit Skip To Next Raw File
            ArrayList<String> arrayRecordData = new ArrayList<>();      // Create ListArray for store record decode data before write to text file
            ArrayList<String> arrayDetailDecodeData = new ArrayList<>();      // Create ListArray for store record decode data before write to text file
            int fileIndex = 0;
            String tag_1hex_str, tag_2hex_str, tag_3hex_str;
            int record_indx = 0;
            int recordCount = 1;
            int recordErrorCount = 0;
            String addressErrorList = "";
            String recordErrorList = "";    //use for add to integer for convert integer to string
            int sumRecordLength = 0;
//            String detailDecodeData=""; // Buffer For Store Detail Decode Data

//----------------------------- Start of log file ------------------------
            System.out.println(lineSeparator + "------------------------------------ (FileSeqNo:" + DecimalFormat.format((rawFileNo + 1)) + "/"
                    + DecimalFormat.format(totalRawFile) + ") " + fileName + " ------------------------------------");
            System.out.println("File Start Time " + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));      // Start of log file
            FileIO.FileWriter(writeLogFileName, true, lineSeparator + "------------------------------------ (FileSeqNo:" + DecimalFormat.format((rawFileNo + 1))
                    + "/" + DecimalFormat.format(totalRawFile) + ") " + fileName + " ------------------------------------" + lineSeparator);
            FileIO.FileWriter(writeLogFileName, true, "File Start Time " + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())) + lineSeparator);
            RawFile readFile = new RawFile(); // Create Object from class readRaw
            int[] rawFileInt = readFile.readBinaryFile(rawFilePathName);
            int fileLength = rawFileInt.length;
            System.out.println("FileSize " + DecimalFormat.format(fileLength) + " Bytes");
            FileIO.FileWriter(writeLogFileName, true, "FileSize " + DecimalFormat.format(fileLength) + " Bytes" + lineSeparator);

            FieldDecoder decode = new FieldDecoder();       // Create Object from class FieldDecoder
            RawFile getRaw = new RawFile();                 // Create Object from class RawFile
            DataConverter data_conv = new DataConverter();  // Create Object from class DataConverter

//------------------------- Start of loop file process -----------------------------------
            do {
                record_decode_data = "";    // Reset Recodr data buffer
//                String detailDecodeData=""; // Buffer For Store Detail Decode Data
                String field_hex_str = "";  // Reset old record length
                HashMap<String, String> mapFieldData = new HashMap<>();  //Create HashMap for store field data decoded

//------------------------- Start of loop record process ------------------------
                String record_type_hex_str = String.format("%02X", rawFileInt[fileIndex]) + String.format("%02X", rawFileInt[fileIndex + 1]);
                if ("BF4F".equals(record_type_hex_str)) {
                    notPGWCount = 0;          // Reset Record Error Counter
                    fileIndex = fileIndex + 2;
                    sumRecordLength = sumRecordLength + 2;
                    int record_len_byte = rawFileInt[fileIndex] - 0x80; //Check number of Byte is define Record length (1 or 2 Byte)
                    fileIndex++;
                    sumRecordLength = sumRecordLength + record_len_byte + 1;  // +1 = LengthByte
                    for (int i = 0; i < record_len_byte; i++) {
                        field_hex_str = field_hex_str + String.format("%02X", rawFileInt[fileIndex]);
                        fileIndex++;
                    }
                    field_hex_str = "0x" + field_hex_str; // Add prefix Hex String format (0x)
                    int record_length = data_conv.hexString2int(field_hex_str); // Calculate Record length ( xxx Byte)
                    sumRecordLength = sumRecordLength + record_length;

//------------------- Start of loop record process ------------------------
                    try {
                        do {
//------------ Start of Check Tag field length 1,2,3Byte ------------------
                            tag_1hex_str = "x" + String.format("%02X", rawFileInt[fileIndex]);    // Convert Integer to Hex String for check Tag field 1Byte

//----------------------------- Start of tag_1hex_str ---------------------------
                            if (fieldConfig.contains(tag_1hex_str + ",")) {     // Check 1Byte Tag field is in Tag list table
                                fileIndex++;
                                record_indx++;
                                int tag_list_length = fieldConfig.indexOf("|", fieldConfig.indexOf(tag_1hex_str));
                                String field_conf = fieldConfig.substring(fieldConfig.indexOf(tag_1hex_str), tag_list_length);
                                String field_decode = field_conf.substring(field_conf.indexOf(",") + 1, field_conf.indexOf(",") + 2);
                                int field_length = rawFileInt[fileIndex];
                                if ((field_decode.equals("1"))|(tag_1hex_str.equals("xAC"))) {          // Check Field Config is Enable(=1) OR Field=xAC(List of traffic volume)

//********** Special check for Tag [xAC x81 (x80...x83)] not found in manual **********
                                    if (("xAC".equals(tag_1hex_str)) && (rawFileInt[fileIndex] > 0x80) && (rawFileInt[fileIndex + 2] == 0x30)) {
                                        fileIndex += 2;       //Skip 2Byte  (Skip 0x8.., 0x8..)
                                        record_indx += 2;     //Skip 2Byte  (Skip 0x8.., 0x8..)

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
                                            record_indx++;
                                        }
                                        String field_decode_data = decode.decoder(tag_1hex_str, field_raw_int, field_conf);
                                        mapFieldData.put(tag_1hex_str, field_decode_data);  // push to HashMapArray
                                        record_decode_data = record_decode_data + tag_1hex_str + ":" + field_decode_data + "|";     //Test , Debug
                                    } //********** End of Special check for Tag [xAC x81 (x80...x83)] not found in manual **********
                                    else {
                                        int[] field_raw_int = new int[field_length]; // Array for field raw data interger
                                        fileIndex++;
                                        record_indx++;
                                        for (int i = 0; i < field_length; i++) {
                                            field_raw_int[i] = rawFileInt[fileIndex];
                                            fileIndex++;
                                            record_indx++;
                                        }
                                        String field_decode_data = decode.decoder(tag_1hex_str, field_raw_int, field_conf);
                                        mapFieldData.put(tag_1hex_str, field_decode_data);  // push to HashMapArray
                                        record_decode_data = record_decode_data + tag_1hex_str + ":" + field_decode_data + "|";     //Test , Debug
                                    }
                                } else {
                                    fileIndex = fileIndex + field_length + 1;           // Skip file index to next field
                                    record_indx = record_indx + field_length + 1;       // Add field_length to field_indx for correct field_indx position
                                }
//----------------------------- End of tag_1hex_str ---------------------------

//----------------------------- Start of tag_2hex_str ---------------------------
                            } else {
                                tag_2hex_str = tag_1hex_str.substring(0, 3) + String.format("%02X", rawFileInt[fileIndex + 1]);    // Convert Integer to Hex String for check Tag field 2Byte
                                if (fieldConfig.contains(tag_2hex_str + ",")) {     // Check 2Byte Tag field is in Tag list table
                                    fileIndex += 2;
                                    record_indx += 2;
                                    int tag_list_length = fieldConfig.indexOf("|", fieldConfig.indexOf(tag_2hex_str));
                                    String field_conf = fieldConfig.substring(fieldConfig.indexOf(tag_2hex_str), tag_list_length);
                                    String field_decode = field_conf.substring(field_conf.indexOf(",") + 1, field_conf.indexOf(",") + 2);
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
                                    } else {
                                        fileIndex = fileIndex + field_length + 1;   // Skip file index to next field
                                        record_indx = record_indx + field_length + 1;     // Add field_length to field_indx for correct field_indx position
                                    }
//----------------------------- End of tag_2hex_str ---------------------------

//----------------------------- Start of tag_3hex_str ---------------------------
                                } else {
                                    tag_3hex_str = tag_2hex_str.substring(0, 5) + String.format("%02X", rawFileInt[fileIndex + 2]);    // Convert Integer to Hex String for check Tag field 2Byte
                                    if (fieldConfig.contains(tag_3hex_str)) {     // Check 3Byte Tag field is in Tag list table
                                        fileIndex += 3;
                                        record_indx += 3;
                                        int tag_list_length = fieldConfig.indexOf("|", fieldConfig.indexOf(tag_3hex_str));
                                        String field_conf = fieldConfig.substring(fieldConfig.indexOf(tag_3hex_str), tag_list_length);
                                        String field_decode = field_conf.substring(field_conf.indexOf(",") + 1, field_conf.indexOf(",") + 2);
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
                                            String field_decode_data = decode.decoder(tag_3hex_str, field_raw_int, field_conf);
                                            mapFieldData.put(tag_3hex_str, field_decode_data);  // push to HashMapArray
                                            record_decode_data = record_decode_data + tag_1hex_str + ":" + field_decode_data + "|";     //  Test , Debug
                                        } else {
                                            fileIndex = fileIndex + field_length + 1;   // Skip file index to next field
                                            record_indx = record_indx + field_length + 1;     // Add field_length to field_indx for correct field_indx position
                                        }
                                    } else {
//*************  Skip to next Record and count error **********
                                        mapFieldData.put(tag_2hex_str, "UnknowTagField");  // push to HashMapArray
                                        record_decode_data = record_decode_data + tag_1hex_str + ":" + "UnknowTagField" + "|";     //Test , Debug
                                        recordErrorCount++;
                                        recordErrorList = recordErrorList + recordCount + ",";
                                        addressErrorList = addressErrorList + "0x" + String.format("%02X", fileIndex) + ",";
                                        fileIndex = sumRecordLength;     //Skip Record error
                                    }
                                }
                            }
                            mapFieldData.put("sumDataUplink_record", Long.toString(sumDataUplink_record));       // push to HashMapArray
                            mapFieldData.put("sumDataDownlink_record", Long.toString(sumDataDownlink_record));   // push to HashMapArray
                        } while (fileIndex < sumRecordLength);
                    } catch (Exception ex) {
                        recordErrorCount++;
                        recordErrorList = recordErrorList + recordCount + ",";
                        addressErrorList = addressErrorList + "0x" + String.format("%02X", fileIndex) + ",";
                        fileIndex = sumRecordLength;                //Skip Record error
                    }
                } else {
                    if (notPGWCount < recordErrorCountLimit) {      // Set Limit of Record Error Count = 5
                        System.out.println("fileIndex:0x" + String.format("%02X", fileIndex) + " record_indx:0x" + String.format("%02X", record_indx) + " Not PGW-Record");
                        FileIO.FileWriter(writeLogFileName, true, "*** Error *** ==> fileIndex:0x" + String.format("%02X", fileIndex) + " record_indx:0x" + String.format("%02X", record_indx)
                                + " Not PGW-Record" + lineSeparator);
                        recordErrorCount++;
                        notPGWCount++;
                        fileIndex++;    //Check next byte
                    } else {
                        System.out.println("Skip File \"" + fileName + "\" Because Record Error Count is Over Limit(>" + recordErrorCountLimit + ")");
                        FileIO.FileWriter(writeLogFileName, true, "Skip File \"" + fileName + "\" Because Record Error Count is Over Limit(>" + notPGWCount + ") and RAW Data Remaining:"
                                + ((fileLength) - fileIndex) + " Bytes" + lineSeparator);
                        fileIndex = fileLength - 1;       // Set fileIndex For Skip This File
                        recordErrorCount = recordErrorCount - recordErrorCountLimit;
                    }
                }
//                recordCount++;

//------***** Arrange dataDecode order by field list before write to array buffer (recordDataBuffer)  after that write from array (recordDataBuffer) to Text File
                //------***** mapFieldData
                
                
                
                
            if(createDetailTextFile.equals("YES")){
                
              if(createOutputSelectedFieldOnly.equals("NO")){           // Create Text Output All Field List (Include Blank Field)
                
                    fieldTotal = arrayFieldList.length;
                    
//                String recordFieldData = "";     //Create Variable and Assign Value
                String getFieldData;
                String keyValue;
                    
                arrayDetailDecodeData.add("========== StartRecord:"+recordCount+"==========");   //------ Create Text Detail Decode Data by Enable Field Config ----------
                
                for (int i = 0; i < fieldTotal; i++) {
                    getFieldData = mapFieldData.get(arrayFieldList[i]);
                    
                    keyValue=mapHexString2FieldName.get(arrayFieldList[i]);
//                    arrayDetailDecodeData.add(keyValue+" = "+getFieldData);
                    
                    if (null != (getFieldData)) {
//                        recordFieldData = recordFieldData + getFieldData + "|";
                        arrayDetailDecodeData.add(keyValue+" = "+getFieldData);
                    } else {
//                        recordFieldData = recordFieldData + "|";
                        arrayDetailDecodeData.add(keyValue+" =");
                    }
                }
                
                arrayDetailDecodeData.add("========== EndRecord:"+recordCount+"=========="+lineSeparator);
                
//                arrayRecordData.add(recordFieldData);   //write to array buffer before write to text file
//                mapFieldData.clear();                   // Clear array buffer
//                sumDataUplink_record = 0;               //Reset value
//                sumDataDownlink_record = 0;             //Reset value
                
                }else{                                  // Create Text Output Only Selected Field List (Not Include Blank Field)
                    fieldTotal = arrayListFieldEnableOnly.size();
                    
//                    for(int i=0;i<fieldTotal;i++){
//                        outputFieldList=outputFieldList+arrayFieldList[i];
//                    }
                    
//                    String recordFieldData="";     //Create Variable and Assign Value
                    String getFieldData;
                    String keyValue;
                    
                    arrayDetailDecodeData.add("========== StartRecord:"+recordCount+"==========");   //------ Create Text Detail Decode Data by Enable Field Config ----------
                    
                    for (int i = 0; i < fieldTotal; i++) {
                    getFieldData = mapFieldData.get(arrayListFieldEnableOnly.get(i));   // +2Field for skip ==> sumDataUplink,sumDataDownLink
                     
                    keyValue=mapHexString2FieldName.get(arrayListFieldEnableOnly.get(i));
//                    arrayDetailDecodeData.add(keyValue+" = "+getFieldData);
                                   
                                    if (null != (getFieldData)) {
//                        recordFieldData = recordFieldData + getFieldData + "|";
                                        arrayDetailDecodeData.add(keyValue+" = "+getFieldData);
                    } else {
//                        recordFieldData = recordFieldData + "|";
                                        arrayDetailDecodeData.add(keyValue+" =");
                    }
                }
                    arrayDetailDecodeData.add("========== EndRecord:"+recordCount+"=========="+lineSeparator);
                    
                    
                    
                    
                    
//                arrayRecordData.add(recordFieldData);   //write to array buffer before write to text file
//                mapFieldData.clear();                   // Clear array buffer
//                sumDataUplink_record = 0;               //Reset value
//                sumDataDownlink_record = 0;             //Reset value
//                
               }   
                
                
            }
                
//                
//            Iterator<String> Vmap = mapFieldData.keySet().iterator();           // Debug print (Tag) = (DecodeData)
//
////            *********************************
////          **** map key with field name ****
////            *********************************
//                System.out.println("----------- Record "+recordCount+" Detail --------------");      // Debug   
//            while(Vmap.hasNext()){                                              // Debug print (Tag) = (DecodeData)
//			String key = (String)(Vmap.next());  // Key             // Debug print (Tag) = (DecodeData)
//                        String keyName=mapHexString2FieldName.get(key);
//			String val = mapFieldData.get(key); // Value            // Debug print (Tag) = (DecodeData)
//			System.out.println(keyName + " = " + val);                  // Debug print (Tag) = (DecodeData)
//		}                                                               // Debug print (Tag) = (DecodeData)
//                System.out.println("------------ Record End --------------");   // Debug
//            }else{                                                              // Debug
//                                                                                // Debug
//            }                                                                   // Debug
            
            
            
//            recordCount++;
            
                
                
                
                
                
//############################# Arange Data Order by Filed List Before Write to Text File ###############################
//--------- Check Create Text Output Only Selected Field (Yes/No) ----------------
//                
//        String outputFieldList="";          // Field list for create text file (for map with decode data field)
        
                if(createOutputSelectedFieldOnly.equals("NO")){           // Create Text Output All Field List (Include Blank Field)
                
                    fieldTotal = arrayFieldList.length;
                    
//                    for(int i=0;i<fieldTotal;i++){
//                        outputFieldList=outputFieldList+arrayFieldList[i];
//                    }
                    
                String recordFieldData = "";     //Create Variable and Assign Value
                String getFieldData;
//                String keyValue;
                    
//                arrayDetailDecodeData.add("========== StartRecord:"+recordCount+"==========");   //------ Create Text Detail Decode Data by Enable Field Config ----------
                
                for (int i = 0; i < fieldTotal; i++) {
                    getFieldData = mapFieldData.get(arrayFieldList[i]);
                    
//                    keyValue=mapHexString2FieldName.get(arrayFieldList[i]);
//                    arrayDetailDecodeData.add(keyValue+" = "+getFieldData);
                    
                    if (null != (getFieldData)) {
                        recordFieldData = recordFieldData + getFieldData + "|";
                    } else {
                        recordFieldData = recordFieldData + "|";
                    }
                }
                
//                arrayDetailDecodeData.add("========== EndRecord:"+recordCount+"=========="+lineSeparator);
                
                arrayRecordData.add(recordFieldData);   //write to array buffer before write to text file
                mapFieldData.clear();                   // Clear array buffer
                sumDataUplink_record = 0;               //Reset value
                sumDataDownlink_record = 0;             //Reset value
                }else{                                  // Create Text Output Only Selected Field List (Not Include Blank Field)
                    fieldTotal = arrayListFieldEnableOnly.size();
                    
//                    for(int i=0;i<fieldTotal;i++){
//                        outputFieldList=outputFieldList+arrayFieldList[i];
//                    }
                    
                    String recordFieldData="";     //Create Variable and Assign Value
                    String getFieldData;
//                    String keyValue;
                    
//                    arrayDetailDecodeData.add("========== StartRecord:"+recordCount+"==========");   //------ Create Text Detail Decode Data by Enable Field Config ----------
                    
                    for (int i = 0; i < fieldTotal; i++) {
                    getFieldData = mapFieldData.get(arrayListFieldEnableOnly.get(i));   // +2Field for skip ==> sumDataUplink,sumDataDownLink
                     
//                    keyValue=mapHexString2FieldName.get(arrayListFieldEnableOnly.get(i));
//                    arrayDetailDecodeData.add(keyValue+" = "+getFieldData);
                                   
                                    if (null != (getFieldData)) {
                        recordFieldData = recordFieldData + getFieldData + "|";
                    } else {
                        recordFieldData = recordFieldData + "|";
                    }
                }
//                    arrayDetailDecodeData.add("========== EndRecord:"+recordCount+"=========="+lineSeparator);
                    
                    
                    
                    
                    
                arrayRecordData.add(recordFieldData);   //write to array buffer before write to text file
                mapFieldData.clear();                   // Clear array buffer
                sumDataUplink_record = 0;               //Reset value
                sumDataDownlink_record = 0;             //Reset value
//                
               }
                recordCount++;          // End of Record Loop
                
            } while ((fileIndex + 1) < fileLength);     // +1Byet for adjust length (protect array out of bound)    // End of File Loop

//----------------- End of File Summarry Report -------------------
            sumRecord_allFile = sumRecord_allFile + (recordCount - 1);
            sumRecordError_allFile = sumRecordError_allFile + recordErrorCount;
            if (recordErrorList.length() > 0) {
                recordErrorList = " {[Record:" + recordErrorList.substring(0, (recordErrorList.length() - 1)) + "],";
            }
            if (addressErrorList.length() > 0) {
                addressErrorList = "[Address:" + addressErrorList.substring(0, (addressErrorList.length() - 1)) + "]}";
            }
            System.out.println("Record Total:" + DecimalFormat.format(recordCount - 1) + " ;  Records Error:" + DecimalFormat.format(recordErrorCount)
                    + recordErrorList + addressErrorList);
            System.out.println("Raw Data Remaining(can't process):" + ((fileLength - 1) - fileIndex) + " Byte");
            System.out.println("SumDataVolumeUplink: " + DecimalFormat.format(sumDataUplink_file) + " ;  SumDataVolumeDownlink: " + DecimalFormat.format(sumDataDownlink_file));
            FileIO.FileWriter(writeLogFileName, true, "Record Total:" + DecimalFormat.format(recordCount - 1) + " ;  Records Error:" + DecimalFormat.format(recordErrorCount)
                    + recordErrorList + addressErrorList + lineSeparator
                    + "Raw Data Remaining(can't process):" + DecimalFormat.format((fileLength - 1) - fileIndex) + " Bytes" + lineSeparator);
            FileIO.FileWriter(writeLogFileName, true, "SumDataVolumeUplink: " + DecimalFormat.format(sumDataUplink_file)
                    + " ;  SumDataVolumeDownlink: " + DecimalFormat.format(sumDataDownlink_file) + lineSeparator);
            if (recordErrorCount > 0 | notPGWCount > 0) {                                 // if have record error increment file error counter
                rawFileErrorList = rawFileErrorList + (rawFileNo + 1) + ",";
                rawFileErrorCount++;
                listRawFileError = listRawFileError + DecimalFormat.format(rawFileErrorCount) + ". (FileSeqNo:" + DecimalFormat.format((rawFileNo + 1)) + ") " + fileName + lineSeparator;
                backupRawDestination = pathBackupRawFileError + fileName;       // Save Backup RAW File To == > PathBackupRawFileError
            } else {
                backupRawDestination = pathBackupRawFile + fileName;            // Save Backup RAW File To == > PathBackupRawFile
            }
            String writeDecodeFileName = pathDecodeFile + fileName + ".txt";
            String writeDecodeDetailFileName = pathDecodeDetailFile+fileName+"_detail.txt";

//------------ Check Folder Existing Before Write File ------------------------
            File pathTextData = new File(pathDecodeFile);
            if (pathTextData.exists()) {
                FileIO.bufferWriter(writeDecodeFileName, arrayRecordData);     // Write output to text file
                if(createDetailTextFile.equals("YES")){
                    FileIO.bufferWriter(writeDecodeDetailFileName, arrayDetailDecodeData);     // Write output Detail Decode Data to text file
                }
            } else {
                System.out.println("Directory " + pathDecodeFile + " Not exists !!!");
            }

            File textData = new File(writeDecodeFileName);
            if (textData.exists()) {
            } else {
                System.out.println("Directory " + pathDecodeFile + " Not exists !!!");
            }

//------------------------------------- Backup Original RAW File --------------------------------------------
//   1. Select Backup (Yes/No)
//      1.1 Select Type of Backup (RAW/Gzip)
//   2. Delete Original RAW File (Yes/No)
//-----------------------------------------------------------------------------------------------------------
            if (copyRawToBackup.equals("YES")) {                                        // 1. Check Backup (Yes/No)
                if (backupWithGzip.equals("YES")) {                                     //      1.1 Check Type of Backup (RAW/Gzip)
                    backupRawDestination = backupRawDestination + fileName + ".gz";
                    if (FileIO.gZIP(rawFilePathName, backupRawDestination)) {           // Create Gzip and Check File *.gz Is Created
//                        System.out.println("gZIP Complete");    // Debug
                    } else {
//                        System.out.println("gZIP Error");       // Debug
                    }
                } else {      //----------------- copy RAW File To Backup RAW File Path ---------------------
                    File rawFileSourcePath = new File(rawFilePathName);                 //source path
                    backupRawDestination = backupRawDestination + fileName;
                    File backupRawPath = new File(backupRawDestination);                //destination path
                    try {
                        FileUtils.copyFile(rawFileSourcePath, backupRawPath);
                    } catch (IOException e) {
                    }
                }
            } else {

//----------------- Skip Backup Original RAW File --------------------
            }

            if (deleteOriginalRawFile.equals("YES")) {                                  // 2.Delete Original RAW File (Yes/No)
                File fileToDelete = FileUtils.getFile(rawFilePathName);
                FileUtils.deleteQuietly(fileToDelete);
            }

//------------------------------------------- End of File Process Clear Counter ----------------------------------------
            arrayRecordData.clear();
            sumDataUplink_record = 0;
            sumDataDownlink_record = 0;
            sumDataUplink_file = 0;
            sumDataDownlink_file = 0;
        }

//############################################ End of all File Decoder ###################################################
//############################################# Decode Summary Report ####################################################
//
        if (rawFileErrorCount > 0) {
            rawFileErrorList = " {FileSeqNo:" + rawFileErrorList.substring(0, (rawFileErrorList.length() - 1)) + "}";
        }
        FileIO.FileWriter(writeLogFileName, true, lineSeparator + "\r\n============================================== Decode Summary ==============================================" + lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, "Total File: " + DecimalFormat.format(totalRawFile) + " ;  File Error:" + DecimalFormat.format(rawFileErrorCount)
                + rawFileErrorList + lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, "List of Error File:\r\n");
        FileIO.FileWriter(writeLogFileName, true, listRawFileError);
        FileIO.FileWriter(writeLogFileName, true, "Total Record: " + DecimalFormat.format(sumRecord_allFile) + " ;  Total Record Error: "
                + DecimalFormat.format(sumRecordError_allFile) + " (Exclude File Error)" + lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, "Total DataVolumeUplink: " + DecimalFormat.format(sumDataUplink_allFile) + " ;  Total DataVolumeDownlink: "
                + DecimalFormat.format(sumDataDownlink_allFile) + lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, lineSeparator + "====================================== Decode End "
                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " ======================================" + lineSeparator);
        System.out.print(lineSeparator + "\r\n============================================== Decode Summary ==============================================" + lineSeparator);
        System.out.print("Total File:" + DecimalFormat.format(totalRawFile) + " ;  File Error:" + DecimalFormat.format(rawFileErrorCount)
                + rawFileErrorList + lineSeparator);
        System.out.print("List of Error File:\r\n");
        System.out.print(listRawFileError);
        System.out.print("Total Record: " + DecimalFormat.format(sumRecord_allFile) + " ;  Total Record Error: "
                + DecimalFormat.format(sumRecordError_allFile) + " (Exclude File Error)" + lineSeparator);
        System.out.print("Total DataVolumeUplink: " + DecimalFormat.format(sumDataUplink_allFile) + " ;  Total DataVolumeDownlink: "
                + DecimalFormat.format(sumDataDownlink_allFile) + lineSeparator);
        System.out.print(lineSeparator + "====================================== Decode End "
                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " ======================================" + lineSeparator);
        sumDataUplink_allFile = 0;
        sumDataDownlink_allFile = 0;
        sumRecord_allFile = 0;
        sumRecordError_allFile = 0;
    }
}
