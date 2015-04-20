package boom.boom.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.protocol.HTTP;

import java.io.File;
import java.nio.charset.Charset;

/**
 * Created by lenovo on 2015/4/20.
 */
public class FileUploadAsyncTask extends AsyncTask<File, Integer, String> {

    private String url = Utils.serveraddr + Utils.put_file_api;
    private Context context;
    private ProgressDialog pd;
    private long totalSize;
    private Integer progress_long;
    private Boolean onComplete;

    public FileUploadAsyncTask(Context context, Integer progress_percent, Boolean _onComplete) {
        this.context = context;
        progress_long = progress_percent;
        onComplete = _onComplete;
        onComplete = false;
    }

    public boolean getState(){
        return onComplete;
    }

    public int getProgress(){
        return progress_long;
    }

    @Override
    protected void onPreExecute() {
//        pd = new ProgressDialog(context);
//        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        pd.setMessage("上传中....");
//        pd.setCancelable(false);
//        pd.show();
    }

    @Override
    public String doInBackground(File... params) {
        // 保存需上传文件信息
        MultipartEntityBuilder entitys = MultipartEntityBuilder.create();
        entitys.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        entitys.setCharset(Charset.forName(HTTP.UTF_8));

        File file = params[0];
        entitys.addPart("file", new FileBody(file));
        HttpEntity httpEntity = entitys.build();
        totalSize = httpEntity.getContentLength();
        ProgressOutHttpEntity progressHttpEntity = new ProgressOutHttpEntity(
                httpEntity, new ProgressListener() {
            @Override
            public void transferred(long transferedBytes) {
//                publishProgress((int) (100 * transferedBytes / totalSize));
                progress_long = Integer.valueOf(new Long(transferedBytes).intValue());
            }
        });
//        return uploadFile(url, progressHttpEntity);
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        pd.setProgress((int) (progress[0]));
    }

    @Override
    protected void onPostExecute(String result) {
        pd.dismiss();
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
    }


    //上传文件到服务器
    //服务器地址
    //文件

    public void uploadFile(String url, ProgressOutHttpEntity entity) {
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(
                CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        // 设置连接超时时间
        httpClient.getParams().setParameter(
                CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);
        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                onComplete = true;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpClient != null && httpClient.getConnectionManager() != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }
        onComplete = false;
    }

}