package com.light.oss.response;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class AmbryBlobInfoResponse extends AmbryBaseResponse {

	private String blobSize;
    private String serviceId;
    private Date creationTime;
    private boolean isPrivate;
    private String contentType;
    private String ownerId;
    private String umDesc;
}
