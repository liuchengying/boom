package boom.boom.bianjixinxi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import boom.boom.R;
import boom.boom.api.SysApplication;
import boom.boom.shezhi.Shezhi_activity;
import boom.boom.zhujiemian.Main_activity;

/**
 * Created by 刘成英 on 2015/3/19.
 */
public class Bianjixinxi_activity  extends Activity{
    private List<String> list1 = new ArrayList<String>();
    private List<String> list = new ArrayList<String>();

    private Spinner mySpinner;
    private Spinner      MySpinner1;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bianjiziliao);
        SysApplication.getInstance().addActivity(this);
        LinearLayout fanhui = (LinearLayout)findViewById(R.id.bianjixinxi_fh);

        list.add("男");
        list.add("女");
        mySpinner = (Spinner)findViewById(R.id.spinner);
         adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        mySpinner.setAdapter(adapter);
        MySpinner1 = (Spinner)findViewById(R.id.spinner1);





        list1.add("摩羯座");
        list1.add("水瓶座");
        list1.add("双鱼座");
        list1.add("白羊座");
        list1.add("金牛座");
        list1.add("双子座");
        list1.add("巨蟹座");
        list1.add("狮子座");
        list1.add("处女座");
        list1.add("天枰座");
        list1.add("天蝎座");
        list1.add("射手座");
        adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list1);
        adapter1.setDropDownViewResource(android.R.layout.select_dialog_item);
        MySpinner1.setAdapter(adapter1);

        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
