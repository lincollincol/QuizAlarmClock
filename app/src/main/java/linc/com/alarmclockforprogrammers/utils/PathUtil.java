package linc.com.alarmclockforprogrammers.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class PathUtil {

    private Context context;

    public PathUtil(Context context) {
        this.context = context;
    }

    @SuppressLint("NewApi")
    public String getPath(final Uri uri) {
        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            if(isSDCard(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return ("/storage/"+split[0]+File.separator + split[1]);

            }else if (isExternalStorageDocument(uri)) {
                // ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }else if (isDownloadsDocument(uri)) {
                // DownloadsProvider
                return getDownloadFilePath(uri);

            }else if (isMediaDocument(uri)) {
                // MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                Uri contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(contentUri, selection, selectionArgs);
            }

        }else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // MediaStore (and general)
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(uri, null, null);

        }else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // File
            return uri.getPath();
        }

        return null;
    }

    private String getDataColumn(Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };
        try {
            cursor = context.getContentResolver()
                    .query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String getDownloadFilePath(Uri uri){
        Cursor cursor = null;
        final String[] projection = { MediaStore.MediaColumns.DISPLAY_NAME };
        try {
            cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                String fileName=cursor.getString(0);
                String path =Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName;
                if (!TextUtils.isEmpty(path)){
                    return path;
                }
            }
        } finally {
            cursor.close();
        }
        String id = DocumentsContract.getDocumentId(uri);
        if (id.startsWith("raw:")) {
            return id.replaceFirst("raw:", "");
        }
        Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads"), java.lang.Long.valueOf(id));

        return getDataColumn(contentUri, null, null);
    }


    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    private boolean isSDCard(Uri uri) {
        // Final set of paths
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");

        //Get primary & secondary external device storage (internal storage & micro SDCARD slot...)
        File[]  listExternalDirs = ContextCompat.getExternalFilesDirs(context, null);

        for (File listExternalDir : listExternalDirs) {
            if (listExternalDir != null) {
                String path = listExternalDir.getAbsolutePath();
                int indexMountRoot = path.indexOf("/Android/data/");
                if (indexMountRoot >= 0 && indexMountRoot <= path.length()) {
                    //Get the root path for the external directory
                    if(split[0].equals(path.substring(0, indexMountRoot))) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    /*
    private boolean isSDCard(Uri uri) {
        // Final set of paths
        final Set<String> rv = new HashSet<>();
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");

        //Get primary & secondary external device storage (internal storage & micro SDCARD slot...)
        File[]  listExternalDirs = ContextCompat.getExternalFilesDirs(context, null);

        for (File listExternalDir : listExternalDirs) {
            if (listExternalDir != null) {
                String path = listExternalDir.getAbsolutePath();
                int indexMountRoot = path.indexOf("/Android/data/");
                if (indexMountRoot >= 0 && indexMountRoot <= path.length()) {
                    //Get the root path for the external directory
                    rv.add(path.substring(0, indexMountRoot));
                }
            }
        }
        for(String path : rv.toArray(new String[rv.size()])) {
            if(path.contains(split[0])) {
                return true;
            }
        }

        return false;
    }
     */

}