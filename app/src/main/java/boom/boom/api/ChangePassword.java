package boom.boom.api;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/4/23.
 */
public class ChangePassword {
    public String ServerErr;

    private final String USER_LOGIN_URL = Utils.serveraddr + "/api/userRegister.php";

    public ChangePassword()
    {

    }

    public boolean Change(Context context,String oldPassword, String newPassword)
    {
        try {
            Utils.GetBuilder get = new Utils.GetBuilder(USER_LOGIN_URL);
            get.addItem("action", "alter_password");
            get.addItem("old_pass", Utils.StrToMD5(oldPassword));
            get.addItem("new_pass", Utils.StrToMD5(newPassword));
            String url_request = get.toString();

            final HttpIO io = new HttpIO(url_request);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    io.SetCustomSessionID(Static.session_id);
                    io.GETToHTTPServer();
                }
            }).start();

        /*
        List<NameValuePair> post = new ArrayList<NameValuePair>();
        post.add(new BasicNameValuePair("name", this.username));
        post.add(new BasicNameValuePair("passhash", Utils.StrToMD5(this.password)));
        io.POSTToHTTPServer(post);
        */
            while(io.getResultData() == null);
            if (io.LastError == 0) {
                String httpResult = io.getResultData();
                JSONObject obj = new JSONObject(httpResult);
                String status = obj.getString("state");
                if (status.equalsIgnoreCase("FAILED")) {
                    if(obj.getString("reason").equals("AUTH_FAILED_PASSWORD_INVALID"))
                    {
                        ServerErr = "旧密码输入错误！";
                    }
                    else
                    {
                        ServerErr = "暂时无法修改！";
                    }
                    return false;
                }
                if (status.equalsIgnoreCase("SUCCESS"))
                    return true;
            } else {
                ServerErr = "登陆超时！";
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

