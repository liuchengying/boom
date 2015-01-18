package boom.boom.api;

import android.app.Application;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 1eekai on 2015/1/16.
 */
public class User extends Application {
    private String username;
    private String password;
    private boolean ifUserLoggedIn;
    private String ServerErr;
    public Map<String, String> userData;
    public void onCreate(){
        super.onCreate();
        userData = new HashMap<String, String>();
        ServerErr = null;

    }
    public void loginUser(String user, String pass){
        this.username = user;
        this.password = pass;
        AttemptLogin();
        SyncUserData(true);
    }

    public String getServerErr(){
        return this.ServerErr;
    }

    public boolean SyncUserData(boolean ToWhere){
        return true;
    }

    public boolean ifLoggedIn(){
        return this.ifUserLoggedIn;
    }

    public boolean AttemptLogin(){
        ifUserLoggedIn = true;
        return this.ifLoggedIn();
    }

 //   public String
}
