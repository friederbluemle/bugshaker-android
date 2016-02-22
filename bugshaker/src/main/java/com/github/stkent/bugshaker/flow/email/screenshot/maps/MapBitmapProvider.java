package com.github.stkent.bugshaker.flow.email.screenshot.maps;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.OnMapReadyCallback;

public interface MapBitmapProvider {

    void getMapAsync(@NonNull final OnMapReadyCallback callback);

    void getLocationOnScreen(int[] locationOnScreen);

}
