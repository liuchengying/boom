package boom.boom.tongxunlu;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.SysApplication;
import boom.boom.myview.SildingFinishLayout;

/**
 * Created by Lyp on 2015/1/22.
 */
public class tongxunlu_activity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tongxunlu);
        SildingFinishLayout mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.sildingFinishLayout);
        mSildingFinishLayout
                .setOnSildingFinishListener(new SildingFinishLayout.OnSildingFinishListener() {

                    @Override
                    public void onSildingFinish() {
                        tongxunlu_activity.this.finish();
                    }
                });

        mSildingFinishLayout.setTouchView(mSildingFinishLayout);

        Button fanhui=(Button)findViewById(R.id.txl_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
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
