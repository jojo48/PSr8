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
public class Test_xA9 {

    public static void main(String[] args) {

        DataConverter DataConverter1 = new DataConverter();  //Create Object DataConverter
        String field_decode_data;
        String field_raw_hex_string = "0x";
        int field_indx = 0;
        String sub_field_type;
        String sub_field_list = "x80|x81|x82|x83|";

        int[] field_raw_data = new int[]{160, 6, 132, 4, 10, 224, 3, 52};  // {A0,06,80,04,0A,E0,03,34}
//        int[] field_raw_data = new int[]{128, 4, 1, 2, 3, 4, 129, 4, 5, 6, 7, 8, 130, 4, 9, 10, 11, 12, 131, 4, 13, 14, 15, 16, 128, 4, 17, 18, 19, 20};  // {80,04,B4,D6,C8,E1,80,04,B4,D6,C8,C2}
        int field_len = field_raw_data.length;
        int remain_raw_byte = field_len;

        int sub_field_len;

        System.out.println("field raw data length: " + field_raw_data.length);
//        field_decode_data = "{";

        int field_raw_length = field_raw_data.length;

        if (field_raw_data[0] == 0xA0) {        // iPAddress
            int sub_field_length = field_raw_data[1];
            int[] sub_field_raw_data = new int[sub_field_length];
            for (int i = 0; i < sub_field_length; i++) {
                sub_field_raw_data[i] = field_raw_data[i + 2];
            }
            field_decode_data=DataConverter1.IPAddress(sub_field_raw_data);
//            return field_decode_data;
            System.out.println("field_decode_data:"+field_decode_data);

        }else{
        if (field_raw_data[0] == 0x81) {        // eTSIAddress
field_decode_data=DataConverter1.Int2HexString(field_raw_data);
//return field_decode_data;
System.out.println("field_decode_data:"+field_decode_data);
        } else {
field_decode_data="Unknow subTAG"+DataConverter1.Int2HexString(field_raw_data);
//return field_decode_data;
System.out.println("field_decode_data:"+field_decode_data);
        }
        }

    }

}
