package boom.boom.zhujiemian;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import boom.boom.R;
import boom.boom.denglu.dengluzhuce_activity;
import boom.boom.tianzhan.Tiaozhan_activity;

/**
 * Created by 刘成英 on 2015/1/13.
 */
public class Main_activity extends Activity {
    private Button denglu;
    private TextView zhucezhanghao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhujiemian);
        denglu = (Button) findViewById(R.id.denglu);
       zhucezhanghao =(TextView) findViewById(R.id.zhucezhanghao);
        denglu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Main_activity.this, Tiaozhan_activity.class);
                startActivity(intent);
            }
        });
        zhucezhanghao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Main_activity.this,dengluzhuce_activity.class);
                startActivity(intent);

            }
        });

    }
}
