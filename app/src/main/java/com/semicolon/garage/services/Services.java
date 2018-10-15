package com.semicolon.garage.services;

import com.semicolon.garage.models.BankAccountModel;
import com.semicolon.garage.models.Country_Nationality;
import com.semicolon.garage.models.ResponsModel;
import com.semicolon.garage.models.SocialContactModel;
import com.semicolon.garage.models.Terms_Conditions;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Services {
    @GET("AboutApp/Countries")
    Call<List<Country_Nationality>> getCountries_Nationality();

    @GET("AboutApp/TermsAndConditions")
    Call<Terms_Conditions> getTerms_Condition();

    @GET("AppUser/Logout/{user_id}")
    Call<ResponsModel> logout(@Path("user_id") String user_id);

    @FormUrlEncoded
    @POST("AppUser/UpdateLocation/{user_id}")
    Call<ResponsModel> updateLocation(@Path("user_id") String user_id,
                                      @Field("user_google_lat") double user_google_lat,
                                      @Field("user_google_long") double user_google_long
                                      );

    @GET("AboutApp/Banks")
    Call<List<BankAccountModel>> getBanks();


    @GET("AboutApp/SocialMedia")
    Call<SocialContactModel> getContacts();

    @FormUrlEncoded
    @POST("AboutApp/ContactUs")
    Call<ResponsModel> sendProblemViaContact(@Field("name") String name,
                                             @Field("email") String email,
                                             @Field("subject") String subject,
                                             @Field("message") String messag);
}
