package boom.boom.haoyouliebiao;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.SysApplication;
import boom.boom.myview.SildingFinishLayout;

/**
 * Created by lcy on 2015/4/29.
 */
public class Haoyouliebiao_activity extends Activity {
    ListView lv;
    LinearLayout haoyouliebiao_fh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.haoyouliebiao);
        SildingFinishLayout mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.sildingFinishLayout);
        mSildingFinishLayout
                .setOnSildingFinishListener(new SildingFinishLayout.OnSildingFinishListener() {
                    @Override
                    public void onSildingFinish() {
                        Haoyouliebiao_activity.this.finish();
                    }
                });

        mSildingFinishLayout.setTouchView(mSildingFinishLayout);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        lv = (ListView) findViewById(R.id.haoyouliebiao_listview);
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();//*在数组中存放数据*//*
        for (int i = 0; i < 10; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("title", R.drawable.android_181);//加入图片            map.put("ItemTitle", "第"+i+"行");
            map.put("count", "这是第" + i + "个好友");
            listItem.add(map);
        }
        SimpleAdapter mSimpleAdapter = new SimpleAdapter(this, listItem,//需要绑定的数据
                R.layout.haoyouliebiao_item_down,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                new String[]{
                        "title", "count"},
                new int[]{R.id.haoyouliebiao_touxiang, R.id.haoyouliebiao_nicheng}
        );

        lv.setAdapter(mSimpleAdapter);
        haoyouliebiao_fh = (LinearLayout) findViewById(R.id.haoyouliebiao_fh);
        haoyouliebiao_fh.setOnClickListener(new View.OnClickListener() {
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
