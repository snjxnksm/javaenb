package jp.co.example.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;


/**
 * 起動時にApplication.ymlのsetting以降の値を取り込む
 *
 * @author user01
 *
 */
@Component
@ConfigurationProperties(prefix = "settings")
@Data
public class AppSettings {
	private String errorRedirectTarget;
}
