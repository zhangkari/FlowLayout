package com.kari.flowlayout;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kari.flowlayout.view.FlowAdapter;
import com.kari.flowlayout.view.FlowView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FlowView flowView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flowView = (FlowView) findViewById(R.id.flowView);
        TagAdapter adapter = new TagAdapter(this, mockData());
        flowView.setAdapter(adapter);
    }

    private List<String> mockData() {
        List<String> data = new ArrayList();
        for (int i = 0; i < 30; i++) {
            data.add("标签" + i);
        }
        return data;
    }

    class TagAdapter extends FlowAdapter {

        private Context mContext;
        private List<String> mDataSource;

        public TagAdapter(Context context, List<String> list) {
            mContext = context;
            mDataSource = list;
        }

        @Override
        public int getCount() {
            return mDataSource.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataSource.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder(R.layout.layout_tag_item);
            holder.bindData(mDataSource.get(position));
            return holder.itemView;
        }
    }

    class ViewHolder {
        View itemView;
        TextView txtName;

        public ViewHolder(int rootId) {
            itemView = LayoutInflater.from(MainActivity.this).inflate(rootId, null, false);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
        }

        public void bindData(String name) {
            txtName.setText(name);
        }
    }
}
