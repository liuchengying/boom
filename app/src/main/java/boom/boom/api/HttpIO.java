package boom.boom.api;

import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 1eekai on 2015/1/16.
 */
public class HttpIO {
    private static int CONNECTION_TIMED_OUT = 5744643;
    private String URLstr;
    private boolean sessionState;
    public String SessionID;
    public List<Cookie> cookiearray;
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
                    postParameters);
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

    public String GETToHTTPServer() {
        /*
         *  GetToHTTPServer() 用法：
         *
         *  HttpIO io = new HttpIO("http://example.com/test.php?name=likai");
         *  io.GetToHTTPServer();
         */
        //final Boolean bFinished;

        String result = null;
        BufferedReader reader = null;
        bFinished=false;
        try {
            final DefaultHttpClient client = new DefaultHttpClient();
            final HttpGet request = new HttpGet();
            if (this.SessionID != null){
                request.setHeader("Cookie", "PHPSESSID=" + this.SessionID);
            }
            request.setURI(new URI(
                    this.GetURL()));
            Thread thread=new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        response = client.execute(request);
                        bFinished = true;
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
            TimerTask task=new TimerTask() {
                @Override
                public void run() {
                    bFinished=true;
                }
            };
            Timer timer=new Timer(true);
            timer.schedule(task,5000);
            while(!bFinished){}
            if(response==null)
            {
                //Toast toast=new Toast(getApplicationContext(),"",Toast.LENGTH_SHORT);
                LastError = CONNECTION_TIMED_OUT;
                return null;
            }
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
}

