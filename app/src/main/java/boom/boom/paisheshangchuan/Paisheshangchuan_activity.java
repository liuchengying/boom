package boom.boom.paisheshangchuan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

import boom.boom.R;
import boom.boom.api.SysApplication;
import boom.boom.paishetiaozhan.Paishetiaozhan_activity;
import boom.boom.shangchuanchenggong.Shangchuanchenggong_activity;
import boom.boom.shangchuandengdai.Shangchuandengdai_activity;
import boom.boom.tingzhitiaozhan.Tingzhipaishe_activity;

/**
 * Created by 刘成英 on 2015/1/28.
 */
public class Paisheshangchuan_activity extends Activity
{
    private boolean bianliang;
    private Button paisheshangchuan;
    private Button shangchuanpaishefanhui;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paisheshangchuan);
        SysApplication.getInstance().addActivity(this);
        shangchuanpaishefanhui= (Button) findViewById(R.id.paisheshangchuanfanhui);
        paisheshangchuan = (Button) findViewById(R.id.paisheshangchuan);
        shangchuanpaishefanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Paisheshangchuan_activity.this, Tingzhipaishe_activity.class);
                startActivity(intent);
            }

        });
        paisheshangchuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Paisheshangchuan_activity.this, Shangchuandengdai_activity.class);
                startActivity(intent);
            }
        });


    }
   /** Timer timer = new Timer();
    TimerTask task = new TimerTask(){
        @Override
        public void run() {
            if(bianliang) {
                Intent intent = new Intent(Paishetiaozhan_activity.this, Shangchuandengdai_activity.class);
                startActivity(intent);
                Paishetiaozhan_activity.this.finish();

            }
        }
    };
    timer.schedule(task, 2000);
    **/

}
