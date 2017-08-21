package service;

import model.Pharmacy;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by Kavin on 17/08/17.
 */

public interface PharmacyService {

    String SERVICE_ENDPOINT1 = "https://api.84r.co/locations?lat=51.657&long=0.121&radius=50000.0&unit=km";

    @GET("/users/{login}")
    Observable<Pharmacy> getUser(@Path("data") String data);
}


