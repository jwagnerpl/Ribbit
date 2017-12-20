package com.teamtreehouse.ribbit.mockdata;

import android.net.Uri;
import android.util.Log;

import com.teamtreehouse.ribbit.R;
import com.teamtreehouse.ribbit.RibbitApplication;
import com.teamtreehouse.ribbit.models.Message;
import com.teamtreehouse.ribbit.models.MessageFile;
import com.teamtreehouse.ribbit.models.User;
import com.teamtreehouse.ribbit.utils.FileHelper;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class MockMessages {
    private static final String TAG = "MockMessages";
    private static MockMessages instance = null;
    private ArrayList<Message> allMessages;

    private static final int NUM_TEST_MESSAGES = 15;

    public static MockMessages getInstance() {
        if (instance == null) {
            instance = new MockMessages();
        }
        Log.d(TAG, "returned existing instance");
        return instance;
    }

    public MockMessages() {
        allMessages = new ArrayList<>();

        for (int i = 0; i < NUM_TEST_MESSAGES; i++) {
            allMessages.add(getTestPhotoMessage());
        }
    }

    public void saveMessage(Message msg) {
        allMessages.add(msg);
    }

    public ArrayList<Message> getAllMessages() {
        return allMessages;
    }

    public ArrayList<Message> getMessagesForUser(String userId) {
        ArrayList<Message> userMessages = new ArrayList<>();

        for (Message msg : allMessages) {
            ArrayList<String> recipients = (ArrayList<String>) msg.getList(Message.KEY_RECIPIENT_IDS);
            for (String recipient : recipients) {
                if (recipient.equals((userId))) {
                    userMessages.add(msg);
                }
            }
        }

        return userMessages;
    }

    private Message getTestPhotoMessage() {
        int randomUserIndex = new Random().nextInt(MockUsers.testUsers.size());
        User sender = MockUsers.testUsers.get(randomUserIndex);

        Message photoMessage = new Message("");

        Uri testPhotoUri = Uri.parse("android.resource://" + RibbitApplication.PACKAGE_NAME + "/" + R.raw.poley_thinks);
        photoMessage.put(Message.KEY_SENDER_ID, sender.getObjectId());
        photoMessage.put(Message.KEY_SENDER_NAME, sender.getUsername());

        ArrayList<String> recipientIds = new ArrayList<>();
        recipientIds.add(User.getCurrentUser().getObjectId());
        photoMessage.put(Message.KEY_RECIPIENT_IDS, recipientIds);

        photoMessage.put(Message.KEY_FILE_TYPE, Message.TYPE_IMAGE);
        byte[] fileBytes = FileHelper.getByteArrayFromFile(null, testPhotoUri);
        MessageFile file = new MessageFile("test_photo.png", fileBytes, testPhotoUri);
        photoMessage.put(Message.KEY_FILE, file);

        return photoMessage;
    }

    public boolean deleteMessage(UUID msgId) {
        for (Message msg : allMessages) {
            Log.d(TAG, msg.getId().toString()+ "loop message");
            Log.d(TAG, msgId.toString() + "my deleted id");

            if (msg.getId() == msgId) {
                Log.d(TAG, "message finally deleted");
                allMessages.remove(msg);
                return true;
            }
            else {Log.d(TAG, "message not deleted");}
        }

        return false;
    }
}
