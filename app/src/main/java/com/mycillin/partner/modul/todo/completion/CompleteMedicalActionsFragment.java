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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mvc.imagepicker.ImagePicker;
import com.mycillin.partner.R;
import com.mycillin.partner.modul.searchResult.SearchResultActivity;
import com.mycillin.partner.modul.searchResult.adapterList.SearchResultList;
import com.mycillin.partner.modul.todo.completion.adapterList.ModelRestMedicalAction;
import com.mycillin.partner.modul.todo.completion.adapterList.ModelRestMedicalActionResultData;
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

public class CompleteMedicalActionsFragment extends Fragment {

    public static final int REQUEST_CODE_GET_MEDICAL_ACTION = 3000;

    @BindView(R.id.completeMedicalActionsFragment_et_medicalActionOne)
    EditText edtxMedicalAction;

    private PartnerAPI partnerAPI;
    private Handler mHandler;
    private ProgressBarHandler mProgressBarHandler;

    private List<SearchResultList> searchResultList = new ArrayList<>();

    public CompleteMedicalActionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_complete_medical_actions, container, false);
        ButterKnife.bind(this, rootView);
        partnerAPI = RestClient.getPartnerRestInterfaceNoToken();
        mHandler = new Handler(Looper.getMainLooper());
        mProgressBarHandler = new ProgressBarHandler(getActivity());
        return rootView;
    }

    @OnClick(R.id.completeMedicalActionsFragment_et_medicalActionOne)
    public void onMedicalActionClicked() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mProgressBarHandler.show();
            }
        });
        partnerAPI.getMedicalAction().enqueue(new Callback<ModelRestMedicalAction>() {
            @Override
            public void onResponse(@NonNull Call<ModelRestMedicalAction> call, @NonNull Response<ModelRestMedicalAction> response) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler.hide();
                    }
                });
                if (response.isSuccessful()) {
                    ModelRestMedicalAction modelRestMedicalAction = response.body();
                    assert modelRestMedicalAction != null;
                    for (ModelRestMedicalActionResultData modelRestMedicalActionResultData : modelRestMedicalAction.getResult().getData()) {
                        searchResultList.add(new SearchResultList("Code", modelRestMedicalActionResultData.getActionTypeId(), "Medical Action", modelRestMedicalActionResultData.getActionTypeDesc(),
                                "", "", "", "", "", "", "", ""));
                    }
                    Intent intent = new Intent(getContext(), SearchResultActivity.class);
                    intent.putParcelableArrayListExtra(SearchResultActivity.EXTRA_SEARCH_DATA, (ArrayList<? extends Parcelable>) searchResultList);
                    intent.putExtra(SearchResultActivity.EXTRA_SEARCH_REQUEST_CODE, REQUEST_CODE_GET_MEDICAL_ACTION);
                    startActivityForResult(intent, REQUEST_CODE_GET_MEDICAL_ACTION);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelRestMedicalAction> call, @NonNull Throwable t) {
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
        Timber.tag("#8#8#").d("onActivityResult: %s", requestCode + resultCode);
        String item1 = "";
        String item2 = "";
        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode != 234) {
                item1 = data.getStringExtra("ITEM_1");
                item2 = data.getStringExtra("ITEM_2");
            }
            switch (requestCode) {
                case REQUEST_CODE_GET_MEDICAL_ACTION:
                    edtxMedicalAction.setText(getString(R.string.itemConcat, item1, item2));
            }
        }
    }
}
