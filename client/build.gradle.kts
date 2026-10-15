// couponnote API 호출 SDK. 소비자는 이 모듈만 의존한다.
//   publish: ./gradlew :client:publishToMavenLocal
//   consume: implementation("kr.couponnote:couponnote-client:<version>")
//            + couponnote.client.base-url=https://couponnote.kr
plugins {
    `maven-publish`
}

dependencies {
    api(project(":dto"))
    implementation("org.springframework:spring-web")
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("tools.jackson.core:jackson-databind")
    implementation("tools.jackson.module:jackson-module-kotlin")
    compileOnly("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework:spring-test")
    testImplementation("org.springframework.boot:spring-boot-test")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "couponnote-client"
            from(components["java"])
        }
    }
}
