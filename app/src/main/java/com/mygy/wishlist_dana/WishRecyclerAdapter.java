package com.mygy.wishlist_dana;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class WishRecyclerAdapter extends RecyclerView.Adapter<WishRecyclerAdapter.ViewHolder>{
    private final LayoutInflater inflater;
    private WishList wishes;
    private ArrayList<Wish> source;
    private AppCompatActivity activity;
    public WishRecyclerAdapter(Context context, WishList wishes, AppCompatActivity activity) {
        this.wishes = wishes;
        this.inflater = LayoutInflater.from(context);
        this.source = wishes.getList();
        this.activity = activity;
    }
    @Override
    public WishRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.wish_item, parent, false);
        return new WishRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WishRecyclerAdapter.ViewHolder holder, int position) {
        Wish wish = source.get(position);

        if(wish.getIcoUri() == null) holder.ico.setImageResource(R.drawable.img);
        else{
            holder.ico.setImageURI(wish.getIcoUri());
        }

        holder.name.setText(wish.getName());
        holder.description.setText(wish.getDescription());
        if (!wish.isPinned()) {
            holder.pinBtn.setImageResource(R.drawable.baseline_push_pin_24);
        } else {
            holder.pinBtn.setImageResource(R.drawable.baseline_push_pinned_24);
        }

        holder.pinBtn.setOnClickListener(v -> {
            if(!wish.isDone()) {
                if (wish.isPinned()) {
                    wish.setPinned(false);
                    holder.pinBtn.setImageResource(R.drawable.baseline_push_pin_24);
                   wishes.sort();
                    notifyDataSetChanged();
                } else {
                    wish.setPinned(true);
                    holder.pinBtn.setImageResource(R.drawable.baseline_push_pinned_24);
                    wishes.sort();
                    notifyDataSetChanged();
                }
                ((MainActivity)activity).saveToFile();
            }
        });

        if (!wish.isDone()) {
            holder.doneBtn.setImageResource(R.drawable.baseline_done_outline_24);
            holder.black.setVisibility(View.GONE);
        } else {
            holder.doneBtn.setImageResource(R.drawable.baseline_donen_outline_24);
            holder.black.setVisibility(View.VISIBLE);
        }
        holder.doneBtn.setOnClickListener(v -> {
            if (wish.isDone()) {
                wish.setDone(false);
                holder.doneBtn.setImageResource(R.drawable.baseline_done_outline_24);
                holder.black.setVisibility(View.GONE);
                wishes.sort();
                notifyDataSetChanged();
            } else {
                wish.setDone(true);
                holder.doneBtn.setImageResource(R.drawable.baseline_donen_outline_24);
                holder.black.setVisibility(View.VISIBLE);
                wishes.sort();
                notifyDataSetChanged();
            }
            ((MainActivity)activity).saveToFile();
        });

        holder.root.setOnClickListener(v -> {
            Intent intent = new Intent(activity, WishActivity.class);
            intent.putExtra(Wish.class.getSimpleName(), wish);
            activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return source.size();
    }
    public void setWishList(WishList wishList){
        this.wishes = wishList;
        this.source = wishes.getList();
        notifyDataSetChanged();
    }
    public void setWishList(ArrayList<Wish> list){
        this.source = list;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView ico,black;
        final TextView name, description;
        final ConstraintLayout root;
        final ImageButton pinBtn;
        final ImageButton doneBtn;
        ViewHolder(View view){
            super(view);
            ico = view.findViewById(R.id.wish_ico);
            name = view.findViewById(R.id.wish_name);
            description = view.findViewById(R.id.wish_description);
            root = view.findViewById(R.id.wish_root);
            pinBtn = view.findViewById(R.id.wish_pinBtn);
            doneBtn = view.findViewById(R.id.wish_doneBtn);
            black = view.findViewById(R.id.wish_blac);
        }
    }
}
