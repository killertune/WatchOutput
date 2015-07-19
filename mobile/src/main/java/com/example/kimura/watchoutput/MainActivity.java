package com.example.kimura.watchoutput;

import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.common;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.Collection;
import java.util.HashSet;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private GoogleApiClient mGoogleApiClient;

    private EditText mEditText;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button) findViewById(R.id.button_send);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        mEditText = (EditText) findViewById(R.id.edit_text);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Wearと接続する
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        Toast.makeText(MainActivity.this, "Wearと接続", Toast.LENGTH_SHORT).show();
                        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient)
                            .setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                                @Override
                                public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                                    for (Node node : getConnectedNodesResult.getNodes()) {
                                        Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), common.START_ACTIVITY_PATH, null)
                                                .setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                                                    @Override
                                                    public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                                                        if (!sendMessageResult.getStatus().isSuccess()) {
                                                            Log.e(TAG, "ERROR: failed to send Message: " + sendMessageResult.getStatus());
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                        Toast.makeText(MainActivity.this, "Wearと切断", Toast.LENGTH_SHORT).show();
                    }
                }).build();
        mGoogleApiClient.connect();
    }

    private void sendMessage() {
        if (mEditText.getText().length() <= 0) {
            Toast.makeText(MainActivity.this, "入力してね", Toast.LENGTH_SHORT).show();
            return;
        }

        PutDataMapRequest dataMap = PutDataMapRequest.create(common.MESSAGE_PATH);   //…… 1
        dataMap.getDataMap().putString(common.MESSAGE_KEY, mEditText.getText().toString());   //…… 2
        PutDataRequest request = dataMap.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mGoogleApiClient, request);
        pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(DataApi.DataItemResult dataItemResult) {
                // メッセージ送信成功時
                mEditText.getText().clear();
            }
        });

    }

}
