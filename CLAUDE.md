# CLAUDE.md — couponnote

쿠폰 어필리에이트 사이트. 구조·URL·운영 규칙은 `README.md`가 SSOT. 여기엔 작업 규칙만.

## 규칙

- **공정위 문구**(`couponnote.disclosure`)는 삭제·수정 금지. 모든 공개 페이지 상단에 노출돼야 한다.
- **마이링크 버튼은 반드시 `/go/{couponId}`를 거친다.** 템플릿에 `myrealt.rip` 주소를 직접 쓰지 않는다.
- 404는 HTTP 404. 200 + 오류 페이지 금지.
- 개인정보: 클릭 로그에 IP 원문·이메일 저장 금지. 해시만.
- 비밀번호·토큰은 코드/yml에 쓰지 않는다. 환경변수(`DB_PASSWORD` 등)로.
- 운영 스키마 변경은 `deploy/schema.sql`에 ALTER 추가 + `ddl-auto=validate` 유지. 자동 DDL 금지.
- 모듈 의존 방향: `api → application → infrastructure → domain → core`. 역방향 금지.
- 배포 트리거는 `release` 브랜치. `main`은 개발 기준.

## 기획·인프라 문서

- 기획·TODO: `~/workspace/highjune/highjune-archiving/source/projects/coupon-site/README.md`
- 홈서버·nginx: `~/workspace/highjune/highjune-archiving/source/projects/tennis-record-k/architecture/`
- 홈서버 작업을 하면 archiving 문서를 같은 세션에서 갱신한다.
