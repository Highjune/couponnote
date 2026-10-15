// 외부 세계와의 어댑터: 마크다운 렌더, (예정) 파트너페이지 엑셀 임포트, 쿠폰 수집 크롤러.
dependencies {
    api(project(":domain"))
    implementation("org.springframework:spring-context")
    implementation("com.vladsch.flexmark:flexmark-all:0.64.8")
}
