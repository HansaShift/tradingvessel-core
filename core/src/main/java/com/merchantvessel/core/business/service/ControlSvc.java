package com.merchantvessel.core.business.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.merchantvessel.core.business.enumeration.ECtrlVar;
import com.merchantvessel.core.persistence.model.CtrlVar;
import com.merchantvessel.core.persistence.repository.CtrlVarRepo;

@Service
public class ControlSvc {

	@Autowired
	private LogSvc logSvc;
	@Autowired
	private CtrlVarRepo ctrlVarRepo;

	// Date Formats
	public static SimpleDateFormat dateFormatAPI = new SimpleDateFormat("yyyyMMMddHH24mmss");
	public static SimpleDateFormat dateFormatSQLTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat dateFormatSQLDate = new SimpleDateFormat("yyyy-mm-dd");
	public static SimpleDateFormat dateFormatYYYYMMDD = new SimpleDateFormat("yyyymmdd");
	private static String globalTimeZone = "Asia/Manila";

	// Central Dates
	public static Date minDate = new GregorianCalendar(1000, 0, 1).getTime();
	public static Date maxDate = new GregorianCalendar(3000, 11, 1).getTime();
	private static LocalDateTime minDateLocalDateTime = minDate.toInstant().atZone(ZoneId.of(globalTimeZone))
			.toLocalDateTime();
	public static LocalDateTime maxDateLocalDateTime = maxDate.toInstant().atZone(ZoneId.of(globalTimeZone))
			.toLocalDateTime();

	// --------------------------------------------------------------
	// DATE OPERATIONS
	// --------------------------------------------------------------
	public LocalDateTime getCurrentDate() {

		LocalDateTime finDate = new Date().toInstant().atZone(ZoneId.of(globalTimeZone)).toLocalDateTime();

		finDate = getLastDayOfYear(finDate);
		return finDate;
	}

	public LocalDateTime getFinDate() {

		CtrlVar ctrlVarFinDate = getByEnum(ECtrlVar.FIN_DATE);

		LocalDateTime finDateDB = null;
		if (ctrlVarFinDate != null) {
			finDateDB = ctrlVarFinDate.getValDate();
		}

		if (finDateDB == null) {
			ctrlVarFinDate.setValDate(getCurrentDate());
			ctrlVarRepo.save(ctrlVarFinDate);
			return finDateDB;
		} else {

			return finDateDB;
		}
	}

	public int getFinYear() {
		return getFinDate().getYear();
	}

	public LocalDateTime getLastDayOfYear(LocalDateTime localDate) {
		return localDate.with(TemporalAdjusters.lastDayOfYear());
	}

	public int setFinYear(int finYear) {
		CtrlVar ctrlVarFinDate = getByEnum(ECtrlVar.FIN_DATE);
		LocalDateTime finDateLocal = ctrlVarFinDate.getValDate();
		finDateLocal = finDateLocal.plusYears(finYear - finDateLocal.getYear());
		ctrlVarFinDate.setValDate(finDateLocal);
		ctrlVarFinDate = ctrlVarRepo.save(ctrlVarFinDate);
		return ctrlVarFinDate.getValDate().getYear();
	}

	// --------------------------------------------------------------
	// CONTROL VARIABLES
	// --------------------------------------------------------------
	// CREATE DATE
	public CtrlVar create(ECtrlVar eCtrlVar, LocalDateTime dateVal) {
		if (ctrlVarRepo.findByKey(eCtrlVar.toString()) != null) {
			return null;
		}
		CtrlVar ctrlVar = new CtrlVar(eCtrlVar.getDataType(), eCtrlVar.getName(), eCtrlVar.toString(), null, dateVal, 0,
				false);
		return ctrlVarRepo.save(ctrlVar);
	}

	// CREATE BOOLEAN
	public CtrlVar create(ECtrlVar eCtrlVar, boolean valBool) {
		if (ctrlVarRepo.findByKey(eCtrlVar.toString()) != null) {
			return null;
		}
		CtrlVar ctrlVar = new CtrlVar(eCtrlVar.getDataType(), eCtrlVar.getName(), eCtrlVar.toString(), null, null, 0,
				valBool);
		return ctrlVarRepo.save(ctrlVar);
	}

	// CREATE DOUBLE
	public CtrlVar create(ECtrlVar eCtrlVar, double valDbl) {
		if (ctrlVarRepo.findByKey(eCtrlVar.toString()) != null) {
			return null;
		}
		CtrlVar ctrlVar = new CtrlVar(eCtrlVar.getDataType(), eCtrlVar.getName(), eCtrlVar.toString(), null, null,
				valDbl, false);
		return ctrlVarRepo.save(ctrlVar);
	}

	// GET BOOLEAN
	public CtrlVar setVal(ECtrlVar eCtrlVar, boolean valBool) {
		CtrlVar ctrlVar = ctrlVarRepo.findByKey(eCtrlVar.toString());
		if (ctrlVar == null) {
			logSvc.write("ControlSvc.setVal(ECtrlVar, LocalDateTime)",
					"Control Variable with key: '" + eCtrlVar.toString() + "' could not be found.");
		}
		ctrlVar.setValBool(valBool);
		return ctrlVarRepo.save(ctrlVar);
	}

	// GET DATE
	public CtrlVar setVal(ECtrlVar eCtrlVar, LocalDateTime valDate) {
		CtrlVar ctrlVar = ctrlVarRepo.findByKey(eCtrlVar.toString());
		if (ctrlVar == null) {
			logSvc.write("ControlSvc.setVal(ECtrlVar, LocalDateTime)",
					"Control Variable with key: '" + eCtrlVar.toString() + "' could not be found.");
		}
		ctrlVar.setValDate(valDate);
		return ctrlVarRepo.save(ctrlVar);
	}

	// GET DOUBLE
	public CtrlVar setVal(ECtrlVar eCtrlVar, double valDouble) {
		CtrlVar ctrlVar = ctrlVarRepo.findByKey(eCtrlVar.toString());
		if (ctrlVar == null) {
			logSvc.write("ControlSvc.setVal(ECtrlVar, double)",
					"Control Variable with key: '" + eCtrlVar.toString() + "' could not be found.");
		}
		ctrlVar.setValDouble(valDouble);
		return ctrlVarRepo.save(ctrlVar);
	}

	// GET STRING
	public CtrlVar setVal(ECtrlVar eCtrlVar, String valString) {
		CtrlVar ctrlVar = ctrlVarRepo.findByKey(eCtrlVar.toString());
		if (ctrlVar == null) {
			logSvc.write("ControlSvc.setVal(ECtrlVar, String)",
					"Control Variable with key: '" + eCtrlVar.toString() + "' could not be found.");
		}
		ctrlVar.setValString(valString);
		return ctrlVarRepo.save(ctrlVar);
	}

	public CtrlVar getByEnum(ECtrlVar eCtrlVar) {
		return ctrlVarRepo.findByKey(eCtrlVar.toString());
	}

	public List<CtrlVar> getAll() {
		return ctrlVarRepo.findAll();
	}

	public String getGlobalTimeZone() {
		return globalTimeZone;
	}

	public static void setGlobalTimeZone(String globalTimeZone) {
		ControlSvc.globalTimeZone = globalTimeZone;
	}

	public LocalDateTime getMinDateLocalDateTime() {
		return minDateLocalDateTime;
	}

	public static void setMinDateLocalDateTime(LocalDateTime minDateLocalDateTime) {
		ControlSvc.minDateLocalDateTime = minDateLocalDateTime;
	}

	public LocalDateTime getMaxDateLocalDateTime() {
		return maxDateLocalDateTime;
	}

	public static void setMaxDateLocalDateTime(LocalDateTime maxDateLocalDateTime) {
		ControlSvc.maxDateLocalDateTime = maxDateLocalDateTime;
	}

}
