package boom.boom.shipintianzhanpinglun;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.AsyncLoadAvatar;
import boom.boom.api.HttpIO;
import boom.boom.api.Static;
import boom.boom.api.SysApplication;
import boom.boom.api.Utils;
import boom.boom.liuyanhuifu.Liuyanhuifu_activity;
import boom.boom.myview.RoundedImageView;
import boom.boom.myview.SildingFinishLayout;

/**
 * Created by Administrator on 2015/7/11.
 */
public class Shipinpinglun_fragment extends Fragment{
    private VideoView frame_frontvideo;

    private JSONObject challenge_data;
    private String demoToken;
    private Button shipinbofang1;
    private int PlayerState = 0;
    private String nickname;
    private String cl_name;
    private String date;
    private String elapsed;
    private ListView lv;
    private RelativeLayout liuyan;
    private TextView nickname_tv;
    private TextView cl_name_tv;
    private TextView date_tv;
    private TextView elapsed_tv;
    private ToggleButton heartlike;
    private SimpleAdapter mSimpleAdapter;
    private RelativeLayout shipinpinglun_fh;
    private String guestID;
    private String cl_id;
    private String ID;
    public LinearLayout allLinear;
    private LinearLayout sppl_horizon;
    private ArrayList<String> avatarlist;
    private ArrayList<HashMap<String, Object>> listItem= new ArrayList<HashMap<String,     Object>>();
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
    android.os.Handler myMessageHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mSimpleAdapter.notifyDataSetChanged();
            LoadImage();
            setListViewHeightBasedOnChildren(lv);
        }
    };
    Handler getdataHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == 1){
                try{
                    String result = msg.getData().getString("data");
                    JSONObject obj = new JSONObject(result);
                    try{
                        JSONObject data = Utils.GetSubJSONObject(obj, "data");
                        cl_name = data.getString("challenge_frontname");
                        date = data.getString("date");
                        elapsed = data.getString("elapsed_time");
                        frame_frontvideo.setVideoURI(Uri.parse(Utils.serveraddr + Utils.getVideoAPI(data.getString("video")) + "&" + Utils.parsSessionViaGET()));
                        JSONObject winner_user = Utils.GetSubJSONObject(obj, "winner_user");
                        nickname = winner_user.getString("nickname");
                        int liked = winner_user.getInt("heart_like");
                        heartlike.setChecked(liked > 0);
                        nickname_tv.setText(nickname);
                        cl_name_tv.setText(cl_name);
                        date_tv.setText(date);
                        elapsed_tv.setText(elapsed + "s");

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        JSONObject heart_like = Utils.GetSubJSONObject(obj,"all_heart_like_record");
                        int i=0;
                        JSONObject line = null;
                        avatarlist = new ArrayList<>();
                        while((line = Utils.GetSubJSONObject(heart_like,"line"+i))!=null){
                            String avatar = line.getString("avatar");
                            avatarlist.add(avatar);
                            i++;
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    LoadImage();
                    JSONObject refer_data = Utils.GetSubJSONObject(obj,"refer_data");
                    int i = 0;
                    JSONObject line;
                    try {
                        while ((line = Utils.GetSubJSONObject(refer_data, "line" + i)) != null) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("ID", line.getString("ID"));
                            map.put("text", line.getString("text_value"));
                            map.put("date", line.getString("assign_date"));
                            String positionID = line.getString("identifyDigit");
                            map.put("nickname", (positionID.equals(Static.identifyDigit)) ? "我" : line.getString("nickname"));
                            map.put("avatar", line.getString("avatar"));
                            listItem.add(map);
                            i++;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    mSimpleAdapter.notifyDataSetChanged();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(getActivity(), "网络连接错误！请检查网络连接", Toast.LENGTH_SHORT).show();
            }
            setListViewHeightBasedOnChildren(lv);
            return true;
        }
    });
    private void LoadImage (){
        sppl_horizon.removeAllViews();
        for(int i=0;i<avatarlist.size();i++){
            RoundedImageView imageView=new RoundedImageView(getActivity());
            imageView.setImageResource(R.drawable.android_181);
            imageView.setPadding(10, 0, 0, 0);
            imageView.setId(i);
            sppl_horizon.addView(imageView);
            Drawable avatar;
            final String data = avatarlist.get(i);
            if ((avatar = AsyncLoadAvatar.GetLocalImage(getActivity(),(String) data)) == null)           //获取存在本地的Bitmap
            {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (AsyncLoadAvatar.SaveBitmapToLocal(AsyncLoadAvatar.DownloadBitmap((String) data), (String) data));  //returned Bitmap   把Bitmap保存到本地
                        {
                            Message msg = new Message();
                            myMessageHandler.sendMessage(msg);
                        }
                    }
                });
                thread.start();
                imageView.setImageResource(R.drawable.android_181);
            } else {
                Log.e("1","width:"+Static.width+" height:"+Static.height);
                Bitmap bmAvatar;
                if(Static.width == 480 && Static.height == 854)
                    bmAvatar = Utils.zoomImage(Utils.drawableToBitmap(avatar),10,15);
                else
                    bmAvatar = Utils.zoomImage(Utils.drawableToBitmap(avatar),60,70);

                imageView.setImageBitmap(bmAvatar);
            }

        }
    }
    private void LoadData (){

        listItem.clear();
        HttpIO.GetHttpEX(getActivity(), getdataHandler, Utils.serveraddr + "/api/rank.php?action=getsingle&id=" + ID);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LoadData();
        mSimpleAdapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(lv);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.shipinpinlun_frame,container,false);
        SildingFinishLayout mSildingFinishLayout = (SildingFinishLayout) getActivity().findViewById(R.id.sildingFinishLayout);
        mSildingFinishLayout
                .setOnSildingFinishListener(new SildingFinishLayout.OnSildingFinishListener() {

                    @Override
                    public void onSildingFinish() {
                        getActivity().finish();
                    }
                });

        mSildingFinishLayout.setTouchView(mSildingFinishLayout);
        SysApplication.getInstance().addActivity(getActivity());
        FontManager.changeFonts(FontManager.getContentView(getActivity()), getActivity());//字体
        Utils.getResolution(getActivity());
        allLinear = (LinearLayout) v.findViewById(R.id.list_all);
        lv = (ListView) v.findViewById(R.id.shipinpinglun_listview);
        nickname_tv = (TextView) v.findViewById(R.id.nickname);
        cl_name_tv = (TextView) v.findViewById(R.id.cl_name);
        date_tv = (TextView) v.findViewById(R.id.date);
        elapsed_tv = (TextView) v.findViewById(R.id.elapsed);
        sppl_horizon = (LinearLayout)v.findViewById(R.id.sppl_horizon);
        heartlike = (ToggleButton) v.findViewById(R.id.shipinpinglun_toggle);
        heartlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final HttpIO io = new HttpIO(Utils.serveraddr + "api/msg.php?action=like&type=9&position="+ID);
                io.SessionID = Static.session_id;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        io.GETToHTTPServer();
                    }
                }).start();
                while (io.getResultData()==null);
                LoadData();
            }
        });
        Intent intent = getActivity().getIntent();
        //guestID = intent.getStringExtra("guestID");
        ID = intent.getStringExtra("ID");
        LoadData();
        setListViewHeightBasedOnChildren(lv);
        /*nickname = intent.getStringExtra("nickname");
        cl_name = intent.getStringExtra("cl_name");
        date = intent.getStringExtra("date");
        elapsed = intent.getStringExtra("elapsed");
*/




        mSimpleAdapter = new SimpleAdapter(getActivity(),listItem,R.layout.shipinpinglun_item,new String[]{
                "nickname","text","date","avatar"},new int[]{R.id.shipinpinglun_nickname,R.id.shipinpinglun_neirong,R.id.shipinpinglun_date,R.id.shipinpinglun_Item_touxiang}
        );
        lv.setAdapter(mSimpleAdapter);
        mSimpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, final Object data, String textRepresentation) {
                Drawable avatar;
                if (view instanceof ImageView && data instanceof String) {
                    ImageView imageView = (ImageView) view;
                    if ((avatar = AsyncLoadAvatar.GetLocalImage(getActivity(),(String) data)) == null)           //获取存在本地的Bitmap
                    {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (AsyncLoadAvatar.SaveBitmapToLocal(AsyncLoadAvatar.DownloadBitmap((String) data), (String) data));  //returned Bitmap   把Bitmap保存到本地
                                {
                                    Message msg = new Message();
                                    myMessageHandler.sendMessage(msg);
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
        shipinbofang1 = (Button)v.findViewById(R.id.shipinbofang1);
        frame_frontvideo = (VideoView) v.findViewById(R.id.videoView_front1);

        shipinbofang1.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                switch (PlayerState){
                    case 0:
                        frame_frontvideo.start();
                        shipinbofang1.setVisibility(View.INVISIBLE);
                        PlayerState = 1;
                        break;
                    case 1:
                        shipinbofang1.setVisibility(View.VISIBLE);
                        frame_frontvideo.pause();
                        PlayerState = 0;
                        break;
                }
            }
        });
        frame_frontvideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("Video", "====== Enter video view switcher ========");
                switch (PlayerState) {
                    case 0:
                        Log.e("Video", "======== VideoView start playing =========");
                        frame_frontvideo.start();
                        shipinbofang1.setVisibility(View.INVISIBLE);
                        PlayerState = 1;
                        break;
                    case 1:
                        Log.e("Video", "========  VideoView stop playing =========");
                        shipinbofang1.setVisibility(View.VISIBLE);
                        frame_frontvideo.pause();
                        PlayerState = 0;
                        break;
                }
                return false;
            }
        });
        /**
         * 向ListView设置Adapter。
         */
        lv.setAdapter(mSimpleAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), Liuyanhuifu_activity.class);
                intent.putExtra("nickname",(String)listItem.get(position).get("nickname"));
                intent.putExtra("ID",(String)listItem.get(position).get("ID"));
                intent.putExtra("type","2");
                startActivityForResult(intent, 1);
                getActivity().overridePendingTransition(0,R.anim.liuyan_in);
            }
        });
        liuyan = (RelativeLayout) getActivity().findViewById(R.id.pinglun_liuyan);
        liuyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),Liuyanhuifu_activity.class);
                intent.putExtra("type","1");
                intent.putExtra("nickname",nickname);
                intent.putExtra("ID",ID);
                startActivityForResult(intent,1);
                getActivity().overridePendingTransition(0, R.anim.liuyan_in);
            }
        });
        shipinpinglun_fh = (RelativeLayout) getActivity().findViewById(R.id.shipinpinglun_fh);
        shipinpinglun_fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                getActivity().overridePendingTransition(0, R.anim.base_slide_right_out);
            }
        });
        return v;
    }
}
