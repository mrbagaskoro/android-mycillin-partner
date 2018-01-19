package com.mycillin.partner.modul.chat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.mycillin.partner.R;
import com.mycillin.partner.util.Configs;
import com.mycillin.partner.util.DialogHelper;
import com.mycillin.partner.util.SessionManager;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {

    public static String KEY_FLAG_CHAT_PATIENT_ID = "CHAT_PATIENT_ID";
    public static String KEY_FLAG_CHAT_PATIENT_NAME = "CHAT_PATIENT_NAME";
    public static String KEY_FLAG_CHAT_USER_ID = "CHAT_USER_ID";
    public static String KEY_FLAG_CHAT_USER_NAME = "CHAT_USER_NAME";
    public static String KEY_FLAG_CHAT_BOOKING_ID = "CHAT_BOOKING_ID";

    @BindView(R.id.chat_ll_reference1)
    LinearLayout layoutRef1;
    @BindView(R.id.chat_rl_reference2)
    RelativeLayout layoutRef2;
    @BindView(R.id.chat_IV_sendButton)
    ImageView ivSend;
    @BindView(R.id.chat_ET_messageArea)
    EditText etMessageArea;
    @BindView(R.id.chat_scrollView)
    ScrollView svScroll;
    @BindView(R.id.chat_btn_confirm)
    Button confirmBtn;

    private String patientID;
    private String patientName;
    private String doctorID;
    private String doctorName;
    private String bookingID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        patientID = getIntent().getStringExtra(KEY_FLAG_CHAT_PATIENT_ID);
        patientName = getIntent().getStringExtra(KEY_FLAG_CHAT_PATIENT_NAME);
        doctorID = getIntent().getStringExtra(KEY_FLAG_CHAT_USER_ID);
        doctorName = getIntent().getStringExtra(KEY_FLAG_CHAT_USER_NAME);
        bookingID = getIntent().getStringExtra(KEY_FLAG_CHAT_BOOKING_ID);

        Log.d("#8#8#", "onCreate: " + patientID + " # " + patientName + " # " + doctorID + " # " + doctorName);

        final DatabaseReference user1 = FirebaseDatabase.getInstance().getReferenceFromUrl("https://android-mycillin-1507307522195.firebaseio.com/messages/" + patientID + "_" + doctorID + "");
        final DatabaseReference user2 = FirebaseDatabase.getInstance().getReferenceFromUrl("https://android-mycillin-1507307522195.firebaseio.com/messages/" + doctorID + "_" + patientID + "");

        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = etMessageArea.getText().toString();
                if (!messageText.equals("")) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", doctorName);
                    user1.push().setValue(map);
                    user2.push().setValue(map);
                    etMessageArea.setText("");
                }
            }
        });

        user1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {
                };
                Map<String, String> map = dataSnapshot.getValue(genericTypeIndicator);
                assert map != null;
                String message = map.get("message");
                String userName = map.get("user");

                if (userName.equals(doctorName)) {
                    addMessageBox("You:-\n" + message, 1);
                } else {
                    addMessageBox("" + patientName + " :-\n" + message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @OnClick(R.id.chat_btn_confirm)
    public void onConfirmClick() {
        final Handler mHandler = new Handler(Looper.getMainLooper());
        SessionManager sessionManager = new SessionManager(ChatActivity.this);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Map<String, Object> data = new HashMap<>();
        data.put("user_id", doctorID);
        data.put("booking_id", bookingID);

        JSONObject jsonObject = new JSONObject(data);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Configs.URL_REST_CLIENT + "partner_booking_confirmation/")
                .post(body)
                .addHeader("content-type", "application/json; charset=utf-8")
                .addHeader("Authorization", sessionManager.getUserToken())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                DialogHelper.showDialog(mHandler, ChatActivity.this, "Warning", "Connection Problem, Please Try Again Later." + e.getMessage(), false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    DialogHelper.showDialog(mHandler, ChatActivity.this, "Confirm", "Confirm Success.", true);
                } else {
                    DialogHelper.showDialog(mHandler, ChatActivity.this, "Warning", "Please Try Again", false);
                }
            }
        });

    }

    public void addMessageBox(String message, int type) {
        TextView textView = new TextView(ChatActivity.this);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if (type == 1) {
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubble_in);
        } else {
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.bubble_out);
        }
        textView.setLayoutParams(lp2);
        layoutRef1.addView(textView);
        svScroll.fullScroll(View.FOCUS_DOWN);
    }
}
