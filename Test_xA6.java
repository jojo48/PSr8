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
public class Test_xA6 {

    public static void main(String[] args) {

        DataConverter DataConverter1 = new DataConverter();  //Create Object DataConverter
        String field_decode_data = "";
        String field_raw_hex_string = "0x";
        int field_indx = 0;
        String sub_field_type;
        String sub_field_list = "x80|x81|x82|x83|";

//        int[] field_raw_data = new int[]{128, 4, 180, 214, 200, 225, 120, 4, 180, 214, 200, 194, 128, 4, 180, 214, 200, 123};  // {80,04,B4,D6,C8,E1,80,04,B4,D6,C8,C2}
        int[] field_raw_data = new int[]{128, 4, 1, 2, 3, 4, 129, 4, 5, 6, 7, 8, 130, 4, 9, 10, 11, 12, 131, 4, 13, 14, 15, 16, 128, 4, 17, 18, 19, 20};  // {80,04,B4,D6,C8,E1,80,04,B4,D6,C8,C2}
        int field_len = field_raw_data.length;
        int remain_raw_byte = field_len;

        int sub_field_len;

        System.out.println("field raw data length: " + field_raw_data.length);
        field_decode_data = "{";

        do {
            String sub_raw = "";
            sub_field_type = "x" + String.format("%02X", field_raw_data[field_indx]) + "|";

            System.out.println("sub field type:" + sub_field_type); //Debug

            if (sub_field_list.contains(sub_field_type)) {
                sub_field_len = field_raw_data[field_indx + 1];
                int[] sub_field_raw_data = new int[sub_field_len + 2];
                for (int i = 0; i < sub_field_len + 2; i++) {
                    sub_field_raw_data[i] = field_raw_data[field_indx];

                    sub_raw = sub_raw + String.format("%02X", field_raw_data[field_indx]) + "|"; //Debug

                    field_indx++;
                }
                
                System.out.println("sub raw data:" + sub_raw);    //Debug
                
            } else {
                sub_raw = "";
                int remain_raw = (field_len - field_indx);
                for (int i = 0; i < remain_raw; i++) {
                    sub_raw = sub_raw + String.format("%02X", field_raw_data[field_indx]) + "|"; //Debug
                    field_indx++;
                }
                System.out.println("Unknow sub_field type: " + sub_field_type + " Raw Data:" + sub_raw);
                break;
            }
        } while (field_indx < field_len);

    }

}