package jp.co.example.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jp.co.example.common.AppException;
import jp.co.example.model.EB10501WJAJobInfoReq;
import jp.co.example.model.EB10501WJAJobInfoRes;
import jp.co.example.model.eb10401wja.JobInfoDto;
import jp.co.example.repository.JobsRepository;

@Service
public class EB10501WJAJobInfoService {

    @Autowired
    JobsRepository jobsRepository;

    // Job一件取得
    public EB10501WJAJobInfoRes getJobNm(EB10501WJAJobInfoReq req) {    //
    	EB10501WJAJobInfoRes res = new EB10501WJAJobInfoRes();
    	JobInfoDto job = jobsRepository.findByJobCd(req.getJobCd());
    	if (job == null) {
    		throw new AppException(HttpStatus.NOT_FOUND.value(),"test test test ");
    	}
		BeanUtils.copyProperties(job,res);
        return res;
    }
}
