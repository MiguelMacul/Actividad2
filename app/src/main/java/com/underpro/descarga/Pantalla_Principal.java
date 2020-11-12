package com.underpro.descarga;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;

public class Pantalla_Principal  extends AppCompatActivity {
    public static String version_actual="1.0.0";
    public static String version_firebase;
    public static String url_firebase;
    private boolean permisol;
    TextView txtVersion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.pantalla_principal);
        txtVersion=(TextView) findViewById(R.id.txt_version);
        txtVersion.setText("Version "+version_actual);
        this.permisol=false;
       // isStoragePermissionGranted();


    }

    protected void onStart() {
        super.onStart();

    }
    private  void Obtener_Firebase(){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference referencia_version,referencia_url;
        referencia_url=database.getReference("url");
        referencia_url.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                url_firebase=dataSnapshot.getValue().toString();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Pantalla_Principal.this,"URL "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        referencia_version=database.getReference("version");
        referencia_version.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                version_firebase=dataSnapshot.getValue().toString();
                if(version_firebase.trim().equals(version_actual.trim())){
                    Toast.makeText(Pantalla_Principal.this,"No es Necesario Actualizar ", Toast.LENGTH_SHORT).show();
                }
                else{

                    Intent pantaActualizar=new Intent(getApplicationContext(),Pantalla_Actualizar.class);
                    pantaActualizar.putExtra("version",version_firebase);
                    pantaActualizar.putExtra("url",url_firebase);

                    finish();
                    startActivity(pantaActualizar);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Pantalla_Principal.this,"Version "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Evento_Action", "Permission is granted");


                this.Obtener_Firebase();
                return true;
            } else {

                Log.v("Evento_Action", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Evento_Action", "Permission is granted");
            this.Obtener_Firebase();
            return true;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            this.Obtener_Firebase();

        }
    }
}

