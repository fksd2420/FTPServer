package com.example.mylibrary;

import android.content.Context;
import android.util.DisplayMetrics;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Common {

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static boolean isValidIPv4Address(String ip) {
        return ip.matches("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
    }
    public static boolean isValidIPv6Address(String ip) {
        return ip.matches("/(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))/g");
    }
    public static boolean isValidNonIPHostname(String hostname) {
        return hostname.matches("^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9])$");
    }
    public static InetAddress parseINetAddress(String addr) throws UnknownHostException {
        return InetAddress.getByName(addr);
    }
    public static InetAddress tryParseINetAddress(String addr) {
        try {
            var addr_ = InetAddress.getByName(addr);
            return addr_;
        } catch (Exception e) {
            return null;
        }
    }
    public static Inet4Address parseIPv4Address(String ip) throws Exception {
        var addr = InetAddress.getByName(ip);
        return (Inet4Address) addr;
    }
    public static Inet6Address parseIPv6Address(String ip) throws Exception {
        var addr = InetAddress.getByName(ip);
        return (Inet6Address) addr;
    }
    public static Inet4Address tryParseIPv4Address(String ip) {

        try {
            return parseIPv4Address(ip);
        } catch (Exception e) {
            return null;
        }
    }
    public static Inet6Address tryParseIPv6Address(String ip) {

        try {
            return parseIPv6Address(ip);
        } catch (Exception e) {
            return null;
        }
    }
    public static byte[] parseMACAddress(String macStr) throws IllegalArgumentException {
        byte[] bytes = new byte[6];
        String[] hex = macStr.split("(\\:|\\-)");
        if (hex.length != 6) {
            throw new IllegalArgumentException("Invalid MAC address.");
        }
        try {
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
            }
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex digit in MAC address.");
        }
        return bytes;
    }
    public static byte[] tryParseMACAddress(String mac) {
        try {
            return parseMACAddress(mac);
        } catch (Exception e) {
            return null;
        }
    }

    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertPixelsToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

}
