package com.hdlight.wallpaperapps.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hdlight.wallpaperapps.R;
import com.hdlight.wallpaperapps.databinding.ActivityForgotPasswordBinding;
import com.hdlight.wallpaperapps.utils.Utils;

public class ForgotPasswordActivity extends BaseActivity {


    ActivityForgotPasswordBinding binding;
    String countryCode = "+91";
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utils.FullScreenUIMode(ForgotPasswordActivity.this);

        FirebaseAuth.getInstance().getFirebaseAuthSettings().setAppVerificationDisabledForTesting(false);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        binding.txtOtpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.txtOtpButton:
                if (isValid()){
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.txtOtpButton.setVisibility(View.GONE);
                    sendOtp();
                }
                break;
        }
    }

    private void sendOtp() {

        final String phNo = binding.etMobileNumber.getText().toString().trim();
        Query checkUser = FirebaseDatabase.getInstance().getReference("users").child(phNo);
//            Toast.makeText(this, checkUser.toString(), Toast.LENGTH_SHORT).show();
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("Info", "onDataChange: " + String.valueOf(snapshot.getValue()));

                if (snapshot.exists()) {
//                        Toast.makeText(ForgotPasswordActivity.this, "Otp", Toast.LENGTH_SHORT).show();
//                        sendOtp(phNo);
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        public final void run() {
                            Intent intent = new Intent(ForgotPasswordActivity.this.getApplicationContext(), OtpActivity.class);
                            intent.putExtra("mobile", phNo);
                            intent.putExtra("data", "update");
                            startActivity(intent);
                            finish();
                            binding.progressBar.setVisibility(View.GONE);
                            binding.txtOtpButton.setVisibility(View.VISIBLE);
                        }
                    }, 1000);

                }else {
//                    Toast.makeText(ForgotPasswordActivity.this, "User not register", Toast.LENGTH_SHORT).show();
                    setCustomToast(getString(R.string.user_not_reg),ForgotPasswordActivity.this);
                    binding.progressBar.setVisibility(View.GONE);
                    binding.txtOtpButton.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    boolean isValid() {
        String message = "";
        if (binding.etMobileNumber.getText().toString().trim().isEmpty()) {
            message = getString(R.string.please_enter_mobile_number);
            binding.etMobileNumber.requestFocus();
        } else if (binding.etMobileNumber.getText().toString().trim().length() < 10) {
            message = getString(R.string.mobile_no_length);
            binding.etMobileNumber.requestFocus();
        }

        if (!message.trim().isEmpty()) {
            Utils.hideKeyBoardFromView(this);
            setCustomToast(message,this);
        }
        return message.trim().isEmpty();
    }

}