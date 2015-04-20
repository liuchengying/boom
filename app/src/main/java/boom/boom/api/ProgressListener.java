package boom.boom.api;

/**
 * Created by lenovo on 2015/4/20.
 */

public interface ProgressListener {
    public void transferred(long transferedBytes);
}