package com.dell.projectsmart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.BarcodeView;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

public class BarCodeScannerActivity extends AppCompatActivity implements DecoratedBarcodeView.TorchListener {

    private static final int CAMERA_PIC_REQUEST = 1337;
    private Context context;
    public static boolean proceed = false;
    private static String serviceTag;

    private DecoratedBarcodeView decoratedBarcodeView;

    private boolean isFlashOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_scanner);

        context = this;

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra("userName");

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.welcome_message);
        textView.setText("WELCOME " + message);
        setupBarcodeView();

        Button button = findViewById(R.id.scan_bar_code);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toggleBarcodeScan(false);
                toggleBarcodeScan(true);
            }
        });

        ImageView ivFlash = findViewById(R.id.iv_flash);
        ivFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFlash(!isFlashOn);
            }
        });
    }

    private void setupBarcodeView() {

        decoratedBarcodeView = findViewById(R.id.barcode_view);
        decoratedBarcodeView.setStatusText("");
        decoratedBarcodeView.setTorchListener(BarCodeScannerActivity.this);
    }

    private void toggleFlash(Boolean enableFlash) {

        if (enableFlash) {
            decoratedBarcodeView.setTorchOn();
        } else {
            decoratedBarcodeView.setTorchOff();
        }

    }

    private void toggleBarcodeScan(Boolean startScan) {

        if (startScan) {
            decoratedBarcodeView.resume();
            decoratedBarcodeView.decodeSingle(getBarcodeCallback());
        } else {
            decoratedBarcodeView.pause();
        }

    }


    private BarcodeCallback getBarcodeCallback() {

        return new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                Toast.makeText(context, result.getText(), Toast.LENGTH_SHORT).show();
                serviceTag = result.getText();
                toggleBarcodeScan(false);
                proceed = true;
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {

            }
        };

    }


    public void onClickValidate(View view) {
        if (proceed) {
            Intent intent = new Intent(this, OcrActivity.class);
            intent.putExtra("serviceTag", serviceTag);
            startActivity(intent);
        } else {
            Toast.makeText(context, "Please Scan Barcode before proceeding", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onTorchOn() {
        isFlashOn = true;
    }

    @Override
    public void onTorchOff() {
        isFlashOn = false;
    }
}


