package com.merchantvessel.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.merchantvessel.core.model.jpa.Log;

@Repository
public interface LogRepo extends JpaRepository<Log, Long> {

	List<Log> findFirst20ByOrderByTimestampCreateDesc();

}
