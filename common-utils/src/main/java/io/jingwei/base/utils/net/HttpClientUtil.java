package io.jingwei.base.utils.net;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class HttpClientUtil {
    private static volatile HttpClientUtil mInstance;

    private OkHttpClient mOkHttpClient;

    private HttpClientUtil() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(30, TimeUnit.SECONDS);//读取超时
        builder.connectTimeout(10, TimeUnit.SECONDS);//连接超时
        builder.writeTimeout(60, TimeUnit.SECONDS);//写入超时

        //协议
        List<Protocol> protocols = new ArrayList<>();
        protocols.add(Protocol.HTTP_1_1);
        protocols.add(Protocol.HTTP_2);

        builder.protocols(protocols);

        mOkHttpClient = builder.build();
    }

    public static HttpClientUtil getInstance() {
        if (mInstance == null) {
            synchronized (HttpClientUtil.class) {
                if (mInstance == null) {
                    mInstance = new HttpClientUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * 设置Header头
     *
     * @param headersParams
     * @return
     */
    private Headers setHeaders(Map<String, Object> headersParams) {
        Headers.Builder headerBuilder = new Headers.Builder();

        if (headersParams != null) {
            for (String key : headersParams.keySet()) {
                headerBuilder.add(key, headersParams.get(key).toString());
            }
        }

        Headers headers = headerBuilder.build();

        return headers;
    }

    /**
     * 设置post表单请求
     *
     * @param params
     * @return
     */
    private RequestBody setPostBody(Map<String, Object> params) {

        FormBody.Builder formBodyBuilder = new FormBody.Builder();

        if (params != null) {
            for (String key : params.keySet()) {
                formBodyBuilder.add(key, params.get(key).toString());
            }
        }

        RequestBody body = formBodyBuilder.build();

        return body;
    }

    /**
     * 设置post表单请求
     *
     * @param strBody
     * @return
     */
    private RequestBody setPostBody(String strBody) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), strBody);

        return body;
    }

    /**
     * 异步Get请求
     *
     * @param url
     * @param headerParams
     * @return
     */
    public void getAsync(String url, Map<String, Object> headerParams, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .headers(setHeaders(headerParams))
                .get()
                .build();

        Call call = mOkHttpClient.newCall(request);

        log.info("Http info:" + request.toString());
        log.info("Http Header:" + request.headers().toString());


        call.enqueue(callback);
    }

    /**
     * 异步Post请求
     * @param url
     * @param headerParams
     * @param bodyParams
     * @return
     */
    public void postAsync(String url, Map<String, Object> headerParams, Map<String, Object> bodyParams,  Callback callback){
        Request request = new Request.Builder()
                .url(url)
                .headers(setHeaders(headerParams))
                .post(setPostBody(bodyParams))
                .build();

        Call call = mOkHttpClient.newCall(request);

        log.info("Http info:" + request.toString());
        log.info("Http Header:" + request.headers().toString());
        log.info("Http params:" + request.body().toString());

        if (callback != null) {
            call.enqueue(callback);
        }
    }

    /**
     * 同步Post请求
     * @param url
     * @param strBody
     * @return
     */
    public String postSync(String url, String strBody) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(setPostBody(strBody))
                .build();

        return mOkHttpClient.newCall(request).execute().body().string();
    }

}
