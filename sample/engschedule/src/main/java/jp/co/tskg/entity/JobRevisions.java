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
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import jp.co.example.common.AppBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * JOBリビジョンエンティティ
 *
 * @author try034
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "job_revisions")
public class JobRevisions extends AppBaseEntity implements Serializable {

	/** JOBコード */
	private String jobCd;
	/** 改訂番号 */
	private int revisionNo;
	/** スケジュール作成ステータス */
	private String scheduleCreSts;
	/** 日程展開基準日 */
	private Date scheduleCalStartedOn;
	/** 日程展開フラグ */
	private boolean scheduleCalFlg;
}