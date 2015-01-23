package com.practicas.janhout.galeriaimagenes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class Principal extends Activity {

    private GridView gv;
    private Adaptador ad;

    private int foto;
    private Cursor lista;

    private AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        initCursor();
        initComponents();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        foto = savedInstanceState.getInt(getString(R.string.foto),-1);
        if(foto != -1){
            mostrarFoto();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(getString(R.string.foto), foto);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(alerta != null){
            alerta.dismiss();
        }
    }

    private void initComponents(){
        foto = -1;
        gv = (GridView)findViewById(R.id.gv);
        lista.moveToFirst();
        ad = new Adaptador(this, R.layout.detallegrid, lista);
        gv.setAdapter(ad);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                foto = position;
                mostrarFoto();
            }
        });
    }

    private void initCursor(){
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Images.Media._ID };
        String selection = null;
        String [] selectionArgs = null;
        String order = null;
        lista = getContentResolver().query(uri, projection, selection, selectionArgs, order);
    }

    private void mostrarFoto(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View vista = inflater.inflate(R.layout.layout_foto, null);
        alert.setView(vista);
        ImageView iv = (ImageView)vista.findViewById(R.id.iv);
        lista.moveToPosition(foto);

        int imageID = lista.getInt(lista.getColumnIndex(MediaStore.Images.Media._ID));
        Uri uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                Integer.toString(imageID) );

        alert.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    Principal.this.setFoto(-1);
                    dialog.dismiss();
                    return true;
                }
                return false;
            }
        });
        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                Principal.this.setFoto(-1);
            }
        });
        Picasso.with(this).load(uri).into(iv);
        alerta = alert.create();
        alerta.show();
    }

    public void setFoto(int foto){
        this.foto = foto;
    }
}
