package boom.boom.xinxizhongxin;



import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
        while (true) {
            Msg msg = new Msg();
            Map<String, Object> map = new HashMap<String, Object>();
            if (msg.LastError == 0) {
                map = msg.GetSimpleMap();
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
        );
        list.setAdapter(new ExpandableAdapter(this));
        // ExpandableAdapter.setListViewHeightBasedOnChildren(list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ExpandableTextView e=(ExpandableTextView)view.findViewById(R.id.expand_text_view);
                    e.onClick(view);
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
    private final String[] sampleStrings;

    public ExpandableAdapter(Context context) {
        mContext  = context;
        mCollapsedStatus = new SparseBooleanArray();
        sampleStrings=new String[20];
        for(int i=0;i<20;i++)
        {
            sampleStrings[i]="haskdjhaskdhfaskdfhksadhfklasdhkasdhfksjhdfksahdkfhasflksahfk\nkajhkajhsdkahsdkasdjaksdhak\najsdlajsdljasl\n\n\nawsdjhaskd";
        }
        //sampleStrings = mContext.getResources().getStringArray(R.array.sampleStrings);
    }

    @Override
    public int getCount() {
        return sampleStrings.length;
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
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.xiaoxixiang, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.expandableTextView = (ExpandableTextView) convertView.findViewById(R.id.expand_text_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.expandableTextView.setText(sampleStrings[position], mCollapsedStatus, position);

        return convertView;
    }


    private static class ViewHolder{
        ExpandableTextView expandableTextView;
    }

}