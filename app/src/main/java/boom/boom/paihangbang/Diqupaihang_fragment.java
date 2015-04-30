package boom.boom.paihangbang;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import boom.boom.R;
import boom.boom.myview.XListView;

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

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.paihangbang1, container, false);
        lv = (XListView) v.findViewById(R.id.listView4);
        lv.setPullLoadEnable(true);
        mHandler = new Handler();
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();//*在数组中存放数据*//*
        for (int i = 0; i < 10; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("title", "XXX");//加入图片            map.put("ItemTitle", "第"+i+"行");
            map.put("count", "这是第" + i + "行");
            listItem.add(map);
        }
        SimpleAdapter mSimpleAdapter = new SimpleAdapter(getActivity(), listItem,//需要绑定的数据
                R.layout.diqupaihang_item,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                new String[]{
                        "title", "count"},
                new int[]{R.id.title, R.id.count}
        );

        lv.setPullLoadEnable(true);
        lv.setPullRefreshEnable(true);
        lv.setXListViewListener(this);
        lv.setAdapter(mSimpleAdapter);


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