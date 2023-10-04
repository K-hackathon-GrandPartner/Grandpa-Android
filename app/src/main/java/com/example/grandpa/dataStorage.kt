package com.example.grandpa

import java.io.Serializable
import javax.crypto.AEADBadTagException

//room 모두 보여주는 data class
data class ShowRoomResponse(
    val statusCode: Int,
    val message: String,
    val result: List<room_data>
)
data class room_data(
    val id: Int,
    val imageUrl: String,
    val buildingType: String,
    val roomSizeType: String,
    val roomSize: Float,
    val roomFloor: Int,
    val deposit: Int,
    val monthlyRent: Int,
    val address: String,
    val title: String,
    val postDate: String,
)

//room id 보여주는 data class
data class DetailRoomResponse(
    val statusCode: Int,
    val message: String,
    val result: room_detail_data
)

data class room_detail_data(
    val id: Int,
    val monthlyRent: Int,
    val deposit: Int,
    val buildingType: String,
    val buildingFloor: Int,
    val roomFloor: Int,
    val roomSize: Float,
    val moveInDate: String,
    val address: String,
    val coordinate: Location,
    val postDate: String,
    val updateDate: String,
    val images: List<String>,
    val detail: Detail,
    val option: Option,
    val rule: Rule,
    val safety: Safety,
    val pet: Pet,
    val careServices: List<String>
)

data class Location(
    val lat: Double,
    val lng: Double
)

data class Detail(
    val title: String,
    val content: String
)

data class Option(
    val bathroom: Int,
    val bed: Int,
    val airConditioner: Int,
    val desk: Int,
    val freeParking: Int,
    val wifi: Int,
    val kitchen: Int,
    val washer: Int,
    val elevator: Int,
    val paidParking: Int,
    val closet: Int,
    val tv: Int
)

data class Rule(
    val curfew: Int,
    val smoking: Int,
    val drinking: Int,
    val religion: String,
)

data class Safety(
    val cctv: Int,
    val fireExtinguisher: Int,
    val firstAidKit: Int,
    val fireAlarm: Int,
    val carbonMonoxideAlarm: Int
)

data class Pet(
    val dog: Int,
    val cat: Int,
    val etc: Int
)

data class PushAccessAuth(
    val accessToken: String,
    val loginType: String
)
data class AnyAuthToken<T>(
    val statusCode: String,
    val message: String,
    val result: T
)

data class UserBigInfo(
    val externalId : Long,
    val nickname: String,
    val profileImage: String,
    val gender: String
):Serializable

data class SignUpToken(
    val statusCode: String,
    val message: String,
    val result: UserLoginInfo
)

data class UserLoginInfo(
    val accessToken: String
)
data class GetCheckResponse(
    val statusCode: String,
    val message: String,
    val result: ChecklistData
)
data class ChecklistData(
    val month: List<String>? = null,
    val week: List<String>? = null,
    val day: List<String>? = null,
    val etc: List<String>? = null
)

// 메거진 데이터 클래스
data class MagazineResponse(
    val statusCode: String,
    val message: String,
    val result: List<MagazineData>
)

data class MagazineData(
    val id: String,
    val imageUrl: String,
    val tag: String,
    val title: String
)