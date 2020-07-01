package cn.edu.scujcc.diandian;

import com.squareup.moshi.Moshi;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * 使用单例模式创建Retrofit，避免浪费资源
 * 创建单例的三步骤
 * 1.创建私有静态变量
 * 2.创建私有构造方法
 * 3.共有的方法返回该对象
 */
public class RetrofitClient {
    private static Retrofit INSTANCE = null;

    public static Retrofit get() {
        if (INSTANCE == null) {
            Moshi moshi = new Moshi.Builder()
                    .add(new MyDate())
                    .build();
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor())
                    .build();

            INSTANCE = new Retrofit.Builder()
                    .baseUrl("http://47.112.239.237:8080")   //访问自己的阿里云
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .callFactory(client)
                    .build();
        }
        return INSTANCE;
    }
}
