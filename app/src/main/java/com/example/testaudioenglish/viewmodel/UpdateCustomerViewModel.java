package com.example.testaudioenglish.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.testaudioenglish.Model.AccountCustomer;
import com.example.testaudioenglish.Repository.AccountRepository;
import com.example.testaudioenglish.Response.CustomerResponse;

public class UpdateCustomerViewModel extends AndroidViewModel {
    private final AccountRepository repository;
    private final MutableLiveData<String> textName = new MutableLiveData<>();
    private final MutableLiveData<String> textAge = new MutableLiveData<>();
    private final MutableLiveData<String> textNumberPhone = new MutableLiveData<>();
    private final MutableLiveData<String> message = new MutableLiveData<>();
    private final MutableLiveData<Boolean> clickToChange = new MutableLiveData<>();

    public UpdateCustomerViewModel(@NonNull Application application) {
        super(application);
        repository = new AccountRepository();
    }

    public LiveData<String> getTextName() {
        return textName;
    }

    public LiveData<String> getTextAge() {
        return textAge;
    }

    public LiveData<String> getTextNumberPhone() {
        return textNumberPhone;
    }

    public void setClickToChange() {
        clickToChange.setValue(true);
    }

    public void setTextToChange(Long idCustomer) {
        repository.getCustomerByIdCustomer(idCustomer).observeForever(customerResponse -> {
            if (customerResponse != null && customerResponse.getData() != null) {
                AccountCustomer customer = customerResponse.getData();
                textName.setValue(customer.getName());
                textAge.setValue(String.valueOf(customer.getAge()));
                textNumberPhone.setValue(customer.getNumberphone());
            }
        });
    }

    public LiveData<String> getMessage() {
        return message;
    }

    public LiveData<Boolean> getClickToChange() {
        return clickToChange;
    }

    public void updateCustomer(Long idCustomer, AccountCustomer accountCustomer) {
        repository.updateCustomerByIdCustomer(idCustomer, accountCustomer).observeForever(customer -> {
            if (customer != null) {
                message.setValue(customer.getMessage());
            } else {
                message.setValue("Update failed");
            }
        });
    }
}
