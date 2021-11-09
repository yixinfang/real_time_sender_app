package edu.neu.madcourse.numad21fa_a7team21days.http;

import edu.neu.madcourse.numad21fa_a7team21days.bean.SendFcmBean;
import edu.neu.madcourse.numad21fa_a7team21days.bean.SendFcmResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiEndpointService {

    @POST("fcm/send")
    Call<SendFcmResponse> sendFCMMessage(@Body SendFcmBean sendFcmBean);
}
