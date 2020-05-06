package io.jingwei.base.utils.exception;

import io.jingwei.base.utils.model.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;


import static io.jingwei.base.utils.exception.LocalUtils.getLocale;

/**
 * 全局异常处理器基类
 */
@Slf4j
public class BaseGlobalExceptionHandler {


    @Autowired
    private MessageSource messageSource;
    @Autowired
    private HttpServletRequest request;

    /**
     * 拦截自定义的异常
     */
    @ExceptionHandler(BizErr.class)
    public R handleBizException(BizErr e) {
        log.warn(e.getMessage(), e);
        return R.failed(e.getCode().getCode(), getLocaleMessage(e.getCode().getCode(), e.getMessage(), e.getArgs()));
    }

    /**
     * controller入参校验异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.warn("MethodArgumentNotValidException: {}, {}", e.getMessage(), e);

        String errorMessage = getErrMessage(e.getBindingResult());
        return R.failed(errorMessage);
    }

    /**
     * controller入参校验异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ConstraintViolationException.class)
    public R validationErrorHandler(ConstraintViolationException e) {
        log.warn("ConstraintViolationException: {}, {}", e.getMessage(), e);

        String errorMessage = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .reduce((s1, s2) -> s1.concat(",").concat(s2)).get();
        return R.failed(errorMessage);
    }

    /**
     * controller入参校验异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BindException.class)
    public R bindExceptionHandler(BindException e) {
        log.warn("BindException: {}, {}", e.getMessage(), e);

        String errorMessage = getErrMessage(e.getBindingResult());
        return R.failed(errorMessage);
    }


    /**
     * 获取国际化异常信息(支持url中包含lang和浏览器的Accept-Language)
     */
    protected String getLocaleMessage(String code, String defaultMsg, Object[] params) {
        try {
            return messageSource.getMessage(code, params, getLocale(request));
        } catch (Exception e) {
            log.warn("本地化异常消息发生异常: {}, {}", code, params);
            return defaultMsg;
        }
    }

    private static String getErrMessage(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .reduce((s1, s2) -> s1.concat(",").concat(s2)).get();
    }
}
