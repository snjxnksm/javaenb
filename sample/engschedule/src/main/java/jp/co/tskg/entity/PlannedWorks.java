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
import java.time.LocalDate;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import jp.co.example.common.AppBaseEntity;
import jp.co.example.common.AppLocalDateToDateConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ワーク予定エンティティ
 *
 * @author try034
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "planned_works")
public class PlannedWorks extends AppBaseEntity implements Serializable {

	/** JOBコード */
	private String jobCd;
	/** 改訂番号 */
	private int revisionNo;
	/** 実施対象タスクコード */
	private String exeTaskCd;
	/** 実施対象ワークコード */
	private String exeWorkCd;
	/** スケジュールマージンフラグ */
	private boolean scheduleMarginFlg;
	/** ワーク日数編集結果 */
	private int editWorkDays;
	/** ワークMH編集結果 */
	private String editWorkMh;
	/** ワーク予定開始日 */
	@JsonFormat(pattern = "yyyy/MM/dd")
	@Convert(converter = AppLocalDateToDateConverter.class)
	private LocalDate plnWorkStartedOn;
	/** ワーク予定終了日 */
	@JsonFormat(pattern = "yyyy/MM/dd")
	@Convert(converter = AppLocalDateToDateConverter.class)
	private LocalDate plnWorkEndedOn;
	/** 主要マイルストーンフラグ */
	private boolean milestoneFlg;
	/** 主要マイルストーン年月日 */
	@JsonFormat(pattern = "yyyy/MM/dd")
	@Convert(converter = AppLocalDateToDateConverter.class)
	private LocalDate milestoneSettedOn;
	/** 内製外注フラグ */
	private boolean internalOutsourceFlg;
	/** ワーク予定リソースコード */
	private String plnWorkResourceCd;
	/** ワーク予定レポーターコード */
	private String plnWorkReporterCd;
	/** ワーク改訂区分 */
	private String reviceType;
	/** ワーク改訂理由 */
	private String reviceRsn;
	/** ワーク予定ネットワーク図X座標 */
	private int plnWorkCoordinateX;
	/** ワーク予定ネットワーク図Y座標 */
	private int plnWorkCoordinateY;
	/** クリティカルパスフラグ */
	private boolean criticalPathFlg;

}