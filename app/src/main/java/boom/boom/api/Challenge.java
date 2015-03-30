package boom.boom.api;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 1eekai on 2015/2/22.
 */
public class Challenge {
    public static final String challenge_api = "/api/getChallenge.php";
    public static final String take_cl_api = "/api/takeChallenge.php";
    private String RawDataStore;
    private JSONObject json_data;
    private int counter = 0;
    public Challenge(){
        HttpIO io = new HttpIO(Utils.serveraddr + challenge_api + "?action=get_short_intro");
        io.SetCustomSessionID(Static.session_id);
        io.GETToHTTPServer();
        RawDataStore = io.getResultData();
        try {
            json_data = new JSONObject(RawDataStore);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public Map<String, Object> GetSimpleMap(boolean reset){
        if (reset == true)  counter = 0;
        try {
            JSONObject tmp = Utils.GetSubJSONObject(json_data, "" + counter + "");
            if (tmp.toString() == "") {

                return null;
            } else if (tmp == null) {
                return null;
            }
            Map<String,Object>  map = new HashMap<String,Object>();
            map.put("label", tmp.getString("title"));
            map.put("text", tmp.getString("text"));
            return map;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Map<String, Object>  GetSimpleMap(){
        return this.GetSimpleMap(false);
    }

    public static String getChallengeByNumber(int number){
        HttpIO io = new HttpIO(Utils.serveraddr + challenge_api + "?action=fetchByNumber&id=" + number);
        io.SetCustomSessionID(Static.session_id);
        io.GETToHTTPServer();
        return io.getResultData();
    }
    public static boolean subMitChallenge(String challenge, String Token){
        HttpIO io = new HttpIO(Utils.serveraddr + take_cl_api + Utils.GetBuilder.Item("dvtoken", Token) + Utils.GetBuilder.Item("challenge", challenge));
        io.SetCustomSessionID(Static.session_id);
        io.GETToHTTPServer();
        try {
            String result = new JSONObject(io.getResultData()).getString("state");
            if (result == "FAILED") return false;
            else    if (result == "SUCCESS")    return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

//    public static JSONObject getChall
}
