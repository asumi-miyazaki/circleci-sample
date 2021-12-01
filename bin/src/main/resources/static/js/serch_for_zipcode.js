'use strict';
$('#search').on('click', function() {
	AjaxZip3.zip2addr('destinationZipcode', '', 'destinationAddress','destinationAddress');

	//失敗時に実行する処理
	AjaxZip3.onFailure = function() {
		alert('郵便番号に該当する住所が見つかりません');
	};

	return false;
});