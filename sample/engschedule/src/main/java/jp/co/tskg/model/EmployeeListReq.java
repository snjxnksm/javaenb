package jp.co.example.model;

import lombok.Data;

@Data
public class EmployeeListReq {
	Record[] empIds;

	@Data
	public static class Record {
		String id;
	}
}
