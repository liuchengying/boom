package boom.boom.gerenzhuye;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import boom.boom.R;
import boom.boom.api.HttpIO;
import boom.boom.api.Static;
import boom.boom.api.Utils;

/**
 * Created by 刘成英 on 2015/3/12.
 */
public class Xiangxiziliao_fragment extends Fragment
{
    private Button tianjiahaoyou_button;
    private String guestID;
    private TextView sex;
    private TextView age;
    private TextView star;
    private TextView school;
    private TextView address;
    private String result;
    private JSONObject obj;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v= inflater.inflate(R.layout.tianjiahaoyou3, container, false);

        guestID = getFragmentManager().findFragmentByTag("179521").getArguments().getString("guestID");
        sex = (TextView)v.findViewById(R.id.tjhy_sex);
        age = (TextView)v.findViewById(R.id.tjhy_age);
        star = (TextView)v.findViewById(R.id.tjhy_star);
        school = (TextView)v.findViewById(R.id.tjhy_school);
        address = (TextView)v.findViewById(R.id.tjhy_addr);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //http://172.24.10.118/api/userdata.php?action=getFriends&guest_id=10000
                Utils.GetBuilder get  = new Utils.GetBuilder(Utils.serveraddr + Utils.userdata_api);
                get.addItem("action", "getFriends");
                get.addItem("guest_id",guestID);
                HttpIO io = new HttpIO(get.toString());
                io.SetCustomSessionID(Static.session_id);
                io.GETToHTTPServer();
                switch (io.LastError){
                    case HttpIO.CONNECTION_TIMED_OUT:
                        result = "TIME_OUT";
                        break;
                    default:
                        result = io.getResultData();
                        break;
                }
            }
        }).start();
        while (result == null);
        Log.e("RESULT", "Result ==> " + result);
        if (result == "TIME_OUT"){
            Toast.makeText(getActivity().getApplicationContext(), "获取数据失败！服务器响应超时。", Toast.LENGTH_SHORT).show();
        }else{
            try {
                obj = Utils.GetSubJSONObject(new JSONObject(result), "data");
                Log.e("Test", obj.toString());
                if (obj.getString("sex").equals("0"))  sex.setText("男");
                else    if (obj.getString("sex").equals("1"))  sex.setText("女");
                else    sex.setText("保密");
                age.setText(obj.getString("age") + "岁");
                star.setText(Utils.GetXingzuo(obj.getString("constellation")));
                school.setText(obj.getString("school"));
                address.setText(obj.getString("location"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return v;
    }
}
