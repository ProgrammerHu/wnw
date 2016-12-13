package com.hemaapp.luna_demo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.luna_demo.BluetoothChat.BluetoothChatActivity;
import com.hemaapp.luna_demo.BluetoothChat.DeviceListActivity;
import com.hemaapp.luna_demo.R;

import java.util.ArrayList;

/**
 * Created by HuHu on 2016/4/18.
 */
public class ListActivity extends Activity {

    private ListView listView;
    private ArrayList<MainModel> listData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listView = (ListView) findViewById(R.id.listView);
        listData.add(new MainModel("查看基站号和小区号", GetCellAndLac.class));
        listData.add(new MainModel("隐藏ToolBar", MainActivity.class));
        listData.add(new MainModel("zxing扫描二维码", CodeCaptureActivity.class));
        listData.add(new MainModel("http://192.168.3.73/html/index.html", WebviewActivity.class));
        listData.add(new MainModel("http://192.168.1.140:8081/MyBlog/html/index.php", WebviewActivity.class));
        listData.add(new MainModel("http://192.168.2.28:8081/MyBlog/html/index.php", WebviewActivity.class));
        listData.add(new MainModel("富文本编辑器", RichTextActivity.class));
        listData.add(new MainModel("OkHttp", OkHttpActivity.class));
        listData.add(new MainModel("Glide", GlideActivity.class));
        listData.add(new MainModel("SwipeRefreshLayout", SwipeRefreshLayoutActivity.class));
        listData.add(new MainModel("DownLoad by OkHttp", DownLoadActivity.class));
        listData.add(new MainModel("属性动画", PropertyAnimationActivity.class));
        listData.add(new MainModel("地图画线", MapLineActivity.class));
        listData.add(new MainModel("赵京伟的日历", CalendarActivity.class));
        listData.add(new MainModel("波纹", WaveViewActivity.class));
        listData.add(new MainModel("抽屉", NavigationViewActivity.class));
        listData.add(new MainModel("录视频", VideoActivity.class));
        listData.add(new MainModel("ProgressView", ProgressViewActivity.class));
        listData.add(new MainModel("http://m.kuaidi100.com/result.jsp?nu=3905681408737", WebviewActivity.class));
        listData.add(new MainModel("http://www.kuaidi100.com/query?postid=3905681408737&type=yunda", WebviewActivity.class));
        listData.add(new MainModel("多线程下载", MultiThreadDownloadActivity.class));
        listData.add(new MainModel("Camera", CameraActivity.class));
        listData.add(new MainModel("贝塞尔曲线", BezierCurveActivity.class));
        listData.add(new MainModel("解析Docx", ReadDocxActivity.class));
        listData.add(new MainModel("Svg", SvgActivity.class));
        listData.add(new MainModel("DeviceListActivity", DeviceListActivity.class));
        listData.add(new MainModel("BluetoothChatActivity", BluetoothChatActivity.class));
        listData.add(new MainModel("Main2Activity", Main2Activity.class));

        ListAdapter adapter = new ListAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListActivity.this, listData.get(position).getCls());
                intent.putExtra("content", listData.get(position).getContent());
                startActivity(intent);
            }
        });
    }

    private class ListAdapter extends HemaAdapter {

        public ListAdapter(Context mContext) {
            super(mContext);
        }

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_text, null, false);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            textView.setText(listData.get(position).getContent());
            return convertView;
        }
    }

    private class MainModel {
        private String content;
        private Class<?> cls;

        public MainModel(String content, Class<?> cls) {
            this.content = content;
            this.cls = cls;
        }

        public String getContent() {
            return content;
        }

        public Class<?> getCls() {
            return cls;
        }
    }
}
