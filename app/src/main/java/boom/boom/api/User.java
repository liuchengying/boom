package boom.boom.api;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 1eekai on 2015/1/16.
 */
public class User {
    private static final String USER_LOGIN_URL = Utils.serveraddr + "/api/userlogin.php";
    private static String session_id;
    private String username;
    private String password;
    private boolean ifUserLoggedIn;
    private String ServerErr;

    public User(String user, String pass) {
        this.username = user;
        this.password = pass;
        ServerErr = null;
        session_id = null;

    }

    public User(final String session){
        session_id = session;
        Utils.GetBuilder get = new Utils.GetBuilder(Utils.serveraddr + Utils.userdata_api);
        get.addItem("action", "getState");
        Log.e("URI", get.toString());
        HttpIO io = new HttpIO(get.toString());
        io.SetCustomSessionID(session_id);
        io.GETToHTTPServer();
        JSONObject obj = null;
        try {
                    if (io.LastError == 0) {
                        String result = io.getResultData();
                        Log.e("Result", "Result ==> " + result);
                        obj = new JSONObject(result);
                        if (obj.getString("state").equals("SUCCESS")) {
                            ifUserLoggedIn = true;
                        } else {
                            ifUserLoggedIn = false;
                        }
                    }else{
                        ifUserLoggedIn = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        String url_request = get.toString();
        HttpIO io = new HttpIO(url_request);
        /*
        List<NameValuePair> post = new ArrayList<NameValuePair>();
        post.add(new BasicNameValuePair("name", this.username));
        post.add(new BasicNameValuePair("passhash", Utils.StrToMD5(this.password)));
        io.POSTToHTTPServer(post);
        */
        io.GETToHTTPServer();
        if(io.LastError==0) {
            String httpResult = io.getResultData();
            this.session_id = io.GetSessionID();
            JSONObject obj = new JSONObject(httpResult);
            String status = obj.getString("state");
            if (status.equalsIgnoreCase("FAILED"))
            {   ifUserLoggedIn = false;
                ServerErr="用户名或密码错误！";
            }
            if (status.equalsIgnoreCase("SUCCESS")) ifUserLoggedIn = true;
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
