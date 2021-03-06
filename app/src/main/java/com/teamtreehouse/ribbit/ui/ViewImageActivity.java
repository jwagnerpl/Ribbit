package com.teamtreehouse.ribbit.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.teamtreehouse.ribbit.R;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class ViewImageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        // Show the Up button in the action bar.
        setupActionBar();


        final TextView timerTextView = (TextView) findViewById(R.id.timer);

        if (getIntent().getStringExtra("type").equals("photo")) {
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            Uri imageUri = getIntent().getData();
            Picasso.with(this).load(imageUri.toString()).into(imageView);
        } else {
            String msgText = getIntent().getStringExtra("messageText");
            TextView msgView = (TextView) findViewById(R.id.messageTextView);
            msgView.setVisibility(View.VISIBLE);
            msgView.setText(msgText);
        }

        CountDownTimer cdt = new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                timerTextView.setText("Seconds Remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                finish();
            }

        }.start();
    }

    /**
     * Set up the {@link android.app.ActionBar}.
     */
    private void setupActionBar() {

        getActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
