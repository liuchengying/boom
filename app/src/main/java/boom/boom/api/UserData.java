package boom.boom.api;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 1eekai on 2015/2/17.
 */
public class UserData {
    public static String USER_DATA_URL = Utils.serveraddr + "/api/userdata.php";
    private JSONObject userdata;
    private String userdata_str;
    public UserData(String SessionID){
        HttpIO io = new HttpIO(USER_DATA_URL + "?action=query");
        io.SetCustomSessionID(SessionID);
        io.GETToHTTPServer();
        try {
            userdata_str = io.getResultData();
            JSONObject json = new JSONObject(this.userdata_str);
            this.userdata = json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String toString(){
        return this.userdata_str;
    }
    public JSONObject toJSONObject(){
        return this.userdata;
    }
    public String QueryData(String item) {
        try {
            return this.userdata.getString(item);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
