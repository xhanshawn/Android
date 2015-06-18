package com.xhanshawn.view;

import java.util.ArrayList;

import com.fedorvlasov.lazylist.ImageLoader;
import com.xhanshawn.data.LatalkMessage;

import android.R;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

public class CustomGalleryAdapter extends BaseAdapter{

	private LayoutInflater mInflater;
	private Context context;
	private int count;
	
	public CustomGalleryAdapter(Context context, int count) {
		this.context = context;
		mInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.count = count;
    }

    public int getCount() {
        return count;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
//            convertView = mInflater.inflate(R.layout.galleryitem, null);
//            holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);
//            holder.checkbox = (CheckBox) convertView.findViewById(R.id.itemCheckBox);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
//        holder.checkbox.setId(position);
//        holder.imageview.setId(position);
//        holder.checkbox.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                CheckBox cb = (CheckBox) v;
//                int id = cb.getId();
//                if (thumbnailsselection[id]){
//                    cb.setChecked(false);
//                    thumbnailsselection[id] = false;
//                } else {
//                    cb.setChecked(true);
//                    thumbnailsselection[id] = true;
//                }
//            }
//        });
//        holder.imageview.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                int id = v.getId();
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.parse("file://" + arrPath[id]), "image/*");
//                startActivity(intent);
//            }
//        });
//        holder.imageview.setImageBitmap(thumbnails[position]);
//        holder.checkbox.setChecked(thumbnailsselection[position]);
//        holder.id = position;
        return convertView;
    }
    
    
    
    class ViewHolder {
        ImageView imageview;
        CheckBox checkbox;
        int id;
    }
}


