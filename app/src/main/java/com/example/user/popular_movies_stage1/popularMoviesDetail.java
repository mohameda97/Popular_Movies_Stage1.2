package com.example.user.popular_movies_stage1;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/**
 * Created by Mohamed Amin on 4/9/2018.
 */

public class popularMoviesDetail extends AppCompatActivity {
    private static final int DEFAULT_POSITION= -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent =getIntent();
        if (intent==null){
            finish();
        }
        int position =intent.getIntExtra("position",DEFAULT_POSITION);
        if (position==DEFAULT_POSITION){
            finish();
            return;
        }
        Movies movies=JsonUtilis.parseMovieJson(MainActivity.moviesList.get(position));
        try {
            ActionBar actionBar=this.getSupportActionBar();
            if (actionBar!=null){
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle(movies.getTitle());
            }
        }catch (Exception e){
            Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
        }

     populateUi(movies);
    }
    private void populateUi(Movies movies){
        ImageView imageView= findViewById(R.id.image_detail);
        Picasso.with(this).load(movies.getPhotoPath()).into(imageView);

        TextView overview =  findViewById(R.id.overview_id);
        TextView rate = findViewById(R.id.rate_id);
        TextView date =findViewById(R.id.date_id);

        overview.setText(movies.getOverview());
        rate.setText(movies.getUserRate()+"/10");
        date.setText(movies.getReleaseDate());


    }
}
