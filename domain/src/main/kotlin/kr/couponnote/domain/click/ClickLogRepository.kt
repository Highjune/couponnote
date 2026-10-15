package kr.couponnote.domain.click

import org.springframework.data.jpa.repository.JpaRepository

interface ClickLogRepository : JpaRepository<ClickLog, Long>
