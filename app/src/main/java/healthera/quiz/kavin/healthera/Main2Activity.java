package healthera.quiz.kavin.healthera;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import adapter.DownloadImageTask;
import cz.msebera.android.httpclient.Header;

public class Main2Activity extends AppCompatActivity {


    String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl9pZCI6IjhlMjhjNzAwLTgzZjYtMTFlNy1hMjhiLWQ2NGFmMGUwZWUwNiIsImV4cCI6MTUxMTY4ODA2MiwiaWF0IjoxNTAzMDQ4MDYyLCJ1c2VyX2lkIjoiZWQwMTBjZDAtODM1NC0xMWU3LTg3NWYtMGJmNTQyOWIyOTBjIn0.w7_qpNCx164kgJ9XzE2qoKLN8bJGObCOEYNgk_f_1Ic";
    private JSONArray responseJsonArrayPharmacy;
    private TextView descriptionTV, priceTV;
    private ImageView imageView;

    String description, price, icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        descriptionTV = (TextView) findViewById(R.id.descriptionTv);
        priceTV = (TextView) findViewById(R.id.priceTv);
        imageView = (ImageView) findViewById(R.id.imageView);

        Intent intent = getIntent();
        String place_id = intent.getStringExtra("place_id");


        String url = "https://api.84r.co/pharmacies/"+place_id+"/services";

        fetchServices(url);
    }


    private void fetchServices(String url) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("app-version", "1.5");
        client.addHeader("app-platform", "ios");
        client.addHeader("client-id", "mzUFOsQJLESdYOY");
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Accept", "application/json");
        client.addHeader("Token", TOKEN);

        client.get(url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                final String responseJson = response.toString();


                try {
                    responseJsonArrayPharmacy = response.getJSONArray("data");


                    for (int i = 0; i < responseJsonArrayPharmacy.length(); i++) {
                        try {

                             description = responseJsonArrayPharmacy.getJSONObject(i).getString("service_description");
                             price = responseJsonArrayPharmacy.getJSONObject(i).getString("service_price");
                             icon = responseJsonArrayPharmacy.getJSONObject(i).getString("service_icon");


                            descriptionTV.setText(description);
                            priceTV.setText(price);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } new DownloadImageTask(imageView).execute(icon);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

}
