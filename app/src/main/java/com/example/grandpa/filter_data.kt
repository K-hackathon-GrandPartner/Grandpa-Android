package com.example.grandpa

data class filter_data(
    //금액
    var startDeposit: Float? =null,
    var endDeposit: Float?=null,
    var startMonthlyRent: Float?=null,
    var endMonthlyRent: Float?=null,
    //지역
    var region_Gwangjin: String? =null,
    var region_Nowon: String? =null,
    var region_Seongbuk: String? =null,
    //건물 유형
    var buildingType_apartment: String? =null,
    var buildingType_officetel: String? =null,
    var buildingType_villa: String? =null,
    var buildingType_house: String? =null,
    //방 크기
    var roomSize_small: String? =null,
    var roomSize_medium: String? =null,
    var roomSize_big: String? =null,
    var roomSize_bigger: String? =null,
    //옵션
    var option_bathroom: String? =null,
    var option_kitchen: String? =null,
    var option_bed: String? =null,
    var option_laundry: String? =null,
    var option_aircon: String? =null,
    var option_elevator: String? =null,
    var option_desk: String? =null,
    var option_feeParking: String? =null,
    var option_freeParking: String? =null,
    var option_closet: String? =null,
    var option_internet: String? =null,
    var option_tv: String? =null
)


