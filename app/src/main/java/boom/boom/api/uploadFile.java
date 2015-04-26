package boom.boom.api;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lenovo on 2015/4/25.
 */
public class uploadFile {
    private static final int timeout = 30000;
    public static String  uploadFile(ProgressListener listener, String url, String fullFilePath, String fileName){
        String requestURL = url;

        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        StringBuffer sb = new StringBuffer();
        try{
//            SSLContext sc = SSLContext.getInstance("TLS");
//            sc.init(null, new TrustManager[] { (TrustManager) new MyTrustManager() }, new SecureRandom());
//            HttpURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//            HttpURLConnection.setDefaultHostnameVerifier((HostnameVerifier) new MyHostnameVerifier());
            HttpURLConnection conn = (HttpURLConnection) new URL(requestURL).openConnection();
            conn.setChunkedStreamingMode(1024 * 1024*10);	// 1M
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(true);
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout * 2);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            // 开始构造文件表单
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\"" + fileName + "\"" + end);
            dos.writeBytes(end);

            int fileLength = 0;
            FileInputStream fis = new FileInputStream(fullFilePath);
            fileLength = fis.available();
            byte[] buffer = new byte[1024];
            int count = 0;
            // 读取文件
            int percentage = -1;
            double sendLength = 0;
            while ((count = fis.read(buffer)) != -1) {
                sendLength += count;
                dos.write(buffer, 0, count);
                int p = (int) (sendLength / fileLength * 100);
                if (p > 100)
                    p = 100;
                if (p != percentage)
                {
                    percentage = p;
                    if (listener != null) {
                        Log.d("UPLOAD", "Percentage while upload ==> " + percentage);
                        listener.transferred(percentage);
                    }
                }

            }
            fis.close();

            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null)
                sb.append(line);
            dos.close();
            is.close();
            conn.disconnect();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
