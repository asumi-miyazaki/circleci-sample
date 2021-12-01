'use strict';
$(function() {
	calc_price();
	$('.size').on('change', function () {
		calc_price();
	});

	$('.checkbox').on('change', function () {
		calc_price();
	});

	$('#noodlenum').on('change', function () {
		calc_price();
	});
	
	function calc_price() {
		let str = $('.size:checked').val().split(',');
		let size = str[0];
		let noodlePrice = Number(str[1]);
		let topping_count = $('#topping input:checkbox:checked').length;
		let noodle_num = $('#noodlenum option:selected').val();
		let topping_price = 0;
		if (size == 'M') {
			topping_price = 200 * topping_count;
		} else {
			topping_price = 300 * topping_count;
		}
		let price = (noodlePrice + topping_price) * noodle_num;
		console.log(price)
		$('#total-price').text(price.toLocaleString());
	};
});