package com.iavariav.kkntambakajibanksampah.rest;

import com.iavariav.kkntambakajibanksampah.model.EdukasiModel;
import com.iavariav.kkntambakajibanksampah.model.RiwayatModel;
import com.iavariav.kkntambakajibanksampah.model.StatusSampahModel;
import com.iavariav.kkntambakajibanksampah.model.StokBarangModel;
import com.iavariav.kkntambakajibanksampah.model.berita.ArticlesItem;
import com.iavariav.kkntambakajibanksampah.model.berita.RootBeritaModel;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @GET("api_login.php")
    Call<ResponseBody> getLogin(@Query("username") String username,
                                @Query("password") String password);
    @GET("api_get.php")
    Call<ArrayList<RiwayatModel>> getRiwayat(@Query("change") String change,
                                             @Query("nama_penyetor") String nama_penyetor);
    @GET("api_get.php")
    Call<ArrayList<StokBarangModel>> getStokBarang(@Query("change") String change);

    @GET("api_get.php")
    Call<ArrayList<RiwayatModel>> getTukarBarangRiwayat(@Query("change") String change,
                                                           @Query("id_user") String idUser);
    @GET("api_get.php")
    Call<ArrayList<StatusSampahModel>> getStatusBarang(@Query("change") String change,
                                                       @Query("id_user") String idUser);
    @GET("api_get.php")
    Call<ArrayList<StatusSampahModel>> getStatusBarangSelesai(@Query("change") String change);

    @GET("api_get.php")
    Call<ArrayList<StatusSampahModel>> getSampahNotif(@Query("change") String change);

    @GET("api_get.php")
    Call<ArrayList<EdukasiModel>> getEdukasi(@Query("change") String change);

    @GET("api_get.php")
    Call<ResponseBody> getHome(@Query("change") String change,
                               @Query("reg_id") String regId);

    @GET("firebase")
    Call<ResponseBody> pushNotif(
            @Query("title") String title,
            @Query("message") String message,
            @Query("push_type") String push_type,
            @Query("regId") String regId

    );

    @FormUrlEncoded
    @POST("user/api__tambah_pemesanan_sampah.php")
    Call<ResponseBody> postPemesanan(@Field("id_user") String id_user,
                                @Field("nama_penyetor") String nama_penyetor,
                                @Field("alamat_penyetor") String alamat_penyetor,
                                @Field("lat") double lat,
                                @Field("longi") double longi,
                                @Field("jenis_sampah") String jenis_sampah,
//                                @Field("berat_sampah") String berat_sampah,
                                @Field("status_sampah") String status_sampah,
                                @Field("token_reg") String token_reg,
//                                @Field("point") String point,
                                @Field("firebase_id") String firebase_id,
                                @Field("reg_id") String reg_id
    );
    @FormUrlEncoded
    @POST("api_update_reg_id.php")
    Call<ResponseBody> postUpdateRegID(@Field("firebase_id") String firebaseid,
                                @Field("id_user") String id_user
    );
    @FormUrlEncoded
    @POST("user/tukar_sampah.php")
    Call<ResponseBody> postTukarSampah(
                                @Field("id_user") String id_user,
                                @Field("reg_id") String reg_id,
                                @Field("kurang") String kurang,
                                @Field("nama_barang") String nama_barang,
                                @Field("nama_pemasok") String nama_pemasok,
                                @Field("tipe_barang") String tipe_barang,
                                @Field("deskripsi_barang") String deskripsi_barang,
                                @Field("point_barang") String point_barang,
                                @Field("reg_barang") String reg_barang,
                                @Field("foto_url_barang") String foto_url_barang,
                                @Field("status_barang") String status_barang,
                                @Field("kadaluarsa_barang") String kadaluarsa_barang,
                                @Field("change") String change
    );
    @FormUrlEncoded
    @POST("petugas/aprove_sampah.php")
    Call<ResponseBody> postAproveSampah(
                                @Field("token_reg") String token_reg,
                                @Field("status_sampah") String status_sampah,
                                @Field("nama_petugas") String nama_petugas
    );
    @FormUrlEncoded
    @POST("petugas/update_berat_sampah.php")
    Call<ResponseBody> postSelesaiSampah(
                                @Field("token_reg") String token_reg,
                                @Field("berat_sampah") String berat_sampah
    );
    @FormUrlEncoded
    @POST("user/api_daftar_akun.php")
    Call<ResponseBody> postDaftarUser(
                                @Field("nama_user") String nama_user,
                                @Field("nik_user") String nik_user,
                                @Field("alamat_user") String alamat_user,
                                @Field("lat_user") double lat_user,
                                @Field("long_user") double long_user,
                                @Field("username") String username,
                                @Field("password") String password,
                                @Field("reg_id") String reg_id,
                                @Field("firebase_id") String firebase_id,
                                @Field("rt") String rt,
                                @Field("rw") String rw
    );
    @FormUrlEncoded
    @POST("petugas/update_poin_selesai.php")
    Call<ResponseBody> postUpdatePoin(
                                @Field("reg_id") String idUser,
                                @Field("token_reg") String token
    );


    // API BERITA

    @GET("everything?q=sampah&apiKey=12850cd010b54441aaeff6749dc99cd0")
    Call<RootBeritaModel> getBeritaSampah();


}