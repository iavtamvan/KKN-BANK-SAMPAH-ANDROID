package com.iavariav.kkntambakajibanksampah.ui.user.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iavariav.kkntambakajibanksampah.R;
import com.iavariav.kkntambakajibanksampah.adapter.TukarBarangAdapter;
import com.iavariav.kkntambakajibanksampah.model.StokBarangModel;
import com.iavariav.kkntambakajibanksampah.rest.ApiService;
import com.iavariav.kkntambakajibanksampah.rest.Client;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TukarSampahFragment extends Fragment {
    private RecyclerView rv;
    private ArrayList<StokBarangModel> stokBarangModels;

    public TukarSampahFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tukar_sampah, container, false);
        initView(view);

        stokBarangModels = new ArrayList<>();

        getTukarSampah();
        return view;
    }

    private void getTukarSampah() {
        ApiService apiService = Client.getInstanceRetrofit();
        apiService.getStokBarang("getStokBarang")
                .enqueue(new Callback<ArrayList<StokBarangModel>>() {
                    @Override
                    public void onResponse(Call<ArrayList<StokBarangModel>> call, Response<ArrayList<StokBarangModel>> response) {
                        if (response.isSuccessful()){
                            stokBarangModels = response.body();
                            for (int i = 0; i < stokBarangModels.size(); i++) {
                                TukarBarangAdapter tukarBarangAdapter = new TukarBarangAdapter(getActivity(), stokBarangModels);
                                rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                                rv.setAdapter(tukarBarangAdapter);
                                tukarBarangAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<StokBarangModel>> call, Throwable t) {
                        Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void initView(View view) {
        rv = view.findViewById(R.id.rv);
    }
}
