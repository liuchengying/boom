package boom.boom.guizejieshao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import boom.boom.R;
import boom.boom.paishetiaozhan.Paishetiaozhan_activity;
import boom.boom.tianzhan.Tiaozhan_activity;

/**
 * Created by 刘成英 on 2015/1/19.
 */
public class Guizejieshao_activity extends Activity{
    private Button fanhuitianzhan;
    private  Button woyaotianzhan;
    private VideoView frame_frontvideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guizejieshao);
        fanhuitianzhan = (Button) findViewById(R.id.fanhuitiaozhan);
        woyaotianzhan = (Button) findViewById(R.id.woyaotiaozhan);
        frame_frontvideo = (VideoView) findViewById(R.id.videoView_front);
//        frame_frontvideo.
        woyaotianzhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                        Intent intent = new Intent();
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
