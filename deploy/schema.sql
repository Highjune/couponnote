-- 운영 DDL. tennis-mysql 에서 1회 실행. (beta 프로파일은 ddl-auto=validate)
CREATE DATABASE IF NOT EXISTS coupon CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- CREATE USER 'coupon'@'%' IDENTIFIED BY '<비밀번호>';
-- GRANT ALL ON coupon.* TO 'coupon'@'%';
USE coupon;

CREATE TABLE brands (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  slug        VARCHAR(40)  NOT NULL UNIQUE,
  name        VARCHAR(60)  NOT NULL,
  category    VARCHAR(20)  NOT NULL,  -- TRAVEL / SHOPPING / SUBSCRIPTION (BrandCategory)
  tagline     VARCHAR(120) NOT NULL,
  sort_order  INT          NOT NULL DEFAULT 0,
  active      BIT(1)       NOT NULL DEFAULT b'1',
  created_at  DATETIME(6)  NOT NULL,
  updated_at  DATETIME(6)  NOT NULL
) ENGINE=InnoDB;

CREATE TABLE coupons (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  brand_id    BIGINT       NOT NULL,
  kind        VARCHAR(20)  NOT NULL,
  title       VARCHAR(120) NOT NULL,
  code        VARCHAR(60)  NULL,
  benefit     VARCHAR(120) NOT NULL,
  `condition` VARCHAR(300) NULL,
  starts_at   DATE         NULL,
  ends_at     DATE         NULL,
  target_url  VARCHAR(300) NOT NULL,              -- 마이링크 (myrealt.rip/...)
  landing_url VARCHAR(300) NULL,                  -- 마이링크가 가리키는 원본 URL
  sort_order  INT          NOT NULL DEFAULT 0,
  featured    BIT(1)       NOT NULL DEFAULT b'0',  -- 섹션 내 강조(BEST)
  active      BIT(1)       NOT NULL DEFAULT b'1',
  created_at  DATETIME(6)  NOT NULL,
  updated_at  DATETIME(6)  NOT NULL,
  CONSTRAINT fk_coupons_brand FOREIGN KEY (brand_id) REFERENCES brands(id),
  INDEX idx_coupons_brand_kind (brand_id, kind, sort_order)
) ENGINE=InnoDB;

CREATE TABLE articles (
  id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  brand_id      BIGINT       NOT NULL,
  slug          VARCHAR(80)  NOT NULL,
  title         VARCHAR(150) NOT NULL,
  description   VARCHAR(160) NOT NULL,
  body_markdown TEXT         NOT NULL,
  published     BIT(1)       NOT NULL DEFAULT b'0',
  created_at    DATETIME(6)  NOT NULL,
  updated_at    DATETIME(6)  NOT NULL,
  CONSTRAINT fk_articles_brand FOREIGN KEY (brand_id) REFERENCES brands(id),
  UNIQUE KEY uk_articles_brand_slug (brand_id, slug)
) ENGINE=InnoDB;

CREATE TABLE click_logs (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  coupon_id   BIGINT       NOT NULL,
  path        VARCHAR(200) NOT NULL,
  referer     VARCHAR(500) NULL,
  user_agent  VARCHAR(300) NULL,
  ip_hash     VARCHAR(64)  NOT NULL,
  clicked_at  DATETIME(6)  NOT NULL,
  INDEX idx_click_logs_coupon_at (coupon_id, clicked_at),
  INDEX idx_click_logs_at (clicked_at)
) ENGINE=InnoDB;

-- 2026-10-15 브랜드 카테고리 추가 (최초 생성 이후 적용한 서버에만 실행)
-- ALTER TABLE brands ADD COLUMN category VARCHAR(20) NOT NULL DEFAULT 'TRAVEL' AFTER name;
-- ALTER TABLE brands ALTER COLUMN category DROP DEFAULT;
-- 2026-10-15 쿠폰 강조 플래그
-- ALTER TABLE coupons ADD COLUMN featured BIT(1) NOT NULL DEFAULT b'0' AFTER sort_order;
-- 2026-10-16 원본 URL
-- ALTER TABLE coupons ADD COLUMN landing_url VARCHAR(300) NULL AFTER target_url;
