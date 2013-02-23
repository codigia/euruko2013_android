package org.hypest.erk13;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class Markers extends ItemizedOverlay<OverlayItem> {

    private List<OverlayItem> locations = new ArrayList<OverlayItem>();
    private Drawable marker;

    public Markers(Context ctx, int defaultMarkerId) {
        super(boundCenterBottom(ctx.getResources().getDrawable(defaultMarkerId)));
        marker = ctx.getResources().getDrawable(defaultMarkerId);
    }

    public void add(String name, float lat, float lng) {
        GeoPoint myPlace = new GeoPoint((int)(lat * 1E6), (int)(lng * 1E6));
        locations.add(new OverlayItem(myPlace, name, name));
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
}
