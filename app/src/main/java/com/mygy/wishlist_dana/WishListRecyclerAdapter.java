package com.mygy.wishlist_dana;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.imagepicker.ImagePicker;

import java.util.List;

public class WishListRecyclerAdapter extends RecyclerView.Adapter<WishListRecyclerAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<WishList> lists;

    public WishListRecyclerAdapter(Context context, List<WishList> lists) {
        this.lists = lists;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public WishListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.wishlist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WishListRecyclerAdapter.ViewHolder holder, int position) {
        WishList list = lists.get(position);

        if(list.getIcoUri() == null) holder.ico.setImageResource(R.drawable.img);
        else {
            holder.ico.setImageURI(list.getIcoUri());
        }
        holder.name.setText(list.getName());
        holder.date.setText(list.getFormattedDate());
        holder.root.setOnClickListener(v -> {
            MainActivity.setCurrentList(list);
        });

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView ico;
        final TextView name, date;
        final LinearLayout root;
        ViewHolder(View view){
            super(view);
            ico = view.findViewById(R.id.wishlist_ico);
            name = view.findViewById(R.id.wishlist_name);
            date = view.findViewById(R.id.wishlist_date);
            root = view.findViewById(R.id.wishlist_root);
        }
    }
}
