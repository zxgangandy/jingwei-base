package io.jingwei.base.feign.decoder;

import io.jingwei.base.utils.exception.IBizErrCode;
import lombok.ToString;

@ToString
public class FeignBizErrCode implements IBizErrCode {

    private FeignBizErrCode(String code, String description) {
        this.code = code;
        this.desc = description;
    }

    public static FeignBizErrCode of (String code, String description) {
        return new FeignBizErrCode(code, description);
    }

    /**
     * 枚举编码
     */
    private String code;

    /**
     * 描述说明
     */
    private String desc;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public String getMsg() {
        return desc;
    }

}
