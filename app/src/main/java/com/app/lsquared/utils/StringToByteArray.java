package com.app.lsquared.utils;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class StringToByteArray {

    public static String getByteArray(){
        try {
            String s = "testing";
            String charsetName = "UTF-16";
            byte[] byteArray = s.getBytes("UTF-16");
            System.out.println(Arrays.toString(byteArray));
            System.out.println("Length of String"
                    + " " + s.length() + " "
                    + "Length of byte Array"
                    + " " + byteArray.length);
            return Arrays.toString(byteArray);
        }
        catch (UnsupportedEncodingException e) {
            System.out.println("Unsupported charset :" + e);
        }
        return "";
    }


    public static byte[] hexToByteArray(){
        String s = "AA11FE010010";
        byte[] ans = new byte[s.length() / 2];
        for (int i = 0; i < ans.length; i++) {
            int index = i * 2;
            int val = Integer.parseInt(s.substring(index, index + 2), 16);
            ans[i] = (byte)val;
        }
        Log.d("TAG", "hexToByteArray: "+ Arrays.toString(ans));
        return ans;
    }

    public static byte[] hexStringToByteArray(){
        String hexString = "AA11FE010010";         // device off
//        String hexString = "AA62FE010061";      // volume up

        String[] hexArray = hexString.split("");
        byte[] bytes = new byte[hexArray.length];for (int i = 0; i < hexArray.length; i++) {
            String hex = hexArray[i];
            bytes[i] = Integer.valueOf(hex, 16).byteValue();
        }
        Log.d("TAG", "hexToByteArray: "+ Arrays.toString(bytes));
        return bytes;
    }

}
