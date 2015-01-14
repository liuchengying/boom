package boom.boom.denglu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import boom.boom.R;
import boom.boom.zhujiemian.Main_activity;

/**
 * Created by 刘成英 on 2015/1/13.
 */
public class dengluzhuce_activity extends Activity {
    private Button fanhui;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhuce);
        fanhui = (Button)findViewById(R.id.zhucufanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(dengluzhuce_activity.this, Main_activity.class);
                startActivity(intent);


            }
        });

    }
}
