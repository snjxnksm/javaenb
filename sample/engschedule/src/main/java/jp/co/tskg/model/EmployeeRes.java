package jp.co.example.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class EmployeeRes {
	private int lockVersion;
	private String empName;

	private LocalDateTime  createdAt;

	// 書式の変更が必要なら@JsonFormatで指定する
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime  updatedAt;
}
