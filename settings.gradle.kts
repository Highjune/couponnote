rootProject.name = "couponnote"

include(":core")
include(":domain")
include(":infrastructure")
include(":application")
include(":api")

// 외부 소비자용
include(":dto")      // API 응답 타입. api · client 가 공유
include(":client")   // 다른 서비스가 couponnote API 를 호출할 때 쓰는 SDK. mavenLocal publish
