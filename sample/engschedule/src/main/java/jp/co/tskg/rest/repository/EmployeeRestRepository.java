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
package jp.co.example.rest.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import jp.co.example.entity.Employee;

/**
 * spring-boot-starter-data-rest を使ったサンプル。
 * PagingAndSortingRepositoryを継承すると、Controllerなどを作らなくとも
 * 自動的に application/hal+json タイプのCRUDワンセットのAPIを生成する。
 *
 * @author user01
 *
 */
public interface EmployeeRestRepository extends PagingAndSortingRepository<Employee, Long> {


}
