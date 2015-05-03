package boom.boom.tianjiahaoyou;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import boom.boom.R;

/**
 * Created by 刘成英 on 2015/3/12.
 */
public class Xiangxiziliao_fragment_tjhy extends Fragment
{
    private Button tianjiahaoyou_button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v= inflater.inflate(R.layout.tianjiahaoyou3, container, false);
       final Button tianjiahaoyou_button = (Button) v.findViewById(R.id.tianjiahaoyou_button);

        tianjiahaoyou_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tianjiahaoyou_button.setText("等待通过");
            }
        });
        return v;
    }
}
