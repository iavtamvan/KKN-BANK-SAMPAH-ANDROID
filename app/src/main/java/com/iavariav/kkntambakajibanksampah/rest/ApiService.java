package com.iavariav.kkntambakajibanksampah.rest;

import com.iavariav.kkntambakajibanksampah.model.RiwayatModel;
import com.iavariav.kkntambakajibanksampah.model.StatusSampahModel;
import com.iavariav.kkntambakajibanksampah.model.StokBarangModel;

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

    @FormUrlEncoded
    @POST("user/api__tambah_pemesanan_sampah.php")
    Call<ResponseBody> postPemesanan(@Field("id_user") String id_user,
                                @Field("nama_penyetor") String nama_penyetor,
                                @Field("alamat_penyetor") String alamat_penyetor,
                                @Field("lat") String lat,
                                @Field("longi") String longi,
                                @Field("jenis_sampah") String jenis_sampah,
                                @Field("berat_sampah") String berat_sampah,
                                @Field("status_sampah") String status_sampah,
                                @Field("token_reg") String token_reg,
                                @Field("point") String point
    );
    @FormUrlEncoded
    @POST("user/tukar_sampah.php")
    Call<ResponseBody> postTukarSampah(
                                @Field("id_user") String id_user,
                                @Field("kurang") String kurang,
                                @Field("nama_barang") String nama_barang,
                                @Field("nama_pemasok") String nama_pemasok,
                                @Field("tipe_barang") String tipe_barang,
                                @Field("deskripsi_barang") String deskripsi_barang,
                                @Field("point_barang") String point_barang,
                                @Field("reg_barang") String reg_barang,
                                @Field("foto_url_barang") String foto_url_barang,
                                @Field("status_barang") String status_barang,
                                @Field("tgl_penukaran_barang") String tgl_penukaran_barang,
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
}