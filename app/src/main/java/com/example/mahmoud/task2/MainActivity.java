package com.example.mahmoud.task2;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static android.graphics.ColorSpace.Model.XYZ;

public class MainActivity extends AppCompatActivity {
    StringRequest stringRequest;
    ArrayList<String> counries = new ArrayList<>();
    ArrayList<String> code = new ArrayList<>();
    ArrayList<String> codeNumber = new ArrayList<>();
    ArrayList<String> cites = new ArrayList<>();
    TextView register;
    EditText codetxt;
    Spinner spinnercity;
    Button changeLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Spinner codespinner = (Spinner) findViewById(R.id.code);
        final Spinner country = (Spinner) findViewById(R.id.country);
        spinnercity = (Spinner) findViewById(R.id.city);
        codetxt = (EditText) findViewById(R.id.zipcode);
        changeLanguage = (Button) findViewById(R.id.changelanguage);
        register = (TextView) findViewById(R.id.regist);
        Typeface typefaceuser = Typeface.createFromAsset(getAssets(), "fonts/GE Dinar One Medium.ttf");
        register.setTypeface(typefaceuser);
        changeLanguage.setTypeface(typefaceuser);

        counries.add("Country");
        code.add("Code");
        cites.add("city");
        spinner(cites).setDropDownViewResource(R.layout.spiner_item);
        spinnercity.setAdapter(spinner(cites));

        changeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String languageToLoad = "fr_FR";
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getResources().updateConfiguration(config, getResources().getDisplayMetrics());

            }
        });

        String url = "http://souq.hardtask.co/app/app.asmx/GetCountries";
        stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            Log.i("data2", response);

                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                counries.add(jsonObject1.getString(getResources().getString(R.string.title)));
                                code.add(jsonObject1.getString(getResources().getString(R.string.code)));
                                codeNumber.add(jsonObject1.getString("code"));
                            }

                            spinner(counries).setDropDownViewResource(R.layout.spiner_item);
                            country.setAdapter(spinner(counries));
                            spinner(code).setDropDownViewResource(R.layout.spiner_item);
                            codespinner.setAdapter(spinner(code));

                            codespinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    if (i > 0) {
                                        codetxt.setText(codeNumber.get(i).toString());
                                    }
                                }
                            });
                            country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    String selectedItemText = (String) parent.getItemAtPosition(position);
                                    // If user change the default selection
                                    // First item is disable and it is used for hint
                                    if (position > 0) {
                                        loadCity(position);
                                        Toast.makeText(MainActivity.this, counries.get(position).toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        } catch (Exception e) {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage());
                    }
                });
        Singleton.getInstance(MainActivity.this).addRequestQue(stringRequest);
    }

    ArrayAdapter spinner(ArrayList<String> llist) {
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spiner_item, llist) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        return spinnerArrayAdapter;
    }

    void loadCity(int position) {
        String url = "http://souq.hardtask.co/app/app.asmx/GetCities?countryId=" + position;
        stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            Log.i("data2", response);

                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                cites.add(jsonObject1.getString(getResources().getString(R.string.title)));

                            }

                            spinner(cites).setDropDownViewResource(R.layout.spiner_item);
                            spinnercity.setAdapter(spinner(cites));


                        } catch (Exception e) {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage());
                    }
                });
        Singleton.getInstance(MainActivity.this).addRequestQue(stringRequest);
    }












}
