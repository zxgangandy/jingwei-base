package io.jingwei.base.utils.model;

import io.jingwei.base.utils.constant.ApiConstant;
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
	@ApiModelProperty(value = "返回标记：成功标记=0，失败标记=1")
	private int code;

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
		return restResult(null, ApiConstant.SUCCESS, null);
	}

	public static <T> R<T> ok(T data) {
		return restResult(data, ApiConstant.SUCCESS, null);
	}

	public static <T> R<T> ok(T data, String msg) {
		return restResult(data, ApiConstant.SUCCESS, msg);
	}

	public static <T> R<T> failed() {
		return restResult(null, ApiConstant.FAIL, null);
	}

	public static <T> R<T> failed(String msg) {
		return restResult(null, ApiConstant.FAIL, msg);
	}

	public static <T> R<T> failed(T data) {
		return restResult(data, ApiConstant.FAIL, null);
	}

	public static <T> R<T> failed(T data, String msg) {
		return restResult(data, ApiConstant.FAIL, msg);
	}

	private static <T> R<T> restResult(T data, int code, String msg) {
		R<T> apiResult = new R<>();
		apiResult.setCode(code);
		apiResult.setData(data);
		apiResult.setMsg(msg);
		apiResult.setTraceId(MDC.get("X-B3-TraceId"));
		return apiResult;
	}
}
