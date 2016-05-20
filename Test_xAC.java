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
public class Test_xAC {

//       DataConverter DataConverter2 = new DataConverter();
//       SubFieldDecoder SubFieldDecoder = new SubFieldDecoder();
    public static void main(String[] args) {

        DataConverter DataConverter = new DataConverter();
        SubFieldDecoder SubFieldDecoder = new SubFieldDecoder();

        //---------------------- Subfield xAC:30 ----------------------
//    30 1E 83 02 2E 0A 84 02 66 58 85 01 00 86 09 14 04 03 22 00 21 2B 07 00 A9 06 81 01 06 86 01 58
//       String xAC= 0x301E83022E0A8402665885010086091404032200212B0700A906810106860158
//    int[] raw_xAC_x30 = new int[]{0x30, 0x1E, 0x83, 0x02, 0x2E, 0x0A, 0x84, 0x02, 0x66, 0x58, 0x85, 0x01, 0x00, 0x86, 0x09, 0x14, 0x04, 0x03, 0x22, 0x00, 0x21, 0x2B, 0x07, 0x00, 0xA9, 0x06, 0x81, 0x01, 0x06, 0x86, 0x01, 0x58};
        int[] field_raw_data = new int[]{0x30, 0x1E, 0x83, 0x02, 0x2E, 0x0A, 0x84, 0x02, 0x66, 0x58, 0x85, 0x01, 0x00, 0x86, 0x09, 0x14, 0x04, 0x03, 0x22, 0x00, 0x21, 0x2B, 0x07, 0x00, 0xA9, 0x06, 0x81, 0x01, 0x06, 0x86, 0x01, 0x58, 0x30, 0x26, 0x83, 0x01, 0x08, 0x84, 0x01, 0x09, 0x85, 0x01, 0x00, 0x86, 0x09, 0x16, 0x04, 0x28, 0x09, 0x14, 0x00, 0x2B, 0x07, 0x00, 0x88, 0x08, 0x01, 0x25, 0xF0, 0x40, 0x11, 0xFC, 0x04, 0x97, 0xA9, 0x06, 0x81, 0x01, 0x08, 0x86, 0x01, 0x58};
//    int[] raw_xAC_x30 = new int[]{0x30, 0x1E, 0x83, 0x02, 0x2E, 0x0A, 0x84, 0x02, 0x66, 0x58, 0x85, 0x01, 0x00, 0x86, 0x09, 0x14, 0x04, 0x03, 0x22, 0x00, 0x21, 0x2B, 0x07, 0x00, 0xA9, 0x1B, 0x81, 0x01, 0x06, 0x82, 0x01, 0x58, 0x83, 0x01, 0x06, 0x84, 0x01, 0x58, 0x85, 0x01, 0x06, 0x86, 0x01, 0x58, 0x87, 0x01, 0x06, 0x88, 0x01, 0x58, 0x89, 0x01, 0x58};

//    public String sub_field_xAC_30(int[] raw_field_xAC_30) {
        String xAC_30_decode_data;
        String decode_data_xAC_30_sub1="";
        int field_raw_data_indx=0;
        int xAC_indx = 0;
        int xAC_len = field_raw_data.length;  // may be not use
        String xAC_30_tag_list = "x81,x82,x83,x84,x85,x86,x87,x88,xA9";
      
         do {
        int xAC30_sub_len=field_raw_data[xAC_indx+1]+2;  //  +2 for TagHeader & Length
        int[] raw_xAC_x30 =new int[xAC30_sub_len];
        for(int i=0;i<xAC30_sub_len;i++){
            raw_xAC_x30[i]=field_raw_data[xAC_indx];
            xAC_indx++;
        }
 
        
        
        
             int xAC_x30_indx = 0;
        if (raw_xAC_x30[xAC_x30_indx] == 0x30) {  // Check sub_tag level 1 (x30)
//            xAC_indx++;
//            xAC_x30_indx++;
            int xAC_x30_len = raw_xAC_x30[xAC_x30_indx+1];  // may be not use
//            xAC_indx++;
            xAC_x30_indx++;

            do {
                int raw_xAC_30_sub1_len = raw_xAC_x30[xAC_x30_indx + 2];    // +2 for Skip Tag & Length
                int[] sub_raw_xAC_30 = new int[raw_xAC_30_sub1_len + 2];

                for (int i = 0; i < (raw_xAC_30_sub1_len + 2); i++) {   //Tag+Length+Data (0x83, 0x02, 0x2E, 0x0A)
                    sub_raw_xAC_30[i] = raw_xAC_x30[xAC_x30_indx+1];
//                    xAC_indx++;
                    xAC_x30_indx++;
                }
                System.out.println("Dump array sub xAC30:" + DataConverter.Int2HexString(sub_raw_xAC_30));    //Debug
                decode_data_xAC_30_sub1 =decode_data_xAC_30_sub1+","+ SubFieldDecoder.decode_xAC_x30(sub_raw_xAC_30);
                System.out.println("decode_xAC_30_sub1:" + decode_data_xAC_30_sub1); // Debug
//            }
           
            
        }  while (xAC_x30_indx < xAC_x30_len);  
        } else{  // Unknow SubTag
              }
        
//         xAC_indx--;
        decode_data_xAC_30_sub1=decode_data_xAC_30_sub1.substring(1,decode_data_xAC_30_sub1.length());  //trim first comma (,)
        decode_data_xAC_30_sub1="["+decode_data_xAC_30_sub1+"]";
        
    } while(xAC_indx <xAC_len);
        
        
        
    }
//---------------------- End of Subfield xAC:30 ---------------------- 

}
