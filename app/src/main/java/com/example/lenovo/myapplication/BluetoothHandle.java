package com.example.lenovo.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

import static android.bluetooth.BluetoothProfile.STATE_CONNECTED;
import static android.bluetooth.BluetoothProfile.STATE_CONNECTING;
import static android.bluetooth.BluetoothProfile.STATE_DISCONNECTED;
import static android.bluetooth.BluetoothProfile.STATE_DISCONNECTING;

public class BluetoothHandle {
    
    
    private final String tag = "KE,Junxing";
    private static final UUID mentionNotify_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    private static final UUID serviceBT_UUID = UUID.fromString("F000AA80-0451-4000-4000-000000000000");
    private static final UUID mentionBT_UUID = UUID.fromString("F000AA81-0451-4000-B000-000000000000");
    private static final UUID configureBT_UUID = UUID.fromString("F000AA82-0451-4000-B000-000000000000");


    private BluetoothAdapter BTAdapter;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothGattCharacteristic mentionCharacteristic;
    private BluetoothGattCharacteristic configureCharacteristic;
    private BluetoothGattDescriptor mDescriptor;

    private ListView mListView;
    private List<BluetoothDevice> mStringList;
    private ListAdapter mListAdapter;
    
    
//    public void initBluetoothAdapter() {
//        final BluetoothManager mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
//
//
//        BTAdapter = mBluetoothManager.getAdapter();
//        // Phone does not support Bluetooth so let the user know and exit.
//        if (BTAdapter == null) {
//            Toast.makeText(getBaseContext(), "Your phone does not support Bluetooth", Toast.LENGTH_LONG);
//            Log.d(tag, "Your phone does not support Bluetooth");
//            return;
//        }
//        // open phone`s bluetooth
//        if (!BTAdapter.isEnabled()) {
//            Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivity(enabler);
//            BTAdapter.enable();
//        }
//        if (BTAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
//            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//            i.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
//            startActivity(i);
//        }
//
//    }

    public void scanAction() {

        BTAdapter.startLeScan(mScanCallback);
    }

    private BluetoothAdapter.LeScanCallback mScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int rss, byte[] bytes) {
            Log.d(tag, bluetoothDevice.getName() + "  " + bluetoothDevice.getAddress() + "  " + rss);
            if (bluetoothDevice != null) {
                if (!mStringList.contains(bluetoothDevice)) {
                    mStringList.add(bluetoothDevice);
                    Log.d(tag, bluetoothDevice.getAddress());
                    mListAdapter.notifyDataSetChanged();
                }
            }

        }
    };


    private void connectAction() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BTAdapter.stopLeScan(mScanCallback);

                BluetoothDevice BTDevice = (BluetoothDevice) adapterView.getItemAtPosition(i);
                Log.d(tag, "Paring device " + BTDevice.getAddress());
//                mBluetoothGatt = BTDevice.connectGatt(this, false, getCallback);

            }
        });
    }
    
    
    
    public BluetoothGattCallback getCallback = new BluetoothGattCallback() {
        @Override
        public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
            super.onPhyUpdate(gatt, txPhy, rxPhy, status);

            Log.d(tag, "onPhyUpdate");
        }

        @Override
        public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
            super.onPhyRead(gatt, txPhy, rxPhy, status);

            Log.d(tag, "onPhyRead");
        }


        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, final int newState) {
            super.onConnectionStateChange(gatt, status, newState);

//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    switch (newState) {
//                        case STATE_CONNECTED:
//                            Log.d(tag, "STATE_CONNECTED");
//                            mTextView.setText(R.string.STATE_CONNECTED);
//                            HomeTitleString = "STATE_CONNECTED";
//                            //discoverServices from device
//                            mBluetoothGatt.discoverServices();
//
//                            break;
//                        case STATE_CONNECTING:
//                            Log.d(tag, "STATE_CONNECTING");
//                            mTextView.setText(R.string.STATE_CONNECTING);
//                            break;
//                        case STATE_DISCONNECTED:
//                            Log.d(tag, "STATE_DISCONNECTED");
//                            mTextView.setText(R.string.STATE_DISCONNECTED);
//                            break;
//                        case STATE_DISCONNECTING:
//                            Log.d(tag, "STATE_DISCONNECTING");
//                            mTextView.setText(R.string.STATE_DISCONNECTING);
//                            break;
//                        default:
//                            break;
//                    }
//                }
//            });


        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);

            if (status == mBluetoothGatt.GATT_SUCCESS) {
                List<BluetoothGattService> servers = mBluetoothGatt.getServices();
                for (BluetoothGattService bluetoothGattService : servers) {
                    Log.d(tag, "Service " + bluetoothGattService.getUuid().toString());
                    List<BluetoothGattCharacteristic> Characteristics = bluetoothGattService.getCharacteristics();

                    for (BluetoothGattCharacteristic bluetoothGattCharacteristic : Characteristics) {
                        Log.d(tag, "  Chars " + bluetoothGattCharacteristic.getUuid().toString());
                        if (bluetoothGattCharacteristic.getUuid().equals(mentionBT_UUID)) {
                            mentionCharacteristic = bluetoothGattCharacteristic;
                            mDescriptor = mentionCharacteristic.getDescriptor(mentionNotify_UUID);
                        } else if (bluetoothGattCharacteristic.getUuid().equals(configureBT_UUID)) {
                            configureCharacteristic = bluetoothGattCharacteristic;

                        }

                    }

                }
                
            }

        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);

            if (status == mBluetoothGatt.GATT_SUCCESS) {
                if (characteristic.getUuid().toString().equals(mentionCharacteristic.getUuid().toString())) {
//                    sensorMpu9250Convert(characteristic);

                    Log.d(tag, "onCharacteristicRead SUCCESS");
                    gatt.readCharacteristic(characteristic);

                } else {
                    Log.d(tag, "onCharacteristicRead was run but the status is not GATT SUCCESS " + status);
                }
            }


        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.d(tag, "onCharacteristicWrite " + status);


            if (status == mBluetoothGatt.GATT_SUCCESS) {
                Log.d(tag, "status == mBluetoothGatt.GATT_SUCCESS " + status);
                if (characteristic.getUuid().toString().equals(configureCharacteristic.getUuid().toString())) {
                    Log.d(tag, "configureCharacteristic onCharacteristicWrite SUCCESS.");
                }
            } else if (status == mBluetoothGatt.GATT_INVALID_ATTRIBUTE_LENGTH) {
                Log.d(tag, "status == mBluetoothGatt.GATT_INVALID_ATTRIBUTE_LENGTH " + status);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);

            Log.d(tag, "onCharacteristicChanged");
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);

            Log.d(tag, "onDescriptorRead run");

            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(tag, "onDescriptorRead successfully");
            } else {
                Log.d(tag, "onDescriptorRead failure " + status);
            }
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);

            Log.d(tag, "onDescriptorWrite run");

            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(tag, "onDescriptorWrite successfully");
            } else {
                Log.d(tag, "onDescriptorWrite failure " + status);
            }

        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);


        }


    };
}
