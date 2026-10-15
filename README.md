# couponnote

쿠폰노트 — 마이리얼트립 등 플랫폼의 할인코드·쿠폰을 매달 정리하는 사이트. https://couponnote.kr

- 수익 모델: 마이리얼트립 마케팅파트너 마이링크(CPS). 모든 버튼은 `/go/{couponId}` → 클릭 기록 → 302 마이링크
- 기획·운영 문서: `highjune-archiving/source/projects/coupon-site/`
- 홈서버 인프라: `highjune-archiving/source/projects/tennis-record-k/architecture/`

## 스택

Spring Boot 4.1 · Kotlin 2.3 · Java 21 · JPA · Thymeleaf · Caffeine 캐시 · MySQL (운영) / H2 (로컬)

## 모듈

| 모듈 | 역할 | 의존 |
|---|---|---|
| `core` | 시간(KST)·예외. 스프링 무관 | — |
| `domain` | 엔티티(Brand, Coupon, Article, ClickLog)·리포지토리 | core |
| `infrastructure` | 외부 어댑터. 마크다운 렌더 (예정: 파트너페이지 엑셀 임포트, 쿠폰 수집) | domain |
| `application` | 유스케이스·캐시 | domain, infrastructure |
| `api` | **배포 단위.** 컨트롤러·Thymeleaf·JSON API·설정·Dockerfile | application, dto |
| `dto` | 공개 API 응답 타입(순수 data class). api·client 공유 | — |
| `client` | 다른 서비스가 couponnote API를 호출할 때 쓰는 SDK. `CouponnoteClient` + 자동설정. mavenLocal publish | dto |

## URL

| URL | 내용 |
|---|---|
| `/` | 브랜드 카드 |
| `/{brand}` | 브랜드 메인. 제목에 `[YYYY년 M월]` 자동. 섹션: 쿠폰팩 → 할인코드 → 카드 → 여행지 → 항공 → 특가 |
| `/{brand}/{slug}` | 보조 글 (마크다운) |
| `/go/{couponId}` | 클릭 기록 후 마이링크로 302. `noindex` |
| `/api/v1/brands/{slug}` | JSON. 페이지와 같은 캐시된 데이터. 없으면 `application/problem+json` 404 |
| `/actuator/health`, `/actuator/prometheus` | 운영 |

만료(`ends_at < 오늘`)·비활성 쿠폰은 자동 숨김. 페이지는 5분 캐시(`brandPage` 캐시 키에 오늘 날짜 포함).

## 로컬 실행

```bash
./gradlew :api:bootRun          # http://localhost:8081  (H2 + data.sql 시드)
./gradlew :api:test
```

H2 콘솔: http://localhost:8081/h2-console (JDBC URL `jdbc:h2:mem:couponnote`, sa / 빈 비밀번호)

## client 모듈 (다른 서비스에서 호출할 때)

회사 레포의 `client` 모듈과 같은 역할. 소비자는 HTTP·JSON을 몰라도 된다.

```bash
./gradlew :client:publishToMavenLocal     # kr.couponnote:couponnote-client:0.0.1-SNAPSHOT
```

소비자 쪽 (예: tennis-record-k 홈에 "오늘의 쿠폰"):

```kotlin
// build.gradle.kts
repositories { mavenLocal(); mavenCentral() }
dependencies { implementation("kr.couponnote:couponnote-client:0.0.1-SNAPSHOT") }
```
```yaml
# application.yml
couponnote.client.base-url: https://couponnote.kr
```
```kotlin
class HomeController(private val couponnote: CouponnoteClient) {
    fun home(model: Model) {
        val brand = couponnote.getBrand("myrealtrip")          // null 이면 브랜드 없음
        val first = brand?.sections?.firstOrNull()?.coupons?.firstOrNull()
        model.addAttribute("couponUrl", first?.let { couponnote.absoluteUrl(it.goPath) })
    }
}
```

`base-url`만 있으면 `CouponnoteClient` 빈이 자동 등록된다. 테스트에선 인터페이스를 가짜로 바꿔 끼운다.

## 배포 (홈서버)

1. 최초 1회: `deploy/schema.sql`을 tennis-mysql에서 실행, `coupon` 계정 생성
2. 홈서버 `/home/highjune/services/couponnote/`에 `deploy/docker-compose.yml` 복사 + `.env`에 `COUPON_DB_PASSWORD`
3. nginx `couponnote.kr` server 블록 → `127.0.0.1:8081`
4. 이후: `git push origin release` → Jenkins(2분 폴링) → 빌드·테스트 → DockerHub push → compose up

## 운영 규칙

- 공정위 문구(`couponnote.disclosure`)는 모든 페이지 상단 고정. 문구 임의 수정 금지
- 마이링크(`coupons.target_url`)만 DB에서 바꾸면 모든 버튼에 반영. 글 수정 불필요
- 404는 HTTP 404로 응답 (검색엔진이 빈 페이지를 인덱싱하지 않도록)
- 클릭 로그의 IP는 SHA-256 해시만 저장
