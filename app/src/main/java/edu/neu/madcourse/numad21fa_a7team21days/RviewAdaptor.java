package edu.neu.madcourse.numad21fa_a7team21days;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.neu.madcourse.numad21fa_a7team21days.cookbook.Sticker;


public class RviewAdaptor extends RecyclerView.Adapter<RviewHolder> {

    private ArrayList<Sticker> stickerList;
    private OnItemListener onItemListener;

    //Constructor
    public RviewAdaptor(ArrayList<Sticker> stickerList, OnItemListener listener) {
        this.stickerList = stickerList;
        this.onItemListener = listener;
    }

    public void setOnItemClickListener(OnItemListener listener) {
        this.onItemListener = listener;
    }

    @Override
    public RviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_card, parent, false);
        return new RviewHolder(view, onItemListener);
    }
    public String[] mColors = {"#F1EE34","#DDF134","#BBF134","#95F134"};

    @Override
    public void onBindViewHolder(RviewHolder holder, int position) {
        Sticker currentItem = stickerList.get(position);

        holder.stickerID.setText(currentItem.getStickerId());
        holder.sender.setText(currentItem.getSender());
        holder.receiveTime.setText(currentItem.getSendTime());
        holder.itemView.setBackgroundColor(Color.parseColor(mColors[position % 4]));

    }

    @Override
    public int getItemCount() {
        return stickerList.size();
    }

    public interface OnItemListener{
        void onItemClick(int position);
    }
}
