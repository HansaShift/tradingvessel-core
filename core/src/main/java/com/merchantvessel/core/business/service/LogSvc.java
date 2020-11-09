package com.merchantvessel.core.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.merchantvessel.core.persistence.model.Log;
import com.merchantvessel.core.persistence.repository.LogRepo;

@Service
public class LogSvc {

	@Autowired
	private LogRepo logRepo;

	public void write(String msg) {

		Log log = new Log(msg);
		logRepo.save(log);
	}

	public List<Log> getAll() {
		return logRepo.findAll();
	}

	public List<Log> getRecentEntries() {
		return logRepo.findFirst20ByOrderByTimestampCreateDesc();

	}

}
