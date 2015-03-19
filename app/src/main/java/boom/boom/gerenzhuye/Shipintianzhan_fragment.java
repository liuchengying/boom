package boom.boom.gerenzhuye;

import android.app.Activity;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import boom.boom.R;

/**
 * Created by 刘成英 on 2015/3/12.
 */
public class Shipintianzhan_fragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.gerenzhuye1, container, false);
        ListView lv=(ListView)v.findViewById(R.id.listView2);
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,     Object>>();//*在数组中存放数据*//*
        for(int i=0;i<10;i++)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("title","斜立易拉罐");//加入图片            map.put("ItemTitle", "第"+i+"行");
            map.put("count", "这是第"+i+"行");
            listItem.add(map);
        }
        SimpleAdapter mSimpleAdapter = new SimpleAdapter(getActivity(),listItem,//需要绑定的数据
                R.layout.shipintiaozhan_item,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                new String[] {
                        "title", "count"},
                new int[] {R.id.title,R.id.count}
        );

        return v;
    }
}
   /* @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,     Object>>();*//*在数组中存放数据*//*
        for(int i=0;i<10;i++)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("title","斜立易拉罐");//加入图片            map.put("ItemTitle", "第"+i+"行");
            map.put("count", "这是第"+i+"行");
            listItem.add(map);
        }
        SimpleAdapter mSimpleAdapter = new SimpleAdapter(getActivity(),listItem,//需要绑定的数据
                R.layout.shipintiaozhan_item,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                 new String[] {
                "title", "count"},
    new int[] {R.id.title,R.id.count}
    );
        Activity activity = getActivity();
        ListView listView=(ListView)activity.findViewById(R.id.listView2);
        listView.setAdapter(mSimpleAdapter);
        //setListAdapter(mSimpleAdapter);
    }
}
*/