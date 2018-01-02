package com.mycillin.partner.modul.todo.completion;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mvc.imagepicker.ImagePicker;
import com.mycillin.partner.R;
import com.mycillin.partner.modul.searchResult.SearchResultActivity;
import com.mycillin.partner.modul.searchResult.adapterList.SearchResultList;
import com.mycillin.partner.modul.todo.completion.adapterList.ModelRestPrescriptionType;
import com.mycillin.partner.modul.todo.completion.adapterList.ModelRestPrescriptionTypeResultData;
import com.mycillin.partner.util.DialogHelper;
import com.mycillin.partner.util.PartnerAPI;
import com.mycillin.partner.util.ProgressBarHandler;
import com.mycillin.partner.util.RestClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class CompletePrescriptionsFragment extends Fragment {
    public static final int REQUEST_CODE_GET_PRESCRIPTION = 4000;
    @BindView(R.id.accountDetailActivity_iv_prescription)
    AppCompatImageView prescriptionImage;

    @BindView(R.id.accountDetailActivity_et_prescriptionType)
    EditText edtxPrescriptionType;

    private PartnerAPI partnerAPI;
    private Handler mHandler;
    private ProgressBarHandler mProgressBarHandler;

    private List<SearchResultList> searchResultList = new ArrayList<>();

    public CompletePrescriptionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_complete_prescriptions, container, false);
        ButterKnife.bind(this, rootView);
        partnerAPI = RestClient.getPartnerRestInterfaceNoToken();
        mHandler = new Handler(Looper.getMainLooper());
        mProgressBarHandler = new ProgressBarHandler(getActivity());
        return rootView;
    }

    @OnClick(R.id.accountDetailActivity_iv_prescription)
    public void onPrescriptionClicked() {
        ImagePicker.pickImage(CompletePrescriptionsFragment.this, "Select Image From :");
    }

    @OnClick(R.id.accountDetailActivity_et_prescriptionType)
    public void onPrescriptionTypeClicked() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mProgressBarHandler.show();
            }
        });
        partnerAPI.getPrescriptionType().enqueue(new Callback<ModelRestPrescriptionType>() {
            @Override
            public void onResponse(@NonNull Call<ModelRestPrescriptionType> call, @NonNull Response<ModelRestPrescriptionType> response) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler.hide();
                    }
                });
                if (response.isSuccessful()) {
                    ModelRestPrescriptionType modelRestPrescriptionType = response.body();
                    assert modelRestPrescriptionType != null;
                    for (ModelRestPrescriptionTypeResultData modelRestPrescriptionTypeResultData : modelRestPrescriptionType.getResult().getData()) {
                        searchResultList.add(new SearchResultList("Code", modelRestPrescriptionTypeResultData.getPrescriptionTypeId(), "Prescription Type", modelRestPrescriptionTypeResultData.getPrescriptionTypeDesc(),
                                "", "", "", "", "", "", "", ""));
                    }
                    Intent intent = new Intent(getContext(), SearchResultActivity.class);
                    intent.putParcelableArrayListExtra(SearchResultActivity.EXTRA_SEARCH_DATA, (ArrayList<? extends Parcelable>) searchResultList);
                    intent.putExtra(SearchResultActivity.EXTRA_SEARCH_REQUEST_CODE, REQUEST_CODE_GET_PRESCRIPTION);
                    startActivityForResult(intent, REQUEST_CODE_GET_PRESCRIPTION);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelRestPrescriptionType> call, @NonNull Throwable t) {
                final String message = t.getMessage();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler.hide();
                        DialogHelper.showDialog(mHandler, getActivity(), "Error", "Connection problem " + message, false);
                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Timber.tag("#8#8#").d("onActivityResultResep: %s", requestCode + resultCode);
        String item1 = "";
        String item2 = "";
        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode != 234) {
                item1 = data.getStringExtra("ITEM_1");
                item2 = data.getStringExtra("ITEM_2");
            }
            switch (requestCode) {
                case 234:
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        Bitmap bmp = ImagePicker.getImageFromResult(getContext(), requestCode, resultCode, data);
                        if (bmp != null) {
                            prescriptionImage.setImageBitmap(bmp);
                        }
                    }
                    break;
                case REQUEST_CODE_GET_PRESCRIPTION:
                    edtxPrescriptionType.setText(getString(R.string.itemConcat, item1, item2));
            }
        }
    }
}
