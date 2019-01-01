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
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
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


import com.example.lenovo.myapplication.Model.AngleData;
import com.example.lenovo.myapplication.Model.FeatureData;
import com.example.lenovo.myapplication.Model.MentionData;
import com.example.lenovo.myapplication.Model.SorDfeatureData;
import com.example.lenovo.myapplication.Model.externalMention;
import com.example.lenovo.myapplication.Model.buildInMention;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static android.bluetooth.BluetoothProfile.STATE_CONNECTED;
import static android.bluetooth.BluetoothProfile.STATE_CONNECTING;
import static android.bluetooth.BluetoothProfile.STATE_DISCONNECTED;
import static android.bluetooth.BluetoothProfile.STATE_DISCONNECTING;


public class MainActivity extends AppCompatActivity   {


    private final String tag = "KE,Junxing";
    private final String tag1 = "realm:";
    private String posture_realm = "";
    private String posture = "";

    private static final UUID mentionNotify_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    private static final UUID serviceBT_UUID = UUID.fromString("F000AA80-0451-4000-4000-000000000000");
    private static final UUID mentionBT_UUID = UUID.fromString("F000AA81-0451-4000-B000-000000000000");
    private static final UUID configureBT_UUID = UUID.fromString("F000AA82-0451-4000-B000-000000000000");
    private static final UUID SetPeriod_UUID = UUID.fromString("F000AA83-0451-4000-B000-000000000000");

    private Thread BISensorThread;
    private Thread EXSensorThread;
    private Thread detectionThread;
    private Thread dataProcessThread;
    private Thread fallDataProcessThread;


    private SensorManager mSensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;
    private Sensor magnetometer;
    private String HomeTitleString = null;


    private BluetoothAdapter BTAdapter;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothGattCharacteristic mentionCharacteristic;
    private BluetoothGattCharacteristic configureCharacteristic;
    private BluetoothGattCharacteristic SetPeriodCharacteristic;
    private BluetoothGattDescriptor mDescriptor;
    private TextView mTextView;
    private TextView activityStatus;
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

    private boolean switch_notify = false;
    private boolean switch_BISensor = false;
    private boolean switch_saveEX = false;
    private boolean switch_detection = false;
    private boolean clickOnce = false;
    private boolean btConnectionStatus = false;
    private boolean result_fall = false;

    private MediaPlayer dynamic_Player;
    private MediaPlayer static_Player;
    private MediaPlayer standing_Player;
    private MediaPlayer sitting_Player;
    private MediaPlayer lying_Player;
    private MediaPlayer bend_Player;
    private MediaPlayer falling_Player;
    private MediaPlayer running_Player;
    private MediaPlayer walking_Player;
    private MediaPlayer other_Player;



    private Realm realm;

    private Button btnNotify;
    private Button btnEnableSensor;
    private Button btnReadData;
    private Button btnStop;
    private Button btnSave;
    private Button btnExport;
    private Button btnDetection;

    private double[] posture_q = {0,0};
    private double R_A_BI_q;
    private double R_A_EX_q;
    private double R_W_BI_q;
    private double R_W_EX_q;



    private double[] sensorDataTemp = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
    private double[][] rawData = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},

            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},

            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},

            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},

            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},

            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    };
    private double[][] averageValue = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},

            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},

            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},

            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},

            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},

            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    };
    private double[][] featureValue = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},

            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    };
    private double[][] featureValueForFallDec = {
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},

            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    };

    private ListView mListView;
    private List<BluetoothDevice> mStringList;
    private ListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getDefaultInstance();



        iniMainActivity();
        iniMediaPlayer();
        iniListData();
        iniListAdapter();
        initBluetoothAdapter();


        btnSetOnClickListener();

        scanAction();
        connectAction();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    protected void onResume() {
        super.onResume();

        iniThread();

        BISensorThread.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void iniThread(){

        dataProcessThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true){
                        pushStackRowData(rawData,sensorDataTemp);
                        calAverageValue(rawData);
                        calFeatureValue(rawData);
                        Thread.sleep(85);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        fallDataProcessThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    while (true){

                        AccGyroPushStackFeatureValue(rawData);
                        Thread.sleep(70);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        detectionThread =  new Thread(new Runnable() {

            @Override
            public void run() {
                boolean result = false;
                String dynamicStatus;

                try {

                    while (true){

                        result = staticOrDynamicDetection(featureValue);
                        posture = postureDetection(rawData);
                        result_fall = fallDetection(featureValueForFallDec);


                        Log.d(tag,"static:"+ result);
                        if (result){
                            activityStatus.setText("Static Postures");

                            if(posture == "standing"){
                                standing_Player.start();
                            } else if (posture == "bending"){
                                bend_Player.start();
                            } else if (posture == "sitting"){
                                sitting_Player.start();
                            } else if (posture == "lying" && !result_fall ){
                                lying_Player.start();
                            } else if (posture == "lying" && result_fall){
                                falling_Player.start();
                            }
                            posture_realm = posture;
                        } else {
                            activityStatus.setText("Dynamic Transitions");
                            dynamicStatus = dynamicDetection(featureValueForFallDec);

                            if (posture == "lying" && result_fall){
                                falling_Player.start();
                            } else if (posture == "standing" && dynamicStatus == "walking"){
                                walking_Player.start();
                            } else if (posture == "standing" && dynamicStatus == "running"){
                                running_Player.start();
                            } else {
//                                dynamic_Player.start();
                            }

                            posture_realm = dynamicStatus;


                        }



                        Thread.sleep(75);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        BISensorThread = new Thread(()->{
            iniSensor();
        });

    }

    private void iniMainActivity() {
        //TextView
        mTextView = findViewById(R.id.tv_title_top);
        sensorTagTitle = findViewById(R.id.tv_title_sensorTag);
        activityStatus = findViewById(R.id.tv_status);

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

        //Button
        btnEnableSensor = findViewById(R.id.btn_enableSensor);
        btnNotify = findViewById(R.id.btn_enableNotify);
        btnReadData = findViewById(R.id.btn_readData);
        btnStop = findViewById(R.id.btn_stopReadData);
        btnExport = findViewById(R.id.btn_exportData);
        btnSave = findViewById(R.id.btn_saveData);
        btnDetection = findViewById(R.id.btn_detection);

        //ListView
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


    }

    private void iniMediaPlayer(){
        dynamic_Player = MediaPlayer.create(this,R.raw.dynamic_gb_1);
        static_Player = MediaPlayer.create(this,R.raw.static_gb_1);
        standing_Player = MediaPlayer.create(this,R.raw.standing_gb_1);
        sitting_Player = MediaPlayer.create(this,R.raw.sitting_gb_1);
        lying_Player = MediaPlayer.create(this,R.raw.lying_gb_1);
        bend_Player = MediaPlayer.create(this,R.raw.bend_gb_1);
        falling_Player = MediaPlayer.create(this,R.raw.falling_gb_1);
        running_Player = MediaPlayer.create(this,R.raw.running_gb_1);
        walking_Player = MediaPlayer.create(this,R.raw.walking_gb_1);
        other_Player = MediaPlayer.create(this,R.raw.other_gb_1);
    }

    private void btnSetOnClickListener(){

        btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!switch_notify) {
                    enableNotification(mBluetoothGatt, mDescriptor);
                    btnNotify.setText("N On");
                    switch_notify = true;
                } else {
                    btnNotify.setText("N Off");
                    switch_notify = false;
                }
            }
        });

        btnEnableSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result_enableMovement = enableMovement(mBluetoothGatt, configureCharacteristic);
                SetPeriod(mBluetoothGatt,SetPeriodCharacteristic);
                if (result_enableMovement) {
                    btnEnableSensor.setText("S On");
                } else {
                    btnEnableSensor.setText("S Off");
                }
            }
        });

        btnReadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickOnce) {
                    Toast.makeText(MainActivity.this, "只能点击一次，数据正在跑，去看数据日志", Toast.LENGTH_SHORT).show();
                    return;
                }
                clickOnce = true;

                new Thread(() -> {
                    Realm realm1 = Realm.getDefaultInstance();

                    while (true){
                        try {
                            readCharacteristicValue(mBluetoothGatt, mentionCharacteristic);
                            Log.d(tag,"sensorDataTemp:"+ Arrays.toString(sensorDataTemp));
                            Thread.sleep(90);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
                btConnectionStatus = true;
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!switch_BISensor){
                    mSensorManager.unregisterListener(mSensorEventListener);
                    switch_BISensor = true;
                    iniDeleteDataAtDatabase();

                    Toast.makeText(MainActivity.this, "delete successfully", Toast.LENGTH_SHORT).show();


                } else {
                    mSensorManager.registerListener(mSensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                    mSensorManager.registerListener(mSensorEventListener, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
                    mSensorManager.registerListener(mSensorEventListener, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
                    switch_BISensor = false;
                    Toast.makeText(MainActivity.this, "mSensorManager successfully", Toast.LENGTH_SHORT).show();
                }


            }
        });

        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!btConnectionStatus) {
                    Toast.makeText(MainActivity.this, "Please make sure connection is Ready", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(MainActivity.this, "Onclick btnExport", Toast.LENGTH_SHORT).show();
                exportRealmFile();

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!btConnectionStatus) {
                    Toast.makeText(MainActivity.this, "Please make sure connection is Ready", Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Realm realm1 = Realm.getDefaultInstance();
                        try {

                            while (true){
                                saveFeatureValue(realm1,System.currentTimeMillis(),featureValue[3][0],featureValue[0][0],featureValue[4][0],featureValue[1][0],posture_realm);
                                saveAngleData(realm1,System.currentTimeMillis(),posture_q[0],posture_q[1],posture);
                                saveSorDFeatureData(realm1,System.currentTimeMillis(),featureValueForFallDec[2][0],featureValueForFallDec[0][0],featureValueForFallDec[3][0],featureValueForFallDec[1][0],Boolean.toString(result_fall));
                                Thread.sleep(29);
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            realm.close();
                        }


                    }
                }).start();

            }
        });

        btnDetection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!btConnectionStatus) {
                    Toast.makeText(MainActivity.this, "Please make sure connection is Ready", Toast.LENGTH_SHORT).show();
                    return;
                }

                dataProcessThread.start();
                fallDataProcessThread.start();

                if(!switch_detection){
                    detectionThread.start();
                    switch_detection = true;
                    return;
                }

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorEventListener);
    }

    private void iniSensor() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        mSensorManager.registerListener(mSensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(mSensorEventListener, gyroscope, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(mSensorEventListener, magnetometer, SensorManager.SENSOR_DELAY_UI);

    }

    SensorEventListener mSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float[] values = sensorEvent.values;
            int sensorType = sensorEvent.sensor.getType();

            switch (sensorType) {
                case Sensor.TYPE_GYROSCOPE:
                    gyro_x_TV.setText("gyro_x: " + Math.toDegrees(values[0]));
                    gyro_y_TV.setText("gyro_y: " + Math.toDegrees(values[1]));
                    gyro_z_TV.setText("gyro_z: " + Math.toDegrees(values[2]));
                    sensorDataTemp[3] = Math.toDegrees(values[0]);
                    sensorDataTemp[4] = Math.toDegrees(values[1]);
                    sensorDataTemp[5] = Math.toDegrees(values[2]);
                    break;

                case Sensor.TYPE_ACCELEROMETER:
                    acc_x_TV.setText("x_Acc: " + values[0]);
                    acc_y_TV.setText("y_Acc: " + values[1]);
                    acc_z_TV.setText("z_Acc: " + values[2]);
                    sensorDataTemp[0] = values[0];
                    sensorDataTemp[1] = values[1];
                    sensorDataTemp[2] = values[2];
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    sensorDataTemp[6] = values[0];
                    sensorDataTemp[7] = values[1];
                    sensorDataTemp[8] = values[2];
                    break;

                default:
                    break;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    private void initBluetoothAdapter() {
        final BluetoothManager mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BTAdapter = mBluetoothManager.getAdapter();
        // Phone does not support Bluetooth so let the user know and exit.
        if (BTAdapter == null) {
            Toast.makeText(getBaseContext(), "Your phone does not support Bluetooth", Toast.LENGTH_LONG);
            Log.d(tag, "Your phone does not support Bluetooth");
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
            i.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
            startActivity(i);
        }

    }

    public void scanAction() {
        mTextView.setText("Scan BT Device");
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
                mBluetoothGatt = BTDevice.connectGatt(MainActivity.this, false, getCallback);

            }
        });
    }

    private BluetoothGattCallback getCallback = new BluetoothGattCallback() {
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

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (newState) {
                        case STATE_CONNECTED:
                            Log.d(tag, "STATE_CONNECTED");
                            mTextView.setText(R.string.STATE_CONNECTED);
                            HomeTitleString = "STATE_CONNECTED";
                            //discoverServices from device
                            mBluetoothGatt.discoverServices();

                            break;
                        case STATE_CONNECTING:
                            Log.d(tag, "STATE_CONNECTING");
                            mTextView.setText(R.string.STATE_CONNECTING);
                            break;
                        case STATE_DISCONNECTED:
                            Log.d(tag, "STATE_DISCONNECTED");
                            mTextView.setText(R.string.STATE_DISCONNECTED);
                            break;
                        case STATE_DISCONNECTING:
                            Log.d(tag, "STATE_DISCONNECTING");
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

                        }else if (bluetoothGattCharacteristic.getUuid().equals(SetPeriod_UUID)) {
                            SetPeriodCharacteristic = bluetoothGattCharacteristic;

                        }

                    }

                }

                mTextView.setText(R.string.Title_sensorTag_status_RFC);
            }

        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);

            if (status == mBluetoothGatt.GATT_SUCCESS) {
                if (characteristic.getUuid().toString().equals(mentionCharacteristic.getUuid().toString())) {
                    sensorMpu9250Convert(characteristic);

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


    //Run successfully
    private void readCharacteristicValue(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {

        boolean result = gatt.readCharacteristic(characteristic);

        if (result) {
            Log.d(tag, "readCharacteristic success");

        } else {
            Log.d(tag, "readCharacteristic failure");
        }

    }

    private void enableNotification(BluetoothGatt gatt, BluetoothGattDescriptor descriptor) {
        boolean result = false;
        Log.d(tag, "enableNotification called");

        if (descriptor != null) {
            result = descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);

        } else {
            Log.d(tag, "enableNotification setValue descriptor == null");
        }


        Log.d(tag, "enableNotification ENABLE_NOTIFICATION_VALUE result " + result);
        if (result) {
            Log.d(tag, "set enableNotification ENABLE_NOTIFICATION_VALUE value true");
        } else {
            Log.d(tag, "set enableNotification ENABLE_NOTIFICATION_VALUE value false");
        }

        gatt.writeDescriptor(descriptor);

    }

    private boolean enableMovement(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        characteristic.setValue(new byte[]{(byte) 0xff, (byte) 0x80});
        boolean result = gatt.writeCharacteristic(characteristic);

        if (result) {
            Log.d(tag, "writeCharacteristic success");
        } else {
            Log.d(tag, "writeCharacteristic failure");
        }
        Log.d(tag, "configure value: " + characteristic.getValue().toString());
        Log.d(tag, "enableMovement finished.");

        return result;
    }

    private void SetPeriod(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic){

        characteristic.setValue(new byte[]{(byte)0x0a});

        boolean result = gatt.writeCharacteristic(characteristic);

        if (result) {
            Log.d(tag, "SetPeriod writeCharacteristic success");
        } else {
            Log.d(tag, "SetPeriod writeCharacteristic failure");
        }

    }

    private void iniListData() {
        mStringList = new ArrayList();
    }

    private void iniListAdapter() {
        mListAdapter = new ListAdapter(this, mStringList);
        mListView.setAdapter(mListAdapter);
    }

    private void displaySensorData(boolean DisplaySwitch) {
        if (DisplaySwitch) {
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

    private void displayBTDeviceList(boolean DisplaySwitch) {

        if (DisplaySwitch) {
            mListView.setVisibility(View.VISIBLE);
        } else {
            mListView.setVisibility(View.GONE);
        }

    }

    private void displayBtn(boolean DisplaySwitch) {
        if (DisplaySwitch) {
            btnNotify.setVisibility(View.VISIBLE);
            btnEnableSensor.setVisibility(View.VISIBLE);
            btnReadData.setVisibility(View.VISIBLE);
            btnStop.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.VISIBLE);
            btnExport.setVisibility(View.VISIBLE);
            btnDetection.setVisibility(View.VISIBLE);


        } else {
            btnNotify.setVisibility(View.GONE);
            btnEnableSensor.setVisibility(View.GONE);
            btnReadData.setVisibility(View.GONE);
            btnStop.setVisibility(View.GONE);
            btnSave.setVisibility(View.GONE);
            btnExport.setVisibility(View.GONE);
            btnDetection.setVisibility(View.GONE);
        }
    }

    private void sensorMpu9250Convert(BluetoothGattCharacteristic BTCharacteristic) {

        byte[] value = BTCharacteristic.getValue();

        if (BTCharacteristic.equals(mentionCharacteristic)) {
            Point3D value_acc;
            Point3D value_gyro;
            Point3D value_mag;

            SensorConvert sensorConvert = new SensorConvert();
            value_acc = sensorConvert.accConvert(value);
            value_gyro = sensorConvert.gyroConvert(value);
            value_mag = sensorConvert.magConvert(value);


            sensorDataTemp[9] = value_acc.x * 10;
            sensorDataTemp[10] = value_acc.y * 10;
            sensorDataTemp[11] = value_acc.z * 10;
            sensorDataTemp[12] = value_gyro.x;
            sensorDataTemp[13] = value_gyro.y;
            sensorDataTemp[14] = value_gyro.z;
            sensorDataTemp[15] = value_mag.x;
            sensorDataTemp[16] = value_mag.y;
            sensorDataTemp[17] = value_mag.z;

            sensorTag_acc_x_TV.setText("x_Acc: " + Double.toString(sensorDataTemp[9]));
            sensorTag_acc_y_TV.setText("y_Acc: " + Double.toString(sensorDataTemp[10]));
            sensorTag_acc_z_TV.setText("z_Acc: " + Double.toString(sensorDataTemp[11]));
            sensorTag_gyro_x_TV.setText("x_Gyro: " + Double.toString(value_gyro.x));
            sensorTag_gyro_y_TV.setText("y_Gyro: " + Double.toString(value_gyro.y));
            sensorTag_gyro_z_TV.setText("z_Gyro: " + Double.toString(value_gyro.z));
            sensorTag_mag_x_TV.setText("x_Mag: " + Double.toString(value_mag.x));
            sensorTag_mag_y_TV.setText("y_Mag: " + Double.toString(value_mag.y));
            sensorTag_mag_z_TV.setText("z_Mag: " + Double.toString(value_mag.z));

        }


    }

    private void saveToDatabase(Realm realm, final long q_time, final double x_AccBI, final double y_AccBI, final double z_AccBI, final double x_GyroBI, final double y_GyroBI, final double z_GyroBI, final double x_MagBI, final double y_MagBI, final double z_MagBI, final double x_AccEX, final double y_AccEX, final double z_AccEX, final double x_GyroEX, final double y_GyroEX, final double z_GyroEX, final double x_MagEX, final double y_MagEX, final double z_MagEX, final String q_lable) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                MentionData mentionData = realm.createObject(MentionData.class);
                mentionData.setTimestamp(q_time);
                mentionData.setxAccBI(x_AccBI);
                mentionData.setyAccBI(y_AccBI);
                mentionData.setzAccBI(z_AccBI);
                mentionData.setxGyroBI(x_GyroBI);
                mentionData.setyGyroBI(y_GyroBI);
                mentionData.setzGyroBI(z_GyroBI);
                mentionData.setxMagBI(x_MagBI);
                mentionData.setyMagBI(y_MagBI);
                mentionData.setzMagEX(z_MagBI);

                mentionData.setxAccEx(x_AccEX);
                mentionData.setyAccEX(y_AccEX);
                mentionData.setzAccEX(z_AccEX);
                mentionData.setxGyroEX(x_GyroEX);
                mentionData.setyGyroEX(y_GyroEX);
                mentionData.setzGyroEX(z_GyroEX);
                mentionData.setxMagEx(x_MagEX);
                mentionData.setyMagEX(y_MagEX);
                mentionData.setzMagEX(z_MagEX);

                mentionData.setGestureState(q_lable);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(tag, "save to database success");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d(tag, "save to database error");
            }
        });
    }

    public void saveExternalMentionData(Realm realm, final long q_time, final double x_AccEX, final double y_AccEX, final double z_AccEX, final double x_GyroEX, final double y_GyroEX, final double z_GyroEX, final double x_MagEX, final double y_MagEX, final double z_MagEX, final String q_lable) {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                externalMention externalMention = realm.createObject(externalMention.class);
                externalMention.setTimestamp(q_time);
                externalMention.setxAccEx(x_AccEX);
                externalMention.setyAccEx(y_AccEX);
                externalMention.setzAccEx(z_AccEX);
                externalMention.setxGyroEx(x_GyroEX);
                externalMention.setyGyroEx(y_GyroEX);
                externalMention.setzGyroEx(z_GyroEX);
                externalMention.setxMagEx(x_MagEX);
                externalMention.setyMagEx(y_MagEX);
                externalMention.setzMagEx(z_MagEX);

                externalMention.setGestureState(q_lable);

                Log.d(tag,"---->saveExternalMentionData successfully<-----");
            }
        });
    }

    private void saveBuildInMentionData(Realm realm,final long q_time, final double x_AccBI, final double y_AccBI, final double z_AccBI, final double x_GyroBI, final double y_GyroBI, final double z_GyroBI, final double x_MagBI, final double y_MagBI, final double z_MagBI, final String q_lable) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                buildInMention buildInMention = realm.createObject(buildInMention.class);
                buildInMention.setTimestamp(q_time);
                buildInMention.setxAccBi(x_AccBI);
                buildInMention.setyAccBi(y_AccBI);
                buildInMention.setzAccBi(z_AccBI);
                buildInMention.setxGyroBi(x_GyroBI);
                buildInMention.setyGyroBi(y_GyroBI);
                buildInMention.setzGyroBi(z_GyroBI);
                buildInMention.setxMagBi(x_MagBI);
                buildInMention.setyMagBi(y_MagBI);
                buildInMention.setzMagBi(z_MagBI);
                buildInMention.setGestureState(q_lable);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(tag, "save buildInMention to database success");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d(tag, "save buildInMention to database error");
            }
        });
    }

    private void saveFeatureValue(Realm realm, long q_time, final double q_a_bi, final double q_a_ex,final double q_w_bi,final double q_w_ex, String q_lable){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                FeatureData featureData = realm.createObject(FeatureData.class);

                featureData.setTimestamp(q_time);
                featureData.setR_A_BI(q_a_bi);
                featureData.setR_A_EX(q_a_ex);
                featureData.setR_W_BI(q_w_bi);
                featureData.setR_W_EX(q_w_ex);
                featureData.setStatus(q_lable);

                Log.d(tag,"---->saveFeatureData successfully<-----");
            }
        });
    }

    private void saveAngleData(Realm realm,long q_time, final double q_angle_BI, final double q_angle_EX, String q_lable){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                AngleData angleData = realm.createObject(AngleData.class);
                angleData.setTimestamp(q_time);
                angleData.setAngle_BI(q_angle_BI);
                angleData.setAngle_EX(q_angle_EX);
                angleData.setStatus(q_lable);
                Log.d(tag,"---->saveAngleData successfully<-----");
            }
        });
    }

    private void saveSorDFeatureData(Realm realm, long q_time, final double q_Dvalue_A_BI,final double q_Dvalue_A_EX,final double q_Dvalue_W_BI,final double q_Dvalue_W_EX, String q_lable){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                SorDfeatureData sorDfeatureData = realm.createObject(SorDfeatureData.class);

                sorDfeatureData.setTimestamp(q_time);
                sorDfeatureData.setDValue_A_BI(q_Dvalue_A_BI);
                sorDfeatureData.setDValue_A_EX(q_Dvalue_A_EX);
                sorDfeatureData.setDValue_W_BI(q_Dvalue_W_BI);
                sorDfeatureData.setDValue_W_EX(q_Dvalue_W_EX);
                sorDfeatureData.setStatus(q_lable);
                Log.d(tag,"---->saveSorDFeatureData successfully<-----");

            }
        });
    }

    private void exportRealmFile() {
        Realm realm = Realm.getDefaultInstance();
        Log.d(tag,"------->Start exportRealmFile<--------");
        final File file = new File(Environment.getExternalStorageDirectory().getPath().concat("/sample_"+ System.currentTimeMillis() + ".realm"));
        if (file.exists()) {
            file.delete();
        }
        realm.writeCopyTo(file);
        realm.close();
        Toast.makeText(MainActivity.this, "Success export realm file", Toast.LENGTH_SHORT).show();
    }

    private void iniDeleteDataAtDatabase() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(MentionData.class);
                realm.delete(externalMention.class);
                realm.delete(buildInMention.class);

                realm.delete(FeatureData.class);
                realm.delete(AngleData.class);
                realm.delete(SorDfeatureData.class);
            }
        });
    }

    private void pushStackRowData(double[][] q_arr, double[] q_arrRead) {
        moveStack(q_arr);
        for (int i = 0; i < q_arr.length; i++) {
            if (i == 0) {
                q_arr[i][0] = q_arrRead[9];
            } else if (i == 1) {
                q_arr[i][0] = q_arrRead[10];
            } else if (i == 2) {
                q_arr[i][0] = q_arrRead[11];
            } else if (i == 3) {
                q_arr[i][0] = q_arrRead[12];
            } else if (i == 4) {
                q_arr[i][0] = q_arrRead[13];
            } else if (i == 5) {
                q_arr[i][0] = q_arrRead[14];
            } else if (i == 6) {
                q_arr[i][0] = q_arrRead[15];
            } else if (i == 7) {
                q_arr[i][0] = q_arrRead[16];
            } else if (i == 8) {
                q_arr[i][0] = q_arrRead[17];
            } else if (i == 9) {
                q_arr[i][0] = q_arrRead[0];
            } else if (i == 10) {
                q_arr[i][0] = q_arrRead[1];
            } else if (i == 11) {
                q_arr[i][0] = q_arrRead[2];
            } else if (i == 12) {
                q_arr[i][0] = q_arrRead[3];
            } else if (i == 13) {
                q_arr[i][0] = q_arrRead[4];
            } else if (i == 14) {
                q_arr[i][0] = q_arrRead[5];
            } else if (i == 15) {
                q_arr[i][0] = q_arrRead[6];
            } else if (i == 16) {
                q_arr[i][0] = q_arrRead[7];
            } else if (i == 17) {
                q_arr[i][0] = q_arrRead[8];
            }
        }
        Log.d(tag,"---->pushStackRowData finish<----");
        Log.d(tag,"RowData:" + Arrays.deepToString(q_arr));

    }

    private void pushStackAverageValue(double[][] q_arr, double q_value, int cont) {
        moveStack(averageValue);
        q_arr[cont][0] = q_value;
    }

    private void pushStackFeatureValue(double[] q_arrRead,double[][] q_arrWrite){
        moveStack(q_arrWrite);

        for (int i = 0; i < q_arrRead.length; i++) {
            q_arrWrite[i][0] = q_arrRead[i];
        }

    }

    private void AccGyroPushStackFeatureValue(double[][] q_arr){
        moveStack(featureValueForFallDec);


        double temp[] = {0,0,0,0};
        //EX
        temp[0] = calFeature(q_arr[0][0],q_arr[1][0],q_arr[2][0]);
        temp[1] = calFeature(q_arr[3][0],q_arr[4][0],q_arr[5][0]);

        //BI
        temp[2] = calFeature(q_arr[9][0],q_arr[10][0],q_arr[11][0]);
        temp[3] = calFeature(q_arr[12][0],q_arr[13][0],q_arr[14][0]);

        featureValueForFallDec[0][0] = temp[0];
        featureValueForFallDec[1][0] = temp[1];
        featureValueForFallDec[2][0] = temp[2];
        featureValueForFallDec[3][0] = temp[3];

        Log.d(tag,"--->AccGyroPushStackFeatureValue successfully<-----");
    }

    private void moveStack(double[][] q_arr) {

        for (int i = 0; i < q_arr.length; i++) {
            for (int j = q_arr[i].length - 1; j > 0; j--) {
                q_arr[i][j] = q_arr[i][j-1];
            }
        }


    }

    private double calAverage(double[] q_singleArr) {
        double averageValue = 0;
        for (int i = 0; i < q_singleArr.length; i++) {
            averageValue = averageValue + q_singleArr[i];
        }
        return averageValue / q_singleArr.length;
    }

    private void calAverageValue(double[][] q_arr) {
        double temp = 0;
        for (int i = 0; i < q_arr.length; i++) {
            temp = calAverage(q_arr[i]);
            pushStackAverageValue(averageValue,temp,i);
        }

        Log.d(tag,"averageValue:" + Arrays.deepToString(averageValue));
    }

    private double calFeature(double q_x, double q_y, double q_z) {
        double temp = 0;

        temp = Math.sqrt(q_x * q_x + q_y * q_y + q_z * q_z);

        return temp;
    }

    private void calFeatureValue(double[][] q_arr) {

        double[] arrys = {0,0,0, 0,0,0, 0,0,0, 0,0,0, 0,0,0, 0,0,0};
        double[] temp = {0,0,0,0,0,0};



        for (int i = 0; i < q_arr.length; i++) {
            arrys[i]=q_arr[i][0];
        }

        temp[0] = calFeature(arrys[0],arrys[1],arrys[2]);
        temp[1] = calFeature(arrys[3],arrys[4],arrys[5]);
        temp[2] = calFeature(arrys[6],arrys[7],arrys[8]);

        temp[3] = calFeature(arrys[9],arrys[10],arrys[11]);
        temp[4] = calFeature(arrys[12],arrys[13],arrys[14]);
        temp[5] = calFeature(arrys[15],arrys[16],arrys[17]);



        pushStackFeatureValue(temp,featureValue);

        Log.d(tag,"featureValue:"+ Arrays.deepToString(featureValue));
    }

    private double maxValue(double[] q_arrs){
        double max = q_arrs[0];
        for (int i = 0; i < q_arrs.length; i++) {
            if (q_arrs[i] > max){
                max = q_arrs[i];
            }
        }
        return max;
    }

    private double minValue(double[] q_arrs){
        double min = q_arrs[0];
        for (int i = 0; i < q_arrs.length; i++) {
            if (q_arrs[i] < min){
                min = q_arrs[i];
            }
        }
        return min;
    }

    private double minValue(double[] q_arrs, int t){
        double min = q_arrs[0];
        for (int i = 0; i < t; i++) {
            if (q_arrs[i] < min){
                min = q_arrs[i];
            }
        }
        return min;
    }

    private double maxValue(double[] q_arrs, int t){
        double max = q_arrs[0];
        for (int i = 0; i < t; i++) {
            if (q_arrs[i] > max){
                max = q_arrs[i];
            }
        }
        return max;
    }

    private boolean staticOrDynamicDetection(double[][] q_arr){
        double Gap_A_BI = maxValue(q_arr[3]) -  minValue(q_arr[3]);
        double Gap_A_EX = maxValue(q_arr[0]) -  minValue(q_arr[0]);
        double Gap_W_BI = maxValue(q_arr[4]) -  minValue(q_arr[4]);
        double Gap_W_EX = maxValue(q_arr[1]) -  minValue(q_arr[1]);

        double T_Acc_DandS = 0.5;
        double T_Gyro_DandS = 60;
        Log.d(tag,"Gap_A_BI:" + Gap_A_BI + "  " + "Gap_A_EX:" + Gap_A_EX + "  " + "Gap_W_BI:" + Gap_W_BI + "  " + "Gap_W_EX:" + Gap_W_EX);

        if(Gap_A_BI < T_Acc_DandS && Gap_A_EX < T_Acc_DandS && Gap_W_BI < T_Gyro_DandS && Gap_W_EX < T_Gyro_DandS){
            return true;
        }

        return false;
    }

    private double calAngleOfBody(double acc){
        double temp = 0;
        double g = 9.8;

        if(acc/g > 1){
            return Math.toDegrees(Math.acos(1));
        }

        if(acc/g < -1){
            return Math.toDegrees(Math.acos(-1));
        }

        temp = Math.acos(acc/g);


        return Math.toDegrees(temp);
    }

    private String postureDetection(double[][] q_arrs){
        //parameters
        double T_angle_low = 40;
        double T_angle_high = 60;

        String result = "";
        Log.d(tag,"angle_q_arrs[10][1]:"+ q_arrs[10][1] + "  " + "angle_q_arrs[0][1]:" + q_arrs[0][1]);

        double angle_BI = calAngleOfBody(q_arrs[10][1]);
        double angle_EX = calAngleOfBody(q_arrs[0][1]);

        posture_q[0] = angle_BI;
        posture_q[1] = angle_EX;

        Log.d(tag,"angle_EX:"+ angle_EX + "  " + "angle_BI:" + angle_BI);

        if(angle_EX < T_angle_low && angle_BI < T_angle_low){
            result = "standing";
        } else if(angle_EX > T_angle_high && angle_BI < T_angle_low){
            result = "bending";
        } else if(angle_EX < T_angle_low && angle_BI > T_angle_high){
            result = "sitting";
        } else if(angle_EX > T_angle_high && angle_BI > T_angle_high){
            result = "lying";
        }
        return result;
    }

    private String dynamicDetection(double[][] q_arrs){

        double D_A_BI = maxValue(q_arrs[2],10) - minValue(q_arrs[2],10);
        double D_A_EX = maxValue(q_arrs[0],10) - minValue(q_arrs[2],10);
        double D_W_BI = maxValue(q_arrs[3],10) - minValue(q_arrs[2],10);
        double D_W_EX = maxValue(q_arrs[1],10) - minValue(q_arrs[2],10);

        double T_A_BI_walking = 3;
        double T_A_EX_walking = 1.5;
        double T_W_BI_walking = 30;
        double T_W_EX_walking = 15;

        double T_A_BI_running = 15;
        double T_A_EX_running = 12;
        double T_W_BI_running = 150;
        double T_W_EX_running = 80;

        if (D_A_BI > T_A_BI_walking && D_A_BI < T_A_BI_running &&
                D_A_EX > T_A_EX_walking && D_A_EX < T_A_EX_running &&
                D_W_BI > T_W_BI_walking && D_W_BI < T_W_BI_running &&
                D_W_EX > T_W_EX_walking && D_W_EX < T_W_EX_running){
            return "walking";
        } else if (D_A_BI > T_A_BI_running && D_A_EX > T_A_EX_running && D_W_BI >T_W_BI_running && D_W_EX > T_W_EX_running){
            return "running";
        } else {
            return "other";
        }

    }

    private boolean fallDetection(double[][] q_arrs){

        double T_A_BI = 14;
        double T_A_EX = 16;
        double T_W_BI = 160;
        double T_W_EX = 150;

        double T_S_W_sum = 600;
        double T_S_W_sub = 150;
        double T_S_A_sum = 45;
        double T_S_A_min = 15;


        double R_A_BI = maxValue(q_arrs[2]) - minValue(q_arrs[2]);
        double R_A_EX = maxValue(q_arrs[0]) - minValue(q_arrs[0]);
        double R_W_BI = maxValue(q_arrs[3]) - minValue(q_arrs[3]);
        double R_W_EX = maxValue(q_arrs[1]) - minValue(q_arrs[1]);

        Log.d(tag,"R_A_BI:" + R_A_BI + "R_A_EX:" + R_A_EX +"R_W_BI:" + R_W_BI +"R_W_EX:" + R_W_EX);

        if (R_A_BI > T_A_BI && R_A_EX > T_A_EX && R_W_BI > T_W_BI && R_W_EX >T_W_EX){
            return true;
        }

        if ((R_A_BI + R_A_EX)>T_S_A_sum && R_A_BI > T_S_A_min && (R_W_BI+R_W_EX)>T_S_W_sum && Math.abs(R_W_BI-R_A_EX)<T_S_W_sub){
            return true;
        }

        return false;
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (HomeTitleString != null) {
                        mTextView.setText(HomeTitleString);
                    }
                    if (mTextView.getText() == "Dashboard") {
                        mTextView.setText(HomeTitleString);
                    } else if (mTextView.getText() == "Notifications") {
                        mTextView.setText(HomeTitleString);
                    }
                    displaySensorData(false);
                    displayBTDeviceList(true);
                    displayBtn(false);
                    return true;


                case R.id.navigation_dashboard:
                    if (!(mTextView.getText() == "Inner Sensor")) {
                        HomeTitleString = mTextView.getText().toString();
                    } else if (!(mTextView.getText() == "Notifications")) {
                        HomeTitleString = mTextView.getText().toString();
                    }

                    mTextView.setText(R.string.Title_inner_sensor);

                    displaySensorData(true);
                    displayBTDeviceList(false);
                    displayBtn(true);
                    return true;

                case R.id.navigation_notifications:
                    if (!(mTextView.getText() == "Dashboard")) {
                        HomeTitleString = mTextView.getText().toString();
                    } else if (!(mTextView.getText() == "Notifications")) {
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


