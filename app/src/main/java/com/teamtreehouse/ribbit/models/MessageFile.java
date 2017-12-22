package com.teamtreehouse.ribbit.models;

import android.net.Uri;

public class MessageFile {
    private String fileName;
    private byte[] fileBytes;
    private Uri uri;

    public MessageFile(String fileName, byte[] fileBytes, Uri uri) {
        this.fileName = fileName;
        this.fileBytes = fileBytes;
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }
}
