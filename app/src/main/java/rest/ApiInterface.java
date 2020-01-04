package rest;

import model.OcrResponseModel;
import model.ResponseModel;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface ApiInterface {
    @Multipart
    @Headers({
            "Accept-Encoding: gzip,deflate",
            "Prediction-Key: 1253b95813564dde988e68de14c7de5a"
    })
    @POST("/customvision/v3.0/Prediction/50fa373f-4668-4f46-9e95-1d5245f07acc/classify/iterations/Iteration1/image")
    Call<ResponseModel> uploadImage(@Part MultipartBody.Part image);


    @Multipart
    @Headers({
            "Accept-Encoding: gzip,deflate",
            "Ocp-Apim-Subscription-Key: 1253b95813564dde988e68de14c7de5a"
    })

    @POST("/vision/v2.0/ocr")
    Call<OcrResponseModel> uploadOcrImage(@Query("language")String language, @Query("detectOrientation")String detectOrientation, @Part MultipartBody.Part image);
}
