package com.example.user.popular_movies_stage1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mohamed Amin on 4/9/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.NumberViewHolder>{
    private final ListItemClickListener mOnItemClickListener;
    private final Context context;

    public MoviesAdapter(ListItemClickListener listener,Context context){
        this.mOnItemClickListener=listener;
        this.context=context;

    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.image_view,parent,false);
        return new NumberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return MainActivity.moviesList.size();
    }

    public class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;

        public NumberViewHolder(View itemView){
            super(itemView);
            imageView= itemView.findViewById(R.id.image_view);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition =getAdapterPosition();
            mOnItemClickListener.onItemClickListener(clickedPosition);
        }

        public void bind(int position) {
            Picasso.with(context)
                    .load(JsonUtilis.PhotoUrl(MainActivity.moviesList.get(position)))
                    .into(imageView);
        }
    }
    public interface ListItemClickListener{
       void onItemClickListener(int clickedItemIndex);
   }
}
