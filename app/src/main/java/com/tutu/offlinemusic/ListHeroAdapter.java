package com.tutu.offlinemusic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tutu.offlinemusic.R;

import java.util.ArrayList;

public class ListHeroAdapter extends RecyclerView.Adapter<ListHeroAdapter.ListViewHolder> {
    private OnItemClickCallback onItemClickCallback;
    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }
    private ArrayList<com.tutu.offlinemusic.Hero> listHero;

    public ListHeroAdapter(ArrayList<com.tutu.offlinemusic.Hero> list) {
        this.listHero = list;
    }


    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_hero, viewGroup, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, int position) {




        com.tutu.offlinemusic.Hero hero = listHero.get(position);
        Glide.with(holder.itemView.getContext())
                .load(R.drawable.icon)
                .apply(new RequestOptions().override(55, 55))
                .into(holder.imgPhoto);
        holder.tvjudul.setText(hero.getJudul());
        holder.deskripsi.setText(hero.getDeskripsi());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listHero.get(holder.getAdapterPosition()));
            }
        });


    }

    public interface OnItemClickCallback {
        void onItemClicked(Hero data);
    }



    @Override
    public int getItemCount() {
        return listHero.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView tvjudul, deskripsi;
        ListViewHolder(View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_item_photo);
            tvjudul = itemView.findViewById(R.id.tv_item_name);
            deskripsi = itemView.findViewById(R.id.tv_item_from);
        }
    }
}