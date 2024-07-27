package com.example.weatherupdate;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    EditText etCity, etCountry;
    TextView tvResult;
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCity = findViewById(R.id.etCity);
        etCountry = findViewById(R.id.etCountry);
        tvResult = findViewById(R.id.tvResult);

        // Other initialization code
    }



    public void getWeatherDetails(View view) {
        String city = etCity.getText().toString().trim();
        String country = etCountry.getText().toString().trim();
        tvResult.setTextColor(Color.BLACK); // Change to your desired color

        if (city.isEmpty()) {
            tvResult.setText(R.string.scity_field_can_not_be_empty);
        } else {
            String apiKey = "3928a56c693edd6edcc48973a2e1bf72";
            String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "," + country + "&appid=" + apiKey;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONObject main = jsonResponse.getJSONObject("main");
                    JSONObject weatherObject = jsonResponse.getJSONArray("weather").getJSONObject(0);

                    double temp = main.getDouble("temp") - 273.15;
                    double feelsLike = main.getDouble("feels_like") - 273.15;
                    int humidity = main.getInt("humidity");
                    String description = weatherObject.getString("description");

                    tvResult.setTextColor(Color.rgb(68, 134, 199));
                    String output = "Current weather of " + city + " (" + country + ")" +
                            "\nTemp: " + df.format(temp) + "°C" +
                            "\nFeels Like: " + df.format(feelsLike) + "°C" +
                            "\nHumidity: " + humidity + "%" +
                            "\nDescription: " + description;
                    tvResult.setTextColor(Color.WHITE);
                    tvResult.setText(output);

                } catch (JSONException e) {
                    e.printStackTrace();
                    tvResult.setText("Error parsing JSON");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    tvResult.setText("Error fetching weather data");
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

}
