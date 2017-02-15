package com.test.rssfeeder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by nishant- on 19-11-2015.
 */
public class RssCardAdapter extends RecyclerView.Adapter<RssCardAdapter.NewsViewHolder>{



    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_item,viewGroup,false);
        NewsViewHolder holder = new NewsViewHolder(v,viewGroup.getContext());
        return holder;
    }

    @Override
    public void onBindViewHolder(final NewsViewHolder newsViewHolder, int i) {
        newsViewHolder.newsTitle.setText(newsList.get(i).itemTitle);
        newsViewHolder.pubDate.setText(newsList.get(i).pubDate);
        if (newsList.get(i).itemImage != "") {
            newsViewHolder.imageViewProgress.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(newsList.get(i).itemImage).placeholder(R.drawable.placeholder).into(newsViewHolder.newsImage, new Callback() {
                @Override
                public void onSuccess() {
                    newsViewHolder.imageViewProgress.setVisibility(View.GONE);
                }

                @Override
                public void onError() {

                }
            });

        } else {
            Picasso.with(mContext).load("http://").placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(newsViewHolder.newsImage);
        }


        newsViewHolder.setClickListener(new NewsViewHolder.ClickListener() {
            @Override
            public void onClick(View v, int position, boolean isLongClick) {
                if (!isLongClick) {
                    RssDataModel mDataModel = newsList.get(position);
                    Intent mIntent = new Intent(mContext, RssWebView.class);
                    mIntent.putExtra("LINK", mDataModel.itemDescription);
                    mContext.startActivity(mIntent);
                }
            }
        });


        //Set Card Animation
        setAnimation(newsViewHolder.cv, i);
    }

    private void setAnimation(View viewToAnimate,int position)
    {
        if(position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext,android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }

    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }


    static List<RssDataModel> newsList;
    public RssCardAdapter(List<RssDataModel> newsList) {
        this.newsList = newsList;
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        CardView cv;
        ImageView newsImage;
        TextView newsTitle;
        TextView pubDate;
        ProgressBar imageViewProgress;
        LinearLayout placeNameHolder;
        private ClickListener clickListener;

        public NewsViewHolder(View itemView,Context context) {
            super(itemView);
            mContext = context;
            cv = (CardView) itemView.findViewById(R.id.cardView);
            newsImage = (ImageView) itemView.findViewById(R.id.cardImageView);
            newsTitle = (TextView) itemView.findViewById(R.id.cardTitle);
            imageViewProgress = (ProgressBar) itemView.findViewById(R.id.cardImageViewProgress);
            pubDate = (TextView) itemView.findViewById(R.id.pubDateTextView);
            placeNameHolder = (LinearLayout) itemView.findViewById(R.id.placeNameHolder);
            itemView.setOnClickListener(this);
        }


        //Called when the view is clicked
        public interface ClickListener
        {
            public void onClick(View v,int position,boolean isLongClick);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v,getPosition(),false);
        }

        public void setClickListener(ClickListener clickListener)
        {
            this.clickListener = clickListener;
        }




    }
}
