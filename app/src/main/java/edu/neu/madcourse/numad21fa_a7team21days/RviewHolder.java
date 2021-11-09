package edu.neu.madcourse.numad21fa_a7team21days;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class RviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView stickerID;
    public TextView sender;
    public TextView receiveTime;
    RviewAdaptor.OnItemListener onItemListener;

    public RviewHolder(View itemView, RviewAdaptor.OnItemListener Listener) {
        super(itemView);
        this.stickerID = itemView.findViewById(R.id.sticker_id);
        this.sender = itemView.findViewById(R.id.sender);
        this.receiveTime = itemView.findViewById(R.id.sent_time);
        this.onItemListener = Listener;
        itemView.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        onItemListener.onItemClick(getAdapterPosition());
    }
}