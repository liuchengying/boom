package boom.boom.api;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lcy on 2015/5/20.
 */
public class Suggest {
    public String ServerErr;

    private final String USER_LOGIN_URL = Utils.serveraddr + "/api/suggest.php";
    public Suggest(){

                     }

   public boolean SuggestChange(String suggest){
       try {
           Utils.GetBuilder get = new Utils.GetBuilder(USER_LOGIN_URL);
           get.addItem("text", suggest);
           String url_request = get.toString();
           url_request=url_request.replace("\n","\\n");
           HttpIO io = new HttpIO(url_request);
           io.SetCustomSessionID(Static.session_id);
           io.getJson();
           if (io.LastError == 0) {
               String httpResult = io.getResultData();
               JSONObject obj = new JSONObject(httpResult);
               String status = obj.getString("state");
               if (status.equalsIgnoreCase("FAILED")) {
                   if(obj.getString("reason").equals("TAKE_SUGGEST_TO_FREQUENTLY")){
                        ServerErr = "您提交的太频繁了哦！";
                   }else
                        ServerErr = "提交失败";
                   return false;
               }
               if (status.equalsIgnoreCase("SUCCESS")){
                   return true;}
           } else {
               ServerErr = "登陆超时！";
               return false;
           }
       }
       catch (Exception e) {
           e.printStackTrace();
           Log.e("Login_deamon", "Caught a JSON decode error. Application might be crash soon.");
       }

       return false;
   }
}
