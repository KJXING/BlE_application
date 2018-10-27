package com.example.lenovo.myapplication;

public class SensorConvert {

    public Point3D accConvert(final byte[] value){
        // Range 8G
        final float SCALE = (float) 4096.0;

        int x = (value[7]<<8) + value[6];
        int y = (value[9]<<8) + value[8];
        int z = (value[11]<<8) + value[10];
        return new Point3D(((x / SCALE) * -1), y / SCALE, ((z / SCALE)*-1));
    }


    public Point3D gyroConvert(final byte[] value){
        final float SCALE = (float) 128.0;

        int x = (value[1]<<8) + value[0];
        int y = (value[3]<<8) + value[2];
        int z = (value[5]<<8) + value[4];
        return new Point3D(x / SCALE, y / SCALE, z / SCALE);
    }

    public Point3D magConvert(final byte[] value){
        final float SCALE = (float) (32768 / 4912);
        if (value.length >= 18) {
            int x = (value[13]<<8) + value[12];
            int y = (value[15]<<8) + value[14];
            int z = (value[17]<<8) + value[16];
            return new Point3D(x / SCALE, y / SCALE, z / SCALE);
        }
        else return new Point3D(0,0,0);
    }
}
