package com.example.lenovo.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class ConnectThread extends Thread {
    private final String tag = "KE,Junxing:";

    private BluetoothSocket mmSocket = null;
    private BluetoothDevice mmDevice;

    private static final UUID BT_UUID = UUID.fromString("F000AA80-0451-4000-B000-000000000000");

    public ConnectThread(BluetoothDevice device) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final

        mmDevice = device;


    }


    public void run() {
        // Cancel discovery because it will slow down the connection
//        mBluetoothAdapter.cancelDiscovery();
        BluetoothSocket tmp = null;
        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = mmDevice.createRfcommSocketToServiceRecord(BT_UUID);
        } catch (IOException e) { }
        mmSocket = tmp;
        Log.d(tag,"ConnectThread device: " + mmDevice.getName());
        Log.d(tag,"ConnectThread.java ConnectThread() onRun ");


        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            Log.d(tag,"ConnectThread.java run() onRun ");

            mmSocket.connect();
            Log.d(tag,"connected successful!");
        } catch (IOException connectException) {

            Log.d(tag,"ConnectThread.java Unable to connect !");
            Log.d(tag,"ConnectThread.java" + connectException.getMessage());

            try {
                mmSocket = (BluetoothSocket) mmDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(mmDevice,1);
                mmSocket.connect();
                        Log.d(tag,"connected successful!");
                    } catch (IllegalAccessException e1) {
                        e1.printStackTrace();
                        Log.d(tag,"connected Error! 1");
                    } catch (InvocationTargetException e1) {
                        e1.printStackTrace();
                        Log.d(tag,"connected Error! 2");
                    } catch (NoSuchMethodException e1) {
                        e1.printStackTrace();
                        Log.d(tag,"connected Error! 3");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        Log.d(tag, "exception: " + e1.getMessage());
                        Log.d(tag, "exception: " + e1.toString());

                        Log.d(tag,"connected Error! 4 for connect");
                    }





            // Unable to connect; close the socket and get out
            try {
                mmSocket.close();

                Log.d(tag,"ConnectThread.java  mmSocket.close(); !");
            } catch (IOException closeException) {
                Log.d(tag,"connecting failed! ");

            }
            return;
        }
        // Do work to manage the connection (in a separate thread)
//        manageConnectedSocket(mmSocket);
    }

    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }


}
