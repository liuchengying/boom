package boom.boom.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 1eekai on 2015/1/16.
 */
public class User {
    private String username;
    private String password;
    private boolean ifUserLoggedIn;
    private Map<String, String> userData;
    public User(String _username, String _password){
        this.username = _username;
        this.password = _password;
        AttemptLogin();
        SyncUserData();
    }

    public boolean SyncUserData(){
        userData = new HashMap<String, String>();
        return true;

    }

    public boolean ifLoggedIn(){
        return this.ifUserLoggedIn;
    }

    public boolean AttemptLogin(){

        return this.ifLoggedIn();
    }

 //   public String
}
