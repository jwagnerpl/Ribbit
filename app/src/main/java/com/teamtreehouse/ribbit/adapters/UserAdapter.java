package com.teamtreehouse.ribbit.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.teamtreehouse.ribbit.R;
import com.teamtreehouse.ribbit.models.Relation;
import com.teamtreehouse.ribbit.models.User;
import com.teamtreehouse.ribbit.models.callbacks.SaveCallback;
import com.teamtreehouse.ribbit.utils.MD5Util;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {

    protected Context mContext;
    protected List<User> mUsers;



    public UserAdapter(Context context, List<User> users) {
        super(context, R.layout.message_item, users);
        mContext = context;

        // Create a full copy of mUsers
        mUsers = new ArrayList<User>();
        for (User user : users) {
            mUsers.add(user);
        }
    }

    private static final String TAG = "UserAdapter";

    @Override
    public int getCount() {
        return mUsers.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.user_item, null);
            holder = new ViewHolder();
            holder.userImageView = (ImageView) convertView.findViewById(R.id.userImageView);
            holder.nameLabel = (TextView) convertView.findViewById(R.id.nameLabel);
            holder.checkImageView = (ImageView) convertView.findViewById(R.id.checkImageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        User user = mUsers.get(position);
        String email = user.getEmail();

        if (email == null || email.equals("")) {
            holder.userImageView.setImageResource(R.drawable.avatar_empty);
        } else {
            email = email.toLowerCase();
            String hash = MD5Util.md5Hex(email);
            String gravatarUrl = "http://www.gravatar.com/avatar/" + hash +
                    "?s=204&d=404";
            Picasso.with(mContext)
                    .load(gravatarUrl)
                    .placeholder(R.drawable.avatar_empty)
                    .into(holder.userImageView);
        }

        holder.nameLabel.setText(user.getUsername());

        GridView gridView = (GridView) parent;
        if (gridView.isItemChecked(position)) {
            holder.checkImageView.setVisibility(View.VISIBLE);
        } else {
            holder.checkImageView.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView userImageView;
        ImageView checkImageView;
        TextView nameLabel;
    }

    public void refill(List<User> users) {
        mUsers.clear();
        mUsers.addAll(users);
        notifyDataSetChanged();
    }
}






