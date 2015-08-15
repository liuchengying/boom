package boom.boom.gerenzhuye;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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


/**
 * Created by 刘成英 on 2015/3/12.
 */
public class Shipintianzhan_fragment extends Fragment implements XListView.IXListViewListener
{

    private XListView lv;
    private android.os.Handler mHandler;
    private final static String DATE_FORMAT_STR = "yyyy-MM-dd HH:mm";
    private SimpleAdapter mSimpleAdapter;
    private Button tianjiahaoyou_button;
    private String guestID;
    LinearLayout all;
    String result = null;
    ArrayList<HashMap<String, Object>> listItem= new ArrayList<HashMap<String,     Object>>();
    Handler asncHandler =new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == 1){
                listItem.clear();
                int round = 0;
                result = msg.getData().getString("data");
                try {
                    Gerenzhuye_activity.obj = new JSONObject(result);
                    JSONObject tmp = Utils.GetSubJSONObject(Gerenzhuye_activity.obj, "response");
                    round = Integer.parseInt(tmp.getString("limit"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for(int i=0;i<round;i++){
                    String title = null, text = null, location = null, assign_time = null, elapsed = null, cl_id = null,ID = null;
                    if (Gerenzhuye_activity.obj != null) try {
                        JSONObject tmp = Utils.GetSubJSONObject(Gerenzhuye_activity.obj, "line"+i);
                        title = tmp.getString("frontname");
                        text = "观看次数" + tmp.getString("play_time") + "次";
                        location = tmp.getString("location_intent");
                        JSONObject loc = new JSONObject(location);
                        location = Utils.locationlessthan5(loc.getString("province"),loc.getString("city"));
                        assign_time = tmp.getString("date");
                        elapsed = tmp.getString("elapsed_time");
                        cl_id = tmp.getString("challenge_id");
                        ID = tmp.getString("ID");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("title",title);
                    map.put("count", text);
                    map.put("location", location);
                    map.put("assign_time", assign_time);
                    map.put("elapsed", elapsed);
                    map.put("cl_id",cl_id);
                    map.put("ID",ID);
                    listItem.add(map);
                }
            }else{
                try {
                    Toast.makeText(getActivity(), "网络连接错误！请检查网络连接", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            mSimpleAdapter.notifyDataSetChanged();
            return true;
        }
    });
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.tianjiahaoyou1, container, false);
        lv= (XListView) v.findViewById(R.id.listView4);
        try {
            mSimpleAdapter = new SimpleAdapter(getActivity(), listItem,//需要绑定的数据
                    R.layout.shipintiaozhan_item,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                    new String[]{
                            "title", "count", "location", "assign_time", "elapsed"},
                    new int[]{R.id.title, R.id.count, R.id.location, R.id.assign_time, R.id.elapsed}
            );
        }catch (Exception e){
            e.printStackTrace();
        }
        guestID = getFragmentManager().findFragmentByTag("179521").getArguments().getString("guestID");
        lv.setPullLoadEnable(true);
        mHandler = new android.os.Handler();
        onSyncDataFromServer();
        lv.setPullLoadEnable(true);
        lv.setPullRefreshEnable(true);
        lv.setXListViewListener(this);
        lv.setAdapter(mSimpleAdapter);
        lv.mContext = getActivity();
        lv.transit = (ViewGroup) getActivity().findViewById(R.id.shangbanbufen);
        lv.transit_top = (ViewGroup) getActivity().findViewById(R.id.transit_top);

        lv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:


                    case MotionEvent.ACTION_MOVE:

                    case  MotionEvent.ACTION_UP:

                }
                return false;
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent localIntent = new Intent(getActivity(), Shipintianzhan_pinglun.class);
                int pos = position-1;
                localIntent.putExtra("guestID",guestID);
                localIntent.putExtra("ID",(String)listItem.get(pos).get("ID"));
                localIntent.putExtra("cl_id",(String)listItem.get(pos).get("cl_id"));
                localIntent.putExtra("count",(String)listItem.get(pos).get("count"));
                localIntent.putExtra("date",(String)listItem.get(pos).get("assign_time"));
                localIntent.putExtra("elapsed",(String)listItem.get(pos).get("elapsed"));
                localIntent.putExtra("nickname",((Gerenzhuye_activity)getActivity()).nickname);
                localIntent.putExtra("cl_name",(String)listItem.get(pos).get("title"));
                startActivity(localIntent);
                getActivity().overridePendingTransition(R.anim.base_slide_right_in,R.anim.base_slide_remain);
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
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onSyncDataFromServer();
                onLoad();
            }
        });

    }

    @Override
    public void onLoadMore() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                onLoad();
            }
        });

    }

    public void onSyncDataFromServer(){
        listItem.clear();
                result = null;
                //http://172.24.10.118/api/rank.php?action=getFriendsrank&guest_id=10000
                Utils.GetBuilder get = new Utils.GetBuilder(Utils.serveraddr + "/api/rank.php");
                get.addItem("action", "getFriendsrank");
                get.addItem("guest_id", guestID);
                HttpIO.GetHttpEX(getActivity(),asncHandler,get.toString());

    }
}