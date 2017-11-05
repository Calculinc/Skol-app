package calculinc.google.httpssites.skol_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * Created by ruboss on 2017-10-24.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<String> mData = Collections.emptyList();
    private List<String> mUrl = Collections.emptyList();
    private List<String> mCommercialData = Collections.emptyList();
    private Context context;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    LinearLayout commercialLayout;

    // data is passed into the constructor
    public MyRecyclerViewAdapter(Context context, List<String> data, List<String> data2,  List<String> data3) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mUrl = data2;
        this.mCommercialData = data3;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.nyheter_recycler_view_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.myTextView.setText(mData.get(position));
        Picasso.with(context).load(mUrl.get(position)).into(holder.myImageView);

        try {
            Picasso.with(context).load(mCommercialData.get(0)).into(holder.commercialImageView);

            holder.commercialHeader.setText(mCommercialData.get(1));
            holder.commercialTextView.setText(mCommercialData.get(2));
            holder.commercialHeader.setTextColor(Color.parseColor(mCommercialData.get(3)));
            holder.view1.setBackgroundColor(Color.parseColor(mCommercialData.get(3)));
            holder.view2.setBackgroundColor(Color.parseColor(mCommercialData.get(3)));

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (position == 0 && mCommercialData.size() > 2 ) {

            commercialLayout.setVisibility(View.VISIBLE);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView myTextView;
        public ImageView myImageView;
        public TextView commercialTextView;
        public ImageView commercialImageView;
        public TextView commercialHeader;
        public View view1;
        public View view2;

        public ViewHolder(View itemView) {
            super(itemView);
            myTextView = (TextView) itemView.findViewById(R.id.recycler_row_text);
            myImageView = (ImageView) itemView.findViewById(R.id.news_feed_image);
            commercialImageView = (ImageView) itemView.findViewById(R.id.news_feed_image_commercial);
            commercialTextView = (TextView) itemView.findViewById(R.id.recycler_row_text_commercial);
            commercialLayout = (LinearLayout) itemView.findViewById(R.id.nyheter_commercial_layout);
            commercialHeader = (TextView) itemView.findViewById(R.id.commercial_header_name);
            view1 = (View) itemView.findViewById(R.id.commercial_header_view1);
            view2 = (View) itemView.findViewById(R.id.commercial_header_view2);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

