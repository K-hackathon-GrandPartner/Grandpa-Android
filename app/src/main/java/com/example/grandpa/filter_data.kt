package com.example.grandpa

data class filter_data(
    //금액
    var depositFrom: Float? =null,
    var depositTo: Float? =null,
    var monthPriceFrom: Float? =null,
    var monthPriceTo: Float? =null,
    //지역
    var Gwangjin: Boolean? =false,
    var Nowon: Boolean? =false,
    var Seongbuk: Boolean? =false,
    //건물 유형
    var apartment: Boolean? =false,
    var officetel: Boolean? =false,
    var villa: Boolean? =false,
    var house: Boolean? =false,
    //방 크기
    var small: Boolean? =false,
    var medium: Boolean? =false,
    var big: Boolean? =false,
    var bigger: Boolean? =false,
    //옵션
    var bathroom: Boolean? =false,
    var kitchen: Boolean? =false,
    var bed: Boolean? =false,
    var laundary: Boolean? =false,
    var aircon: Boolean? =false,
    var elivator: Boolean? =false,
    var desk: Boolean? =false,
    var feeParking: Boolean? =false,
    var freeParking: Boolean? =false,
    var closet: Boolean? =false,
    var internet: Boolean? =false,
    var tv: Boolean? =false
)
