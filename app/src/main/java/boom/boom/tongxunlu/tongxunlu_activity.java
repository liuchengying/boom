package boom.boom.tongxunlu;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.SysApplication;


import boom.boom.mimaxiugai.Mimaxiugai_activity;
import boom.boom.myview.SildingFinishLayout;

import static android.app.PendingIntent.getActivity;

/**
 * Created by Lyp on 2015/1/22.
 */
public class tongxunlu_activity extends Activity{
    LinearLayout horizon;


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

        horizon=(LinearLayout)findViewById(R.id.horizon);


        ArrayList<String> listItem = new ArrayList<>();
        for(int i=0;i<30;i++)
        {
            listItem.add("老"+i);
        }
        final MyAdapter mSimpleAdapter=new MyAdapter(listItem,this);
       /* SimpleAdapter mSimpleAdapter = new SimpleAdapter(this,listItem,//需要绑定的数据
                R.layout.tongxunlu_listview_item,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                new String[] {
                        "picther", "nicheng"},
                new int[] {R.id.tongxunlu_touxiang,R.id.tongxunlu_nicheng}
        );*/
        lv.setAdapter(mSimpleAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSimpleAdapter.select(position);
            }
        });

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


    public class MyAdapter extends BaseAdapter {
        // 填充数据的list
        private ArrayList<String> list;
        // 用来控制CheckBox的选中状况
        private HashMap<Integer,Boolean> isSelected;

        // 上下文
        private Context context;
        // 用来导入布局
        private LayoutInflater inflater = null;

        // 构造器
        public MyAdapter(ArrayList<String> list, Context context) {
            this.context = context;
            this.list = list;
            inflater = LayoutInflater.from(context);
            isSelected = new HashMap<Integer, Boolean>();
            // 初始化数据
            initDate();

        }

        // 初始化isSelected的数据
        private void initDate(){
            for(int i=0; i<list.size();i++) {
                getIsSelected().put(i,false);
            }
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        void selectedChanged()
        {
            horizon.removeAllViews();
            for(int i=0;i<getIsSelected().size();i++)
            {
                if(getIsSelected().get(i)){
                    ImageView imageView=new ImageView(tongxunlu_activity.this);
                    imageView.setImageResource(R.drawable.android_181);
                    imageView.setPadding(10,0,0,0);
                    horizon.addView(imageView);
                }
            }
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                // 获得ViewHolder对象
                holder = new ViewHolder();
                // 导入布局并赋值给convertview
                convertView = inflater.inflate(R.layout.tongxunlu_listview_item, null);
                holder.tv = (TextView) convertView.findViewById(R.id.tongxunlu_nicheng);
                holder.cb = (CheckBox) convertView.findViewById(R.id.tongxunlu_check);
                // 为view设置标签
                convertView.setTag(holder);
            } else {
                // 取出holder
                holder = (ViewHolder) convertView.getTag();
            }
            // 设置list中TextView的显示
            holder.tv.setText(list.get(position));
            // 根据isSelected来设置checkbox的选中状况
            holder.cb.setChecked(getIsSelected().get(position));
            return convertView;
        }

        public HashMap<Integer,Boolean> getIsSelected() {
            return isSelected;
        }

        public void select(int position) {
            getIsSelected().put(position,!getIsSelected().get(position));
            notifyDataSetChanged();
            selectedChanged();
        }
class ViewHolder
{
            TextView tv;
            CheckBox cb;
}
    }//MyAdapter
}//tongxunlu_activity
