package boom.boom.api;

import android.graphics.Bitmap;

import com.loopj.android.http.PersistentCookieStore;

import org.json.JSONObject;

import boom.boom.myview.BadgeView;

/**
 * Created by laoli on 2015/2/19.
 */

public class Static {
    public static Bitmap avatarImage = null;
    public static String session_id = null;
    public static String username = null;
    public static String nickname = null;
    public static int logintime;
    public static int coins;
    public static String avatar = null;
    public static String uniqueSign = null;
    public static String reg_date = null;
    public static JSONObject object = null;
    public static String tempStr = null;
    public static android.os.Handler tiaozhan_handler;
    public static String identifyDigit = null;
    public static String province=null;
    public static String city= null;
    public static String area = null;
    public static float density;
    public static float width;
    public static float height;
    public static BadgeView badgeView;
    public static String user_id;
    public static String channel_id;
    public static PersistentCookieStore cookieStore;
}
