package com.example.demo1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.demo1.databinding.ActivityMainBinding;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'demo1' library on application startup.
    static {
        System.loadLibrary("demo1");
    }

    private ActivityMainBinding binding;

    //图像id集合
    int[] imgs = new int[]{
            R.drawable.a1,
            R.drawable.a2,
            R.drawable.a3,
            R.drawable.a4,
            R.drawable.a5,

    };
    int current = 0; //当前图像索引

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Example of a call to a native method
        TextView tv = binding.sampleText;
        tv.setText(stringFromJNI());

        //获取按钮对象
        Button btnShowDate = binding.btnShowDate;
        //添加按钮点击监听
        btnShowDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //处理按钮点击逻辑
                System.out.println("===>点击了显示系统时间按钮");
                //动态修改TextView文本
                tv.setText(new Date().toString());
            }
        });

        //获取ImageView组件对象
        ImageView iv = binding.imageView;
        iv.setImageResource(imgs[current]);
        //点击图像时变更索引
        iv.setOnClickListener(v -> {
            iv.setImageResource(imgs[++current % imgs.length]);
        });


    }

    /**
     * A native method that is implemented by the 'demo1' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}