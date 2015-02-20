package boom.boom.api;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by laoli on 2015/2/19.
 */
public class Msg {
    public static String MSG_API_URL = "/api/msg.php";
    private String RawDataStore;
    private JSONObject json_data;
    private static int counter = 0;
    public Msg(){
        HttpIO io = new HttpIO(Utils.serveraddr + MSG_API_URL + "?action=query");
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
}
