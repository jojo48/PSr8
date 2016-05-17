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
public class SubFieldDecoder {

    DataConverter DataConverter = new DataConverter();

    public String decode_xAC_x30(int[] raw_xAC_x30_sub1) {
        String subTAG_xAC_30 = "x" + String.format("%02X", raw_xAC_x30_sub1[0]);
        int raw_xAC_30_sub1_len = raw_xAC_x30_sub1.length - 2;
        int[] raw_data_xAC_x30_sub1 = new int[raw_xAC_30_sub1_len];

        for (int i = 0; i < raw_xAC_30_sub1_len; i++) {
            raw_data_xAC_x30_sub1[i] = raw_xAC_x30_sub1[i + 2];
        }
        String decode_data_xAC_x30;
        switch (subTAG_xAC_30) {
//--------------------------------- Start x81 ---------------------------------
            case "x81":
                decode_data_xAC_x30 = DataConverter.Int2HexString(raw_data_xAC_x30_sub1);
                return decode_data_xAC_x30;
//--------------------------------- End x81 ---------------------------------

//--------------------------------- Start x82 ---------------------------------
            case "x82":
                decode_data_xAC_x30 = DataConverter.Int2HexString(raw_data_xAC_x30_sub1);
                return decode_data_xAC_x30;
//--------------------------------- End x82 ---------------------------------

//--------------------------------- Start x83 (dataVolumeMBMSUplink) ---------------------------------
            case "x83":
                decode_data_xAC_x30 = "dataVolumeUplink:" + Integer.toString(DataConverter.hexString2int(DataConverter.Int2HexString(raw_data_xAC_x30_sub1)));
                return decode_data_xAC_x30;
//--------------------------------- End x83 ---------------------------------

//--------------------------------- Start x84 (dataVolumeMBMSDownlink) ---------------------------------
            case "x84":
                decode_data_xAC_x30 = "dataVolumeDownlink:" + Integer.toString(DataConverter.hexString2int(DataConverter.Int2HexString(raw_data_xAC_x30_sub1)));
                return decode_data_xAC_x30;
//--------------------------------- End x84 ---------------------------------

//--------------------------------- Start x85 (changeCondition) ---------------------------------
            case "x85":
                decode_data_xAC_x30 = "changeCondition:" + Integer.toString(DataConverter.hexString2int(DataConverter.Int2HexString(raw_data_xAC_x30_sub1)));
                return decode_data_xAC_x30;
//--------------------------------- End x85 ---------------------------------

//--------------------------------- Start x86 (changeTime) ---------------------------------
            case "x86":
                decode_data_xAC_x30 = "changeTime:" + DataConverter.Int2Datetime(raw_data_xAC_x30_sub1);
                return decode_data_xAC_x30;
//--------------------------------- End x86 ---------------------------------

//--------------------------------- Start x87 ---------------------------------
            case "x87":
                decode_data_xAC_x30 = DataConverter.Int2HexString(raw_data_xAC_x30_sub1);
                return decode_data_xAC_x30;
//--------------------------------- End x87 ---------------------------------

//--------------------------------- Start x88 ---------------------------------
            case "x88":
                decode_data_xAC_x30 = DataConverter.Int2HexString(raw_data_xAC_x30_sub1);
                return decode_data_xAC_x30;
//--------------------------------- End x88 ---------------------------------

//--------------------------------- Start xA9 (ePCQoSInformation) ---------------------------------
            case "xA9":
                int raw_len = raw_data_xAC_x30_sub1.length;
                int raw_indx = 0;

                String decode_data = "";
                String tag_string;

                while (raw_indx < raw_len) {
                    tag_string = "x" + String.format("%02X", raw_data_xAC_x30_sub1[raw_indx]);
                    raw_indx++;
                    int raw_tag_len = raw_data_xAC_x30_sub1[raw_indx];
                    raw_indx++;
                    int[] raw_tag_data = new int[raw_tag_len];
                    for (int j = 0; j < raw_tag_len; j++) {
                        raw_tag_data[j] = raw_data_xAC_x30_sub1[raw_indx];
                        raw_indx++;
                    }
                    decode_data = decode_data+","+decode_xAC_x30_xA9(tag_string, raw_tag_data);
                }
                decode_data=decode_data.substring(1,decode_data.length());  //trim first comma (,)
                return decode_data;
//--------------------------------- End xA9 ---------------------------------

//--------------------------------- Start Default ---------------------------------
            default:
                decode_data_xAC_x30 = "Unknow TAG:" + DataConverter.Int2HexString(raw_data_xAC_x30_sub1);
                return decode_data_xAC_x30;
//--------------------------------- End Default ---------------------------------
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
}