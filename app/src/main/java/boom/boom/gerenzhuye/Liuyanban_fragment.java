package boom.boom.gerenzhuye;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Message;
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
import android.widget.ImageView;
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
import boom.boom.api.AsyncLoadAvatar;
import boom.boom.api.HttpIO;
import boom.boom.api.Static;
import boom.boom.api.Utils;

import boom.boom.liuyanbanpinglun.Liuyanban_pinglun;
import boom.boom.liuyanhuifu.Liuyanhuifu_activity;
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
    public Handler myHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //Toast.makeText(getActivity(),"11",Toast.LENGTH_SHORT).show();
            try {
                mSimpleAdapter.notifyDataSetChanged();
            }catch (Exception e){
                e.printStackTrace();
            }
            return true;
        }
    });

    Handler asynHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == 1){
                listItem.clear();
                int round = 0;
                JSONObject obj=null;
                try {
                        obj= new JSONObject(msg.getData().getString("data"));
                        if(obj.getString("state").equals("SUCCESS")) {
                            round = obj.getInt("limit");
                            JSONObject tmp;
                            int i = 0;
                            while ((tmp = Utils.GetSubJSONObject(obj, "line" + i)) != null) {
                                String title = null, text = null, time = null, ID = null, avatar = null;
                                int like = 0, comment = 0;
                                if (obj != null) try {
                                    ID = tmp.getString("ID");
                                    title = tmp.getString("nickname");
                                    text = tmp.getString("text_value");
                                    like = tmp.getInt("heart_like");
                                    comment = tmp.getInt("refer_sum");
                                    time = tmp.getString("assign_date");
                                    avatar = tmp.getString("avatar");
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
                                map.put("avatar",avatar);
                                listItem.add(map);
                                i++;
                            }
                        }else {
                            if(obj.getString("reason").equals("USER_NOT_PERMITTED")){
                                Toast.makeText(getActivity(),"您没有权限访问",Toast.LENGTH_SHORT).show();
                            }
                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mSimpleAdapter.notifyDataSetChanged();
            }else {
                try {
                    Toast.makeText(getActivity(), "网络连接错误！请检查网络连接", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return true;
        }
    });
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onSyncDataFromServer();
        Message msg = new Message();
        myHandler.sendMessage(msg);
    }
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
        try {
//        JSONObject obj_new = Challenge.getChallengeByIdentify()
            mSimpleAdapter = new SimpleAdapter(getActivity(), listItem,//需要绑定的数据
                    R.layout.liuyanban_item,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                    new String[]{
                            "title", "text", "like", "comment", "time", "avatar"},
                    new int[]{R.id.nickname, R.id.lyb_text, R.id.like, R.id.comment, R.id.date,R.id.lyb_avatar}
            );
            mSimpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, final Object data, String s) {
                    Drawable avatar;
                    if (view instanceof ImageView && data instanceof String) {
                        ImageView imageView = (ImageView) view;
                        if ((avatar = AsyncLoadAvatar.GetLocalImage(getActivity(), (String) data)) == null)           //获取存在本地的Bitmap
                        {
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if (AsyncLoadAvatar.SaveBitmapToLocal(AsyncLoadAvatar.DownloadBitmap((String) data), (String) data));  //returned Bitmap   把Bitmap保存到本地
                                    {
                                        getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mSimpleAdapter.notifyDataSetChanged();
                                        }
                                    });
                                    }
                                }
                            });
                            thread.start();
                            imageView.setImageResource(R.drawable.android_181);
                            return true;
                        } else {
                            imageView.setImageDrawable(avatar);
                            return true;
                        }
                    }
                    return false;
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        lv.setPullLoadEnable(true);
        lv.setPullRefreshEnable(true);
        lv.setXListViewListener(this);
        lv.setAdapter(mSimpleAdapter);
        lv.mContext = getActivity();
        lv.transit = (ViewGroup) getActivity().findViewById(R.id.shangbanbufen);
        lv.transit_top = (ViewGroup) getActivity().findViewById(R.id.transit_top);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent localIntent = new Intent(getActivity(), Liuyanban_pinglun.class);
                localIntent.putExtra("ID",(String)listItem.get(position-1).get("ID"));
                localIntent.putExtra("nickname",(String)listItem.get(position-1).get("title"));
                localIntent.putExtra("text",(String)listItem.get(position-1).get("text"));
                localIntent.putExtra("date",(String)listItem.get(position-1).get("time"));
                startActivityForResult(localIntent, 1);

            }
        });

        if(guestID .equals( Static.identifyDigit)) {
            popupWindowView = inflater.inflate(R.layout.shanchuliuyan_item, null);


            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
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
                    confirmButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String  ID = (String) listItem.get(position-1).get("ID");
                            if(popupWindow.isShowing()){
                                popupWindow.dismiss();
                            }
                            final HttpIO io = new HttpIO(Utils.serveraddr + "/api/comment.php?action=delete&id="+ID);
                            io.SessionID = Static.session_id;
                            new Thread(new Runnable() {
                            @Override
                            public void run() {
                                io.GETToHTTPServer();
                            }
                            }).start();
                            while(io.getResultData()==null);
                            onSyncDataFromServer();
                            Message msg = new Message();
                            myHandler.sendMessage(msg);
                            Toast.makeText(getActivity(),"删除成功！",Toast.LENGTH_SHORT).show();
                        }
                    });

                    cancleButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(popupWindow.isShowing()){
                                popupWindow.dismiss();
                            }
                        }
                    });
                    return true;
                }

            });
        }
        return v;
    }
    private void onLoad() {
        lv.stopRefresh();
        lv.stopLoadMore();
        lv.setRefreshTime(new SimpleDateFormat(DATE_FORMAT_STR, Locale.CHINA).format(new Date()));
    }

    @Override
    public void onRefresh() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onSyncDataFromServer();
                onLoad();
            }
        });

    }

    @Override
    public void onLoadMore() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                onLoad();
            }
        });

    }


    public void onSyncDataFromServer(){
        String challenge_name = null, challenge_nickname = null;
        listItem.clear();
        //http://172.24.10.118/api/comment.php?action=queryFriends&guest_id=10000
        Utils.GetBuilder get = new Utils.GetBuilder(Utils.serveraddr + "/api/comment.php");
        get.addItem("action", "queryFriends");
        get.addItem("type","3");
        get.addItem("guest_id",guestID);
        HttpIO.GetHttpEX(getActivity(),asynHandler,get.toString());

        //lv.
    }
}