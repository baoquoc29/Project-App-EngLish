package com.example.testaudioenglish.View.AppView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.testaudioenglish.Adapter.NotificationAdapter;
import com.example.testaudioenglish.Model.NotificationModel;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.Response.NotificationResponse;
import com.example.testaudioenglish.databinding.FragmentHomeBinding;
import com.example.testaudioenglish.databinding.FragmentNofiticationBinding;
import com.example.testaudioenglish.viewmodel.HomeFragmentViewModel;
import com.example.testaudioenglish.viewmodel.NotificationFragmentViewModel;

import java.util.ArrayList;
import java.util.List;


public class NotificationFragment extends Fragment {
    private NotificationFragmentViewModel notificationFragmentViewModel;
    private NotificationAdapter adapter;
    private RecyclerView recyclerView;
    private List<NotificationModel> notificationModelList = new ArrayList<>();
    private long idUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentNofiticationBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_nofitication, container, false);
        notificationFragmentViewModel = new ViewModelProvider(this).get(NotificationFragmentViewModel.class);
        binding.setViewModel(notificationFragmentViewModel);
        binding.setLifecycleOwner(this);
        View view = binding.getRoot();
        idUser = getIdUser();
        mapping(binding);
        setUpView();
        return view;
    }

    public void mapping(FragmentNofiticationBinding binding){
        recyclerView = binding.recyleNotification;
    }

    public void setUpView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NotificationAdapter(notificationModelList);
        recyclerView.setAdapter(adapter);
        notificationFragmentViewModel.getNotification(idUser, "system").observe(getViewLifecycleOwner(), new Observer<NotificationResponse>() {
            @Override
            public void onChanged(NotificationResponse notificationResponse) {
                if (notificationResponse != null) {
                    notificationModelList.clear();
                    notificationModelList.addAll(notificationResponse.getData());
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(getContext(), "Loi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private Long getIdUser() {
        Context context = getContext().getApplicationContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences("GetIDUser", Context.MODE_PRIVATE);

        return sharedPreferences.getLong("idUser", -1);
    }

}
