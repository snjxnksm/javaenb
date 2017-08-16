/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.co.example.repository;

import java.sql.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jp.co.example.entity.PlannedWorks;

/**
 * ワーク予定 Repository
 */
@Repository
public interface PlannedWorksRepository extends JpaRepository<PlannedWorks, Long> {

	/** SQLID 001 実施対象ワーク一覧を検索する */
	final String SQLID_001 =
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

	/** SQLID 002 ワーク予定を更新する */
	final String SQLID_002 =
			"UPDATE"
			+ " planned_works"
			+ " SET"
			+ " milestone_flg = :milestone_flg"
			+ ",milestone_setted_on = :milestone_setted_on"
			+ ",updater_cd = :updater_cd"
			+ ",updater_pgm = :updater_pgm"
			+ ",updated_at = CURRENT_DATE"
			+ ",lock_version = (lock_version + 1)"
			+ " WHERE"
			+ " job_cd = :job_cd"
			+ " AND revision_no = :revision_no"
			+ " AND exe_task_cd = :exe_task_cd"
			+ " AND exe_work_cd = :exe_work_cd"
			+ " AND schedule_margin_flg = '0'"; //--0:エンジニアリングスケジュール作成時に設定されたワーク

	/** SQLID 003 ワーク予定からスケジュールマージンを削除する */
	final String SQLID_003 =
			"DELETE FROM"
			+ " planned_works"
			+ " WHERE"
			+ " job_cd = :job_cd"
			+ " AND revision_no = :revision_no"
			+ " AND exe_task_cd = :exe_task_cd"
			+ " AND exe_work_cd = :exe_work_cd"
			+ " AND schedule_margin_flg = '1'"; //--0:エンジニアリングスケジュール作成時に設定されたワーク

	/** SQLID 004 ワーク予定にスケジュールマージンを追加する */
	final String SQLID_004 = "INSERT INTO planned_works ("
			+"job_cd"
			+",revision_no"
			+",exe_task_cd"
			+",exe_work_cd"
			+",schedule_margin_flg"
			+",edit_work_days"
			+",pln_work_started_on"
			+",pln_work_ended_on"
			+",milestone_flg"
			+",milestone_setted_on"
			+",internal_outsource_flg"
			+",revice_type"
			+",pln_work_coordinate_x"
			+",pln_work_coordinate_y"
			+",critical_path_flg"
			+",creator_cd"
			+",creator_pgm"
			+",updater_cd"
			+",updater_pgm"
			+",lock_version"
			+ ")"
			+ "SELECT"
			+" job_cd"
			+",revision_no"
			+",exe_task_cd"
			+",exe_work_cd"
			+",'1' schedule_margin_flg"         //1:日程展開時にスケジュールマージンとして追加されたワーク
			+",:edit_work_days edit_work_days"
			+",:pln_work_started_on pln_work_started_on"
			+",:pln_work_ended_on pln_work_ended_on"
			+",milestone_flg"
			+",milestone_setted_on"
			+",internal_outsource_flg"
			+",revice_type"
			+",0 pln_work_coordinate_x"
			+",0 pln_work_coordinate_y"
			+",critical_path_flg"
			+",:creator_cd creator_cd"
			+",:creator_pgm creator_pgm"
			+",:creator_cd updater_cd"
			+",:creator_pgm updater_pgm"
			+",lock_version"
			+ " FROM"
			+ " planned_works"
			+ " WHERE"
			+ " job_cd = :job_cd"
			+ " AND revision_no = :revision_no"
			+ " AND exe_task_cd = :exe_task_cd"
			+ " AND exe_work_cd = :exe_work_cd"
			+ " AND schedule_margin_flg = '0'" //--0:エンジニアリングスケジュール作成時に設定されたワーク
			+ "";

	/**
	 * ワーク予定をユニークキーによって取得する
	 *
	 * @param jobCd
	 *            JOBコード
	 * @param revisionNo
	 *            改訂番号
	 * @param exeTaskCd
	 *            実施対象タスクコード
	 * @param exeWorkCd
	 *            実施対象ワークコード
	 * @param scheduleMarginFlg
	 *            スケジュールマージンフラグ
	 * @return ワーク予定エンティティ
	 */
	public PlannedWorks findByJobCdAndRevisionNoAndExeTaskCdAndExeWorkCdAndScheduleMarginFlg(
			final String jobCd,
			final int revisionNo,
			final String exeTaskCd,
			final int exeWorkCd,
			final String scheduleMarginFlg);

// FIXME:Date型がnullの場合にうまくいかない
//
//	/**
//	 * ワーク予定を更新する
//	 *
//	 * @param milestoneFlg
//	 *            主要マイルストーンフラグ
//	 * @param milestoneSettedOn
//	 *            主要マイルストーン年月日
//	 * @param updaterCd
//	 *            更新者
//	 * @param updaterPgm
//	 *            更新プログラムID
//	 * @param jobCd
//	 *            JOBコード
//	 * @param revisionNo
//	 *            改訂番号
//	 * @param exeTaskCd
//	 *            実施対象タスクコード
//	 * @param exeWorkCd
//	 *            実施対象ワークコード
//	 * @return 更新件数
//	 */
//	@Modifying
//	@Query(value = SQLID_002, nativeQuery = true)
//	public int updateByUK(
//			final @Param("milestone_flg") boolean milestoneFlg,
//			final @Param("milestone_setted_on") Date milestoneSettedOn,
//			final @Param("updater_cd") String updaterCd,
//			final @Param("updater_pgm") String updaterPgm,
//			final @Param("job_cd") String jobCd,
//			final @Param("revision_no") int revisionNo,
//			final @Param("exe_task_cd") String exeTaskCd,
//			final @Param("exe_work_cd") String exeWorkCd);

	/**
	 * ワーク予定からスケジュールマージンを削除する
	 *
	 * @param ent
	 *            ワーク予定エンティティ
	 * @return 削除件数
	 */
	@Modifying
	@Query(value = SQLID_003, nativeQuery = true)
	public int deleteScheduleMargin(
			final @Param("job_cd") String jobCd,
			final @Param("revision_no") int revisionNo,
			final @Param("exe_task_cd") String exeTaskCd,
			final @Param("exe_work_cd") int exeWorkCd);

	/**
	 * ワーク予定にスケジュールマージンを追加する
	 *
	 * @param ent
	 *            ワーク予定エンティティ
	 * @return 追加件数
	 */
	@Modifying
	@Query(value = SQLID_004, nativeQuery = true)
	public int insertScheduleMargin(
			final @Param("edit_work_days") int edit_work_days,
			final @Param("pln_work_started_on") Date pln_work_started_on,
			final @Param("pln_work_ended_on") Date pln_work_ended_on,
//			final @Param("creator_cd") String creator_cd,
//			final @Param("creator_pgm") String creator_pgm,
			final @Param("job_cd") String job_cd,
			final @Param("revision_no") int revision_no,
			final @Param("exe_task_cd") String exe_task_cd,
			final @Param("exe_work_cd") String exe_work_cd);

}
