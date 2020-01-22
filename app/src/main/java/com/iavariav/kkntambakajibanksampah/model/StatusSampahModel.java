package com.iavariav.kkntambakajibanksampah.model;

import com.google.gson.annotations.SerializedName;

public class StatusSampahModel {

    @SerializedName("id_bs")
    private String idBs;

    @SerializedName("nama_petugas")
    private Object namaPetugas;

    @SerializedName("status_sampah")
    private String statusSampah;

    @SerializedName("id_user")
    private String idUser;

    @SerializedName("longi")
    private String longi;

    @SerializedName("berat_sampah")
    private String beratSampah;

    @SerializedName("point")
    private String point;

    @SerializedName("tgl_input")
    private String tglInput;

    @SerializedName("nama_penyetor")
    private String namaPenyetor;

    @SerializedName("jenis_sampah")
    private String jenisSampah;

    @SerializedName("token_reg")
    private String tokenReg;

    @SerializedName("lat")
    private String lat;

    @SerializedName("alamat_penyetor")
    private String alamatPenyetor;

    public void setIdBs(String idBs) {
        this.idBs = idBs;
    }

    public String getIdBs() {
        return idBs;
    }

    public void setNamaPetugas(Object namaPetugas) {
        this.namaPetugas = namaPetugas;
    }

    public Object getNamaPetugas() {
        return namaPetugas;
    }

    public void setStatusSampah(String statusSampah) {
        this.statusSampah = statusSampah;
    }

    public String getStatusSampah() {
        return statusSampah;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public String getLongi() {
        return longi;
    }

    public void setBeratSampah(String beratSampah) {
        this.beratSampah = beratSampah;
    }

    public String getBeratSampah() {
        return beratSampah;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getPoint() {
        return point;
    }

    public void setTglInput(String tglInput) {
        this.tglInput = tglInput;
    }

    public String getTglInput() {
        return tglInput;
    }

    public void setNamaPenyetor(String namaPenyetor) {
        this.namaPenyetor = namaPenyetor;
    }

    public String getNamaPenyetor() {
        return namaPenyetor;
    }

    public void setJenisSampah(String jenisSampah) {
        this.jenisSampah = jenisSampah;
    }

    public String getJenisSampah() {
        return jenisSampah;
    }

    public void setTokenReg(String tokenReg) {
        this.tokenReg = tokenReg;
    }

    public String getTokenReg() {
        return tokenReg;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLat() {
        return lat;
    }

    public void setAlamatPenyetor(String alamatPenyetor) {
        this.alamatPenyetor = alamatPenyetor;
    }

    public String getAlamatPenyetor() {
        return alamatPenyetor;
    }
}