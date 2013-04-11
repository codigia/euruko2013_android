package org.hypest.erk13;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;

public class Markers extends BalloonItemizedOverlay<OverlayItem> {

    private List<OverlayItem> locations = new ArrayList<OverlayItem>();
    private List<Runnable> runnables = new ArrayList<Runnable>();
    private Drawable marker;

	public Markers(Context ctx, int defaultMarkerId, MapView map) {
		super(boundCenterBottom(ctx.getResources().getDrawable(
						defaultMarkerId)), map);
		marker = ctx.getResources().getDrawable(defaultMarkerId);
	}

    public void add(String name, String subtitle, float lat, float lng,
    		Runnable runnable) {
        GeoPoint myPlace = new GeoPoint((int)(lat * 1E6), (int)(lng * 1E6));
        locations.add(new OverlayItem(myPlace, name, subtitle));
        runnables.add(runnable);
        populate();
    }

    @Override
    protected OverlayItem createItem(int i) {
        // TODO Auto-generated method stub
        return locations.get(i);
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return locations.size();
    }

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        // TODO Auto-generated method stub
        super.draw(canvas, mapView, shadow);

        boundCenterBottom(marker);
    }

    @Override
    protected boolean onBalloonTap(int index, OverlayItem item) {
    	runnables.get(index).run();
    	return true;
    }
}
