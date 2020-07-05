package com.light.rbac.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter@Setter
public class IdListDTO {
	private List<Long> ids = new ArrayList<>();
}
