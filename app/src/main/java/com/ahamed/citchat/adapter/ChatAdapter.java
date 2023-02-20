package com.ahamed.citchat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahamed.citchat.R;
import com.ahamed.citchat.model.ChatModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<ChatModel> list;
    static final int RIGHT = 0;
    static final int LEFT = 1;
    private FirebaseUser currentUser;

    public ChatAdapter(List<ChatModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == RIGHT) {
            View rightView = LayoutInflater.from(parent.getContext()).inflate(R.layout.right_chat, parent, false);
            return new ChatViewHolder(rightView);
        } else {
            View leftView = LayoutInflater.from(parent.getContext()).inflate(R.layout.left_chat, parent, false);
            return new ChatViewHolder(leftView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatModel model = list.get(position);
        holder.msg.setText(model.getMsg());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentId = null;
        if (currentUser != null) {
            currentId = currentUser.getUid();
        }
        if (list.get(position).getSender().equals(currentId)) {
            return RIGHT;
        } else {
            return LEFT;
        }
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView msg;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.tv_msg);
        }
    }
}
