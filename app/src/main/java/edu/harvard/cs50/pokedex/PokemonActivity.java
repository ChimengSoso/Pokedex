package edu.harvard.cs50.pokedex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class PokemonActivity extends AppCompatActivity {
    private TextView nameTextView;
    private TextView numberTextView;
    private TextView type1TextView;
    private TextView type2TextView;
    private String url;
    private String url_descrip;
    private String name;
    private RequestQueue requestQueue;
    private Button btn_catch;
    private ImageView img_pokemon;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        url = getIntent().getStringExtra("url");
        int id = 0, t = 1;
        for (int i = url.length() - 2; i >= 0 && url.charAt(i) != '/'; --i) {
            id += t * (url.charAt(i) - '0');
            t *= 10;
        }
        url_descrip = "https://pokeapi.co/api/v2/pokemon-species/" + id + "/";

        nameTextView = findViewById(R.id.pokemon_name);

        numberTextView = findViewById(R.id.pokemon_number);
        type1TextView = findViewById(R.id.pokemon_type1);
        type2TextView = findViewById(R.id.pokemon_type2);
        btn_catch = findViewById(R.id.btn_catch);

        img_pokemon = findViewById(R.id.img_pokemon);

        description = findViewById(R.id.description);

        load();

        name = getIntent().getStringExtra("name");
        String check = getPreferences(Context.MODE_PRIVATE).getString(name, "C");

        assert check != null;
        btn_catch.setText(check.equals("C") ? "Catch" : "Release");
    }

    public void load() {
        type1TextView.setText("");
        type2TextView.setText("");
        description.setText("...Description...");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    nameTextView.setText(response.getString("name"));
                    numberTextView.setText("ID: " + String.format("#%03d", response.getInt("id")));
                    String urlImg = response.getJSONObject("sprites").getString("front_default");
                    new DownloadSpriteTask().execute(urlImg);

                    JSONArray typeEntries = response.getJSONArray("types");
                    for (int i = 0; i < typeEntries.length(); i++) {
                        JSONObject typeEntry = typeEntries.getJSONObject(i);
                        int slot = typeEntry.getInt("slot");
                        String type = typeEntry.getJSONObject("type").getString("name");

                        if (slot == 1) {
                            type1TextView.setText("   + " + type);
                        }
                        else if (slot == 2) {
                            type2TextView.setText("   + " + type);
                        }
                    }
                } catch (JSONException e) {
                    Log.e("cs50", "Pokemon json error", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("cs50", "Pokemon details error", error);
            }
        });

        JsonObjectRequest request_descrip =  new JsonObjectRequest(Request.Method.GET, url_descrip, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray fav = response.getJSONArray("flavor_text_entries");
                    for (int i = 0; i < fav.length(); i++) {
                        JSONObject elm = fav.getJSONObject(i);
                        JSONObject lang = elm.getJSONObject("language");
                        if (lang.getString("name").equals("en")) {
                            description.setText(elm.getString("flavor_text"));
                            break;
                        } else {
                            Log.e("cs50", "index " + i);
                        }
                    }
                    Log.e("cs50 TEST text desp", "text: " + description.getText() + " | END Text");
                } catch (JSONException e) {
                    Log.e("cs50", "Description error");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("cs50", "Pokemon details error", error);
            }
        });

        requestQueue.add(request);
        requestQueue.add(request_descrip);
    }

    public void toggleCatch(View view) {
        String check = getPreferences(Context.MODE_PRIVATE).getString(name, "C");
        assert check != null;

        boolean status = check.equals("C");
        btn_catch.setText(status ? "Release" : "Catch");
        getPreferences(Context.MODE_PRIVATE).edit().putString(name, status ? "R" : "C").apply();
    }

    private class DownloadSpriteTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                return BitmapFactory.decodeStream(url.openStream());
            }
            catch (IOException e) {
                Log.e("cs50", "Download sprite error", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            // load the bitmap into the ImageView!
            img_pokemon.setImageBitmap(bitmap);
        }
    }
}

