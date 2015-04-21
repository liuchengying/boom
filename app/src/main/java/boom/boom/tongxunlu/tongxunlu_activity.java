package boom.boom.tongxunlu;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.SysApplication;


import boom.boom.myview.SildingFinishLayout;

import static android.app.PendingIntent.getActivity;

/**
 * Created by Lyp on 2015/1/22.
 */
public class tongxunlu_activity extends Activity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tongxunlu);
        SildingFinishLayout mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.sildingFinishLayout);
        mSildingFinishLayout
                .setOnSildingFinishListener(new SildingFinishLayout.OnSildingFinishListener() {

                    @Override
                    public void onSildingFinish() {
                        tongxunlu_activity.this.finish();
                    }
                });

        mSildingFinishLayout.setTouchView(mSildingFinishLayout);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        ListView lv = (ListView) findViewById(R.id.tongxunlu_listview);

        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,     Object>>();
        for(int i=0;i<30;i++)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("picther",R.drawable.android_181);//加入图片            map.put("ItemTitle", "第"+i+"行");
            map.put("nicheng", "好友昵称");
            listItem.add(map);
        }
        SimpleAdapter mSimpleAdapter = new SimpleAdapter(this,listItem,//需要绑定的数据
                R.layout.tongxunlu_listview_item,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                new String[] {
                        "picther", "nicheng"},
                new int[] {R.id.tongxunlu_touxiang,R.id.tongxunlu_nicheng}
        );
        lv.setAdapter(mSimpleAdapter);


        LinearLayout fanhui= (LinearLayout) findViewById(R.id.txl_fh);
        fanhui.setOnClickListener(new View.OnClickListener() {
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
