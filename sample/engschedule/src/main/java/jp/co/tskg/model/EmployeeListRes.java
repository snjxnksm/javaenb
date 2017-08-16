package jp.co.example.model;

import lombok.Data;

@Data
public class EmployeeListRes {
	Record[] empNames;

	public static class Record {
		String id;
	}
}
