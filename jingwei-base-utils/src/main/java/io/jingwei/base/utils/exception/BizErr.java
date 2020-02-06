package io.jingwei.base.utils.exception;


import lombok.Getter;

/**
 *
 */
@Getter
public class BizErr extends RuntimeException {
    /**
     * 异常错误代码
     */
    protected IBizErrCode code;

    private boolean haveExtraMsg = false;
    private String[] args;

    public static BizErr of(IBizErrCode code) {
        BizErr bizException = new BizErr(code);
        return bizException;
    }


    /**
     * 创建一个对象
     *
     * @param e 业务异常
     */
    public BizErr(BizErr e) {
        super(e);
        this.code = e.getCode();
    }

    /**
     * @param code 错误码
     */
    public BizErr(IBizErrCode code, String... args) {
        super(code.getDesc());
        this.args = args;
        this.code = code;
    }

    /**
     * @param code         错误码
     * @param errorMessage 错误描述
     */
    public BizErr(IBizErrCode code, String errorMessage) {
        super(errorMessage);
        haveExtraMsg = true;
        this.code = code;
    }

    /**
     * @param code  错误码
     * @param cause 异常
     */
    public BizErr(IBizErrCode code, Throwable cause) {
        super(code.getDesc(), cause);
        haveExtraMsg = true;
        this.code = code;
    }

    /**
     * @param code         错误码
     * @param errorMessage 错误描述
     * @param cause        异常
     */
    public BizErr(IBizErrCode code, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        haveExtraMsg = true;
        this.code = code;
    }


    /**
     * @see Throwable#toString()
     */
    @Override
    public final String toString() {
        String s = getClass().getName();
        String message = getLocalizedMessage();
        return s + ": " + code.getCode() + "[" + message + "]。";
    }

    /**
     * @see Throwable#getMessage()
     */
    @Override
    public final String getMessage() {
        if (haveExtraMsg) {
            return super.getMessage();
        }
        /**
         * 自定义的message
         */
        return code.getDesc();
    }


    /**
     * @return Returns the code.
     */
    public IBizErrCode getCode() {
        return code;
    }

    /**
     * @param code The code to set.
     */
    public void setCode(IBizErrCode code) {
        this.code = code;
    }
}
