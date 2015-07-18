package boom.boom.api;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.baidu.android.pushservice.PushMessageReceiver;

import java.util.List;

import boom.boom.xinxizhongxin.xinxizhongxin_activity;

public class MyReceiver extends PushMessageReceiver {
    @Override
    public void onBind(Context context, int i, String s, String s1, String s2, String s3) {
        Log.e("push", i + "   " + s );
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
        Log.e("11", "11");
        int count = Static.badgeView.getBadgeCount();
        count++;
        Static.badgeView.setBadgeCount(count);
    }
}