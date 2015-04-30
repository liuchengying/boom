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
}
