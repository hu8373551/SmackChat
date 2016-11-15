package com.sky.skyweight;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Andy on 15/7/13.
 * SharePrefence保存操作
 */
public class SharePrefenceUtils {

    public static void write(Context context, String fileName, String k, int v) {
        SharedPreferences preference = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        Editor editor = preference.edit();
        editor.putInt(k, v);
        editor.commit();
    }

    public static void write(Context context, String fileName, String k, boolean v) {
        SharedPreferences preference = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        Editor editor = preference.edit();
        editor.putBoolean(k, v);
        editor.commit();
    }

    public static void write(Context context, String fileName, String k, String v) {
        SharedPreferences preference = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        Editor editor = preference.edit();
        editor.putString(k, v);
        editor.commit();
    }

    public static void write(Context context, String fileName, String k, float v) {
        SharedPreferences preference = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        Editor editor = preference.edit();
        editor.putFloat(k, v);
        editor.commit();
    }

    public static void write(Context context, String fileName, String k, long v) {
        SharedPreferences preference = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        Editor editor = preference.edit();
        editor.putLong(k, v);
        editor.commit();
    }

    public static int readInt(Context context, String fileName, String k) {
        SharedPreferences preference = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return preference.getInt(k, 0);
    }

    public static int readInt(Context context, String fileName, String k, int defv) {
        SharedPreferences preference = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return preference.getInt(k, defv);
    }

    public static long readLong(Context context, String fileName, String k) {
        SharedPreferences preference = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return preference.getLong(k, 0);
    }

    public static long readLong(Context context, String fileName, String k, long defv) {
        SharedPreferences preference = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return preference.getLong(k, defv);
    }

    public static float readFloat(Context context, String fileName, String k) {
        SharedPreferences preference = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return preference.getFloat(k, 0);
    }

    public static float readFloat(Context context, String fileName, String k, float defv) {
        SharedPreferences preference = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return preference.getFloat(k, defv);
    }

    public static boolean readBoolean(Context context, String fileName, String k) {
        SharedPreferences preference = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return preference.getBoolean(k, false);
    }

    public static boolean readBoolean(Context context, String fileName, String k, boolean defBool) {
        SharedPreferences preference = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return preference.getBoolean(k, defBool);
    }

    public static String readString(Context context, String fileName, String k) {
        SharedPreferences preference = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return preference.getString(k, null);
    }

    public static String readString(Context context, String fileName, String k, String defV) {
        SharedPreferences preference = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return preference.getString(k, defV);
    }

    public static void remove(Context context, String fileName, String k) {
        SharedPreferences preference = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        Editor editor = preference.edit();
        editor.remove(k);
        editor.commit();
    }

    public static void clean(Context cxt, String fileName) {
        SharedPreferences preference = cxt.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        Editor editor = preference.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * desc:将数组转为16进制
     *
     * @param bArray
     * @return modified:
     */
    public static String bytesToHexString(byte[] bArray) {
        if (bArray == null) {
            return null;
        }
        if (bArray.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * desc:将16进制的数据转为数组
     * <p>创建人：聂旭阳 , 2014-5-25 上午11:08:33</p>
     *
     * @param data
     * @return modified:
     */
    public static byte[] StringToBytes(String data) {
        if (data == null) {
            return null;
        }
        String hexString = data.toUpperCase().trim();
        if (hexString.length() % 2 != 0) {
            return null;
        }
        byte[] retData = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i++) {
            int int_ch;  // 两位16进制数转化后的10进制数
            char hex_char1 = hexString.charAt(i); ////两位16进制数中的第一位(高位*16)
            int int_ch3;
            if (hex_char1 >= '0' && hex_char1 <= '9')
                int_ch3 = (hex_char1 - 48) * 16;   //// 0 的Ascll - 48
            else if (hex_char1 >= 'A' && hex_char1 <= 'F')
                int_ch3 = (hex_char1 - 55) * 16; //// A 的Ascll - 65
            else
                return null;
            i++;
            char hex_char2 = hexString.charAt(i); ///两位16进制数中的第二位(低位)
            int int_ch4;
            if (hex_char2 >= '0' && hex_char2 <= '9')
                int_ch4 = (hex_char2 - 48); //// 0 的Ascll - 48
            else if (hex_char2 >= 'A' && hex_char2 <= 'F')
                int_ch4 = hex_char2 - 55; //// A 的Ascll - 65
            else
                return null;
            int_ch = int_ch3 + int_ch4;
            retData[i / 2] = (byte) int_ch;//将转化后的数放入Byte里
        }
        return retData;
    }


    public static byte[] getBytesFromFile(File f) {
        if (f == null) {
            return null;
        }
        try {
            FileInputStream stream = new FileInputStream(f);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = stream.read(b)) != -1)
                out.write(b, 0, n);
            stream.close();
            out.close();
            return out.toByteArray();
        } catch (IOException e){
        }
        return null;
    }

}
