package com.test.rssfeeder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by nishant- on 16-11-2015.
 */
public class RssAdapter extends ArrayAdapter<RssDataModel> {

    private final Context mContext;
    private final ArrayList<RssDataModel> values;

    public RssAdapter(Context context,  ArrayList<RssDataModel> values) {
        super(context,-1, values);
        this.mContext = context;

        this.values = values;
    }

    @Override
    public int getCount() {
       return values.size();
    }

    @Override
    public RssDataModel getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if(rowView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_item, null);
            ViewHolder holder = new ViewHolder();
            holder.title = (TextView) rowView.findViewById(R.id.textViewTitle);
            holder.desc = (TextView) rowView.findViewById(R.id.textViewDesc);
            holder.image = (ImageView) rowView.findViewById(R.id.imageView);
            rowView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.title.setText(values.get(position).itemTitle);
        holder.desc.setText(values.get(position).itemDescription);
        if(values.get(position).itemImage != "") {
            Picasso.with(mContext).load(values.get(position).itemImage).placeholder(R.drawable.placeholder).into(holder.image);
        }else
        {
            Picasso.with(mContext).load("http://").placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(holder.image);
        }
        return rowView;
    }

    static class ViewHolder
    {
        TextView title;
        TextView desc;
        ImageView image;

    }
}
