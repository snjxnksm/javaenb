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

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jp.co.example.common.AppRepository;
import jp.co.example.entity.Employee;

/**
 * SpringJPAにおける検索方法のサンプル
 * フレームワークに添った形でのサンプル
 *
 * ↓参考URL
 * http://qiita.com/tag1216/items/55742fdb442e5617f727
 */
@Repository
public interface EmployeeRepository extends AppRepository<Employee, Integer> {

	// SpringDATAの命名規約に従ったメソッド名での自動実装
	// EmployeeエンティティのIsFoundで検索する場合。
	// select * from employee where is_found =isFound order by id asc;と同等
	public List<Employee> findByIsFoundOrderByIdAsc(Boolean isFound);

	// ネイティブSQL
	// 検索結果が、エンティティに収まる場合。
	// sectionテーブルのidを指定して、部署ごとの従業員リストを得る
	@Query(value="select * from employee where id in (select employee_id from section_employee where section_id=:SEC_ID)", nativeQuery = true)
	public List<Employee> findBySectionId(@Param("SEC_ID")Integer sectionId);

	// さらに複雑な処理が必要な場合は、JpaSpecificationExecutorを使って
	// mixin用のインターフェースと、それを継承した実装クラスを作ってその中に処理を書く。
	// あまり使いようがない気がするのでここでは詳細を書かない
	//
	//	public interface EmployeeRepository extends JpaRepository<Employee, Long>,
	//												JpaSpecificationExecutor<Employee>,
	//												EmployeeRepositoryCustom {

}
