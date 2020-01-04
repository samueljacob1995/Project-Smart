package com.dell.projectsmart;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.OcrLine;
import model.OcrRegion;
import model.OcrResponseModel;
import model.OcrWord;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rest.ApiClient;
import rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OcrActivity extends AppCompatActivity {

    private static final int CAMERA_PIC_REQUEST = 1337;
    private Context context;
    public static boolean proceed = false;
    private static String responseStringText;
    private static String serviceTag;
    private static ProgressBar spinner;
    private static String filename;
    private static File myDir;
    private static File f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);
        Intent intent = getIntent();
        serviceTag = intent.getStringExtra("serviceTag");


        context = this;
        Toast.makeText(context, serviceTag, Toast.LENGTH_LONG).show();
        TextView textView = findViewById(R.id.welcome_message);
        textView.setText("Capture the BIOS Version");
        spinner = findViewById(R.id.spinner2);
        spinner.setVisibility(View.GONE);
        openCamera();
    }

    public void openCamera() {
        Button button = findViewById(R.id.capture_bios);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {
            spinner.setVisibility(View.VISIBLE);
            Bitmap image = (Bitmap) data.getExtras().get("data");
            ImageView imageview = findViewById(R.id.bios_image); //sets imageview as the bitmap
            imageview.setImageBitmap(image);
            Log.i("image height", String.valueOf(image.getHeight()));
            Log.i("image width", String.valueOf(image.getWidth()));
            int newHeight = (int) (image.getHeight() * 0.5), newWidth = (int) (image.getWidth() * 0.5);
            Log.i("new image height", String.valueOf(newHeight));
            Log.i("new image width", String.valueOf(newWidth));
            Bitmap enhanced_bitmap = getResizedBitmap(image, 3000);
            ByteArrayOutputStream imageBytes = new ByteArrayOutputStream();
            enhanced_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageBytes);
            byte[] byteArray = imageBytes.toByteArray();
            saveFile(byteArray, "B");


            ocrApi(byteArray);
        }
    }

    private void saveFile(byte[] byteArray, String status) {
        myDir = new File(Environment.getExternalStorageDirectory() + "/PROJECT_SMART");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        filename = new SimpleDateFormat("yyyyMMddHHmm'.jpg'").format(new Date());
        filename = status + filename;
        f = new File(myDir, filename);


        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(byteArray);
            fo.close();
            Toast.makeText(context, "Image Saved", Toast.LENGTH_LONG).show();
//            Log.i("File", "Saved Successfully");
        } catch (IOException e) {
//            Log.i("File", "Save Failed");
            e.printStackTrace();
        }
    }

    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {

        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);

    }

    private void ocrApi(final byte[] bytes) {
        ApiInterface apiInterface = ApiClient.getOcrClient().create(ApiInterface.class);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), f);
        Log.i("File Size:", f.getAbsolutePath() + f.length());
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
        Call<OcrResponseModel> call = apiInterface.uploadOcrImage("en", "true", body);

        call.enqueue(new Callback<OcrResponseModel>() {
            @Override
            public void onResponse(Call<OcrResponseModel> call, Response<OcrResponseModel> response) {
                responseStringText = "";
                List<OcrRegion> callRegion = response.body().getRegions();
                List<OcrLine> callLine = new ArrayList<>();
                List<OcrWord> callWord = new ArrayList<>();
                for (int i = 0; i < callRegion.size(); i++) {
                    callLine = callRegion.get(i).getLines();
                }
                for (int j = 0; j < callLine.size(); j++) {
                    callWord = callLine.get(j).getWords();
                    for (int i = 0; i < callWord.size(); i++) {
                        responseStringText += callWord.get(i).getText();
                    }
                }
//                Log.i("RSText:", responseStringText);
//                Log.i("Service TAG:", serviceTag.toUpperCase());
                String serviceTag1 = serviceTag.replaceAll("O", "0");
                String serviceTag2 = serviceTag.replaceAll("0", "O");


                spinner.setVisibility(View.GONE);
                if (responseStringText.toLowerCase().contains(serviceTag.toLowerCase()) ||
                        responseStringText.toLowerCase().contains(serviceTag1.toLowerCase()) ||
                        responseStringText.toLowerCase().contains(serviceTag2.toLowerCase())) {
                    Toast.makeText(context, "Service Tag Validated Successfully", Toast.LENGTH_LONG).show();
                    proceed = true;
                } else {
                    Toast.makeText(context, "Service Tag does not match", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<OcrResponseModel> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onClickValidate(View view) {

        if (proceed) {
            Intent intent = new Intent(this, EsdBandActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(context, "Please verify BIOS before proceeding", Toast.LENGTH_LONG).show();
        }
    }
}
