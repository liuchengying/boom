package boom.boom.xinxizhongxin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import boom.boom.R;

/**
 * Created by Lyp on 2015/1/22
 */

public class xinxizhongxin_activity extends Activity {
    @Override
      protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xinxizhongxin);
        Button fh=(Button)findViewById(R.id.xxzx_fanhui);
        Button rightSideButton;
        fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
