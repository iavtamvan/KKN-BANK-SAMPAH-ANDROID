package com.iavariav.kkntambakajibanksampah.model;

import com.google.gson.annotations.SerializedName;

public class StokBarangModel {

    @SerializedName("tipe_barang")
    private String tipeBarang;

    @SerializedName("id_barang")
    private String idBarang;

    @SerializedName("kadaluarsa_barang")
    private String kadaluarsaBarang;

    @SerializedName("reg_barang")
    private String regBarang;

    @SerializedName("foto_url_barang")
    private String fotoUrlBarang;

    @SerializedName("nama_barang")
    private String namaBarang;

    @SerializedName("status_barang")
    private String statusBarang;

    @SerializedName("deskripsi_barang")
    private String deskripsiBarang;

    @SerializedName("point_barang")
    private String pointBarang;

    @SerializedName("nama_pemasok")
    private String namaPemasok;

    public void setTipeBarang(String tipeBarang) {
        this.tipeBarang = tipeBarang;
    }

    public String getTipeBarang() {
        return tipeBarang;
    }

    public void setIdBarang(String idBarang) {
        this.idBarang = idBarang;
    }

    public String getIdBarang() {
        return idBarang;
    }

    public void setKadaluarsaBarang(String kadaluarsaBarang) {
        this.kadaluarsaBarang = kadaluarsaBarang;
    }

    public String getKadaluarsaBarang() {
        return kadaluarsaBarang;
    }

    public void setRegBarang(String regBarang) {
        this.regBarang = regBarang;
    }

    public String getRegBarang() {
        return regBarang;
    }

    public void setFotoUrlBarang(String fotoUrlBarang) {
        this.fotoUrlBarang = fotoUrlBarang;
    }

    public String getFotoUrlBarang() {
        return fotoUrlBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setStatusBarang(String statusBarang) {
        this.statusBarang = statusBarang;
    }

    public String getStatusBarang() {
        return statusBarang;
    }

    public void setDeskripsiBarang(String deskripsiBarang) {
        this.deskripsiBarang = deskripsiBarang;
    }

    public String getDeskripsiBarang() {
        return deskripsiBarang;
    }

    public void setPointBarang(String pointBarang) {
        this.pointBarang = pointBarang;
    }

    public String getPointBarang() {
        return pointBarang;
    }

    public void setNamaPemasok(String namaPemasok) {
        this.namaPemasok = namaPemasok;
    }

    public String getNamaPemasok() {
        return namaPemasok;
    }
}