package com.survey.api.config.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.survey.api.common.exception.BadRequestException;
import com.survey.api.config.entity.RegionData;
import com.survey.api.config.entity.RegionTypeMaster;
import com.survey.api.config.repo.RegionDataRepo;
import com.survey.api.config.repo.RegionTypeRepo;

@Service
public class ConfigRegionDataService {

	@Autowired
	RegionTypeRepo regionTypeRepo;

	@Autowired
	RegionDataRepo regionDataRepo;
	
	private RegionData parentRegionData= null;


	public Boolean uploadRegionData(MultipartFile file) {

		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
				CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT);) {

			List<CSVRecord> csvRecords = csvParser.getRecords();
			long totalRegionHerarcy = regionTypeRepo.count();

			List<RegionTypeMaster> regionTypesList = regionTypeRepo.findAllByOrderByIdAsc();
			for (RegionTypeMaster regionTypeMaster : regionTypesList) {
				System.out.println(regionTypeMaster.getName());
			}
			// Assumption is there will only be CODE and NAME in sequence

			loadRegionData(csvRecords, regionTypesList);

		} catch (IOException e) {

			e.printStackTrace();
		}

		return true;
	}

	private void loadRegionData(List<CSVRecord> csvRecords, List<RegionTypeMaster> regionTypesList) {
		// One CSV record will have commit equal to herirachy
		CSVRecord csvRecord = null;
		try {
			// skip first two records
			int titleRecordIndex = 0;
			for (int j=0; j< csvRecords.size(); j++) {
				// Skip first two records as they are headers
				if (titleRecordIndex < 2) {
					titleRecordIndex++;
					continue;
				}
				int insertHerarchyIndex = 0;

				//load the Record;
				csvRecord = csvRecords.get(j);
				System.out.println("*****"+csvRecord);

				//Check if Records is of expected length 
				if(csvRecord.size() != (regionTypesList.size()-1)*2) {
					throw new BadRequestException("Issue in size of record please check "+csvRecord);
				}
				
				parentRegionData = null;
				for (RegionTypeMaster regionTypeMaster : regionTypesList) {
					
					if (regionTypeMaster.getParentRegionType() == null) {
						continue;
					}
					insertRegionLevelWise(regionTypeMaster, insertHerarchyIndex, csvRecord);
					insertHerarchyIndex = insertHerarchyIndex + 2;
				}
			}
		} catch (Exception e) {
			throw new BadRequestException("Issue in importing the File with Record "+csvRecord);
		}

	}

	private void insertRegionLevelWise(RegionTypeMaster regionTypeMaster, int insertHerarchyLevel,
			CSVRecord csvRecord) {
		
		Optional<RegionData> ExistingEntry=regionDataRepo.findByCode(csvRecord.get(insertHerarchyLevel));
		if(ExistingEntry.isPresent()) {
			System.out.println("Data Exist so skip for code "+csvRecord.get(insertHerarchyLevel));
			parentRegionData  =ExistingEntry.get();
			return;
			
		}
		System.out.println("Data Do not Exist entring for code "+csvRecord.get(insertHerarchyLevel));

		RegionData entity= new RegionData();
		entity.setCode(csvRecord.get(insertHerarchyLevel));
		entity.setName(csvRecord.get(insertHerarchyLevel+1));
		entity.setRegionType(regionTypeMaster);
		entity.setType(regionTypeMaster.getName());
		entity.setParentRegion(parentRegionData);
		//check the parent region
		entity = regionDataRepo.save(entity);
		parentRegionData=entity;
	}

}
