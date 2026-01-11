package com.example.demo17;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AdapterViewFlipper imageFlipper;
    private AdapterViewFlipper newsFlipper;
    private Button btnPrevImage, btnNextImage, btnToggleImage;
    private Button btnPrevNews, btnNextNews, btnToggleNews;
    private boolean isImagePlaying = true;
    private boolean isNewsPlaying = true;

    // 图片资源
    private int[] imageResources = {
            R.drawable.m1,
            R.drawable.m2,
            R.drawable.m3,
            R.drawable.m4,
            R.drawable.m5
    };

    // 图片描述
    private String[] imageTitles = {
            "美丽风景 1",
            "城市夜景",
            "自然风光",
            "海岸线",
            "山脉景观"
    };

    // 新闻数据列表
    private List<NewsItem> newsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initData();
        setupImageFlipper();
        setupNewsFlipper();
        setupButtons();
    }

    private void initViews() {
        imageFlipper = findViewById(R.id.imageFlipper);
        newsFlipper = findViewById(R.id.newsFlipper);

        btnPrevImage = findViewById(R.id.btnPrevImage);
        btnNextImage = findViewById(R.id.btnNextImage);
        btnToggleImage = findViewById(R.id.btnToggleImage);

        btnPrevNews = findViewById(R.id.btnPrevNews);
        btnNextNews = findViewById(R.id.btnNextNews);
        btnToggleNews = findViewById(R.id.btnToggleNews);
    }

    private void initData() {
        // 初始化新闻数据
        newsList.add(new NewsItem(
                "人工智能新突破",
                "研究人员开发出新一代AI模型，在自然语言处理方面取得重大进展，准确率提升至95%。",
                R.drawable.m6,
                "2024-01-15",
                "科技日报"
        ));

        newsList.add(new NewsItem(
                "科技创新大会开幕",
                "全球科技领袖齐聚北京，讨论人工智能、量子计算和生物技术的未来发展趋势。",
                R.drawable.m7,
                "2024-01-14",
                "新华网"
        ));

        newsList.add(new NewsItem(
                "绿色能源发展迅速",
                "太阳能和风能发电成本持续下降，可再生能源占比预计年底将达到30%。",
                R.drawable.m8,
                "2024-01-13",
                "人民日报"
        ));

        newsList.add(new NewsItem(
                "太空探索新里程碑",
                "中国空间站完成扩建，将为多国科学家提供太空实验平台。",
                R.drawable.m9,
                "2024-01-12",
                "央视新闻"
        ));

        newsList.add(new NewsItem(
                "医疗技术革新",
                "新型癌症早期检测技术问世，准确率高达99%，有望挽救数百万生命。",
                R.drawable.m11,
                "2024-01-11",
                "健康时报"
        ));
    }

    private void setupImageFlipper() {
        // 设置图片轮播适配器
        ImageFlipperAdapter imageAdapter = new ImageFlipperAdapter(this, imageResources, imageTitles);
        imageFlipper.setAdapter(imageAdapter);
        // 配置图片轮播
        imageFlipper.setFlipInterval(3000); // 3秒切换
        imageFlipper.setAutoStart(true);

        // 设置点击事件
        imageFlipper.setOnItemClickListener((parent, view, position, id) -> Toast.makeText(MainActivity.this,
                "点击了: " + imageTitles[position],
                Toast.LENGTH_SHORT).show());
    }

    private void setupNewsFlipper() {
        // 设置新闻轮播适配器
        NewsFlipperAdapter newsAdapter = new NewsFlipperAdapter(this, newsList);
        newsFlipper.setAdapter(newsAdapter);
        // 配置新闻轮播
        newsFlipper.setFlipInterval(5000); // 5秒切换
        newsFlipper.setAutoStart(true);

        // 设置点击事件
        /*newsFlipper.setOnItemClickListener((parent, view, position, id) -> {
            NewsItem newsItem = newsList.get(position);

            // 跳转到详情页面
            Intent intent = new Intent(MainActivity.this, NewsDetailActivity.class);
            intent.putExtra("title", newsItem.getTitle());
            intent.putExtra("description", newsItem.getDescription());
            intent.putExtra("imageRes", newsItem.getImageRes());
            intent.putExtra("date", newsItem.getDate());
            intent.putExtra("source", newsItem.getSource());
            startActivity(intent);
        });*/

        // 设置点击事件
        newsFlipper.setOnItemClickListener((parent, view, position, id) -> {
            // 确保position在有效范围内
            if (position >= 0 && position < newsList.size()) {
                NewsItem newsItem = newsList.get(position);

                // 跳转到详情页面
                Intent intent = new Intent(MainActivity.this, NewsDetailActivity.class);
                intent.putExtra("title", newsItem.getTitle());
                intent.putExtra("description", newsItem.getDescription());
                intent.putExtra("imageRes", newsItem.getImageRes());
                intent.putExtra("date", newsItem.getDate());
                intent.putExtra("source", newsItem.getSource());
                startActivity(intent);

                // 添加Activity切换动画
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            } else {
                Toast.makeText(MainActivity.this,
                        "无效的位置: " + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupButtons() {
        // 图片轮播控制按钮
        btnPrevImage.setOnClickListener(v -> {
            imageFlipper.showPrevious();
            if (isImagePlaying) {
                imageFlipper.stopFlipping();
                isImagePlaying = false;
                btnToggleImage.setText("开始播放");
            }
        });

        btnNextImage.setOnClickListener(v -> {
            imageFlipper.showNext();
            if (isImagePlaying) {
                imageFlipper.stopFlipping();
                isImagePlaying = false;
                btnToggleImage.setText("开始播放");
            }
        });

        btnToggleImage.setOnClickListener(v -> {
            if (isImagePlaying) {
                imageFlipper.stopFlipping();
                btnToggleImage.setText("开始播放");
            } else {
                imageFlipper.startFlipping();
                btnToggleImage.setText("暂停播放");
            }
            isImagePlaying = !isImagePlaying;
        });

        // 新闻轮播控制按钮
        btnPrevNews.setOnClickListener(v -> {
            newsFlipper.showPrevious();
            if (isNewsPlaying) {
                newsFlipper.stopFlipping();
                isNewsPlaying = false;
                btnToggleNews.setText("开始播放");
            }
        });

        btnNextNews.setOnClickListener(v -> {
            newsFlipper.showNext();
            if (isNewsPlaying) {
                newsFlipper.stopFlipping();
                isNewsPlaying = false;
                btnToggleNews.setText("开始播放");
            }
        });

        btnToggleNews.setOnClickListener(v -> {
            if (isNewsPlaying) {
                newsFlipper.stopFlipping();
                btnToggleNews.setText("开始播放");
            } else {
                newsFlipper.startFlipping();
                btnToggleNews.setText("暂停播放");
            }
            isNewsPlaying = !isNewsPlaying;
        });
    }

    // 图片轮播适配器
    class ImageFlipperAdapter extends BaseAdapter {
        private Context context;
        private int[] images;
        private String[] titles;
        private LayoutInflater inflater;

        public ImageFlipperAdapter(Context context, int[] images, String[] titles) {
            this.context = context;
            this.images = images;
            this.titles = titles;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int position) {
            return images[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_image, parent, false);
                holder = new ViewHolder();
                holder.imageView = convertView.findViewById(R.id.flipper_image);
                holder.titleTextView = convertView.findViewById(R.id.flipper_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.imageView.setImageResource(images[position]);
            holder.titleTextView.setText(titles[position]);

            return convertView;
        }

        class ViewHolder {
            ImageView imageView;
            TextView titleTextView;
        }
    }

    // 新闻轮播适配器
    class NewsFlipperAdapter extends BaseAdapter {
        private Context context;
        private List<NewsItem> newsList;
        private LayoutInflater inflater;

        public NewsFlipperAdapter(Context context, List<NewsItem> newsList) {
            this.context = context;
            this.newsList = newsList;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return newsList.size();
        }

        @Override
        public Object getItem(int position) {
            return newsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_news, parent, false);
                holder = new ViewHolder();
                holder.cardView = convertView.findViewById(R.id.news_card);
                holder.imageView = convertView.findViewById(R.id.news_image);
                holder.titleTextView = convertView.findViewById(R.id.news_title);
                holder.descTextView = convertView.findViewById(R.id.news_description);
                holder.dateTextView = convertView.findViewById(R.id.news_date);
                holder.sourceTextView = convertView.findViewById(R.id.news_source);
                holder.detailHintTextView = convertView.findViewById(R.id.news_detail_hint);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            NewsItem item = newsList.get(position);
            holder.imageView.setImageResource(item.getImageRes());
            holder.titleTextView.setText(item.getTitle());
            holder.descTextView.setText(item.getDescription());
            holder.dateTextView.setText(item.getDate());
            holder.sourceTextView.setText("来源: " + item.getSource());
            holder.detailHintTextView.setText("点击查看详情 →");

            // 添加点击事件到整个卡片
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 跳转到详情页面
                    Intent intent = new Intent(context, NewsDetailActivity.class);
                    intent.putExtra("title", item.getTitle());
                    intent.putExtra("description", item.getDescription());
                    intent.putExtra("imageRes", item.getImageRes());
                    intent.putExtra("date", item.getDate());
                    intent.putExtra("source", item.getSource());
                    context.startActivity(intent);

                    // 添加Activity切换动画
                    if (context instanceof Activity) {
                        ((Activity) context).overridePendingTransition(
                                android.R.anim.fade_in,
                                android.R.anim.fade_out
                        );
                    }
                }
            });

            return convertView;
        }

        class ViewHolder {
            CardView cardView;
            ImageView imageView;
            TextView titleTextView;
            TextView descTextView;
            TextView dateTextView;
            TextView sourceTextView;
            TextView detailHintTextView;
        }
    }

    // 新闻数据类
    class NewsItem {
        private String title;
        private String description;
        private int imageRes;
        private String date;
        private String source;

        public NewsItem(String title, String description, int imageRes, String date, String source) {
            this.title = title;
            this.description = description;
            this.imageRes = imageRes;
            this.date = date;
            this.source = source;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public int getImageRes() {
            return imageRes;
        }

        public String getDate() {
            return date;
        }

        public String getSource() {
            return source;
        }
    }
}