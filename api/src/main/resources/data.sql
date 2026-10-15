-- 로컬(H2) 시드. 운영 데이터 아님. 마이링크는 가입 후 발급받은 값으로 교체.
INSERT INTO brands (slug, name, category, tagline, sort_order, active, created_at, updated_at) VALUES
  ('myrealtrip', '마이리얼트립', 'TRAVEL',   '투어·티켓·항공·숙소 예약', 1, true, NOW(), NOW()),
  ('coupang',    '쿠팡',         'SHOPPING', '로켓배송·로켓직구 (준비 중)', 1, false, NOW(), NOW());

INSERT INTO coupons (brand_id, kind, title, code, benefit, condition, starts_at, ends_at, target_url, sort_order, active, created_at, updated_at) VALUES
  (1, 'PACK',   '히든 쿠폰팩 한 번에 받기',          NULL,        '투어·숙소 쿠폰 일괄 발급',  '마이리얼트립 회원, 발급 후 7일 내 사용', NULL, NULL, 'https://www.myrealtrip.com/promotions/marketing_partner_coupon', 1, true, NOW(), NOW()),
  (1, 'CODE',   '파트너 할인코드',                   'EXAMPLE10', '해외 투어·티켓 최대 5,000원', '15만원 이상 1건당', NULL, DATEADD('DAY', 20, CURRENT_DATE), 'https://www.myrealtrip.com/promotions/checkout', 1, true, NOW(), NOW()),
  (1, 'CARD',   '삼성카드 즉시할인',                 NULL,        '항공권 최대 15%',           '카드 결제 시 자동 적용', NULL, DATEADD('DAY', 16, CURRENT_DATE), 'https://www.myrealtrip.com/', 1, true, NOW(), NOW()),
  (1, 'REGION', '다낭 투어 쿠폰',                    NULL,        '10% 할인',                  '다낭 지역 투어 한정', NULL, NULL, 'https://www.myrealtrip.com/promotions/bangkokpattaya_tourticket', 1, true, NOW(), NOW()),
  (1, 'FLIGHT', '10월 항공권 프로모션',              NULL,        '특가 노선 모음',            NULL, NULL, DATEADD('DAY', 16, CURRENT_DATE), 'https://www.myrealtrip.com/', 1, true, NOW(), NOW()),
  (1, 'CODE',   '만료된 코드 (노출 안 됨)',           'OLD',       '테스트',                    NULL, NULL, DATEADD('DAY', -1, CURRENT_DATE), 'https://www.myrealtrip.com/', 9, true, NOW(), NOW());

INSERT INTO articles (brand_id, slug, title, description, body_markdown, published, created_at, updated_at) VALUES
  (1, 'new-member', '마이리얼트립 신규회원 혜택 정리', '첫 가입 쿠폰, 앱 설치 혜택, 첫 예약 적립을 한 번에 정리했습니다.',
   '## 첫 가입 쿠폰

가입 직후 **쿠폰함**에서 확인할 수 있습니다.

| 구분 | 혜택 |
|---|---|
| 투어·티켓 | 5천원 |
| 숙소 | 3천원 |

## 앱 설치 혜택

앱 첫 예약 시 추가 할인.', true, NOW(), NOW());
