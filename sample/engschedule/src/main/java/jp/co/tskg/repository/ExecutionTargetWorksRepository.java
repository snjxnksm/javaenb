/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an"AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.co.example.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import jp.co.example.common.AppException;
import jp.co.example.model.EB10501WJAInitReq;
import jp.co.example.model.EB10501WJAInitRes;
import jp.co.example.model.EB10501WJAInitRes;

/**
 * ワーク予定
 *
 * ↓参考URL
 * http://qiita.com/tag1216/items/55742fdb442e5617f727
 */
@Repository
public class ExecutionTargetWorksRepository {

	// EntityManagerを直接使う
	@Autowired
	EntityManager entityManager;

	final String sql =
			"SELECT"
			+ " t1.exe_task_cd"
			+ ",t1.exe_task_nm"
			+ ",t2.exe_work_cd"
			+ ",t2.exe_work_nm"
			+ ",t2.init_work_days"
			+ ",t4.edit_work_days"
			+ ",t4.milestone_flg"
			+ ",t4.milestone_setted_on"
			+ ",t4.revice_type"
			+ ",t4.revice_rsn"
			+ ",t0.lock_version"
			+ ",t2.exe_work_sts"
			+ ",t4.id"
			+ " FROM"
			+ " job_revisions t0"
			+ " INNER JOIN execution_target_tasks t1 ON"
			+ " t1.job_cd = t0.job_cd"
			+ " INNER JOIN execution_target_works t2 ON"
			+ " t2.job_cd = t1.job_cd"
			+ " AND t2.exe_task_cd = t1.exe_task_cd"
			+ " INNER JOIN planned_tasks t3 ON"
			+ " t3.job_cd = t1.job_cd"
			+ " AND t3.exe_task_cd = t1.exe_task_cd"
			+ " INNER JOIN planned_works t4 ON"
			+ " t4.job_cd = t2.job_cd"
			+ " AND t4.revision_no = t3.revision_no"
			+ " AND t4.exe_task_cd = t2.exe_task_cd"
			+ " AND t4.exe_work_cd = t2.exe_work_cd"
			+ " AND t4.schedule_margin_flg = '0'" //--0:エンジニアリングスケジュール作成時に設定されたワーク
			+ " WHERE"
			+ " t1.job_cd = :job_cd"
			+ " AND t3.revision_no = :revision_no"
			+ " AND t3.revice_type <> 'C'" //--C:削除
			+ " ORDER BY"
			+ " t1.exe_task_display_order ASC"
			+ ",t2.exe_work_display_order ASC";
	/**
     * 二つのテーブルの項目を、一つのオブジェクトで取得する
	 *
	 * @param sectionId
	 * @return
	 */
	public List<EB10501WJAInitRes> findByJobCdAndRevisionNo(EB10501WJAInitReq req) {


		@SuppressWarnings("unchecked")
		List<EB10501WJAInitRes> result = entityManager
					.createNativeQuery(sql, EB10501WJAInitRes.class)
			        .setParameter("job_cd", req.getJobCd())
			        .setParameter("revision_no", req.getRevisionNo())
			        .getResultList();

		List<EB10501WJAInitRes> result2 = entityManager
				.createNativeQuery(sql, EB10501WJAInitRes.class)
		        .setParameter("job_cd", req.getJobCd())
		        .setParameter("revision_no", req.getRevisionNo())
		        .getResultList();

		if (result.isEmpty()) {
    		// たとえば、発見できなかった場合に404 NOT_FOUNDを上げたい場合は
    		// 以下のように記述する。
    		throw new AppException(HttpStatus.NOT_FOUND.value(),"test test test");
		}
		return result;
	}
}
