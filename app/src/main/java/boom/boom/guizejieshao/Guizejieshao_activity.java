package boom.boom.guizejieshao;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
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
 * Created by 刘成英 on 2015/1/19.
 */
public class Guizejieshao_activity extends Activity{
    private LinearLayout fanhuitianzhan;
    private  Button woyaotianzhan;
    private VideoView frame_frontvideo;
    private TextView challenge_title;
    private TextView challenge_text;
    private JSONObject challenge_data;
    private String demoToken;
    private Button shipinbofang;
    private int PlayerState = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("Video", "Video frame entry here.");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.guizejieshao);

        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        Intent intent = getIntent();
        final int position = intent.getIntExtra("challenge_number", 1);
        try {
            challenge_data = new JSONObject(Challenge.getChallengeByNumber(position));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        fanhuitianzhan = (LinearLayout) findViewById(R.id.fanhuitiaozhan);
        woyaotianzhan = (Button) findViewById(R.id.woyaotiaozhan);
        shipinbofang = (Button)findViewById(R.id.shipinbofang);
        frame_frontvideo = (VideoView) findViewById(R.id.videoView_front);
        challenge_title = (TextView) findViewById(R.id.challenge_title);
        challenge_text = (TextView) findViewById(R.id.challenge_text);
        try {
            challenge_text.setText(challenge_data.getString("frontname"));
            challenge_title.setText(challenge_data.getString("shortintro"));
            demoToken = challenge_data.getString("demovideo");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //MediaController ctrller = new MediaController(this);
        //ctrller.setAnchorView(frame_frontvideo);
        //frame_frontvideo.setMediaController(ctrller);
//        String debug = Utils.serveraddr+Utils.getVideoAPI(demoToken);
//        frame_frontvideo.setVideoURI(Uri.parse("http://172.24.10.118/download/download1.mp4"));
        frame_frontvideo.setVideoURI(Uri.parse(Utils.serveraddr+Utils.getVideoAPI(demoToken)+"&"+Utils.parsSessionViaGET()));
        //frame_frontvideo.requestFocus();
        shipinbofang.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                switch (PlayerState){
                    case 0:
                    frame_frontvideo.start();
                    shipinbofang.setVisibility(View.INVISIBLE);
                    PlayerState = 1;
                        break;
                    case 1:
                    shipinbofang.setVisibility(View.VISIBLE);
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
                        shipinbofang.setVisibility(View.INVISIBLE);
                        PlayerState = 1;
                        break;
                    case 1:
                        Log.e("Video", "========  VideoView stop playing =========");
                        shipinbofang.setVisibility(View.VISIBLE);
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
        woyaotianzhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                        Intent intent = new Intent();
                        intent.putExtra("challenge_number", position);
                try {
                    intent.putExtra("challenge_name", challenge_data.getString("name"));
                    intent.putExtra("challenge_id", challenge_data.getString("identifyDigit"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.setClass(Guizejieshao_activity.this, Paishetiaozhan_activity.class);
                       startActivity(intent);
                //Guizejieshao_activity.this.finish();
                }
        });
        fanhuitianzhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        }


}
