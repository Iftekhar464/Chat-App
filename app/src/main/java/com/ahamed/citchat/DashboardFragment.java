package com.ahamed.citchat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ahamed.citchat.adapter.UserAdapter;
import com.ahamed.citchat.databinding.FragmentDashbordBinding;
import com.ahamed.citchat.model.UserModel;
import com.ahamed.citchat.viewmodel.MyViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


public class DashboardFragment extends Fragment {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private MyViewModel viewModel;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentDashbordBinding binding = FragmentDashbordBinding.inflate(inflater);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        viewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
        if (user == null) {
            Navigation.findNavController(container).navigate(R.id.action_dashbordFragment_to_logInFragment);
        } else {
            binding.logOut.setOnClickListener(v -> {
                auth.signOut();
                Navigation.findNavController(v).navigate(R.id.action_dashbordFragment_to_logInFragment);
            });

            List<UserModel> list = new ArrayList<>();
            UserAdapter adapter = new UserAdapter(list);
            binding.rvFnd.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.rvFnd.setAdapter(adapter);

            viewModel.getAllUser(user.getUid()).observe(getViewLifecycleOwner(), userModels -> {
                list.clear();
                list.addAll(userModels);
                adapter.notifyDataSetChanged();
            });
            viewModel.myData(user.getUid()).observe(getViewLifecycleOwner(), userModel -> binding.tvName.setText(userModel.getName()));
        }
        return binding.getRoot();
    }
}