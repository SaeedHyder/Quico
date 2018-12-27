package com.app.quico.map.abstracts;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore.Images;

import com.app.quico.activities.DockActivity;
import com.app.quico.activities.MainActivity;
import com.app.quico.entities.MapScreenItem;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;

/**
 * Created by saeedhyder on 4/7/2017.
 */

public class MapMarkerItemBinder extends MapMarkerBinder<MapScreenItem> {

    private MainActivity activity;
    DockActivity dockActivity;
    Bitmap bitmap;
    ImageLoader imageLoader;
    Bitmap myBitmap;

    public MapMarkerItemBinder(MainActivity activity, DockActivity dockActivity) {
        this.activity = activity;
        this.dockActivity = dockActivity;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    @Override
    public void addMarker(final GoogleMap googleMap, final MapScreenItem entity, final int position) {

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.valueOf(entity.getLat()), Double.valueOf(entity.getLng())))
                .icon(BitmapDescriptorFactory.fromResource(entity.getMarker())));

    }
}

