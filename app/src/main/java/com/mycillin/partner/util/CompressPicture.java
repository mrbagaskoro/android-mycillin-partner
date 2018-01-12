package com.mycillin.partner.util;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

public class CompressPicture {
    public Bitmap scaleBitmap(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        int maxWidthPortrait = Configs.maxHeight;
        int maxHeightPortrait = Configs.maxWidth;

        int maxWidthLandscape = Configs.maxWidth;
        int maxHeightLandscape = Configs.maxHeight;

        // PORTRAIT
        if (width < height) {
            if (width > maxWidthPortrait || height > maxHeightPortrait) {
                width = maxWidthPortrait;
                height = maxHeightPortrait;
            }
        }

        // LANDSCAPE
        else {
            if (width > maxWidthLandscape || height > maxHeightLandscape) {
                width = maxWidthLandscape;
                height = maxHeightLandscape;
            }
        }

        Timber.tag("###").d("scaleBitmap: after " + width + "x" + height);
        bm = Bitmap.createScaledBitmap(bm, width, height, true);
        return bm;
    }

    public Uri saveResizedImage(Bitmap finalBitmap) {

        String IMAGE_DIRECTORY_NAME = Configs.IMAGE_DIRECTORY;

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);
        mediaStorageDir.mkdir();
        String path = mediaStorageDir.getPath();

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Timber.tag("IMG").d("Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
            } else {
                Timber.tag("IMG").d("Success create " + IMAGE_DIRECTORY_NAME + " directory");
                createNomediaFile(path);
            }
        } else {
            createNomediaFile(path);
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

        if (mediaFile.exists()) mediaFile.delete();
        try {
            FileOutputStream out = new FileOutputStream(mediaFile);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Uri.fromFile(mediaFile);
    }

    private void createNomediaFile(String path) {
        try {
            File output = new File(path, ".nomedia");
            boolean fileCreated = output.createNewFile();
            Timber.tag("###").d("saveResizedImage: %s", fileCreated);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
