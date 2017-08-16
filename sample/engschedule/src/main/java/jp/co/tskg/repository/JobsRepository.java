package jp.co.example.repository;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jp.co.example.model.eb10401wja.JobInfoDto;

/**
 * JOBリポジトリ
 *
 * @author fetc04
 *
 */
@Repository
public class JobsRepository {

	@Autowired
	EntityManager entityManager;

	public JobInfoDto findByJobCd(String jobCd) {
		JobInfoDto res = (JobInfoDto)entityManager.createNativeQuery(
				"select j1.job_cd, j1.job_nm, j1.basis_past_job_cd, j2.job_nm as basis_past_job_nm"
						+ " from jobs as j1"
						+ " left join jobs as j2 on j2.job_cd = j1.basis_past_job_cd"
						+ " where j1.job_cd = :jobCd", JobInfoDto.class)
				.setParameter("jobCd", jobCd)
				.getSingleResult();
		return res;
	}

}
