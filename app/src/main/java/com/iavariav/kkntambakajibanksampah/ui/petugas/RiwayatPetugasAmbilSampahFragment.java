package com.iavariav.kkntambakajibanksampah.ui.petugas;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iavariav.kkntambakajibanksampah.R;
import com.iavariav.kkntambakajibanksampah.adapter.AproveSampahAdapter;
import com.iavariav.kkntambakajibanksampah.adapter.RiwayatAproveSampahAdapter;
import com.iavariav.kkntambakajibanksampah.adapter.RiwayatTukarBarangAdapter;
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
public class RiwayatPetugasAmbilSampahFragment extends Fragment {
    private RecyclerView rv;
    private ArrayList<StatusSampahModel> statusSampahModels;

    public RiwayatPetugasAmbilSampahFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_riwayat_petugas_ambil_sampah, container, false);
        initView(view);
        statusSampahModels = new ArrayList<>();
        getRiwayatAmbil();
        return view;
    }

    private void getRiwayatAmbil() {
        ApiService apiService = Client.getInstanceRetrofit();
        apiService.getStatusBarangSelesai("getStatusSampahSelesai")
                .enqueue(new Callback<ArrayList<StatusSampahModel>>() {
                    @Override
                    public void onResponse(Call<ArrayList<StatusSampahModel>> call, Response<ArrayList<StatusSampahModel>> response) {
                        if (response.isSuccessful()){
                            statusSampahModels = response.body();
                            RiwayatAproveSampahAdapter aproveSampahAdapter = new RiwayatAproveSampahAdapter(getActivity(), statusSampahModels);
                            rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                            rv.setAdapter(aproveSampahAdapter);
                            aproveSampahAdapter.notifyDataSetChanged();
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
