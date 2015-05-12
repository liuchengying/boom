package boom.boom.bangzhuyufankui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.SysApplication;
import boom.boom.myview.SildingFinishLayout;

/**
 * Created by 刘成英 on 2015/4/1.
 */
public class Bangzhuyufankui_activity extends Activity {
    private LinearLayout bangzhuyufankui_fh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bangzhuyufankui);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        SildingFinishLayout mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.sfl);
        mSildingFinishLayout
                .setOnSildingFinishListener(new SildingFinishLayout.OnSildingFinishListener() {

                    @Override
                    public void onSildingFinish() {
                        Bangzhuyufankui_activity.this.finish();
                    }
                });

        mSildingFinishLayout.setTouchView(mSildingFinishLayout);
        bangzhuyufankui_fh = (LinearLayout) findViewById(R.id.bangzhuyufankui_fh);
        bangzhuyufankui_fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, R.anim.base_slide_right_out);
            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.liuyan_out);
    }


}
