package boom.boom.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaExtractor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.GZIPInputStream;

/**
 * Created by 1eekai on 2015/1/16.
 */
public class HttpIO {
    public final static int CONNECTION_TIMED_OUT = 5744643;
    private String URLstr;
    private boolean sessionState;
    public String SessionID;
    public List<Cookie> cookiearray;
    public Context mContext;
    public PersistentCookieStore mCookieStore;
    private static String SESSION_MAGIC_STRING = "PHPSESSID";   // SESSION中的魔法字串
    Boolean bFinished;          //多线程同步事件
    HttpResponse response;
    public int LastError;
    public HttpIO(String ur1){
        SessionID = null;
        this.sessionState = true;      // 默认情况下，保持SESSION。
        this.SetURL(ur1);
    }

    public HttpIO(String ur1, boolean SessionState){
        this.sessionState = SessionState;   // 重构构造函数。
        this.SetURL(ur1);
    }

    public void SetCustomSessionID(String id){
        if (!this.sessionState) return;
        this.SessionID = id;
    }

    public String GetSessionID(){
        if (!this.sessionState) return null;
        return this.SessionID;
    }

    public String UpdateSessionID(){
        if (!this.sessionState)     return null;
        for (int counter=0; counter<this.cookiearray.size();counter++){
            if (SESSION_MAGIC_STRING.equals(this.cookiearray.get(counter).getName())){
                this.SessionID = this.cookiearray.get(counter).getValue();
                return this.SessionID;
            }
        }
        return null;
    }
    private String ResultBuffer;
    public void SetURL(String UR1){  this.URLstr = UR1;  }
    public String GetURL(){   return this.URLstr; }

    public String getResultData(){
        return ResultBuffer;
    }

    public void CleanBuffer(){
        ResultBuffer = null;
    }

    public static List<Cookie> getCookies(){
        //PersistentCookieStore cookieStore = new PersistentCookieStore();
        //client.setCookieStore(Static.cookieStore);
        List<Cookie> cookies = Static.cookieStore.getCookies();
        Log.e("cookie","Got");
        if(cookies.size()>0) {
            Static.session_id = cookies.get(0).getValue();
        }
        return cookies;
    }
    public String POSTToHTTPServer(List<NameValuePair> postParameters) {
        /*
         *  POSTToHTTPServer() 用法：
         *  参数1 ： List<NameValuePair> 传入一个List容器类，容器成员为对称节点。
         *
         *  例如：
         *  HttpIO io = new HttpIO("http://example.com/init.jsp");   // 初始化一个连接（此时客户端并未访问服务器）
         *  List<NameValuePair> post = new ArrayList<NameValuePair>();
         *  post.add(new BasicNameValuePair("name", "likai")); // 增加POST表单数据
         *  io.POSTToHTTPServer(post); // 发送访问请求和POST数据
         *
         *  By: M0xkLurk3r
         */
        String result = null;
        BufferedReader reader = null;
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost();
            if (this.SessionID != null){
                request.setHeader("Cookie", "PHPSESSID=" + this.SessionID);
            }
            request.setURI(new URI(this.GetURL()));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
                    postParameters, "UTF-8");
            request.setEntity(formEntity);
            HttpResponse response = client.execute(request);
            if (this.sessionState) {
                cookiearray = client.getCookieStore().getCookies();
                UpdateSessionID();
            }
            reader = new BufferedReader(new InputStreamReader(response
                    .getEntity().getContent()));
            StringBuffer strBuffer = new StringBuffer("");
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuffer.append(line);
            }
            result = strBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    reader = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        this.ResultBuffer = result;
        return result;
    }
 //   @TargetApi(Build.VERSION_CODES.GINGERBREAD)

    public String GETToHTTPServer() {
        /*
         *  GetToHTTPServer() 用法：
         *
         *  HttpIO io = new HttpIO("http://example.com/test.php?name=likai");
         *  io.GetToHTTPServer();
         */

        String result = null;
        /*BufferedReader reader = null;
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            if (this.SessionID != null){
                request.setHeader("Cookie", "PHPSESSID=" + this.SessionID);
            }
            request.setURI(new URI(
                    this.GetURL()));
            HttpResponse response = client.execute(request);
            if (this.sessionState){
                cookiearray = client.getCookieStore().getCookies();
                UpdateSessionID();
            }
            reader = new BufferedReader(new InputStreamReader(response
                    .getEntity().getContent()));

            StringBuffer strBuffer = new StringBuffer("");
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuffer.append(line);
            }
            result = strBuffer.toString();
        } catch (Exception e) {
            this.LastError = 1;
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    reader = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/

        SyncHttpClient client = new SyncHttpClient();
        if(mCookieStore != null) {
            client.setCookieStore(mCookieStore);
        }else {
            client.setCookieStore(Static.cookieStore);
        }
        client.get(this.GetURL(), new TextHttpResponseHandler() {
            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {

            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {
                Log.e("Async",s);
                getCookies();
                ResultBuffer = s;
            }
        });
        //this.ResultBuffer = result;
        return ResultBuffer;
    }

    public JSONObject getJSONObject(){
        try {
            return new JSONObject(getJson());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

 //   @TargetApi(Build.VERSION_CODES.GINGERBREAD)

//    @SuppressLint("NewApi")
    public String getJson()
    {
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        //严格策略

        String resultData = "";
        InputStreamReader in = null;
        HttpURLConnection urlConn = null;
        BufferedReader buffer = null;
        try {
            URL url = new URL(GetURL());
            if (url != null) {
                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setConnectTimeout(5000);// 设置超时时间
                //urlConn.setRequestProperty("Accept-Encoding", "utf-8, deflate");
                urlConn.setRequestProperty("Cookie","PHPSESSID=" + this.SessionID);
                //urlConn.setRequestProperty("Accept-Charset", "utf-8");
                //urlConn.setRequestProperty("contentType", "utf-8");
                urlConn.setRequestProperty("Content-type", "text/html");
                urlConn.setRequestProperty("Accept-Charset", "utf-8");
                urlConn.setRequestProperty("contentType", "utf-8");
                try {
                    InputStream gin = urlConn.getInputStream();
                    in = new InputStreamReader(gin);

                } catch (ConnectException e) {
                    e.printStackTrace();
                    return resultData;
                }
            }
            String encoding = urlConn.getContentEncoding();

            buffer = new BufferedReader(in);
            String inputLine = null;

            while ((inputLine = buffer.readLine()) != null) {
                resultData += inputLine + "\n";
            }
            urlConn.disconnect();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        this.ResultBuffer = resultData;
            return resultData;
    }

    public Bitmap getImage()
    {
        Bitmap bitmap=null;
        String resultData = "";
        InputStream in = null;
        HttpURLConnection urlConn = null;
        BufferedReader buffer = null;

        try {
            URL url = new URL(GetURL());
            if (url != null) {
                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setConnectTimeout(5000);// 设置超时时间
                urlConn.setRequestProperty("Cookie","PHPSESSID=" + this.SessionID);
                try {
                   in = urlConn.getInputStream();
                } catch (ConnectException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            AsyncHttpClient client = new AsyncHttpClient();


            //解析得到图片
            bitmap = BitmapFactory.decodeStream(in);
            //关闭数据流
            in.close();



            urlConn.disconnect();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        this.ResultBuffer = resultData;
        return bitmap;
    }
    public static void GetHttpEX (final Context context , final Handler handler , String URL){
        try{
           /* final LoadingDialog loadingDialog = new LoadingDialog(context,"正在加载...");
            loadingDialog.setCancelable(false);
            loadingDialog.show();*/
            final HttpIO io = new HttpIO(URL);
            io.SessionID = Static.session_id;
            io.mContext = context;
            Static.cookieStore = new PersistentCookieStore(context);
            final TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what = 0;
                    //loadingDialog.cancel();
                    handler.sendMessage(msg);
                    //Toast.makeText(context,"网络连接失败！请检查网络连接",Toast.LENGTH_SHORT).show();
                }
            };
            Timer timer = new Timer(true);
            timer.schedule(task,5000);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    io.GETToHTTPServer();
                    while(io.getResultData()==null);
                    if(io.LastError==0) {
                        Message msg = new Message();
                        msg.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("data", io.getResultData());
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                        task.cancel();
                        //loadingDialog.cancel();
                    }else {

                    }
                }
            }).start();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

