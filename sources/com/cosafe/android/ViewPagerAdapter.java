package com.cosafe.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class ViewPagerAdapter extends Adapter<MyViewHolder> {
    private ArrayList<Integer> arrayList;
    private Context context;

    public static class MyViewHolder extends ViewHolder {
        ImageView image;

        public MyViewHolder(View view) {
            super(view);
            this.image = (ImageView) view.findViewById(R.id.iv_image);
        }
    }

    public ViewPagerAdapter(Context context2, ArrayList<Integer> arrayList2) {
        this.context = context2;
        this.arrayList = arrayList2;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_view_pager, viewGroup, false));
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        Glide.with(this.context).load((Integer) this.arrayList.get(i)).into(myViewHolder.image);
    }

    public int getItemCount() {
        return this.arrayList.size();
    }
}
