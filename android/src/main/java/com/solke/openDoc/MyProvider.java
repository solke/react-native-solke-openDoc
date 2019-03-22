package com.solke.openDoc;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import java.io.File;
import java.io.FileNotFoundException;

public class MyProvider extends ContentProvider {


    private static String mAuthority;

    @Override
    public void attachInfo(Context context, ProviderInfo info) {    super.attachInfo(context, info);    // Sanity check our security
        if (info.exported) {
            throw new SecurityException("Provider must not be exported");
        }
        if (!info.grantUriPermissions) {
            throw new SecurityException("Provider must grant uri permissions");
        }
        mAuthority = info.authority;
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        File file = new File(uri.getPath());
        ParcelFileDescriptor parcel = ParcelFileDescriptor.open(file,ParcelFileDescriptor.MODE_READ_ONLY);
        return parcel;
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public int delete(Uri uri, String s, String[] as) {
        throw new UnsupportedOperationException("Not supported by this quipoogtofileprovider");
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not supported by this quipoogtofileprovider");
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentvalues) {
        throw new UnsupportedOperationException("Not supported by this quipoogtofileprovider");
    }

    @Override
    public Cursor query(Uri uri, String[] as, String s, String[] as1, String s1) {        throw new UnsupportedOperationException("Not supported by this quipoogtofileprovider");
    }

    @Override
    public int update(Uri uri, ContentValues contentvalues, String s, String[] as) {
        throw new UnsupportedOperationException("Not supported by this quipoogtofileprovider");
    }

    public static Uri getUriForFile(File file) {
        return new Uri.Builder().scheme("content").authority(mAuthority).encodedPath(file.getPath()).build();
    }


}