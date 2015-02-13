package boom.boom.api;

import android.app.Application;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 1eekai on 2015/1/16.
 */
public class User extends Application {
    private static final String USER_LOGIN_URL = "/api/userlogin.jsp";

    private String username;
    private String password;
    private boolean ifUserLoggedIn;
    private String ServerErr;
    public JSONObject userData;
    public static final int SERVER_TO_CLIENT = 33311312;
    public static final int CLIENT_TO_SERVER = 31455144;
    public void onCreate(){
        super.onCreate();
        ServerErr = null;

    }
    public void loginUser(String user, String pass) {
        this.username = user;
        this.password = pass;
        try {
            AttemptLogin();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SyncUserData(SERVER_TO_CLIENT);
    }

    public String getServerErr(){
        HttpIO io = new HttpIO(Utils.serveraddr + USER_LOGIN_URL + "?action=lasterror");
        io.GETToHTTPServer();
        try {
            JSONObject obj = new JSONObject(io.getResultData());
            this.ServerErr = obj.getString("errmsg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this.ServerErr;
    }

    public boolean SyncUserData(int ToWhere){
        if (ToWhere == this.SERVER_TO_CLIENT){
            HttpIO io = new HttpIO(new Utils.GetBuilder(USER_LOGIN_URL, Utils.GetBuilder.Item("action", "getuaerdata")).toString());

        }
        return true;
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
        String httpResult = io.getResultData();
        JSONObject obj = new JSONObject(httpResult);
        String status = obj.getString("status");
        if (status == "FAIL")   ifUserLoggedIn = false;
        if (status == "SUCCESS")    ifUserLoggedIn = true;
        return this.ifLoggedIn();
    }

 //   public String
}
