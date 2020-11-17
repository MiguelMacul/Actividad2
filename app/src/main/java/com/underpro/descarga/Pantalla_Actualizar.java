package com.underpro.descarga;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import Clases.MyReceiver;
import Clases.adaptador;
import Clases.presetdata;
public class Pantalla_Actualizar extends AppCompatActivity {
    private MyReceiver oMyReceiver;
    private Button btn_descargar;
    private String url, version;
    private adaptador adaptadordatos;
    private RecyclerView recyclerViewDatos;
    private ReceiverListener receiverListener;
    private  List<presetdata> data;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_actualizar);
        cargadatos();
        version = Pantalla_Principal.version_firebase;
        url = Pantalla_Principal.url_firebase;
        Init();
        btn_descargar = (Button) findViewById(R.id.btn_Actualizar);
        btn_descargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId()==R.id.btn_Actualizar){
                oMyReceiver.Descargar(url);
                }
            }
        });
    }
    public Pantalla_Actualizar(){
        this.data=null;
    }
    public void cargadatos(){
        RecyclerView recyclerView = (RecyclerView) this.findViewById(R.id.reciclercontenedor);
        this.recyclerViewDatos = recyclerView;
        recyclerViewDatos.setLayoutManager(new LinearLayoutManager(this));
        adaptador adapterunionRecycler = new adaptador(obtenerDatos());
        this.adaptadordatos = adapterunionRecycler;
        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerViewDatos);
        this.recyclerViewDatos.setAdapter(this.adaptadordatos);
        adaptadordatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "apk");
                File file[] = f.listFiles();
                receiverListener.onInstall( file[recyclerViewDatos.getChildAdapterPosition(view)]);
                //System.out.println(recyclerViewDatos.getChildAdapterPosition(view)+"\t"+data.get(recyclerViewDatos.getChildAdapterPosition(view)).getNombre());
            }
        });
    }
    private void Init() {
         receiverListener = new ReceiverListener() {
            @Override
            public void onInstall(File file) {
                System.out.print(file);
                Intent pantallaInstall = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Uri apkUri = FileProvider.getUriForFile(Pantalla_Actualizar.this, "com.underpro.descarga" + ".fileprovider", file);
                    pantallaInstall = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                    pantallaInstall.setData(apkUri);
                    pantallaInstall.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }else {
                    Uri apkUri = Uri.fromFile(file);
                    pantallaInstall = new Intent(Intent.ACTION_VIEW);
                    pantallaInstall.setDataAndType(apkUri, "application/vnd.android.package-archive");
                    pantallaInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                }
                startActivity(pantallaInstall);

                Log.e("MsjDescarga", "Se descargo sin problemas");
            }
        };
        oMyReceiver = new MyReceiver(Pantalla_Actualizar.this, receiverListener);
        System.out.println("asd");
    }
    @Override
    protected void onPause() {
        super.onPause();
        oMyReceiver.borrarRegistro(oMyReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        oMyReceiver.registrar(oMyReceiver);
        cargadatos();
    }

    public List<presetdata> obtenerDatos() {
       data=new ArrayList<>() ;
        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "apk");
        if(f.exists()){
            File file[] = f.listFiles();
            for(int i=0;i<file.length;i++){
                String direccion=file[i].toString();
                long ms = file[i].lastModified();
                Date d = new Date(ms);
                Calendar c = new GregorianCalendar();
                c.setTime(d);
                if((direccion.substring(direccion.lastIndexOf("."),direccion.length())).equals(".apk"))
                data.add(new presetdata("               "+direccion.substring(direccion.lastIndexOf(" "), direccion.lastIndexOf(".")),
                          "      "+c.get(Calendar.DATE)+"/"+c.get(Calendar.MONTH)+"/"+c.get(Calendar.YEAR), R.mipmap.hola_round));

            }
        }
        return data;
    }

    ItemTouchHelper.SimpleCallback itemTouch=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT){
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            data.remove(viewHolder.getAdapterPosition());
            File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "apk");
            File file[] = f.listFiles();
            file[viewHolder.getAdapterPosition()].delete();
            cargadatos();
        }
    };
}
