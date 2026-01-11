package com.example.demo37;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class PersonAdapter extends ArrayAdapter<MainActivity.Person> {

    private Context context;
    private ArrayList<MainActivity.Person> personList;

    public PersonAdapter(Context context, ArrayList<MainActivity.Person> personList) {
        super(context, 0, personList);
        this.context = context;
        this.personList = personList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_person, parent, false);
        }

        MainActivity.Person person = personList.get(position);

        ImageView ivIcon = convertView.findViewById(R.id.iv_icon);
        TextView tvName = convertView.findViewById(R.id.tv_name);
        TextView tvJob = convertView.findViewById(R.id.tv_job);

        // 这里使用系统图标作为示例，实际项目中应使用自己的图标资源
        ivIcon.setImageResource(android.R.drawable.ic_menu_my_calendar);
        tvName.setText(person.getName());
        tvJob.setText(person.getJob());

        return convertView;
    }

    @Override
    public int getCount() {
        return personList.size();
    }
}
