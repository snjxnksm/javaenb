package jp.co.example.service.eb10401wja;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.example.model.eb10401wja.BaseModelTasksDto;
import jp.co.example.model.eb10401wja.BaseModelTasksReq;
import jp.co.example.model.eb10401wja.BaseModelTasksRes;
import jp.co.example.repository.StandardModelsStandardTasksRepository;

@Service
public class EB10401WJASelectedBaseService {

	@Autowired
	StandardModelsStandardTasksRepository standardTasksRepos;

	public List<BaseModelTasksRes> getStdModelTasks(BaseModelTasksReq req) {
    	List<BaseModelTasksRes> list = new ArrayList<BaseModelTasksRes>();
		List<BaseModelTasksDto> recs = standardTasksRepos.findStdModelTasks(req.getStdModelCd());
		for (BaseModelTasksDto rec : recs) {
			BaseModelTasksRes res = new BaseModelTasksRes();
			BeanUtils.copyProperties(rec, res);
			res.setIsPlanned(true);
			list.add(res);
		}
		return list;
	}
}
