package io.jingwei.base.feign.log;

import com.google.common.collect.Lists;
import feign.Request;
import feign.Response;
import feign.Util;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static feign.Util.UTF_8;
import static feign.Util.decodeOrDefault;

/**
 * info feign 日志
 * ref:https://www.jianshu.com/p/ae369d38c8b2
 * https://stackoverflow.com/questions/46663455/feign-client-how-to-log-the-server-name-which-the-request-was-sent-to?rq=1
 */
@Slf4j
public class InfoFeignLogger extends feign.Logger {

    /**
     * 头尾显示多少个字符，默认头尾显示两个字符
     */
    @Value("${show.desen.char.number:2}")
    private Integer showDesenCharNumber;


    /******************************************************************************************************************
     ***********************************************feign打印配置相关开始 ***********************************************
     * 尤其是文件、隐私、私钥、token、文章等数据的打印需要注意，在这里需要配置。
     * 文件由于上传或者下载可能涉及流数据，数据太大，在日志中打印会非常难看
     * 隐私、私钥、token绝对不能打印日志落地，这里提供header的隐藏，如果是在请求和response中，隐藏不方便，那么直接就不打印请求或者返回
     * 文章由于可能返回内容太大，打印返回也没有很大意义，所以也需要关注是否需要打印
     * 比如获取用户钱包信息，这些会返回用户的私钥，不能打印
     * 这个哪些能打印，哪些不能打印和HttpTraceLogFilter有非常多相识的地方，比如应用中不能显示返回给前端私钥，这个私钥是通过feign调用
     * 后端钱包获取的，那么调用钱包的接口就需要不打印
     */

    /**
     * 如果uri是如下列表中的一项开头的，那么在feign中的request和response都不会打印
     * 比如健康检查、swagger
     */
    List<String> ignorePrintUriList = Lists.newArrayList("/swagger-ui", "/favicon", "/userCenter/user/register", "/userCenter/user/resetTokenExpireTime");
    /**
     * 如果uri是如下列表中的一项开头的，那么feign中request不打印
     * 比如文件上传相关的，那就request不打印，但是打印response
     */
    List<String> ignorePrintRequestUriList = Lists.newArrayList("/cis/user/file/upload/publicFile",
            "/userCenter/user/getUserNoByToken", "/userCenter/user/getWalletInfo");
    /**
     * 如果uri是如下列表中的一项开头的，那么feign的response不打印
     * 比如文章相关的，就是打印request不打印response，比如针对一些返回私钥的接口也需要配置在这里，不打印私钥信息
     */
    List<String> ignorePrintResponseUriList = Lists.newArrayList("/article/", "/user/getWalletInfo", "/userCenter/user/login",
            "/userCenter/user/register", "/cis/country/query/countryList");

    /******************************************************************************************************************
     ***********************************************feign打印配置相关结束 ************************************************
     */


    private final Logger logger;

    public InfoFeignLogger(Logger logger) {
        this.logger = logger;
    }


    /**
     * 如果uri开头是需要ignore的uri开头，那么不打印http trace
     *
     * @param uri
     * @return
     */
    private boolean isIgnorePrint(String uri) {
        for (String item : ignorePrintUriList) {
            if (uri.startsWith(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否不打印request
     *
     * @param uri
     * @return
     */
    private boolean isIgnorePrintRequest(String uri) {
        for (String item : ignorePrintRequestUriList) {
            if (uri.startsWith(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否不打印response
     *
     * @param uri
     * @return
     */
    private boolean isIgnorePrintResponse(String uri) {
        for (String item : ignorePrintResponseUriList) {
            if (uri.startsWith(item)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 覆写feign.Logger中的logRequest
     * 这个是打印请求
     * 去掉打印header头
     *
     * @param configKey
     * @param logLevel
     * @param request
     */
    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {

//        后续要看看org.springframework.cloud.netflix.feign.ribbon.FeignRibbonClientAutoConfiguration 这个如何自己注入一个client实现
//        https://github.com/jmnarloch/ribbon-discovery-filter-spring-cloud-starter  这个项目的路由
//        com.netflix.client.AbstractLoadBalancerAwareClient.executeWithLoadBalancer()
//                中的                        URI finalUri = reconstructURIWithServer(server, request.getUri()); 才是需要的
//        通过org.springframework.cloud.netflix.feign.ribbon.LoadBalancerFeignClient.execute() 调用的
        if (logger.isInfoEnabled()) {
//            覆写 super.logRequest(configKey, logLevel, request);
            log(configKey, "---> %s %s HTTP/1.1", request.method(), request.url());
            if (logLevel.ordinal() >= Level.HEADERS.ordinal()) {
                String urlString = request.url();

                int bodyLength = 0;
                if (request.body() != null) {
                    bodyLength = request.body().length;
                    if (logLevel.ordinal() >= Level.FULL.ordinal()) {
                        /**
                         * 去除换行的打印，这样查问题更加方便看request更加方便
                         */
                        String bodyText =
                                request.charset() != null ? new String(request.body(), request.charset()).replace("\n", "") : null;
                        try {
                            URL url = new URL(urlString);
                            String path = url.getPath();

                            if (isIgnorePrint(path) || isIgnorePrintRequest(path)) {
                                log.info("path:{} is no need to print request", path);
                            } else {
                                log(configKey, "%s", bodyText != null ? bodyText : "Binary data");
                            }
                        } catch (MalformedURLException e) {
                            log.error("urlString:{} is not a valid url", urlString);
                        }

                    }
                }
                log(configKey, "---> END HTTP (%s-byte body)", bodyLength);
            }

        }
    }

    @Override
    protected void log(String configKey, String format, Object... args) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format(methodTag(configKey) + format, args));
        }
    }

    /**
     * 这个是打印response
     * 去掉打印headers
     *
     * @param configKey
     * @param logLevel
     * @param response
     * @param elapsedTime
     * @return
     * @throws IOException
     */
    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel,
                                              Response response, long elapsedTime) throws IOException {
        String requestUrl = response.request().url();

//        覆写super.logAndRebufferResponse(configKey, logLevel, response, elapsedTime);
//        通过feign的日志，只能通过response打印 https://github.com/OpenFeign/feign/issues/598

        String reason = response.reason() != null && logLevel.compareTo(Level.NONE) > 0 ?
                " " + response.reason() : "";
        int status = response.status();
        log(configKey, "<--- HTTP/1.1 %s%s (%sms)", status, reason, elapsedTime);
        log(configKey, "requestUrl:%s", requestUrl);
        if (logLevel.ordinal() >= Level.HEADERS.ordinal()) {
            int bodyLength = 0;
            if (response.body() != null && !(status == 204 || status == 205)) {
                // HTTP 204 No Content "...response MUST NOT include a message-body"
                // HTTP 205 Reset Content "...response MUST NOT include an entity"
                byte[] bodyData = Util.toByteArray(response.body().asInputStream());
                bodyLength = bodyData.length;
                if (logLevel.ordinal() >= Level.FULL.ordinal() && bodyLength > 0) {
                    URL url = new URL(requestUrl);
                    String path = url.getPath();
                    if (isIgnorePrint(path) || isIgnorePrintResponse(path)) {
                        log.info("path:{} is no need to print response", path);
                    } else {
                        log(configKey, "%s", decodeOrDefault(bodyData, UTF_8, "Binary data"));
                    }
                }
                log(configKey, "<--- END HTTP (%s-byte body)", bodyLength);
                return response.toBuilder().body(bodyData).build();
            } else {
                log(configKey, "<--- END HTTP (%s-byte body)", bodyLength);
            }
        }
        return response;
    }
}
