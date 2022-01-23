package com.example.settingsnotification;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class example_adapter extends RecyclerView.Adapter<example_adapter.ExampleViewHolder>
{
    private ArrayList<exemple_item> exemple_items;
    private onItemClickListener listener;



    public interface onItemClickListener
    {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener)
    {
        this.listener = listener;
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tvLine1;
        public TextView tvLine2;
        public ImageView ivDelete;

        public ExampleViewHolder(@NonNull View itemView, final onItemClickListener listener) {
            super(itemView);
            tvLine1 = (TextView) itemView.findViewById(R.id.tvLine1);
            tvLine2 = (TextView) itemView.findViewById(R.id.tvLine2);
            //ivDelete = (ImageView) itemView.findViewById(R.id.ivDelete);

            /*
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

             */

            /*
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

             */

        }

    }

    public example_adapter(ArrayList<exemple_item> exemple_items)
    {
        this.exemple_items = exemple_items;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exemple_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(view, listener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        exemple_item currentItem = exemple_items.get(position);

        holder.tvLine1.setText(currentItem.getLine1());
        holder.tvLine2.setText(currentItem.getLine2());
    }
    @Override
    public int getItemCount() {
        return exemple_items.size();
    }
}
