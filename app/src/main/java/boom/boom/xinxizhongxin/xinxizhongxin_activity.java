package boom.boom.xinxizhongxin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import boom.boom.R;
import boom.boom.api.Msg;

/**
 * Created by Lyp on 2015/1/22
 */

public class xinxizhongxin_activity extends Activity {
      private ListView list;
      protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xinxizhongxin);
        Button fh=(Button)findViewById(R.id.xxzx_fanhui);
//        Button rightSideButton;
        final ArrayList<Map<String, Object>> listItem = new ArrayList<>();
        list = (ListView)findViewById(R.id.challenge_list);
        while (true) {
            Msg msg = new Msg();
            Map<String, Object> map = new HashMap<String, Object>();
            map = msg.GetSimpleMap();
            if (map == null) {
                break;
            }
            listItem.add(map);
        }
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,
                  R.layout.msgcenter_listitem,
                  new String[] {"label", "text"},
                  new int[] {R.id.msg_center_list_label, R.id.msg_center_list_text}
        );
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "删除、回信功能尚未实现，敬请期待。", Toast.LENGTH_SHORT);
                return false;
            }
        });
        fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
