/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.bluetoothadvertisements;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.ListFragment;

import java.util.concurrent.TimeUnit;

import io.nodle.sdk.Nodle;
import io.nodle.sdk.api.exposed.NodleApi;

//import io.nodle.sdk.INodle;
//import static io.nodle.sdk.Nodle.Nodle;


/**
 * Scans for Bluetooth Low Energy devices and report.
 */
public class ScannerFragment extends ListFragment {

    private static final String TAG = ScannerFragment.class.getSimpleName();
    //private INodle nodle = Nodle();

    /**
     * Check status after 5 seconds.
     */
    private static final long SCAN_PERIOD = 5000;

    private BluetoothAdapter mBluetoothAdapter;

    private Handler mHandler;

    /**
     * Must be called after object creation by MainActivity.
     *
     * @param btAdapter the local BluetoothAdapter
     */
    public void setBluetoothAdapter(BluetoothAdapter btAdapter) {
        this.mBluetoothAdapter = btAdapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        mHandler = new Handler();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getListView().setDivider(null);
        getListView().setDividerHeight(0);

        setEmptyText(getString(R.string.empty_list));

        // Trigger refresh on app's 1st load
        startScanning();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.scanner_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.refresh:
                startScanning();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Start scanning for BLE Advertisements.
     */
    public void startScanning() {
        Log.d(TAG, "Starting Scanning");

        // Will stop the scanning after a set time.
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scannerStatus();
            }
        }, SCAN_PERIOD);

        // Start nodle.
        try {
            Context context = getActivity().getApplicationContext();
            getPref(context, "");
            //nodle.stop(context);
            //Nodle().config(context, "ble.scan.duration-msec", 50000);
            //Nodle().config(context, "ble.scan.interval-msec", 8000);
            //Nodle().config(context, "ble.scan.interval-x-factor", 1);
            //Nodle().config(context, "dtn.use-cellular", true);
            //nodle.start(context, Constants.NodleDevKey);
            NodleApi nodleApi = Nodle.with(context, Constants.NodleDevKey);
        } catch (Exception ex) {
            Log.e(TAG, "Exception!", ex);
        }


        String toastText = getString(R.string.scan_start_toast) + " "
                + TimeUnit.SECONDS.convert(SCAN_PERIOD, TimeUnit.MILLISECONDS) + " "
                + getString(R.string.seconds);
        Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG).show();
    }

    public static void setPref(Context context, String config) {
        SharedPreferences.Editor pref = context.getSharedPreferences("NODLE_SDK_PREFS", Context.MODE_PRIVATE).edit();
        pref.putString("PREF_PARAM_CONFIG", config);
        pref.apply();
    }

    public static void getPref(Context context, String config) {
        SharedPreferences sharedPref = context.getSharedPreferences("NODLE_SDK_PREFS", Context.MODE_PRIVATE);
        String defaultValue = sharedPref.getString("PREF_PARAM_CONFIG", "");
        Log.d(TAG, "PREF_PARAM_CONFIG: " + defaultValue);
    }

    /**
     * Check the scanner status.
     */
    public void scannerStatus() {
        Log.d(TAG, "Checking scanner status.");

        StringBuilder toastText = new StringBuilder();
        //toastText.append("Started: " +  nodle.isStarted() + " ");
        //toastText.append("Scanning: " +  nodle.isScanning() + " ");
        Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG).show();
    }
}
