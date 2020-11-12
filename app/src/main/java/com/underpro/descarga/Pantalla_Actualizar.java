package com.underpro.descarga;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import Clases.MyReceiver;
import Clases.adaptador;
import Clases.presetdata;
public class Pantalla_Actualizar extends AppCompatActivity {
    MyReceiver oMyReceiver;
    Button btn_descargar;
    String url, version;
    private adaptador adaptadordatos;
    private RecyclerView recyclerViewDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_actualizar);

        RecyclerView recyclerView = (RecyclerView) this.findViewById(R.id.reciclercontenedor);
        this.recyclerViewDatos = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adaptador adapterunionRecycler = new adaptador(obtenerDatos());
        this.adaptadordatos = adapterunionRecycler;
        this.recyclerViewDatos.setAdapter(adapterunionRecycler);


        version = Pantalla_Principal.version_firebase;
        url = Pantalla_Principal.url_firebase;
        Init();



        btn_descargar = (Button) findViewById(R.id.btn_Actualizar);
        btn_descargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                oMyReceiver.Descargar(url);


            }
        });
    }


    private void Init() {

        ReceiverListener receiverListener = new ReceiverListener() {
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
    }

    public List<presetdata> obtenerDatos() {
        List<presetdata> data = new ArrayList<>();
        data.add(new presetdata("Item 1", "Esta es una ","sadas", R.drawable.ic_launcher_foreground));
        data.add(new presetdata("Item 2", "Esta es una ","", R.drawable.ic_launcher_foreground));
        data.add(new presetdata("Item 3", "Esta es una ","", R.drawable.ic_launcher_foreground));
        return data;
    }
}
