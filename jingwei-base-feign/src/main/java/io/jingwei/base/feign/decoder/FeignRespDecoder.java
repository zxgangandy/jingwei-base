package io.jingwei.base.feign.decoder;

import com.alibaba.fastjson.JSONObject;
import feign.Util;
import feign.codec.Decoder;

import io.jingwei.base.utils.exception.BizErr;
import io.jingwei.base.utils.exception.IBizErrCode;
import io.jingwei.base.utils.exception.SysErr;
import io.jingwei.base.utils.model.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;


/**
 * @Description: feign client返回值的拦截器，如果外部调用失败，非000000统一当失败返回系统繁忙
 * 在FeignClientRespDecoderConfig中扔出的异常统一会被包装成DecodeException，所以处理这种exception的时候需要抓DecodeException
 * Object decode(Response response) throws Throwable {
 * try {
 * return decoder.decode(response, metadata.returnType());
 * } catch (FeignException e) {
 * throw e;
 * } catch (RuntimeException e) {
 * throw new DecodeException(e.getMessage(), e);
 * }
 * }
 * https://www.cnblogs.com/li3807/p/8890622.html
 * @date 2019/7/9 11:47 AM
 */
@Configuration
@Slf4j
public class FeignRespDecoder {
    @Bean
    public Decoder createDecoder() {
        return (response, type) -> {
            try {
                HttpStatus httpStatus = HttpStatus.valueOf(response.status());
                if (httpStatus.is2xxSuccessful()) {
                    String feignResp = Util.toString(response.body().asReader());
                    R commonResp = JSONObject.parseObject(feignResp, R.class);
                    if (commonResp.isSuccess()) {
                        return JSONObject.parseObject(feignResp, type);
                    } else {
                        log.info("commonResp is not success, commonResp:{}", commonResp);
                        IBizErrCode bizErr = FeignBizErrCode.of(commonResp.getCode(), commonResp.getMsg());
                        throw new BizErr(bizErr);
                    }
                } else {
                    log.error("resp http status is not 2xx, HttpStatus:{}", httpStatus);
                    throw new SysErr();
                }
            } catch (BizErr | SysErr e) {
                throw e;
            } catch (Throwable e1) {
                log.error("feign client fail,error message:{}", e1.getMessage(), e1);
                throw new SysErr();
            }
        };
    }
}
