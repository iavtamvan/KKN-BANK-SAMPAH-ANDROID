package com.iavariav.kkntambakajibanksampah.ui.user;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.iavariav.kkntambakajibanksampah.R;
import com.iavariav.kkntambakajibanksampah.model.EdukasiModel;
import com.iavariav.kkntambakajibanksampah.rest.ApiService;
import com.iavariav.kkntambakajibanksampah.rest.Client;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailEdukasiActivity extends AppCompatActivity {

    private SliderLayout mSliderSlider;
    private PagerIndicator customIndicator;
    private PagerIndicator customIndicator2;
    private TextView tvJudulEdukasi;
    private TextView tvTanggalPlusAdminEdukasi;
    private TextView tvDeskripsiEdukasi;
    private TextView tvKeteranganLainnyaEdukasi;

    private String judul;
    private String tanggal;
    private String deskripsi;
    private String ket_lain;
    private String backdrop1;

    private ArrayList<EdukasiModel> edukasiModels;
    private ImageView ivBackdrop1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_edukasi);
        getSupportActionBar().hide();
        initView();
        edukasiModels = new ArrayList<>();
        judul = getIntent().getStringExtra("BUNDLE_JUDUL");
        tanggal = getIntent().getStringExtra("BUNDLE_TANGGAL");
        deskripsi = getIntent().getStringExtra("BUNDLE_DESKRIPSI");
        ket_lain = getIntent().getStringExtra("BUNDLE_KET_LAIN");
        backdrop1 = getIntent().getStringExtra("BUNDLE_BACKDROP_1");

        tvJudulEdukasi.setText(judul);
        tvTanggalPlusAdminEdukasi.setText(tanggal);
        tvDeskripsiEdukasi.setText(deskripsi);
        tvKeteranganLainnyaEdukasi.setText(ket_lain);
        Glide.with(DetailEdukasiActivity.this).load(backdrop1).into(ivBackdrop1);
//        getSlider();
    }

    private void getSlider() {
        ApiService apiService = Client.getInstanceRetrofit();
        apiService.getEdukasi("getEvent").enqueue(new Callback<ArrayList<EdukasiModel>>() {
            @Override
            public void onResponse(Call<ArrayList<EdukasiModel>> call, Response<ArrayList<EdukasiModel>> response) {
                if (response.isSuccessful()) {
                    edukasiModels = response.body();
                    for (int i = 0; i < edukasiModels.size(); i++) {
                        mSliderSlider.setVisibility(View.VISIBLE);
                        customIndicator.setVisibility(View.VISIBLE);
                        customIndicator2.setVisibility(View.VISIBLE);
                        HashMap<String, String> url_maps = new HashMap<String, String>();
                        // * Get internet
                        url_maps.put(edukasiModels.get(i).getJudulBerita(), edukasiModels.get(i).getGambar1Berita());
                        url_maps.put(edukasiModels.get(i).getJudulBerita(), edukasiModels.get(i).getGambar2Berita());
                        url_maps.put(edukasiModels.get(i).getJudulBerita(), edukasiModels.get(i).getGambar3Berita());
                        url_maps.put(edukasiModels.get(i).getJudulBerita(), edukasiModels.get(i).getGambar4Berita());


                        for (String name : url_maps.keySet()) {
                            TextSliderView textSliderView = new TextSliderView(DetailEdukasiActivity.this);
                            // initialize a SliderLayout
                            textSliderView
                                    .description(name)
                                    .image(url_maps.get(name))
                                    .setScaleType(BaseSliderView.ScaleType.Fit);
                            //add your extra information
                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle()
                                    .putString("extra", name);

                            mSliderSlider.addSlider(textSliderView);
                        }
                        mSliderSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                        mSliderSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        mSliderSlider.setCustomAnimation(new DescriptionAnimation());
                        mSliderSlider.setDuration(4000);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<EdukasiModel>> call, Throwable t) {
                Toast.makeText(DetailEdukasiActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        mSliderSlider = findViewById(R.id.mSliderSlider);
        customIndicator = findViewById(R.id.custom_indicator);
        customIndicator2 = findViewById(R.id.custom_indicator2);
        tvJudulEdukasi = findViewById(R.id.tv_judul_edukasi);
        tvTanggalPlusAdminEdukasi = findViewById(R.id.tv_tanggal_plus_admin_edukasi);
        tvDeskripsiEdukasi = findViewById(R.id.tv_deskripsi_edukasi);
        tvKeteranganLainnyaEdukasi = findViewById(R.id.tv_keterangan_lainnya_edukasi);
        ivBackdrop1 = findViewById(R.id.iv_backdrop_1);
    }
}
