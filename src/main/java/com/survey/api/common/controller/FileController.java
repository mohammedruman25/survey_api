package com.survey.api.common.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.survey.api.auth.domain.UserAuthDetails;
import com.survey.api.common.dto.SurveyAppFileDto;
import com.survey.api.common.service.SurveyAppFileService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/file")
public class FileController {

	@Autowired
	SurveyAppFileService surFileService;
	
	@ApiOperation( value = "Get Answer File")
	@GetMapping("/getFile/{UUID}")
	public void getAnswerFile(@PathVariable("UUID") String uuid, Authentication authentication, HttpServletResponse response) throws IOException {

		UserAuthDetails user = (UserAuthDetails) authentication.getPrincipal();
		SurveyAppFileDto result = surFileService.getFile(uuid);
    	
		response.setHeader("Content-Disposition", "attachment; filename=" + result.getOriginalFileName());
		response.getOutputStream().write(result.getFileData());
	}
    	
}
