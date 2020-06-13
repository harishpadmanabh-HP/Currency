package com.hp.ocr_googlevisionapi.network;

import com.hp.ocr_googlevisionapi.models.HistoryModel;
import com.hp.ocr_googlevisionapi.models.LoginModel;
import com.hp.ocr_googlevisionapi.models.SignUpModel;
import com.hp.ocr_googlevisionapi.models.StoreMoneyModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Apis {

    @GET("user_reg.php?")
    Call<SignUpModel> signUpCall(@Query("name") String name,
                                 @Query("phone") String phone);

    @GET("user_login.php?")
    Call<LoginModel> loginCall(@Query("phone") String phone);

    @GET("add_amount.php?")
    Call<StoreMoneyModel> storeMoneyCall(@Query("id") String id,
                                         @Query("amount") String amount);

    @GET("sum.php?")
    Call<HistoryModel> historyCall(@Query("id") String id);

}
