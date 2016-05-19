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

//                field_decode_data=DataConverter.Int2HexString(field_raw_data);
//                return field_decode_data;
                
                if(field_raw_data[0]==0x30){    //Check is SubTag 0x30
                    
                
//                DataConverter DataConverter = new DataConverter();
                SubFieldDecoder SubFieldDecoder1 = new SubFieldDecoder();
                field_decode_data = "";

 
//                String xAC_30_decode_data;
                String decode_data_xAC_x30="";
//                int field_raw_data_indx = 0;
                int xAC_indx = 0;
                int xAC_len = field_raw_data.length;  // may be not use
//                String xAC_30_tag_list = "x81,x82,x83,x84,x85,x86,x87,x88,xA9";
                int xAC_sub_len;

                do {
                    int xAC30_sub_len = field_raw_data[xAC_indx + 1];  //  +2 for TagHeader & Length
                    int[] raw_xAC_x30 = new int[xAC30_sub_len];
                    for (int i = 0; i < xAC30_sub_len; i++) {
                        raw_xAC_x30[i] = field_raw_data[xAC_indx+2];
                        xAC_indx++;
                    }
                     xAC_indx+=2;    // Skip TagHeader+length (2Byte)
                     int decode_indx=0;
                     String sub1_decode_data="";
                     do{
                         int sub1_len=raw_xAC_x30[decode_indx+1];
                         int[] sub1_raw=new int[sub1_len];
                         for(int i=0;i<sub1_len;i++){
                             sub1_raw[i]=raw_xAC_x30[decode_indx];
                             decode_indx++;
                         }
                         sub1_decode_data=sub1_decode_data+","+SubFieldDecoder1.decode_xAC_x30(sub1_raw);
                     }while(xAC30_sub_len<decode_indx);
                     
                     
                    //xAC_indx--;
//                    decode_data_xAC_x30 = SubFieldDecoder1.decode_xAC_x30(raw_xAC_x30);
//                    decode_data_xAC_x30 = decode_data_xAC_x30.substring(1, decode_data_xAC_x30.length());  //trim first comma (,)
//                    decode_data_xAC_x30 = "[" + decode_data_xAC_x30 + "]";
//                    field_decode_data = field_decode_data + decode_data_xAC_x30;
                    
                    field_decode_data=field_decode_data+DataConverter.Int2HexString(raw_xAC_x30)+" xAC_indx:"+xAC_indx+" xAC_len:"+xAC_len;
                    

                } while (xAC_indx < xAC_len);
                
                
                }else{
                    field_decode_data="Unknow Tag";
                }
                
                return field_decode_data;

//---------------------- End of case xAC ----------------------

//---------------------- Start of case x96 (servedMSISDN) ----------------------
            case "x96":
                int[] servedMSISDN_int = new int[field_raw_data.length];
                if (field_raw_data.length > 7) {
                    for (int i = 0; i < 7; i++) {
                        servedMSISDN_int[i] = field_raw_data[i + 1];
                    }
                    field_decode_data = DataConverter.tBCD2String(servedMSISDN_int);

                } else {
                    field_decode_data = "(" + String.format("%02X", field_raw_data[0]) + ")" + DataConverter.tBCD2String(field_raw_data);
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

//---------------------- Start of case x9F 1F (mSTimeZone) ----------------------
            case "x9F1F":
                field_decode_data = DataConverter.Int2HexString(field_raw_data);
                return field_decode_data;
//---------------------- End of case x9F 1F ----------------------

//---------------------- Start of case x9F 20 (userLocationInformation) ----------------------
            case "x9F20":
                int[] plmn_raw = new int[3];
                for (int i = 0; i < 3; i++) {
                    plmn_raw[i] = field_raw_data[i + 1];
                }
                String lac_hex_str = "0x" + String.format("%02X", field_raw_data[4]) + String.format("%02X", field_raw_data[5]);
                String ci_hex_str = "0x" + String.format("%02X", field_raw_data[6]) + String.format("%02X", field_raw_data[7]);

                if (field_raw_data.length == 8) {
                    if (field_raw_data[0] == 0) {    // 0=CGI =>(PLMN+LAC+CI) ; 1=SAI =>(PLMN+LAC+SAC)
                        field_decode_data = "PLMN:" + DataConverter.tBCD2String(plmn_raw) + " LAC:" + Integer.toString(DataConverter.hexString2int(lac_hex_str))
                                + " CI:" + Integer.toString(DataConverter.hexString2int(ci_hex_str));
                    } else {
                        field_decode_data = "PLMN:" + DataConverter.tBCD2String(plmn_raw) + " LAC:" + Integer.toString(DataConverter.hexString2int(lac_hex_str))
                                + " SAC:" + Integer.toString(DataConverter.hexString2int(ci_hex_str));
                    }
                } else {
                    if (field_raw_data.length == 0x0D) {
                        String tai_hex_str = "0x" + String.format("%02X", field_raw_data[4]) + String.format("%02X", field_raw_data[5]);
                        String ecgi_hex_str = "0x" + String.format("%02X", field_raw_data[9]) + String.format("%02X", field_raw_data[10])
                                + String.format("%02X", field_raw_data[11]) + String.format("%02X", field_raw_data[12]);
                        if (field_raw_data[0] == 0x18) {    // 0x18 = 4G (TAI,ECGI)
                            field_decode_data = "PLMN:" + DataConverter.tBCD2String(plmn_raw) + " TAI:" + Integer.toString(DataConverter.hexString2int(tai_hex_str))
                                    + " ECGI:" + Integer.toString(DataConverter.hexString2int(ecgi_hex_str));
                        } else {
                            field_decode_data = "Unknow TAG:" + DataConverter.Int2HexString(field_raw_data);
                        }
                    } else {
                        field_decode_data = "Unknow TAG:" + DataConverter.Int2HexString(field_raw_data);
                    }
                }
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
                field_decode_data = DataConverter.Int2Datetime(field_raw_data);
                return field_decode_data;
//---------------------- End of case x9F 26 ----------------------

//---------------------- Start of case Default ----------------------
            default:
                field_decode_data = "Unknow TAG:" + DataConverter.Int2HexString(field_raw_data);
                return field_decode_data;
        }
    }
}
