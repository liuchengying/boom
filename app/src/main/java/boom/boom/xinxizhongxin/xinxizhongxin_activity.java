package boom.boom.xinxizhongxin;



import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.Msg;
import boom.boom.api.SysApplication;
import boom.boom.ExpandableTextView.ExpandableTextView;
import boom.boom.myview.SildingFinishLayout;
import boom.boom.myview.XListView;

/**
 * Created by Lyp on 2015/1/22
 */

public class xinxizhongxin_activity extends Activity implements XListView.IXListViewListener{
    private XListView list;
    private final static String DATE_FORMAT_STR = "yyyy-MM-dd HH:mm";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.xinxizhongxin);
        SildingFinishLayout mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.sildingFinishLayout);
        mSildingFinishLayout
                .setOnSildingFinishListener(new SildingFinishLayout.OnSildingFinishListener() {

                    @Override
                    public void onSildingFinish() {
                        xinxizhongxin_activity.this.finish();
                    }
                });

        mSildingFinishLayout.setTouchView(mSildingFinishLayout);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        LinearLayout fh = (LinearLayout) findViewById(R.id.xxzx_fanhui);


//        Button rightSideButton;
        final ArrayList<Map<String, Object>> listItem = new ArrayList<>();
        list = (XListView) findViewById(R.id.challenge_list);
        list.setPullLoadEnable(true);
        list.setPullRefreshEnable(true);
        list.setXListViewListener(this);
        /*while (true) {
            Msg msg = new Msg();
            Map<String, Object> map = new HashMap<String, Object>();
            if (msg.LastError == 0) {
                //map = msg.GetSimpleMap();
                if (map == null) {
                    break;
                }
                listItem.add(map);
            } else {
                Toast.makeText(getApplicationContext(), "数据错误！", Toast.LENGTH_LONG).show();
                break;
            }
            map.put("label", "1");
            map.put("text", "2");
            listItem.add(map);
        }
        SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,
                R.layout.msgcenter_listitem,
                new String[]{"label", "text"},
                new int[]{R.id.msg_center_list_label, R.id.msg_center_list_text}
        );*/
        Msg msg = new Msg();
        ArrayList<HashMap<String,Object>> arrayList = new ArrayList<>();
        arrayList = msg.GetList(false,this);
        final ExpandableAdapter adapter = new ExpandableAdapter(this,arrayList);
        list.setAdapter(adapter);
        // ExpandableAdapter.setListViewHeightBasedOnChildren(list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int type = (int) adapter.list.get(position-1).get("type");
                switch (type) {
                    case 4:
                    ExpandableTextView e = (ExpandableTextView) view.findViewById(R.id.expand_text_view);
                    e.onClick(view);
                        break;
                }
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "删除、回信功能尚未实现，敬请期待。", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, R.anim.base_slide_right_out);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }
    private void onLoad() {
        list.stopRefresh();
        list.stopLoadMore();
        list.setRefreshTime(new SimpleDateFormat(DATE_FORMAT_STR, Locale.CHINA).format(new Date()));
    }
    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
    class ExpandableAdapter extends BaseAdapter {

    private final Context mContext;
    private final SparseBooleanArray mCollapsedStatus;
    //private final String[] sampleStrings;
    public ArrayList<HashMap<String,Object>> list;
    public ExpandableAdapter(Context context,ArrayList<HashMap<String,Object>> arrlist) {
        mContext  = context;
        mCollapsedStatus = new SparseBooleanArray();
        list = arrlist;
        //sampleStrings = mContext.getResources().getStringArray(R.array.sampleStrings);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        HashMap<String,Object> hashMap = list.get(position);
        int type = (int) hashMap.get("type");
        switch (type) {
            case 4:
                view = LayoutInflater.from(mContext).inflate(R.layout.xiaoxixiang, parent, false);
                ExpandableTextView expandableTextView = (ExpandableTextView) view.findViewById(R.id.expand_text_view);
                expandableTextView.setText(((String) hashMap.get("content")), mCollapsedStatus, position);
                TextView title = (TextView) view.findViewById(R.id.xxx_title);
                TextView date = (TextView) view.findViewById(R.id.xxx_time);
                ImageView icon = (ImageView) view.findViewById(R.id.xxx_cursor);
                title.setText((String) hashMap.get("title"));
                date.setText((String) hashMap.get("date"));
                icon.setImageBitmap((Bitmap) hashMap.get("icon"));
                break;
            case 1:
            case 2:
            case 3:
            case 5:
            case 6:
            case 7:
                view = LayoutInflater.from(mContext).inflate(R.layout.xiaoxizhongxin_item2,parent,false);
                TextView title2 = (TextView) view.findViewById(R.id.xxzx_item_title);
                TextView text2 = (TextView) view.findViewById(R.id.xxzx_item_text);
                TextView date2 = (TextView) view.findViewById(R.id.xxzx_item_date);
                ImageView icon2 = (ImageView) view.findViewById(R.id.xxzx_item_icon);
                ImageView smallicon = (ImageView) view.findViewById(R.id.xxzx_item_smallicon);
                title2.setText((String)hashMap.get("title"));
                text2.setText((String)hashMap.get("content"));
                date2.setText((String)hashMap.get("date"));
                icon2.setImageBitmap((Bitmap) hashMap.get("icon"));
                Bitmap bmSmallicon = (Bitmap) hashMap.get("smallicon");
                if(bmSmallicon == null){
                    smallicon.setVisibility(View.INVISIBLE);
                }else
                {
                    smallicon.setImageBitmap(bmSmallicon);
                }
        }
        return view;
    }


    private static class ViewHolder{
        ExpandableTextView expandableTextView;
        TextView title;
        ImageView icon;
        TextView date;
    }

}