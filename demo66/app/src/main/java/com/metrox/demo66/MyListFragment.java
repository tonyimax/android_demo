package com.metrox.demo66;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.fragment.app.ListFragment;

public class MyListFragment extends ListFragment {

    private String[] data = {
            "苹果", "香蕉", "橙子", "西瓜", "葡萄",
            "芒果", "菠萝", "草莓", "樱桃", "梨子",
            "桃子", "李子", "荔枝", "龙眼", "山竹"
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 设置适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1, // 内置布局
                data
        );
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        // 点击事件处理
        String selectedItem = (String) l.getItemAtPosition(position);
        Toast.makeText(
                getActivity(),
                "你选择了: " + selectedItem + " (位置: " + position + ")",
                Toast.LENGTH_SHORT
        ).show();
    }
}
