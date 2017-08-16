package jp.co.example.service.eb10401wja;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jp.co.example.common.AppException;
import jp.co.example.model.eb10401wja.JobInfoDto;
import jp.co.example.model.eb10401wja.JobInfoReq;
import jp.co.example.model.eb10401wja.JobInfoRes;
import jp.co.example.repository.JobsRepository;

@Service
public class EB10401WJAInitJobInfoService {
	@Autowired
	JobsRepository jobRepos;

	public JobInfoRes getJobInfo(JobInfoReq req) {
		JobInfoDto record = jobRepos.findByJobCd(req.getJobCd());
		if (record != null) {
			JobInfoRes res = new JobInfoRes();
			BeanUtils.copyProperties(record, res);
			// JOBリビジョンはそのまま結果に移送する。
			res.setJobRev(req.getJobRev());
			return res;
		} else {
			throw new AppException(HttpStatus.NOT_FOUND.value(), "存在しないJOBコードです。");
		}
	}
}
