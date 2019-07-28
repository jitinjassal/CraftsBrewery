package com.example.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements BeerAdapter.BeersAdapterListener{

    public static final String TAG= "MainActivity";
    RecyclerView recyclerView;
    //RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    //BottomSheetBehavior sheetBehavior;
    private BeerAdapter mAdapter;

    ArrayList<Beers> beersArrayList = new ArrayList<Beers>();

    List<Beers> beerList;

    ArrayList<Beers> styleArrayList = new ArrayList<Beers>();

    ArrayList<String> styleArrayList2 = new ArrayList<String>();

    ArrayList<String> duplicateList = new ArrayList<String>();

    ArrayList<HashMap<String, String>> styleList;
    HashMap<String, String> hashMapQuestions;

    Set<String> hashset = new LinkedHashSet<>();
    String strStyle;

//    List<Beers> beerList;

    RequestQueue rq;

    String request_url = "http://starlord.hackerearth.com/beercraft";

    private Toolbar toolbar;

    ImageView btnFilter;
    Button btnSort;
    TextView tvHighToLow, tvLowToHigh;
    TextView tvLager, tvAle, tvIPA;

    private SearchView searchView;
    Spinner spinnerBeerStyle;
    String strSpinner;
    private final static int LOADING_DURATION = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sheetBehavior = findViewById(R.id.shee);

        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        rq = Volley.newRequestQueue(this);

        sendRequest();
        loadingAndDisplayContent();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        btnFilter= findViewById(R.id.btnFilter);
        btnSort= findViewById(R.id.btnSort);

        layoutManager = new LinearLayoutManager(this);

        whiteNotificationBar(recyclerView);


        recyclerView.setLayoutManager(layoutManager);


        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterBottomSheetDialog();
            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSortBottomSheetDialog();
            }
        });




        mAdapter = new BeerAdapter(this, beersArrayList, this);

        whiteNotificationBar(recyclerView);


        recyclerView.setAdapter(mAdapter);





        ArrayAdapter<String> styleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, duplicateList);
        styleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBeerStyle = (Spinner)findViewById(R.id.spn_beer_style);
        spinnerBeerStyle.setAdapter(styleAdapter);
        spinnerBeerStyle.setAdapter(styleAdapter);
        spinnerBeerStyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int count=0;
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
//                System.out.println("whatever you wanna do");

                strSpinner = spinnerBeerStyle.getItemAtPosition(spinnerBeerStyle.getSelectedItemPosition()).toString();
                Log.d(TAG, "strSpinner : " + strSpinner);
                mAdapter.getFilter().filter(strSpinner);


            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                Toast.makeText(getBaseContext(),"inside no item selected ",Toast.LENGTH_SHORT).show();
            }
        });


        Log.d(TAG, "strSpinner : " + strSpinner);

    }


    private void loadingAndDisplayContent() {
        final LinearLayout lyt_progress = (LinearLayout) findViewById(R.id.lyt_progress);
        lyt_progress.setVisibility(View.VISIBLE);
        lyt_progress.setAlpha(1.0f);
//        recyclerView.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewAnimation.fadeOut(lyt_progress);
            }
        }, LOADING_DURATION);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //initComponent();
            }
        }, LOADING_DURATION + 400);
    }


    public void showFilterBottomSheetDialog() {
        View view = getLayoutInflater().inflate(R.layout.filter_fragment_bottom_sheet_dialog, null);

        tvLager = view.findViewById(R.id.tvLager);
        tvAle = view.findViewById(R.id.tvAle);
        tvIPA = view.findViewById(R.id.tvIPA);

        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();


        tvLager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mAdapter.getFilter().filter("Lager");
                //Toast.makeText(getApplicationContext(), "Lager", Toast.LENGTH_SHORT).show();

                //mAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }
        });

        tvAle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // mAdapter.getFilter().filter("Ale");

                //Toast.makeText(getApplicationContext(), "Ale", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });

        tvIPA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // mAdapter.getFilter().filter("IPA");

                //Toast.makeText(getApplicationContext(), "IPA", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });



    }

    public void showSortBottomSheetDialog() {
        View view = getLayoutInflater().inflate(R.layout.sort_fragment_bottom_sheet_dialog, null);

        tvLowToHigh = view.findViewById(R.id.tvLowToHigh);
        tvHighToLow = view.findViewById(R.id.tvHighToLow);

        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();

        tvLowToHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "LowtoHigh", Toast.LENGTH_SHORT).show();
                sendRequestABVLowToHigh();
                loadingAndDisplayContent();
                dialog.dismiss();

            }
        });

        tvHighToLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(getApplicationContext(), "High", Toast.LENGTH_SHORT).show();
                sendRequestABVHighToLow();
                loadingAndDisplayContent();
                dialog.dismiss();
            }
        });


    }

    public static JSONArray sortJsonArray(JSONArray array, final String type) {
        List<JSONObject> jsons = new ArrayList<JSONObject>();
        for (int i = 0; i < array.length(); i++) {
            try {
                jsons.add(array.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(jsons, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject lhs, JSONObject rhs) {
                String lid = null;
                try {
                    lid = lhs.getString(type);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String rid = null;
                try {
                    rid = rhs.getString(type);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Here you could parse string id to integer and then compare.
                return lid.compareTo(rid);
            }
        });
        return new JSONArray(jsons);
    }

    public void sendRequest(){

        beersArrayList = new ArrayList<>();

        styleList = new ArrayList<HashMap<String, String>>();


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, request_url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                Log.i(TAG, String.valueOf(response));

                //JSONArray jsA= sortJsonArray(response, "abv");


                for(int i = 0; i < response.length(); i++){

                    Beers beers = new Beers();

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        beers.setAbv("Alcohol Content: "+jsonObject.getString("abv"));
                        beers.setIbu(jsonObject.getString("ibu"));
                        beers.setId(jsonObject.getString("id"));
                        beers.setName(jsonObject.getString("name"));
                        beers.setStyle(jsonObject.getString("style"));
                        beers.setOunces("Serving: "+jsonObject.getString("ounces")+ " Ounces");


                        strStyle = jsonObject.getString("style");

//                        styleArrayList.add(strStyle);

                        hashMapQuestions = new HashMap<String, String>();


                        hashMapQuestions.put("strStyle", strStyle);
//                        hashMapQuestions.put("strAnswer", strAnswer);

                        styleList.add(hashMapQuestions);

                        styleArrayList2.add(strStyle);
                        duplicateList.add(strStyle);


                        LinkedHashSet<String> hashset2 = new LinkedHashSet<>(duplicateList);

                        Log.d(TAG, "+++++++++++++= HashMap : " + hashset2.size());



                        Log.d(TAG, "styleList : " + styleList.get(i) + styleList.size());

                        beersArrayList.add(i, beers);
                        spinnerBeerStyle.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, duplicateList));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    hashset.addAll(duplicateList);

                    duplicateList.clear();
                    duplicateList.addAll(hashset);



                    Log.d(TAG, "-------- HashMap : " + hashset.size() + "duplicate : " + duplicateList.size());



                    mAdapter.notifyDataSetChanged();

                }

//                mAdapter = new BeerAdapter(this,  beersArrayList, this);
//                recyclerView.setAdapter(mAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Volley Error: ", String.valueOf(error));
            }
        });

        rq.add(jsonArrayRequest);

    }


    public void sendRequestABVLowToHigh(){

        Log.d(TAG, "sendRequestABVLowToHigh");


        beersArrayList = new ArrayList<>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, request_url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                Log.i(TAG, String.valueOf(response));

                JSONArray jsA= sortJsonArray(response, "abv");


                for(int i = 0; i < jsA.length(); i++){

                    Beers beers = new Beers();

                    try {
                        JSONObject jsonObject = jsA.getJSONObject(i);

                        beers.setAbv(jsonObject.getString("abv"));
                        beers.setIbu(jsonObject.getString("ibu"));
                        beers.setId(jsonObject.getString("id"));
                        beers.setName(jsonObject.getString("name"));
                        beers.setStyle(jsonObject.getString("style"));
                        beers.setOunces(jsonObject.getString("ounces"));
                        beersArrayList.add(beers);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    mAdapter.notifyDataSetChanged();
                }

//                mAdapter = new BeerAdapter(MainActivity.this, beerList);
//                recyclerView.setAdapter(mAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Volley Error: ", String.valueOf(error));
            }
        });

        rq.add(jsonArrayRequest);

    }



    public void sendRequestABVHighToLow(){

        Log.d(TAG, "sendRequestABVHighToLow");

        beersArrayList = new ArrayList<>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, request_url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {




                Log.i(TAG, String.valueOf(response));

                JSONArray jsA= sortJsonArray(response, "abv");


                for(int i = jsA.length(); i > 0; i--){

                    Beers beers = new Beers();

                    try {
                        JSONObject jsonObject = jsA.getJSONObject(i);

                        beers.setAbv(jsonObject.getString("abv"));
                        beers.setIbu(jsonObject.getString("ibu"));
                        beers.setId(jsonObject.getString("id"));
                        beers.setName(jsonObject.getString("name"));
                        beers.setStyle(jsonObject.getString("style"));
                        beers.setOunces(jsonObject.getString("ounces"));

                        beersArrayList.add(beers);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//
//                    mAdapter = new BeerAdapter(MainActivity.this, beerList, this);
//                    recyclerView.setAdapter(mAdapter);

//                    mAdapter = new BeerAdapter(this, beersArrayList, this);
//                    recyclerView.setAdapter(mAdapter);

                    mAdapter.notifyDataSetChanged();

//                    mAdapter = new BeerAdapter(MainActivity.this, beerList);
//                    recyclerView.setAdapter(mAdapter);
                }

//                mAdapter = new BeerAdapter(this,  beerList, this);
//                recyclerView.setAdapter(mAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Volley Error: ", String.valueOf(error));
            }
        });

        rq.add(jsonArrayRequest);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        int id = item.getItemId();

        if (id == R.id.action_search) {
            return true;
        }

        if (id == R.id.action_cart) {
            Toast.makeText(getApplicationContext(), "Cart", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }


    @Override
    public void onBeerSelected(Beers beers) {

    }

}
