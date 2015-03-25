package boom.boom.guizejieshao;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.MediaController;
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
import boom.boom.tianzhan.Tiaozhan_activity;

/**
 * Created by 刘成英 on 2015/1/19.
 */
public class Guizejieshao_activity extends Activity{
    private Button fanhuitianzhan;
    private  Button woyaotianzhan;
    private VideoView frame_frontvideo;
    private TextView challenge_title;
    private TextView challenge_text;
    private JSONObject challenge_data;
    private String demoToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        fanhuitianzhan = (Button) findViewById(R.id.fanhuitiaozhan);
        woyaotianzhan = (Button) findViewById(R.id.woyaotiaozhan);
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
        MediaController ctrller = new MediaController(this);
        ctrller.setAnchorView(frame_frontvideo);
        frame_frontvideo.setMediaController(ctrller);
        frame_frontvideo.setVideoURI(Uri.parse(Utils.getVideoAPI(demoToken)));
        frame_frontvideo.start();
        woyaotianzhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                        Intent intent = new Intent();
                        intent.putExtra("challenge_number", position);
                try {
                    intent.putExtra("challenge_name", challenge_data.getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.setClass(Guizejieshao_activity.this, Paishetiaozhan_activity.class);
                       startActivity(intent);
                }
        });
        fanhuitianzhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Guizejieshao_activity.this, Tiaozhan_activity.class);
                startActivity(intent);
            }
        });
    }
}
