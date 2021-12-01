package com.example.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.Coupon;
import com.example.repository.CouponRepository;

/**
 * クーポンの業務処理を行うServiceクラス.
 * 
 * @author inada
 *
 */
@Service
public class CouponService {

	@Autowired
	private CouponRepository couponRepository;

	/**
	 * ユーザーが保持しているクーポンを取得.
	 * 
	 * @param userId ログインしているユーザ
	 * @return 取得した情報
	 */
	public Coupon searchByUserHaveCoupon(Integer userId) {
		Coupon coupon = couponRepository.findByUserId(userId);
		if (coupon == null) {
			coupon = new Coupon();
			coupon.setDiscount(0);
		}
		return coupon;
	}

	/**
	 * クーポンを登録する.
	 * 
	 * @param userId 誰のクーポンか
	 * @return 登録した情報
	 */
	public Coupon insertCoupon(Integer userId) {
		Random random = new Random();
		Coupon coupon = new Coupon();
		int randomNum = random.nextInt(100) + 1;
		if (0 < randomNum && randomNum <= 80) {
			coupon.setName("50円OFF");
			coupon.setDiscount(50);
		} else if (80 < randomNum && randomNum <= 90) {
			coupon.setName("100円OFF");
			coupon.setDiscount(100);
		} else {
			coupon.setName("200円OFF");
			coupon.setDiscount(200);
		}
		coupon.setCode("test");
		coupon.setUserId(userId);
		int couponId = couponRepository.insert(coupon);
		coupon.setId(couponId);
		return coupon;
	}

	/**
	 * クーポンを更新する.
	 * 
	 * @param id 更新するクーポンID
	 */
	public void updateCoupon(Coupon coupon, int orderId) {
		coupon.setOrderId(orderId);
		coupon.setDeleted(true);
		couponRepository.update(coupon);
	}

	/**
	 * オーダーで使用したクーポンを取得.
	 * 
	 * @param orderId オーダーID
	 * @return 取得したクーポン情報
	 */
	public Coupon searchByOrderUseCoupon(Integer orderId) {
		Coupon coupon = couponRepository.findByOrderId(orderId);
		if (coupon == null) {
			coupon = new Coupon();
			coupon.setDiscount(0);
		}
		return coupon;
	}
}
