package boom.boom.haoyouliebiao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.AsyncLoadAvatar;
import boom.boom.api.FriendList;
import boom.boom.api.SysApplication;
import boom.boom.gerenzhuye.Gerenzhuye_activity;
import boom.boom.myview.SildingFinishLayout;
import boom.boom.search_friends.Search_friends_activity;
import boom.boom.tianjiahaoyou.Tianjiahaoyou_activity;

/**
 * Created by lcy on 2015/4/29.
 */
public class Haoyouliebiao_activity extends Activity {
    ListView lv;
    RelativeLayout haoyouliebiao_fh;
    SimpleAdapter mSimpleAdapter;
    TextView hylb_search;

    ArrayList<HashMap<String, Object>> listItem;
    android.os.Handler myMessageHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mSimpleAdapter.notifyDataSetChanged();
        }
    };
            @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.haoyouliebiao);
            SildingFinishLayout mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.sildingFinishLayout);
            mSildingFinishLayout
                    .setOnSildingFinishListener(new SildingFinishLayout.OnSildingFinishListener() {
                        @Override
                        public void onSildingFinish() {
                            Haoyouliebiao_activity.this.finish();
                        }
                    });

            mSildingFinishLayout.setTouchView(mSildingFinishLayout);
            SysApplication.getInstance().addActivity(this);
            FontManager.changeFonts(FontManager.getContentView(this), this);//字体
            lv = (ListView) findViewById(R.id.haoyouliebiao_listview);

            FriendList friendList = new FriendList();


            listItem = friendList.GetFriendList();
       /* ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();/*//*在数组中存放数据*//**//*
        for (int i = 0; i < 10; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("avatar", R.drawable.android_181);//加入图片            map.put("ItemTitle", "第"+i+"行");
            map.put("nickname", "这是第" + i + "个好友");
            listItem.add(map);
        }*/
            mSimpleAdapter = new SimpleAdapter(this, listItem,//需要绑定的数据
                    R.layout.haoyouliebiao_item_down,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                    new String[]{
                            "avatar", "nickname"},
                    new int[]{R.id.haoyouliebiao_touxiang, R.id.haoyouliebiao_nicheng}
            );
            mSimpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, final Object data, String textRepresentation) {
                    Bitmap avatar;
                    if (view instanceof ImageView && data instanceof String) {
                        ImageView imageView = (ImageView) view;
                        if ((avatar = AsyncLoadAvatar.GetLocalImage((String) data)) == null)           //获取存在本地的Bitmap
                        {
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if (AsyncLoadAvatar.SaveBitmapToLocal(AsyncLoadAvatar.DownloadBitmap((String) data), (String) data));  //returned Bitmap   把Bitmap保存到本地
                                    {
                                        Message msg = new Message();
                                        Haoyouliebiao_activity.this.myMessageHandler.sendMessage(msg);
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
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String guestID = (String) ((HashMap<String, Object>) listItem.get(position)).get("guestID");
                    Intent intent = new Intent();
                    intent.putExtra("guestID", guestID);
                    intent.putExtra("type",1);
                    intent.setClass(Haoyouliebiao_activity.this, Gerenzhuye_activity.class);
                    startActivity(intent);
                }
            });

            hylb_search = (TextView) findViewById(R.id.hylb_search);
            hylb_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent();
                    intent.setClass(Haoyouliebiao_activity.this, Search_friends_activity.class);
                    startActivity(intent);
                }
            });
            lv.setAdapter(mSimpleAdapter);
            haoyouliebiao_fh = (RelativeLayout) findViewById(R.id.haoyouliebiao_fh);
            haoyouliebiao_fh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(0, R.anim.base_slide_right_out);
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }

}
