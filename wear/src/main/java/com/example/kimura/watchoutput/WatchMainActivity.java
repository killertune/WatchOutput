package com.example.kimura.watchoutput;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

public class WatchMainActivity extends Activity {

    private Button mButton;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mButton = (Button) stub.findViewById(R.id.button);
                mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(WatchMainActivity.this, "押された！", Toast.LENGTH_SHORT).show();
                        mButton.setVisibility(View.GONE);
                        mImageView.setVisibility(View.VISIBLE);
                    }
                });
                mImageView = (ImageView) stub.findViewById(R.id.image);
                mImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mButton.setVisibility(View.VISIBLE);
                        mImageView.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

}
