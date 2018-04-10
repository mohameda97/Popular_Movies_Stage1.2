package com.example.user.popular_movies_stage1;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mohamed Amin on 4/9/2018.
 */

public class JsonUtilis {
    private static final String POSTER_PATH="poster_path";
    private static final String ORIGINAL_TITLE="original_title";
    private static final String OVERVIEW="overview";
    private static final String RELEASE_DATE="release_date";
    private static final String VOTE_AVERAGE="vote_average";
    public static Movies parseMovieJson(String json){
    Movies movies=new Movies();
    try{
        JSONObject jsonObject =new JSONObject(json);
        movies.setTitle(jsonObject.getString(ORIGINAL_TITLE));
        movies.setPhotoPath("http://image.tmdb.org/t/p/w342"+jsonObject.getString(POSTER_PATH));
        movies.setOverview(jsonObject.getString(OVERVIEW));
        movies.setReleaseDate(jsonObject.getString(RELEASE_DATE));
        movies.setUserRate(jsonObject.getString(VOTE_AVERAGE));
    }catch(JSONException e){
        e.printStackTrace();
    }
    return movies;
    }
    public static String PhotoUrl(String json){
        try {
            JSONObject jsonObject =new JSONObject(json);
            return "http://image.tmdb.org/t/p/w185" +jsonObject.getString(POSTER_PATH);

        }catch (JSONException e){
            e.printStackTrace();
        }
        return "Error";
    }
}
