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

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.github.stkent.bugshaker.flow.email.screenshot.BaseScreenshotProvider;
import com.github.stkent.bugshaker.utilities.ActivityUtils;
import com.github.stkent.bugshaker.utilities.Logger;
import com.google.android.gms.maps.MapView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

public final class MapScreenshotProvider extends BaseScreenshotProvider {

    private static final Func2<Bitmap, List<LocatedBitmap>, Bitmap> BITMAP_COMBINING_FUNCTION
            = new Func2<Bitmap, List<LocatedBitmap>, Bitmap>() {
                @Override
                public Bitmap call(
                        final Bitmap baseBitmap,
                        final List<LocatedBitmap> locatedOverlayBitmaps) {

                    for (final LocatedBitmap locatedBitmap : locatedOverlayBitmaps) {
                        final Canvas canvas = new Canvas(baseBitmap);
                        final int[] overlayLocation = locatedBitmap.getLocation();

                        canvas.drawBitmap(
                                locatedBitmap.getBitmap(),
                                overlayLocation[0],
                                overlayLocation[1],
                                MAP_PAINT);
                    }

                    return baseBitmap;
                }
    };

    private static final Paint MAP_PAINT = new Paint();

    static {
        MAP_PAINT.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
    }

    public MapScreenshotProvider(
            @NonNull final Context applicationContext,
            @NonNull final Logger logger) {

        super(applicationContext, logger);
    }

    @NonNull
    @Override
    public Observable<Bitmap> getScreenshotBitmap(@NonNull final Activity activity) {
        final Observable<Bitmap> nonMapViewsBitmapObservable = getNonMapViewsBitmap(activity);

        final View rootView = ActivityUtils.getRootView(activity);
        final List<MapBitmapProvider> mapBitmapProviders = locateMapsInHierarchy(rootView);

        if (mapBitmapProviders.isEmpty()) {
            return nonMapViewsBitmapObservable;
        } else {
            final Observable<List<LocatedBitmap>> mapViewBitmapsObservable
                    = getMapBitmapsObservable(mapBitmapProviders);

            return Observable.zip(
                    nonMapViewsBitmapObservable,
                    mapViewBitmapsObservable,
                    BITMAP_COMBINING_FUNCTION);
        }
    }

    @NonNull
    private Observable<List<LocatedBitmap>> getMapBitmapsObservable(
            @NonNull final List<MapBitmapProvider> mapBitmapProviders) {

        return Observable
                .from(mapBitmapProviders)
                .concatMap(new Func1<MapBitmapProvider, Observable<LocatedBitmap>>() {
                    @Override
                    public Observable<LocatedBitmap> call(
                            @NonNull final MapBitmapProvider mapBitmapProvider) {

                        return MapBitmapObservable.create(mapBitmapProvider);
                    }
                })
                .toList();
    }

    @NonNull
    private List<MapBitmapProvider> locateMapsInHierarchy(@NonNull final View view) {
        final List<MapBitmapProvider> result = new ArrayList<>();

        if (view instanceof MapView && view.getVisibility() == View.VISIBLE) {
            result.add(new MapViewBitmapProvider((MapView) view));
        } else if (view instanceof ViewGroup) {
            final ViewGroup viewGroup = (ViewGroup) view;

            for (int childIndex = 0; childIndex < viewGroup.getChildCount(); childIndex++) {
                result.addAll(locateMapsInHierarchy(viewGroup.getChildAt(childIndex)));
            }
        }

        return result;
    }

}
