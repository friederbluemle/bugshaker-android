package com.github.stkent.bugshaker.flow.email.screenshot.maps;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

final class MapViewBitmapProvider implements MapBitmapProvider {

    @NonNull
    private final MapView mapView;

    MapViewBitmapProvider(@NonNull final MapView mapView) {
        this.mapView = mapView;
    }

    @Override
    public void getMapAsync(@NonNull final OnMapReadyCallback callback) {
        mapView.getMapAsync(callback);
    }

    @Override
    public void getLocationOnScreen(final int[] locationOnScreen) {
        mapView.getLocationOnScreen(locationOnScreen);
    }

}
