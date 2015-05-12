package boom.boom.api;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/5/7.
 */
public class EditInformation implements Serializable {
    public String ServerErr;
    public String nickname;
    public String name;
    public int sex;
    public int age;
    public int star;
    public String job;
    public String address;
    public String school;
    public String company;
    public String email;
    public String uniquesign;
    public String avatar;
    //http://172.24.10.118/api/userdata.php?action=alter&method=email&value=yangxue@net.cn
    private static final String GETINFOMATION_SERVER=Utils.serveraddr+"/api/userdata.php";
    public int GetInformation()
            throws JSONException
    {
        Utils.GetBuilder get = new Utils.GetBuilder(GETINFOMATION_SERVER);
        get.addItem("action", "get");
        String url_request = get.toString();
        HttpIO io = new HttpIO(url_request);
        io.SessionID=Static.session_id;
        io.GETToHTTPServer();
        if(io.LastError==0) {
            String httpResult = io.getResultData();
            try {
                JSONObject obj = new JSONObject(httpResult);
                String status = obj.getString("state");
                if (status.equalsIgnoreCase("FAILED"))
                {
                    ServerErr="获取失败！";
                    return 0;
                }
                if (status.equalsIgnoreCase("SUCCESS")) {
                    JSONObject data = Utils.GetSubJSONObject(obj,"data");
                    nickname = data.getString("nickname");
                    name = data.getString("name");
                    sex = data.getInt("sex");
                    age = data.getInt("age");
                    star = data.getInt("constellation");
                    job = data.getString("job");
                    address = data.getString("location");
                    school = data.getString("school");
                    company = data.getString("company");
                    email = data.getString("email");
                    uniquesign = data.getString("uniquesign");
                    avatar = data.getString("avatar");
                }
            }
            catch(Exception e)
            {
                ServerErr="获取失败！";
                return 0;
            }

        }
        else {

            ServerErr="登陆超时！";
            return 0;
        }
        return 1;
    }

    public int save()
    {
        String str;
        try {
            Utils.GetBuilder get = new Utils.GetBuilder(GETINFOMATION_SERVER);
            get.addItem("action", "alter_json");

            JSONObject json = new JSONObject();
            json.put("nickname", nickname);
            json.put("name", name);
            json.put("sex", sex);
            json.put("age", age);
            json.put("constellation", star);
            json.put("job", job);
            json.put("location", address);
            json.put("school", school);
            json.put("company", company);
            json.put("email", email);
            json.put("uniquesign", uniquesign);
            json.put("avatar", avatar);
            get.addItem("data",json.toString());
            String url_request = new String(get.toString().getBytes("UTF-8"));
            HttpIO io = new HttpIO(url_request);
            io.SessionID=Static.session_id;
            JSONObject result = new JSONObject(io.GetJson());
            String status = result.getString("state");
            if (status.equalsIgnoreCase("FAILED"))
            {
                ServerErr="保存失败，请重试！";
                return 0;
            }
            if (status.equalsIgnoreCase("SUCCESS")) return 1;
        }
        catch (Exception e)
        {
            ServerErr = "保存失败，请重试！";
            return 0;
        }

        return 1;
    }

}
