package jp.co.example.common;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

/**
 * 共通の呼び出し
 *
 * @author user01
 *
 */
@Component
public class AppUtils {

	/**
	 * 現在時刻を取得
	 *
	 * @return
	 */
	public static LocalDateTime getNow() {
		return LocalDateTime.now();
	}
}
