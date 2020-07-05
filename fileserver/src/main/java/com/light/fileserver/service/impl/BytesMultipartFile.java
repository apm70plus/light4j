package com.light.fileserver.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class BytesMultipartFile implements MultipartFile {

    private final ByteArrayInputStream inputStream;

    private final String DEFAULT_NAME = "base64.jpg";

    private String originalFilename;

    private String name;

    private long size;

    public BytesMultipartFile(final byte[] bytes, final String originalName) {
        if ((bytes == null) || (bytes.length == 0)) {
            this.size = 0;
            this.inputStream = new ByteArrayInputStream(new byte[0]);
        } else {
            this.size = bytes.length;
            this.inputStream = new ByteArrayInputStream(bytes);
        }
        if (StringUtils.hasText(originalName)) {
            this.originalFilename = originalName;
            this.name = FilenameUtils.getName(this.originalFilename);
        } else {
            this.originalFilename = this.name = this.DEFAULT_NAME;
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getOriginalFilename() {
        return this.originalFilename;
    }

    @Override
    public String getContentType() {
        return "base64";
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public long getSize() {
        return this.size;
    }

    @Override
    public byte[] getBytes() throws IOException {
        final byte[] bytes = new byte[(int) this.size];
        this.inputStream.read(bytes);
        return bytes;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return this.inputStream;
    }

    @Override
    public void transferTo(final File dest) throws IOException, IllegalStateException {
        throw new UnsupportedOperationException();
    }

}
