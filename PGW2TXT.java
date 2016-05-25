/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pgw;

import java.io.FileNotFoundException;
import java.io.IOException;
//import java.lang.reflect.Method;

/**
 *
 * @author Samreng 2016-05-25 19:58
 */
public class PGW2TXT {

    public static int sumDataUplink_file = 0;
    public static int sumDataDownlink_file = 0;
    public static int sumDataUplink_record = 0;
    public static int sumDataDownlink_record = 0;
    

    public void DataUplink(int dataUplink) {
        sumDataUplink_file = sumDataUplink_file + dataUplink;
        sumDataUplink_record = sumDataUplink_record + dataUplink;

    }

    public void DataDownlink(int dataDownlink) {
        sumDataDownlink_file = sumDataDownlink_file + dataDownlink;
        sumDataDownlink_record = sumDataDownlink_record + dataDownlink;
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {

        FileIO FileIO = new FileIO();
        String pathFileConfig = "D:\\Training\\Java\\SourceCode\\pgw\\PGWr8.csv";
        String[] readConfig = FileIO.ReadFileConfig(pathFileConfig);    //Return with array of header group
        String pathConfig = readConfig[0];
        String fieldConfig = readConfig[1];
        String record_decode_data; // Recodr data buffer
        
        int fieldCount=Integer.parseInt(readConfig[2]);   
        String [] fieldList=new String[fieldCount+2];     // array for store field list  //+2 for sumUplink, sumDownlink
        int fieldConfig_len = fieldConfig.length();
        int fieldConfig_indx=0;
        int tagIndex;
        
        fieldList[0]="sumUplink";
        fieldList[1]="sumDownlink";
        for(int i=0;i<fieldCount;i++){
            fieldList[i+2]=readConfig[i+3];
            System.out.print(fieldList[i+2]+" ");     //Debug
        }
        
        System.out.println(fieldList[0]+" "+fieldList[1]);     //Debug
        

        int fileIndex = 0;
//        int fieldLength = 0;
//        int record_byte = 0;
//        String record_type;
        String tag_1hex_str, tag_2hex_str, tag_3hex_str;

        int record_indx = 0;
//        int recordEndAddress=0;   //use sumRecordLength
        int recordCount = 1;
        int recordErrorCount = 0;
        String recordErrorList = "";
        int sumRecordLength = 0;

        
        RawFile readFile = new RawFile(); // Create Object from class readRaw
        int[] rawFileInt = readFile.readBinaryFile //                 ("C:\\Users\\Samreng\\Documents\\HSPA\\CDR Project\\CDR_Description PS_R8\\raw_PS_R8\\PGW\\006295981_20160428091436.cdr");
                //                ("D:\\Training\\Java\\SourceCode\\cdr\\cdr_raw_PS_R8\\0000204700_20140403221949.cdr");
                //                ("D:\\Training\\Java\\SourceCode\\cdr\\cdr_raw_PS_R8\\20140403221949_sample.cdr");
//                ("D:\\Training\\Java\\SourceCode\\cdr\\cdr_raw_PS_R8\\20140403221949_sample_fieldErr.cdr");
                         ("D:\\Training\\Java\\SourceCode\\cdr\\cdr_raw_PS_R8\\006295981_20160428091436_sample.cdr");
//                        ("D:\\Training\\Java\\SourceCode\\cdr\\cdr_raw_PS_R8\\20140403221949_sample2.cdr");
//                                ("C:\\Users\\Samreng\\Documents\\HSPA\\CDR Project\\CDR_Description PS_R8\\pscdr2text\\0000211585_20140920201238.cdr");
//                ("D:\\Training\\Java\\SourceCode\\cdr\\cdr_raw_PS_R8\\20140920201238_sample_x30x80.cdr");
//                ("D:\\Training\\Java\\SourceCode\\cdr\\cdr_raw_PS_R8\\b00000001.dat");
        int fileLength = rawFileInt.length;
        System.out.println("File length = " + fileLength + "|0x" + String.format("%02X", fileLength) + " Byte"); // Debug

        FieldDecoder decode = new FieldDecoder();       // Create Object from class FieldDecoder
        RawFile getRaw = new RawFile();                 // Create Object from class RawFile
        DataConverter data_conv = new DataConverter();  // Create Object from class DataConverter

//---------------- Start of loop file process ------------------------
        do {
            record_decode_data = ""; // Reset Recodr data buffer
            String field_hex_str = "";  // Reset old record length

//---------------- Start of loop record process ------------------------
            String record_type_hex_str = String.format("%02X", rawFileInt[fileIndex]) + String.format("%02X", rawFileInt[fileIndex + 1]);
            if ("BF4F".equals(record_type_hex_str)) {
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
//            System.out.println("Rec len Str:"+field_hex_str);         // Debug print
                System.out.println("Record length:" + record_length + " Byte");    // Debug print
//            System.out.println("Raw File @file_indx:"+rawFileInt[file_indx]);   // Debug print

//                String record_decode_data = ""; // Recodr data buffer
//            String field_decode_data = ""; // Recodr data buffer

//---------------- Start of loop record process ------------------------     
                try {

                    do {
                        sumDataUplink_record = 0;
                        sumDataDownlink_record = 0;
//---------- Start of Check Tag field length 1,2,3Byte ---------------------
                        tag_1hex_str = "x" + String.format("%02X", rawFileInt[fileIndex]);    // Convert Integer to Hex String for check Tag field 1Byte

//----------------------------- Start of tag_1hex_str ---------------------------
                        System.out.println("------------------- Start of field (Record:" + recordCount + ") --------------------");
                        if (fieldConfig.contains(tag_1hex_str + ",")) {     // Check 1Byte Tag field is in Tag list table

////---------- Special check for Tag [xAC x81 (x80...x83)] not found in manual----------                    
//                        if (("xAC".equals(tag_1hex_str)) && (rawFileInt[fileIndex + 1] > 0x80) && (rawFileInt[fileIndex + 3] == 0x30)) {
//                            fileIndex += 3;       //Skip 3Byte
//                            record_indx += 3;    //Skip 3Byte
//                            // ------- Check all sub field x30 Length -------
//                            int x30_indx = fileIndex;
//                            int x30_len = rawFileInt[x30_indx + 1] + 2;    //+1Byte for Skip tag x30; +2Byte(Tag+Length Byte)
//
//                            while (rawFileInt[(x30_indx + x30_len)] == 0x30) {
//                                x30_len = x30_len + 2 + rawFileInt[(x30_indx + x30_len + 1)];    //+2Byte for Skip Tag and Length Byte
//                            }
//                            int[] field_raw_int = new int[x30_len];
//                            for(int i=0; i<x30_len;i++){
//                                field_raw_int[i]=rawFileInt[fileIndex];
//                                fileIndex++;
//                            }
//                        }
////---------- End of Special check for Tag [xAC x81 (x80...x83)] not found in manual----------
                            System.out.print("Field Tag: " + tag_1hex_str + "   Address: 0x" + String.format("%02X", fileIndex) + "  "); // Debug print
                            fileIndex++;
                            record_indx++;
                            int tag_list_length = fieldConfig.indexOf("|", fieldConfig.indexOf(tag_1hex_str));
                            String field_conf = fieldConfig.substring(fieldConfig.indexOf(tag_1hex_str), tag_list_length);
                            String field_decode = field_conf.substring(field_conf.indexOf(",") + 1, field_conf.indexOf(",") + 2);
                            int field_length = rawFileInt[fileIndex];
                            if ("1".equals(field_decode)) {
//                        System.out.println("File Index(StartValue):" + String.format("%02X", file_indx + 1)); // Debug print

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
                                    record_decode_data = record_decode_data  + tag_1hex_str+":"+field_decode_data+ "|";

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
                                    record_decode_data = record_decode_data + tag_1hex_str+":"+ field_decode_data+ "|" ;

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
                                        record_decode_data = record_decode_data + tag_1hex_str+":" + field_decode_data+ "|";

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
                                    System.out.println("Unknow Tag!!! File address: " + "0x" + String.format("%02X", fileIndex));
                                    System.out.println("Raw data remaining in process: " + (record_indx - record_length) + " Byte");
//                                break;  // Break or Return
//*************  Skip to next Record and count error **********
                                    recordErrorCount++;
                                    recordErrorList = recordErrorList + recordCount + ",";
                                    fileIndex = sumRecordLength;     //Skip Record error

                                }
                            }
                        }
                        System.out.println("SumDataVolumeUplink: " + sumDataUplink_record + " ;  SumDataVolumeDownlink: " + sumDataDownlink_record);
                    } //            while (record_indx < record_length);    
                    while (fileIndex < sumRecordLength);

                } catch (Exception ex) {
                    System.out.println("Record Error Raw data invalid format!!!");

                    recordErrorCount++;
                    recordErrorList = recordErrorList + recordCount + ",";
                    fileIndex = sumRecordLength;     //Skip Record error
                }

            } else {
                System.out.println("fileIndex:0x" + String.format("%02X", fileIndex) + " record_indx:0x" + String.format("%02X", record_indx) + " Not PGW-Record");
            }
            recordCount++;
            System.out.println("record_decode_data=> "+record_decode_data);     //Debux print text data record
        } while ((fileIndex + 1) < fileLength);     // +1Byet for adjust length (protect array out of bound)

//----------------- End of File Summarry Report -------------------  
        if (recordErrorList.length() > 0) {
            recordErrorList = "[" + recordErrorList.substring(0, (recordErrorList.length() - 1)) + "]";
        }
        System.out.println("");
        System.out.println("***************************************** End of file *****************************************");
        System.out.println("Record Total:" + (recordCount - 1) + " ;  Records Error:" + recordErrorCount + recordErrorList + " ; " + "  Raw Data Remaining(can't process):" + (fileLength - (fileIndex + 1)) + " Byte");
        System.out.println("SumDataVolumeUplink: " + sumDataUplink_file + " ;  SumDataVolumeDownlink: " + sumDataDownlink_file);
    }
}
