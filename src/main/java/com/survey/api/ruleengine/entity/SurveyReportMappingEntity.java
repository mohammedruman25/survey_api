//package com.survey.api.ruleengine.entity;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import lombok.ToString;
//
//@Entity
////@Table(name = "SURVEY_REPORT_MAPPING_DATA", indexes = {@Index(name = "survey_rep_mapping_index1",  columnList="survey_id", unique = false),
////        @Index(name = "survey_rep_mapping_index2", columnList="target_designation_id",     unique = false),
////        @Index(name = "survey_rep_mapping_index3", columnList="target_region_data_id",     unique = false)})
//@Table(name = "SURVEY_REPORT_MAPPING_DATA")
//@NoArgsConstructor
//@Getter
//@Setter
//@ToString
//public class SurveyReportMappingEntity {
//	
//	private static final long serialVersionUID = 1L;
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//	
//	@Column(name = "survey_id")
//	private Long surveyId;
//	
//	@Column(name = "parent_region_data_id")
//	private Long parentRegionDataId;
//	
//	
//}
