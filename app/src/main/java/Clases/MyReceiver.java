package Clases;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;

import com.underpro.descarga.Pantalla_Actualizar;
import com.underpro.descarga.ReceiverListener;

import java.io.File;

public class MyReceiver extends BroadcastReceiver {
    DownloadManager my_DownloadManager;
    long tamaño;
    IntentFilter my_IntentFilter;
    private static final String TAG = "MyReceiver";
    private Context my_context;
    private Activity my_activity;
    private ReceiverListener receiverListener;
    private boolean completado;
    public MyReceiver(Activity activity_, ReceiverListener receiverListener) {
        this.my_context = activity_;
        this.my_activity = activity_;
        this.receiverListener = receiverListener;
    }

    public boolean isCompletado() {
        return completado;
    }

    public void setCompletado(boolean completado) {
        this.completado = completado;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Evento_Action", intent.getAction());
        String action = intent.getAction();
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
            intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(tamaño);
            Cursor cursor = my_DownloadManager.query(query);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex)) {
                    String uriString = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    String filename = uriString.substring(uriString.lastIndexOf('/') + 1, uriString.length()).replace("%20", " ");
                    Log.i(TAG, "onReceive: " + filename);
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/apk", filename);
                    Log.i(TAG, "onReceive: " + file.getAbsolutePath());
                    //receiverListener.onInstall(file);
                    Pantalla_Actualizar object=(Pantalla_Actualizar)context;
                    object.cargadatos();
                    cursor.close();
                }
            }


        }

    }

    public void Descargar(String url) {
        // String url=
        // "https://firebasestorage.googleapis.com/v0/b/toks-4278b.appspot.com/o/Descarga%201.1.0.apk?alt=media&token=7e71ed09-0ad9-44f4-b303-eb0a526bd134";
        //https://firebasestorage.googleapis.com/v0/b/toks-4278b.appspot.com/o/Descarga%202.0.0.apk?alt=media&token=b6388f8f-6247-42dd-96f5-48f7801e20f9
        this.setCompletado(false);
        Log.i(TAG, "Descargar: " + url);
        DownloadManager.Request my_Request;

        my_DownloadManager = (DownloadManager) my_context.getSystemService(Context.DOWNLOAD_SERVICE);


        my_Request = new DownloadManager.Request(Uri.parse(url));
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(url);
        String name = URLUtil.guessFileName(url, null, fileExtension);

        //crear la carpeta
        File miFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "apk");

        boolean isCreada = miFile.exists();

        if (isCreada == false) {
            isCreada = miFile.mkdirs();
        }

        my_Request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS + "/apk", name.replace("%", ""));
        String h = my_Request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS + "/apk", name.replace("%", "")).toString();


        Log.e("ruta_apk", h);

        Log.e("Descargar", "Ok");

        tamaño = my_DownloadManager.enqueue(my_Request);


    }



    public void registrar(MyReceiver oMyReceiver) {

        my_IntentFilter = new IntentFilter();
        my_IntentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        my_context.registerReceiver(oMyReceiver, my_IntentFilter);

    }

    public void borrarRegistro(MyReceiver oMyReceiver) {
        my_context.unregisterReceiver(oMyReceiver);
    }


}
