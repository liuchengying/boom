package boom.boom.faqitianzhan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.guizejieshao.Guizejieshao_activity;
import boom.boom.myview.SildingFinishLayout;
import boom.boom.zinitiaozhan.Zinitianzhan_activity;

/**
 * Created by 刘成英 on 2015/3/25.
 */
public class Faqitianzhan_activity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.faqitiaozhan);
        SildingFinishLayout mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.sildingFinishLayout);
        mSildingFinishLayout
                .setOnSildingFinishListener(new SildingFinishLayout.OnSildingFinishListener() {

                    @Override
                    public void onSildingFinish() {
                        Faqitianzhan_activity.this.finish();
                    }
                });

        mSildingFinishLayout.setTouchView(mSildingFinishLayout);


    // Press the back button in mobile phone

        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        final LinearLayout fqtz_fh = (LinearLayout)findViewById(R.id.fqtz_fh);
        Button zntz = (Button)findViewById(R.id.zntz);
        zntz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Faqitianzhan_activity.this, Zinitianzhan_activity.class);
                startActivity(intent);
            }
        });
        fqtz_fh.setOnClickListener(new View.OnClickListener() {
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
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }
}
