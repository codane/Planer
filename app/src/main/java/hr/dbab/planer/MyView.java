package hr.dbab.planer;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.collection.LruCache;

public class MyView extends View {

    private static TextPaint titlePaint;
    private static TextPaint descriptionPaint;


    private StaticLayout titleLayout;
    private StaticLayout timeLayout;

    private final int iconSize = (int) dp(56);
    private final int iconMargin = (int) dp(8);
    private final int verticalPadding = (int) dp(4);


    public MyView(Context context) {
        super(context);
        init();
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int textHeight = titleLayout.getHeight() + timeLayout.getHeight();
        int viewHeight = 2 * verticalPadding;
        setMeasuredDimension(width, viewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(2 * iconMargin + iconSize, verticalPadding);
        titleLayout.draw(canvas);
        canvas.translate(0, titleLayout.getHeight());
        timeLayout.draw(canvas);
        canvas.restore();
    }


    public void setTask(Task task) {
        titleLayout = LayoutCache.INSTANCE.titleLayoutFor(task.getTitle());
        timeLayout = LayoutCache.INSTANCE.descriptionLayoutFor(task.getTime());

        requestLayout();
        invalidate();
    }

    private void init() {
        if (titlePaint == null) {
            titlePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            titlePaint.setColor(Color.BLACK);
            titlePaint.setTextSize(sp(22));
        }

        if (descriptionPaint == null) {
            descriptionPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            descriptionPaint.setColor(Color.BLUE);
            descriptionPaint.setTextSize(sp(18));
        }
    }

    private float sp(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    private float dp(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private enum LayoutCache {
        INSTANCE;

        private int width;
        private final LruCache<CharSequence, StaticLayout> titleCache = new LruCache<CharSequence, StaticLayout>(100) {
            @Override
            protected StaticLayout create(CharSequence key) {
                CharSequence truncatedTitle = TextUtils.ellipsize(key, titlePaint, width, TextUtils.TruncateAt.END);
                return new StaticLayout(truncatedTitle, titlePaint, width, Layout.Alignment.ALIGN_NORMAL, 1, 1, true);
            }
        };
        private final LruCache<CharSequence, StaticLayout> descriptionCache = new LruCache<CharSequence, StaticLayout>(100) {
            @Override
            protected StaticLayout create(CharSequence key) {
                return new StaticLayout(key, descriptionPaint, width, Layout.Alignment.ALIGN_NORMAL, 1, 1, true);
            }
        };

        public void changeWidth(int newWidth) {
            if (width != newWidth) {
                width = newWidth;
                titleCache.evictAll();
                descriptionCache.evictAll();
            }
        }

        public StaticLayout titleLayoutFor(CharSequence text) {
            return titleCache.get(text);
        }

        public StaticLayout descriptionLayoutFor(CharSequence text) {
            return descriptionCache.get(text);
        }
    }
}
