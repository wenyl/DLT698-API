package cn.com.wenyl.bs.utils;

import lombok.Data;
import org.apache.http.HttpStatus;

import java.io.Serializable;

/**
 * 请求返回的数据格式
 */
@Data
public class R<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean success = true;
	private String message = "";
	private Integer code = 0;
	private T result;
	public R() {
	}
	public static<T> R<T> ok() {
		R<T> r = new R<>();
		r.setSuccess(true);
		r.setCode(HttpStatus.SC_OK);
		return r;
	}
	public static<T> R<T> ok(String msg) {
		R<T> r = new R<T>();
		r.setSuccess(true);
		r.setCode(HttpStatus.SC_OK);
		r.setMessage(msg);
		return r;
	}
	public static<T> R<T> ok(T data) {
		R<T> r = new R<T>();
		r.setSuccess(true);
		r.setCode(HttpStatus.SC_OK);
		r.setResult(data);
		return r;
	}

	public static<T> R<T> error(String msg, T data) {
		R<T> r = new R<T>();
		r.setSuccess(false);
		r.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		r.setMessage(msg);
		r.setResult(data);
		return r;
	}

	public static<T> R<T> error(String msg) {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
	}
	public static<T> R<T> error(int code, String msg) {
		R<T> r = new R<T>();
		r.setCode(code);
		r.setMessage(msg);
		r.setSuccess(false);
		return r;
	}
}
