package boom.boom.tingzhitiaozhan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import boom.boom.R;
import boom.boom.tianzhan.Tiaozhan_activity;

/**
 * Created by 刘成英 on 2015/1/20.
 */
public class Tingzhipaishe_activity extends Activity{
    private Button tingzhipaishefanhui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tingzhipaishe);
        tingzhipaishefanhui = (Button) findViewById(R.id.tingzhipaishefanhui);
        tingzhipaishefanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Tingzhipaishe_activity.this, Tiaozhan_activity.class);
                startActivity(intent);

            }
        });

    }

}
