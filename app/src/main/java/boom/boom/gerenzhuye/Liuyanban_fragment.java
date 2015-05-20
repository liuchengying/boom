package boom.boom.gerenzhuye;

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
import android.widget.PopupWindow;
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
import boom.boom.myview.XListView;

/**
 * Created by 刘成英 on 2015/3/12.
 */
public class Liuyanban_fragment extends Fragment implements XListView.IXListViewListener
{
    private XListView lv;
    private Handler mHandler;
    private final static String DATE_FORMAT_STR = "yyyy-MM-dd HH:mm";
    private SimpleAdapter mSimpleAdapter;
    private Button button;
    private Button confirmButton;
    private Button cancleButton;
    private PopupWindow popupWindow;
    private View popupWindowView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.gerenzhuye2, container, false);
        lv= (XListView) v.findViewById(R.id.listView3);
        lv.setPullLoadEnable(true);
        mHandler = new Handler();
        this.onSyncDataFromServer();
        lv.setPullLoadEnable(true);
		lv.setPullRefreshEnable(true);
        lv.setXListViewListener(this);
        lv.setAdapter(mSimpleAdapter);


        popupWindowView = inflater.inflate(R.layout.shezhi_touxiang, null);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow = new PopupWindow(popupWindowView);
                popupWindow.setWidth(LinearLayout.LayoutParams.FILL_PARENT);
                popupWindow.setHeight(LinearLayout.LayoutParams.FILL_PARENT);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);

                //设置PopupWindow的弹出和消失效果
                popupWindow.setAnimationStyle(R.style.popupAnimation);


                cancleButton = (Button) popupWindowView.findViewById(R.id.liuyanban_cencle);
                button = (Button) popupWindowView.findViewById(R.id.shanchuliuyan);
                popupWindow.showAtLocation(confirmButton, Gravity.CENTER, 0, 0);
                confirmButton.setOnClickListener(Itemclick);

                button.setOnClickListener(Itemclick);
                return true;
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
        onSyncDataFromServer();
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
    private View.OnClickListener Itemclick = new View.OnClickListener() {
        @Override
        public void onClick( View v) {
            if(popupWindow.isShowing()){
                popupWindow.dismiss();
            }
            switch (v.getId()) {
                case R.id.shanchuliuyan:

                    break;
                case R.id.liuyanban_cencle:

                    break;
                default:
                    break;
            }
        }



    };


    public void onSyncDataFromServer(){
        String challenge_name = null, challenge_nickname = null;
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,     Object>>();//*在数组中存放数据*//*
        Utils.GetBuilder get = new Utils.GetBuilder(Utils.serveraddr + "/api/comment.php");
        get.addItem("action", "query");
        get.addItem("type", "1");
        HttpIO io = new HttpIO(get.toString());
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
//        JSONObject obj_new = Challenge.getChallengeByIdentify()
        for(int i=0;i<round;i++){
            String title = null, text = null, like = null, comment = null;
            if (Gerenzhuye_activity.obj != null) try {
                JSONObject tmp = Utils.GetSubJSONObject(Gerenzhuye_activity.obj, "line"+i);
//                title = tmp.getString("frontname");
                text = tmp.getString("text_value");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("title", Static.nickname);//加入图片            map.put("ItemTitle", "第"+i+"行");
            map.put("text", text);
            map.put("like", like);
            map.put("comment", comment);
            listItem.add(map);
        }
        mSimpleAdapter = new SimpleAdapter(getActivity(),listItem,//需要绑定的数据
                R.layout.liuyanban_item,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                new String[] {
                        "title", "count"},
                new int[] {R.id.title,R.id.count}
        );
    }
}