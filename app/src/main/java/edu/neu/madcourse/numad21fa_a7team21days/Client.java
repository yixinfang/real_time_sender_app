package edu.neu.madcourse.numad21fa_a7team21days;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class Client {
    private static Retrofit retrofit=null;

    public static Retrofit getClient(String url)
    {
        if(retrofit==null)
        {
            retrofit=new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return  retrofit;
    }
}
