package boom.boom.api;

import org.apache.http.NameValuePair;

import java.util.HashMap;
import java.util.List;

/**
 * Created by 1eekai on 2015/1/17.
 */

/*
 *  用途：用户注册。
 *  使用示例（伪代码）：
 *
 *
 *  LoginUser user = new LoginUser("likai", "123456");  // 新建对象，传参，用户名为likai，密码为123456
 *  List<NameValuePair> userd = new List<NameValuePair>();
 */

public class LoginUser {
    private String user;
    private String pass;
    private boolean reg_ok;
    private List<NameValuePair> userdata;

    public LoginUser(String _user, String _pass){
        this.user = _user;
        this.pass = _pass;
  //      this.gotUserDataArray(userd);     注册的时候暂时不要求用户输入个人信息。
        AttemptToRegisted();
    }

    public void gotUserDataArray(List<NameValuePair> userd){
        userdata = userd;
    }

    public boolean AttemptToRegisted(){
        /*
         *  尝试注册。
         */
        return reg_ok;
    }
}
