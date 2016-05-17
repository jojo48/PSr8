/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pgw;

/**
 *
 * @author Samreng
 */
public class Test_xACx30xA9 {

    public static void main(String[] args) {

//         int[] raw_xAC_30 = new int[]{0xA9, 0x06, 0x81, 0x01, 0x06, 0x86, 0x01, 0x58};
        int[] raw_xAC_x30_xA9 = new int[]{0x81, 0x01, 0x06, 0x82, 0x01, 0x58, 0x83, 0x01, 0x06, 0x84, 0x01, 0x58, 0x85, 0x01, 0x06, 0x86, 0x01, 0x58, 0x87, 0x01, 0x06, 0x88, 0x01, 0x58, 0x89, 0x01, 0x58};

//        String xAC_x30_decode_data;
//        String decode_data_xAC_30_sub1;
//        int xAC_indx = 0;
//        int xAC_len = raw_xAC_30.length;  // may be not use

        int raw_len = raw_xAC_x30_xA9.length;
        int raw_indx = 0;

        String decode_data, tag_string;

        while (raw_indx < raw_len) {
            tag_string = "x" + String.format("%02X", raw_xAC_x30_xA9[raw_indx]);
            raw_indx++;
            int raw_tag_len = raw_xAC_x30_xA9[raw_indx];
            raw_indx++;

            int[] raw_tag_data = new int[raw_tag_len];

//            raw_tag_data[0] = raw_xAC_30[raw_indx];
            for (int j = 0; j < raw_tag_len; j++) {
                raw_tag_data[j] = raw_xAC_x30_xA9[raw_indx];
                raw_indx++;
            }

            decode_data = decodeRaw(tag_string, raw_tag_data);
            System.out.println("decode_data:" + decode_data);
        }
    }

    private static String decodeRaw(String tag_string, int[] raw_tag_data) {
        DataConverter DataConverter = new DataConverter();
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
