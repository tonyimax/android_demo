package com.example.demo4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.demo4.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'demo4' library on application startup.
    static {
        System.loadLibrary("demo4");
    }

    private ActivityMainBinding binding;

    String[] btnTexts = new String[]{
            "7","8","9","/",
            "4","5","6","x",
            "1","2","3","-",
            ".","0","=","+",
    } ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Example of a call to a native method
        TextView tv = binding.sampleText;
        //tv.setText(stringFromJNI());

        //获取表格布局控件
        GridLayout gl = binding.root;
        //遍历要动态生成的文本数组
        for (int i=0;i<btnTexts.length;i++){
            Button btn = new Button(this);//创建按钮
            btn.setText(btnTexts[i]);//设置按钮显示文本
            btn.setTextSize(40);//设置字体大小
            btn.setPadding(5,35,5,35);//设置填充
            //设置表格属性
            GridLayout.Spec spec = GridLayout.spec(i/4+2);
            GridLayout.Spec spec1 = GridLayout.spec(i%4);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(spec,spec1);
            params.setGravity(Gravity.FILL);
            gl.addView(btn,params);//添加按钮
        }


    }

    /**
     * A native method that is implemented by the 'demo4' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}