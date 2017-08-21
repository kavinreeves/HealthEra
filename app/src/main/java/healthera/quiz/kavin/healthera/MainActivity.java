package healthera.quiz.kavin.healthera;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.CardAdapter;
import cz.msebera.android.httpclient.Header;
import model.Pharmacy;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {


    JSONArray responseJsonArrayPharmacy;
    JSONArray responseJsonArrayServices;

    private List<Pharmacy> pharmacyList = new ArrayList<>();
    private CardAdapter mCardAdapter;
    RecyclerView mRecyclerView;
    private static final String TAG = "MainActivity";

    String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl9pZCI6IjhlMjhjNzAwLTgzZjYtMTFlNy1hMjhiLWQ2NGFmMGUwZWUwNiIsImV4cCI6MTUxMTY4ODA2MiwiaWF0IjoxNTAzMDQ4MDYyLCJ1c2VyX2lkIjoiZWQwMTBjZDAtODM1NC0xMWU3LTg3NWYtMGJmNTQyOWIyOTBjIn0.w7_qpNCx164kgJ9XzE2qoKLN8bJGObCOEYNgk_f_1Ic";

    private Observable<JSONArray> mObservable;
    private Observer<JSONArray> mObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /**
         * Set up Android CardView/RecycleView
         */
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCardAdapter = new CardAdapter(pharmacyList);
        mRecyclerView.setAdapter(mCardAdapter);


        Observable<JSONArray> fetchFromApi1 = Observable.create(new Observable.OnSubscribe<JSONArray>() {
            @Override
            public void call(Subscriber<? super JSONArray> subscriber) {
                try {
                    JSONArray data = fetchData("https://api.84r.co/locations?lat=51.657&long=0.121&radius=50000.0&unit=km");
                    subscriber.onNext(data); // Emit the contents of the URL
                    subscriber.onCompleted(); // Nothing more to emit
                } catch (Exception e) {
                    subscriber.onError(e); // In case there are network errors
                }
            }
        });


        fetchFromApi1
                .subscribeOn(Schedulers.newThread()) // Create a new Thread
                .observeOn(AndroidSchedulers.mainThread()) // Use the UI thread
                .subscribe(new Action1<JSONArray>() {
                    @Override
                    public void call(JSONArray s) {

                        Log.d("RXResult", String.valueOf(s));
                        for (int i = 0; i < s.length(); i++) {
                            try {

                                String name = s.getJSONObject(i).getString("place_title");
                                String id = s.getJSONObject(i).getString("place_id");
                                String distance = s.getJSONObject(i).getString("current_distance") + " miles";

                                Log.d("alldata", name + id + distance);

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


}
