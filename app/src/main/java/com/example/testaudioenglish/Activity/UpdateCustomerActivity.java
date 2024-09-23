package com.example.testaudioenglish.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.testaudioenglish.Model.AccountCustomer;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.databinding.ActivityUpdateCustomerBinding;
import com.example.testaudioenglish.viewmodel.UpdateCustomerViewModel;

public class UpdateCustomerActivity extends AppCompatActivity {

    private SharedPreferences userPrefs;
    private UpdateCustomerViewModel updateCustomerViewModel;
    private ActivityUpdateCustomerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_customer);

        // Initialize ViewModel
        updateCustomerViewModel = new ViewModelProvider(this).get(UpdateCustomerViewModel.class);
        binding.setViewModel(updateCustomerViewModel);
        binding.setLifecycleOwner(this);

        // Initialize SharedPreferences
        userPrefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        Long idCustomer = userPrefs.getLong("idCustomer", 1L);
        updateCustomerViewModel.setTextToChange(idCustomer);

        updateCustomerViewModel.getClickToChange().observe(this, clicked -> {
            if (clicked) {
                AccountCustomer accountCustomer = new AccountCustomer(
                        binding.editTextName.getText().toString(),
                        Long.valueOf(binding.editTextAge.getText().toString()),
                        binding.editTextPhone.getText().toString()
                );
                updateCustomerViewModel.updateCustomer(idCustomer, accountCustomer);
            }
        });

        updateCustomerViewModel.getMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(UpdateCustomerActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();

        return true;
    }
}
