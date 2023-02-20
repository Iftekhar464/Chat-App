package com.ahamed.citchat.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ahamed.citchat.model.ChatModel;
import com.ahamed.citchat.model.UserModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MyViewModel extends AndroidViewModel {
    private final FirebaseFirestore firestore;
    private static final String USER_REF = "user_data";
    private static final String CHAT_REF = "message_data";

    public MyViewModel(@NonNull Application application) {
        super(application);
        firestore = FirebaseFirestore.getInstance();
    }

    public MutableLiveData<List<UserModel>> getAllUser(String myId) {
        MutableLiveData<List<UserModel>> allUser = new MutableLiveData<>();
        firestore.collection(USER_REF).addSnapshotListener((value, error) -> {
            if (error != null) return;
            if (value != null) {
                List<UserModel> tempList = new ArrayList<>();
                for (DocumentSnapshot document : value.getDocuments()) {
                    UserModel model = document.toObject(UserModel.class);
                    assert model != null;
                    if (!model.getId().equals(myId)) {
                        tempList.add(model);
                    }
                }
                allUser.postValue(tempList);
            }
        });
        return allUser;

    }

    public MutableLiveData<UserModel> myData(String id) {
        MutableLiveData<UserModel> myAccount = new MutableLiveData<>();
        firestore.collection(USER_REF).document(id).addSnapshotListener((value, error) -> {
            if (error != null) return;
            if (value != null) {
                UserModel model = value.toObject(UserModel.class);
                myAccount.postValue(model);
            }
        });

        return myAccount;
    }

    public MutableLiveData<List<ChatModel>> getAllChat(String myId, String fndId) {
        List<String> list = new ArrayList<>();
        list.add(myId);
        list.add(fndId);
        MutableLiveData<List<ChatModel>> chatList = new MutableLiveData<>();
        firestore.collection(CHAT_REF)
                .whereIn("sender", list)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;
                    if (value != null) {
                        List<ChatModel> tempList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                            ChatModel model = documentSnapshot.toObject(ChatModel.class);
                            assert model != null;
                            if (model.getSender().equals(myId)
                                    && model.getReceiver().equals(fndId)
                                    || model.getReceiver().equals(myId)
                                    && model.getSender().equals(fndId)) {
                                tempList.add(model);
                            }

                        }
                        chatList.postValue(tempList);
                    }
                });
        return chatList;
    }

}
