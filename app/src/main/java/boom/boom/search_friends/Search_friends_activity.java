package boom.boom.search_friends;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.logging.Handler;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.AsyncLoadAvatar;
import boom.boom.api.HttpIO;
import boom.boom.api.Static;
import boom.boom.api.SysApplication;
import boom.boom.api.Utils;
import boom.boom.gerenzhuye.Gerenzhuye_activity;
import boom.boom.myview.RoundedImageView;
import boom.boom.shezhi.Shezhi_activity;


/**
 * Created by Administrator on 2015/6/3.
 */
public class Search_friends_activity extends Activity{
    EditText search;
    ListView search_listview;
    RoundedImageView avatar_round;
    RelativeLayout hylb_fh;
    ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,     Object>>();
    SimpleAdapter mSimpleAdapter;
    android.os.Handler myMessageHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mSimpleAdapter.notifyDataSetChanged();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_friends);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        search = (EditText) findViewById(R.id.ss_search);
        search_listview = (ListView) findViewById(R.id.search_listview);
        hylb_fh = (RelativeLayout) findViewById(R.id.haoyouliebiao_fh);
        mSimpleAdapter = new SimpleAdapter(Search_friends_activity.this, listItem,//需要绑定的数据
                R.layout.search_friends_item,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                new String[]{
                        "nickname","avatar"},
                new int[]{R.id.search_nickname,R.id.search_avatar}
        );
        mSimpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, final Object data, String textRepresentation) {
                Bitmap avatar;
                if (view instanceof ImageView && data instanceof String) {
                    ImageView imageView = (ImageView) view;
                    if ((avatar = AsyncLoadAvatar.GetLocalImage(Search_friends_activity.this,(String) data)) == null)           //获取存在本地的Bitmap
                    {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (AsyncLoadAvatar.SaveBitmapToLocal(AsyncLoadAvatar.DownloadBitmap((String) data), (String) data));  //returned Bitmap   把Bitmap保存到本地
                                {
                                    Message msg = new Message();
                                    Search_friends_activity.this.myMessageHandler.sendMessage(msg);
                                }
                            }
                        });
                        thread.start();
                        imageView.setImageResource(R.drawable.android_181);
                        return true;
                    } else {
                        imageView.setImageBitmap(avatar);
                        return true;
                    }
                }
                return false;
            }
        });
        avatar_round = (RoundedImageView) findViewById(R.id.search_avatar);
        search_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(Search_friends_activity.this,"asdasd",Toast.LENGTH_SHORT).show();
                HashMap<String,Object>pos = listItem.get(position);
                String friendship = (String)pos.get("friendship");
                if(friendship.equals("FRIENDSHIP_EXISTED")){
                    Intent intent = new Intent();
                    intent.putExtra("guestID",(String)pos.get("identifyDigit"));
                    intent.putExtra("type",1);
                    intent.setClass(Search_friends_activity.this, Gerenzhuye_activity.class);
                    startActivity(intent);
                }else if(friendship.equals("FRIENDSHIP_NOT_EXISTED")){
                    Intent intent = new Intent();
                    intent.putExtra("type",2);
                    intent.putExtra("guestID",(String)pos.get("identifyDigit"));
                    intent.setClass(Search_friends_activity.this, Gerenzhuye_activity.class);
                    startActivity(intent);
                }else if(friendship.equals("AWAITING_FRIENDS_ACCEPTED")){
                    Intent intent = new Intent();
                    intent.putExtra("type",3);
                    intent.putExtra("guestID",(String)pos.get("identifyDigit"));
                    intent.setClass(Search_friends_activity.this, Gerenzhuye_activity.class);
                    startActivity(intent);
                }
            }
        });
        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, final KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm.isActive()){

                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0 );
                        try{
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        HttpIO io = new HttpIO(Utils.serveraddr + "/api/newfriend.php?action=searchByName&name=" + search.getText());
                                        io.SessionID = Static.session_id;
                                        io.GETToHTTPServer();
                                        JSONObject obj = new JSONObject(io.getResultData());
                                        Log.e("obj",io.getResultData());

                                        JSONObject response = Utils.GetSubJSONObject(obj,"response");
                                        if(response.getString("state").equals("SUCCESS"))
                                        {
                                            int round = response.getInt("limit");
                                            for(int i=0; i<round; i++)
                                            {
                                                listItem.clear();
                                                JSONObject item = Utils.GetSubJSONObject(obj,""+i);
                                                HashMap<String, Object> map = new HashMap<String, Object>();
                                                map.put("nickname",item.getString("nickname"));
                                                map.put("avatar",item.getString("avatar"));
                                                map.put("identifyDigit",item.getString("identifyDigit"));
                                                map.put("friendship",item.getString("friendship"));
                                                listItem.add(map);
                                            }
                                        }
                                    }catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                    try {
//        JSONObject obj_new = Challenge.getChallengeByIdentify()
                                        Message m = new Message();
                                        myMessageHandler.sendMessage(m);
                                    }catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            thread.start();
                            search_listview.setAdapter(mSimpleAdapter);

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    return true;
                }
                return false;
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,0);
    }
}
