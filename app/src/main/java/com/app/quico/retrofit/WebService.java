package com.app.quico.retrofit;


import com.app.quico.entities.AllServicesEnt;
import com.app.quico.entities.BatchCount;
import com.app.quico.entities.Chat.ChatThreadEnt;
import com.app.quico.entities.Chat.ThreadMsgesEnt;
import com.app.quico.entities.CitiesEnt;
import com.app.quico.entities.Cms;
import com.app.quico.entities.CompanyDetail;
import com.app.quico.entities.CompanyEnt;
import com.app.quico.entities.NotificationEnt;
import com.app.quico.entities.ResponseWrapper;
import com.app.quico.entities.ServicesEnt;
import com.app.quico.entities.UpdateProfile;
import com.app.quico.entities.UpdateProfileResponse;
import com.app.quico.entities.UserEnt;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WebService {


    @FormUrlEncoded
    @POST("login")
    Call<ResponseWrapper<UserEnt>> loginUser(@Field("email") String email,
                                             @Field("password") String password,
                                             @Field("device_type") String device_type,
                                             @Field("device_token") String device_token
    );


    @FormUrlEncoded
    @POST("register")
    Call<ResponseWrapper<UserEnt>> registerUser(@Field("name") String name,
                                                @Field("country_code") String country_code,
                                                @Field("phone") String phone,
                                                @Field("email") String email,
                                                @Field("password") String password,
                                                @Field("password_confirmation") String password_confirmation,
                                                @Field("role") String role,
                                                @Field("push_notification") String push_notification,
                                                @Field("device_type") String device_type,
                                                @Field("device_token") String device_token);

    @FormUrlEncoded
    @POST("social_login")
    Call<ResponseWrapper<UserEnt>> socialLogin(@Field("platform") String platform,
                                               @Field("client_id") String client_id,
                                               @Field("token") String token,
                                               @Field("username") String username,
                                               @Field("email") String email,
                                               @Field("role") String role,
                                               @Field("push_notification") String push_notification,
                                               @Field("device_token") String device_token,
                                               @Field("device_type") String device_type,
                                               @Field("image") String image);

    @FormUrlEncoded
    @POST("social_login")
    Call<ResponseWrapper<UserEnt>> socialLogin(@Field("platform") String platform,
                                               @Field("client_id") String client_id,
                                               @Field("username") String username,
                                               @Field("email") String email,
                                               @Field("role") String role,
                                               @Field("push_notification") String push_notification,
                                               @Field("device_token") String device_token,
                                               @Field("device_type") String device_type,
                                               @Field("image") String image);


    @GET("forget-password")
    Call<ResponseWrapper<UserEnt>> forgetPassword(@Query("country_code") String country_code,
                                                  @Query("phone") String phone);

    @FormUrlEncoded
    @POST("verify-reset-code")
    Call<ResponseWrapper> verifyForgotCode(@Field("country_code") String country_code,
                                           @Field("phone") String phone,
                                           @Field("verification_code") String verification_code
    );

    @FormUrlEncoded
    @POST("register-verify-reset-code")
    Call<ResponseWrapper> verifyCode(@Field("country_code") String country_code,
                                     @Field("phone") String phone,
                                     @Field("verification_code") String verification_code
    );


    @FormUrlEncoded
    @POST("reset-password")
    Call<ResponseWrapper> resetPassword(@Field("country_code") String country_code,
                                        @Field("phone") String phone,
                                        @Field("verification_code") String verification_code,
                                        @Field("password") String password,
                                        @Field("password_confirmation") String password_confirmation
    );

    @FormUrlEncoded
    @POST("change-password")
    Call<ResponseWrapper> changePassword(@Field("current_password") String current_password,
                                         @Field("password") String password,
                                         @Field("password_confirmation") String password_confirmation
    );

    @GET("resend-code")
    Call<ResponseWrapper> resendCode();

    @POST("logout")
    Call<ResponseWrapper> logout();

    @FormUrlEncoded
    @POST("change-push-notification")
    Call<ResponseWrapper> chanePushNotification(@Field("push_notification") String push_notification);

    @FormUrlEncoded
    @POST("contactus")
    Call<ResponseWrapper> contactUs(@Field("name") String name,
                                    @Field("email") String email,
                                    @Field("phone") String phone,
                                    @Field("message") String message,
                                    @Field("company_name") String company_name
    );

    @FormUrlEncoded
    @POST("change-device-token")
    Call<ResponseWrapper> updateToken(@Field("device_type") String device_type,
                                      @Field("device_token") String device_token);

    @GET("pages/{id}")
    Call<ResponseWrapper<Cms>> Cms(@Path("id") String id);

    @GET("get-batch-count")
    Call<ResponseWrapper<BatchCount>> bacthCount();

    @GET("services")
    Call<ResponseWrapper<ArrayList<ServicesEnt>>> getServices(@Query("locale") String locale);


    @Multipart
    @POST("users")
    Call<ResponseWrapper<UpdateProfileResponse>> updateProfile(
            @Part("id") RequestBody userId,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("country_code") RequestBody country_code,
            @Part("phone") RequestBody phone,
            @Part("address") RequestBody address,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part MultipartBody.Part image
    );


    @GET("notifications")
    Call<ResponseWrapper<ArrayList<NotificationEnt>>> notifications(
    );


    @GET("companies/{id}")
    Call<ResponseWrapper<CompanyDetail>> getCompanyDetail(
            @Path("id") String id,
            @Query("locale") String locale
    );

    @FormUrlEncoded
    @POST("user-company-rate")
    Call<ResponseWrapper<CompanyDetail>> rating(
            @Field("company_id") String company_id,
            @Field("rate") String rate,
            @Field("review") String review);

    @FormUrlEncoded
    @POST("company-favorite")
    Call<ResponseWrapper<CompanyDetail>> favorite(
            @Field("company_id") String company_id,
            @Field("like") String like);

    @GET("cities")
    Call<ResponseWrapper<ArrayList<CitiesEnt>>> getCities(
            @Query("lang") String lang
    );

    @GET("get-favorites")
    Call<ResponseWrapper<ArrayList<CompanyEnt>>> getFavorites(@Query("locale") String locale);


//companies

    @GET("companies")
    Call<ResponseWrapper<ArrayList<CompanyEnt>>> getCompanies(
            @Query("service_id") String service_id,
            @Query("is_feature") Integer is_feature,
            @Query("is_rate") Integer is_rate,
            @Query("is_review") Integer is_review,
            @Query("is_near") Integer is_near
    );
    @GET("companies")
    Call<ResponseWrapper<ArrayList<CompanyEnt>>> getCompanies(
            @Query("service_id") String service_id,
            @Query("is_feature") Integer is_feature,
            @Query("is_rate") Integer is_rate,
            @Query("is_review") Integer is_review,
            @Query("is_near") Integer is_near,
            @Query("locale") String locale
    );



    @GET("companies")
    Call<ResponseWrapper<ArrayList<CompanyEnt>>> getCompanies(
            @Query("service_id") String service_id,
            @Query("latitude") String latitude,
            @Query("longitude") String longitude,
            @Query("is_feature") Integer is_feature,
            @Query("is_rate") Integer is_rate,
            @Query("is_review") Integer is_review,
            @Query("is_near") Integer is_near
    );

    @GET("companies")
    Call<ResponseWrapper<ArrayList<CompanyEnt>>> getCompanies(
            @Query("service_id") String service_id,
            @Query("latitude") String latitude,
            @Query("longitude") String longitude,
            @Query("is_feature") Integer is_feature,
            @Query("is_rate") Integer is_rate,
            @Query("is_review") Integer is_review,
            @Query("is_near") Integer is_near,
            @Query("locale") String locale
    );

    @GET("companies")
    Call<ResponseWrapper<ArrayList<CompanyEnt>>> getCompanies(
            @Query("service_id") String service_id,
            @Query("city_id") String city_id,
            @Query("area_id") String area_id,
            @Query("latitude") String latitude,
            @Query("longitude") String longitude,
            @Query("is_feature") Integer is_feature,
            @Query("is_rate") Integer is_rate,
            @Query("is_review") Integer is_review,
            @Query("is_near") Integer is_near
    );

    @GET("companies")
    Call<ResponseWrapper<ArrayList<CompanyEnt>>> getCompanies(
            @Query("service_id") String service_id,
            @Query("city_id") String city_id,
            @Query("area_id") String area_id,
            @Query("latitude") String latitude,
            @Query("longitude") String longitude,
            @Query("is_feature") Integer is_feature,
            @Query("is_rate") Integer is_rate,
            @Query("is_review") Integer is_review,
            @Query("is_near") Integer is_near,
            @Query("locale") String locale
    );


    @GET("get-all-services")
    Call<ResponseWrapper<ArrayList<AllServicesEnt>>> getAllServices(@Query("locale") String locale);

    @GET("chats")
    Call<ResponseWrapper<ArrayList<ChatThreadEnt>>> getChatThreads(
            @Query("offset") int offset,
            @Query("limit") int limit
            );

    @GET("chats/{id}")
    Call<ResponseWrapper<ArrayList<ThreadMsgesEnt>>> getThreadMsges(
            @Path("id") String id,
            @Query("offset") int offset,
            @Query("limit") int limit
         );

    @GET("get-thread/{id}")
    Call<ResponseWrapper<ChatThreadEnt>> getThreadDetail(
            @Path("id") String id);



    @Multipart
    @POST("chats")
    Call<ResponseWrapper<ThreadMsgesEnt>> sendMediaMessage(
            @Part("company_id") RequestBody company_id,
            @Part("receiver_id") RequestBody receiver_id,
            @Part("message") RequestBody message,
            @Part ArrayList<MultipartBody.Part> file,
            @Part ArrayList<MultipartBody.Part> thumb_nail,
            @Part ArrayList<MultipartBody.Part> type);

    @FormUrlEncoded
    @POST("chats")
    Call<ResponseWrapper<ThreadMsgesEnt>> sendLocation(
            @Field("company_id") String company_id,
            @Field("receiver_id") String receiver_id,
            @Field("address") String address,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("chats")
    Call<ResponseWrapper<ThreadMsgesEnt>> sendMessage(
            @Field("company_id") String company_id,
            @Field("receiver_id") String receiver_id,
            @Field("message") String message);

    @DELETE("chats/{id}")
    Call<ResponseWrapper> deleteThread(
            @Path("id") String id);
}