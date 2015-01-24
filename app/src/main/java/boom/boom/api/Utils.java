package boom.boom.api;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 1eekai on 2015/1/17.
 */
public class Utils {
    public static final String serveraddr = "http://myhome2.leekai1995.com:82/";

    public static String StrToMD5(String string) {
        /*
         * StrToMD5(String string) ，把字符串转化为MD5。
         * 用法：
         * String a = "abcdef";
         * String md5_of_a = Utils.StrToMD5(a);
         *
         * 此时 md5_of_a 中存储了a的md5散列。
         */
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public static JSONObject GetSubJSONObject(JSONObject grandObj, String key)
            throws JSONException {
        /*
         * GetSubJSONObject(), 得到JSONObject中的子JSONObject。
         */
        return grandObj.getJSONObject(key);
    }

}
