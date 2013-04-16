package org.hypest.erk13;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends SupportMapFragment {

	GoogleMap mMap;
	List<Marker> mMarkers = new ArrayList<Marker>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		return inflater.inflate(R.layout.map, null);
	}

	@Override
	public void onResume() {
		final BaseActivity ba = (BaseActivity) getActivity();

		ba.setBarTitle("Map");

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

	        	new Handler().post(mDelayedLoad);
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

			mMarkers.add(mMap.addMarker(new MarkerOptions()
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ruby_marker))
					.position(new LatLng(37.986067f, 23.774682f))
					.title("Badminton Theater")
					.snippet("The EuRuKo2013 venue!")));
		}
	};
}
