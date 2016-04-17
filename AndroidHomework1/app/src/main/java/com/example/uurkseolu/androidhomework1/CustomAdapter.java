package com.example.uurkseolu.androidhomework1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class CustomAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<nameNumber> mKisiListesi;
    private Context context;

    public CustomAdapter(Activity activity, List<nameNumber> kisiler) {

        mInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        mKisiListesi = kisiler;
        context = activity;
    }

    @Override
    public int getCount() {
        return mKisiListesi.size();
    }

    @Override
    public nameNumber getItem(int position) {

        return mKisiListesi.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View satirView;

        satirView = mInflater.inflate(R.layout.custom_list, null);
        TextView textname =
                (TextView) satirView.findViewById(R.id.txtName);
        TextView textnumber =
                (TextView) satirView.findViewById(R.id.txtNumber);

        nameNumber kisi = mKisiListesi.get(position);

        textname.setText(kisi.getName());

        textnumber.setText(kisi.getNumber());

        satirView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri number = Uri.parse("tel:" + mKisiListesi.get(position).getNumber());
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                context.startActivity(callIntent);
            }
        });

        return satirView;
    }
}