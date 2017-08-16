package jp.co.example.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jp.co.example.model.eb10401wja.StdModelListRes;

/**
 * 標準モデルリポジトリ
 *
 * @author fetc04
 *
 */
@Repository
public class StandardModelsRepository {

	@Autowired
	EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<StdModelListRes> findByGapsTypeOfJob(String jobCd) {
		return entityManager.createNativeQuery("SELECT"
				+ "   m.std_model_cd,"
				+ "   m.std_model_nm"
				+ " FROM standard_models m"
				+ "   INNER JOIN jobs j ON j.job_cd = :jobCd"
				+ " WHERE"
				+ "   j.gaps_type = '1' AND m.gaps_type IN ('1', '3')"
				+ "   OR"
				+ "   j.gaps_type = '3' AND m.gaps_type IN ('2', '3')"
				+ " ORDER BY m.std_model_display_order",
				StdModelListRes.class)
				.setParameter("jobCd", jobCd)
				.getResultList();
	}

}
