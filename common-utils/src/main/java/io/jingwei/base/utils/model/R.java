package io.jingwei.base.utils.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.slf4j.MDC;

import java.io.Serializable;


/**
 * 响应信息主体
 *
 * @param <T>
 * @author Andy
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "响应信息主体")
public class R<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@ApiModelProperty(value = "返回标记：成功标记=true, 失败标记=false")
	private boolean success;

	@Getter
	@Setter
	@ApiModelProperty(value = "返回错误码")
	private String code;

	@Getter
	@Setter
	@ApiModelProperty(value = "返回信息")
	private String msg;


	@Getter
	@Setter
	@ApiModelProperty(value = "数据")
	private T data;

	@Getter
	@Setter
	@ApiModelProperty(value = "traceId")
	private String traceId;

	public static <T> R<T> ok() {
		return restResult(null, true, null, null);
	}

	public static <T> R<T> ok(T data) {
		return restResult(data, true,  null,null);
	}

	public static <T> R<T> failed() {
		return restResult(null, false, null,null);
	}

	public static <T> R<T> failed(String msg) {
		return restResult(null, false, null, msg);
	}

	public static <T> R<T> failed(String code, String msg) {
		return restResult(null, false, code, msg);
	}

	public static <T> R<T> failed(T data) {
		return restResult(data,  false, null, null);
	}

	public static <T> R<T> failed(T data, String msg) {
		return restResult(data, false, null, msg);
	}

	public static <T> R<T> failed(T data, String code, String msg) {
		return restResult(data, false, code, msg);
	}

	private static <T> R<T> restResult(T data, boolean success, String code, String msg) {
		R<T> apiResult = new R<>();
		apiResult.setSuccess(success);
		apiResult.setCode(code);
		apiResult.setData(data);
		apiResult.setMsg(msg);
		apiResult.setTraceId(MDC.get("X-B3-TraceId"));
		return apiResult;
	}
}
