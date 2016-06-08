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
    public static int sumDataUplink_file = 0;
    public static int sumDataDownlink_file = 0;
    public static int sumDataUplink_record = 0;
    public static int sumDataDownlink_record = 0;
    public static double sumDataUplink_allFile = 0;
    public static double sumDataDownlink_allFile = 0;
    public static double sumRecord_allFile = 0;
    public static double sumRecordError_allFile = 0;

    public void DataUplink(int dataUplink) {
        sumDataUplink_allFile = sumDataUplink_allFile + dataUplink;
        sumDataUplink_file = sumDataUplink_file + dataUplink;
        sumDataUplink_record = sumDataUplink_record + dataUplink;
    }

    public void DataDownlink(int dataDownlink) {
        sumDataDownlink_allFile = sumDataDownlink_allFile + dataDownlink;
        sumDataDownlink_file = sumDataDownlink_file + dataDownlink;
        sumDataDownlink_record = sumDataDownlink_record + dataDownlink;
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {

        Locale.setDefault(Locale.US);
        DecimalFormat DecimalFormat = new DecimalFormat("#,###,##0");

        FileIO FileIO = new FileIO();
        String pathFileConfig = "D:\\Training\\Java\\SourceCode\\pgw\\PGWr8.csv";
        String[] readConfig = FileIO.ReadFileConfig(pathFileConfig);    //Return with array of header group
        String pathConfig = readConfig[0];                              // String of Path Configuration
        String fieldConfig = readConfig[1];                             // String of Tag Field list
        String record_decode_data;                                      // Recodr data buffer

//#################################### Read Configuration and Store to ArrayList ###############################################  
//        
        int pathConfig_indxStart;
        int pathConfig_indxEnd = 0;
        int offsetStart = 0;                                            // use for check and Skip "|"

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
        String pathDecodeData = listPathConfig.get("DecodeFile");
        String pathBackupRawFile = listPathConfig.get("BackupRawFile");
        String pathLogData = listPathConfig.get("LogFile");
        String pathBackupRawFileError = listPathConfig.get("BackupRawFileError");
        String rawFileExtension = listPathConfig.get("RawFileExtension");
        String copyRawToBackup = (listPathConfig.get("CopyRawToBackup")).toUpperCase();
        String backupWithGzip = (listPathConfig.get("BackupWithGzip")).toUpperCase();
        String deleteOriginalRawFile = (listPathConfig.get("DeleteOriginalRawFile")).toUpperCase();
        int recordErrorCountLimit = Integer.parseInt(listPathConfig.get("RecordErrorCountLimit"));

        System.out.println(lineSeparator + "====================================== Start Time "
                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " ======================================");
        System.out.println("\r\n------------------------------------ Path Configuration ------------------------------------");
        System.out.println("PathRawFile:          = " + pathRawData);
        System.out.println("PathDecodeFile:       = " + pathDecodeData);
        System.out.println("PathBackupRawFile:    = " + pathBackupRawFile);
        System.out.println("PathLogFile:          = " + pathLogData);
        System.out.println("PathBackupErrorFile:  = " + pathBackupRawFileError);
        System.out.println("RawFileExtension:     = " + rawFileExtension);
        System.out.println("CopyRawToBackup       = " + copyRawToBackup);
        System.out.println("BackupWithGzip        = " + backupWithGzip);
        System.out.println("DeleteOriginalRawFile = " + deleteOriginalRawFile);
        System.out.println("RecordErrorCountLimit = " + recordErrorCountLimit);

        FileIO.createDirectory(pathDecodeData);
        FileIO.createDirectory(pathBackupRawFile);
        FileIO.createDirectory(pathLogData);
        FileIO.createDirectory(pathBackupRawFileError);

        List<String> arrayListFile = FileIO.ListFileByExtension(pathRawData, rawFileExtension);   // List RAW File from folder pathRawData
        String writeLogFileName = pathLogData + "LogDecoder.txt";

        FileIO.FileWriter(writeLogFileName, true, lineSeparator + "====================================== Start Time "
                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " ======================================" + lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, "\r\n------------------------------------ Path Configuration ------------------------------------" + lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, "PathRawFile:          = " + pathRawData + lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, "PathDecodeFile:       = " + pathDecodeData + lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, "PathBackupRawFile:    = " + pathBackupRawFile + lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, "PathLogFile:          = " + pathLogData + lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, "PathBackupErrorFile:  = " + pathBackupRawFileError + lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, "RawFileExtension:     = " + rawFileExtension + lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, "CopyRawToBackup       = " + copyRawToBackup + lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, "BackupWithGzip        = " + backupWithGzip + lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, "DeleteOriginalRawFile = " + deleteOriginalRawFile + lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, "RecordErrorCountLimit = " + recordErrorCountLimit + lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, lineSeparator);
        FileIO.FileWriter(writeLogFileName, true, "Total RAW File = " + arrayListFile.size() + lineSeparator);

        System.out.println(lineSeparator + "Total RAW File = " + arrayListFile.size());

//############################################# Start loop for run all RAW file ####################################################        
//     
        int totalRawFile = arrayListFile.size();
        int rawFileErrorCount = 0;
        String listRawFileError = "";
        String rawFileErrorList = "";
        String backupRawDestination;

        for (int rawFileNo = 0; rawFileNo < totalRawFile; rawFileNo++) {
            String fileName = arrayListFile.get(rawFileNo);
            String rawFilePathName = pathRawData + fileName;
            int notPGWCount = 0;                                        // Counter for Count Not PGW Record If Over Limit Skip To Next Raw File    
            ArrayList<String> arrayRecordData = new ArrayList<>();      //array for store record decode data before write to text file
            int fieldCount = Integer.parseInt(readConfig[2]);
            String[] arrayFieldList = new String[fieldCount + 2];       // array for store field list  //+2 for sumUplink, sumDownlink
            arrayFieldList[0] = "sumUplink";
            arrayFieldList[1] = "sumDownlink";
            for (int i = 0; i < fieldCount; i++) {
                arrayFieldList[i + 2] = readConfig[i + 3];
            }
            int fileIndex = 0;
            String tag_1hex_str, tag_2hex_str, tag_3hex_str;
            int record_indx = 0;
            int recordCount = 1;
            int recordErrorCount = 0;
            String addressErrorList = "";
            String recordErrorList = "";    //use for add to integer for convert integer to string
            int sumRecordLength = 0;

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
                String field_hex_str = "";  // Reset old record length
//                sumDataUplink_file = 0;     // Reset Counter
//                sumDataDownlink_file = 0;   // Reset Counter
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
                                if ("1".equals(field_decode)) {

//---------- Special check for Tag [xAC x81 (x80...x83)] not found in manual----------                    

                                    if (("xAC".equals(tag_1hex_str)) && (rawFileInt[fileIndex] > 0x80) && (rawFileInt[fileIndex + 2] == 0x30)) {

//                                    if (("xAC".equals(tag_1hex_str)) && (rawFileInt[fileIndex] > 0x80)) { 

                                        
                                        fileIndex += 2;       //Skip 2Byte  (Skip 0x8.., 0x8..)
                                        record_indx += 2;     //Skip 2Byte  (Skip 0x8.., 0x8..)
                                        
 System.out.println("xACx81.fileIndex:"+String.format("%02X",fileIndex)+" record_indx:"+String.format("%02X",record_indx)+" rawFileInt[fileIndex]:"+String.format("%02X",rawFileInt[fileIndex])); // Debug
 
//                                        fileIndex=fileIndex+rawFileInt[fileIndex]-0x80+1;           // Calculate Skip Offset ?Byte    // +1Byte for skip Tag x8..
//                                        record_indx=record_indx+rawFileInt[fileIndex]-0x80+1;       // Calculate Skip Offset ?Byte    // +1Byte for skip Tag x8..
                                        
// ------- Check all sub field x30 Length -------   // Old Remark Header


//  **************** Calculate Length of xAC **************************
                                        
                                        int x30_indx = fileIndex;
                                        
                                        int x30_len;    //+2Byte for Skip tag x30 and Lengt;
                                        
                                        while(rawFileInt[fileIndex]==0x30){
//                                        int x30_len = rawFileInt[x30_indx + 1] + 2;    //+1Byte for Skip tag x30; +2Byte(Tag+Length Byte)
                                        
                                        x30_len = rawFileInt[fileIndex + 1]+2;    //+1Byte for Skip tag x30; +2Byte(Tag+Length Byte)

//                                        while (rawFileInt[(x30_indx + x30_len)] == 0x30) {
//                                            x30_len = x30_len + 2 + rawFileInt[(x30_indx + x30_len + 1)];    //+2Byte for Skip Tag and Length Byte
//                                        }

//                                        int x30_len=rawFileInt[fileIndex+1];
                                        int[] field_raw_int = new int[x30_len];
                                        for (int i = 0; i < x30_len; i++) {
                                            field_raw_int[i] = rawFileInt[fileIndex];
                                            fileIndex++;
                                            record_indx++;
                                        }
//                                        String field_decode_data = decode.decoder(tag_1hex_str, field_raw_int, field_conf);
//                                        record_decode_data = record_decode_data + "|" + field_decode_data;
                                        String field_decode_data = decode.decoder(tag_1hex_str, field_raw_int, field_conf);
                                        mapFieldData.put(tag_1hex_str, field_decode_data);  // push to HashMapArray
                                        record_decode_data = record_decode_data + tag_1hex_str + ":" + field_decode_data + "|";     //Test , Debug
                                        
                                    }
                                    } //---------- End of Special check for Tag [xAC x81 (x80...x83)] not found in manual----------
                                    
                                    else {
                                    
System.out.println("xACx30.fileIndex:"+String.format("%02X",fileIndex)+" record_indx:"+String.format("%02X",record_indx)+" rawFileInt[fileIndex]:"+String.format("%02X",rawFileInt[fileIndex])); // Debug

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
                                    fileIndex = fileIndex + field_length + 1;   // Skip file index to next field
                                    record_indx = record_indx + field_length + 1;     // Add field_length to field_indx for correct field_indx position
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
//
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
                                        recordErrorCount++;
                                        recordErrorList = recordErrorList + recordCount + ",";
                                        addressErrorList = addressErrorList + "0x" + String.format("%02X", fileIndex) + ",";
                                        fileIndex = sumRecordLength;     //Skip Record error
                                    }
                                }
                            }
                            mapFieldData.put("sumDataUplink_record", Integer.toString(sumDataUplink_record));       // push to HashMapArray
                            mapFieldData.put("sumDataDownlink_record", Integer.toString(sumDataDownlink_record));   // push to HashMapArray
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
                recordCount++;

//------***** Arrange dataDecode order by field list before write to array buffer (recordDataBuffer)  after that write from array (recordDataBuffer) to Text File
<<<<<<< HEAD
                //------***** mapFieldData
=======
//------***** mapFieldData
>>>>>>> parent of ec21c19... Add condition if have record error or field error
                int fieldTotal = arrayFieldList.length;
//                String keyTag, fieldValue;
                String recordFieldData = sumDataUplink_record + "|" + sumDataDownlink_record + "|";
                String getFieldData;
                for (int i = 0; i < fieldTotal; i++) {
                    getFieldData = mapFieldData.get(arrayFieldList[i]);
                    if (null != (getFieldData)) {
                        recordFieldData = recordFieldData + getFieldData + "|";
                    } else {
                        recordFieldData = recordFieldData + "|";
                    }
                }
                arrayRecordData.add(recordFieldData);   //write to array buffer before write to text file
                mapFieldData.clear();                   // Clear array buffer
                sumDataUplink_record = 0;               //Reset value
                sumDataDownlink_record = 0;             //Reset value
            } while ((fileIndex + 1) < fileLength);     // +1Byet for adjust length (protect array out of bound)
//
//----------------- End of File Summarry Report -------------------  
//
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
            System.out.println("SumDataVolumeUplink: " + sumDataUplink_file + " ;  SumDataVolumeDownlink: " + sumDataDownlink_file);
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
            String writeFileName = pathDecodeData + fileName + ".txt";
//
//------------ Check Folder Existing Before Write File ------------------------
//        
            File pathTextData = new File(pathDecodeData);
            if (pathTextData.exists()) {
                FileIO.bufferWriter(writeFileName, arrayRecordData);     // Write output to text file
            } else {
                System.out.println("Directory " + pathDecodeData + " Not exists !!!");
            }

            File textData = new File(writeFileName);
            if (textData.exists()) {
            } else {
                System.out.println("Directory " + pathDecodeData + " Not exists !!!");
            }

//------------------------------------- Backup Original RAW File --------------------------------------------     
//   1. Select Backup (Yes/No)
//      1.1 Select Type of Backup (RAW/Gzip)
//   2. Delete Original RAW File (Yes/No)
//-----------------------------------------------------------------------------------------------------------
            if (copyRawToBackup.equals("YES")) {                                    // 1. Check Backup (Yes/No)
                if (backupWithGzip.equals("YES")) {                                 //      1.1 Check Type of Backup (RAW/Gzip)
                    backupRawDestination = backupRawDestination + fileName + ".gz";
                    if (FileIO.gZIP(rawFilePathName, backupRawDestination)) {       // Create Gzip and Check File *.gz Is Created
//                        System.out.println("gZIP Complete");    // Debug
                    } else {
//                        System.out.println("gZIP Error");       // Debug
                    }
                } else {      //----------------- copy RAW File To Backup RAW File Path ---------------------
                    File rawFileSourcePath = new File(rawFilePathName);             //source path
                    backupRawDestination = backupRawDestination + fileName;
                    File backupRawPath = new File(backupRawDestination);            //destination path
                    try {
                        FileUtils.copyFile(rawFileSourcePath, backupRawPath);
                    } catch (IOException e) {
                    }
                }
            } else {
                //----------------- Skip Backup Original RAW File --------------------
            }

            if (deleteOriginalRawFile.equals("YES")) {                              // 2.Delete Original RAW File (Yes/No)
                File fileToDelete = FileUtils.getFile(rawFilePathName);
                FileUtils.deleteQuietly(fileToDelete);
            }

//------------------------------------------- End of File Process Clear Counter ----------------------------------------
            arrayRecordData.clear();
            sumDataUplink_record = 0;
            sumDataDownlink_record = 0;
            sumDataUplink_file = 0;
            sumDataDownlink_file = 0;
            
//            sumDataUplink_allFile = 0;
//            sumDataDownlink_allFile = 0;

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
