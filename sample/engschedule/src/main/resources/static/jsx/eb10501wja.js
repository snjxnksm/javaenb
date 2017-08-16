

//TODO: 日付の存在チェック
function isDate(strDate) {
	if (!strDate.match(/\d{8}/) && !strDate.match(/\d{4}\/\d{2}\/\d{2}/)) {return false;}
	return true;
}
//TODO: 日付の休日チェック
function isWorkDay(strDate) {
	if ("20170429" == strDate) {return false;}
	return true;
}

var Eb10501wja = {
		urls : {
			self : "/eb10501wja.html",
			jobInfo : "/eb10501wja/jobInfo",
			taskList : "/eb10501wja/workList",
			save : "/eb10501wja/save",
			detail: "/eb10601wja.html"
		},
		urlParams : getUrlParams(),
		jobCd : "",
		revisionNo : "",
		lockVersion : "",
		scheduleCreSts : "",
		messageCd : "",
		returnType : "",
		jobNm : ""
};
var urlParams = getUrlParams();
var jobCd;
var revisionNo;
var scheduleCreSts;
var messageCd;
var returnType;
var jobNm;

if (undefined != urlParams) {
	jobCd = urlParams.jobCd;
	revisionNo = urlParams.revisionNo;
	scheduleCreSts = urlParams.scheduleCreSts;
	messageCd = (urlParams.messageCd)?urlParams.messageCd:"";
	returnType = (urlParams.returnType)?urlParams.returnType:"";
}


$(document).ready(function(){

	if (undefined != urlParams) {
		$("#jobCd").text(jobCd);
		$("#revisionNo").text(revisionNo);

//		console.info("scheduleCreSts:=" + scheduleCreSts);
		if ("B" != scheduleCreSts && "G" != scheduleCreSts ) {
			$("#btn_save").prop("disabled", true);
		}

		var reqData = {
			"jobCd": jobCd,
			"revisionNo": revisionNo
		};
	}

	$("#btn_save").on('click',function() {
//		console.info("btn_save is clicked.");
		try {
			$("#btn_save").prop("disabled", true);

			var isValid = true;
			var workList = [];
			var workRow = $("tr.workRow");
			jQuery.each(workRow, function() {
				var exeTaskCd = this.querySelector("[name=exeTaskCd]").value;
				var exeWorkCd = this.querySelector("[name=exeWorkCd]").value;
				var milestoneFlg = this.querySelector("[name=milestoneFlg]").value;
				var milestoneSettedOn = this.querySelector("[name=milestoneSettedOn]").value;
				var editSts = this.querySelector("[name=editSts]").value;
				var data = {
						"exeTaskCd": exeTaskCd,
						"exeWorkCd": exeWorkCd,
						"milestoneFlg": milestoneFlg,
						"milestoneSettedOn": milestoneSettedOn,
						"editSts": editSts
				};
				workList.push(data);
//				console.dir(data);
				if (true == milestoneFlg && "" == milestoneSettedOn) {
					isValid = false;
					//"EB0E-012":"マイルストーン予定日が入力されていません。日付を入力してください。",
					showDialogE1("EB0E-012");
					return false;
				}
			});
			if (isValid) {

				var reqData = {
						"jobCd": jobCd,
						"revisionNo": revisionNo,
						"lockVersion" : Eb10501wja.lockVersion,
						"workList" : workList
				}
				//TODO: マイルストーン確定処理
//				console.info(reqData);
				$.ajax({
			        type: "post",
					url: Eb10501wja.urls.save,
					dataType: 'json',
			        contentType: 'application/json',
					data: JSON.stringify(reqData),
					//data: reqData,
					cache: false,
					success: function(dataReceived) {
						Eb10501wja.lockVersion = dataReceived;
//						console.info("success("+dataReceived+")");
//						console.info("success");

						var params = [];
						params.push("jobCd=" + jobCd);
						params.push("revisionNo=" + revisionNo);
						params.push("scheduleCreSts=" + scheduleCreSts);
						params.push("messageCd=EB0I-006");

						var url = Eb10501wja.urls.self + "?" + params.join("&");
						window.location.replace(url);

						//"EB0I-006":"登録が完了しました。",
						//showAlertDialog("確認", "EB0I-006", React.createElement("button", {type: "button", className: "btn btn-primary", "data-dismiss": "modal"}, "OK"))

					},
					error: function(xhr, status, err) {
						//alert("error");
//						console.info("error");
						showDialog("エラー", "登録に失敗しました。");
					},
					complete: function() {
						//alert("complete");
//						console.info("complete");
					}
				});
				return ;

			}
		}
		catch(e) {
			showDialog("エラー", "エラー")
		}
		finally {
			$("#btn_save").prop("disabled", false);
		}
	});

	$("#btn_back").on('click',function() {
//		console.info("btn_back is clicked.");
		//"EB0W-064":"JOB詳細画面へ戻ります。　変更内容は反映されませんがよろしいでしょうか。",
		showDialogW2("EB0W-064",{
			yes: function() {
				var params = [];
				params.push("jobCd=" + jobCd);
				params.push("revisionNo=" + revisionNo);
				params.push("scheduleCreSts=" + scheduleCreSts);

				var url = "eb10201wja.html?" + params.join("&");
				location.replace(url);
			}
		});
	});

//	window.beforeunload = null;
// 	window.addEventListener('beforeunload', function(e) {
// 	    e.returnValue = "変更内容は反映されませんがよろしいでしょうか。";
// 	}, false);

});

var JobName = React.createClass($.extend(true, {}, ReactAjaxBase, {
	getInitialData: function() {
		return {};
	},
	componentDidMount: function() {
		var urlParams = getUrlParams();
		if (urlParams != undefined) {

//FIXME:ここだとエラーになる・・・
//			var jobCd = urlParams.jobCd;
//			var messageCd = urlParams.messageCd;
//			console.info("messageCd=" + messageCd);
//			if ("" != messageCd) {
//				showAlertDialog("確認", messageCd, React.createElement("button", {type: "button", className: "btn btn-primary", "data-dismiss": "modal"}, "OK"))
//			}

			var reqData = {
					"jobCd": jobCd,
					"revisionNo": revisionNo,
			};
			this.doAjax(this.props.url, reqData);
		}
	},
	onAjaxReceived: function() {
//		console.info("JobName.onAjaxReceived");
		var urlParams = getUrlParams();
		if (urlParams != undefined) {
			var jobCd = urlParams.jobCd;
		}
		var messageCd = urlParams.messageCd;
//		console.info("messageCd=" + messageCd);
		if ("" != messageCd) {
			showAlertDialog("確認", messageCd, React.createElement("button", {type: "button", className: "btn btn-primary", "data-dismiss": "modal"}, "OK"))
		}

	},
	render: function() {
		var jobNm = this.state.data.jobNm;
		Eb10501wja.lockVersion = this.state.data.lockVersion;
		return (<span>{jobNm}</span>);
	}

}));

var WorkListBox = React.createClass($.extend(true, {}, ReactAjaxBase, {
	getInitialData: function() {
		return [];
	},
	componentDidMount: function() {
		var urlParams = getUrlParams();
		if (urlParams != undefined) {
			var jobCd = urlParams.jobCd;
			var revisionNo = urlParams.revisionNo;
			var reqData = {
				"jobCd": jobCd,
				"revisionNo": revisionNo
			};

			//画面モックアップ用
			//this.state.data = taskList;
			//this.setState({data: this.state.data});

			this.doAjax(this.props.url, reqData);
		}
	},
	handleClickDetail: function(exeTaskCd) {
		//console.info("TaskListBox.handleClickDetail("+exeTaskCd+")");

		var editFlg = "1";                //TODO: 編集権フラグ
		var params = [];
		params.push("jobCd=" + jobCd);
		params.push("revisionNo=" + revisionNo);
		params.push("scheduleCreSts=" + scheduleCreSts);
		params.push("exeTaskCd=" + exeTaskCd);
		params.push("editFlg=" + editFlg);
		params.push("screenId=EB10501WJA");

		var url = Eb10501wja.urls.detail + "?" + params.join("&");
		var returnVal = openModalWindow(url, window);

	},
	onAjaxReceived: function() {
//		console.info("WorkListBox.onAjaxReceived");
	},
	render: function() {
		return (
			<div>
				<WorkListTable result={this.getData()} handleClickDetail={this.handleClickDetail} />
			</div>
		);
	}
}));

// 結果テーブル
var WorkListTable = React.createClass({
	componentDidMount: function() {
		//console.info("WorkListTable.componentDidMount()");
	},
	handleClickDetail: function(exeTaskCd) {
		//console.info("WorkListTable.handleClickDetail(exeTaskCd:="+exeTaskCd+")");
		this.props.handleClickDetail(exeTaskCd);
	},
	render: function() {
		var handler = this.handleClickDetail;
		var records = this.props.result.map(function(record) {
			var key = record.exeTaskCd + "." + record.exeWorkCd;
			record.id = key;
			return <WorkRow record={record} handleClickDetail={handler} key={key}/>
		});
	    return (
			<div>
				<table className="ListTable table table-striped">
					<thead>
						<tr>
							<th className="exe_task_nm"         >タスク</th>
							<th className="exe_work_nm"         >ワーク</th>
							<th className="init_work_days"      >標準日数</th>
							<th className="edit_work_days"      >調整後日数</th>
							<th className="milestone_flg"       >マイルストーン</th>
							<th className="milestone_setted_on" >予定日</th>
							<th className="revice_type"         >改訂区分</th>
							<th className="revice_rsn"          >理由</th>
							<th className="detail"              >詳細</th>
						</tr>
					</thead>
					<tbody className="tboby">
						{records}
					</tbody>
				</table>
			</div>
		);
	}
});


// 結果レコード
var WorkRow = React.createClass({
	propTypes: {
		record: React.PropTypes.shape({
			id:                React.PropTypes.string.isRequired,
			exeWorkCnt:        React.PropTypes.string.isRequired,
			exeTaskNm:         React.PropTypes.string.isRequired,
			exeWorkNm:         React.PropTypes.string.isRequired,
			initWorkDays:      React.PropTypes.string.isRequired,
			editWorkDays:      React.PropTypes.string.isRequired,
			milestoneFlg:      React.PropTypes.bool.isRequired,
			milestoneSettedOn: React.PropTypes.string.isRequired,
			reviceType:        React.PropTypes.string.isRequired,
			reviceRsn:         React.PropTypes.string.isRequired,
			exeWorkSts:        React.PropTypes.string.isRequired,
			exeWorkCd:         React.PropTypes.string.isRequired,
			exeTaskCd:         React.PropTypes.string.isRequired
		}),
    	handleClickDetail: React.PropTypes.func.isRequired
	},
	getInitialState: function() {
		//console.info("WorkRow.getInitialState()");
		return {
			work: {
				exeWorkCnt:        this.props.record.exeWorkCnt,
				exeTaskNm:         this.props.record.exeTaskNm,
				exeWorkNm:         this.props.record.exeWorkNm,
				initWorkDays:      this.props.record.initWorkDays,
				editWorkDays:      this.props.record.editWorkDays,
				milestoneFlg:      this.props.record.milestoneFlg,
				milestoneSettedOn: this.props.record.milestoneSettedOn,
				reviceType:        this.props.record.reviceType,
				reviceRsn:         this.props.record.reviceRsn,
				exeWorkSts:        this.props.record.exeWorkSts,
				exeWorkCd:         this.props.record.exeWorkCd,
				exeTaskCd:         this.props.record.exeTaskCd
			}
		};
	},
	handleChangeMilestoneFlg: function(e) {
		//event.preventDefault();
		//console.info("WorkRow.handleChangeMilestoneFlg(e:="+e+")");
		//console.dir(e.target.checked);
		//console.dir(this.props.record);
		if (e.target.checked) {
			this.state.work.milestoneFlg = true;
			this.state.work.milestoneSettedOn = this.props.record.milestoneSettedOn;
			this.setState(this.state.work);
		} else {

			//TODO: 未入力の場合はエラーとすべきか？
			if ("" == this.state.work.milestoneSettedOn) {
				this.state.work.milestoneFlg = false;
				this.setState(this.state.work);
				return false;
			}

			var self = this;
			var elm = e.target;
			//"EB0W-009":"主要マイルストーンを解除しますがよろしいでしょうか？",
			showDialogW2("EB0W-009",{
				yes: function() {
//					console.info("yes");
					self.state.work.milestoneFlg = false;
					self.state.work.milestoneSettedOn = "";
					self.setState(self.state.work);
				},
				 no: function() {
//					console.info("no");
					elm.checked = true;
				}
			});
		}
	},
	handleFocusMilestone: function(e) {
		//event.preventDefault();
		//console.info("WorkRow.handleFocusMilestone(e:="+e+")");
		//console.dir(e.target.value);
		var dateText = e.target.value;
		dateText = dateText.replace(/\//g, "");
		this.state.work.milestoneSettedOn = dateText;
		this.setState(this.state.work);
	},
	handleBlurMilestone: function(e) {
		//event.preventDefault();
		console.info("WorkRow.handleBlurMilestone(e:="+e+")");
		//console.dir(e.target.value);
		var dateText = e.target.value;

		//TODO: 未入力の場合はエラーとすべきか？
		if ("" == dateText) {
			return false;
		}
		if (!isDate(dateText)) {
			//event.preventDefault();
			//"EB0E-010":"{1}は実在しない日付です。正しい日付を入力してください。",
			showDialogE1("EB0E-010");
			console.dir(this.refs);
			this.state.work.milestoneSettedOn = "";
			this.setState(this.state.work);

			var elm = e.target;
			setTimeout(function(){elm.focus()}, 100);
			return false;
		}
		if (!isWorkDay(dateText)) {
			//event.preventDefault();
			//"EB0E-011":"{1}は営業日ではありません。営業日を入力してください。",
			showDialogE1("EB0E-011");
			this.state.work.milestoneSettedOn = "";
			this.setState(this.state.work);

			var elm = e.target;
			setTimeout(function(){elm.focus()}, 100);
			return false;
		}

		dateText = dateText.substr(0, 4) + "/" + dateText.substr(4, 2) + "/" + dateText.substr(6, 2);
		this.state.work.milestoneSettedOn = dateText;
		this.setState(this.state.work);
	},
	handleChangeMilestone:  function(e) {
		//event.preventDefault();
		//console.info("WorkRow.handleMilestoneChanged");
		//console.dir(e.target.value);
		this.state.work.milestoneSettedOn = e.target.value;
		this.setState(this.state.work);
	},
	handleClickDetail: function(e) {
		//event.preventDefault();
		//console.info("WorkRow.handleClickDetail");
		var exeTaskCd = this.props.record.exeTaskCd;
		this.props.handleClickDetail(exeTaskCd);
	},
	render: function(){
		var scheduleCreSts = "";
		if (undefined != urlParams) {
			scheduleCreSts = urlParams.scheduleCreSts;
		}
		var milestoneFlgDisabled = "1";
		if ("B" == scheduleCreSts || "G" == scheduleCreSts) {
			milestoneFlgDisabled = "";
		}
	    var defaultChecked = "";
		if (true == this.state.work.milestoneFlg) {
			defaultChecked = "1";
		}
		var milestoneDisabled = "1";
		if (true == this.state.work.milestoneFlg && "" == milestoneFlgDisabled) {
			milestoneDisabled = "";
		}

		var exeWorkSts = this.props.record.exeWorkSts;
		if ("A" != exeWorkSts) {
			milestoneFlgDisabled = "1";
			milestoneDisabled = "1";
		}

		var strRsnType = "";
		if ("B" == this.props.record.reviceType) {
			strRsnType = "追加";
		}
		else if ("C" == this.props.record.reviceType) {
			strRsnType = "削除";
		}

		var editSts = "A"; //変更なし
		if (false == this.props.record.milestoneFlg) {
			if(true == this.state.work.milestoneFlg) {
				editSts = "B"; // 追加
			}
		} else {
			if(false == this.state.work.milestoneFlg) {
				editSts = "D"; // 削除
			} else if(this.props.record.milestoneSettedOn != this.state.work.milestoneSettedOn) {
					editSts = "C"; // 変更
			}
		}

		var rowSpan = this.props.record.exeWorkCnt;
		var rowId = "workRow["+ this.props.record.id +"]"

		if (0 < this.props.record.exeWorkCnt) {
			return (
				<tr className="workRow" id={rowId} >
					<td className="Centering WhiteCell NoExpand exe_task_nm" rowSpan={rowSpan} ><div>{this.props.record.exeTaskNm}</div></td>
					<td className="NoExpand                     exe_work_nm"                   ><div>{this.props.record.exeWorkNm}</div></td>
					<td className="Number                       init_work_days"                >{this.props.record.initWorkDays}</td>
					<td className="Number                       edit_work_days"                >{this.props.record.editWorkDays}</td>
					<td className="Centering                    milestone_flg"                 ><input type="checkbox" disabled={milestoneFlgDisabled}
																									 defaultChecked={defaultChecked}
																									 onChange={this.handleChangeMilestoneFlg}/>
																								<input type="hidden" name="milestoneFlg" value={this.state.work.milestoneFlg} />
																								<input type="hidden" name="exeTaskCd"    value={this.props.record.exeTaskCd} />
																								<input type="hidden" name="exeWorkCd"    value={this.props.record.exeWorkCd} />
																								<input type="hidden" name="editSts"      value={editSts} />
					</td>
					<td className="Date                         milestone_setted_on"           ><input type="text"     disabled={milestoneDisabled}
																									 value={this.state.work.milestoneSettedOn}
																									 ref="milestoneSettedOn" name="milestoneSettedOn"
																									 onChange={this.handleChangeMilestone}
																									 onFocus={this.handleFocusMilestone}
																									 onBlur={this.handleBlurMilestone} />
					</td>
					<td className="Centering                    revice_type"                   >{strRsnType}</td>
					<td className="NoExpand                     revice_rsn"                    ><div>{this.props.record.reviceRsn}</div></td>
					<td className="Centering WhiteCell          detail" rowSpan={rowSpan}      ><button className="DetailButton btn btn-info btn-xs" onClick={this.handleClickDetail.bind(this, this.props.record.exeTaskCd)}>詳細</button></td>
				</tr>
			);
		} else {
			return (
				<tr className="workRow" id={rowId} >
					<td className="NoExpand                     exe_work_nm"                   ><div>{this.props.record.exeWorkNm}</div></td>
					<td className="Number                       init_work_days"                >{this.props.record.initWorkDays}</td>
					<td className="Number                       edit_work_days"                >{this.props.record.editWorkDays}</td>
					<td className="Centering                    milestone_flg"                 ><input type="checkbox" disabled={milestoneFlgDisabled}
																									 defaultChecked={defaultChecked}
																									 onChange={this.handleChangeMilestoneFlg}/>
																								<input type="hidden" name="milestoneFlg" value={this.state.work.milestoneFlg} />
																								<input type="hidden" name="exeTaskCd"    value={this.props.record.exeTaskCd} />
																								<input type="hidden" name="exeWorkCd"    value={this.props.record.exeWorkCd} />
																								<input type="hidden" name="editSts"      value={editSts} />
					</td>
					<td className="Date                         milestone_setted_on"           ><input type="text"     disabled={milestoneDisabled}
																									 value={this.state.work.milestoneSettedOn}
																									 ref="milestoneSettedOn" name="milestoneSettedOn"
																									 onChange={this.handleChangeMilestone}
																									 onFocus={this.handleFocusMilestone}
																									 onBlur={this.handleBlurMilestone} />
					</td>
					<td className="Centering                    revice_type"                   >{strRsnType}</td>
					<td className="NoExpand                     revice_rsn"                    ><div>{this.props.record.reviceRsn}</div></td>
				</tr>
			);
		}
	}
});

$(document).ready(function(){
	//React.render(<JobName     url="./jobInfo0.json" />,  $("#jobNm")[0]);
	//React.render(<TaskListBox url="./taskList0.json" />, $("#TaskList")[0]);
	ReactDOM.render(<JobName     url={Eb10501wja.urls.jobInfo}  />, $("#jobNm")[0]);
	ReactDOM.render(<WorkListBox url={Eb10501wja.urls.taskList} />, $("#TaskList")[0]);
});



// 画面モックアップ用
var jobInfo = ﻿{
	"result": [
		{
		"jobNm":"島根県下水道消化ガス発電"
		}
			]
		};
var taskList = ﻿{
	"result": [
		{
		"exeWorkCnt":"7", "exeTaskNm":"設備容量計算書",
		"exeWorkNm":"作成準備",
		"initWorkDays":"5",
		"editWorkDays":"3",
		"milestoneFlg":"0",
		"milestoneSettedOn":"",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"1",
		"exeTaskCd":"1"
		},
		{
		"exeWorkCnt":"0", "exeTaskNm":"",
		"exeWorkNm":"図書作成",
		"initWorkDays":"15",
		"editWorkDays":"15",
		"milestoneFlg":"0",
		"milestoneSettedOn":"",
		"reviceType":"B",
		"reviceRsn":"ＸＸＸＸＸＸのため",
		"exeWorkSts":"A",
		"exeWorkCd":"2",
		"exeTaskCd":"1"
		},
		{
		"exeWorkCnt":"0", "exeTaskNm":"",
		"exeWorkNm":"図書作成",
		"initWorkDays":"15",
		"editWorkDays":"15",
		"milestoneFlg":"0",
		"milestoneSettedOn":"",
		"reviceType":"C",
		"reviceRsn":"ＸＸＸＸＸＸのため削除",
		"exeWorkSts":"A",
		"exeWorkCd":"3",
		"exeTaskCd":"1"
		},
		{
		"exeWorkCnt":"0", "exeTaskNm":"",
		"exeWorkNm":"社内出図",
		"initWorkDays":"2",
		"editWorkDays":"3",
		"milestoneFlg":"1",
		"milestoneSettedOn":"2016/10/01",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"4",
		"exeTaskCd":"1"
		},
		{
		"exeWorkCnt":"0", "exeTaskNm":"",
		"exeWorkNm":"顧客提出",
		"initWorkDays":"1",
		"editWorkDays":"1",
		"milestoneFlg":"0",
		"milestoneSettedOn":"",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"5",
		"exeTaskCd":"1"
		},
		{
		"exeWorkCnt":"0", "exeTaskNm":"",
		"exeWorkNm":"顧客承認待ち",
		"initWorkDays":"1",
		"editWorkDays":"1",
		"milestoneFlg":"0",
		"milestoneSettedOn":"",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"6",
		"exeTaskCd":"1"
		},
		{
		"exeWorkCnt":"0", "exeTaskNm":"",
		"exeWorkNm":"顧客承認",
		"initWorkDays":"1",
		"editWorkDays":"1",
		"milestoneFlg":"1",
		"milestoneSettedOn":"2017/02/01",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"7",
		"exeTaskCd":"1"
		},
		{
		"exeWorkCnt":"6", "exeTaskNm":"塗装仕様書abcdefghijklmnopqrstuvwxyz1234567890","a":"塗装仕様書①②③④⑤⑥⑦⑧⑨⑩⑪⑫⑬⑭⑮⑰⑰⑱⑲⑳",
		"exeWorkNm":"作成準備abcdefghijklmnopqrstuvwxyz1234567890","a":"作成準備①②③④⑤⑥⑦⑧⑨⑩⑪⑫⑬⑭⑮⑰⑰⑱⑲⑳",
		"initWorkDays":"",
		"editWorkDays":"",
		"milestoneFlg":"0",
		"milestoneSettedOn":"",
		"reviceType":"B",
		"reviceRsn":"ＸＸＸＸＸＸのため①②③④⑤⑥⑦⑧⑨⑩⑪⑫⑬⑭⑮⑰⑰⑱⑲⑳",
		"exeWorkSts":"A",
		"exeWorkCd":"8",
		"exeTaskCd":"2"
		},
		{
		"exeWorkCnt":"0", "exeTaskNm":"",
		"exeWorkNm":"図書作成",
		"initWorkDays":"",
		"editWorkDays":"",
		"milestoneFlg":"0",
		"milestoneSettedOn":"",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"9",
		"exeTaskCd":"2"
		},
		{
		"exeWorkCnt":"0", "exeTaskNm":"",
		"exeWorkNm":"社内出図",
		"initWorkDays":"",
		"editWorkDays":"",
		"milestoneFlg":"1",
		"milestoneSettedOn":"2017/02/01",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"10",
		"exeTaskCd":"2"
		},
		{
		"exeWorkCnt":"0", "exeTaskNm":"",
		"exeWorkNm":"顧客提出",
		"initWorkDays":"",
		"editWorkDays":"",
		"milestoneFlg":"0",
		"milestoneSettedOn":"",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"11",
		"exeTaskCd":"2"
		},
		{
		"exeWorkCnt":"0", "exeTaskNm":"",
		"exeWorkNm":"顧客承認待ち",
		"initWorkDays":"",
		"editWorkDays":"",
		"milestoneFlg":"0",
		"milestoneSettedOn":"",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"12",
		"exeTaskCd":"2"
		},
		{
		"exeWorkCnt":"0", "exeTaskNm":"",
		"exeWorkNm":"顧客承認",
		"initWorkDays":"",
		"editWorkDays":"",
		"milestoneFlg":"1",
		"milestoneSettedOn":"2017/02/01",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"13",
		"exeTaskCd":"2"
		},
		{
		"exeWorkCnt":"6", "exeTaskNm":"保温仕様書",
		"exeWorkNm":"作成準備",
		"initWorkDays":"",
		"editWorkDays":"",
		"milestoneFlg":"0",
		"milestoneSettedOn":"",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"14",
		"exeTaskCd":"3"
		},
		{
		"exeWorkCnt":"0", "exeTaskNm":"",
		"exeWorkNm":"図書作成",
		"initWorkDays":"",
		"editWorkDays":"",
		"milestoneFlg":"0",
		"milestoneSettedOn":"",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"15",
		"exeTaskCd":"3"
		},
		{
		"exeWorkCnt":"0", "exeTaskNm":"",
		"exeWorkNm":"社内出図",
		"initWorkDays":"",
		"editWorkDays":"",
		"milestoneFlg":"0",
		"milestoneSettedOn":"",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"16",
		"exeTaskCd":"3"
		},
		{
		"exeWorkCnt":"0", "exeTaskNm":"",
		"exeWorkNm":"顧客提出",
		"initWorkDays":"",
		"editWorkDays":"",
		"milestoneFlg":"0",
		"milestoneSettedOn":"",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"17",
		"exeTaskCd":"3"
		},
		{
		"exeWorkCnt":"0", "exeTaskNm":"",
		"exeWorkNm":"顧客承認待ち",
		"initWorkDays":"",
		"editWorkDays":"",
		"milestoneFlg":"0",
		"milestoneSettedOn":"",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"18",
		"exeTaskCd":"3"
		},
		{
		"exeWorkCnt":"0", "exeTaskNm":"",
		"exeWorkNm":"顧客承認",
		"initWorkDays":"",
		"editWorkDays":"",
		"milestoneFlg":"0",
		"milestoneSettedOn":"",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"19",
		"exeTaskCd":"3"
		},
		{
		"exeWorkCnt":"6", "exeTaskNm":"Ｐ＆ＩＤ",
		"exeWorkNm":"作成準備",
		"initWorkDays":"",
		"editWorkDays":"",
		"milestoneFlg":"0",
		"milestoneSettedOn":"",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"20",
		"exeTaskCd":"4"
		},
		{
		"exeWorkCnt":"0", "exeTaskNm":"",
		"exeWorkNm":"図書作成",
		"initWorkDays":"",
		"editWorkDays":"",
		"milestoneFlg":"0",
		"milestoneSettedOn":"",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"21",
		"exeTaskCd":"4"
		},
		{
		"exeWorkCnt":"0", "exeTaskNm":"",
		"exeWorkNm":"社内出図",
		"initWorkDays":"",
		"editWorkDays":"",
		"milestoneFlg":"0",
		"milestoneSettedOn":"",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"22",
		"exeTaskCd":"4"
		},
		{
		"exeWorkCnt":"0", "exeTaskNm":"",
		"exeWorkNm":"顧客提出",
		"initWorkDays":"",
		"editWorkDays":"",
		"milestoneFlg":"0",
		"milestoneSettedOn":"",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"23",
		"exeTaskCd":"4"
		},
		{
		"exeWorkCnt":"0", "exeTaskNm":"",
		"exeWorkNm":"顧客承認待ち",
		"initWorkDays":"",
		"editWorkDays":"",
		"milestoneFlg":"0",
		"milestoneSettedOn":"",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"24",
		"exeTaskCd":"4"
		},
		{
		"exeWorkCnt":"0", "exeTaskNm":"",
		"exeWorkNm":"顧客承認",
		"initWorkDays":"",
		"editWorkDays":"",
		"milestoneFlg":"0",
		"milestoneSettedOn":"",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"25",
		"exeTaskCd":"4"
		},
		{
		"exeWorkCnt":"6", "exeTaskNm":"全体配置図",
		"exeWorkNm":"作成準備",
		"initWorkDays":"",
		"editWorkDays":"",
		"milestoneFlg":"0",
		"milestoneSettedOn":"",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"26",
		"exeTaskCd":"5"
		},
		{
		"exeWorkCnt":"0", "exeTaskNm":"",
		"exeWorkNm":"図書作成",
		"initWorkDays":"",
		"editWorkDays":"",
		"milestoneFlg":"0",
		"milestoneSettedOn":"",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"27",
		"exeTaskCd":"5"
		},
		{
		"exeWorkCnt":"0", "exeTaskNm":"",
		"exeWorkNm":"社内出図",
		"initWorkDays":"",
		"editWorkDays":"",
		"milestoneFlg":"0",
		"milestoneSettedOn":"",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"28",
		"exeTaskCd":"5"
		},
		{
		"exeWorkCnt":"0", "exeTaskNm":"",
		"exeWorkNm":"顧客提出",
		"initWorkDays":"",
		"editWorkDays":"",
		"milestoneFlg":"0",
		"milestoneSettedOn":"",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"29",
		"exeTaskCd":"5"
		},
		{
		"exeWorkCnt":"0", "exeTaskNm":"",
		"exeWorkNm":"顧客承認待ち",
		"initWorkDays":"",
		"editWorkDays":"",
		"milestoneFlg":"0",
		"milestoneSettedOn":"",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"30",
		"exeTaskCd":"5"
		},
		{
		"exeWorkCnt":"0", "exeTaskNm":"",
		"exeWorkNm":"顧客承認",
		"initWorkDays":"",
		"editWorkDays":"",
		"milestoneFlg":"1",
		"milestoneSettedOn":"2017/02/01",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"31",
		"exeTaskCd":"5"
		},
		{
		"exeWorkCnt":"6", "exeTaskNm":"ダクト図作成",
		"exeWorkNm":"ダクト口径計算",
		"initWorkDays":"",
		"editWorkDays":"",
		"milestoneFlg":"0",
		"milestoneSettedOn":"",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"32",
		"exeTaskCd":"6"
		},
		{
		"exeWorkCnt":"0", "exeTaskNm":"",
		"exeWorkNm":"ダクト仕様",
		"initWorkDays":"",
		"editWorkDays":"",
		"milestoneFlg":"0",
		"milestoneSettedOn":"",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"33",
		"exeTaskCd":"6"
		},
		{
		"exeWorkCnt":"0", "exeTaskNm":"",
		"exeWorkNm":"ダクトルート検討",
		"initWorkDays":"",
		"editWorkDays":"",
		"milestoneFlg":"0",
		"milestoneSettedOn":"",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"34",
		"exeTaskCd":"6"
		},
		{
		"exeWorkCnt":"0", "exeTaskNm":"",
		"exeWorkNm":"ダクト図作成",
		"initWorkDays":"",
		"editWorkDays":"",
		"milestoneFlg":"0",
		"milestoneSettedOn":"",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"35",
		"exeTaskCd":"6"
		},
		{
		"exeWorkCnt":"0", "exeTaskNm":"",
		"exeWorkNm":"社内承認",
		"initWorkDays":"",
		"editWorkDays":"",
		"milestoneFlg":"0",
		"milestoneSettedOn":"",
		"reviceType":"A",
		"reviceRsn":"",
		"exeWorkSts":"A",
		"exeWorkCd":"36",
		"exeTaskCd":"6"
		},
		{
		"exeWorkCnt":"0", "exeTaskNm":"",
		"exeWorkNm":"出図",
		"initWorkDays":"",
		"editWorkDays":"",
		"milestoneFlg":"0",
		"milestoneSettedOn":"",
		"reviceType":"B",
		"reviceRsn":"ＸＸＸＸＸＸのため追加",
		"exeWorkSts":"A",
		"exeWorkCd":"37",
		"exeTaskCd":"6"
		}
			]
		};