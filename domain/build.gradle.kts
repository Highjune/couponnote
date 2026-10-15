// 엔티티 · 리포지토리 · 도메인 규칙. JPA 까지만 의존.
plugins {
    kotlin("plugin.jpa")
}

dependencies {
    api(project(":core"))
    api("org.springframework.boot:spring-boot-starter-data-jpa")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}
