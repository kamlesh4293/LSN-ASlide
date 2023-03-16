package com.app.lsquared;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.lsquared.utils.StringToByteArray;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

//https://github.com/mik3y/usb-serial-for-android

public class SerialPortActivity extends AppCompatActivity implements SerialInputOutputManager.Listener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView iv_logo = findViewById(R.id.logo);
        iv_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openConnection();
            }
        });
    }

    private void openConnection() {

        int WRITE_WAIT_MILLIS = 5000;
        int READ_WAIT_MILLIS = 5000;
        byte[] byteArray = StringToByteArray.hexStringToByteArray();

        // Find all available drivers from attached devices.
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        Map<String, UsbDevice> devices = manager.getDeviceList();
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
        if (availableDrivers.isEmpty()) {
            return;
        }

        // Open a connection to the first available driver.
        UsbSerialDriver driver = availableDrivers.get(0);

        UsbDeviceConnection connection = manager.openDevice(driver.getDevice());
        if (connection == null) {
//             add UsbManager.requestPermission(driver.getDevice(), ..) handling here
            showToast("connection is null");
            return;
        }

        for (int i=0;i<driver.getPorts().size();i++){
            showToast("port "+i+" "+driver.getPorts().get(i).getSerial());
        }


        UsbSerialPort port = driver.getPorts().get(0); // Most devices have just one port (port 0)
        try {
            showToast("Serial port - "+ port.getSerial());
//            port.open(connection);
            port.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
        } catch (IOException e) {
            showToast("exception 1 - "+ e);
            e.printStackTrace();
        }

//       read/write
//        try {
//            port.write(byteArray, WRITE_WAIT_MILLIS);
//            int len = port.read(response, READ_WAIT_MILLIS);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


//        or direct write + event driven read:
        SerialInputOutputManager usbIoManager = new SerialInputOutputManager(port, this);
        usbIoManager.start();
        try {
            showToast("port write data ");
            port.write(byteArray, WRITE_WAIT_MILLIS);
        } catch (IOException e) {
            showToast("exception 2 - "+ e);
            e.printStackTrace();
        }

        // close port
        try {
            port.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onNewData(byte[] data) {

    }

    @Override
    public void onRunError(Exception e) {

    }

    public void showToast(String msg){
        Toast.makeText(this,msg, Toast.LENGTH_LONG).show();
    }

}
