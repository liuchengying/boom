package boom.boom.zhucechenggong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.SysApplication;
import boom.boom.wanshanxinxi.Wanshanxinxi_activity;
import boom.boom.zhujiemian.Main_activity;

/**
 * Created by 刘成英 on 2015/1/15.
 */
public class Zhucechenggong_activity extends Activity {
    private Button wanshanxinxi;
    private String identifyDigit;
    private RelativeLayout fanhui;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhucechenggong);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        identifyDigit = getIntent().getStringExtra("identifyDigit");
        wanshanxinxi=(Button)findViewById(R.id.wanshanxinxi);
        fanhui = (RelativeLayout) findViewById(R.id.shezhifanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Zhucechenggong_activity.this, Main_activity.class);
                startActivity(intent);
                finish();
            }
        });
        wanshanxinxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Zhucechenggong_activity.this, Wanshanxinxi_activity.class);
                intent.putExtra("identifyDigit",identifyDigit);
                startActivity(intent);
            }
        });
    }
}
