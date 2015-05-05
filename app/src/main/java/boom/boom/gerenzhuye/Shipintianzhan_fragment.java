package boom.boom.gerenzhuye;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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
import boom.boom.myview.XListView;
import boom.boom.shipintianzhanpinglun.Shipintianzhan_pinglun;
import boom.boom.zinitiaozhan.Zinitianzhan_activity;


/**
 * Created by 刘成英 on 2015/3/12.
 */
public class Shipintianzhan_fragment extends Fragment implements XListView.IXListViewListener
{

    private XListView lv;
    private android.os.Handler mHandler;
    private final static String DATE_FORMAT_STR = "yyyy-MM-dd HH:mm";
    private SimpleAdapter mSimpleAdapter;
    private String resultdata = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.gerenzhuye1, container, false);
         lv= (XListView) v.findViewById(R.id.listView2);
        lv.setPullLoadEnable(true);
        mHandler = new android.os.Handler();
        onSyncDataFromServer();

        lv.setPullLoadEnable(true);
        lv.setPullRefreshEnable(true);
        lv.setXListViewListener(this);
        lv.setAdapter(mSimpleAdapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),"adsfadsfasdfasdfasdfasdf!",Toast.LENGTH_SHORT).show();
                Intent localIntent=new Intent(getActivity(),Shipintianzhan_pinglun.class);
                startActivity(localIntent);

            }
        });
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
/*
    Handler http_receiver = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            resultdata = bundle.getString("result");
        }
    };
*/

    public void onSyncDataFromServer(){
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,     Object>>();/*在数组中存放数据*/
        Gerenzhuye_activity.obj = null;
        int round = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Utils.GetBuilder get = new Utils.GetBuilder(Utils.serveraddr + "/api/rank.php");
                get.addItem("action", "getrank");
                HttpIO io = new HttpIO(get.toString());
                io.SetCustomSessionID(Static.session_id);
                io.GETToHTTPServer();
                resultdata = io.getResultData();
//                Bundle bundle = new Bundle();
//                bundle.putString("result", result);
//                Message m = new Message();
//                m.setData(bundle);
//                m.what = 1;
//                Shipintianzhan_fragment.this.http_receiver.sendMessage(m);
            }
        }).start();
        while (resultdata == null);
        try {
            Gerenzhuye_activity.obj = new JSONObject(this.resultdata);
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
        );
        }
}