package com.solke.openDoc;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableArray;

/* bridge react native
int size();
boolean isNull(int index);
boolean getBoolean(int index);
double getDouble(int index);
int getInt(int index);
String getString(int index);
ReadableArray getArray(int index);
ReadableMap getMap(int index);
ReadableType getType(int index);
*/
//Third Libraries
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.support.v4.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.webkit.CookieManager;
import android.webkit.MimeTypeMap;

public class RNReactNativeDocViewerModule extends ReactContextBaseJavaModule {
    public static final int ERROR_NO_HANDLER_FOR_DATA_TYPE = 53;
    public static final int ERROR_FILE_NOT_FOUND = 2;
    public static final int ERROR_UNKNOWN_ERROR = 1;
    private final ReactApplicationContext reactContext;

    public RNReactNativeDocViewerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNDocViewer";
    }

    @ReactMethod
    public void openFile(ReadableArray args, Callback callback){
        System.out.println("====================");
        final ReadableMap arg_object = args.getMap(0);

        try {
            if (arg_object.getString("url") != null && arg_object.getString("fileName") != null) {
                // parameter parsing
                final String filPath = arg_object.getString("url");
                final String fileName =arg_object.getString("fileName");
                final String type =arg_object.getString("fileType");
                handleFile(filPath,fileName,type,callback);
                // Begin the Download Task

            }else{
                callback.invoke(false);
            }
        } catch (Exception e) {
            callback.invoke(e.getMessage());
        }
    }

    public void handleFile(final String url,final String fileName,final String fileType,Callback callback){
//        Context currentActivity = getCurrentActivity();
        Context currentActivity = getCurrentActivity();
        switch (fileType){
            case "pdf":
                Intent pdfIntent =  OpenFiles.getPdfFileIntent(url);
                startIntentAction(pdfIntent,callback,fileName);
                break;
            case "word":
                Intent wordIntent =  OpenFiles.getWordFileIntent(url);
                startIntentAction(wordIntent,callback,fileName);
                break;
            case "ppt":
                Intent pptIntent =  OpenFiles.getPPTFileIntent(url);
                startIntentAction(pptIntent,callback,fileName);
                break;
            case "txt":
                Intent textIntent =  OpenFiles.getTextFileIntent(url);
                startIntentAction(textIntent,callback,fileName);
                break;
            case "excel":
                Intent excelIntent =  OpenFiles.getExcelFileIntent(url);
                startIntentAction(excelIntent,callback,fileName);
                break;
            case "audio":
                Intent audioIntent =  OpenFiles.getAudioFileIntent(url);
                startIntentAction(audioIntent,callback,fileName);
                break;
            case "video":
                Intent videoIntent =  OpenFiles.getVideoFileIntent(url);
                startIntentAction(videoIntent,callback,fileName);
                break;
            case "apk":
                Intent apkIntent =  OpenFiles.getApkFileIntent(url);
                startIntentAction(apkIntent,callback,fileName);
                break;
            case "zip":
                Intent zipIntent =  OpenFiles.getZipFileIntent(url);
                startIntentAction(zipIntent,callback,fileName);
                break;
        }
    }

    public void startIntentAction(Intent intent,Callback callback,String fileName){
        Context context = getCurrentActivity();
        try{
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
                // Thread-safe.
                callback.invoke(null, fileName);
            } else {
                callback.invoke(("Activity not found to handle: " + intent.toString()));
            }

        }catch (Exception e){
            //没有安装第三方的软件会提示
            callback.invoke(e.getMessage());
        }



    }

    @ReactMethod
    public void openDoc(ReadableArray args, Callback callback) {
//      final ReadableMap arg_object = args.getMap(0);
//      try {
//        if (arg_object.getString("url") != null && arg_object.getString("fileName") != null) {
//            // parameter parsing
//            final String url = arg_object.getString("url");
//            final String fileName =arg_object.getString("fileName");
//            // Begin the Download Task
//            new FileDownloaderAsyncTask(callback, url, fileName).execute();
//        }else{
//            callback.invoke(false);
//        }
//       } catch (Exception e) {
//            callback.invoke(e.getMessage());
//       }

        openFile(args,callback);
    }


    @ReactMethod
    public void openDocb64(ReadableArray args, Callback callback) {
        final ReadableMap arg_object = args.getMap(0);
        try {
            if (arg_object.getString("base64") != null && arg_object.getString("fileName") != null && arg_object.getString("fileType") != null) {
                // parameter parsing
                final String base64 = arg_object.getString("base64");
                final String fileName =arg_object.getString("fileName");
                final String fileType =arg_object.getString("fileType");
                // Begin the Download Task
                //new FileDownloaderAsyncTask(callback, url, fileName).execute();
            }else{
                callback.invoke(false);
            }
        } catch (Exception e) {
            callback.invoke(e.getMessage());
        }
    }

    // used for all downloaded files, so we can find and delete them again.
    private final static String FILE_TYPE_PREFIX = "PP_";
    /**
     * downloads the file from the given url to external storage.
     *
     * @param url
     * @return
     */
    private File downloadFile(String url, Callback callback) {

        try {
            // get an instance of a cookie manager since it has access to our
            // auth cookie
            CookieManager cookieManager = CookieManager.getInstance();

            // get the cookie string for the site.
            String auth = null;
            if (cookieManager.getCookie(url) != null) {
                auth = cookieManager.getCookie(url).toString();
            }

            URL url2 = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
            if (auth != null) {
                conn.setRequestProperty("Cookie", auth);
            }

            InputStream reader = conn.getInputStream();

            String extension = MimeTypeMap.getFileExtensionFromUrl(url);
            if (extension.equals("")) {
                extension = "pdf";
                System.out.println("extension (default): " + extension);
            }

            Context context = getReactApplicationContext().getBaseContext();
            File outputDir = context.getCacheDir();
            File f = File.createTempFile(FILE_TYPE_PREFIX, "." + extension,
                    outputDir);
            // make sure the receiving app can read this file
            f.setReadable(true, false);
            System.out.println(f.getPath());
            FileOutputStream outStream = new FileOutputStream(f);

            byte[] buffer = new byte[1024];
            int readBytes = reader.read(buffer);
            while (readBytes > 0) {
                outStream.write(buffer, 0, readBytes);
                readBytes = reader.read(buffer);
            }
            reader.close();
            outStream.close();
            if (f.exists()) {
                System.out.println("File exists");
            } else {
                System.out.println("File doesn't exist");
            }
            return f;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            callback.invoke(ERROR_FILE_NOT_FOUND);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            callback.invoke(ERROR_UNKNOWN_ERROR);
            return null;
        }
    }
    /**
     * Returns the MIME Type of the file by looking at file name extension in
     * the URL.
     *
     * @param url
     * @return
     */
    private static String getMimeType(String url) {
        String mimeType = null;

        String extension = MimeTypeMap.getFileExtensionFromUrl(Uri.encode(url));
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            mimeType = mime.getMimeTypeFromExtension(extension);
        }

        System.out.println("Mime Type: " + mimeType);

        if (mimeType == null) {
            mimeType = "application/pdf";
            System.out.println("Mime Type (default): " + mimeType);
        }

        return mimeType;
    }

    private class FileDownloaderAsyncTask extends AsyncTask<Void, Void, File> {
        private final Callback callback;
        private final String url;
        private final String fileName;

        public FileDownloaderAsyncTask(Callback callback,
                                       String url, String fileName) {
            super();
            this.callback = callback;
            this.url = url;
            this.fileName = fileName;
        }

        @Override
        protected File doInBackground(Void... arg0) {
            if (!url.startsWith("file://")) {
                return downloadFile(url, callback);
            } else {

                Context context = getCurrentActivity();
                File file = new File(context.getFilesDir(),url.replace("file://", ""));
//                File file = new File(url.replace("file://", ""));
                return file;
            }
        }

        @Override
        protected void onPostExecute(File result) {
            if (result == null) {
                // Has already been handled
                return;
            }

            Context context = getCurrentActivity();

            // mime type of file data
            String mimeType = getMimeType(url);
            if (mimeType == null || context == null) {
                return;
            }
            try {
                Uri contentUri = FileProvider.getUriForFile(context, "com.solke.openDoc.quipoogtofileprovider", result);
                System.out.println("ContentUri");
                System.out.println(contentUri);
                System.out.println(context.getFilesDir());
                System.out.println(result);
                System.out.println("++++++++++++++++++++++++");
                File imagePath = new File(context.getFilesDir(), "shareFile");
                File newFile = new File(context.getFilesDir(), "shareFile/file1505784920993/file1505784920993.doc");
                Uri ctenUri = FileProvider.getUriForFile(context, "com.solke.openDoc.doc.quipoogtofileprovider", newFile);

                System.out.print(ctenUri);



                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(contentUri, mimeType);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                    // Thread-safe.
                    callback.invoke(null, fileName);
                } else {
                    activityNotFoundMessage("Activity not found to handle: " + contentUri.toString() + " (" + mimeType + ")");
                }
            } catch (ActivityNotFoundException e) {
                activityNotFoundMessage(e.getMessage());
            }

        }

        private void activityNotFoundMessage(String message) {
            System.out.println("ERROR");
            System.out.println(message);
            callback.invoke(message);
            //e.printStackTrace();
        }
    }
}
