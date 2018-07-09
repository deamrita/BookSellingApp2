package com.example.amrita.booksellingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.amrita.booksellingapp.myClasses.BookDetails;
import com.example.amrita.booksellingapp.myClasses.UserDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    public List<BookDetails> bookDetailsList;
    private OnItemClickListener mListener;
    public interface  OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;

    }

    public BookAdapter(List<BookDetails> bookDetailsList) {
        this.bookDetailsList =bookDetailsList;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.row,parent,false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Context context=holder.itemView.getContext();
        BookDetails bookDetails= bookDetailsList.get(position);
        holder.BookTitle.setText(bookDetails.getBookname());
       Picasso.get().load(bookDetails.getImg()).fit().centerCrop().into(holder.BookPreview);

    }

    @Override
    public int getItemCount() {
        return bookDetailsList.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView BookPreview;
        TextView BookTitle;
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            BookPreview=itemView.findViewById(R.id.bookpreview);
            BookTitle=itemView.findViewById(R.id.bookTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener!= null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
