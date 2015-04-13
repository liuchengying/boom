package boom.boom.zhuceyanzheng;

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
 * Created by 刘成英 on 2015/3/12.
 */
public class Zhuceyanzheng_activity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.shoujizhuce);
        SildingFinishLayout mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.sildingFinishLayout);
        mSildingFinishLayout
                .setOnSildingFinishListener(new SildingFinishLayout.OnSildingFinishListener() {

                    @Override
                    public void onSildingFinish() {
                        Zhuceyanzheng_activity.this.finish();
                    }
                });

        mSildingFinishLayout.setTouchView(mSildingFinishLayout);
        LinearLayout zhucefanhui=(LinearLayout)findViewById(R.id.zhucufanhui);
        zhucefanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, R.anim.base_slide_right_out);
            }
        });
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }
}
