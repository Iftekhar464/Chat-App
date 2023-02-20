package com.ahamed.citchat;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.ahamed.citchat.databinding.FragmentRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class RegisterFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private static final String USER_REF = "user_data";
    private ProgressDialog progressDialog;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentRegisterBinding binding = FragmentRegisterBinding.inflate(inflater);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");

        binding.btnReg.setOnClickListener(v -> {
            String strName = binding.etName.getEditableText().toString().trim();
            String strMail = binding.mail.getEditableText().toString().trim();
            String strPass = binding.pass.getEditableText().toString().trim();

            if (strName.isEmpty()) {
                binding.etName.setError("can't be empty");
                return;
            }
            if (strMail.isEmpty()) {
                binding.mail.setError("can't be empty");
                return;
            }
            if (strPass.isEmpty()) {
                binding.pass.setError("can't be empty");
                return;
            }
            progressDialog.show();
            auth.createUserWithEmailAndPassword(strMail, strPass).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null) {
                        String id = user.getUid();
                        DocumentReference reference = db.collection(USER_REF).document(id);
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("name", strName);
                        map.put("mail", strMail);
                        map.put("pass", strPass);
                        map.put("id", id);
                        reference.set(map).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                progressDialog.dismiss();
                                Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_dashbordFragment);
                            }
                        }).addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            auth.signOut();
                            Toast.makeText(getActivity(), "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            });

        });
        binding.logIn.setOnClickListener(v -> Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_logInFragment));
        return binding.getRoot();
    }
}