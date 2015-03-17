package boom.boom.zhucechenggong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import boom.boom.R;
import boom.boom.api.SysApplication;
import boom.boom.wanshanxinxi.Wanshanxinxi_activity;

/**
 * Created by 刘成英 on 2015/1/15.
 */
public class Zhucechenggong_activity extends Activity {
    private Button wanshanxinxi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhucechenggong);
        SysApplication.getInstance().addActivity(this);
        wanshanxinxi=(Button)findViewById(R.id.wanshanxinxi);
        wanshanxinxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Zhucechenggong_activity.this, Wanshanxinxi_activity.class);
                startActivity(intent);
            }
        });
    }
}
