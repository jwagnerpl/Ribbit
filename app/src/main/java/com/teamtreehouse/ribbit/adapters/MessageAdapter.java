package com.teamtreehouse.ribbit.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamtreehouse.ribbit.R;
import com.teamtreehouse.ribbit.models.Message;

import org.joda.time.Instant;
import org.joda.time.Interval;
import org.joda.time.ReadablePeriod;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {
    private static final String TAG = "MessageAdapter";
    protected Context mContext;
    protected List<Message> mMessages;


    public MessageAdapter(Context context, List<Message> messages) {
        super(context, R.layout.message_item, messages);
        mContext = context;

        // Create a full copy of mMessages
        mMessages = new ArrayList<Message>();
        for (Message msg : messages) {
            mMessages.add(msg);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null || convertView.getTag() == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.message_item, null);
            holder = new ViewHolder();
            holder.iconImageView = (ImageView) convertView.findViewById(R.id.messageIcon);
            holder.nameLabel = (TextView) convertView.findViewById(R.id.senderLabel);
            holder.timeLabel = (TextView) convertView.findViewById(R.id.timeLabel);
            Log.d(TAG, "convertview was not null" );
        } else {
            holder = (ViewHolder) convertView.getTag();
            Log.d(TAG, holder.toString());
            Log.d(TAG, "convertview was null" );
        }

        Message message = mMessages.get(position);

        Date createdAt = message.getCreatedAt();
        Long timeInMs = createdAt.getTime();
        Interval interval = new Interval(timeInMs, new Instant().getMillis());
        Long intervalMillis = interval.toDurationMillis();
        holder.timeLabel.setText(displayTimeSinceCreation(intervalMillis));

        if (message.getString(Message.KEY_FILE_TYPE).equals(Message.TYPE_IMAGE)) {
            holder.iconImageView.setImageResource(R.drawable.ic_picture);
        } else {
            holder.iconImageView.setImageResource(R.drawable.ic_video);
        }
        holder.nameLabel.setText(message.getString(Message.KEY_SENDER_NAME));

        return convertView;
    }

    private static class ViewHolder {
        ImageView iconImageView;
        TextView nameLabel;
        TextView timeLabel;
    }

    public void refill(List<Message> messages) {
        mMessages.clear();
        mMessages.addAll(messages);
        notifyDataSetChanged();
    }

    public String displayTimeSinceCreation(Long time) {
        int seconds;
        int minutes;
        int hours;
        int days;


        if (time > 24 * 60 * 60 * 1000 /*greater than a day*/) {
            days =(int) Math.rint(time / 24 / 60 / 60 / 1000);
            return days + " day(s) ago.";
        }
        else if (time > 60 * 60 * 1000 /*greater than an hour*/) {
            hours =(int) Math.rint(time / 60 / 60 / 1000);
            return hours + " hour(s) ago.";
        }
        else if (time > 60 * 1000 /*greater than a minute*/) {
            minutes =(int) Math.rint(time / 60 / 1000);
            return minutes + " minute(s) ago.";
        }
        else {
            seconds =(int) Math.rint(time/1000);
            return seconds + " seconds ago.";
        }
    }

    }







