package edu.neu.madcourse.numad21fa_a7team21days.http;

import com.google.gson.annotations.SerializedName;

public class HttpResponse<T> {

    @SerializedName("data") private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "data=" + data +
                '}';
    }
}
