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

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jp.co.example.entity.JobRevisions;

/**
 * JOBリビジョン Repository
 */
@Repository
public interface JobRevisionsRepository extends JpaRepository<JobRevisions, Long> {

	/** SQLID 001 JOBリビジョンを繰り上げる */
	final String SQLID_001 =
			"UPDATE job_revisions"
			+ " SET"
			+ " lock_version = (lock_version + 1)"
			+ ",updater_cd = :updater_cd"
			+ ",updater_pgm = :updater_pgm"
			+ ",updated_at = CURRENT_DATE"
			+ " WHERE"
			+ " job_cd = :job_cd"
			+ " AND revision_no = :revision_no"
			+ " AND lock_version = :lock_version";

	/**
	 * JOBリビジョンをJOBコード及び改定番号によって取得する
	 *
	 * @param jobCd
	 *            JOBコード
	 * @param revisionNo
	 *            改訂番号
	 * @return JOBリビジョン
	 */
	public JobRevisions findByJobCdAndRevisionNo(final String jobCd,final int revisionNo);

	/**
	 * JOBリビジョンを繰り上げる
	 *
	 * @param updaterCd
	 *            更新者
	 * @param updaterPgm
	 *            更新プログラムID
	 * @param jobCd
	 *            JOBコード
	 * @param revisionNo
	 *            改訂番号
	 * @param lockVersion
	 *            ロックバージョン
	 * @return 更新件数
	 */
	@Modifying
	@Query(value = SQLID_001, nativeQuery = true)
	public int updateLockVersion(
			final @Param("updater_cd") String updaterCd,
			final @Param("updater_pgm") String updaterPgm,
			final @Param("job_cd") String jobCd,
			final @Param("revision_no") int revisionNo,
			final @Param("lock_version") int lockVersion);
}
