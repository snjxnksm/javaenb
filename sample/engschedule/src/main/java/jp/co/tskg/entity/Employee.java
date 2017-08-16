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
package jp.co.example.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jp.co.example.common.AppBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "employee")
public class Employee extends AppBaseEntity implements Serializable {

    @NotNull
    @Size(min = 1, max = 50)
    private String empName;

    @NotNull
    private Boolean isFound;

//    @OneToMany(fetch=FetchType.LAZY, mappedBy="employee", targetEntity=EmployeeSection.class)
//    private List<EmployeeSection> employeeSectionz;


}