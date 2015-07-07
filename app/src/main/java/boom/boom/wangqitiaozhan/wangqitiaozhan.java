package boom.boom.wangqitiaozhan;

/**
 * Created by Administrator on 2015/4/14.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import boom.boom.ExpandableTextView.ExpandableTextView;
import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.HttpIO;
import boom.boom.api.Static;
import boom.boom.api.SysApplication;
import boom.boom.api.Utils;
import boom.boom.guizejieshao.Guizejieshao_activity;

/**
 * Created by 刘成英 on 2015/1/16.
 */
public class wangqitiaozhan extends Activity{
    ExpandableListView expandableListView;
    List<itemData> group_list;
    List<List<itemData>> item_list;
    LinearLayout imageView;
    String str = null;
    RelativeLayout wqtz_fh;
    private class itemData{
        String frontname;
        int identifyDigit;
        itemData(String name, int digit)
        {
            frontname = name;
            identifyDigit = digit;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.wangqitianzhan);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        group_list = new ArrayList<itemData>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                    HttpIO io = new HttpIO(Utils.serveraddr + "api/getChallenge.php?action=gethistoryBySeries");
                    io.SessionID = Static.session_id;
                    io.GETToHTTPServer();
                    str = io.getResultData();

            }
        });
        wqtz_fh = (RelativeLayout) findViewById(R.id.wytz_fanhui);
        wqtz_fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        thread.start();
        while(str == null);
        try{
        JSONObject obj = new JSONObject(str);
        item_list = new ArrayList<List<itemData>>();
        if(obj.getString("state").equals("SUCCESS"))
        {
            int round = obj.getInt("limit");
            for(int i=1;i<round+1;i++) {
                JSONObject tmp = Utils.GetSubJSONObject(obj,""+i);
                group_list.add(new itemData(tmp.getString("nickname"),tmp.getInt("identifyDigit")));

                JSONObject perSeries = Utils.GetSubJSONObject(tmp,"data");
                int limit = perSeries.getInt("limit");
                ArrayList<itemData> strItem = new ArrayList<itemData>();
                for(int m=1;m<limit+1;m++){
                    try {
                        JSONObject item = Utils.GetSubJSONObject(perSeries, "" + m);
                        strItem.add(new itemData(item.getString("frontname"),item.getInt("identifyDigit")));
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                item_list.add(strItem);
            }
        }
    }catch (Exception e)
    {
        e.printStackTrace();
    }


        expandableListView=(ExpandableListView)findViewById(R.id.expand);
        expandableListView.setAdapter(new MyExpandableListViewAdapter(this));

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                itemData item = item_list.get(groupPosition).get(childPosition);
                Intent intent = new Intent();
                intent.putExtra("challenge_number",item.identifyDigit);
                intent.putExtra("ifFaqi",1);
                intent.setClass(wangqitiaozhan.this, Guizejieshao_activity.class);
                startActivity(intent);
                return false;
            }
        });
    }
    class MyExpandableListViewAdapter extends BaseExpandableListAdapter {

        private Context context;

        public MyExpandableListViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getGroupCount() {
            return group_list.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            int tmp =0;
            try {
                tmp = item_list.get(groupPosition).size();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return tmp;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return group_list.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return item_list.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            GroupHolder groupHolder = null;
            if (convertView == null) {
                convertView = (View) getLayoutInflater().from(context).inflate(
                        R.layout.wangqitianzhan_item1, null);
                groupHolder = new GroupHolder();
                groupHolder.txt = (TextView) convertView.findViewById(R.id.series);
                // groupHolder.img = (ImageView) convertView
                // .findViewById(R.id.img);
                convertView.setTag(groupHolder);
            } else {
                groupHolder = (GroupHolder) convertView.getTag();
            }
            groupHolder.txt.setText(group_list.get(groupPosition).frontname);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            /*ItemHolder itemHolder = null;
            if (convertView == null) {
                convertView = (View) getLayoutInflater().from(context).inflate(
                        R.layout.wangqitiaozhan_item2, null);
                itemHolder = new ItemHolder();
                itemHolder.txt = (TextView) convertView.findViewById(R.id.text_item);
                itemHolder.img=(LinearLayout)convertView.findViewById(R.id.wqtzitem2);
                convertView.setTag(itemHolder);
            } else {
                itemHolder = (ItemHolder) convertView.getTag();
            }
            itemHolder.txt.setText(item_list.get(groupPosition).get(
                    childPosition));
            if(isLastChild) {
                itemHolder.img.setBackground(getDrawable(R.drawable.android_184));
            }
            return convertView;*/
            View view = (View) getLayoutInflater().from(context).inflate(
                    R.layout.wangqitiaozhan_item2, null);
             imageView = (LinearLayout)view.findViewById(R.id.wqtzitem2);
            TextView textView = (TextView)view.findViewById(R.id.text_item);
            textView.setText(item_list.get(groupPosition).get(
                    childPosition).frontname);
            if(isLastChild)
            {
                imageView.setBackgroundResource(R.drawable.android_184);
            }
            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }

    class GroupHolder {
        public TextView txt;
        public ImageView img;
    }

    class ItemHolder {
        public LinearLayout img;
        public TextView txt;
    }
}
