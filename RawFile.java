/**
 * @author Samreng Kalika 21 April 2016 11:07 Read binary file and store in
 * array (Integer format)
 */

package pgw;

import java.io.*;
import java.io.FileInputStream;
import java.io.IOException;

class RawFile {
    public int[] readBinaryFile(String fileName) throws FileNotFoundException, IOException {
        InputStream inputStream = new FileInputStream(fileName); //throws IOException;
        {
            int rdInt;
            int fileLength = inputStream.available();
            int[] intArrayBuffer = new int[fileLength + 1];     //Create Array for store Integer Data

            for (int indx = 0; indx <= fileLength; indx++) {
                rdInt = inputStream.read();         //Read one byte
                intArrayBuffer[indx] = rdInt;       // Store to array
            }
            inputStream.close();        //add command close inputStream
            return intArrayBuffer;      //Returen File Value Array (Integer)
        }
    }
    
     public int[] getRawField(int[] rawFileInt, int indx, int field_len) {
        int[] array_field_int_val = new int[field_len];
        for (int i = 0; i < field_len; i++) {
            array_field_int_val[i] = rawFileInt[indx + i];
        }
        return array_field_int_val;
    }
}
