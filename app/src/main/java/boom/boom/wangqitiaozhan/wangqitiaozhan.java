package boom.boom.wangqitiaozhan;

/**
 * Created by Administrator on 2015/4/14.
 */
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import boom.boom.ExpandableTextView.ExpandableTextView;
import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.SysApplication;

/**
 * Created by 刘成英 on 2015/1/16.
 */
public class wangqitiaozhan extends Activity{
    ExpandableListView expandableListView;
    List<String> group_list;
    List<List<String>> item_list;
    List<List<String>> item_list2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wangqitianzhan);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        group_list = new ArrayList<String>();
        group_list.add("A");
        group_list.add("B");
        group_list.add("C");

        item_list = new ArrayList<List<String>>();
        item_list.add(group_list);
        item_list.add(group_list);
        item_list.add(group_list);
        expandableListView=(ExpandableListView)findViewById(R.id.expand);
        expandableListView.setAdapter(new MyExpandableListViewAdapter(this));
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
            return item_list.get(groupPosition).size();
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
            groupHolder.txt.setText(group_list.get(groupPosition));
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
            LinearLayout imageView = (LinearLayout)view.findViewById(R.id.wqtzitem2);
            TextView textView = (TextView)view.findViewById(R.id.text_item);
            textView.setText(item_list.get(groupPosition).get(
                    childPosition));
            if(isLastChild)
            {
                imageView.setBackground(getDrawable(R.drawable.android_184));
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
