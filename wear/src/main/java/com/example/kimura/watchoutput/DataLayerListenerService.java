package com.example.kimura.watchoutput;

import android.content.Intent;
import android.widget.Toast;

import com.example.common;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class DataLayerListenerService extends WearableListenerService {

    public DataLayerListenerService() {
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {   //…… 2
        // 送られてきたパスの値がSTART_ACTIVITY_PATHに等しければ、Activityを起動する
        if (common.START_ACTIVITY_PATH.equals(messageEvent.getPath())) {   //…… 3
            Intent intent = new Intent(this, WatchMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            DataItem dataItem = event.getDataItem();
            if (common.MESSAGE_PATH.equals(dataItem.getUri().getPath())) {   //…… 1
                DataMap dataMap = DataMapItem.fromDataItem(dataItem).getDataMap();   //…… 2
                String message = dataMap.getString(common.MESSAGE_KEY);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
