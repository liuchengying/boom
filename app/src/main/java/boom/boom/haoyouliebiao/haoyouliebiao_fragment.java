package boom.boom.haoyouliebiao;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.AsyncLoadAvatar;
import boom.boom.api.FriendList;
import boom.boom.api.SysApplication;
import boom.boom.gerenzhuye.Gerenzhuye_activity;
import boom.boom.search_friends.Search_friends_activity;

/**
 * Created by Administrator on 2015/7/11.
 */
public class haoyouliebiao_fragment extends Fragment {


    ListView lv;
    RelativeLayout haoyouliebiao_fh;
    SimpleAdapter mSimpleAdapter;
    TextView hylb_search;
    FriendList friendList;
    ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();
    android.os.Handler myMessageHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mSimpleAdapter.notifyDataSetChanged();
        }
    };
    Handler getListHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == 1){
                friendList.GetFriendList(listItem);

                mSimpleAdapter.notifyDataSetChanged();
            }else {
                Toast.makeText(getActivity(), "网络连接错误！请检查网络连接", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    });
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.haoyouliebiao_frame,container,false);
        {
            try {
                SysApplication.getInstance().addActivity(getActivity());
                FontManager.changeFonts(FontManager.getContentView(getActivity()), getActivity());//字体
                lv = (ListView) v.findViewById(R.id.haoyouliebiao_listview);
                friendList = new FriendList(getActivity(),getListHandler);
                mSimpleAdapter = new SimpleAdapter(getActivity(), listItem,//需要绑定的数据
                        R.layout.haoyouliebiao_item_down,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                        new String[]{
                                "avatar", "nickname"},
                        new int[]{R.id.haoyouliebiao_touxiang, R.id.haoyouliebiao_nicheng}
                );
                mSimpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                    @Override
                    public boolean setViewValue(View view, final Object data, String textRepresentation) {
                        Bitmap avatar;
                        if (view instanceof ImageView && data instanceof String) {
                            ImageView imageView = (ImageView) view;
                            if ((avatar = AsyncLoadAvatar.GetLocalImage((String) data)) == null)           //获取存在本地的Bitmap
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
                                return true;
                            } else {
                                imageView.setImageBitmap(avatar);
                                return true;
                            }
                        }
                        return false;
                    }
                });
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String guestID = (String) ((HashMap<String, Object>) listItem.get(position)).get("guestID");
                        Intent intent = new Intent();
                        intent.putExtra("guestID", guestID);
                        intent.putExtra("type",1);
                        intent.setClass(getActivity(), Gerenzhuye_activity.class);
                        startActivity(intent);
                    }
                });

                hylb_search = (TextView) v.findViewById(R.id.hylb_search);
                hylb_search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent();
                        intent.setClass(getActivity(), Search_friends_activity.class);
                        startActivity(intent);
                    }
                });
                lv.setAdapter(mSimpleAdapter);
                haoyouliebiao_fh = (RelativeLayout) getActivity().findViewById(R.id.haoyouliebiao_fh);
                haoyouliebiao_fh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().finish();
                        getActivity().overridePendingTransition(R.anim.base_slide_remain, R.anim.base_slide_right_out);
                    }
                });
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return v;
    }
}
