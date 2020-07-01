package com.shimhg02.solorestorant.Test.Data

/**
 * @description 리뷰 데이터클래스
 */

data class Review(
    val author_name: String,
    val author_url: String,
    val language: String,
    val profile_photo_url: String,
    val rating: Int,
    val relative_time_description: String,
    val text: String,
    val time: Int
)