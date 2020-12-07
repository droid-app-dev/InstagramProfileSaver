package com.funcoders.Instadpsaver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.funcoders.Instadpsaver.bean.ProfileBean;
import com.funcoders.Instadpsaver.common.Constants;

import java.util.List;

public class InstaListAdapter extends RecyclerView.Adapter<InstaListAdapter.MyViewHolder> {

    Context mcontext;
    List<ProfileBean> profileList;


    public InstaListAdapter(Context mcontext, List<ProfileBean> profileList) {

        this.mcontext = mcontext;
        this.profileList = profileList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ProfileBean bean = profileList.get(position);
        holder.username_tv.setText(bean.getInsta_id());
        holder.name_tv.setText(bean.getFull_name());
        holder.following_tv.setText("" + bean.getFollowing());
        holder.followers_tv.setText("" + bean.getFollowers());

        Glide.with(mcontext)
                .load(bean.getProfile_pic_url_hd())
                .placeholder(R.drawable.progress_animation)
                // .error(R.drawable.ic_arrow_back_24_px)
                .into(holder.profile_image);

        if (bean.getIs_private().equalsIgnoreCase("true")) {
            holder.img_privacy.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.ic_lock));
        } else {
            holder.img_privacy.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.ic_unlock));
        }


        holder.detailsarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.displayLongToast(mcontext, "Work in Progress");

            }
        });

    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }

    public void removeItem(int position) {
        profileList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(ProfileBean item, int position) {
        profileList.add(position, item);
        notifyItemInserted(position);
    }

    public List<ProfileBean> getData() {
        return profileList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView username_tv, name_tv, followers_tv, following_tv;
        ImageView profile_image, detailsarrow, img_privacy;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            username_tv = itemView.findViewById(R.id.username_tv);
            name_tv = itemView.findViewById(R.id.name_tv);
            followers_tv = itemView.findViewById(R.id.followers_tv);
            following_tv = itemView.findViewById(R.id.following_tv);
            profile_image = itemView.findViewById(R.id.profile_image);
            detailsarrow = itemView.findViewById(R.id.detailsarrow);
            img_privacy = itemView.findViewById(R.id.img_privacy);

        }
    }
}
