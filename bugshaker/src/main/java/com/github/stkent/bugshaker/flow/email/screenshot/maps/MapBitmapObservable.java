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

import android.graphics.Bitmap;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import rx.Observable;
import rx.Subscriber;

final class MapBitmapObservable {

    @MainThread
    static Observable<MapBitmap> create(@NonNull final MapBitmapProvider mapBitmapProvider) {
        final int[] locationOnScreen = new int[] {0, 0};
        mapBitmapProvider.getLocationOnScreen(locationOnScreen);

        return Observable.create(new Observable.OnSubscribe<MapBitmap>() {
            @Override
            public void call(final Subscriber<? super MapBitmap> subscriber) {
                mapBitmapProvider.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull final GoogleMap googleMap) {
                        googleMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
                            @Override
                            public void onSnapshotReady(@Nullable final Bitmap bitmap) {
                                if (bitmap != null) {
                                    subscriber.onNext(
                                            new MapBitmap(bitmap, locationOnScreen));

                                    subscriber.onCompleted();
                                } else {
                                    subscriber.onError(new MapSnapshotFailedException());
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    private MapBitmapObservable() {

    }

}
