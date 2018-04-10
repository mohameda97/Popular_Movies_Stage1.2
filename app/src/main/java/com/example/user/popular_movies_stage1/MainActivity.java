package com.example.user.popular_movies_stage1;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Looper;
import android.provider.SyncStateContract;
import android.support.v4.app.LoaderManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.content.Loader;
import android.support.v4.content.AsyncTaskLoader;

import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public  class MainActivity extends AppCompatActivity implements  LoaderManager.LoaderCallbacks<String>,MoviesAdapter.ListItemClickListener {
private String userSelect;
private RecyclerView recyclerView;
public static List<String> moviesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=(RecyclerView) findViewById(R.id.recycler);

        String myUrl=buildUrl();
        Toast.makeText(this,myUrl,Toast.LENGTH_SHORT).show(); // show the url on the main
// check if the device connect with internet or not
     if (Online()){
         Bundle bundle=new Bundle();
         bundle.putString("URL",myUrl);
         LoaderManager loaderManager=getSupportLoaderManager();
         Loader<String> movierSearchLoader=loaderManager.getLoader(2);
         if (movierSearchLoader==null){
             loaderManager.initLoader(2,bundle,this);

         }else {
             loaderManager.restartLoader(2,bundle,this);
         }
     }else {
         Toast.makeText(this,"you are not online",Toast.LENGTH_SHORT).show();
     }
    }
    public boolean Online(){
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return  networkInfo !=null && networkInfo.isConnectedOrConnecting();
    }


    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            String resultJson;
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(args == null){
                    return;
                }

                if(resultJson == null || resultJson.equals("") ||
                        !(userSelect.equals(PreferenceManager.getDefaultSharedPreferences(getApplicationContext())))){
                    forceLoad();
                    return;
                }
            }

            @Override
            public String loadInBackground() {
                String searchQueryUrlString = args.getString("URL");

                if(searchQueryUrlString== null || searchQueryUrlString.equals("")){
                    return null;
                }
                try {
                    // here is our code to start downloading
                    resultJson="";
                    URL url = new URL(searchQueryUrlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    int data = reader.read();
                    while (data != -1){
                        char current =(char) data;
                        resultJson += current;
                        data = reader.read();
                    }

                    return resultJson;
                }
                catch (IOException e){
                    Toast.makeText(MainActivity.this, "Error while loading", Toast.LENGTH_SHORT).show();
                    return null;

                }
            }



            @Override
            public void deliverResult(String data) {
                resultJson= data;
                super.deliverResult(data);
            }
        };
    }


    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if (data!=null&& !(data.equals(""))){

            moviesList=new ArrayList<>();
            try {
                JSONObject jsonObject=new JSONObject(data);
                JSONArray result=new JSONArray(jsonObject.getString("results"));
                for (int i=0;i<result.length();i++){
                    JSONObject jsonObject1=result.getJSONObject(i);
                    if (!jsonObject1.isNull("poster_path")){
                        moviesList.add(jsonObject1.toString());
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
                Toast.makeText(this,"Error Fetching Data",Toast.LENGTH_SHORT).show();
            }
            recyclerView.setLayoutManager(new GridLayoutManager(this,2));
            MoviesAdapter moviesAdapter=new MoviesAdapter(this,this);

            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(moviesAdapter);

        }
        else {
            Toast.makeText(this,"No data",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.activity_setting){
            startActivity(new Intent(this,SettingActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClickListener(int clickedItemIndex) {
        Intent intent=new Intent(this,popularMoviesDetail.class);
        intent.putExtra("position",clickedItemIndex);
        startActivity(intent);
    }

    private String buildUrl(){ //create my url with key
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userSelect=mSharedPreferences.getString(getString(R.string.pref_sorting),getString(R.string.popular));
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(userSelect)
                .appendQueryParameter("api_key",getString(R.string.MyKey));
        return builder.build().toString();
    }
}
