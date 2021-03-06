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

	public void write(String location, String msg) {
		write(location, msg, false);
	}

	public void write(String location, String msg, boolean sysErr) {
		Log log = new Log(location, msg);
		logRepo.save(log);
		if (sysErr) {
			System.err.println(location + " : " + msg);
		}
	}

	public List<Log> getAll() {
		return logRepo.findAll();
	}

	public List<Log> getRecentEntries() {
		return logRepo.findFirst20ByOrderByTimestampCreateDesc();

	}

}
