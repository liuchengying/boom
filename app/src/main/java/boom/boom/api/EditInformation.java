package boom.boom.api;

import android.graphics.Bitmap;
import android.os.Message;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public Bitmap avatarImage;
    public int coins;

    //http://172.24.10.118/api/userdata.php?action=alter&method=email&value=yangxue@net.cn
    private static final String GETINFOMATION_SERVER=Utils.serveraddr+"/api/userdata.php";
    public int GetInformation()
            throws JSONException
    {
        Utils.GetBuilder get = new Utils.GetBuilder(GETINFOMATION_SERVER);
        get.addItem("action", "get");
        String url_request = get.toString();
        final HttpIO io = new HttpIO(url_request);
        new Thread(new Runnable() {
            @Override
            public void run() {
                io.SessionID=Static.session_id;
                io.GETToHTTPServer();
            }
        }).start();
       while(io.getResultData() == null);
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
                    coins = data.getInt("coins");
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

    public int save(boolean avatarChanged) {
        if (avatarChanged) {
            Utils.GetBuilder getImageUrl = new Utils.GetBuilder(Utils.serveraddr + Utils.put_file_api);
            getImageUrl.addItem("s_id", Static.session_id);
            getImageUrl.addItem("type", "image");
            String tmp = uploadFile.uploadFile(new ProgressListener() {
                @Override
                public void transferred(int transferedBytes) {
                    Message m = new Message();
                    if (transferedBytes >= 99) m.what = 99;
                    else m.what = transferedBytes;
                    //EditInformation.this.myMessageHandler.sendMessage(m);
                }

                public void transferred(long transfetedBytes) {

                }
            }, getImageUrl.toString(), "/storage/sdcard0/small.jpg", "hehe.jpg", "image/jpeg");


            String str;
            try {
                JSONObject obj = new JSONObject(tmp);
                String state = obj.getString("state");
                if (state.equals("SUCCESS")) {
                    avatar = obj.getString("fileToken");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
            Utils.GetBuilder get = new Utils.GetBuilder(GETINFOMATION_SERVER);
            get.addItem("action", "alter_post");
            try{
            /*JSONObject json = new JSONObject();
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
            get.addItem("data", json.toString());
            String url_request = new String(get.toString().getBytes("UTF-8"));
            HttpIO io = new HttpIO(url_request);
            io.SessionID = Static.session_id;*/
            final HttpIO io = new HttpIO(get.toString());   // 初始化一个连接（此时客户端并未访问服务器）

            final List<NameValuePair> post = new ArrayList<NameValuePair>();
                post.add(new BasicNameValuePair("nickname", Utils.UTF8str(nickname))); // 增加POST表单数据
                post.add(new BasicNameValuePair("name", Utils.UTF8str(name)));
                post.add(new BasicNameValuePair("sex", Utils.UTF8str(""+sex)));
                post.add(new BasicNameValuePair("age", Utils.UTF8str(""+age)));
                post.add(new BasicNameValuePair("constellation", Utils.UTF8str(""+star)));
                post.add(new BasicNameValuePair("job", Utils.UTF8str(job)));
                post.add(new BasicNameValuePair("location", Utils.UTF8str(address)));
                post.add(new BasicNameValuePair("school", Utils.UTF8str(school)));
                post.add(new BasicNameValuePair("company", Utils.UTF8str(company)));
                post.add(new BasicNameValuePair("email", Utils.UTF8str(email)));
                post.add(new BasicNameValuePair("uniquesign", Utils.UTF8str(uniquesign)));
                post.add(new BasicNameValuePair("avatar", Utils.UTF8str(avatar)));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        io.SessionID = Static.session_id;
                        io.POSTToHTTPServer(post); // 发送访问请求和POST数据
                    }
                }).start();
            while(io.getResultData() == null);
            JSONObject result = new JSONObject(io.getResultData());
            String status = result.getString("state");
            if (status.equalsIgnoreCase("FAILED")) {
                ServerErr = "保存失败，请重试！";
                return 0;
            }
            if (status.equalsIgnoreCase("SUCCESS")) return 1;
        }
        catch(Exception e)
        {
            ServerErr = "保存失败，请重试！";
            return 0;
        }

        return 1;
    }

}
