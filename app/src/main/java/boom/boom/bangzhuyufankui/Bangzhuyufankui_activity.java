package boom.boom.bangzhuyufankui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.SysApplication;

/**
 * Created by 刘成英 on 2015/4/1.
 */
public class Bangzhuyufankui_activity extends Activity {
    private LinearLayout bangzhuyufankui_fh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bangzhuyufankui);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        bangzhuyufankui_fh = (LinearLayout) findViewById(R.id.bangzhuyufankui_fh);
        bangzhuyufankui_fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
