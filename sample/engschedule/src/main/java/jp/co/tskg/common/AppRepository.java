package jp.co.example.common;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;


/**
 * 独自Repositoryインターフェース
 *
 * @author user01
 *
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public interface AppRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

	/**
	 * 楽観的排他処理を使う場合のレコード取得
	 * 画面から取得してきたlockVersionを指定して、メソッド実行時のlockVersionと比較する。
	 * 相違があったら例外を投げる。
	 * 相違がなければ、通常の findOneと同じ動作をする。
	 *
     * @param id サロゲートキー
     * @param lockVersion 楽観ロック用バージョン値
     * @return
	 */
	public T findOneWithValidVersion(ID id, Integer lockVersion);

}
