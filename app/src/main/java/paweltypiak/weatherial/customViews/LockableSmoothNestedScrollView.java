package paweltypiak.weatherial.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import me.henrytao.smoothappbarlayout.widget.NestedScrollView;

public class LockableSmoothNestedScrollView extends NestedScrollView {

    private boolean isScrollable = true;

    public LockableSmoothNestedScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LockableSmoothNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LockableSmoothNestedScrollView(Context context) {
        super(context);
    }

    public void setScrollingEnabled(boolean isScrollable) {
        this.isScrollable = isScrollable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isScrollable) return super.onTouchEvent(ev);
                return isScrollable;
            default:
                return super.onTouchEvent(ev);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isScrollable) return super.onInterceptTouchEvent(ev);
                return isScrollable;
            default:
                return super.onInterceptTouchEvent(ev);
        }
    }
}

