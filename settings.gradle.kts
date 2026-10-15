rootProject.name = "couponnote"

include(":core")
include(":domain")
include(":infrastructure")
include(":application")
include(":api")

// 외부 소비자가 생기면 주석 해제. 코드는 dto/, client/ 에 그대로 있음 (README "client 모듈" 참고)
// include(":dto")      // 공개 API 요청·응답 타입. api · client 가 공유
// include(":client")   // 다른 서비스가 couponnote API 를 호출할 때 쓰는 SDK. mavenLocal publish
