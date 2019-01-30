package com.app.quico.helpers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;

import com.app.quico.activities.DockActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.ByteArrayOutputStream;
//import org.apache.commons.io.FileUtils;

/**
 * Created by saeedhyder on 5/22/2018.
 */

public class ShareIntentHelper {


    private ShareIntentHelper() {
    }

    public static void shareTextIntent(DockActivity context, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.onLoadingFinished();
        try {
            //  context.startActivity(intent);
            context.startActivity(Intent.createChooser(intent, "send"));
        } catch (android.content.ActivityNotFoundException ex) {
            UIHelper.showShortToastInCenter(context, "App have not been installed.");
        }
    }


    public static void shareImageAndTextResultIntent(DockActivity context, String image, String text) {
        try {

            getBitmap(image, context, text);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void getBitmap(String image, final DockActivity context, final String text) {

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.loadImage(image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                context.onLoadingFinished();
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                context.onLoadingFinished();
                if (getImageUri(context, loadedImage) != null) {
                    Uri imageUrl = getImageUri(context, loadedImage);
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, text);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUrl);
                    shareIntent.setType("image/jpeg");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    context.startActivity(Intent.createChooser(shareIntent, "send"));
                } else {
                    UIHelper.showShortToastInCenter(context, "try again...");
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                context.onLoadingFinished();
            }
        });

       /* Picasso.with(context)
                .load(image)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        context.onLoadingFinished();
                        Uri imageUri = getImageUri(context,bitmap);
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                        shareIntent.setType("image/jpeg");
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                       context.startActivity(Intent.createChooser(shareIntent, "send"));


                     //  context.startActivity(shareIntent);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        context.onLoadingFinished();
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }

                });*/


    }


    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        if (inImage != null) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
            if (path != null) {
                return Uri.parse(path);
            } else {
                return null;
            }
        } else {
            return null;
        }

    }
}
