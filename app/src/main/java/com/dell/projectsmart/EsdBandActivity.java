package com.dell.projectsmart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.util.List;

import model.Prediction;
import model.ResponseModel;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rest.ApiClient;
import rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EsdBandActivity extends AppCompatActivity {
    private static final int CAMERA_PIC_REQUEST = 1337;
    private Context context;
    List<Prediction> prediction;
    public static boolean proceed = false;
    private static ProgressBar spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esd_band);

        context = this;

        TextView textView = findViewById(R.id.welcome_message);
        textView.setText("Verify if the ESD Band is worn correctly");
        spinner = findViewById(R.id.spinner1);
        spinner.setVisibility(View.GONE);
        openCamera();
    }

    public void openCamera() {
        Button button = findViewById(R.id.capture_esd_band);
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
            ImageView imageview = findViewById(R.id.esd_band_image); //sets imageview as the bitmap
            imageview.setImageBitmap(image);
            ByteArrayOutputStream imageBytes = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, imageBytes);
            byte[] byteArray = imageBytes.toByteArray();
            customVisionApi(byteArray);
        }
    }

    private void customVisionApi(final byte[] bytes){

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), bytes);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
        Call<ResponseModel> call = apiInterface.uploadImage(body);

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                prediction  = response.body().getPredictions();
                String prediction0 = prediction.get(0).getTagName() + " " + Math.round(prediction.get(0).getProbability()*100) + "%";
                String prediction1 = prediction.get(1).getTagName() + " " + Math.round(prediction.get(1).getProbability()*100) + "%";

                Toast.makeText(context,prediction0 +"\n"+prediction1 , Toast.LENGTH_LONG).show();
                spinner.setVisibility(View.GONE);
                proceed = true;
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



    public void onClickValidate(View view) {
        if (proceed) {
            Intent intent = new Intent(this, FinalPageActivity1.class);
            startActivity(intent);
        } else {
            Toast.makeText(context, "Please ensure band is verified before proceeding", Toast.LENGTH_LONG).show();
        }
    }
}
