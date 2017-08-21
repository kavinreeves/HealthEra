package healthera.quiz.kavin.healthera;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.CardAdapter;
import adapter.RecyclerItemClickListener;
import cz.msebera.android.httpclient.Header;
import model.Pharmacy;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static healthera.quiz.kavin.healthera.R.id.recyclerView;

public class MainActivity extends AppCompatActivity {


    private JSONArray responseJsonArrayPharmacy;
    private JSONArray responseJsonArrayServices;

    private List<Pharmacy> pharmacyList = new ArrayList<>();
    private CardAdapter mCardAdapter;
    RecyclerView mRecyclerView;
    private static final String TAG = "MainActivity";

    String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl9pZCI6IjhlMjhjNzAwLTgzZjYtMTFlNy1hMjhiLWQ2NGFmMGUwZWUwNiIsImV4cCI6MTUxMTY4ODA2MiwiaWF0IjoxNTAzMDQ4MDYyLCJ1c2VyX2lkIjoiZWQwMTBjZDAtODM1NC0xMWU3LTg3NWYtMGJmNTQyOWIyOTBjIn0.w7_qpNCx164kgJ9XzE2qoKLN8bJGObCOEYNgk_f_1Ic";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /**
         * Set up Android CardView/RecycleView
         */
        mRecyclerView = (RecyclerView) findViewById(recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCardAdapter = new CardAdapter(pharmacyList);
        mRecyclerView.setAdapter(mCardAdapter);


        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Pharmacy pharmacy = mCardAdapter.getItem(position);
                String id = pharmacy.getId();

                Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                intent.putExtra("place_id", id);
                startActivity(intent);


            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        Observable<JSONArray> fetchFromApi1 = Observable.create(new Observable.OnSubscribe<JSONArray>() {
            @Override
            public void call(Subscriber<? super JSONArray> subscriber) {
                try {
                    JSONArray data = fetchData("https://api.84r.co/locations?lat=51.657&long=0.121&radius=50000.0&unit=km");
                    subscriber.onNext(data);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });

       /* Observable<JSONArray> fetchFromApi2 = Observable.create(new Observable.OnSubscribe<JSONArray>() {
            @Override
            public void call(Subscriber<? super JSONArray> subscriber) {
                try {
                    JSONArray data = fetchData2("https://api.84r.co/pharmacies/:place_id/services");
                    subscriber.onNext(data);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });*/


        fetchFromApi1
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JSONArray>() {
                    @Override
                    public void call(JSONArray s) {

                        Log.d(TAG, String.valueOf(s));
                        for (int i = 0; i < s.length(); i++) {
                            try {

                                String name = s.getJSONObject(i).getString("place_title");
                                String id = s.getJSONObject(i).getString("place_id");
                                String distance = s.getJSONObject(i).getString("current_distance") + " miles";



                                Pharmacy pharmacy = new Pharmacy(name, id, distance);
                                pharmacyList.add(pharmacy);
                                mCardAdapter.notifyDataSetChanged();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });




    }


    private JSONArray fetchData(String url) {

        SyncHttpClient client = new SyncHttpClient();
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
                Log.d("async_sucess", responseJson);

                try {
                    responseJsonArrayPharmacy = response.getJSONArray("data");
                    Log.d("resultJSon", String.valueOf(responseJsonArrayPharmacy));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return responseJsonArrayPharmacy;
    }

   /* private JSONArray fetchData2(String url) {

        SyncHttpClient client = new SyncHttpClient();
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
                Log.d("async_sucess", responseJson);

                try {
                    responseJsonArrayPharmacy = response.getJSONArray("data");
                    Log.d("resultJSon", String.valueOf(responseJsonArrayPharmacy));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return responseJsonArrayPharmacy;
    }
*/

}
