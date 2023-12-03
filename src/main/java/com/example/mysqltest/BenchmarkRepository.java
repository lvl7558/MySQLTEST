// BenchmarkRepository.java
package com.example.mysqltest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
public interface BenchmarkRepository extends JpaRepository<BenchmarkEntity, Long> {
}