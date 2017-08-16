package jp.co.example.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ログメッセージ一覧
 *
 * @author user01
 *
 */
@Getter
@AllArgsConstructor
public enum AppLogMessageConstants {
	// メソッド呼び出し
	// "{}"には指定した引数の内容が展開される。
	BEFORE_INVOKE("before invoke, parameters = {}, by {} #{}"),
	AFTER_INVOKE("after invoke, result = {}, #{}"),
	// 独自リポジトリ
	DOSE_NOT_FOUNT_VERSION_FILED("Does not found lockVersion field in entity class. class is {}."),
	RESOURCE_CONFLICT("Resource Conflict."), // 楽観ロックで引っかかった場合
	// 共通エラー処理
	INTENTIONAL_ERROR("意図したエラー発生"),
	UNINTENTIONAL_ERROR("意図しないエラー発生"),
	// その他エラーメッセージ
	SPECIFIED_ID_DOSE_NOT_EXIST("Specified ID does not exist."),
	DATA_HAS_NOT_BEEN_UPDATE("Data has not been updated.");

	private final String message;

}
