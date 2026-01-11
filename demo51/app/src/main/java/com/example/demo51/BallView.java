package com.example.demo51;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class BallView extends View {

    // 小球属性
    private PointF ballPosition; // 小球当前位置
    private float ballRadius = 60f; // 小球半径
    private int ballColor = Color.RED; // 小球颜色

    // 绘图工具
    private Paint ballPaint;
    private Paint shadowPaint;

    // 动画相关
    private float touchX, touchY;
    private boolean isFollowing = false;
    private float animationSpeed = 0.2f; // 动画速度系数

    // 构造函数
    public BallView(Context context) {
        super(context);
        init();
    }

    public BallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 初始化小球位置为View中心
        ballPosition = new PointF(0, 0);

        // 初始化小球画笔
        ballPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ballPaint.setColor(ballColor);
        ballPaint.setStyle(Paint.Style.FILL);

        // 初始化阴影画笔
        shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shadowPaint.setColor(Color.argb(100, 0, 0, 0));
        shadowPaint.setStyle(Paint.Style.FILL);

        // 设置View可以接收触摸事件
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 初始化小球位置为View中心
        ballPosition.x = w / 2f;
        ballPosition.y = h / 2f;
        touchX = ballPosition.x;
        touchY = ballPosition.y;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制背景
        canvas.drawColor(Color.LTGRAY);

        // 绘制阴影（在小球下方）
        canvas.drawCircle(
                ballPosition.x + ballRadius * 0.1f,
                ballPosition.y + ballRadius * 0.1f,
                ballRadius,
                shadowPaint
        );

        // 绘制小球
        canvas.drawCircle(ballPosition.x, ballPosition.y, ballRadius, ballPaint);

        // 绘制小球高光
        Paint highlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        highlightPaint.setColor(Color.argb(150, 255, 255, 255));
        highlightPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(
                ballPosition.x - ballRadius * 0.3f,
                ballPosition.y - ballRadius * 0.3f,
                ballRadius * 0.4f,
                highlightPaint
        );

        // 如果正在跟随手指，则继续动画
        if (isFollowing) {
            updateBallPosition();
            invalidate(); // 请求重绘，实现动画效果
        }
    }

    // 更新小球位置，实现平滑移动
    private void updateBallPosition() {
        // 计算当前点到目标点的距离
        float dx = touchX - ballPosition.x;
        float dy = touchY - ballPosition.y;

        // 如果距离很小，直接设置位置
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        if (distance < 2) {
            ballPosition.x = touchX;
            ballPosition.y = touchY;
            isFollowing = false;
            return;
        }

        // 平滑移动到目标位置
        ballPosition.x += dx * animationSpeed;
        ballPosition.y += dy * animationSpeed;

        // 确保小球不会超出View边界
        ensureBallInBounds();
    }

    // 确保小球在View边界内
    private void ensureBallInBounds() {
        int width = getWidth();
        int height = getHeight();

        if (ballPosition.x - ballRadius < 0) {
            ballPosition.x = ballRadius;
        } else if (ballPosition.x + ballRadius > width) {
            ballPosition.x = width - ballRadius;
        }

        if (ballPosition.y - ballRadius < 0) {
            ballPosition.y = ballRadius;
        } else if (ballPosition.y + ballRadius > height) {
            ballPosition.y = height - ballRadius;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 检查是否点击在小球上
                float x = event.getX();
                float y = event.getY();
                float distance = (float) Math.sqrt(
                        Math.pow(x - ballPosition.x, 2) +
                                Math.pow(y - ballPosition.y, 2)
                );

                if (distance <= ballRadius) {
                    // 点击在小球上，直接设置位置
                    touchX = x;
                    touchY = y;
                    ballPosition.x = x;
                    ballPosition.y = y;
                    isFollowing = false;
                } else {
                    // 点击在其他位置，开始跟随
                    touchX = x;
                    touchY = y;
                    isFollowing = true;
                }
                invalidate();
                return true;

            case MotionEvent.ACTION_MOVE:
                // 更新目标位置
                touchX = event.getX();
                touchY = event.getY();
                isFollowing = true;
                invalidate();
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 停止跟随
                isFollowing = false;
                return true;
        }

        return super.onTouchEvent(event);
    }

    // 公共方法：设置小球颜色
    public void setBallColor(int color) {
        ballColor = color;
        ballPaint.setColor(ballColor);
        invalidate();
    }

    // 公共方法：设置小球半径
    public void setBallRadius(float radius) {
        ballRadius = Math.max(20, Math.min(radius, 200));
        invalidate();
    }

    // 公共方法：设置动画速度
    public void setAnimationSpeed(float speed) {
        animationSpeed = Math.max(0.05f, Math.min(speed, 1.0f));
    }

    // 公共方法：获取小球当前位置
    public PointF getBallPosition() {
        return new PointF(ballPosition.x, ballPosition.y);
    }

    // 新增方法：获取小球半径
    public float getBallRadius() {
        return ballRadius;
    }
}
