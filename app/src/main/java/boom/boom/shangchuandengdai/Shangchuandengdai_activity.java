package boom.boom.shangchuandengdai;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import boom.boom.R;
import boom.boom.paishetiaozhan.Paishetiaozhan_activity;
import boom.boom.tianzhan.Tiaozhan_activity;

/**
 * Created by 刘成英 on 2015/1/20.
 */
public class Shangchuandengdai_activity extends Activity {
    private Button shangchuandengdaifanhui;
    private Button fangqishangchuang;
    private Button tanchuang1queding;
    private Button tanchuang2quxiao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shangchuandengdai);
        initEvent();
        shangchuandengdaifanhui = (Button) findViewById(R.id.shangchuandengdaifanhui);
        fangqishangchuang = (Button) findViewById(R.id.fangqishangchuan);

        shangchuandengdaifanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Shangchuandengdai_activity.this, Paishetiaozhan_activity.class);
                startActivity(intent);
            }
        });




}

    private void initEvent() {
        findViewById(R.id.fangqishangchuan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog1();


            }
        });
    }
    private  void showDialog1(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.tanchaung2,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);





        AlertDialog dialog = builder.create();
        dialog.show();


    }
   }

