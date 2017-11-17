package com.mycillin.partner.modul.ewallet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mycillin.partner.R;
import com.mycillin.partner.modul.ewallet.adapterList.EWalletHistoryAdapter;
import com.mycillin.partner.modul.ewallet.adapterList.EWalletHistoryList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EWalletFragment extends Fragment {

    @BindView(R.id.eWalletFragment_rv_recyclerView)
    RecyclerView eWalletHistoryRecyclerView;

    private List<EWalletHistoryList> eWalletHistoryLists = new ArrayList<>();
    private EWalletHistoryAdapter eWalletHistoryAdapter;

    public EWalletFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ewallet, container, false);
        ButterKnife.bind(this, rootView);

        getActivity().setTitle(R.string.nav_e_wallet);

        getEWalletHistoryList();

        return rootView;
    }

    private void getEWalletHistoryList() {
        eWalletHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        eWalletHistoryRecyclerView.setItemAnimator(new DefaultItemAnimator());

        eWalletHistoryLists.clear();
        eWalletHistoryLists.add(new EWalletHistoryList("dr Andi Husada", "Rp 175.000", "25 Maret 2017", false));
        eWalletHistoryLists.add(new EWalletHistoryList("Top Up Mandiri", "Rp 700.000", "02 April 2017", true));
        eWalletHistoryLists.add(new EWalletHistoryList("dr Budi Sukma", "Rp 175.000", "14 Juni 2017", false));
        eWalletHistoryLists.add(new EWalletHistoryList("Apotek Antar", "Rp 250.000", "14 Juni 2017", false));

        eWalletHistoryAdapter = new EWalletHistoryAdapter(eWalletHistoryLists);
        eWalletHistoryRecyclerView.setAdapter(eWalletHistoryAdapter);
        eWalletHistoryAdapter.notifyDataSetChanged();
    }
}
