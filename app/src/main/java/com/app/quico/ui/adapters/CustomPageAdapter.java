package com.app.quico.ui.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.quico.R;
import com.app.quico.activities.MainActivity;
import com.app.quico.entities.Medium;
import com.jsibbold.zoomage.ZoomageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ahmedsyed on 10/9/2018.
 */


public class CustomPageAdapter extends PagerAdapter {
    MainActivity context;
    ArrayList<Medium> images;
    LayoutInflater layoutInflater;
    ImageLoader imageLoader;


    public CustomPageAdapter(MainActivity context, ArrayList<Medium> images) {
        this.context = context;
        this.images = images;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.pager_item, container, false);

        ZoomageView imageView = (ZoomageView) itemView.findViewById(R.id.imageView);
       // ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        if (images.get(position).getImageUrl() != null && !images.get(position).getImageUrl().equals("")) {
            Picasso.with(context).load(images.get(position).getImageUrl()).placeholder(R.drawable.placeholder_thumb).into(imageView);
           // imageLoader.displayImage(images.get(position).getImageUrl(), imageView);
        }
        //  imageView.setImageResource(images[position]);
        container.addView(itemView);

       /* imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
            }
        });*/

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}

