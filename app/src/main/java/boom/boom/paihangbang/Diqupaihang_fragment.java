package boom.boom.paihangbang;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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
import boom.boom.shipintianzhanpinglun.Shipinpinglun_fragment;

/**
 * Created by 刘成英 on 2015/3/12.
 */
public class Diqupaihang_fragment extends Fragment implements XListView.IXListViewListener {
    private XListView lv;
    private Handler mHandler;
    private final static String DATE_FORMAT_STR = "yyyy-MM-dd HH:mm";
//    private Button button;
//    private Button confirmButton;
//    private Button cancleButton;
//    private PopupWindow popupWindow;
//    private View popupWindowView;
    private ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,     Object>>();;
    private SimpleAdapter mSimpleAdapter;
    private int loadedline=0;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.paihangbang1, container, false);
        lv = (XListView) v.findViewById(R.id.listView4);
        lv.setPullLoadEnable(true);
        mHandler = new Handler();


        lv.setPullLoadEnable(true);
        lv.setPullRefreshEnable(true);
        lv.setXListViewListener(this);

        onRefresh();
        mSimpleAdapter = new SimpleAdapter(getActivity(), listItem,//需要绑定的数据
                R.layout.quanguopaiming_item,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                new String[]{
                        "nickname", "count", "address", "commit", "used"},
                new int[]{R.id.qgph_nickname, R.id.qgph_count, R.id.qgph_addr, R.id.qgph_committime, R.id.qgph_usedtime}
        );
        lv.setAdapter(mSimpleAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("ID", (String) listItem.get(i-1).get("ID"));
                intent.setClass(getActivity(), Shipinpinglun_fragment.class);
                startActivity(intent);
            }
        });


//        popupWindowView = inflater.inflate(R.layout.shezhi_touxiang, null);
//        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent,  View view, int position, long id) {
////                Toast toast = Toast.makeText(getActivity(), "adfadsfasdfasdfadfadsf",
////                        Toast.LENGTH_LONG);
////                toast.show();
//                popupWindow = new PopupWindow(popupWindowView);
//                popupWindow.setWidth(LinearLayout.LayoutParams.FILL_PARENT);
//                popupWindow.setHeight(LinearLayout.LayoutParams.FILL_PARENT);
//                // popupWindowView = inflater.inflate(R.layout.shezhi_touxiang, null);
////        popupWindow = new PopupWindow(popupWindowView, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, true);
//                popupWindow.setBackgroundDrawable(new BitmapDrawable());
//                popupWindow.setOutsideTouchable(true);
//                popupWindow.setFocusable(true);
//
//                //设置PopupWindow的弹出和消失效果
//                 popupWindow.setAnimationStyle(R.style.popupAnimation);
//                confirmButton = (Button) popupWindowView.findViewById(R.id.sz_touxiang_paishe);
//
//                cancleButton = (Button) popupWindowView.findViewById(R.id.cancleButton);
//                button = (Button) popupWindowView.findViewById(R.id.sz_touxiang_bendi);
//                popupWindow.showAtLocation(confirmButton, Gravity.CENTER, 0, 0);
//                confirmButton.setOnClickListener(Itemclick);
//                cancleButton.setOnClickListener(Itemclick);
//                button.setOnClickListener(Itemclick);
//                return true;
//            }
//        });
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
                try {
                    listItem.clear();
                    String httpResult;
                    Utils.GetBuilder getImageUrl = new Utils.GetBuilder(Utils.serveraddr + "api/rank.php");
                    getImageUrl.addItem("action", "board_hero");
                    getImageUrl.addItem("location", Static.city);
                    getImageUrl.addItem("start", "1");
                    getImageUrl.addItem("line", "5");
                    final HttpIO io = new HttpIO(getImageUrl.toString());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            io.SessionID = Static.session_id;
                            io.getJson();
                        }
                    }).start();
                    while(io.getResultData() == null);
                    if (io.LastError == 0) {
                        httpResult = io.getResultData();
                        try {
                            JSONObject obj = Utils.GetSubJSONObject(new JSONObject(httpResult), "response");

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
                                    map.put("ID",line.getString("ID"));
                                    listItem.add(map);
                                }
                                //mSimpleAdapter.notifyDataSetChanged();
                                loadedline = round;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    onLoad();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onLoadMore() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                String httpResult;
                Utils.GetBuilder getImageUrl = new Utils.GetBuilder(Utils.serveraddr + "api/rank.php");
                getImageUrl.addItem("action", "board_hero");
                getImageUrl.addItem("location",Static.city);
                getImageUrl.addItem("start", "" + (loadedline + 1));
                getImageUrl.addItem("line", "10");
                final HttpIO io = new HttpIO(getImageUrl.toString());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        io.SessionID = Static.session_id;
                        io.getJson();
                    }
                }).start();
                while(io.getResultData() == null);
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
        });


    }
//    private View.OnClickListener Itemclick = new View.OnClickListener() {
//        @Override
//        public void onClick( View v) {
//           if(popupWindow.isShowing()){
//               popupWindow.dismiss();
//           }
//
//
//
//
//                switch (v.getId()) {
//                    case R.id.sz_touxiang_paishe:
////                        popupWindow.dismiss();
//                        break;
//                    case R.id.sz_touxiang_bendi:
////                        popupWindow.dismiss();
//                        break;
//                    default:
//                        break;
//                }
//            }
//
//
//
//    };
}