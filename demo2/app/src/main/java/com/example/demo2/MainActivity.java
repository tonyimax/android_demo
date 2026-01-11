package com.example.demo2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.example.demo2.databinding.ActivityMainBinding;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'demo2' library on application startup.
    static {
        System.loadLibrary("demo2");
    }

    private ActivityMainBinding binding;

    //颜色列表
    final int[] colors = new int[]{
            R.color.color1,
            R.color.color2,
            R.color.color3,
            R.color.color4,
            R.color.color5,
            R.color.color6,
    };

    //控件资源ID列表
    final int[] ids = new int[]{
            R.id.sample_text,
            R.id.sample_text1,
            R.id.sample_text2,
            R.id.sample_text3,
            R.id.sample_text4,
            R.id.sample_text5,
    };

    //控件列表
    TextView[] views = new TextView[ids.length];
    int currentColor=0;//当前TextView控件
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            //修改0x123消息
            if (msg.what == 0x123){
                //遍历所有TextView
                for (int i=0;i<ids.length;i++){
                    //变更TextView背景色
                    views[i].setBackgroundResource(colors[(i+currentColor) % ids.length]);
                }
                currentColor++;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Example of a call to a native method
        TextView tv = binding.sampleText;
        tv.setText(stringFromJNI());

        //根据id查找所有TextView并保存到列表中
        for (int i=0;i<ids.length;i++){
            views[i] = findViewById(ids[i]);
        }
        //启动计时器
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0x123);//发送0x123消息
            }
        },0,200);

    }

    /**
     * A native method that is implemented by the 'demo2' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}