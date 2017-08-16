package jp.co.example.service.eb10401wja;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.example.model.eb10401wja.StdModelListReq;
import jp.co.example.model.eb10401wja.StdModelListRes;
import jp.co.example.repository.StandardModelsRepository;

@Service
public class EB10401WJAInitStandardModelListService {

	@Autowired
	StandardModelsRepository standardModelsRepos;

	public List<StdModelListRes> getStandardModels(StdModelListReq req) {
    	List<StdModelListRes> list = standardModelsRepos.findByGapsTypeOfJob(req.getJobCd());
        return list;

	}

}
