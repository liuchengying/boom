package boom.boom.shipintianzhanpinglun;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import org.json.JSONException;
import org.json.JSONObject;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.Challenge;
import boom.boom.api.SysApplication;
import boom.boom.api.Utils;
import boom.boom.paishetiaozhan.Paishetiaozhan_activity;

/**
 * Created by lcy on 2015/5/4.
 */
public class Shipintianzhan_pinglun extends Activity {

    private VideoView frame_frontvideo;

    private JSONObject challenge_data;
    private String demoToken;
    private Button shipinbofang1;
    private int PlayerState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shipintiaozhan_pinglun);
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



    }
        }



