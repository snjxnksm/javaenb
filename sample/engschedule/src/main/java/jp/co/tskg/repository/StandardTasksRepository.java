package jp.co.example.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jp.co.example.model.eb10401wja.StandardTaskDto;

@Repository
public class StandardTasksRepository {

	@Autowired
	EntityManager entityManager;

	public List<StandardTaskDto> findByTaskNm(String taskNm) {
		@SuppressWarnings("unchecked")
		List<StandardTaskDto> result = entityManager.createNativeQuery(
				"SELECT"
				+ "   st.std_task_cd,"
				+ "   st.std_task_nm,"
				+ "   tg.task_grp_cd,"
				+ "   tg.task_grp_nm"
				+ " FROM standard_tasks st"
				+ "   INNER JOIN task_groups tg ON tg.task_grp_cd = st.task_grp_cd"
				+ " WHERE"
				+ "   st.std_task_nm LIKE '%' || :taskNm || '%'"
				+ " ORDER BY tg.task_grp_display_order, st.std_task_display_order",
				StandardTaskDto.class)
				.setParameter("taskNm", taskNm)
				.getResultList();
		return result;
	}
}
