package cn.edu.scujcc.diandian;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ChannelAPI {
    @GET("/channel")
    Call<List<Channel>> getAllChannels();
}
