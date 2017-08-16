package jp.co.example.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 *
 * 複数のテーブルから項目を集める
 *
 * ↓参考URL
 * http://ksoichiro.blogspot.jp/2016/12/spring-data-jpa.html
 *
 * employee.emp_name と section.section_name で一つのクラスを作る。
 *
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class EmployeeWithSectionNameRes implements Serializable {

	@Id
	private Long id;

	private String empName;

	private String sectionName;
}
