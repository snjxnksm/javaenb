package jp.co.example.common;

import lombok.Getter;

/**
 * アプリケーションで使用する独自例外
 *
 * @author user01
 *
 */
public class AppException extends RuntimeException {
	@Getter
    final int status;

    public AppException(int status, String message) {
        super(message);
        this.status = status;
    }

    public AppException(int status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }
}
