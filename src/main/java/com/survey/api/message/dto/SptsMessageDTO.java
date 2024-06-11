package com.survey.api.message.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "Data Transfer Object for SptsMessage")
public class SptsMessageDTO {

    @ApiModelProperty(value = "Title of the message", example = "Prerana")
    private String title;

    @ApiModelProperty(value = "Content of the message", example = "Prerana program is in your school! Use stars to motivate the students.")
    private String message;

    @ApiModelProperty(value = "ID of the group", example = "1")
    private Long groupsId;

    @ApiModelProperty(value = "Source of the message", example = "sikshana")
    private String msgSrc;

    @ApiModelProperty(value = "Start date of the message", example = "2018-12-01")
    private Date startDate;

    @ApiModelProperty(value = "End date of the message", example = "2021-01-30")
    private Date endDate;

    // getters and setters
}


