package boom.boom.api;



import android.hardware.Camera;
import android.os.Environment;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;

/**
 * Created by 1eekai on 2015/1/17.
 */
public class Utils {
    public static final String serveraddr = "http://172.24.10.118/";
    public static final String video = "/api/getvideo.php";
    public static final String put_file_api = "api/upStream.php";
    public static final String take_cl_api = "api/take_challenge.php";
    public static final String userdata_api = "/api/userdata.php";
    public static final String comment_api = "api/comment.php";
    public static final String newCl_api = "api/bring_challenge.php";

    public static class GetBuilder {
        private String str;
        public GetBuilder(String initial){
            str = "";
            str += initial + "?";
        }
        public GetBuilder(String initial, String item){
            str = "";
            str += initial + "?" + item;
        }
        public void addItem(String label, String value){
            str = str + label + "=" + value + "&";
        }
        public static String Item(String label, String value){
            return label + "=" + value + "&";
        }
        public String toString(){
            return str.substring(0,str.length()-1); //  删除末尾的 & 符号。
        }
    }

    public static class CameraUtils {
        public static Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
            final double ASPECT_TOLERANCE = 0.1;
            double targetRatio = (double) w / h;
            if (sizes == null) return null;
            Camera.Size optimalSize = null;
            double minDiff = Double.MAX_VALUE;
            int targetHeight = h;
            // Try to find an size match aspect ratio and size
            for (Camera.Size size : sizes) {
                double ratio = (double) size.width / size.height;
                if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
            // Cannot find the one match the aspect ratio, ignore the requirement
            if (optimalSize == null) {
                minDiff = Double.MAX_VALUE;
                for (Camera.Size size : sizes) {
                    if (Math.abs(size.height - targetHeight) < minDiff) {
                        optimalSize = size;
                        minDiff = Math.abs(size.height - targetHeight);
                    }
                }
            }
            return optimalSize;
        }
    }

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

    public static String GetXingzuo(int id){
        switch (id){
            case 0: return "白羊座";
            case 1: return "金牛座";
            case 2: return "双子座";
            case 3: return "巨蟹座";
            case 4: return "狮子座";
            case 5: return "处女座";
            case 6: return "天秤座";
            case 7: return "天蝎座";
            case 8: return "射手座";
            case 9:    return "摩羯座";
            case 10:    return "水瓶座";
            case 11:    return "双鱼座";
        }
        return "你是肉做的";
    }

    public static String GetXingzuo(String id){
        switch (Integer.valueOf(id)){
            case 0: return "白羊座";
            case 1: return "金牛座";
            case 2: return "双子座";
            case 3: return "巨蟹座";
            case 4: return "狮子座";
            case 5: return "处女座";
            case 6: return "天秤座";
            case 7: return "天蝎座";
            case 8: return "射手座";
            case 9:    return "摩羯座";
            case 10:    return "水瓶座";
            case 11:    return "双鱼座";
        }
        return "你是肉做的";
    }

    public static JSONObject GetSubJSONObject(JSONObject grandObj, String key)
            throws JSONException {
        /*
         * GetSubJSONObject(), 得到JSONObject中的子JSONObject。
         */
        return grandObj.getJSONObject(key);
    }

    public static String getVideoAPI(String Token){
        return Utils.video + "?token=" + Token;
    }

    public static String parsSessionViaGET(){
        return  "s_id=" + Static.session_id;
    }

    public static String getStoragePath(){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return Environment.getExternalStorageDirectory().toString();
        }else{
            return null;
        }
    }

    public static File getStorage(){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return Environment.getExternalStorageDirectory();
        }else{
            return null;
        }
    }

    public static String getRandomName(String tailname){
        int length = 20;
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString() + "." + tailname;
    }

    public static String getWorkPath(){
        return Utils.getStoragePath() + "/boom/";
    }

    public static String getVideoPath(){
        return Utils.getWorkPath() + "savedvideo/";
    }

    public static String getImagePath(){
        return Utils.getWorkPath() + "savedimg/";
    }

    public static String UTF8str(String str){
//            return new String(str.getBytes("UTF-8"), "UTF-8");
//            return new String(new String(str.getBytes("UTF-8"), "ISO-8859-1").getBytes("ISO-8859-1"), "UTF-8");
            return getUTF8StringFromGBKString(str);
    }
    private static String getUTF8StringFromGBKString(String gbkStr) {
        try {
            return new String(getUTF8BytesFromGBKString(gbkStr), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new InternalError();
        }
    }

    private static byte[] getUTF8BytesFromGBKString(String gbkStr) {
        int n = gbkStr.length();
        byte[] utfBytes = new byte[3 * n];
        int k = 0;
        for (int i = 0; i < n; i++) {
            int m = gbkStr.charAt(i);
            if (m < 128 && m >= 0) {
                utfBytes[k++] = (byte) m;
                continue;
            }
            utfBytes[k++] = (byte) (0xe0 | (m >> 12));
            utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
            utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
        }
        if (k < utfBytes.length) {
            byte[] tmp = new byte[k];
            System.arraycopy(utfBytes, 0, tmp, 0, k);
            return tmp;
        }
        return utfBytes;
    }

}
