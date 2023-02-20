package com.ahamed.citchat;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.ahamed.citchat.databinding.FragmentLogInBinding;
import com.google.firebase.auth.FirebaseAuth;


public class LogInFragment extends Fragment {
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;

    public LogInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentLogInBinding binding = FragmentLogInBinding.inflate(inflater);
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");

        binding.btnNewOne.setOnClickListener(v -> Navigation.findNavController(requireView()).navigate(R.id.action_logInFragment_to_registerFragment));
        binding.btnLogIn.setOnClickListener(v -> {
            String strMail = binding.etEmail.getEditableText().toString().trim();
            String strPass = binding.etPass.getEditableText().toString().trim();
            if (strMail.isEmpty()) {
                binding.etEmail.setError("can't be empty");
                return;
            }
            if (strPass.isEmpty()) {
                binding.etPass.setError("can't be empty");
                return;
            }
            progressDialog.show();
            auth.signInWithEmailAndPassword(strMail, strPass).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Navigation.findNavController(requireView()).navigate(R.id.action_logInFragment_to_dashbordFragment);
                }
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            });
        });

        return binding.getRoot();
    }
}