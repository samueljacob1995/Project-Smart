package rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String BASE_URL = "https://southeastasia.api.cognitive.microsoft.com/";
    private static Retrofit retrofit = null;

    public static final String OCR_BASE_URL = "https://southeastasia.api.cognitive.microsoft.com/";
    private static Retrofit ocrRetrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getOcrClient() {
        if (ocrRetrofit==null) {
            ocrRetrofit = new Retrofit.Builder()
                    .baseUrl(OCR_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return ocrRetrofit;
    }
}
