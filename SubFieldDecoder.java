/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pgw;

/**
 *
 * @author Samreng 2016-05-14 15:15
 */
class SubFieldDecoder {

    DataConverter DataConverter = new DataConverter();
    String decode_data_xAC_x30_sub;

    String decode_xAC_x30(int[] raw_xAC_x30) {
        decode_data_xAC_x30_sub = "";
        int raw_xAC_x30_len = raw_xAC_x30.length;
        int xAC_x30_sub1_len;
        for (int xAC_x30_indx = 0; xAC_x30_indx < raw_xAC_x30_len; xAC_x30_indx++) {
            xAC_x30_sub1_len = raw_xAC_x30[xAC_x30_indx + 1];
            int[] raw_xAC_x30_sub1 = new int[xAC_x30_sub1_len + 2];
            for (int i = 0; i < (xAC_x30_sub1_len + 2); i++) {
                raw_xAC_x30_sub1[i] = raw_xAC_x30[xAC_x30_indx];
                xAC_x30_indx++;
            }
            xAC_x30_indx--;
            decode_data_xAC_x30_sub = decode_data_xAC_x30_sub + "," + xAC_x30_decode(raw_xAC_x30_sub1);
        }
        decode_data_xAC_x30_sub = decode_data_xAC_x30_sub.substring(1, decode_data_xAC_x30_sub.length());  //trim first comma (,)
        decode_data_xAC_x30_sub = "[" + decode_data_xAC_x30_sub + "]";
        return decode_data_xAC_x30_sub;
    }

    String xAC_x30_decode(int[] raw_xAC_x30_sub) {

        Test_Main_PGW Main_PGW = new Test_Main_PGW();                     // Create Object Test_Main_PGW
        PGW2TXT PGW2TXT = new PGW2TXT();
        int raw_xAC_x30_sub_len = raw_xAC_x30_sub.length;
        int[] raw_xAC_x30_sub1 = new int[raw_xAC_x30_sub_len - 2];
        for (int i = 0; i < (raw_xAC_x30_sub_len - 2); i++) {
            raw_xAC_x30_sub1[i] = raw_xAC_x30_sub[i + 2];
        }
        String subTAG_xAC_x30 = "x" + String.format("%02X", raw_xAC_x30_sub[0]);

        switch (subTAG_xAC_x30) {
//--------------------------------- Start x81 ---------------------------------
            case "x81":
                decode_data_xAC_x30_sub = DataConverter.Int2HexString(raw_xAC_x30_sub1);
                return decode_data_xAC_x30_sub;
//--------------------------------- End x81 ---------------------------------

//--------------------------------- Start x82 ---------------------------------
            case "x82":
                decode_data_xAC_x30_sub = DataConverter.Int2HexString(raw_xAC_x30_sub1);
                return decode_data_xAC_x30_sub;
//--------------------------------- End x82 ---------------------------------

//--------------------------------- Start x83 (dataVolumeMBMSUplink) ---------------------------------
            case "x83":
                long dataVolumeUplink = DataConverter.hexString2int(DataConverter.Int2HexString(raw_xAC_x30_sub1));
                Main_PGW.DataUplink(dataVolumeUplink);  //Sum data volume uplink
                PGW2TXT.DataUplink(dataVolumeUplink);
                decode_data_xAC_x30_sub = "dataVolumeUplink:" + Long.toString(dataVolumeUplink);
                return decode_data_xAC_x30_sub;
//--------------------------------- End x83 ---------------------------------

//--------------------------------- Start x84 (dataVolumeMBMSDownlink) ---------------------------------
            case "x84":
                long dataVolumeDownlink = DataConverter.hexString2int(DataConverter.Int2HexString(raw_xAC_x30_sub1));
                Main_PGW.DataDownlink(dataVolumeDownlink);  //Sum data volume downlink
                PGW2TXT.DataDownlink(dataVolumeDownlink);
                decode_data_xAC_x30_sub = "dataVolumeDownlink:" + Long.toString(dataVolumeDownlink);
                return decode_data_xAC_x30_sub;
//--------------------------------- End x84 ---------------------------------

//--------------------------------- Start x85 (changeCondition) ---------------------------------
            case "x85":
                decode_data_xAC_x30_sub = "changeCondition:" + Integer.toString(DataConverter.hexString2int(DataConverter.Int2HexString(raw_xAC_x30_sub1)));
                return decode_data_xAC_x30_sub;
//--------------------------------- End x85 ---------------------------------

//--------------------------------- Start x86 (changeTime) ---------------------------------
            case "x86":
                decode_data_xAC_x30_sub = "changeTime:" + DataConverter.Int2DateTime(raw_xAC_x30_sub1);
                return decode_data_xAC_x30_sub;
//--------------------------------- End x86 ---------------------------------

//--------------------------------- Start x87 ---------------------------------
            case "x87":
                decode_data_xAC_x30_sub = DataConverter.Int2HexString(raw_xAC_x30_sub1);
                return decode_data_xAC_x30_sub;
//--------------------------------- End x87 ---------------------------------

//--------------------------------- Start x88 ---------------------------------
            case "x88":
                decode_data_xAC_x30_sub = userLocInfo(raw_xAC_x30_sub1);
//                decode_data_xAC_x30_sub = DataConverter.Int2HexString(raw_xAC_x30_sub1);
                return decode_data_xAC_x30_sub;
//--------------------------------- End x88 ---------------------------------

//--------------------------------- Start xA9 (ePCQoSInformation) ---------------------------------
            case "xA9":
                int raw_len = raw_xAC_x30_sub1.length;
                int raw_indx = 0;

                String decode_data = "";
                String tag_string;

                while (raw_indx < raw_len) {
                    tag_string = "x" + String.format("%02X", raw_xAC_x30_sub1[raw_indx]);
                    raw_indx++;
                    int raw_tag_len = raw_xAC_x30_sub1[raw_indx];
                    raw_indx++;
                    int[] raw_tag_data = new int[raw_tag_len];
                    for (int j = 0; j < raw_tag_len; j++) {
                        raw_tag_data[j] = raw_xAC_x30_sub1[raw_indx];
                        raw_indx++;
                    }
                    decode_data = decode_data + "," + decode_xAC_x30_xA9(tag_string, raw_tag_data);
                }
                decode_data = decode_data.substring(1, decode_data.length());  //trim first comma (,)
                return decode_data;
//--------------------------------- End xA9 ---------------------------------

//--------------------------------- Start Default ---------------------------------
            default:
                decode_data_xAC_x30_sub = "Unknow TAG:" + DataConverter.Int2HexString(raw_xAC_x30_sub1);
                return decode_data_xAC_x30_sub;
//--------------------------------- End Default ---------------------------------//--------------------------------- End Default ---------------------------------
        }
    }

    private String decode_xAC_x30_xA9(String tag_string, int[] raw_tag_data) {

        String decode_data;

        switch (tag_string) {
            case "x81":
                decode_data = "qCI:" + Integer.toString(DataConverter.hexString2int(DataConverter.Int2HexString(raw_tag_data)));
                return decode_data;

            case "x82":
                decode_data = "maxRequestedBandwithUL:" + Integer.toString(DataConverter.hexString2int(DataConverter.Int2HexString(raw_tag_data)));
                return decode_data;

            case "x83":
                decode_data = "maxRequestedBandwithDL:" + Integer.toString(DataConverter.hexString2int(DataConverter.Int2HexString(raw_tag_data)));
                return decode_data;

            case "x84":
                decode_data = "guaranteedBitrateUL:" + Integer.toString(DataConverter.hexString2int(DataConverter.Int2HexString(raw_tag_data)));
                return decode_data;

            case "x85":
                decode_data = "guaranteedBitrateDL:" + Integer.toString(DataConverter.hexString2int(DataConverter.Int2HexString(raw_tag_data)));
                return decode_data;

            case "x86":
                decode_data = "aRP:" + Integer.toString(DataConverter.hexString2int(DataConverter.Int2HexString(raw_tag_data)));
                return decode_data;

            case "x87":
                decode_data = "aPNAggregateMaxBitrateUL:" + Integer.toString(DataConverter.hexString2int(DataConverter.Int2HexString(raw_tag_data)));
                return decode_data;

            case "x88":
                decode_data = "aPNAggregateMaxBitrateDL:" + Integer.toString(DataConverter.hexString2int(DataConverter.Int2HexString(raw_tag_data)));
                return decode_data;

            default:
                decode_data = "UnknowTAG xAC-x30-xA9 !!! (" + tag_string + ")";
                return decode_data;
        }
    }

    String userLocInfo(int[] raw_loc_info) {
        String decode_user_loc_info;

        int[] plmn_raw = new int[3];
        for (int i = 0; i < 3; i++) {
            plmn_raw[i] = raw_loc_info[i + 1];
        }
        String lac_hex_str = "0x" + String.format("%02X", raw_loc_info[4]) + String.format("%02X", raw_loc_info[5]);
        String ci_hex_str = "0x" + String.format("%02X", raw_loc_info[6]) + String.format("%02X", raw_loc_info[7]);

        String lacHex = String.format("%02X", raw_loc_info[4]) + String.format("%02X", raw_loc_info[5]);    // use for display LAC+SAC in Hex format
        String ciHex = String.format("%02X", raw_loc_info[6]) + String.format("%02X", raw_loc_info[7]);     // use for display LAC+SAC in Hex format

        if (raw_loc_info.length == 8) {
            if (raw_loc_info[0] == 0) {        // 0=CGI =>(PLMN+LAC+CI) ; 1=SAI =>(PLMN+LAC+SAC)

                decode_user_loc_info = "PLMN:" + DataConverter.tBCD2String(plmn_raw) + " LAC:" + Integer.toString(DataConverter.hexString2int(lac_hex_str)) // Original Include TagName
                        + " CI:" + Integer.toString(DataConverter.hexString2int(ci_hex_str));
            } else {
                decode_user_loc_info = "PLMN:" + DataConverter.tBCD2String(plmn_raw) + " LAC:" + Integer.toString(DataConverter.hexString2int(lac_hex_str)) // Original Include TagName
                        + " SAC:" + Integer.toString(DataConverter.hexString2int(ci_hex_str));                                                                // Original Include TagName
            }
        } else {
            if (raw_loc_info.length == 0x0D) {
                String tai_hex_str = "0x" + String.format("%02X", raw_loc_info[4]) + String.format("%02X", raw_loc_info[5]);
                String ecgi_hex_str = "0x" + String.format("%02X", raw_loc_info[9]) + String.format("%02X", raw_loc_info[10])
                        + String.format("%02X", raw_loc_info[11]) + String.format("%02X", raw_loc_info[12]);
                if (raw_loc_info[0] == 0x18) {    // 0x18 = 4G (TAI,ECGI)
                    decode_user_loc_info = "PLMN:" + DataConverter.tBCD2String(plmn_raw) + " TAI:" + Integer.toString(DataConverter.hexString2int(tai_hex_str))
                            + " ECGI:" + Integer.toString(DataConverter.hexString2int(ecgi_hex_str));
                } else {
                    decode_user_loc_info = "Unknow TAG:" + DataConverter.Int2HexString(raw_loc_info);
                }
            } else {
                decode_user_loc_info = "Unknow TAG:" + DataConverter.Int2HexString(raw_loc_info);
            }
        }

        decode_user_loc_info = lacHex + ciHex;  // Edit 20160715 09:12 remove TagName by override data from location decoder

        return decode_user_loc_info;
    }
}
