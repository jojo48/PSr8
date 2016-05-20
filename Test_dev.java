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
public class Test_dev {

    public static void main(String[] args) {

//        case "xA6": //----- servingNodeAddress -----
        
        
        DataConverter DataConverter1 = new DataConverter();  //Create Object DataConverter
        String field_decode_data;
        String field_raw_hex_string = "0x";
        int field_indx = 0;
        String sub_field_type;
        String sub_field_list = "x80|x81|x82|x83|";

//        int[] field_raw_data = new int[]{128, 4, 180, 214, 200, 225, 120, 4, 180, 214, 200, 194, 128, 4, 180, 214, 200, 123};  // {80,04,B4,D6,C8,E1,80,04,B4,D6,C8,C2}
        int[] field_raw_data = new int[]{128, 4, 1, 2, 3, 4, 128, 4, 5, 6, 7, 8, 128, 4, 9, 10, 11, 12, 128, 4, 13, 14, 15, 16, 128, 4, 17, 18, 19, 20};  // {80,04,B4,D6,C8,E1,80,04,B4,D6,C8,C2}
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
                
                System.out.println("sub raw data:" + sub_raw+" field_indx:"+field_indx);    //Debug
                
                field_decode_data=field_decode_data+"["+DataConverter1.IPAddress(sub_field_raw_data)+"]";
                
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
        field_decode_data=field_decode_data+"}";
        System.out.println("field_decode_data: "+field_decode_data);
        
       }
}
        
    

            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
//            
//            
//            field_decode_data = field_decode_data + DataConverter1.IPAddress(sub_field_raw_data);
//            if (((field_len - 1) - field_indx) >= 6) {
//                field_decode_data = field_decode_data + ",";
//            } else {
//                int unknow_len = (field_len - 1) - field_indx;
//                int[] field_raw_data_unknow = new int[unknow_len];
//                for (int i = 0; i < (unknow_len - 1); i++) {
//                    field_raw_data_unknow[i] = field_raw_data[field_indx];
//                    field_indx++;
//                }
//
//                field_decode_data = field_decode_data + " Unknow:" + DataConverter1.Int2HexString(field_raw_data_unknow);
//            }
//        } else {
//            field_decode_data = field_decode_data + "}";
//
//        }
//
//        field_decode_data = field_decode_data + "}";
//
//        System.out.println("field decode data: " + field_decode_data);
//
//    }
//}
















//        String field_decode_data ="";
//         String field_raw_hex_string = "0x";
//         long tmp_long=0;
//        
//        int[] field_raw_data = new int[]{00,139,110,111,74};  // {00,8B,6E,6F,4A}
//                       
//                for (int i = 0; i < (field_raw_data.length); i++) {
//                    field_raw_hex_string = field_raw_hex_string + String.format("%02X", field_raw_data[i]);
//                }
//                System.out.println("field_raw_hex_string: "+field_raw_hex_string);
//
////                tmp_long=Long.decode(field_raw_hex_string);
//
//               field_decode_data=Long.toString(Long.decode(field_raw_hex_string));
//               
//               
//               System.out.println("DecodeData: "+field_decode_data);
//               System.out.println("tmp_long: "+tmp_long);
//               System.out.println("field_decode_data: "+field_decode_data);
//                
//        
//        int[] rawInt = new int[]{37,00,32,80,01,97,00,249};  // {25,00,20,50,01,61,00,F9}
//        
//        
////            public String tBCD2String(int[] rawInt) {
//        String tBCDstring ="";
//        for(int i=0; i<rawInt.length; i++) {
//            String rawByteHexString = String.format("%02X", rawInt[i]);
//            System.out.println("rawByteHexString: "+rawByteHexString);
//            System.out.println("SubString LSB: "+rawByteHexString.substring(0,1));
//            System.out.println("SubString MSB: "+rawByteHexString.substring(1,2));
//            
//            if(!"F".equals(rawByteHexString.substring(0,1))){
//              tBCDstring =   tBCDstring+rawByteHexString.substring(1,2);
//              tBCDstring =   tBCDstring+rawByteHexString.substring(0,1);
//            }else{
//                tBCDstring =   tBCDstring+rawByteHexString.substring(1,2);
//            }
//        }
////        return tBCDstring;
//        System.out.println("tBCDstring: "+tBCDstring);
//    }
//------------- Measurement Time -------------------------------    
//    public static void main(String args[]) {
//            int MAX_ITERATION = 10000000;
//        long starttime = System.currentTimeMillis();
//        for (int i = 0; i < MAX_ITERATION; ++i) {
//            String s = Integer.toString(10);
//        }
//        long endtime = System.currentTimeMillis();
//        System.out.println("diff1: " + (endtime-starttime));
//
//        starttime = System.currentTimeMillis();
//        for (int i = 0; i < MAX_ITERATION; ++i) {
//            String s1 = new Integer(10).toString();
//        }
//        endtime = System.currentTimeMillis();
//        System.out.println("diff2: " + (endtime-starttime));
//    }
//    
//        int dec_a=0x00, dec_b=0xB7,dec_val;     // 137d = 311h
//        String hex_a, hex_b,hex_str;
//   
//        System.out.println("dec_a:"+dec_a+"  dec_b:"+dec_b);
//        
////        hex_a = Integer.toHexString(dec_a);
////        hex_b = Integer.toHexString(dec_b);
//        
//        hex_a = String.format("%02X",dec_a);
//        hex_b = String.format("%02X",dec_b);
//        System.out.println("Hex_a:"+ hex_a+"   Hex_b:" + hex_b+"  Hex_Str:"+hex_a+hex_b);
//        
//        hex_str="0x"+hex_a+hex_b;
//        dec_val= Integer.decode(hex_str);
//        System.out.println("Decimal_value:"+dec_val);
//        
//    }
//String hexNumber = ...
//int decimal = Integer.parseInt(hexNumber, 16);
//System.out.println("Hex value is " + decimal);
//
//int i = ...
//String hex = Integer.toHexString(i);
//System.out.println("Hex value is " + hex);
//
//String string = ...
//int no = Integer.parseInt(string);
//String hex = Integer.toHexString(no);
//System.out.println("Hex value is " + hex);


//}
