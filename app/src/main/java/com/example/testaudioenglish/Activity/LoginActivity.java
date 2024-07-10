package com.example.testaudioenglish.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.testaudioenglish.Model.EmailSendModel;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.databinding.LoginActivityBinding;
import com.example.testaudioenglish.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;
    private Dialog dialog;
    private Dialog forgotDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.login_activity);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        binding.setViewModelLogin(loginViewModel);
        binding.setLifecycleOwner(this);

        loadingDialog();

        // Setup forgot password dialog
        setupForgotDialog();

        loginViewModel.isLoading.observe(this, isLoading -> {
            if (isLoading) {
                dialog.show();
            } else {
                dialog.dismiss();
            }
        });

        loginViewModel.message.observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });

        loginViewModel.isLoadingForgot.observe(this, event -> {
            if (event) {
             forgotDialog.show();
            }
            else{
                forgotDialog.dismiss();
            }
        });

        loginViewModel.navigateToSignUp.observe(this, event -> {
            Boolean shouldNavigate = event.getContentIfNotHandled();
            if (shouldNavigate != null && shouldNavigate) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish(); // Close the current activity
            }
        });
        loginViewModel.navigateToLogin.observe(this, booleanEvent -> {
            Boolean shouldNavigate = booleanEvent.getContentIfNotHandled();
            if (shouldNavigate != null && shouldNavigate) {
                String data = loginViewModel.username.getValue();
                setDataToSharedPreferences(data);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void loadingDialog() {
        dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(LayoutInflater.from(this).inflate(R.layout.activity_splash, null));
        dialog.setCancelable(false);
    }

    public void setupForgotDialog() {
        forgotDialog = new Dialog(this, android.R.style.Theme_DeviceDefault_DayNight);
        forgotDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        forgotDialog.setContentView(LayoutInflater.from(this).inflate(R.layout.forgotlayout, null));
        forgotDialog.setCancelable(true);

        // Set the dialog window to half of the screen size
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(forgotDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;

        // Apply the layout parameters to the dialog
        forgotDialog.getWindow().setAttributes(layoutParams);

        Button btnSubmit = forgotDialog.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(v -> {
            String email = retrieveForgotEmail();
            EmailSendModel emailSendModel = new EmailSendModel(email);
            if (!email.isEmpty() && loginViewModel.isValidEmail(email)) {
                loginViewModel.servicePass(emailSendModel);
                Toast.makeText(this, "Vui lòng kiểm tra email", Toast.LENGTH_SHORT).show();
                setTextEmpty();
                forgotDialog.dismiss();
            } else {
                Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            }
        });

        // Show the dialog after setting up
        forgotDialog.show();
    }


    public String retrieveForgotEmail() {
        EditText emailEditText = forgotDialog.findViewById(R.id.etEmail);
        return emailEditText.getText().toString().trim();
    }
    public void setTextEmpty(){
        EditText emailEditText = forgotDialog.findViewById(R.id.etEmail);
        emailEditText.setText("");
    }
    public void setDataToSharedPreferences(String data) {
        SharedPreferences sharedPref = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", data);
        editor.apply();
    }


}
