package jp.co.example.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jp.co.example.common.AppException;
import jp.co.example.entity.PlannedWorks;
import jp.co.example.model.EB10501WJASaveMilestonesMdl;
import jp.co.example.model.EB10501WJASaveMilestonesReq;
import jp.co.example.repository.JobRevisionsRepository;
import jp.co.example.repository.JobsRepository;
import jp.co.example.repository.PlannedWorksNativeSqlRepository;
import jp.co.example.repository.PlannedWorksRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * 実施対象ワーク設定画面．マイルストーン保存処理
 *
 * @author try034
 *
 */
@Slf4j
@Service
public class EB10501WJASaveMilestonesService {

	@Autowired
	JobsRepository jobsRepository;

	/** プログラムID */
	private String pgmId = "EB10501WJA";

	/** */
	@Autowired
	PlannedWorksNativeSqlRepository plnWorkNSRepository;

	/** */
	@Autowired
	PlannedWorksRepository plnWorkRepository;

	@Autowired
	JobRevisionsRepository jobRevevisionsRepository;

	/**
	 * マイルストーンを保存する
	 *
	 * @param req
	 *            リクエストパラメータ
	 * @param empId
	 *            従業員コード
	 * @return JOBリビジョンのロックバージョン
	 */
	public int save(final EB10501WJASaveMilestonesReq req, final String empId) {

		int lockVertion = req.getLockVersion();
		int count = 0;
		for (EB10501WJASaveMilestonesMdl record : req.getWorkList()) {

			PlannedWorks ent = new PlannedWorks();
			BeanUtils.copyProperties(req, ent);
			BeanUtils.copyProperties(record, ent);
//			ent.setUpdaterPgm(pgmId);
//			ent.setUpdaterCd(empId);
			if ("B".equals(record.getEditSts())) {
				if (log.isDebugEnabled()) {
					log.debug("追加");
				}
				plnWorkNSRepository.updateByUK(ent);
				plnWorkNSRepository.insertScheduleMargin(ent);
				count++;
			} else if ("C".equals(record.getEditSts())) {
				if (log.isDebugEnabled()) {
					log.debug("変更");
				}
				plnWorkNSRepository.updateByUK(ent);
				count++;
			} else if ("D".equals(record.getEditSts())) {
				if (log.isDebugEnabled()) {
					log.debug("削除");
				}
				plnWorkNSRepository.updateByUK(ent);
				plnWorkNSRepository.deleteScheduleMargin(ent);
				count++;
			}
		}
		if (0 < count) {
			int success = jobRevevisionsRepository.updateLockVersion(empId, pgmId, req.getJobCd(), req.getRevisionNo(),
					lockVertion);
			if (0 == success) {
				if (log.isDebugEnabled()) {
					log.debug(String.format("既に別のユーザによって更新されています。[jobCd=%s,revisionNo=%d]", req.getJobCd(),
							req.getRevisionNo()));
				}
				throw new AppException(HttpStatus.CONFLICT.value(), "既に別のユーザによって更新されています。");
			}
			// UPDATEで＋１しているため
			lockVertion += success;
		}
		return lockVertion;
	}
}
