/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pgw;

/**
 *
 * @author Samreng 2016-05-03 09:49
 */
public class DataConverter {

    public String Int2HexString(int[] rawInt2HexString) {
        String hexString = "0x";
        for (int i = 0; i < rawInt2HexString.length; i++) {
            hexString = hexString + String.format("%02X", rawInt2HexString[i]);
        }
        return hexString;

    }

    public int hexString2int(String hex_str) {
        int int_val;
//        hex_str = "0x" + hex_str;   //add prefix Hex String
        int_val = Integer.decode(hex_str);
        return int_val;

    }

    public String tBCD2String(int[] rawInt2String) {
        String tBCDstring = "";
        for (int i = 0; i < rawInt2String.length; i++) {
            String rawByteHexString = String.format("%02X", rawInt2String[i]);
            if (!"F".equals(rawByteHexString.substring(0, 1))) {
                tBCDstring = tBCDstring + rawByteHexString.substring(1, 2);
                tBCDstring = tBCDstring + rawByteHexString.substring(0, 1);
            } else {
                tBCDstring = tBCDstring + rawByteHexString.substring(1, 2);
            }
        }
        return tBCDstring;
    }

    public String IPv4String(int[] rawInt2IntIP) {
        String IPv4Address = "";
        for (int i = 0; i < 4; i++) {
            IPv4Address = IPv4Address + Integer.toString(rawInt2IntIP[i]);
            if (i < 3) {
                IPv4Address = IPv4Address + ".";
            }
        }
        return IPv4Address;
    }

    public String IPAddress(int[] rawInt2IP) {
        String ip_decode_data;
        String tag_IP = "x" + String.format("%02X", rawInt2IP[0]);
        switch (tag_IP) {
            case "x80":
                int[] tag_raw_data = new int[4];
                for (int i = 0; i < 4; i++) {
                    tag_raw_data[i] = rawInt2IP[i + 2];
                }
                ip_decode_data = IPv4String(tag_raw_data);
                return ip_decode_data;

            case "x81":
                ip_decode_data = Int2HexString(rawInt2IP);
                return ip_decode_data;
            case "x82":
                ip_decode_data = Int2HexString(rawInt2IP);
                return ip_decode_data;
            case "x83":
                ip_decode_data = Int2HexString(rawInt2IP);
                return ip_decode_data;
            default:
                ip_decode_data = "unknow subTAG: " + Int2HexString(rawInt2IP);
                return ip_decode_data;
        }

    }

    public String IA5String(int[] rawInt2String) {
        String string_decode = "";
        for (int i = 0; i < (rawInt2String.length); i++) {
            string_decode = string_decode + (char) rawInt2String[i];
        }
        return string_decode;
    }

    public String Int2Datetime(int[] rawInt2Datetime) {
        String Datetime_decode;
        if (rawInt2Datetime.length == 9) {
            Datetime_decode = "20" + String.format("%02X", rawInt2Datetime[0]) + "-" + String.format("%02X", rawInt2Datetime[1]) + "-" + String.format("%02X", rawInt2Datetime[2]) // Date
                    + " " + String.format("%02X", rawInt2Datetime[3]) + ":" + String.format("%02X", rawInt2Datetime[4]) + ":" + String.format("%02X", rawInt2Datetime[5]) // Time
                    + " " + (char) rawInt2Datetime[6] + String.format("%02X", rawInt2Datetime[7]) + ":" + String.format("%02X", rawInt2Datetime[8]);   // Offset
        } else {
            Datetime_decode = Int2HexString(rawInt2Datetime);
        }
        return Datetime_decode;
    }
}
