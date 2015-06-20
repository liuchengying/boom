package boom.boom.api;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/5/19.
 */
public class CheckIn {
    public String ServerErr;
//http://172.24.10.118/api/msg.php?action=checkin
    private final String USER_LOGIN_URL = Utils.serveraddr + "/api/msg.php";

    public boolean Checkin(int returned[])
    {
        try {
            Utils.GetBuilder get = new Utils.GetBuilder(USER_LOGIN_URL);
            get.addItem("action", "checkin");

            String url_request = get.toString();

            final HttpIO io = new HttpIO(url_request);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    io.SetCustomSessionID(Static.session_id);
                    io.getJson();
                }
            }).start();
            while(io.getResultData() == null);

            if (io.LastError == 0) {
                String httpResult = io.getResultData();
                JSONObject obj = new JSONObject(httpResult);
                String status = obj.getString("state");
                if (status.equalsIgnoreCase("FAILED")) {
                    if(obj.getString("reason").equals("ALREADY_CHECKIN_TODAY"))
                    {
                        ServerErr = "今天已签到！";
                        returned[0]=obj.getInt("checkin_sum");
                        returned[1]=obj.getInt("least_for_coins");
                    }
                    else
                    {
                        ServerErr = "服务器内部错误！";
                    }
                    return false;
                }
                if (status.equalsIgnoreCase("SUCCESS"))
                {
                    returned[0]=obj.getInt("checkin_sum");
                    returned[1]=obj.getInt("least_for_coins");
                    return true;
                }

            } else {
                ServerErr = "网络超时！";
                return false;
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            Log.e("Login_deamon", "Caught a JSON decode error. Application might be crash soon.");
        }
        return false;
    }



}
