package io.jingwei.base.utils.exception;

/**
 * @date 2019/7/16 2:57 PM
 */
public interface IBizErrCode {
    String getCode();

    String getDesc();

    /**
     * 根据国际化的时候获取异常对应的全路径来匹配国际化内容
     * @return
     */
    String getMsg();
}
