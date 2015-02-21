package boom.boom.paishetiaozhan;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

import boom.boom.R;
import boom.boom.guizejieshao.Guizejieshao_activity;
import boom.boom.shangchuandengdai.Shangchuandengdai_activity;
import boom.boom.tingzhitiaozhan.Tingzhipaishe_activity;

/**
 * Created by 刘成英 on 2015/1/20.
 */
public class Paishetiaozhan_activity extends Activity {
    private Button paishefanhui;
    private Button kaishipaishe;
    private SurfaceView sv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.paishetiaozhan);
        paishefanhui = (Button) findViewById(R.id.paishefanhui);
        kaishipaishe = (Button) findViewById(R.id.kaishipaishe);
        sv = (SurfaceView) findViewById(R.id.challengerecord_surface);
        kaishipaishe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Paishetiaozhan_activity.this,Tingzhipaishe_activity.class);
                startActivity(intent);
            }
        });
        paishefanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Paishetiaozhan_activity.this,Guizejieshao_activity.class);
                startActivity(intent);


            }
        });


    }


}
