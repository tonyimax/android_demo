package com.metrox.demo66;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.ListFragment;

import java.util.ArrayList;
import java.util.List;

public class CustomListFragment extends ListFragment {

    private List<Product> productList = new ArrayList<>();

    // 数据模型类
    class Product {
        String name;
        String description;
        String price;

        Product(String name, String description, String price) {
            this.name = name;
            this.description = description;
            this.price = price;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化数据
        productList.add(new Product("iPhone 13", "苹果最新款手机", "¥6999"));
        productList.add(new Product("三星 Galaxy S21", "三星旗舰手机", "¥5999"));
        productList.add(new Product("小米 12", "小米高端手机", "¥3999"));
        productList.add(new Product("华为 Mate 40", "华为旗舰手机", "¥5499"));
        productList.add(new Product("OPPO Find X3", "OPPO旗舰手机", "¥4499"));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 设置自定义适配器
        ProductAdapter adapter = new ProductAdapter();
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Product product = productList.get(position);
        Toast.makeText(getActivity(),
                "购买: " + product.name + " - " + product.price,
                Toast.LENGTH_SHORT).show();
    }

    // 自定义适配器
    class ProductAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return productList.size();
        }

        @Override
        public Object getItem(int position) {
            return productList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_custom, parent, false);
                holder = new ViewHolder();
                holder.icon = convertView.findViewById(R.id.icon);
                holder.title = convertView.findViewById(R.id.title);
                holder.description = convertView.findViewById(R.id.description);
                holder.price = convertView.findViewById(R.id.price);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Product product = productList.get(position);
            holder.title.setText(product.name);
            holder.description.setText(product.description);
            holder.price.setText(product.price);

            return convertView;
        }

        class ViewHolder {
            ImageView icon;
            TextView title;
            TextView description;
            TextView price;
        }
    }
}
