// 유스케이스. 컨트롤러가 부르는 서비스 계층. 캐시는 여기서.
dependencies {
    api(project(":domain"))
    api(project(":infrastructure"))
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-tx")
    implementation("org.springframework.boot:spring-boot-starter-cache")
}
