package boom.boom.liuyanbanpinglun;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.AsyncLoadAvatar;
import boom.boom.api.HttpIO;
import boom.boom.api.Static;
import boom.boom.api.SysApplication;
import boom.boom.api.Utils;
import boom.boom.liuyanhuifu.Liuyanhuifu_activity;
import boom.boom.myview.RoundedImageView;
import boom.boom.myview.SildingFinishLayout;

/**
 * Created by Administrator on 2015/7/11.
 */
public class liuyanpinglun_fragment extends Fragment {
    private ListView lv;
    private RelativeLayout liuyanban_fh;
    private Button liuyan;
    private String ID;
    private SimpleAdapter mSimpleAdapter;
    private String nickname;
    private String avatar;
    private String text;
    private String time;
    private TextView nickname_tv;
    private TextView text_tv;
    private TextView time_tv;
    private boom.boom.myview.RoundedImageView avatarImage;
    private LinearLayout lypl_horizon;
    ToggleButton mTogBtn;
    private ArrayList<String> avatarlist;
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    List<Map<String , Object>> listItem = new ArrayList<Map<String,Object>>();
    android.os.Handler myMessageHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mSimpleAdapter.notifyDataSetChanged();
            LoadImage();
            setListViewHeightBasedOnChildren(lv);
        }
    };
    private void LoadImage (){
        lypl_horizon.removeAllViews();
        for(int i=0;i<avatarlist.size();i++){
            RoundedImageView imageView=new RoundedImageView(getActivity());
            imageView.setImageResource(R.drawable.android_181);
            imageView.setPadding(10, 0, 0, 0);
            imageView.setId(i);
            lypl_horizon.addView(imageView);
            Drawable avatar;
            final String data = avatarlist.get(i);
            if ((avatar = AsyncLoadAvatar.GetLocalImage(getActivity(),(String) data)) == null)           //获取存在本地的Bitmap
            {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (AsyncLoadAvatar.SaveBitmapToLocal(AsyncLoadAvatar.DownloadBitmap((String) data), (String) data));  //returned Bitmap   把Bitmap保存到本地
                        {
                            Message msg = new Message();
                            myMessageHandler.sendMessage(msg);
                        }
                    }
                });
                thread.start();
                imageView.setImageResource(R.drawable.android_181);
            } else {
                imageView.setImageBitmap(Utils.zoomImage(Utils.drawableToBitmap(avatar),60,70));
            }

        }
    }
    private void LoadData (){
        try {
            listItem.clear();
            final HttpIO io = new HttpIO(Utils.serveraddr + "/api/comment.php?action=getsingle&id=" + ID);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    io.SessionID = Static.session_id;
                    io.GETToHTTPServer();
                }
            }).start();
            while(io.getResultData() == null);
            String result = io.getResultData();
            JSONObject obj = new JSONObject(result);
            try {
                JSONObject data = Utils.GetSubJSONObject(obj, "data");
                text = data.getString("text_value");
                JSONObject host_user = Utils.GetSubJSONObject(obj, "host_user");
                nickname = host_user.getString("nickname");
                avatar = host_user.getString("avatar");
                time = data.getString("assign_date");
                int liked = data.getInt("if_i_like");
                mTogBtn.setChecked(liked>0);
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                JSONObject heart_like = Utils.GetSubJSONObject(obj,"all_heart_like_record");
                int i=0;
                JSONObject line = null;
                avatarlist = new ArrayList<>();
                while((line = Utils.GetSubJSONObject(heart_like,"line"+i))!=null){
                    String avatar = line.getString("avatar");
                    avatarlist.add(avatar);
                    i++;
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            LoadImage();
            JSONObject refer_data = null;
            try {
                refer_data = Utils.GetSubJSONObject(obj, "refer_data");
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            int i = 0;
            JSONObject line;
            while((line = Utils.GetSubJSONObject(refer_data,"line"+i))!=null){
                HashMap<String,Object> map = new HashMap<>();
                map.put("ID",line.getString("ID"));
                map.put("text",line.getString("text_value"));
                map.put("date",line.getString("assign_date"));
                String positionID = line.getString("identifyDigit");
                map.put("nickname",(positionID.equals(Static.identifyDigit))?"我":line.getString("nickname"));
                map.put("avatar",line.getString("avatar"));
                listItem.add(map);
                i++;
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.liuyanpinglun_frame,container,false);
        /*SildingFinishLayout mSildingFinishLayout = (SildingFinishLayout) v.findViewById(R.id.sildingFinishLayout);
        mSildingFinishLayout
                .setOnSildingFinishListener(new SildingFinishLayout.OnSildingFinishListener() {

                    @Override
                    public void onSildingFinish() {
                        Liuyanban_pinglun.this.finish();
                    }
                });

        mSildingFinishLayout.setTouchView(mSildingFinishLayout);*/

        SysApplication.getInstance().addActivity(getActivity());
        FontManager.changeFonts(FontManager.getContentView(getActivity()), getActivity());//字体
        mTogBtn = (ToggleButton) v.findViewById(R.id.liuyanpinglun_toggle);

        lypl_horizon = (LinearLayout) v.findViewById(R.id.lypl_horizon);
/*
        for (int i = 0; i<10;i++){
            RoundedImageView imageView=new RoundedImageView(Liuyanban_pinglun.this);
            imageView.setImageResource(R.drawable.android_181);
            imageView.setPadding(10, 0, 0, 0);
            imageView.setId(i);
            lypl_horizon.addView(imageView);
        }
*/

        ID = getActivity().getIntent().getStringExtra("ID");
        /*nickname = getIntent().getStringExtra("nickname");
        text = getIntent().getStringExtra("text");
        time = getIntent().getStringExtra("date");*/
        lv = (ListView)v.findViewById(R.id.liuyanban_pinglun_listview);
        LoadData();
        nickname_tv = (TextView) v.findViewById(R.id.lypl_nickname);
        text_tv = (TextView) v.findViewById(R.id.lypl_text);
        time_tv = (TextView) v.findViewById(R.id.lypl_time);
        nickname_tv.setText(nickname);
        avatarImage = (RoundedImageView) v.findViewById(R.id.lypl_avatar);
        Drawable bpAvatar;
        if ((bpAvatar = AsyncLoadAvatar.GetLocalImage(getActivity(), (String) avatar)) == null)           //获取存在本地的Bitmap
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (AsyncLoadAvatar.SaveBitmapToLocal(AsyncLoadAvatar.DownloadBitmap((String) avatar), (String) avatar))  //returned Bitmap   把Bitmap保存到本地
                    {
                        getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            avatarImage.setImageDrawable(AsyncLoadAvatar.GetLocalImage(getActivity(), (String) avatar));
                        }
                    });
                    }
                }
            }).start();
            avatarImage.setImageResource(R.drawable.android_181);
        } else {
            avatarImage.setImageDrawable(bpAvatar);
        }
        text_tv.setText(text);
        time_tv.setText(time);
        mSimpleAdapter = new SimpleAdapter(getActivity(),listItem,R.layout.shipinpinglun_item,new String[]{
                "nickname","text","date","avatar"},new int[]{R.id.shipinpinglun_nickname,R.id.shipinpinglun_neirong,R.id.shipinpinglun_date,R.id.shipinpinglun_Item_touxiang}
        );
        mSimpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, final Object data, String textRepresentation) {
                Drawable avatar;
                if (view instanceof ImageView && data instanceof String) {
                    ImageView imageView = (ImageView) view;
                    if ((avatar = AsyncLoadAvatar.GetLocalImage(getActivity(),(String) data)) == null)           //获取存在本地的Bitmap
                    {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (AsyncLoadAvatar.SaveBitmapToLocal(AsyncLoadAvatar.DownloadBitmap((String) data), (String) data))
                                    ;  //returned Bitmap   把Bitmap保存到本地
                                {
                                    Message msg = new Message();
                                    myMessageHandler.sendMessage(msg);
                                }
                            }
                        });
                        thread.start();
                        imageView.setImageResource(R.drawable.android_181);
                        return true;
                    } else {
                        imageView.setImageDrawable(avatar);
                        return true;
                    }
                }
                return false;
            }
        });
        /**
         * 向ListView设置Adapter。
         */
        lv.setAdapter(mSimpleAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), Liuyanhuifu_activity.class);
                intent.putExtra("nickname",(String)listItem.get(position).get("nickname"));
                intent.putExtra("ID",(String)listItem.get(position).get("ID"));
                intent.putExtra("type","5");
                startActivityForResult(intent, 1);
                getActivity().overridePendingTransition(R.anim.base_slide_remain, R.anim.liuyan_in);
            }
        });
        liuyan = (Button) getActivity().findViewById(R.id.pinglun_liuyan);
        liuyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),Liuyanhuifu_activity.class);
                intent.putExtra("type","4");
                intent.putExtra("nickname",nickname);
                intent.putExtra("ID",ID);
                startActivityForResult(intent,1);
                getActivity().overridePendingTransition(0, R.anim.liuyan_in);
            }
        });
        liuyanban_fh = (RelativeLayout) getActivity().findViewById(R.id.liuyanban_fh);
        liuyanban_fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.base_slide_remain, R.anim.base_slide_right_out);
            }
        });

        setListViewHeightBasedOnChildren(lv);
        mTogBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                //togglebutton ture 的状态
                final HttpIO io = new HttpIO(Utils.serveraddr + "api/msg.php?action=like&type=12&position="+ID);
                io.SessionID = Static.session_id;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        io.GETToHTTPServer();
                    }
                }).start();
                while (io.getResultData()==null);
                LoadData();
            }
        });
        return v;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LoadData();
        mSimpleAdapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(lv);
    }


}
