package boom.boom.shipintianzhanpinglun;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import android.widget.VideoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.Challenge;
import boom.boom.api.SysApplication;
import boom.boom.api.Utils;
import boom.boom.myview.SildingFinishLayout;


/**
 * Created by lcy on 2015/5/4.
 */
public class Shipintianzhan_pinglun extends Activity {

    private VideoView frame_frontvideo;

    private JSONObject challenge_data;
    private String demoToken;
    private Button shipinbofang1;
    private int PlayerState = 0;

    private ListView lv;

    private LinearLayout shipinpinglun_fh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.shipintiaozhan_pinglun);

        SildingFinishLayout mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.sildingFinishLayout);
        mSildingFinishLayout
                .setOnSildingFinishListener(new SildingFinishLayout.OnSildingFinishListener() {

                    @Override
                    public void onSildingFinish() {
                        Shipintianzhan_pinglun.this.finish();
                    }
                });

        mSildingFinishLayout.setTouchView(mSildingFinishLayout);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        Intent intent = getIntent();
        final int position = intent.getIntExtra("challenge_number", 1);
        try {
            challenge_data = new JSONObject(Challenge.getChallengeByNumber(position));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        shipinbofang1 = (Button)findViewById(R.id.shipinbofang1);
        frame_frontvideo = (VideoView) findViewById(R.id.videoView_front1);

        try {

            demoToken = challenge_data.getString("demovideo");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        frame_frontvideo.setVideoURI(Uri.parse(Utils.serveraddr + Utils.getVideoAPI(demoToken) + "&" + Utils.parsSessionViaGET()));

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
//        frame_frontvideo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

       //listview

        List<Map<String , Object>> mSelfData = new ArrayList<Map<String,Object>>();

        /**
         * 获取ListView组件。
         */
        lv = (ListView) findViewById(R.id.shipinpinglun_listview);


        /**
         * 生成数据。
         */
        for (int i = 0; i < 10; i++) {
            HashMap<String, Object> mMap = new HashMap<String, Object>();
            mMap.put("img", R.drawable.android_181);
            mMap.put("name", "第" + i+"人");
            mMap.put("text", "第" + i);
            mSelfData.add(mMap);
        }

        /**
         * 自定义Adapter。
         */
        final SimpleAdapter mSimpleAdapter = new SimpleAdapter(this, mSelfData,
                R.layout.shipinpinglun_item, new String[] { "img", "name" ,"text"}, new int[] {
                R.id.shipinpinglun_Item_touxiang, R.id.shipinpinglun_nickname ,R.id.shipinpinglun_neirong});

        /**
         * 向ListView设置Adapter。
         */
        lv.setAdapter(mSimpleAdapter);
       shipinpinglun_fh = (LinearLayout) findViewById(R.id.shipinpinglun_fh);
        shipinpinglun_fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, R.anim.base_slide_right_out);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }

}



