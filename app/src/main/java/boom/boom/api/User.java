package boom.boom.api;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

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
        try {
            AttemptLogin();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getServerErr() {
        HttpIO io = new HttpIO(Utils.serveraddr + USER_LOGIN_URL + "?action=lasterror");
        io.GETToHTTPServer();
        try {
            JSONObject obj = new JSONObject(io.getResultData());
            this.ServerErr = obj.getString("errmsg");
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
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
            String status = obj.getString("status");
            if (status == "FAIL") ifUserLoggedIn = false;
            if (status == "SUCCESS") ifUserLoggedIn = true;
        }
        else {
            ifUserLoggedIn = false;
        }
        return this.ifLoggedIn();
    }

    public String getSessionId(){
        return this.session_id;
    }

 //   public String
}
