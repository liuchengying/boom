package boom.boom.api;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.baidu.android.pushservice.PushMessageReceiver;

import java.util.List;

import boom.boom.xinxizhongxin.xinxizhongxin_activity;

public class MyReceiver extends PushMessageReceiver {
    @Override
    public void onBind(Context context, int i, final String s, final String s1, final String s2, String s3) {
        Log.e("push", i + "   " + s );
        Static.user_id = s1;
        Static.channel_id = s2;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpIO io = new HttpIO(Utils.serveraddr + "/api/userRegister.php?action=bring_channel&user_id=" + s1 + "&channel_id=" + s2);
                io.SessionID = Static.session_id;
                io.GETToHTTPServer();
                String result = io.getResultData();
                Log.e("push",result);
            }
        }).start();
    }

    @Override
    public void onUnbind(Context context, int i, String s) {

    }

    @Override
    public void onSetTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override
    public void onDelTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override
    public void onListTags(Context context, int i, List<String> list, String s) {

    }

    @Override
    public void onMessage(Context context, String s, String s1) {
        Log.e("onMessage",s+s1);
    }

    @Override
    public void onNotificationClicked(Context context, String s, String s1, String s2) {
        Log.e("Push",s+s1+s2);
        Intent intent = new Intent();
        intent.setClass(context.getApplicationContext(),xinxizhongxin_activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(intent);

    }

    @Override
    public void onNotificationArrived(Context context, String s, String s1, String s2) {
        Log.e("11", s + "  " + s1 + "  " + s2);
        try {
            int count = Static.badgeView.getBadgeCount();
            count++;
            Static.badgeView.setBadgeCount(count);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}