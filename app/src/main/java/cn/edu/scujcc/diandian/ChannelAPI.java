package cn.edu.scujcc.diandian;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ChannelAPI {
    @GET("/channel")
    Call<Result<List<Channel>>> getAllChannels();

    /**
     * 获取热门评论
     *
     * @param channelId
     * @return
     */
    @GET("/channel/{channelId}/hotcomments")
    Call<Result<List<Comment>>> getHotComments(@Path("channelId") String channelId);

    /**
     * 新增评论
     *
     * @param ChannelId
     * @param c
     * @return
     */
    @POST("/channel/{channelId}/comment")
    Call<Channel> addComment(@Path("channelId") String ChannelId, @Body Comment c);

//    /**
//     * 登陆
//     *
//     * @param username 用户名
//     * @param password 密码
//     * @return
//     */
//    @GET("/user/login/{username}/{password}")
//    Call<Integer> login(@Path("username") String username, @Path("password") String password);

}
