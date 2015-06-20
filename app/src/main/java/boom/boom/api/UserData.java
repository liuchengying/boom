package boom.boom.api;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 1eekai on 2015/2/17.
 */
public class UserData {
    public static String USER_DATA_URL = Utils.serveraddr + "/api/userdata.php";
    private JSONObject userdata;
    private String userdata_str = null;
    public UserData(final String SessionID){
        final HttpIO io = new HttpIO(USER_DATA_URL + "?action=query");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                io.SetCustomSessionID(SessionID);
                io.GETToHTTPServer();
                userdata_str = io.getResultData();
            }
        });
        thread.start();
        while (userdata_str == null);
        try {

            Log.e("Uerdata", userdata_str);

            userdata = new JSONObject(userdata_str);
        } catch (JSONException e) {
            userdata_str = "FUCK";
            e.printStackTrace();
        }
        if (userdata_str.equals("FUCK")){
            Log.e("Status", "完蛋了，扑街了。");
            while (true);
        }
    }
    public String toString(){
        return this.userdata_str;
    }
    public JSONObject toJSONObject(){
        return this.userdata;
    }
    public String QueryData(String item) {
        try {
            return this.userdata.getString(item);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e)
        {
            e.printStackTrace();
            Log.e("UserData",item);
            Log.e("Error",userdata.toString());
        }
        return null;
    }
}
