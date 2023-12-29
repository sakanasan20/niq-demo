package tw.niq.demo.service;

import java.io.File;
import java.util.List;

import tw.niq.demo.model.BeerCsvRecord;

public interface BeerCsvService {

	List<BeerCsvRecord> convertCsv(File file);
	
}
