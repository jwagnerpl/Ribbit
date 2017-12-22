package com.teamtreehouse.ribbit.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.Toast;

import com.teamtreehouse.ribbit.R;
import com.teamtreehouse.ribbit.adapters.MessageAdapter;
import com.teamtreehouse.ribbit.mockdata.MockMessages;
import com.teamtreehouse.ribbit.models.Message;
import com.teamtreehouse.ribbit.models.MessageFile;
import com.teamtreehouse.ribbit.models.Query;
import com.teamtreehouse.ribbit.models.User;
import com.teamtreehouse.ribbit.models.callbacks.FindCallback;
import com.teamtreehouse.ribbit.utils.OutputMediaFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class InboxFragment extends ListFragment {

    public static final int TAKE_PHOTO_REQUEST = 0;
    public static final int TAKE_VIDEO_REQUEST = 1;
    public static final int PICK_PHOTO_REQUEST = 2;
    public static final int PICK_VIDEO_REQUEST = 3;
    public static final int COMPOSE_MESSAGE = 6;

    public static final int MEDIA_TYPE_IMAGE = 4;
    public static final int MEDIA_TYPE_VIDEO = 5;
    public static final int MEDIA_TYPE_MESSAGE = 7;

    public static final int FILE_SIZE_LIMIT = 1024 * 1024 * 10; // 10 MB

    private Uri mMediaUri;
    protected List<Message> mMessages;
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    private static final String TAG = "InboxFragment";
    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //mSwipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipeRefreshLayout);
        //Log.d(TAG, User.getCurrentUser().toString());
        //if(User.getCurrentUser() == null){startActivity(new Intent(getContext(), LoginActivity.class));}
        View rootView = inflater.inflate(R.layout.fragment_inbox,
                container, false);


        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mSwipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipeRefreshLayout);
        Log.d(TAG, mSwipeRefreshLayout.toString());
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);

        // Deprecated method - what should we call instead?

        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.swipeRefresh1,
                R.color.swipeRefresh2,
                R.color.swipeRefresh3,
                R.color.swipeRefresh4
        );

        if (User.getCurrentUser() != null) {
            retrieveMessages();
        }

        final FloatingActionButton fab1 = (FloatingActionButton) getView().findViewById(R.id.performActionFAB1);
        final FloatingActionButton fab2 = (FloatingActionButton) getView().findViewById(R.id.performActionFAB2);
        final FloatingActionButton fab3 = (FloatingActionButton) getView().findViewById(R.id.performActionFAB3);
        final FloatingActionButton fab4 = (FloatingActionButton) getView().findViewById(R.id.performActionFAB4);
        final FloatingActionButton fab5 = (FloatingActionButton) getView().findViewById(R.id.performActionFAB5);
        FloatingActionButton fab6 = (FloatingActionButton) getView().findViewById(R.id.performActionFAB6);
        final OutputMediaFile omf = new OutputMediaFile();

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mMediaUri = omf.getOutputMediaFileUri(getContext(),MEDIA_TYPE_IMAGE);
                if (mMediaUri == null) {
                    // display an error
                    Toast.makeText(getContext(), R.string.error_external_storage,
                            Toast.LENGTH_LONG).show();
                } else {
                    takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                    takePhotoIntent.putExtra("uri", mMediaUri);
                    Log.d(TAG, mMediaUri + "first instance of media uRI");
                    getActivity().startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
                }
            }
        });

        fab6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fab1.getVisibility() == View.INVISIBLE){
                fab1.setVisibility(View.VISIBLE);
                fab2.setVisibility(View.VISIBLE);
                fab3.setVisibility(View.VISIBLE);
                fab4.setVisibility(View.VISIBLE);
                fab5.setVisibility(View.VISIBLE);}

                else{
                    fab1.setVisibility(View.INVISIBLE);
                    fab2.setVisibility(View.INVISIBLE);
                    fab3.setVisibility(View.INVISIBLE);
                    fab4.setVisibility(View.INVISIBLE);
                    fab5.setVisibility(View.INVISIBLE);
                }
            }

        });

    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setProgressBarIndeterminateVisibility(true);
    }

    private void retrieveMessages() {
        Query<Message> query = Message.getQuery();
        if (mSwipeRefreshLayout == null) {
            mSwipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipeRefreshLayout);
        }
        query.whereEqualTo(Message.KEY_RECIPIENT_IDS, User.getCurrentUser().getObjectId());
        query.addDescendingOrder(Message.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> messages, Exception e) {
                getActivity().setProgressBarIndeterminateVisibility(false);

                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }

                if (e == null) {
                    // We found messages!
                    mMessages = messages;

                    String[] usernames = new String[mMessages.size()];
                    int i = 0;
                    for (Message message : mMessages) {
                        usernames[i] = message.getString(Message.KEY_SENDER_NAME);
                        i++;
                    }
                    if (getListView().getAdapter() == null) {
                        MessageAdapter adapter = new MessageAdapter(
                                getListView().getContext(),
                                mMessages);
                        setListAdapter(adapter);
                    } else {
                        // refill the adapter!
                        ((MessageAdapter) getListView().getAdapter()).refill(mMessages);
                    }
                }
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Message message = mMessages.get(position);
        String messageType = message.getString(Message.KEY_FILE_TYPE);
        MessageFile file = message.getFile(Message.KEY_FILE);
        Uri fileUri;

        if (file != null) {
            fileUri = file.getUri();
            if (messageType.equals(Message.TYPE_IMAGE)) {
                // view the image
                Intent intent = new Intent(getActivity(), ViewImageActivity.class);
                intent.setData(fileUri);
                intent.putExtra("type", "photo");
                startActivity(intent);
            } else if (messageType.equals(Message.TYPE_VIDEO)) {
                // view the video
                Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
                intent.putExtra("type", "video");
                intent.setDataAndType(fileUri, "video/*");
                startActivity(intent);
            }

        } else {
            Log.d(TAG, "I am going to compose a message");
            Intent intent = new Intent(getActivity(), ViewImageActivity.class);
            intent.putExtra("messageText", message.getMessageText());
            intent.putExtra("type", "text");
            startActivity(intent);
        }

        // Delete it!
        List<String> ids = message.getList(Message.KEY_RECIPIENT_IDS);

        if (ids.size() == 1) {
            // last recipient - delete the whole thing!
            message.deleteInBackground();
            Log.d(TAG, "deleting message now");
            mMessages.remove(position);
            MessageAdapter adapter = (MessageAdapter) getListView().getAdapter();
            adapter.notifyDataSetChanged();
            //Log.d(TAG,message.getId().toString());
            //message.removeRecipient(User.getCurrentUser().getObjectId());


        } else {
            // remove the recipient
            message.deleteInBackground();
            MessageAdapter adapter = (MessageAdapter) getListView().getAdapter();
            adapter.notifyDataSetChanged();
            message.removeRecipient(User.getCurrentUser().getObjectId());
            message.deleteInBackground();


        }
    }

    protected OnRefreshListener mOnRefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh() {
            retrieveMessages();
        }
    };

}









