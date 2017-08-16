package jp.co.example.service.eb10401wja;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.example.common.DtoUtils;
import jp.co.example.model.eb10401wja.ExtraTasksReq;
import jp.co.example.model.eb10401wja.ExtraTasksRes;
import jp.co.example.model.eb10401wja.StandardTaskDto;
import jp.co.example.repository.JobsRepository;
import jp.co.example.repository.StandardTasksRepository;

@Service
public class EB10401WJAExtraTaskSearchService {
	@Autowired
	StandardTasksRepository stdTasksRepos;

	@Autowired
	JobsRepository jobRepos;

	public List<ExtraTasksRes> searchExtraTasks(ExtraTasksReq req) {
		TaskSource taskSource = TaskSource.valueOf(req.getTaskSource());

		if (taskSource == TaskSource.std) {
			// 標準モデルからの検索
			List<StandardTaskDto> rec = stdTasksRepos.findByTaskNm(req.getTaskNm());
			return DtoUtils.copyDtoList(rec, ExtraTasksRes.class, res -> {
				res.setIsPlanned(false);
			});
		} else {
			// 過去ジョブからの検索（未実装）
			throw new UnsupportedOperationException();
		}
	}
}
