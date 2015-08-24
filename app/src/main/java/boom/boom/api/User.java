package boom.boom.api;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;



/**
 * Created by 1eekai on 2015/1/16.
 */
public class User {
    private static final String USER_LOGIN_URL = Utils.serveraddr + "/api/userlogin.php";
    public static String session_id;
    private String username;
    private String password;
    public boolean ifUserLoggedIn;
    private String ServerErr;
    private String result = null;
    private HttpIO io;
    private Handler LastHandler;
    public User(String user, String pass) {
        this.username = user;
        this.password = pass;
        ServerErr = null;
        session_id = null;

    }
    Handler loginHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Message m = new Message();
            if(msg.what == 1){
                m.what = 1;
                result = msg.getData().getString("data");
                JSONObject obj = null;
                try {
                        Log.e("Result", "Result ==> " + result);
                        obj = new JSONObject(result);
                        if (obj.getString("state").equals("SUCCESS")) {
                            ifUserLoggedIn = true;
                        } else {
                            ifUserLoggedIn = false;
                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                m.what = 0;
                ifUserLoggedIn = false;
            }

            LastHandler.sendMessage(m);
            return true;
        }
    });
    public User(final String session,Context context,Handler handler){

                session_id = session;
                Utils.GetBuilder get = new Utils.GetBuilder(Utils.serveraddr + Utils.userdata_api);
                get.addItem("action", "getState");
                Log.e("URI", get.toString());
                HttpIO.GetHttpEX(context, loginHandler, get.toString());
                LastHandler = handler;
    }

    public String getServerErr() {
       return this.ServerErr;
    }

    public boolean ifLoggedIn(){
        return this.ifUserLoggedIn;
    }

    public boolean AttemptLogin()
            throws JSONException {
        Utils.GetBuilder get = new Utils.GetBuilder(USER_LOGIN_URL);
        get.addItem("action", "login");
        get.addItem("user",this.username);
        get.addItem("passhash",Utils.StrToMD5(this.password));
        get.addItem("user_id",Static.user_id);
        get.addItem("channel_id",Static.channel_id);
        Log.e("User",get.toString());
        String url_request = get.toString();
        final HttpIO io = new HttpIO(url_request);
        new Thread(new Runnable() {
            @Override
            public void run() {
                io.GETToHTTPServer();
            }
        }).start();
        while(io.getResultData() == null);
        if(io.LastError==0) {
            String httpResult = io.getResultData();
            this.session_id = io.GetSessionID();
            JSONObject obj = new JSONObject(httpResult);
            String status = obj.getString("state");
            if (status.equalsIgnoreCase("FAILED"))
            {   ifUserLoggedIn = false;
                ServerErr="用户名或密码错误！";
            }
            if (status.equalsIgnoreCase("SUCCESS")) {
                ifUserLoggedIn = true;
            }
        }
        else {
            ifUserLoggedIn = false;
            ServerErr="登陆超时！";
        }
        return this.ifLoggedIn();
    }

    public String getSessionId(){
        return this.session_id;
    }

 //   public String
}
