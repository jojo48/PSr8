/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pgw;

/**
 *
 * @author Samreng 2016-05-03 20:55
 */
public class FieldDecoder {

    DataConverter DataConverter = new DataConverter();  //Create Object DataConverter
    SubFieldDecoder SubFieldDecoder = new SubFieldDecoder();    //Create Object SubFieldDecoder

    public String decoder(String tag_hex_str, int[] field_raw_data, String field_conf) {
        String field_decode_data;

//-------------------- Switch to decode tag ---------------------------------
        switch (tag_hex_str) {

//---------------------- Start of case x80 (recordType) ----------------------
            case "x80":
                if ((field_raw_data[0] == 0x55) && (field_raw_data.length == 1)) {
                    field_decode_data = "PGW-Record";
                    return field_decode_data;
                } else {
                    field_decode_data = "Unknow-Record Type";
                    return field_decode_data;
                }
//---------------------- End of case x80 ----------------------

//---------------------- Start of case x83 (servedIMSI) ----------------------
            case "x83":
                field_decode_data = DataConverter.tBCD2String(field_raw_data);
                return field_decode_data;
//---------------------- End of case x83 ----------------------

//---------------------- Start of case xA4 (pGWAddress) ----------------------
            case "xA4":
                String tag_xA4 = "x" + String.format("%02X", field_raw_data[0]);
                switch (tag_xA4) {
                    case "x80":
                        int[] tag_raw_data = new int[4];
                        for (int i = 0; i < 4; i++) {
                            tag_raw_data[i] = field_raw_data[i + 2];
                        }
                        field_decode_data = DataConverter.IPv4String(tag_raw_data);
                        return field_decode_data;

                    case "x81":
                        field_decode_data = "xA4 81:" + DataConverter.Int2HexString(field_raw_data);
                        return field_decode_data;
                    case "x82":
                        field_decode_data = "xA4 82:" + DataConverter.Int2HexString(field_raw_data);
                        return field_decode_data;
                    case "x83":
                        field_decode_data = "xA4 83:" + DataConverter.Int2HexString(field_raw_data);
                        return field_decode_data;
                    default:
                        field_decode_data = "xA4 unknow subTAG:" + String.format("%02X", field_raw_data[0]) + DataConverter.Int2HexString(field_raw_data);
                        return field_decode_data;
                }
//---------------------- End of case xA4 ----------------------

//---------------------- Start of case x85 (chargingID) ----------------------
            case "x85":
                String field_raw_hex_string = "0x";
                for (int i = 0; i < (field_raw_data.length); i++) {
                    field_raw_hex_string = field_raw_hex_string + String.format("%02X", field_raw_data[i]);
                }
                field_decode_data = Long.toString(Long.decode(field_raw_hex_string));
                return field_decode_data;
//---------------------- End of case x85 ----------------------

//---------------------- Start of case xA6 (servingNodeAddress) ----------------------
            case "xA6":
                int field_indx = 0;
                String sub_field_type;
                String sub_field_list = "x80|x81|x82|x83|";
                int field_len = field_raw_data.length;
                int remain_raw_xA6 = field_len;
                int sub_field_len;
                field_decode_data = "{";

                do {
                    String sub_raw = "";
                    sub_field_type = "x" + String.format("%02X", field_raw_data[field_indx]) + "|";
                    if (sub_field_list.contains(sub_field_type)) {
                        sub_field_len = field_raw_data[field_indx + 1];
                        int[] sub_field_raw_data = new int[sub_field_len + 2];
                        for (int i = 0; i < sub_field_len + 2; i++) {
                            sub_field_raw_data[i] = field_raw_data[field_indx];
                            sub_raw = sub_raw + String.format("%02X", field_raw_data[field_indx]) + "|";
                            field_indx++;
                        }
                        field_decode_data = field_decode_data + "[" + DataConverter.IPAddress(sub_field_raw_data) + "]";
                    } else {
                        sub_raw = "";
                        int remain_raw = (field_len - field_indx);
                        for (int i = 0; i < remain_raw; i++) {
                            sub_raw = sub_raw + String.format("%02X", field_raw_data[field_indx]) + "|"; //Debug
                            field_indx++;
                        }
                        String unknow_sub_field_type = "Unknow sub_field type: " + sub_field_type + " Raw Data:" + sub_raw;

                        return unknow_sub_field_type;
                    }
                } while (field_indx < field_len);
                field_decode_data = field_decode_data + "}";
                return field_decode_data;
//---------------------- End of case xA6 ----------------------

//---------------------- Start of case x87 (accessPointNameNI) ----------------------
            case "x87":
                field_decode_data = DataConverter.IA5String(field_raw_data);
                return field_decode_data;
//---------------------- End of case xA6 ----------------------

//---------------------- Start of case x88 (pdpPDNType) ----------------------
            case "x88":
                field_decode_data = DataConverter.Int2HexString(field_raw_data);   // Octet String
                return field_decode_data;
//---------------------- End of case x88 ----------------------

//---------------------- Start of case x8D (recordOpeningTime) ----------------------
            case "x8D":
                field_decode_data = DataConverter.Int2DateTime(field_raw_data);   // Octet String
                return field_decode_data;
//---------------------- End of case x88 ----------------------                

//---------------------- Start of case x8E (duration) ----------------------
            case "x8E":
                field_raw_hex_string = "0x";
                for (int i = 0; i < (field_raw_data.length); i++) {
                    field_raw_hex_string = field_raw_hex_string + String.format("%02X", field_raw_data[i]);
                }
                field_decode_data = Long.toString(Long.decode(field_raw_hex_string));
                return field_decode_data;
//---------------------- End of case x88 ----------------------                

//---------------------- Start of case x8F (causeForRecClosing) ----------------------
            case "x8F":
                field_raw_hex_string = "0x";
                for (int i = 0; i < (field_raw_data.length); i++) {
                    field_raw_hex_string = field_raw_hex_string + String.format("%02X", field_raw_data[i]);
                }
                field_decode_data = Long.toString(Long.decode(field_raw_hex_string));
                return field_decode_data;
//---------------------- End of case x88 ----------------------                 

//---------------------- Start of case xA9 (servedPDPPDNAddress) ----------------------
            case "xA9":
                int field_raw_length = field_raw_data.length;

                if (field_raw_data[0] == 0xA0) {        // iPAddress
                    int sub_field_length = field_raw_data[1];
                    int[] sub_field_raw_data = new int[sub_field_length];
                    for (int i = 0; i < sub_field_length; i++) {
                        sub_field_raw_data[i] = field_raw_data[i + 2];
                    }
                    field_decode_data = DataConverter.IPAddress(sub_field_raw_data);
                    return field_decode_data;

                } else {
                    if (field_raw_data[0] == 0x81) {        // eTSIAddress
                        field_decode_data = DataConverter.Int2HexString(field_raw_data);
                        return field_decode_data;
                    } else {
                        field_decode_data = "Unknow subTAG" + DataConverter.Int2HexString(field_raw_data);
                        return field_decode_data;
                    }
                }
//---------------------- End of case x88 ----------------------

//---------------------- Start of case xA9 (dynamicAddressFlag) ----------------------
            case "x8B":
                field_decode_data = DataConverter.Int2HexString(field_raw_data);
                return field_decode_data;
//---------------------- End of case x8B ----------------------

//---------------------- Start of case xAC (listOfTrafficVolumes) ----------------------
            case "xAC":
                if (field_raw_data[0] == 0x30) {    //Check is SubTag 0x30
                    SubFieldDecoder SubFieldDecoder1 = new SubFieldDecoder();
                    field_decode_data = "";
                    String decode_data_xAC_x30;
                    int xAC_indx = 0;
                    int xAC_len = field_raw_data.length;
                    int xAC_sub_len;

                    String[] str_decode = new String[3];  //Debug
                    int arr_indx = 0;                     //Debug

                    do {
                        int xAC30_sub_len = field_raw_data[xAC_indx + 1];  //  +2 for TagHeader & Length
                        int[] raw_xAC_x30 = new int[xAC30_sub_len];
                        for (int i = 0; i < xAC30_sub_len; i++) {
                            raw_xAC_x30[i] = field_raw_data[xAC_indx + 2];
                            xAC_indx++;
                        }
                        xAC_indx += 2;    // Skip TagHeader+length (2Byte)

                        decode_data_xAC_x30 = SubFieldDecoder1.decode_xAC_x30(raw_xAC_x30);
                        field_decode_data = field_decode_data + "," + decode_data_xAC_x30;
                    } while (xAC_indx < xAC_len);
                } else {
                    field_decode_data = " Unknow Tag";
                }

                field_decode_data = field_decode_data.substring(1, field_decode_data.length());  //trim first comma (,)
                field_decode_data = "{" + field_decode_data + "}";
                return field_decode_data;

//---------------------- End of case xAC ----------------------
//---------------------- Start of case x91 (recordSequenceNumber) ----------------------
            case "x91":
                field_raw_hex_string = "0x";
                for (int i = 0; i < (field_raw_data.length); i++) {
                    field_raw_hex_string = field_raw_hex_string + String.format("%02X", field_raw_data[i]);
                }
                field_decode_data = Long.toString(Long.decode(field_raw_hex_string));
                return field_decode_data;
//---------------------- End of case x91 ----------------------       

//---------------------- Start of case x92 (nodeID) ----------------------
            case "x92":
                field_decode_data = DataConverter.IA5String(field_raw_data);
                return field_decode_data;
//---------------------- End of case x92 ----------------------
                
//---------------------- Start of case x94 (localSequenceNumber) ----------------------
            case "x94":
                field_raw_hex_string = "0x";
                for (int i = 0; i < (field_raw_data.length); i++) {
                    field_raw_hex_string = field_raw_hex_string + String.format("%02X", field_raw_data[i]);
                }
                field_decode_data = Long.toString(Long.decode(field_raw_hex_string));
                return field_decode_data;
//---------------------- End of case x94 ----------------------                        

//---------------------- Start of case x95 (apnSelectionMode) ----------------------
            case "x95":
                field_decode_data = Integer.toString(field_raw_data[0]);
                return field_decode_data;
//---------------------- End of case x95 ----------------------        
        
//---------------------- Start of case x96 (servedMSISDN) ----------------------
            case "x96":
                int[] servedMSISDN_int = new int[field_raw_data.length];
                if (field_raw_data.length == 7) {
                    for (int i = 0; i < 6; i++) {
                        servedMSISDN_int[i] = field_raw_data[i+1];  // Delete First Byte (91)
                    }
                    field_decode_data = DataConverter.tBCD2String(servedMSISDN_int);
                } else {
                    for (int i = 0; i < (field_raw_data.length-1); i++) {
                        servedMSISDN_int[i] = field_raw_data[i + 1];
                    }
                    field_decode_data = DataConverter.tBCD2String(servedMSISDN_int);    // Delete First Byte (91)
//                    field_decode_data = "(" + String.format("%02X", field_raw_data[0]) + ")" + DataConverter.tBCD2String(servedMSISDN_int);   // Include First Byte (91)
                }
                return field_decode_data;
//---------------------- End of case x96 ----------------------

//---------------------- Start of case x97 (chargingCharacteristics) ----------------------
            case "x97":
                field_decode_data = DataConverter.Int2HexString(field_raw_data);
                return field_decode_data;
//---------------------- End of case x97 ----------------------

//---------------------- Start of case x98 (chChSelectionMode) ----------------------
            case "x98":
                field_decode_data = Integer.toString(field_raw_data[0]);
                return field_decode_data;
//---------------------- End of case x98 ----------------------

//---------------------- Start of case x9B (servingNodePLMNIdentifier) ----------------------
            case "x9B":
                field_decode_data = DataConverter.tBCD2String(field_raw_data);
                return field_decode_data;
//---------------------- End of case x9B ----------------------

//---------------------- Start of case x9D (servedIMEISV) ----------------------
            case "x9D":
                field_decode_data = DataConverter.tBCD2String(field_raw_data);
                return field_decode_data;
//---------------------- End of case x9D ----------------------

//---------------------- Start of case x9E (rATType) ----------------------
            case "x9E":
                field_decode_data = Integer.toString(DataConverter.hexString2int(DataConverter.Int2HexString(field_raw_data)));
                return field_decode_data;
//---------------------- End of case x9E ----------------------

//---------------------- Start of case xB0 (diagnostics) ----------------------
            case "xB0":
                field_decode_data = DataConverter.Int2HexString(field_raw_data);
                return field_decode_data;
//---------------------- End of case x97 ----------------------                        

//---------------------- Start of case x9F 1F (mSTimeZone) ----------------------
            case "x9F1F":
                field_decode_data = DataConverter.Int2HexString(field_raw_data);
                return field_decode_data;
//---------------------- End of case x9F 1F ----------------------

//---------------------- Start of case x9F 20 (userLocationInformation) ----------------------
            case "x9F20":
                field_decode_data = SubFieldDecoder.userLocInfo(field_raw_data);
                return field_decode_data;
//---------------------- End of case x9F 20 ---------------------- 

//---------------------- Start of case xBF 23 (servingNodeType) ----------------------
            case "xBF23":
                field_decode_data = Integer.toString(field_raw_data[(field_raw_data.length - 1)]);
                return field_decode_data;
//---------------------- End of case xBF 23 ----------------------

//---------------------- Start of case x9F 25 (pGWPLMNIdentifier) ----------------------
            case "x9F25":
                field_decode_data = "PGW-PLMNid:" + DataConverter.tBCD2String(field_raw_data);
                return field_decode_data;
//---------------------- End of case x9F 25 ----------------------

//---------------------- Start of case x9F 26 (startTime) ----------------------
            case "x9F26":
                field_decode_data = DataConverter.Int2DateTime(field_raw_data);
                return field_decode_data;
//---------------------- End of case x9F 26 ----------------------

//---------------------- Start of case Default ----------------------
            default:
                field_decode_data = "Unknow TAG:" + DataConverter.Int2HexString(field_raw_data);
                return field_decode_data;
        }
    }
}
