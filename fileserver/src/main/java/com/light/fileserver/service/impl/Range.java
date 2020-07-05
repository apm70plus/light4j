package com.light.fileserver.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Range {
    private static final int DEFAULT_BUFFER_SIZE = 20480; // ..bytes = 20KB.
    long start;
    long end;
    long length;
    long total;

    /**
     * Construct a byte range.
     *
     * @param start Start of the byte range.
     * @param end End of the byte range.
     * @param total Total length of the byte source.
     */
    public Range(final long start, final long end, final long total) {
        this.start = start;
        this.end = end;
        this.length = (end - start) + 1;
        this.total = total;
    }

    public static long sublong(final String value, final int beginIndex, final int endIndex) {
        final String substring = value.substring(beginIndex, endIndex);
        return (substring.length() > 0) ? Long.parseLong(substring) : -1;
    }

    public static void copy(final InputStream input, final OutputStream output, final long inputSize,
            final long start, final long length) throws IOException {
        final byte[] buffer = new byte[Range.DEFAULT_BUFFER_SIZE];
        int read;

        if (inputSize == length) {
            // Write full range.
            while ((read = input.read(buffer)) > 0) {
                output.write(buffer, 0, read);
                output.flush();
            }
        } else {
            input.skip(start);
            long toRead = length;

            while ((read = input.read(buffer)) > 0) {
                if ((toRead -= read) > 0) {
                    output.write(buffer, 0, read);
                    output.flush();
                } else {
                    output.write(buffer, 0, (int) toRead + read);
                    output.flush();
                    break;
                }
            }
        }
    }
}
