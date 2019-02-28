package com.app.quico.ui.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.quico.R;
import com.app.quico.activities.MainActivity;
import com.app.quico.entities.Chat.DocumentDetail;
import com.app.quico.entities.Medium;
import com.jsibbold.zoomage.ZoomageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class CustomImagesAdapter extends PagerAdapter {
    MainActivity context;
    ArrayList<DocumentDetail> images;
    LayoutInflater layoutInflater;
    ImageLoader imageLoader;


    public CustomImagesAdapter(MainActivity context, ArrayList<DocumentDetail> images) {
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
        if (images.get(position) != null && !images.get(position).equals("")) {
          //  Picasso.with(context).load(images.get(position)).placeholder(R.drawable.placeholder_thumb).into(imageView);
             imageLoader.displayImage(images.get(position).getFileUrl(), imageView);
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

