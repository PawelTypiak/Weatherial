/*******************************************************************************
 * Copyright 2017 Paweł Typiak
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
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

