package jp.co.example.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import jp.co.example.common.AppException;
import jp.co.example.common.AppLogMessageConstants;
import jp.co.example.model.EmployeeReq;
import jp.co.example.model.EmployeeWithSectionNameRes;
import lombok.extern.slf4j.Slf4j;

/**
 * ネイティブSQLで自由自在に問い合わせる。
 *
 * ↓参考URL
 * http://ksoichiro.blogspot.jp/2016/12/spring-data-jpa.html
 *
 */
@Slf4j // ログ出力に必要
@Repository
public class PracticeNativeSqlRepository {

	// ネイティブSQLを使用するためにEntityManagerを直接使う
	@Autowired
	EntityManager entityManager;

	/**
     * 二つのテーブルの項目を、一つのオブジェクトで取得する
	 *
	 * @param sectionId
	 * @return
	 */
	public List<EmployeeWithSectionNameRes> findBySectionIdWithSectionName(Long sectionId) {

		@SuppressWarnings("unchecked")// getResultListの戻り値が総称型の情報を持っていないのでビルドすると警告が出力される。それを抑止。
		List<EmployeeWithSectionNameRes> result = entityManager
					.createNativeQuery(
						"select se.id, e.emp_name,s.section_name from employee e,section s,section_employee se where s.id = se.section_id and e.id = se.employee_id and  s.id=:SEC_ID",
						EmployeeWithSectionNameRes.class)
			        .setParameter("SEC_ID", sectionId)
			        .getResultList();

		if (result.isEmpty()) {
    		// たとえば、発見できなかった場合に404 NOT_FOUNDを上げたい場合は
    		// 以下のように記述する。
    		throw new AppException(HttpStatus.NOT_FOUND.value()
    								,AppLogMessageConstants.SPECIFIED_ID_DOSE_NOT_EXIST.getMessage());
		}

		return result;
	}

	/**
	 * update文で更新する。
	 *
	 * @param req
	 * @param sec_id
	 * @return 更新レコード数
	 */
	public int updateBySectionIdWithSectionName(EmployeeReq req, Long sec_id) {

		// メソッドチェーンでつなげて書く。
//		return entityManager
//					.createNativeQuery(
//						"update employee set emp_name=:NAME where id in(select employee_id from section_employee where section_id=:SEC_ID) ")
//			        .setParameter("SEC_ID", sec_id)
//			        .setParameter("NAME", req.getEmpName())
//			        .executeUpdate();

		// 糖衣構文
		// 上記と同じことを、ステップごとに分解してみた
		int chgCnt = 0;

		String sql = "update employee set emp_name=:NAME where id in(select employee_id from section_employee where section_id=:SEC_ID) ";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("NAME", req.getEmpName());
		query.setParameter("SEC_ID", sec_id);
		chgCnt = query.executeUpdate();

		// ログ出力サンプル。
		// アノテーションに、「@Slf4j」を書くこと
		if (0 == chgCnt) {
			log.info(AppLogMessageConstants.DATA_HAS_NOT_BEEN_UPDATE.getMessage());
//			log.debug(msg);
//			log.error(msg);
//			log.trace(msg);
		}

		return chgCnt;
	}

}
