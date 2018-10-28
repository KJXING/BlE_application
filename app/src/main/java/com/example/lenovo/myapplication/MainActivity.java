package com.example.lenovo.myapplication;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LongDef;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.LoginFilter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static android.bluetooth.BluetoothProfile.STATE_CONNECTED;
import static android.bluetooth.BluetoothProfile.STATE_CONNECTING;
import static android.bluetooth.BluetoothProfile.STATE_DISCONNECTED;
import static android.bluetooth.BluetoothProfile.STATE_DISCONNECTING;


public class MainActivity extends AppCompatActivity implements SensorEventListener {


    private final String tag = "KE,Junxing";
    private static final UUID mentionNotify_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    private static final UUID serviceBT_UUID = UUID.fromString("F000AA80-0451-4000-4000-000000000000");
    private static final UUID mentionBT_UUID = UUID.fromString("F000AA81-0451-4000-B000-000000000000");
    private static final UUID configureBT_UUID = UUID.fromString("F000AA82-0451-4000-B000-000000000000");


    private SensorManager mSensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;
    private String HomeTitleString = null;


    private BluetoothAdapter BTAdapter;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothGattCharacteristic mentionCharacteristic;
    private BluetoothGattCharacteristic configureCharacteristic;
    private BluetoothGattDescriptor mDescriptor;
    private TextView mTextView;
    private TextView sensorTagTitle;
    private TextView acc_x_TV;
    private TextView acc_y_TV;
    private TextView acc_z_TV;
    private TextView gyro_x_TV;
    private TextView gyro_y_TV;
    private TextView gyro_z_TV;
    private TextView sensorTag_acc_x_TV;
    private TextView sensorTag_acc_y_TV;
    private TextView sensorTag_acc_z_TV;
    private TextView sensorTag_gyro_x_TV;
    private TextView sensorTag_gyro_y_TV;
    private TextView sensorTag_gyro_z_TV;
    private TextView sensorTag_mag_x_TV;
    private TextView sensorTag_mag_y_TV;
    private TextView sensorTag_mag_z_TV;



    private Button btnNotify;
    private Button btnEnableSensor;
    private Button btnReadData;


    private ListView mListView;
    private List<BluetoothDevice> mStringList;
    private ListAdapter mListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        iniMainActivityLayout();

        iniData();
        iniAdapter();
        iniSensor();
        initBluetoothAdapter();

        scanAction();
        connectAction();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void iniMainActivityLayout(){
        //find view by id
        mTextView=findViewById(R.id.tv_title_top);
        sensorTagTitle = findViewById(R.id.tv_title_sensorTag);

        acc_x_TV = findViewById(R.id.x_acc);
        acc_y_TV = findViewById(R.id.y_acc);
        acc_z_TV = findViewById(R.id.z_acc);
        gyro_x_TV = findViewById(R.id.x_gyro);
        gyro_y_TV = findViewById(R.id.y_gyro);
        gyro_z_TV = findViewById(R.id.z_gyro);

        sensorTag_acc_x_TV = findViewById(R.id.sensorTag_x_acc);
        sensorTag_acc_y_TV = findViewById(R.id.sensorTag_y_acc);
        sensorTag_acc_z_TV = findViewById(R.id.sensorTag_z_acc);
        sensorTag_gyro_x_TV = findViewById(R.id.sensorTag_x_Gyro);
        sensorTag_gyro_y_TV = findViewById(R.id.sensorTag_y_Gyro);
        sensorTag_gyro_z_TV = findViewById(R.id.sensorTag_z_Gyro);
        sensorTag_mag_x_TV = findViewById(R.id.sensorTag_x_Mag);
        sensorTag_mag_y_TV = findViewById(R.id.sensorTag_y_Mag);
        sensorTag_mag_z_TV = findViewById(R.id.sensorTag_z_Mag);

        btnEnableSensor = findViewById(R.id.btn_enableSensor);
        btnNotify = findViewById(R.id.btn_enableNotify);
        btnReadData = findViewById(R.id.btn_readData);

        mListView = findViewById(R.id.LV_activtyMain);

        //set ini Text
        mTextView.setText("Home Page");

        //set ini visibility
        sensorTagTitle.setVisibility(View.GONE);
        acc_x_TV.setVisibility(View.GONE);
        acc_y_TV.setVisibility(View.GONE);
        acc_z_TV.setVisibility(View.GONE);
        gyro_x_TV.setVisibility(View.GONE);
        gyro_y_TV.setVisibility(View.GONE);
        gyro_z_TV.setVisibility(View.GONE);

        sensorTag_acc_x_TV.setVisibility(View.GONE);
        sensorTag_acc_y_TV.setVisibility(View.GONE);
        sensorTag_acc_z_TV.setVisibility(View.GONE);
        sensorTag_gyro_x_TV.setVisibility(View.GONE);
        sensorTag_gyro_y_TV.setVisibility(View.GONE);
        sensorTag_gyro_z_TV.setVisibility(View.GONE);
        sensorTag_mag_x_TV.setVisibility(View.GONE);
        sensorTag_mag_y_TV.setVisibility(View.GONE);
        sensorTag_mag_z_TV.setVisibility(View.GONE);

        displayBtn(false);

        btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result_enableNotification = enableNotification(mBluetoothGatt,mDescriptor);
                if (result_enableNotification){
                    btnNotify.setText("Notify On");
                } else {
                    btnNotify.setText("Notify Off");
                }
            }
        });

        btnEnableSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               boolean result_enableMovement = enableMovement(mBluetoothGatt,configureCharacteristic);
                if (result_enableMovement){
                    btnEnableSensor.setText("Sensor On");
                } else {
                    btnEnableSensor.setText("Sensor Off");
                }
            }
        });

        btnReadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result_readCharacteristicValue = readCharacteristicValue(mBluetoothGatt,mentionCharacteristic);
//                while (result_readCharacteristicValue){
//                    result_readCharacteristicValue = readCharacteristicValue(mBluetoothGatt,mentionCharacteristic);
//                }
            }
        });

    }

    private void iniSensor(){
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this,gyroscope,SensorManager.SENSOR_DELAY_NORMAL);

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        int sensorType = event.sensor.getType();

        switch (sensorType){
            case Sensor.TYPE_GYROSCOPE:
                gyro_x_TV.setText("gyro_x: " + values[0]);
                gyro_y_TV.setText("gyro_y: " + values[1]);
                gyro_z_TV.setText("gyro_z: " + values[2]);
                break;

            case Sensor.TYPE_ACCELEROMETER:
                acc_x_TV.setText("x_Acc: " + values[0]);
                acc_y_TV.setText("y_Acc: " + values[1]);
                acc_z_TV.setText("z_Acc: " + values[2]);
                break;
            default:
                break;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



    private void initBluetoothAdapter(){
        final BluetoothManager  mBluetoothManager =  (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BTAdapter = mBluetoothManager.getAdapter();
        // Phone does not support Bluetooth so let the user know and exit.
        if (BTAdapter == null){
            Toast.makeText(getBaseContext(),"Your phone does not support Bluetooth",Toast.LENGTH_LONG);
            Log.d(tag,"Your phone does not support Bluetooth");
            return;
        }
        // open phone`s bluetooth
        if (!BTAdapter.isEnabled()) {
            Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enabler);
            BTAdapter.enable();
        }
        if (BTAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            i.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,0);
            startActivity(i);
        }

    }

    public void scanAction(){
        mTextView.setText("Scan BT Device");
        BTAdapter.startLeScan(mScanCallback);
    }

    private BluetoothAdapter.LeScanCallback mScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int rss, byte[] bytes) {
            Log.d(tag,bluetoothDevice.getName() + "  " + bluetoothDevice.getAddress() + "  " + rss);
            if (bluetoothDevice != null) {
                if (!mStringList.contains(bluetoothDevice)) {
                    mStringList.add(bluetoothDevice);
                    Log.d(tag, bluetoothDevice.getAddress());
                    mListAdapter.notifyDataSetChanged();
                }
            }

        }
    };

    private void connectAction(){
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BTAdapter.stopLeScan(mScanCallback);

                BluetoothDevice BTDevice = (BluetoothDevice) adapterView.getItemAtPosition(i);
                Log.d(tag,"Paring device " + BTDevice.getAddress());
                mBluetoothGatt = BTDevice.connectGatt(MainActivity.this,false,getCallback);

            }
        });
    }

    private BluetoothGattCallback getCallback = new BluetoothGattCallback() {
        @Override
        public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
            super.onPhyUpdate(gatt, txPhy, rxPhy, status);

            Log.d(tag,"onPhyUpdate");
        }

        @Override
        public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
            super.onPhyRead(gatt, txPhy, rxPhy, status);

            Log.d(tag,"onPhyRead");
        }


        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, final int newState) {
            super.onConnectionStateChange(gatt, status, newState);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (newState){
                        case STATE_CONNECTED:
                            Log.d(tag,"STATE_CONNECTED");
                            mTextView.setText(R.string.STATE_CONNECTED);
                            HomeTitleString = "STATE_CONNECTED";
                            //discoverServices from device
                            mBluetoothGatt.discoverServices();

                            break;
                        case STATE_CONNECTING:
                            Log.d(tag,"STATE_CONNECTING");
                            mTextView.setText(R.string.STATE_CONNECTING);
                            break;
                        case STATE_DISCONNECTED:
                            Log.d(tag,"STATE_DISCONNECTED");
                            mTextView.setText(R.string.STATE_DISCONNECTED);
                            break;
                        case STATE_DISCONNECTING:
                            Log.d(tag,"STATE_DISCONNECTING");
                            mTextView.setText(R.string.STATE_DISCONNECTING);
                            break;
                        default:
                            break;
                    }
                }
            });


        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);

            if (status == mBluetoothGatt.GATT_SUCCESS){
               List<BluetoothGattService> servers = mBluetoothGatt.getServices();
               for (BluetoothGattService bluetoothGattService: servers){
                   Log.d(tag,"Service " + bluetoothGattService.getUuid().toString());
                   List<BluetoothGattCharacteristic> Characteristics = bluetoothGattService.getCharacteristics();

                   for (BluetoothGattCharacteristic bluetoothGattCharacteristic: Characteristics){
                       Log.d(tag,"  Chars " + bluetoothGattCharacteristic.getUuid().toString());
                       if (bluetoothGattCharacteristic.getUuid().equals(mentionBT_UUID)){
                           mentionCharacteristic = bluetoothGattCharacteristic;
                           mDescriptor = mentionCharacteristic.getDescriptor(mentionNotify_UUID);
                       } else if (bluetoothGattCharacteristic.getUuid().equals(configureBT_UUID)){
                           configureCharacteristic =bluetoothGattCharacteristic;

                       }

                   }

               }

            mTextView.setText(R.string.Title_sensorTag_status_RFC);
            }

        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);

            if (status == mBluetoothGatt.GATT_SUCCESS){
                if (characteristic.getUuid().toString().equals(mentionCharacteristic.getUuid().toString() )){
                    sensorMpu9250AccConvert(characteristic);
                    Log.d(tag,"onCharacteristicRead SUCCESS" );
                } else {
                    Log.d(tag,"onCharacteristicRead was run but the status is not GATT SUCCESS " + status);
                }
            }


        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.d(tag,"onCharacteristicWrite " + status);


            if (status == mBluetoothGatt.GATT_SUCCESS){
                Log.d(tag,"status == mBluetoothGatt.GATT_SUCCESS " + status);
                if (characteristic.getUuid().toString().equals(configureCharacteristic.getUuid().toString())){
                    Log.d(tag,"configureCharacteristic onCharacteristicWrite SUCCESS.");
                }
            }else if (status == mBluetoothGatt.GATT_INVALID_ATTRIBUTE_LENGTH){
                Log.d(tag,"status == mBluetoothGatt.GATT_INVALID_ATTRIBUTE_LENGTH " + status);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);

            Log.d(tag,"onCharacteristicChanged");
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);

            Log.d(tag,"onDescriptorRead run");

            if (status == BluetoothGatt.GATT_SUCCESS){
                Log.d(tag,"onDescriptorRead successfully");
            } else {
                Log.d(tag,"onDescriptorRead failure " + status);
            }
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);

            Log.d(tag,"onDescriptorWrite run");

            if (status == BluetoothGatt.GATT_SUCCESS){
                Log.d(tag,"onDescriptorWrite successfully");
            } else {
                Log.d(tag,"onDescriptorWrite failure " + status);
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



    //Run successfully
    private boolean readCharacteristicValue(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic){

        boolean result = gatt.readCharacteristic(characteristic);

        if (result){
            Log.d(tag,"readCharacteristic success");
        }else{
            Log.d(tag,"readCharacteristic failure");
        }

        return result;

    }

    private boolean enableNotification(BluetoothGatt gatt, BluetoothGattDescriptor descriptor){
        boolean result = false;
        Log.d(tag,"enableNotification called");

        if (descriptor != null){
            result = descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE );

        } else {
            Log.d(tag,"enableNotification setValue descriptor == null");
        }


        Log.d(tag,"enableNotification ENABLE_NOTIFICATION_VALUE result " + result);
        if (result){
            Log.d(tag,"set enableNotification ENABLE_NOTIFICATION_VALUE value true");
        } else {
            Log.d(tag,"set enableNotification ENABLE_NOTIFICATION_VALUE value false");
        }

        boolean WD_result = gatt.writeDescriptor(descriptor);

        Log.d(tag,"enableNotification has execute");

        return WD_result;
    }

    private boolean enableMovement(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic){
        characteristic.setValue(new byte[] {(byte) 0xfe,(byte) 0x80 });
        boolean result = gatt.writeCharacteristic(characteristic);

        if (result){
           Log.d(tag,"writeCharacteristic success");
        } else {
            Log.d(tag,"writeCharacteristic failure");
        }
        Log.d(tag,"configure value: "+ characteristic.getValue().toString());
        Log.d(tag,"enableMovement finished.");

        return result;
    }


    private void iniData(){
        mStringList = new ArrayList();
    }

    private void iniAdapter(){
        mListAdapter = new ListAdapter(this, mStringList);
        mListView.setAdapter(mListAdapter);
    }

    private void displaySensorData(boolean DisplaySwitch){
        if (DisplaySwitch){
            sensorTagTitle.setVisibility(View.VISIBLE);

            acc_x_TV.setVisibility(View.VISIBLE);
            acc_y_TV.setVisibility(View.VISIBLE);
            acc_z_TV.setVisibility(View.VISIBLE);

            gyro_x_TV.setVisibility(View.VISIBLE);
            gyro_y_TV.setVisibility(View.VISIBLE);
            gyro_z_TV.setVisibility(View.VISIBLE);

            sensorTag_acc_x_TV.setVisibility(View.VISIBLE);
            sensorTag_acc_y_TV.setVisibility(View.VISIBLE);
            sensorTag_acc_z_TV.setVisibility(View.VISIBLE);

            sensorTag_gyro_x_TV.setVisibility(View.VISIBLE);
            sensorTag_gyro_y_TV.setVisibility(View.VISIBLE);
            sensorTag_gyro_z_TV.setVisibility(View.VISIBLE);

            sensorTag_mag_x_TV.setVisibility(View.VISIBLE);
            sensorTag_mag_y_TV.setVisibility(View.VISIBLE);
            sensorTag_mag_z_TV.setVisibility(View.VISIBLE);
        } else {
            sensorTagTitle.setVisibility(View.GONE);

            acc_x_TV.setVisibility(View.GONE);
            acc_y_TV.setVisibility(View.GONE);
            acc_z_TV.setVisibility(View.GONE);

            gyro_x_TV.setVisibility(View.GONE);
            gyro_y_TV.setVisibility(View.GONE);
            gyro_z_TV.setVisibility(View.GONE);

            sensorTag_acc_x_TV.setVisibility(View.GONE);
            sensorTag_acc_y_TV.setVisibility(View.GONE);
            sensorTag_acc_z_TV.setVisibility(View.GONE);

            sensorTag_gyro_x_TV.setVisibility(View.GONE);
            sensorTag_gyro_y_TV.setVisibility(View.GONE);
            sensorTag_gyro_z_TV.setVisibility(View.GONE);

            sensorTag_mag_x_TV.setVisibility(View.GONE);
            sensorTag_mag_y_TV.setVisibility(View.GONE);
            sensorTag_mag_z_TV.setVisibility(View.GONE);
        }




    }

    private void displayBTDeviceList(boolean DisplaySwitch){

        if (DisplaySwitch){
            mListView.setVisibility(View.VISIBLE);
        } else {
            mListView.setVisibility(View.GONE);
        }

    }
    private void displayBtn(boolean DisplaySwitch){
        if (DisplaySwitch){
            btnNotify.setVisibility(View.VISIBLE);
            btnEnableSensor.setVisibility(View.VISIBLE);
            btnReadData.setVisibility(View.VISIBLE);
        } else {
            btnNotify.setVisibility(View.GONE);
            btnEnableSensor.setVisibility(View.GONE);
            btnReadData.setVisibility(View.GONE);
        }
    }

    private void sensorMpu9250AccConvert(BluetoothGattCharacteristic BTCharacteristic){

        byte [] value = BTCharacteristic.getValue();

        Log.d(tag,"sensorMpu9250AccConvert byte value= " + value.toString());
        if (BTCharacteristic.equals(mentionCharacteristic)){
            Point3D value_acc;
            Point3D value_gyro;
            Point3D value_mag;

            SensorConvert sensorConvert = new SensorConvert();
            value_acc = sensorConvert.accConvert(value);
            value_gyro = sensorConvert.gyroConvert(value);
            value_mag = sensorConvert.magConvert(value);


            sensorTag_acc_x_TV.setText("x_Acc: " + Double.toString(value_acc.x));
            sensorTag_acc_y_TV.setText("y_Acc: " + Double.toString(value_acc.y));
            sensorTag_acc_z_TV.setText("z_Acc: " + Double.toString(value_acc.z));
            sensorTag_gyro_x_TV.setText("x_Gyro: " + Double.toString(value_gyro.x));
            sensorTag_gyro_y_TV.setText("y_Gyro: " + Double.toString(value_gyro.y));
            sensorTag_gyro_z_TV.setText("z_Gyro: " + Double.toString(value_gyro.z));
            sensorTag_mag_x_TV.setText("x_Mag: " + Double.toString(value_mag.x));
            sensorTag_mag_y_TV.setText("y_Mag: " + Double.toString(value_mag.y));
            sensorTag_mag_z_TV.setText("z_Mag: " + Double.toString(value_mag.z));


            Log.d(tag,"value_acc " + value_acc.x + "   " +  value_acc.y + "  " + value_acc.z);
            Log.d(tag,"value_gyro " + value_gyro.x + "   " + value_gyro.y + "   " + value_gyro.z);
            Log.d(tag,"value_mag " + value_mag.x + "   " + value_mag.y + "   " + value_mag.z);
            
        }
    }












    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (HomeTitleString != null){
                        mTextView.setText(HomeTitleString);
                    }
                    if (mTextView.getText() == "Dashboard"){
                        mTextView.setText(HomeTitleString);
                    } else if (mTextView.getText() == "Notifications"){
                        mTextView.setText(HomeTitleString);
                    }
                    displaySensorData(false);
                    displayBTDeviceList(true);
                    displayBtn(false);
                    return true;


                case R.id.navigation_dashboard:
                    if (!(mTextView.getText() == "Inner Sensor")){
                        HomeTitleString = mTextView.getText().toString();
                    } else if(!(mTextView.getText()== "Notifications")){
                        HomeTitleString = mTextView.getText().toString();
                    }

                    mTextView.setText(R.string.Title_inner_sensor);

                    displaySensorData(true);
                    displayBTDeviceList(false);
                    displayBtn(true);
                    return true;

                case R.id.navigation_notifications:
                    if (!(mTextView.getText() == "Dashboard")){
                        HomeTitleString = mTextView.getText().toString();
                    } else if(!(mTextView.getText()== "Notifications")){
                        HomeTitleString = mTextView.getText().toString();
                    }

                    mTextView.setText(R.string.title_notifications);

                    displayBTDeviceList(false);
                    displaySensorData(false);
                    displayBtn(false);
                    return true;
            }
            return false;
        }
    };

}


