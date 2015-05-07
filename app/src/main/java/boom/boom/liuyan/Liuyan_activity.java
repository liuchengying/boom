package boom.boom.liuyan;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.SysApplication;

/**
 * Created by lcy on 2015/5/7.
 */
public class Liuyan_activity extends Activity {
    private LinearLayout liuyan_fh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.liuyan);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        liuyan_fh = (LinearLayout) findViewById(R.id.liuyan_fh);
        liuyan_fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0,R.anim.liuyan_out);
            }
        });

    }
}
