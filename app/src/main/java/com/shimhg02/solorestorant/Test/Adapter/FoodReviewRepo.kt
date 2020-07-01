package com.shimhg02.solorestorant.Test.Adapter


/**
 * @description 음식 리뷰 레포
 */

data class FoodReviewRepo(
    val profile_photo_url: String,
    val text: String,
    val author_name: String,
    val rating: String
)