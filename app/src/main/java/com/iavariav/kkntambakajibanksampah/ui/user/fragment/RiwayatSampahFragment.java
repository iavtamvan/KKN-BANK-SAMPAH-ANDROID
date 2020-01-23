package com.iavariav.kkntambakajibanksampah.ui.user.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iavariav.kkntambakajibanksampah.R;
import com.iavariav.kkntambakajibanksampah.adapter.RiwayatSampahAdapter;
import com.iavariav.kkntambakajibanksampah.helper.Config;
import com.iavariav.kkntambakajibanksampah.model.RiwayatModel;
import com.iavariav.kkntambakajibanksampah.rest.ApiService;
import com.iavariav.kkntambakajibanksampah.rest.Client;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RiwayatSampahFragment extends Fragment {
    private RecyclerView rv;
    private ArrayList<RiwayatModel> riwayatModels;
    private String namaLengkap;

    public RiwayatSampahFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_riwayat_sampah, container, false);
        initView(view);
        riwayatModels = new ArrayList<>();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        namaLengkap = sharedPreferences.getString(Config.SHARED_PREF_NAMA_LENGKAP, "");
        getRiwayatUser();

        return view;
    }


    private void getRiwayatUser() {
        ApiService apiService = Client.getInstanceRetrofit();
        apiService.getRiwayat("getHistoriUser", namaLengkap)
                .enqueue(new Callback<ArrayList<RiwayatModel>>() {
                    @Override
                    public void onResponse(Call<ArrayList<RiwayatModel>> call, Response<ArrayList<RiwayatModel>> response) {
                        if (response.isSuccessful()){
                            riwayatModels = response.body();
                            for (int i = 0; i < riwayatModels.size(); i++) {
                                RiwayatSampahAdapter riwayatSampahAdapter = new RiwayatSampahAdapter(getActivity(), riwayatModels);
                                rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                                rv.setAdapter(riwayatSampahAdapter);
                                riwayatSampahAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<RiwayatModel>> call, Throwable t) {
                        Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void initView(View view) {
        rv = view.findViewById(R.id.rv);
    }
}
