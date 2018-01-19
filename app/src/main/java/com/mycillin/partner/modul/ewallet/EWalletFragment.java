package com.mycillin.partner.modul.ewallet;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mycillin.partner.R;
import com.mycillin.partner.modul.ewallet.adapterList.EWalletHistoryAdapter;
import com.mycillin.partner.modul.ewallet.adapterList.EWalletHistoryList;
import com.mycillin.partner.util.Configs;
import com.mycillin.partner.util.DialogHelper;
import com.mycillin.partner.util.ProgressBarHandler;
import com.mycillin.partner.util.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import timber.log.Timber;

public class EWalletFragment extends Fragment {

    @BindView(R.id.eWalletFragment_rv_recyclerView)
    RecyclerView eWalletHistoryRecyclerView;
    @BindView(R.id.eWalletFragment_tv_amount)
    TextView tvAmount;
    @BindView(R.id.eWalletFragment_tv_accountNumberTxt)
    TextView tvAccountNUmber;

    private List<EWalletHistoryList> eWalletHistoryLists = new ArrayList<>();
    private EWalletHistoryAdapter eWalletHistoryAdapter;
    private Handler mHandler;
    private ProgressBarHandler mProgressBarHandler;
    private SessionManager sessionManager;

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
        mHandler = new Handler(Looper.getMainLooper());
        mProgressBarHandler = new ProgressBarHandler(getActivity());
        sessionManager = new SessionManager(getContext());
        getActivity().setTitle(R.string.nav_e_wallet);

        getAmount();
        getEWalletHistoryList();
        tvAccountNUmber.setText(sessionManager.getUserId());

        return rootView;
    }

    private void getAmount() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mProgressBarHandler.show();
            }
        });
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        Map<String, Object> data = new HashMap<>();
        data.put("user_id", sessionManager.getUserId());

        JSONObject jsonObject = new JSONObject(data);

        Timber.tag("####").d("Detail Amount %s", jsonObject);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Configs.URL_REST_CLIENT + "partner_check_balance/")
                .post(body)
                .addHeader("content-type", "application/json; charset=utf-8")
                .addHeader("Authorization", sessionManager.getUserToken())
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler.hide();
                    }
                });
                DialogHelper.showDialog(mHandler, getActivity(), "Warning", "Please Try Again : " + e.getMessage(), false);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull final okhttp3.Response response) throws IOException {
                final String result = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler.hide();
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                Timber.tag("####").d("Detail saldo %s", jsonObject);
                                boolean status = jsonObject.getJSONObject("result").getBoolean("status");
                                if (status) {
                                    JSONArray result = jsonObject.getJSONObject("result").getJSONArray("data");
                                    final JSONObject data = result.getJSONObject(0);
                                    final String sisaSaldo = data.optString("sisa_saldo");

                                    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                                    String v_formattedValue = numberFormat.format(Double.parseDouble(sisaSaldo.isEmpty() ? "0" : sisaSaldo));
                                    tvAmount.setText(v_formattedValue.replace("Rp", ""));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }

    private void getEWalletHistoryList() {
        eWalletHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        eWalletHistoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        eWalletHistoryLists.clear();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Map<String, Object> data = new HashMap<>();
        data.put("user_id", sessionManager.getUserId());

        JSONObject jsonObject = new JSONObject(data);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Configs.URL_REST_CLIENT + "partner_check_transaction/")
                .post(body)
                .addHeader("content-type", "application/json; charset=utf-8")
                .addHeader("Authorization", sessionManager.getUserToken())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler.hide();
                    }
                });
                DialogHelper.showDialog(mHandler, getActivity(), "Warning", "Connection Problem, Please Try Again Later." + e, false);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler.hide();
                    }
                });
                final String result = response.body().string();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        Timber.tag("###").d("onResponse: %s", jsonObject);
                        boolean status = jsonObject.getJSONObject("result").getBoolean("status");
                        if (status) {
                            JSONArray data = jsonObject.getJSONObject("result").getJSONArray("data");
                            Timber.tag("###").d("onResponse2: %s", data);
                            for (int i = 0; i < data.length(); i++) {
                                final String transactionDate = data.getJSONObject(i).optString("transaction_date").trim().split(" ")[0];
                                final String transactionID = data.getJSONObject(i).optString("transaction_id").trim();
                                final String transactionTipeId = data.getJSONObject(i).optString("transaction_type_id").trim();
                                final String refNo = data.getJSONObject(i).optString("ref_no").trim();
                                final String amount = data.getJSONObject(i).optString("amount").trim();

                                boolean isTopUp;
                                switch (transactionTipeId) {
                                    case "TOP UP":
                                        isTopUp = true;
                                        break;
                                    default:
                                        isTopUp = false;
                                        break;
                                }

                                NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                                final String formatAmount = numberFormat.format(Double.parseDouble(amount.isEmpty() ? "0" : amount));
                                SimpleDateFormat dateParse = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy", Locale.US);
                                Date tglTrx_ = null;
                                if (!transactionDate.isEmpty()) {
                                    tglTrx_ = dateParse.parse(transactionDate);
                                }

                                final boolean finalIsTopUp = isTopUp;
                                final String finalDateTrx = tglTrx_ != null ? dateFormatter.format(tglTrx_) : "";
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        eWalletHistoryLists.add(new EWalletHistoryList("Apotek Antar", formatAmount, finalDateTrx, finalIsTopUp));
                                        eWalletHistoryAdapter = new EWalletHistoryAdapter(eWalletHistoryLists);
                                        eWalletHistoryRecyclerView.setAdapter(eWalletHistoryAdapter);
                                        eWalletHistoryAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }
                    } catch (JSONException | ParseException e) {
                        Timber.tag("###").d("onResponseror: %s", e.getMessage());
                    }
                }
            }
        });
    }
}
