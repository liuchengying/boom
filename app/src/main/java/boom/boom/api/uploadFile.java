package boom.boom.api;

/**
 * Created by lenovo on 2015/4/28.
 */

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class uploadFile{
    public static String uploadFile(ProgressListener listener, String URL, String fileFullPath, String fileName){
        return uploadFile(listener, URL, fileFullPath, fileName, "video/mp4");
    }
    public static String uploadFile(ProgressListener listener, String URL, String fileFullPath, String fileName, String mimetype){
        int timeout = 5000;
        String requestURL = URL;


        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "sdfgfhgtfjhrfshrhtyhtyhutht";
        StringBuffer sb = new StringBuffer();
        try{
            HttpURLConnection conn = (HttpURLConnection) new URL(requestURL).openConnection();
//            conn.setChunkedStreamingMode(1024*1024);	// 立即输出到网络流
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout * 2);

            // 发送文件
            int fileLength = 0;
            int percentage = -1;
            double sendLength = 0;

            FileInputStream fis = new FileInputStream(fileFullPath);
            fileLength = fis.available();
            fis.close();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("User-Agent", "Generic/Boom");
//            conn.setRequestProperty("Content-Length", fileLength + "");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=" + fileName + "" + end + "Content-Type: "+ mimetype + end);
            dos.writeBytes(end);

            fis = new FileInputStream(fileFullPath);

            byte[] buffer = new byte[1024];
            int count = 0;
            // 读取文件
            while ((count = fis.read(buffer)) != -1){
                dos.write(buffer, 0, count);

                sendLength += count;
                int p = (int) (sendLength / fileLength * 100);
                if (p > 100)
                    p = 100;
                if (p != percentage){
                    percentage = p;
                    if (listener != null)
//                        listener.onPercentageChange(percentage);
                        listener.transferred(percentage);
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
        catch (Exception e){
            e.printStackTrace();
            return "<NetworkError>"+e.toString()+"</NetworkError>";
        }

        return sb.toString();
    }

}
