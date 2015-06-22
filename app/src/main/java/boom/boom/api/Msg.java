package boom.boom.api;



import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import boom.boom.R;

/**
 * Created by laoli on 2015/2/19.
 */
public class Msg {
    public static String MSG_API_URL = "/api/msgcenter.php";
    public int DATAERROR=65412;
    public int LastError = 0;
    private String RawDataStore;
    private JSONObject json_data;
    private static int counter = 0;
    android.os.Handler myMessageHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try{

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };
    public Msg(){

                    final HttpIO io = new HttpIO(Utils.serveraddr + MSG_API_URL );
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            io.SetCustomSessionID(Static.session_id);
                            io.GETToHTTPServer();
                        }
                    }).start();
                    while(io.getResultData() == null);
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





    public ArrayList<HashMap<String, Object>> GetList(boolean reset,Context context){
        if (reset == true)  counter = 0;
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        try {
            JSONObject response = Utils.GetSubJSONObject(json_data, "response");
            if(response.getString("state").equals("SUCCESS")){
                int round = response.getInt("limit");
                for(int i=0;i<round;i++){
                    HashMap<String,Object>  map = new HashMap<String,Object>();
                    Resources res = context.getResources();
                    JSONObject tmp = Utils.GetSubJSONObject(json_data,"line"+i);
                    int type = tmp.getInt("type");
                    String date = tmp.getString("date");
                    Bitmap icon, smallicon;
                    map.put("type",type);
                    map.put("date",date);
                    String title = null;
                    String text = null;
                    JSONObject data = Utils.GetSubJSONObject(tmp,"data");
                    switch (type) {
                        case 1://1.挑战成功或者失败

                            if(data.getString("pass").equals("CHALLENGE_SUCCESS"))
                            {
                                title = "挑战成功！";
                                text = "+"+data.getString("earn_coins")+"★ "+data.getString("challenge_frontname")+"完成 用时"+data.getString("elapsed_time")+"s";
                            }
                            else {
                                title = "挑战失败！";
                                text = "您的"+data.getString("challenge_frontname")+"失败！";
                            }
                            map.put("title",title);
                            map.put("content",text);
                            icon = BitmapFactory.decodeResource(res, R.drawable.android_217);
                            map.put("icon",icon);
                            break;
                        case 2://2.自拟挑战审核状态
                            if(data.getString("state").equals("SUCCESS_VERIFY")){
                                title = "发布的挑战已经通过审核！";
                                text = "挑战 “"+data.getString("frontname")+"” 已经通过审核!";
                                smallicon = BitmapFactory.decodeResource(res,R.drawable.android_213);
                            }else {
                                title = "发布的挑战未通过审核！";
                                text = "挑战 “"+data.getString("frontname")+"” 未通过审核!";
                                smallicon = BitmapFactory.decodeResource(res,R.drawable.android_215);
                            }
                            map.put("title",title);
                            map.put("content",text);
                            icon = BitmapFactory.decodeResource(res,R.drawable.android_217);
                            map.put("icon",icon);
                            map.put("smallicon",smallicon);
                            map.put("identifyDigit",data.getString("identifyDigit"));
                            break;
                        case 3://3.往期挑战，官方的审核状态
                            if(data.getString("pass").equals("CHALLENGE_SUCCESS"))
                            {
                                map.put("title", "发布的往期挑战已通过审核!");
                                map.put("content","挑战“"+data.getString("challenge_frontname")+"”已通过审核！");
                                smallicon = BitmapFactory.decodeResource(res,R.drawable.android_213);
                                map.put("pass",true);
                                map.put("challenge_id",data.getString("challenge_id"));
                            }else {
                                map.put("title","发布的往期挑战未通过审核!");
                                map.put("content","挑战“"+data.getString("challenge_frontname")+"”未通过审核！");
                                smallicon = BitmapFactory.decodeResource(res,R.drawable.android_215);
                                map.put("pass",false);
                            }
                            map.put("smallicon",smallicon);
                            icon = BitmapFactory.decodeResource(res,R.drawable.android_217);
                            map.put("icon",icon);
                            break;
                        case 4://4.官方推送的消息
                            map.put("title",data.getString("title"));
                            map.put("content",data.getString("text"));
                            icon = BitmapFactory.decodeResource(res,R.drawable.android_212);
                            map.put("icon",icon);
                            break;
                        case 5://5.用户好友添加
                            map.put("title",data.getString("nickname")+"添加您为好友!");
                            map.put("content","点击查看TA的主页");
                            icon = BitmapFactory.decodeResource(res,R.drawable.android_214);
                            map.put("host_id",data.getString("host_id"));
                            map.put("avatar",data.getString("avatar"));
                            map.put("icon",icon);
                            break;
                        case 6://6.用户的自拟挑战别人挑战成功与否的消息
                            map.put("title","你屌爆了！");
                            map.put("content","XXX 刚刚挑战您失败");
                            icon = BitmapFactory.decodeResource(res,R.drawable.android_218);
                            map.put("icon",icon);
                            break;
                        case 7://7.评论回复
                            map.put("title","收到评论回复！");
                            map.put("content",data.getString("nickname")+":"+data.getString("text_value"));
                            icon = BitmapFactory.decodeResource(res,R.drawable.android_216);
                            map.put("icon",icon);
                            break;
                        case 8://点好友
                            map.put("title","好友"+data.getString("host_nickname")+"对你发起挑战！");
                            map.put("content","挑战成功 +2★  挑战失败 -2★");
                            icon = BitmapFactory.decodeResource(res,R.drawable.android_209);
                            smallicon = BitmapFactory.decodeResource(res,R.drawable.android_208);
                            map.put("icon",icon);
                            map.put("cl_id",data.getString("cl_id"));
                            map.put("smallicon",smallicon);
                            break;
                        case 9://好友同意或拒绝
                            boolean passed = false;
                            if(data.getString("pass").equals("USER_AGREED")){
                                passed = true;
                            }else {
                                passed = false;
                            }
                            map.put("title",data.getString("nickname") + (passed?"同意":"拒绝") + "添加您为好友!");
                            map.put("content",data.getString("nickname") + "已经" + (passed?"同意":"拒绝") + "了您添加好友的请求。");
                            smallicon = BitmapFactory.decodeResource(res,(passed)?R.drawable.android_213:R.drawable.android_215);
                            map.put("smallicon",smallicon);
                            icon = BitmapFactory.decodeResource(res,R.drawable.android_209);
                            map.put("icon",icon);
                            break;
                        case 10://
                            map.put("title","type10");
                            map.put("content","type10");
                            icon = BitmapFactory.decodeResource(res,R.drawable.android_209);
                            map.put("icon",icon);
                            break;
                    }

                    list.add(map);
                }
                return list;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
