package com.example.cyrate.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyrate.R;
import com.example.cyrate.adapters.CommentThreadAdapter;
import com.example.cyrate.models.CommentThreadCardModel;
import com.example.cyrate.net_utils.Const;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CommentThreadActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CommentThreadAdapter commentThreadAdapter;
    ArrayList<CommentThreadCardModel> commentListCardModels = new ArrayList<>();

    TextView sendBtn, emptyView;
    EditText comment_et;
    ImageView back_btn;
    Bundle extras;

    Handler handler;

    WebSocketClient websocket;
    String SERVER_PATH = "ws://coms-309-020.class.las.iastate.edu:8080/comments/";
    int userId = MainActivity.globalUser.getUserId();
    String idForComment;
    String commentType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_thread);

        extras = getIntent().getExtras();
        sendBtn = findViewById(R.id.commentThread_sendBtn);
        comment_et = findViewById(R.id.commentThread_editText);
        back_btn = findViewById(R.id.commentThread_backBtn);
        emptyView = findViewById(R.id.comment_emptyView);

        emptyView.setVisibility(View.INVISIBLE);

        handler = new Handler();

        // Get the comment type and either the Post ID / Review ID
        commentType = extras.getString(Const.COMMENT_TYPE);
        idForComment = String.valueOf(extras.getInt(Const.ID_FOR_COMMENT));
        try {
            initiateSocketConnection();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        recyclerView = findViewById(R.id.commentThread_recyclerView);


        commentThreadAdapter = new CommentThreadAdapter(this, commentListCardModels);
        recyclerView.setAdapter(commentThreadAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(commentListCardModels.isEmpty()){
            emptyView.setVisibility(View.VISIBLE);
        }

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                websocket.close();
                Intent intent;
                if(extras.getString(Const.COMMENT_TYPE).equals(Const.REVIEW_COMMENT)){
                    intent = new Intent(CommentThreadActivity.this, IndividualReviewActivity.class);
                }
                else{
                    intent = new Intent(CommentThreadActivity.this, BusinessPostFeed.class);
                }
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (comment_et.getText().toString().isEmpty()) {
                    Toast.makeText(CommentThreadActivity.this, "Write out your comment!", Toast.LENGTH_LONG).show();
                } else {
                    // Get the current date
                    Date date = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy");
                    String dateStr = formatter.format(date);

                    JSONObject obj = new JSONObject();

                    try {
                        obj.put("commenterName", MainActivity.globalUser.getFullName());
                        obj.put("photoUrl", MainActivity.globalUser.getPhotoUrl());
                        obj.put("commentBody", comment_et.getText().toString());
                        obj.put("commentType", commentType);
                        obj.put("date", dateStr);

                        websocket.send(obj.toString());
                        commentThreadAdapter.addItem(obj);
                        recyclerView.smoothScrollToPosition(commentThreadAdapter.getItemCount() - 1);

                        // Empty the Edit Text
                        comment_et.setText("");

                        removeThreadBar(150);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void initiateSocketConnection() throws URISyntaxException {
        Draft[] drafts = {
                new Draft_6455()
        };

        Log.d("Socket:", "Trying socket");
        websocket = new WebSocketClient(new URI(SERVER_PATH + commentType + "/" + idForComment + "/" + userId), drafts[0]) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.d("OPEN", "run() returned: " + "is connecting");
                runOnUiThread(() -> {
                    Toast.makeText(CommentThreadActivity.this, "Socket Connection Successful", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onMessage(String message) {
                runOnUiThread(() -> {
                    try {

                        JSONObject obj = new JSONObject(message);
                        if(obj != null){
                            emptyView.setVisibility(View.INVISIBLE);
                        }
                        Log.d("Comment Thread ON MSG", obj.toString());

                        commentThreadAdapter.addItem(obj);
                        recyclerView.smoothScrollToPosition(commentThreadAdapter.getItemCount() - 1);

                        removeThreadBar(350);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {

            }

            @Override
            public void onError(Exception ex) {

            }
        };

        websocket.connect();
    }



    // This is a hacky way to remove the vertical thread bar from the last item in the thread
    private void removeThreadBar(int milliseconds){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                View view = recyclerView.getLayoutManager().findViewByPosition(commentThreadAdapter.getItemCount() - 1);
                if (view != null) {
                    view.findViewById(R.id.threadBar).setVisibility(View.INVISIBLE);
                }
            }
        }, milliseconds);
    }

}




