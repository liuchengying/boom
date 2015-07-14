package boom.boom.api;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import boom.boom.R;

/**
 * Created by Administrator on 2015/5/20.
 */
public class FriendList {
    public String ServerErr;
    private Handler LastHandler;
    String httpResult;
    ///api/newfriend.php?action=query
    private final String USER_LOGIN_URL = Utils.serveraddr + "api/newfriend.php";
    private Handler myHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Message m = new Message();
            m.what = msg.what;
            if(msg.what == 1){
                httpResult = msg.getData().getString("data");
            }
            LastHandler.sendMessage(m);
            return true;
        }
    });
    public FriendList(Context context,Handler handler){
        LastHandler = handler;
        Utils.GetBuilder get = new Utils.GetBuilder(USER_LOGIN_URL);
        get.addItem("action", "query");
        HttpIO.GetHttpEX(context,myHandler,get.toString());
    }
    public void GetFriendList(ArrayList<HashMap<String, Object>> listItem) {
        //ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();//*在数组中存放数据*//*
        listItem.clear();
        try {

                JSONObject obj = new JSONObject(httpResult);
                JSONObject response = Utils.GetSubJSONObject(obj,"response");

                String status = response.getString("state");
                if (status.equalsIgnoreCase("FAILED"))
                {
                    ServerErr = "获取好友列表失败!";
                    return;
                }else if (status.equalsIgnoreCase("SUCCESS")) {
                    int limit = response.getInt("limit");
                    for(int i=0;i<limit;i++)
                    {
                        JSONObject tmp = Utils.GetSubJSONObject(obj,""+i);
                        String nickname = tmp.getString("guest_nickname");
                        String avatar = tmp.getString("guest_avatar");
                        String guestID = tmp.getString("guest_id");
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        //map.put("avatar",avatar);
                        map.put("avatar", avatar);
                        map.put("nickname",nickname);
                        map.put("guestID",guestID);
                        map.put("position",i);
                        listItem.add(map);
                    }
                    return ;
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ;
    }

}
