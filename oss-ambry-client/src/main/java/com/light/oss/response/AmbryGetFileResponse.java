package com.light.oss.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.InputStream;

@Data
@EqualsAndHashCode(callSuper = false)
public class AmbryGetFileResponse extends AmbryBaseResponse {

    private String blobSize;
    private String contentType;

    /**
     * The file object get from Ambry.
     */
    private InputStream content;
}
