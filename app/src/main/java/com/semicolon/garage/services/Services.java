package com.semicolon.garage.services;

import com.semicolon.garage.models.BankAccountModel;
import com.semicolon.garage.models.CityModel;
import com.semicolon.garage.models.Country_Nationality;
import com.semicolon.garage.models.MaintenanceModel;
import com.semicolon.garage.models.RentModel;
import com.semicolon.garage.models.ResponsModel;
import com.semicolon.garage.models.SocialContactModel;
import com.semicolon.garage.models.Terms_Conditions;
import com.semicolon.garage.models.UnReadeModel;
import com.semicolon.garage.models.UserModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
                                             @Field("message") String message);

    @GET("Api/Cars/{user_id}/{id_country}/{type}")
    Call<List<RentModel>> getVehicleData(@Path("user_id") String user_id,
                                         @Path("id_country") String id_country,
                                         @Path("type") String type
                                            );

    @GET("Api/Maintenance/{user_id}/{id_country}/{type}")
    Call<List<MaintenanceModel>> getMaintenance(@Path("user_id") String user_id,
                                                @Path("id_country") String id_country,
                                                @Path("type") String type
                                                );

    @GET("AboutApp/Cities")
    Call<List<CityModel>> getCity();

    @FormUrlEncoded
    @POST("AppUser/Login")
    Call<UserModel> SignIn(@Field("user_email") String user_email,
                           @Field("user_pass") String user_pass
                           );


    @Multipart
    @POST("AppUser/SignUp")
    Call<UserModel> SignUp(@Part("user_pass")RequestBody user_pass,
                           @Part("user_phone")RequestBody user_phone,
                           @Part("user_nationality")RequestBody user_nationality,
                           @Part("user_email")RequestBody user_email,
                           @Part("user_full_name")RequestBody user_full_name,
                           @Part("user_token_id")RequestBody user_token_id,
                           @Part("user_google_lat")RequestBody user_google_lat,
                           @Part("user_google_long")RequestBody user_google_long,
                           @Part("country_personal_proof")RequestBody country_personal_proof,
                           @Part MultipartBody.Part user_photo,
                           @Part MultipartBody.Part document_type_definition
                           );

    @FormUrlEncoded
    @POST("AppUser/UpdateTokenId/{user_id}")
    Call<ResponsModel> updateTokenId(@Path("user_id") String user_id,
                                     @Field("user_token_id") String user_token_id);

    @FormUrlEncoded
    @POST("Api/Evaluation/{id_car_maintenance}")
    Call<ResponsModel> sendEvaluation(@Path("id_car_maintenance") String id_car_maintenance ,
                                      @Field("evaluation_value") float evaluation_value
                                      );

    @Multipart
    @POST("AppUser/Profile/{user_id}")
    Call<UserModel> updateProfileData(@Path("user_id") String user_id,
                                      @Part("user_phone") RequestBody user_phone,
                                      @Part("user_nationality") RequestBody user_nationality,
                                      @Part("user_email") RequestBody user_email,
                                      @Part("user_full_name") RequestBody user_full_name,
                                      @Part("country_personal_proof") RequestBody country_personal_proof
                                      );

    @FormUrlEncoded
    @POST("AppUser/UpdatePass/{user_id}")
    Call<UserModel> updatePassword(@Path("user_id") String user_id,
                                   @Field("user_old_pass") String user_old_pass,
                                   @Field("user_new_pass") String user_new_pass
                                   );

    @Multipart
    @POST("AppUser/Profile/{user_id}")
    Call<UserModel> updateImage(@Path("user_id") String user_id,
                                       @Part("user_phone") RequestBody user_phone,
                                       @Part("user_nationality") RequestBody user_nationality,
                                       @Part("user_email") RequestBody user_email,
                                       @Part("user_full_name") RequestBody user_full_name,
                                       @Part("country_personal_proof") RequestBody country_personal_proof,
                                       @Part MultipartBody.Part image
                                       );
    @FormUrlEncoded
    @POST("Api/Reservation")
    Call<ResponsModel> canReserve(@Field("reservation_start_date") String reservation_start_date,
                                  @Field("reservation_end_date") String reservation_end_date,
                                  @Field("id_car_maintenance") String id_car_maintenance
                                  );

    @Multipart
    @POST("Api/Reservation")
    Call<ResponsModel> Reserve(@Part("id_car_maintenance") RequestBody id_car_maintenance,
                               @Part("user_id") RequestBody user_id,
                               @Part("reservation_cost") RequestBody reservation_cost,
                               @Part("reservation_start_date") RequestBody reservation_start_date,
                               @Part("reservation_end_date") RequestBody reservation_end_date,
                               @Part("reservation_address") RequestBody reservation_address,
                               @Part("reservation_google_lat") RequestBody reservation_google_lat,
                               @Part("reservation_google_long") RequestBody reservation_google_long,
                               @Part("transformation_person") RequestBody transformation_person,
                               @Part("transformation_phone") RequestBody transformation_phone,
                               @Part("transformation_amount") RequestBody transformation_amount,
                               @Part MultipartBody.Part transformation_image
                               );

    @GET("Api/UnReadAlerts/{user_id}")
    Call<UnReadeModel> getUnReadNotification(@Path("user_id") String user_id);

    @FormUrlEncoded
    @POST("Api/UnReadAlerts/{user_id}")
    Call<ResponsModel> readNotification(@Path("user_id") String user_id,@Field("read_all") String read_all);

}
