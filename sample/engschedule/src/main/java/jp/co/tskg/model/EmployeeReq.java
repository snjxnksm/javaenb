package jp.co.example.model;

import lombok.Data;

@Data
public class EmployeeReq {
	private int id;
	private int lockVersion;
	private String empName;
}
