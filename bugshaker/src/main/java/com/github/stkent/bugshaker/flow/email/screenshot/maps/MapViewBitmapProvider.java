/**
 * Copyright 2016 Stuart Kent
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
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
