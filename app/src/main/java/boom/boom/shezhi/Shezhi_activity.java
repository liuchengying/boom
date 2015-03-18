package boom.boom.shezhi;



import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ToggleButton;
import android.os.Vibrator;

import boom.boom.R;
import boom.boom.api.SysApplication;

/**
 * Created by 刘成英 on 2015/1/15.
 */

public class Shezhi_activity extends Activity{
    private LinearLayout shezhifanhui;
    private Button tuichuzhanghu;
    private Vibrator vibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shezhi);

        SysApplication.getInstance().addActivity(this);
        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        shezhifanhui = (LinearLayout) findViewById(R.id.shezhifanhui);
        shezhifanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tuichuzhanghu = (Button)findViewById(R.id.tuichuzhanghu);
        tuichuzhanghu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SysApplication.getInstance().exit();

            }
        });
        final ToggleButton mTogBtn = (ToggleButton) findViewById(R.id.sz_toggle1);
        final ToggleButton mTogBtn1 = (ToggleButton) findViewById(R.id.sz_toggle2);
        final ToggleButton mTogBtn2 = (ToggleButton) findViewById(R.id.sz_toggle3);// 获取到控件
        mTogBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {

                    vibrator.cancel();
                 }else{
                    vibrator.vibrate(new long[]{10, 100, 0, 0, 0}, -1);

                }
            }
        });
        mTogBtn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    //isChecken = ture   条件下的操作
                }else{
                    //isChecken = ture
                }
            }
        });
        mTogBtn2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    //isChecken = ture   条件下的操作
                }else{
                    //isChecken = ture
                }
            }
        });

    }
}
