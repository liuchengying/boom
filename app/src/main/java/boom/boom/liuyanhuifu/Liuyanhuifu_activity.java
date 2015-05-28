package boom.boom.liuyanhuifu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.SysApplication;

/**
 * Created by lcy on 2015/5/28.
 */
public class Liuyanhuifu_activity extends Activity {
    private LinearLayout liuyanhuifu_fh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pinglun_shurukuang);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        liuyanhuifu_fh = (LinearLayout) findViewById(R.id.liuyan_fh);
        liuyanhuifu_fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0,R.anim.liuyan_out);
            }
        });
    }
}
