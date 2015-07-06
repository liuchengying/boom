package boom.boom.gerenzhuye;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import boom.boom.R;
import boom.boom.api.HttpIO;
import boom.boom.api.Static;
import boom.boom.api.Utils;

import boom.boom.liuyanbanpinglun.Liuyanban_pinglun;
import boom.boom.myview.CircleImageView;
import boom.boom.myview.XListView;

/**
 * Created by 刘成英 on 2015/3/12.
 */
public class Liuyanban_fragment extends Fragment implements XListView.IXListViewListener
{
    private XListView lv;
    private Handler mHandler;
    private final static String DATE_FORMAT_STR = "yyyy-MM-dd HH:mm";
    private SimpleAdapter mSimpleAdapter;
    private String guestID;
    private CircleImageView gerenzhuye_touxing;
    private Button button;
    private Button confirmButton;
    private Button cancleButton;
    private PopupWindow popupWindow;
    private View popupWindowView;
    ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,     Object>>();//*在数组中存放数据*//*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.tianjiahaoyou2, container, false);
        lv= (XListView) v.findViewById(R.id.listView5);
        guestID = getFragmentManager().findFragmentByTag("179521").getArguments().getString("guestID");

        lv.setPullLoadEnable(true);
        mHandler = new Handler();
        this.onSyncDataFromServer();
        lv.setPullLoadEnable(true);
        lv.setPullRefreshEnable(true);
        lv.setXListViewListener(this);
        lv.setAdapter(mSimpleAdapter);
        lv.mContext = getActivity();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent localIntent = new Intent(getActivity(), Liuyanban_pinglun.class);
                localIntent.putExtra("ID",(String)listItem.get(position-1).get("ID"));
                localIntent.putExtra("nickname",(String)listItem.get(position-1).get("title"));
                localIntent.putExtra("text",(String)listItem.get(position-1).get("text"));
                localIntent.putExtra("time",(String)listItem.get(position-1).get("time"));
                startActivity(localIntent);

            }
        });

        if(guestID == Static.identifyDigit){
        popupWindowView = inflater.inflate(R.layout.shanchuliuyan_item, null);


        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow = new PopupWindow(popupWindowView);
                popupWindow.setWidth(LinearLayout.LayoutParams.FILL_PARENT);
                popupWindow.setHeight(LinearLayout.LayoutParams.FILL_PARENT);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);

                //设置PopupWindow的弹出和消失效果
                popupWindow.setAnimationStyle(R.style.popupAnimation);


                cancleButton = (Button) popupWindowView.findViewById(R.id.liuyanban_cencle);
                confirmButton = (Button) popupWindowView.findViewById(R.id.shanchuliuyan);
                popupWindow.showAtLocation(confirmButton, Gravity.CENTER, 0, 0);
                confirmButton.setOnClickListener(Itemclick);

                cancleButton.setOnClickListener(Itemclick);
                return true;
            }

        });}
        return v;
    }
    private void onLoad() {
        lv.stopRefresh();
        lv.stopLoadMore();
        lv.setRefreshTime(new SimpleDateFormat(DATE_FORMAT_STR, Locale.CHINA).format(new Date()));
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onSyncDataFromServer();
                onLoad();
            }
        }, 2000);

    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                onLoad();
            }
        }, 2000);

    }
    private View.OnClickListener Itemclick = new View.OnClickListener() {
        @Override
        public void onClick( View v) {
            if(popupWindow.isShowing()){
                popupWindow.dismiss();
            }
            switch (v.getId()) {
                case R.id.shanchuliuyan:

                    break;
                case R.id.liuyanban_cencle:

                    break;
                default:
                    break;
            }
        }



    };


    public void onSyncDataFromServer(){
        String challenge_name = null, challenge_nickname = null;

        //http://172.24.10.118/api/comment.php?action=queryFriends&guest_id=10000
        Utils.GetBuilder get = new Utils.GetBuilder(Utils.serveraddr + "/api/comment.php");
        get.addItem("action", "queryFriends");
        get.addItem("type","1");
        get.addItem("guest_id",guestID);
        final HttpIO io = new HttpIO(get.toString());
        new Thread(new Runnable() {
            @Override
            public void run() {
                io.SessionID=Static.session_id;
                io.GETToHTTPServer();
            }
        }).start();
        while(io.getResultData() == null);
        int round = 0;
        JSONObject obj=null;
        try {
            if(io.getResultData()!=null) {
                obj= new JSONObject(io.getResultData());
                if(obj.getString("state").equals("SUCCESS")) {
                    round = obj.getInt("limit");
                    JSONObject tmp;
                    int i = 0;
                    while ((tmp = Utils.GetSubJSONObject(obj, "line" + i)) != null) {
                        String title = null, text = null, time = null, ID = null;
                        int like = 0, comment = 0;
                        if (obj != null) try {
                            ID = tmp.getString("ID");
                            title = tmp.getString("nickname");
                            text = tmp.getString("text_value");
                            like = tmp.getInt("heart_like");
                            comment = tmp.getInt("refer_sum");
                            time = tmp.getString("assign_date");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("title", title);//加入图片            map.put("ItemTitle", "第"+i+"行");
                        map.put("text", text);
                        map.put("like", like);
                        map.put("comment", comment);
                        map.put("time", time);
                        map.put("ID",ID);
                        listItem.add(map);
                        i++;
                    }
                }else {
                    if(obj.getString("reason").equals("USER_NOT_PERMITTED")){
                        Toast.makeText(getActivity(),"您没有权限访问",Toast.LENGTH_SHORT).show();
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
//        JSONObject obj_new = Challenge.getChallengeByIdentify()
            mSimpleAdapter = new SimpleAdapter(getActivity(), listItem,//需要绑定的数据
                    R.layout.liuyanban_item,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                    new String[]{
                            "title", "text", "like", "comment", "time"},
                    new int[]{R.id.nickname, R.id.lyb_text, R.id.like, R.id.comment, R.id.date}
            );
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}