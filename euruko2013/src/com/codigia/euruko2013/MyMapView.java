package com.codigia.euruko2013;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.google.android.maps.MapView;

public class MyMapView extends MapView {
    private long lastTouchTime = -1;

    public MyMapView(Context ctx) {
        this(ctx, null, 0);
    }

    public MyMapView(Context ctx, AttributeSet as) {
        this(ctx, as, 0);
    }

    public MyMapView(Context ctx, AttributeSet as, int ds) {
        super(ctx, as, ds);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            long thisTime = System.currentTimeMillis();
            if (thisTime - lastTouchTime < 250) {
                // Double tap
                this.getController().zoomInFixing((int) ev.getX(), (int) ev.getY());
                lastTouchTime = -1;
            } else {
                // Too slow :)
                lastTouchTime = thisTime;
            }
        }

        return super.onInterceptTouchEvent(ev);
    }
}