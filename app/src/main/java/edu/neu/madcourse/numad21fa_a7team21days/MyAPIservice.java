package edu.neu.madcourse.numad21fa_a7team21days;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

// FIREBASE_MESSAGE_TOKEN: ebk_vsvuShyGd1tTd3_2KJ:APA91bG0FdHkLtRMD1SUfOnCXbZ5iNz3kNtuU_Qvze2fT4lbDdAen7Qv3DcPSI-2AJhi8rDzLbbJk-R1U8BrihHB6akXBA1eqBhbsQn8zmT_jrrgBRQ5lxehcZBfsRw2zcgVTU0tzSLe

public interface MyAPIservice {
    @Headers(
            {


            "Content-Type:application/json",
                    "Authorization:key=AAAAabFiQns:APA91bG3ArtLRHlcn2mZdYl-02AM-mvUISQlJVIJx2jUhaKlKZmYw2Hc2RptWeOys8QJJreX5m9eWCkZKpxtzEO2YsrMnU4UC0es-MWdsotHg8Nl5lE1qrtapNQjh6MJrXtIZlk8oOB3" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}
