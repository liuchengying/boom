package boom.boom.faqitianzhan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.guizejieshao.Guizejieshao_activity;
import boom.boom.zinitiaozhan.Zinitianzhan_activity;

/**
 * Created by 刘成英 on 2015/3/25.
 */
public class Faqitianzhan_activity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faqitiaozhan);
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
            }
        });

    }
}
