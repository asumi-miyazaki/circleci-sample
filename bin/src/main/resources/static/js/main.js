'use strict'
$(function() {
	$('.pagination').paginathing({
		perPage: 3,
		prevText: '前へ',
		nextText: '次へ',
		containerClass: 'pagination-container',
	})

	let availableTags = ["とんこつラーメン", "赤ラーメン", "からか麺", "かさね味Special", "百福元味", "川越とんこつ醤油", "元祖・白丸元味／元祖・赤丸新味", "鶏とんこつ麺", "辛味噌ちゃあしゅう麺", "追い鰹チャーシュー", "鶏白湯坦々麺", "貝出汁らーめん原点", "澄み切った醤油らーめん", "煮干しつけ麺", "旨辛味噌麺", "まぜ麺Gorgeous4", "台湾まぜそば", "真・澄み切った塩らーめん"];
	$("#code").autocomplete({
		source: availableTags
	})
})
