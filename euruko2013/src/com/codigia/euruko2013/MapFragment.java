package com.codigia.euruko2013;

import java.util.ArrayList;
import java.util.List;

import com.codigia.euruko2013.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends SupportMapFragment {

	View mMapView;
	GoogleMap mMap;
	List<Marker> mMarkers = new ArrayList<Marker>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		if (mMapView == null) {
			mMapView = inflater.inflate(R.layout.map, null);
		} else {
			((ViewGroup) (mMapView.getParent())).removeView(mMapView);
		}

		return mMapView;
	}

	@Override
	public void onResume() {
		final BaseActivity ba = (BaseActivity) getActivity();

		ba.setBarTitle("Map          ");

		setUpMapIfNeeded();

        super.onResume();
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
	    if (mMap == null) {
			mMap = ((SupportMapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
	        if (mMap != null) {
				// The Map is verified. It is now safe to manipulate the map.

	        	mMap.getUiSettings().setZoomControlsEnabled(true);

	        	new Handler().postDelayed(mDelayedLoad, 300);
	        }
	    }
	}

	protected Runnable mDelayedLoad = new Runnable() {
		@Override
		public void run() {
			mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
				@Override
				public void onInfoWindowClick(Marker marker) {
					Intent browserIntent = new Intent(
					Intent.ACTION_VIEW,
					Uri.parse("http://euruko2013.org/#venue"));
					getActivity().startActivity(browserIntent);
				}
			});

			LatLng ll = new LatLng(37.986067f, 23.774682f);
			mMarkers.add(mMap.addMarker(new MarkerOptions()
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ruby_marker))
					.position(ll)
					.title("Badminton Theater")
					.snippet("The EuRuKo2013 venue!")));

			mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 12));
		}
	};
}
