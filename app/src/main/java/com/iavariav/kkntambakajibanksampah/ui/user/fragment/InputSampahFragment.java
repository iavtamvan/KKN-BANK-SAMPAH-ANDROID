package com.iavariav.kkntambakajibanksampah.ui.user.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.iavariav.kkntambakajibanksampah.R;
import com.iavariav.kkntambakajibanksampah.helper.Config;
import com.iavariav.kkntambakajibanksampah.rest.ApiService;
import com.iavariav.kkntambakajibanksampah.rest.Client;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

import im.delight.android.location.SimpleLocation;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class InputSampahFragment extends Fragment {

    private String[] list = {"-Pilih-", "Plastik", "Kertas", "Besi", "Kayu"};
    private EditText edtBeratSampah;
    private Button btnKirim;

    private String selectedSpn;
    private FragmentManager fragmentManager;
    private MaterialSpinner spnJenis;
    private int beratPoin;

    private String username;
    private String rule;
    private String id;
    private String alamat_user;
    private String lat_user;
    private String long_user;
    private String firebase_id;
    private String namaLengkap;
    private String regIdUser;

    private SimpleLocation location;

    public InputSampahFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_input_sampah, container, false);
        initView(view);
        location = new SimpleLocation(Objects.requireNonNull(getActivity()));// if we can't access the location yet
        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(getActivity());
        }

        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(Config.SHARED_PREF_USERNAME, "");
        rule = sharedPreferences.getString(Config.SHARED_PREF_RULE, "");
        id = sharedPreferences.getString(Config.SHARED_PREF_ID, "");
        alamat_user = sharedPreferences.getString(Config.SHARED_PREF_ALAMAT_USER, "");
        lat_user = sharedPreferences.getString(Config.SHARED_PREF_LAT_USER, "");
        long_user = sharedPreferences.getString(Config.SHARED_PREF_LONG_USER, "");
        firebase_id = sharedPreferences.getString(Config.SHARED_PREF_FIREBASE_ID, "");
        namaLengkap = sharedPreferences.getString(Config.SHARED_PREF_NAMA_LENGKAP, "");
        regIdUser = sharedPreferences.getString(Config.SHARED_PREF_REG_ID, "");

        Random r = new Random();
        final int i1 = (r.nextInt(80) + 65);

        spnJenis.setItems(list);
        spnJenis.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                selectedSpn = item.toString();
                Toast.makeText(getActivity(), "" + item, Toast.LENGTH_SHORT).show();

            }
        });

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                beratPoin = 10 * Integer.parseInt(edtBeratSampah.getText().toString().trim());
                ApiService apiService = Client.getInstanceRetrofit();
                apiService.postPemesanan(id, namaLengkap, alamat_user, location.getLatitude(), location.getLongitude(), selectedSpn,
                        "Ordered", "KKN-2020-TAMBAKAJI-" + i1 + location.getLongitude() + lat_user + i1, sharedPreferences.getString("regId", ""), regIdUser)
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response.body().string());
                                        String error_msg = jsonObject.optString("error_msg");
                                        if (error_msg.equalsIgnoreCase("Berhasil")) {
//                                            fragmentManager = getActivity().getSupportFragmentManager();
//                                            fragmentManager.beginTransaction().replace(R.id.fm_view_pager_nav, new PemesananMobilFragment()).commit();
//                                            getActivity().getSupportActionBar().setTitle("Data KBM-ONLINE");
                                            Toast.makeText(getActivity(), "" + error_msg, Toast.LENGTH_SHORT).show();
                                            edtBeratSampah.setText("");
                                        } else {
                                            Toast.makeText(getActivity(), "" + error_msg, Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });


        return view;
    }

    private void initView(View view) {
        edtBeratSampah = view.findViewById(R.id.edt_berat_sampah);
        btnKirim = view.findViewById(R.id.btn_kirim);
        spnJenis = view.findViewById(R.id.spn_jenis);
    }
}
