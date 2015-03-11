package boom.boom.api;



import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by laoli on 2015/2/19.
 */
public class Msg {
    public static String MSG_API_URL = "/api/msg.php";
    public int DATAERROR=65412;
    public int LastError = 0;
    private String RawDataStore;
    private JSONObject json_data;
    private static int counter = 0;
    public Msg(){
        HttpIO io = new HttpIO(Utils.serveraddr + MSG_API_URL + "?action=query");
        io.SetCustomSessionID(Static.session_id);
        io.GETToHTTPServer();
        if(io.LastError==0) {
            RawDataStore = io.getResultData();
            try {
                json_data = new JSONObject(RawDataStore);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            LastError=DATAERROR;
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
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public Map<String, Object>  GetSimpleMap(){
        return this.GetSimpleMap(false);
    }
}
