package com.light.oss.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AmbryUMResponse extends AmbryBaseResponse {
    private String umDesc;
}
