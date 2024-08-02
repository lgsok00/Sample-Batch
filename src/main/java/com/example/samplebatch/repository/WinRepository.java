package com.example.samplebatch.repository;

import com.example.samplebatch.entity.WinEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WinRepository extends JpaRepository<WinEntity, Long> {

  // 승리 횟수 값이 특정 상수 값보다 더 크거나 같을 경우 조회하는 메서드
  Page<WinEntity> findByWinGreaterThanEqual(Long win, Pageable pageable);
}
