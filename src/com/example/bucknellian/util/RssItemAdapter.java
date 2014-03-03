package com.example.bucknellian.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bucknellian.R;

public class RssItemAdapter<T extends ListItem> extends ArrayAdapter<T> {
	public final Context context;
	private final int rowResourceId;
	
	

	public RssItemAdapter(Context context, int resource, int textViewResourceId, List<T> objects){
		super(context, resource, textViewResourceId, objects);
		this.context = context;
		this.rowResourceId = textViewResourceId;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rss_row_view, parent, false);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.rssImageView);
        TextView paperPadItemView = (TextView) rowView.findViewById(R.id.rssPaperPadItemView);
        
        T item = this.getItem(position);
        
        String icon = item.getIcon();
        paperPadItemView.setText(item.toString());
        
        // get input stream
        InputStream ims = null;
        try {
            ims = context.getAssets().open(icon);
        } catch (IOException e) {
        	Log.e("no file", "No FILE");
            e.printStackTrace();
        }
        // load image as Drawable
        Drawable d = Drawable.createFromStream(ims, null);
        // set image to ImageView
        imageView.setImageDrawable(d);
        return rowView;
        
        
	}
}