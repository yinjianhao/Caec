package com.me.caec.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 加载gif图片
 *
 * @auther yjh
 * @date 2016/9/4
 */
public class GifImageView extends ImageView {

    private Movie movie;

    private long movieStart;

    public GifImageView(Context context) {
        super(context);
    }

    public GifImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GifImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int image = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0);

        movie = Movie.decodeStream(getResources().openRawResource(image));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //获取系统当前时间
        long nowTime = android.os.SystemClock.currentThreadTimeMillis();
        if (movieStart == 0) {
            //若为第一次加载，开始时间置为nowTime
            movieStart = nowTime;
        }
        if (movie != null) {//容错处理
            int duration = movie.duration();//获取gif持续时间
            //获取gif当前帧的显示所在时间点
            int relTime = (int) ((nowTime - movieStart) % duration);
            movie.setTime(relTime);
            //渲染gif图像
            movie.draw(canvas, 0, 0);
            invalidate();
        }
    }
}
