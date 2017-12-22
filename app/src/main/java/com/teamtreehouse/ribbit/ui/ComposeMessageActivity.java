package com.teamtreehouse.ribbit.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.teamtreehouse.ribbit.R;
import com.teamtreehouse.ribbit.models.Message;

public class ComposeMessageActivity extends Activity {

    Button submitButton;
    EditText messageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_message);

        submitButton = (Button) findViewById(R.id.sendMessageButton);
        messageText = (EditText) findViewById(R.id.messageEditText);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageText.getText().toString();
                Intent intent = new Intent(ComposeMessageActivity.this, RecipientsActivity.class);
                intent.putExtra("KEY_FILE_TYPE", "text");
                intent.putExtra(Message.KEY_FILE_TYPE, Message.TYPE_TEXT);
                intent.putExtra("message", message);
                startActivity(intent);
                finish();
            }
        });
    }



}
