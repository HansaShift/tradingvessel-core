package com.merchantvessel.core.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.merchantvessel.core.persistence.model.Log;

@Repository
public interface LogRepo extends JpaRepository<Log, Long> {

	List<Log> findFirst20ByOrderByTimestampCreateDesc();

}
