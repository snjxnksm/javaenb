package jp.co.example.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jp.co.example.common.AppException;
import jp.co.example.entity.JobRevisions;
import jp.co.example.model.EB10501WJAExeWorkInfoRes;
import jp.co.example.model.EB10501WJAInitReq;
import jp.co.example.model.EB10501WJAJobInfoRes;
import jp.co.example.model.eb10401wja.JobInfoDto;
import jp.co.example.repository.JobsRepository;
import jp.co.example.repository.JobRevisionsRepository;
import jp.co.example.repository.PlannedWorksNativeSqlRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * 実施対象ワーク設定画面．初期処理
 *
 * @author try034
 *
 */
@Slf4j
@Service
public class EB10501WJAInitService {

	@Autowired
	JobsRepository jobsRepository;

	@Autowired
	JobRevisionsRepository jobsRevisionsRepository;

	/** */
	@Autowired
	PlannedWorksNativeSqlRepository plnWorkRepository;

	/**
	 * JOB名を取得する
	 *
	 * @param req
	 *            リクエストパラメータ（JOBコードと改定番号）
	 * @return JOB名
	 */
	public EB10501WJAJobInfoRes getJobInfo(EB10501WJAInitReq req) { //
		EB10501WJAJobInfoRes res = new EB10501WJAJobInfoRes();
		JobInfoDto job = jobsRepository.findByJobCd(req.getJobCd());
		if (job == null) {
			if (log.isDebugEnabled()) {
				log.debug(
						String.format("該当するJOBは存在しません。[jobCd=%s,revisionNo=%d]", req.getJobCd(), req.getRevisionNo()));
			}
			throw new AppException(HttpStatus.NOT_FOUND.value(), "該当するJOBは存在しません。");
		}

		JobRevisions jobRevs = jobsRevisionsRepository.findByJobCdAndRevisionNo(req.getJobCd(), req.getRevisionNo());
		if (null == jobRevs) {
			if (log.isDebugEnabled()) {
				log.debug(String.format("該当するJOBリビジョンは存在しません。[jobCd=%s,revisionNo=%d]", req.getJobCd(),
						req.getRevisionNo()));
			}
			throw new AppException(HttpStatus.NOT_FOUND.value(), "該当するJOBは存在しません。");
		}

		BeanUtils.copyProperties(job, res);
		BeanUtils.copyProperties(jobRevs, res);
		return res;
	}

	/**
	 * 実施対象ワーク一覧を取得する
	 *
	 * @param req
	 *            リクエストパラメータ（JOBコードと改定番号）
	 * @return 実施対象ワーク一覧
	 */
	public List<EB10501WJAExeWorkInfoRes> getExeWorkList(EB10501WJAInitReq req) {
		List<EB10501WJAExeWorkInfoRes> res = plnWorkRepository.findByJobCdAndRevisionNo(req);
		// rowspanを計算する
		int idx = -1;
		String exeTaskCd = "";
		for (int i = 0; i < res.size(); i++) {
			if (!exeTaskCd.equals(res.get(i).getExeTaskCd())) {
				if (0 <= idx) {
					res.get(idx).setExeWorkCnt(i - idx);
				}
				exeTaskCd = res.get(i).getExeTaskCd();
				idx = i;
			}
		}
		if (0 <= idx) {
			res.get(idx).setExeWorkCnt(res.size() - idx);
		}
		return res;
	}
}
