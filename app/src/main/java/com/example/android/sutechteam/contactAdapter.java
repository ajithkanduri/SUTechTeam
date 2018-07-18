package com.example.android.sutechteam;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class contactAdapter extends CursorAdapter {
    public contactAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.contact, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView username = (TextView) view.findViewById(R.id.name);
        TextView number = (TextView) view.findViewById(R.id.number);
        ImageView photo = (ImageView) view.findViewById(R.id.contactphoto);
        long contactid = getItemId(cursor.getPosition());
        String name = cursor.getString(cursor.getColumnIndex(
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                        ContactsContract.Contacts.DISPLAY_NAME
        ));
        username.setText(name);
        String phnumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        number.setText(phnumber);
        //long  photoid = cursor.getLong(cursor.getColumnIndex(ContactsContract.Data.PHOTO_ID));

        String image = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

        if (image != null) {
            photo.setImageURI(Uri.parse(image));
        } else {
            photo.setImageResource(R.drawable.contact);
        }


    }
}
