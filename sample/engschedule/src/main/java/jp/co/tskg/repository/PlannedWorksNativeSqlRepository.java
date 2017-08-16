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
import javax.persistence.TemporalType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import jp.co.example.common.AppException;
import jp.co.example.common.AppLocalDateToDateConverter;
import jp.co.example.entity.PlannedWorks;
import jp.co.example.model.EB10501WJAExeWorkInfoRes;
import jp.co.example.model.EB10501WJAInitReq;
import lombok.extern.slf4j.Slf4j;
/**
 * ワーク予定 Repository
 */
@Slf4j
@Repository
public class PlannedWorksNativeSqlRepository{

	/**
	 * ワーク予定 Repository
	 */
	@Autowired
	PlannedWorksRepository plnWorkRepository;

	/**
	 * EntityManager
	 */
	@Autowired
	EntityManager entityManager;

	/**
	 * 実施対象ワーク一覧をJOBコードと改定番号によって取得する
	 *
	 * @param req
	 *            リクエストパラメータ（JOBコードと改定番号）
	 * @return 実施対象ワーク一覧
	 */
	public List<EB10501WJAExeWorkInfoRes> findByJobCdAndRevisionNo(EB10501WJAInitReq req) {
		if (log.isDebugEnabled()) {	log.debug(PlannedWorksRepository.SQLID_001);}
		@SuppressWarnings("unchecked")
		List<EB10501WJAExeWorkInfoRes> result = entityManager
				.createNativeQuery(PlannedWorksRepository.SQLID_001, EB10501WJAExeWorkInfoRes.class)
				.setParameter("job_cd", req.getJobCd())
				.setParameter("revision_no", req.getRevisionNo())
				.getResultList();

		if (result.isEmpty()) {
			// 発見できなかった場合に404 NOT_FOUNDを上げる
			throw new AppException(HttpStatus.NOT_FOUND.value(),
					String.format("該当する実施対象ワークがありません。[jobCd=%s,revisionNo=%d]", req.getJobCd(), req.getRevisionNo()));
		}
		return result;
	}

	/**
	 * ワーク予定を更新する
	 *
	 * @param ent
	 *            ワーク予定エンティティ
	 * @return 更新件数
	 */
	public int updateByUK(PlannedWorks ent) {
		if (log.isDebugEnabled()) {	log.debug(PlannedWorksRepository.SQLID_002);}
		int result = entityManager.createNativeQuery(PlannedWorksRepository.SQLID_002)
				.setParameter("milestone_flg", ent.isMilestoneFlg())
				.setParameter("milestone_setted_on",
						AppLocalDateToDateConverter.getDate(ent.getMilestoneSettedOn()),
						TemporalType.DATE)
//				.setParameter("updater_cd", ent.getUpdaterCd())     //TODO:更新者
//				.setParameter("updater_pgm", ent.getUpdaterPgm())   //TODO:更新プログラムID
				.setParameter("job_cd", ent.getJobCd())
				.setParameter("revision_no", ent.getRevisionNo())
				.setParameter("exe_task_cd", ent.getExeTaskCd())
				.setParameter("exe_work_cd", ent.getExeWorkCd())
				.executeUpdate();
		if (log.isDebugEnabled()) {	log.debug("update " + result + " record");}
		return result;
	}

	/**
	 * ワーク予定からスケジュールマージンを削除する
	 *
	 * @param ent
	 *            ワーク予定エンティティ
	 * @return 削除件数
	 */
	public int deleteScheduleMargin(PlannedWorks ent) {
		if (log.isDebugEnabled()) {	log.debug(PlannedWorksRepository.SQLID_003);}
		int result = entityManager.createNativeQuery(PlannedWorksRepository.SQLID_003)
				.setParameter("job_cd", ent.getJobCd())
				.setParameter("revision_no", ent.getRevisionNo())
				.setParameter("exe_task_cd", ent.getExeTaskCd())
				.setParameter("exe_work_cd", ent.getExeWorkCd())
				.executeUpdate();
		if (log.isDebugEnabled()) {	log.debug("delete " + result + " record");}
		return result;
	}

	/**
	 * ワーク予定にスケジュールマージンを追加する
	 *
	 * @param ent
	 *            ワーク予定エンティティ
	 * @return 追加件数
	 */
	public int insertScheduleMargin(PlannedWorks ent) {
		ent.setEditWorkDays(10);                             //TODO:日数（非営業日は含めない）の計算
		ent.setPlnWorkStartedOn(ent.getMilestoneSettedOn()); //TODO:翌営業日
		ent.setPlnWorkEndedOn(ent.getMilestoneSettedOn());
//		ent.setCreatorCd(ent.getUpdaterCd());
//		ent.setCreatorPgm(ent.getUpdaterPgm());

		if (log.isDebugEnabled()) {	log.debug(PlannedWorksRepository.SQLID_004);}
		return plnWorkRepository.insertScheduleMargin(
				ent.getEditWorkDays(),
				AppLocalDateToDateConverter.getDate(ent.getPlnWorkStartedOn()),
				AppLocalDateToDateConverter.getDate(ent.getPlnWorkEndedOn()),
//				ent.getCreatorCd(),
//				ent.getCreatorPgm(),
				ent.getJobCd(),
				ent.getRevisionNo(),
				ent.getExeTaskCd(),
				ent.getExeWorkCd());
	}
}
