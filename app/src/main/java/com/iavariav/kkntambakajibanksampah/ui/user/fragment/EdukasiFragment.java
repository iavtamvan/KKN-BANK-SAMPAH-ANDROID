package com.iavariav.kkntambakajibanksampah.ui.user.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.iavariav.kkntambakajibanksampah.R;
import com.iavariav.kkntambakajibanksampah.adapter.EdukasiAdapter;
import com.iavariav.kkntambakajibanksampah.model.EdukasiModel;
import com.iavariav.kkntambakajibanksampah.rest.ApiService;
import com.iavariav.kkntambakajibanksampah.rest.Client;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class EdukasiFragment extends Fragment {
    private ArrayList<EdukasiModel> edukasiModels;
    private EdukasiAdapter edukasiAdapter;
    private RecyclerView rv;

    public EdukasiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edukasi, container, false);
        initView(view);
        edukasiModels = new ArrayList<>();
        getDataEdukasi();

        return view;
    }

    private void getDataEdukasi() {
        ApiService apiService = Client.getInstanceRetrofit();
        apiService.getEdukasi("getEvent").enqueue(new Callback<ArrayList<EdukasiModel>>() {
            @Override
            public void onResponse(Call<ArrayList<EdukasiModel>> call, Response<ArrayList<EdukasiModel>> response) {
                if (response.isSuccessful()) {
                    edukasiModels = response.body();
                    for (int i = 0; i < edukasiModels.size(); i++) {
                        edukasiAdapter = new EdukasiAdapter(getActivity(), edukasiModels);
                        rv.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL));
                        rv.setAdapter(edukasiAdapter);
                        edukasiAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<EdukasiModel>> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView(View view) {
        rv = view.findViewById(R.id.rv);
    }
}
