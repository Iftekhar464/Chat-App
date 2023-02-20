package com.ahamed.citchat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ahamed.citchat.adapter.ChatAdapter;
import com.ahamed.citchat.databinding.FragmentChatBinding;
import com.ahamed.citchat.model.ChatModel;
import com.ahamed.citchat.model.UserModel;
import com.ahamed.citchat.viewmodel.MyViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ChatFragment extends Fragment {
    private FirebaseFirestore firestore;
    private FirebaseUser user;
    private UserModel fndData;

    public ChatFragment() {
        // Required empty public constructor
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentChatBinding binding = FragmentChatBinding.inflate(inflater);
        firestore = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        MyViewModel viewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);

        Bundle bundle = getArguments();
        assert bundle != null;
        fndData = (UserModel) bundle.getSerializable("fnd_data");
        if (fndData != null) {
            binding.tvFndName.setText(fndData.getName());

            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
           // linearLayoutManager.setReverseLayout(true);
            binding.rvMsg.setHasFixedSize(true);
            binding.rvMsg.setLayoutManager(linearLayoutManager);

            List<ChatModel> list = new ArrayList<>();
            ChatAdapter adapter = new ChatAdapter(list);
            binding.rvMsg.setAdapter(adapter);

            viewModel.getAllChat(user.getUid(), fndData.getId()).observe(getViewLifecycleOwner(), chatModels -> {
                list.clear();
                list.addAll(chatModels);
                adapter.notifyDataSetChanged();

            });
            binding.send.setOnClickListener(v -> {
                String strMsg = binding.etMsg.getEditableText().toString().trim();
                if (strMsg.isEmpty()) {
                    binding.etMsg.setError("can't be empty");
                    return;
                }
                binding.send.setClickable(false);
                String id = String.valueOf(System.currentTimeMillis());
                DocumentReference reference = firestore.collection("message_data").document(id);
                HashMap<String, Object> map = new HashMap<>();
                map.put("sender", user.getUid());
                map.put("receiver", fndData.getId());
                map.put("msg", strMsg);
                reference.set(map).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        binding.etMsg.setText(null);
                        binding.send.setClickable(true);
                    }
                }).addOnFailureListener(e -> {
                    binding.send.setClickable(true);
                    Toast.makeText(getActivity(), "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
            });
            binding.btnBack.setOnClickListener(v -> Navigation.findNavController(requireView()).popBackStack());
        }
        return binding.getRoot();
    }
}