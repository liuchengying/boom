package boom.boom.tianjiahaoyou;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import boom.boom.R;
import boom.boom.api.HttpIO;
import boom.boom.api.Static;
import boom.boom.api.Utils;
import boom.boom.gerenzhuye.Gerenzhuye_activity;
import boom.boom.myview.XListView;


/**
 * Created by 刘成英 on 2015/3/12.
 */
public class Shipintianzhan_fragment_tjhy extends Fragment implements XListView.IXListViewListener
{

    private XListView lv;
    private android.os.Handler mHandler;
    private final static String DATE_FORMAT_STR = "yyyy-MM-dd HH:mm";
    private SimpleAdapter mSimpleAdapter;
    private Button tianjiahaoyou_button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.tianjiahaoyou1, container, false);
         lv= (XListView) v.findViewById(R.id.listView4);

        lv.setPullLoadEnable(true);
        mHandler = new android.os.Handler();
        onSyncDataFromServer();
        lv.setPullLoadEnable(true);
        lv.setPullRefreshEnable(true);
        lv.setXListViewListener(this);
        lv.setAdapter(mSimpleAdapter);
        return v;
    }
    private void onLoad() {
        lv.stopRefresh();
        lv.stopLoadMore();
        lv.setRefreshTime(new SimpleDateFormat(DATE_FORMAT_STR, Locale.CHINA).format(new Date()));
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                onLoad();
            }
        }, 2000);

    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                onLoad();
            }
        }, 2000);

    }

    public void onSyncDataFromServer(){
/*        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,     Object>>();*//*在数组中存放数据*//*
        Utils.GetBuilder get = new Utils.GetBuilder(Utils.serveraddr + "/api/rank.php");
        get.addItem("action", "getrank");
        HttpIO io = new HttpIO(get.toString());
        io.SetCustomSessionID(Static.session_id);
        Gerenzhuye_activity.obj = null;
        int round = 0;
        io.GETToHTTPServer();
        try {
            Gerenzhuye_activity.obj = new JSONObject(io.getResultData());
            JSONObject tmp = Utils.GetSubJSONObject(Gerenzhuye_activity.obj, "response");
            round = Integer.parseInt(tmp.getString("limit"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(int i=0;i<round;i++){
            String title = null, text = null, location = null, assign_time = null, elapsed = null;
            if (Gerenzhuye_activity.obj != null) try {
                JSONObject tmp = Utils.GetSubJSONObject(Gerenzhuye_activity.obj, "line"+i);
                title = tmp.getString("frontname");
                text = "观看次数" + tmp.getString("play_time") + "次";
                location = tmp.getString("location_intent");
                assign_time = tmp.getString("date");
                elapsed = tmp.getString("elapsed_time");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("title",title);
            map.put("count", text);
            map.put("location", location);
            map.put("assign_time", assign_time);
            map.put("elapsed", elapsed);
            listItem.add(map);
        }
        mSimpleAdapter = new SimpleAdapter(getActivity(),listItem,//需要绑定的数据
                R.layout.shipintiaozhan_item,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                new String[] {
                        "title", "count" , "location", "assign_time", "elapsed"},
                new int[] {R.id.title,R.id.count,R.id.location,R.id.assign_time,R.id.elapsed}
        );*/
        }
}