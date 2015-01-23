package com.practicas.janhout.galeriaimagenes;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class Adaptador extends CursorAdapter {

    private int recurso;
    private static LayoutInflater i;

    public Adaptador(Context co, int recurso, Cursor cu) {
        super(co, cu, true);
        this.recurso = recurso;
        this.i = (LayoutInflater)co.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = i.inflate(recurso, parent, false);
        ViewHolder vh = new ViewHolder();

        vh.foto = (ImageView)view.findViewById(R.id.iv);
        view.setTag(vh);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder vh = (ViewHolder) view.getTag();
        vh.foto.setTag(cursor.getPosition());

        int imageID = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
        Uri uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                Integer.toString(imageID));

        Picasso.with(context).load(uri).fit().centerCrop().into(vh.foto);
    }

    public static class ViewHolder{
        public ImageView foto;
    }
}