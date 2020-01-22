package com.iavariav.kkntambakajibanksampah.ui.petugas;


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
import com.iavariav.kkntambakajibanksampah.adapter.AproveTukarBarangAdapter;
import com.iavariav.kkntambakajibanksampah.helper.Config;
import com.iavariav.kkntambakajibanksampah.model.StatusSampahModel;
import com.iavariav.kkntambakajibanksampah.model.StatusSampahModel;
import com.iavariav.kkntambakajibanksampah.rest.ApiService;
import com.iavariav.kkntambakajibanksampah.rest.Client;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AproveFragment extends Fragment {


    private RecyclerView rv;
    private ArrayList<StatusSampahModel> statusSampahModels;
    private String idUser;
    public AproveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_aprove, container, false);
        initView(view);
        statusSampahModels = new ArrayList<>();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        idUser = sharedPreferences.getString(Config.SHARED_PREF_ID, "");
        getAprove();
        return view;
    }

    private void getAprove() {
        ApiService apiService = Client.getInstanceRetrofit();
        apiService.getStatusBarang("getStatusSampah", idUser)
                .enqueue(new Callback<ArrayList<StatusSampahModel>>() {
                    @Override
                    public void onResponse(Call<ArrayList<StatusSampahModel>> call, Response<ArrayList<StatusSampahModel>> response) {
                        if (response.isSuccessful()){
                            statusSampahModels = response.body();
                            AproveTukarBarangAdapter aproveTukarBarangAdapter = new AproveTukarBarangAdapter(getActivity(), statusSampahModels);
                            rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                            rv.setAdapter(aproveTukarBarangAdapter);
                            aproveTukarBarangAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<StatusSampahModel>> call, Throwable t) {
                        Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initView(View view) {
        rv = view.findViewById(R.id.rv);
    }
}
