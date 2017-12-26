package com.mycillin.partner.modul.todo.completion;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mvc.imagepicker.ImagePicker;
import com.mycillin.partner.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CompletePrescriptionsFragment extends Fragment {

    @BindView(R.id.accountDetailActivity_iv_prescription)
    AppCompatImageView prescriptionImage;

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
        return rootView;
    }

    @OnClick(R.id.accountDetailActivity_iv_prescription)
    public void onPrescriptionClicked() {
        ImagePicker.pickImage(getActivity(), "Select Image From :");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (resultCode != Activity.RESULT_CANCELED) {
            switch (requestCode) {
                case 234:
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        Bitmap bmp = ImagePicker.getImageFromResult(getContext(), requestCode, resultCode, data);
                        if (bmp != null) {
                            prescriptionImage.setImageBitmap(bmp);
                        }
                    }
                    break;
            }
        }
    }
}
