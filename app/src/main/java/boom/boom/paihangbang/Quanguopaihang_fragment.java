package boom.boom.paihangbang;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.SimpleAdapter;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import boom.boom.R;
import boom.boom.api.HttpIO;
import boom.boom.api.Static;
import boom.boom.api.Utils;
import boom.boom.myview.XListView;

/**
 * Created by 刘成英 on 2015/3/12.
 */
public class Quanguopaihang_fragment extends Fragment implements XListView.IXListViewListener
{
    private XListView lv;
    private Handler mHandler;
    private SimpleAdapter mSimpleAdapter;
    private ArrayList<HashMap<String, Object>> listItem;
    private int loadedline=0;
    private final static String DATE_FORMAT_STR = "yyyy-MM-dd HH:mm";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.paihangbang2, container, false);
         lv= (XListView) v.findViewById(R.id.listView3);
        lv.setPullLoadEnable(true);
        mHandler = new Handler();


        onRefresh();
        return v;
    }
    private void onLoad() {
        lv.stopRefresh();
        lv.stopLoadMore();
        lv.setRefreshTime(new SimpleDateFormat(DATE_FORMAT_STR, Locale.CHINA).format(new Date()));
    }
    public void onRefresh(){

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //http://172.24.10.118/api/rank.php?action=board_hero
                String httpResult;
                Utils.GetBuilder getImageUrl = new Utils.GetBuilder(Utils.serveraddr +"api/rank.php");
                getImageUrl.addItem("action", "board_hero");
                getImageUrl.addItem("start", "1");
                getImageUrl.addItem("line","5");
                HttpIO io = new HttpIO(getImageUrl.toString());
                io.SessionID=Static.session_id;
                io.getJson();
                if(io.LastError==0) {
                    httpResult = io.getResultData();
                    try{
                        JSONObject obj= Utils.GetSubJSONObject(new JSONObject(httpResult),"response" );
                        listItem = new ArrayList<HashMap<String,     Object>>();//*在数组中存放数据*//*
                        if(obj.getString("state").equals("SUCCESS"))
                        {
                            int round = obj.getInt("limit");
                            for(int i=1;i<round+1;i++)
                            {
                                String nickname, count, address, committime, usedtime;
                                JSONObject line = Utils.GetSubJSONObject(new JSONObject(httpResult),""+i );
                                nickname = line.getString("winner_nickname");
                                count ="观看次数 " +line.getString("play_time")+ " 次";
                                address = line.getString("location_intent");
                                committime = line.getString("date");
                                usedtime = line.getString("elapsed_time")+"s";
                                HashMap<String, Object> map = new HashMap<String, Object>();
                                map.put("nickname",nickname);
                                map.put("count",count);
                                map.put("address",address);
                                map.put("commit",committime);
                                map.put("used",usedtime);
                                listItem.add(map);
                            }
                            //mSimpleAdapter.notifyDataSetChanged();
                            loadedline=round;
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                mSimpleAdapter = new SimpleAdapter(getActivity(),listItem,//需要绑定的数据
                        R.layout.quanguopaiming_item,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                        new String[] {
                                "nickname", "count", "address", "commit", "used"},
                        new int[] {R.id.qgph_nickname,R.id.qgph_count,R.id.qgph_addr,R.id.qgph_committime,R.id.qgph_usedtime}
                );

                lv.setPullLoadEnable(true);
                lv.setPullRefreshEnable(true);
                lv.setXListViewListener(Quanguopaihang_fragment.this);
                lv.setAdapter(mSimpleAdapter);
                onLoad();
            }
        }, 2000);
    }
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String httpResult;
                Utils.GetBuilder getImageUrl = new Utils.GetBuilder(Utils.serveraddr + "api/rank.php");
                getImageUrl.addItem("action", "board_hero");
                getImageUrl.addItem("start", ""+(loadedline+1));
                getImageUrl.addItem("line", "10");
                HttpIO io = new HttpIO(getImageUrl.toString());
                io.SessionID = Static.session_id;
                io.getJson();
                if (io.LastError == 0) {
                    httpResult = io.getResultData();
                    try {
                        JSONObject obj = Utils.GetSubJSONObject(new JSONObject(httpResult), "response");
                        //listItem = new ArrayList<HashMap<String, Object>>();//*在数组中存放数据*//*
                        if (obj.getString("state").equals("SUCCESS")) {
                            int round = obj.getInt("limit");
                            for (int i = 1; i < round + 1; i++) {
                                String nickname, count, address, committime, usedtime;
                                JSONObject line = Utils.GetSubJSONObject(new JSONObject(httpResult), "" + i);
                                nickname = line.getString("winner_nickname");
                                count = "观看次数 " + line.getString("play_time") + " 次";
                                address = line.getString("location_intent");
                                committime = line.getString("date");
                                usedtime = line.getString("elapsed_time") + "s";
                                HashMap<String, Object> map = new HashMap<String, Object>();
                                map.put("nickname", nickname);
                                map.put("count", count);
                                map.put("address", address);
                                map.put("commit", committime);
                                map.put("used", usedtime);
                                listItem.add(map);
                            }
                            mSimpleAdapter.notifyDataSetChanged();
                            loadedline += round;

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    onLoad();
                }
            }
        }, 2000);

    }
}