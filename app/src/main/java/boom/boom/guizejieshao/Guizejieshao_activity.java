package boom.boom.guizejieshao;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.json.JSONException;
import org.json.JSONObject;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.Challenge;
import boom.boom.api.HttpIO;
import boom.boom.api.SysApplication;
import boom.boom.api.Utils;
import boom.boom.paishetiaozhan.Paishetiaozhan_activity;

/**
 * Created by 刘成英 on 2015/1/19.
 */
public class Guizejieshao_activity extends Activity{
    private RelativeLayout fanhuitianzhan;
    private  Button woyaotianzhan;
    private VideoView frame_frontvideo;
    private TextView challenge_title;
    private TextView challenge_text;
    private JSONObject challenge_data;
    private String demoToken;
    private Button shipinbofang;
    private int PlayerState = 0;
    private TextView challenge_date;
    Handler faqiHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == 1){
                try {
                    challenge_data = new JSONObject(msg.getData().getString("data"));
                    setUI();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }else {
                Toast.makeText(Guizejieshao_activity.this,"获取数据失败！服务器响应超时。",Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    });

    private void setUI(){
                try {
                    challenge_data = challenge_data.getJSONObject("1");

                    challenge_text.setText(challenge_data.getString("shortintro"));
                    challenge_title.setText(challenge_data.getString("frontname"));
                    challenge_date.setText(challenge_data.getString("date"));
                    demoToken = challenge_data.getString("demovideo");
                    frame_frontvideo.setVideoURI(Uri.parse(Utils.serveraddr+Utils.getVideoAPI(demoToken)+"&"+Utils.parsSessionViaGET()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("Video", "Video frame entry here.");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.guizejieshao);

        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        Intent intent = getIntent();

        final String position = intent.getStringExtra("challenge_number");
        final int ifFaqi = intent.getIntExtra("ifFaqi",0);
        fanhuitianzhan = (RelativeLayout) findViewById(R.id.fanhuitiaozhan);
        woyaotianzhan = (Button) findViewById(R.id.woyaotiaozhan);
        if(ifFaqi == 1) woyaotianzhan.setText("发起挑战");
        shipinbofang = (Button)findViewById(R.id.shipinbofang);
        frame_frontvideo = (VideoView) findViewById(R.id.videoView_front);
        challenge_title = (TextView) findViewById(R.id.challenge_title);
        challenge_text = (TextView) findViewById(R.id.challenge_text);
        challenge_date = (TextView) findViewById(R.id.challenge_date);
        if(ifFaqi == 1) {
            try {
                //challenge_data = new JSONObject(Challenge.getChallengeByIdentify(position));
                HttpIO.GetHttpEX(Guizejieshao_activity.this,faqiHandler,Utils.serveraddr + "/api/getChallenge.php?action=fetchbyIdentifyDigit&identify=" + position);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(ifFaqi == 0)
        {
            try {
                //Challenge challenge = new Challenge();
                HttpIO.GetHttpEX(Guizejieshao_activity.this,faqiHandler,Utils.serveraddr + "/api/getChallenge.php?action=get_short_intro");

                //challenge_data = challenge.json_data;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        //MediaController ctrller = new MediaController(this);
        //ctrller.setAnchorView(frame_frontvideo);
        //frame_frontvideo.setMediaController(ctrller);
//        String debug = Utils.serveraddr+Utils.getVideoAPI(demoToken);
//        frame_frontvideo.setVideoURI(Uri.parse("http://172.24.10.118/download/download1.mp4"));

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
                    intent.putExtra("pf_iv",getIntent().getIntExtra("pf_iv",0));
                    intent.setClass(Guizejieshao_activity.this, Paishetiaozhan_activity.class);
                    startActivity(intent);
                    Guizejieshao_activity.this.finish();
                } catch (Exception e) {
                    Toast.makeText(Guizejieshao_activity.this,"数据获取失败！请检查网络连接",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
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
