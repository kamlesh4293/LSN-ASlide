package com.app.lsquared.usbserialconnection;

import java.io.PrintWriter;
import java.io.StringWriter;

public class UsbUtilities {

    public static String bytesToHex(byte[] data) {
        StringBuilder response = new StringBuilder();
        for (byte b : data) {
            response.append(String.format("%02X", b)).append(" ");
        }
        return response.toString().trim();
    }

    public static String bytesToString(byte[] data) {
        StringBuilder response = new StringBuilder();
        for (byte b : data) {
            response.append(String.format("%c", b)).append("");
        }
        return response.toString().trim();
    }

    public static String getStackTrace(Exception e) {
        StringWriter errors = new StringWriter();
        PrintWriter writer = new PrintWriter(errors);
        e.printStackTrace(writer);
        return errors.toString().replaceAll("\t", "").replaceAll("\n", " | ").replaceAll("\r", "").trim();
    }

    public static byte[] getDeviceOffByteArray(){
        byte[] values = new byte[6];
        values[0] = (byte) 170;
        values[1] = 17;
        values[2] = (byte) 254;
        values[3] = 1;
        values[4] = 0;
        values[5] = 16;
        return values;
    }

    public static byte[] getDeviceONByteArray(){
        byte[] values = new byte[6];
        values[0] = (byte) 170;
        values[1] = 17;
        values[2] = (byte) 254;
        values[3] = 1;
        values[4] = 1;
        values[5] = 17;
        return values;
    }

}
