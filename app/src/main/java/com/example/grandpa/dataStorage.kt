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
    val buildingFloor: Int,
    val roomFloor: Int,
    val roomSize: Float,
    val buildingType: String,
    val moveInDate: String,
    val address: String,
    val coordinate: Location,
    val postDate: String,
    val updateDate: String,
    val images: List<String>,
    val detail: Detail,
    val landlordProfile: landlordProfile,
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

data class landlordProfile(
    val userId: Int,
    val profileImageUrl: String,
    val name: String,
    val rating: Float,
    val reviewCount: Int
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
    val week: List<String>,
    val day: List<String>,
    val etc: List<String>
)

// 메거진 데이터 클래스
data class MagazineResponse(
    val statusCode: String,
    val message: String,
    val result: List<MagazineData>
)

data class MagazineData(
    val id: String,
    val imageUrl: String
)

// 메거진 상세 조회
data class MagazineDetailResponse(
    val statusCode: String,
    val message: String,
    val result: MagazineDetailData
)

data class MagazineDetailData(
    val id: String,
    val imageUrl: String,
    val content: String
)

// 방 상세 조회의 임대인 프로필 조회
data class DetailProfileResponse(
    val statusCode: String,
    val message: String,
    val result: landlordDetailProfile
)

data class landlordDetailProfile(
    val id: Int,
    val profileImageUrl: String,
    val name: String,
    val introduction : String,
    val rating: Float,
    val reviewCount: Int,
    val reviews : List<ReviewData>
)

data class ReviewData(
    val id: Int,
    val profileImageUrl: String,
    val name: String,
    val rating: Float,
    val content: String,
    val postDate: String,
    val updateDate: String
)