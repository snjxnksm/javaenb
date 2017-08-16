
// REST API 基底クラス（シングル通信）
var ReactAjaxBase = {
	_msgBelongsTo: null,
	_ajaxCom: null,

	// 初期stateを設定する。(オーバーライドしない想定）
	getInitialState: function() {
		return {isLoading: false, data: this.getInitialData()};
	},

	// 初期データを設定する。this.state.dataを指す。
	// 受信データを含む個々のオブジェクト状態はここで保持する。
	getInitialData: function(){
		return {};
	},

	// 受信データを取得する。
	getData: function() {
		return this.state.data;
	},

	// ローディング中かどうかを取得する。
	isLoading: function() {
		return this.state.isLoading;
	},

	// Ajax通信を行う。引数：URL, 送信データ
	doAjax: function(url, data) {
		this._ajaxCom = new AjaxCom(url, {
			onReceived: function(response) {
				this.setState({data: response});
				this.onAjaxReceived();
			}.bind(this),
			onComplete: function() {
				this.setState({isLoading: false});
			}.bind(this)
		});

		this._ajaxCom.doAjax(data);

		this.setState({isLoading: true});
	},

	// データ受信時の処理をオーバーライドで実装する。
	// 受信データチェックはここで行う。
	onAjaxReceived: function() {},

	// 受信データを表示する。Ajax通信中はローディング画像を表示する。
	getDisplay: function(data) {
		return getDisplay(this.isLoading(), data);
	},

	// メッセージ(ツールチップ)を表示する。
	renderMessage: function(target, msg) {
		target.tooltip({
			placement: 'top',
			title: msg,
			trigger: 'manual'
		});

		target.tooltip('show');
		target.change(function() {this.clearMessage()}.bind(this));
		this._msgBelongsTo = target;
	},

	// エラーメッセージを表示する。
	renderError: function(code) {
		showAlertDialog(code);
	},

/* Tooltip版
	// エラーメッセージを表示する。
	renderError: function(target, code) {
		var errmsg = errorMsg(code);
		if (errmsg) {
			errmsg = code + ": " + errorMsg(code)
		} else {
			errmsg = "未定義エラーコード " + code;
		}

		target.tooltip({
			placement: 'top',
			title: errmsg,
			trigger: 'manual',
			container: 'body',
			template: '<div class="tooltip warning" role="tooltip"><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div>'
		});

		target.tooltip('show');
		target.blur(function() {this.clearMessage()}.bind(this));
		this.msgBelongsTo = target;
	},
*/

	// メッセージを消去する。
	clearMessage: function() {
		if (this.msgBelongsTo != null) {
			this._msgBelongsTo.tooltip('destroy');
			this._msgBelongsTo = null;
		}
	}
}

/*
 * REST API 通信制御クラス
 */
var ReactAjax = function(url) {
	this._isLoading = false;
	this._ajaxCom = null;
	this._url = url;
	this._request = null;
	this._response = {};
	this._loadingObjs = [];
	this._handler = {
		onReceived: function() {}
	};
}

$.extend(ReactAjax.prototype, {

	// リクエストJSONを設定する。
	setRequest: function(data) {
		this._request = data;
		return this;
	},

	// 受信データを取得する。
	getData: function() {
		return this._response;
	},

	// ローディング中かどうかを取得する。
	isLoading: function() {
		return this._isLoading;
	},

	// ローディングを制御するReactオブジェクトを追加する。
	// ローディング開始、終了時にUpdateが掛かる。
	addStateControl: function(obj) {
		this._loadingObjs.push(obj);
		return this;
	},

	// Ajax通信を行う。引数：URL, 送信データ
	doAjax: function() {
		this._ajaxCom = new AjaxCom(this._url, {
			onReceived: function(response) {
				this._handler.onReceived(response);
			}.bind(this),
			onComplete: function() {
				this._isLoading = false;
				this._loadingObjs.map(function(obj) {obj.forceUpdate()});
			}.bind(this)
		});

		this._ajaxCom.doAjax(this._request);

		this._isLoading = true;
		this._loadingObjs.map(function(obj) {obj.forceUpdate()});
	},

	// データ受信時のハンドラを設定する。
	get: function(handler) {
		this._handler.onReceived = handler;
		return this;
	},

	// 受信データを表示する。Ajax通信中はローディング画像を表示する。
	getDisplay: function(data) {
		return getDisplay(this.isLoading(), data);
	},

	// メッセージ(ツールチップ)を表示する。
	renderMessage: function(target, msg) {
		target.tooltip({
			placement: 'top',
			title: msg,
			trigger: 'manual'
		});

		target.tooltip('show');
		target.change(function() {this.clearMessage()}.bind(this));
		this._msgBelongsTo = target;
	},

	// メッセージを消去する。
	clearMessage: function() {
		if (this.msgBelongsTo != null) {
			this._msgBelongsTo.tooltip('destroy');
			this._msgBelongsTo = null;
		}
	}
});

/*
 * Ajax通信ラッパークラス（主にエラーハンドリングの共通化のためラップする）
 */
// コンストラクタ
var AjaxCom = function(url, handler) {
	this._url = url;
	this.onReceived = handler.onReceived;
	this.onComplete = handler.onComplete;
}

AjaxCom.prototype.doAjax = function(request) {
	$.ajax({
		url: this._url,
		type: "post",
		contentType: "application/json",
		dataType: "json",
		data: JSON.stringify(request),
		cache: false,
		success: function(response) {
			this.onReceived(response);
		}.bind(this),
		error: function(xhr, status, err) {
			var msg, control, title;
			if (xhr.status == 200) {
				// 受信データが異常の場合
				msg = React.createElement("div", null,
						React.createElement("p", null, status + ": " + err.toString()),
						React.createElement("p", null, "[" + xhr.status + "] " + this._url)
				);
				control = (
						React.createElement("button", {type: "button", className: "btn btn-primary", "data-dismiss": "modal"}, "OK")
				);
				title = "受信データエラー";
			} else {
				// HTTP通信エラーの場合
				msg = React.createElement("div", null,
						React.createElement("p", null, err.toString()),
						React.createElement("p", null, "[" + xhr.status + "] " + this._url)
				);
				control = (
						React.createElement("button", {type: "button", className: "btn btn-primary", "data-dismiss": "modal"}, "OK")
				);
				title = "通信エラー";
			}
			showDialog(title, msg, control);
		}.bind(this),
		complete: function() {
			this.onComplete();
		}.bind(this)
	});
}

// データを表示する。Ajax通信中はローディング画像を表示する。
function getDisplay(isLoading, data) {
	if (isLoading) {
		return React.createElement("img", {src: "./images/ajax-loader.gif", className: "loading"});
	} else {
		return data;
	}
}

// エラーメッセージを取得する
// 引数：code=エラーコード
function errorMsg(code) {
	return ERROR_CONSTS[code];
}

// URLパラメータを取得する。
// URL後の"?key1=value1&key2=value2&..."を
// オブジェクト{key1: value1, key2: value2, ...}に変換する。
// パラメータがない場合はundefinedを返す。
function getUrlParams() {
	var search = $(location).attr("search");

	if (!search) {
		return undefined;
	}

	var paramArray = [];
	search.substring(1).split('&').map(function (param) {
		var kv = param.split('=');
		paramArray[kv[0]] = kv[1];
	});

	return paramArray;
}

// ポップアップウィンドウを開く
// 引数：url=URL
function openPopupWindow(url) {
	var optWindowSize;

	if (!window.document.msFullscreenElement) {
		var optHeight = "height=" + $(window).height();
		var optWidth = "width=" + $(window).width();
		optWindowSize = optHeight + "," + optWidth;
	} else {
		optWindowSize = "fullscreen=yes";
	}

	var newwin = window.open(url, "_blank",
		"resizable=yes,toolbar=no,scrollbars=yes," + optWindowSize);
	return newwin;
}

// モーダルウィンドウを開く
// 引数：url=URL
//       parent=親ウィンドウ
function openModalWindow(url, parent) {
	var optHeight = "dialogheight=" + $(window).height() + "px";
	var optWidth = "dialogwidth=" + $(window).width() + "px";

	var newwin = window.showModalDialog(url, parent,
		"center=yes;resizable=yes;scroll=yes;" + optHeight + ";" + optWidth);
	return newwin;
}

// E1ダイアログを表示する。
// 引数：code=エラーコード
function showDialogE1(code) {
	var control = (
		React.createElement("button", {type: "button", className: "btn btn-primary", "data-dismiss": "modal"}, "OK")
	);
	showAlertDialog("エラー", code, control);
}

// W1ダイアログを表示する。
// 引数：code=エラーコード
function showDialogW1(code) {
	var control = (
		React.createElement("button", {type: "button", className: "btn btn-primary", "data-dismiss": "modal"}, "OK")
	);
	showAlertDialog("警告", code, control);
}

// W2ダイアログを表示する。
// 引数：code=エラーコード
//       handlerEntry=はい、いいえが押下されたときの処理
function showDialogW2(code, handlerEntry) {
	var handler = {yes: function() {}, no: function() {}};
	$.extend(handler, handlerEntry);

	var control = (
		React.createElement("div", null,
			React.createElement("button", {type: "button", className: "btn btn-primary", "data-dismiss": "modal", onClick: handler.yes}, "はい"),
			React.createElement("button", {type: "button", className: "btn btn-default", "data-dismiss": "modal", onClick: handler.no}, "いいえ")
		)
	);
	showAlertDialog("確認", code, control);
}

// W3ダイアログを表示する。
// 引数：code=エラーコード
//       handlerEntry=はい、いいえ、取消しが押下されたときの処理
function showDialogW3(code, handlerEntry) {
	var handler = {yes: function() {}, no: function() {}, cancel: function() {}};
	$.extend(handler, handlerEntry);

	var control = (
		React.createElement("div", null,
			React.createElement("button", {type: "button", className: "btn btn-primary", "data-dismiss": "modal", onClick: handler.yes}, "はい"),
			React.createElement("button", {type: "button", className: "btn btn-default", "data-dismiss": "modal", onClick: handler.no}, "いいえ"),
			React.createElement("button", {type: "button", className: "btn btn-default", "data-dismiss": "modal", onClick: handler.cancel}, "取消し")
		)
	);
	showAlertDialog("確認", code, control);
}

// エラーダイアログを表示する。
//引数：title=タイトルバーの文言
//      code=エラーコード
//      control=下部の操作部に表示するボタン等
function showAlertDialog(title, code, control) {
	var errmsg = errorMsg(code);
	if (errmsg) {
		errmsg = code + ": " + errorMsg(code)
	} else {
		errmsg = "未定義エラーコード " + code;
	}
	showDialog(title, errmsg, control);
}

// ダイアログを表示する。
// 引数：title=タイトルバーの文言
//       msg=メッセージ文言
//       control=下部の操作部に表示するボタン等
function showDialog(title, msg, control) {
	var $div = $("#_modalDialog");

	$div.find('.modal-title').text(title);
	if (msg instanceof Object) {
		React.render(msg, $div.find('.modal-body')[0]);
	} else {
		$div.find('.modal-body').text(msg);
	}
	React.render(control, $div.find('.modal-footer')[0]);

	$div.modal('show');
}

// モーダルダイアログの部品
var ModalDialog = React.createClass({displayName: "ModalDialog",
	render: function() {
		return (
			React.createElement("div", {id: "_modalDialog", className: "modal fade", tabIndex: "-1"},
				React.createElement("div", {className: "modal-dialog"},
					React.createElement("div", {className: "modal-content"},
						React.createElement("div", {className: "modal-header"},
							React.createElement("h4", {className: "modal-title"})
						),
						React.createElement("div", {className: "modal-body"}),
						React.createElement("div", {className: "modal-footer"})
					)
				)
			)
		);
	}
});

function resizeExpandTable() {
	// テーブル高さ調整
	$("table.expand-table").each(function(index, table) {
		var parentH = $(table).closest(".vsection").height();
		var tableTop = $(table).position().top;
		var headerH = $(table).find("thead").height();
		$(table).find("tbody").height(parentH - tableTop - headerH);
		console.debug(parentH+","+tableTop+","+headerH);
	});

	// DIV高さ調整
	$("div.expand-block").each(function(index, block) {
		var parentH = $(block).closest(".vsection").height();
		var blockTop = $(block).position().top;
		$(block).height(parentH - blockTop);
	});
}

// 初期表示処理をonloadで実行
$(window).on("load", function() {
	// モーダルダイアログ
	var $modal = $("<div>").appendTo("body");
	React.render(React.createElement(ModalDialog, null), $modal[0]);
	console.debug("Loaded modal dialog");
});

$(document).ready(function() {
	resizeExpandTable();
});
