package jp.co.example.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jp.co.example.model.eb10401wja.BaseModelTasksDto;

@Repository
public class StandardModelsStandardTasksRepository {

	@Autowired
	EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<BaseModelTasksDto> findStdModelTasks(String stdModelCd) {
		return entityManager.createNativeQuery(
				"select"
				+ "   st.std_task_cd,"
				+ "   st.std_task_nm,"
				+ "   tg.task_grp_cd,"
				+ "   tg.task_grp_nm"
				+ " from standard_models_standard_tasks smst"
				+ "   inner join standard_tasks as st on st.std_task_cd = smst.std_task_cd"
				+ "   inner join task_groups as tg on tg.task_grp_cd = st.task_grp_cd"
				+ " where"
				+ "   smst.std_model_cd = :stdModelCd"
				+ " order by tg.task_grp_display_order, st.std_task_display_order",
				BaseModelTasksDto.class)
				.setParameter("stdModelCd", stdModelCd)
				.getResultList();
	}
}
