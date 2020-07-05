package com.light.oss.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;

import com.light.oss.http.HttpHelper;
import com.light.oss.response.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AmbryClient implements IAmbryClient {

    private final String endpoint;

    private HttpHelper httpHelper = new HttpHelper();

    public AmbryClient(String hostname, int port) {
        if (!hostname.contains("://")) {
            hostname = "http://" + hostname;
        }
        this.endpoint = hostname + ":" + port;
    }

    public AmbryClient(String hostname) {
        this(hostname, 1174);// set with default port
    }

    @Override
    public AmbryBaseResponse healthCheck() {
        AmbryHttpResponse httpResponse = httpHelper.get(endpoint + "/healthCheck");
        String content = new String(readFromStream(httpResponse.getContent()));
        AmbryBaseResponse response = new AmbryBaseResponse();
        response.setStatus(content);
        response.setCode(httpResponse.getCode());
        response.setMessage(httpResponse.getMessage());
        return response;
    }

    @Override
    public AmbryBlobInfoResponse getFileProperty(String ambryId) {
        AmbryHttpResponse httpResponse = httpHelper.get(endpoint +"/"+ambryId+"/BlobInfo");
        AmbryBlobInfoResponse response = new AmbryBlobInfoResponse();
        response.setCode(httpResponse.getCode());
        response.setStatus(httpResponse.getStatus());

        Header[] headers = httpResponse.getHeaders();
        if (headers == null) {
            response.setMessage("Get file properties fail.");
            return response;
        }
        for (Header header : headers) {
            switch (header.getName()) {
                case AmbryHttpResponse.Headers.BLOB_SIZE:
                    response.setBlobSize(header.getValue());
                    break;
                case AmbryHttpResponse.Headers.SERVICE_ID:
                    response.setServiceId(header.getValue());
                    break;
                //case AmbryHttpResponse.Headers.CREATION_TIME:
                //    response.setCreationTime(new Date(header.getValue()));
                //    break;
                case AmbryHttpResponse.Headers.PRIVATE:
                    response.setPrivate(Boolean.parseBoolean(header.getValue()));
                    break;
                case AmbryHttpResponse.Headers.AMBRY_CONTENT_TYPE:
                    response.setContentType(header.getValue());
                    break;
                case AmbryHttpResponse.Headers.OWNER_ID:
                    response.setOwnerId(header.getValue());
                    break;
                case AmbryHttpResponse.Headers.USER_META_DATA_HEADER_PREFIX+"description":
                    response.setUmDesc(header.getValue());
                    break;
                default:
                    break;
            }
        }
        response.setMessage(httpResponse.getMessage());
        return response;
    }

    @Override
    public AmbryGetFileResponse getFile(String ambryId) {
        AmbryHttpResponse httpResponse = httpHelper.get(endpoint +"/"+ambryId);
        AmbryGetFileResponse response = new AmbryGetFileResponse();
        response.setCode(httpResponse.getCode());
        response.setStatus(httpResponse.getStatus());
        response.setMessage(httpResponse.getMessage());
        response.setContent(httpResponse.getContent());

        Header[] headers = httpResponse.getHeaders();
        if (headers == null) {
            return response;
        }
        for (Header header : headers) {
            switch (header.getName()) {
                case AmbryHttpResponse.Headers.BLOB_SIZE:
                    response.setBlobSize(header.getValue());
                    break;
                case AmbryHttpResponse.Headers.AMBRY_CONTENT_TYPE:
                    response.setContentType(header.getValue());
                    break;
                default:
                    break;
            }
        }
        return response;
    }

    @Override
    public AmbryGetFileResponse getFile(String ambryId, File localFile) {
        AmbryGetFileResponse response = getFile(ambryId);
        if (response.getContent() != null) {
            readFromStream(response.getContent(), localFile);
        }
        return response;
    }

    @Override
    public AmbryPostFileResponse postFile(String filePath, String fileType) {
        File file = new File(filePath);
        return postFile(file, fileType);
    }

    @Override
    public AmbryPostFileResponse postFile(File file, String fileType) {
        AmbryPostFileResponse response = new AmbryPostFileResponse();

        if (!file.exists()) {
            response.setCode(404);
            response.setMessage("Local file does not exist: " + file.getPath());
            return response;
        }

        try {
            InputStream inputStream = new FileInputStream(file);
            response = postFile(readFromStream(inputStream), fileType);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public AmbryPostFileResponse postFile(byte[] fileBytes, String fileType) {
        AmbryPostFileResponse response = new AmbryPostFileResponse();

        if(fileType==null || fileType.equals("")) {
            fileType = "text/plain";
        }
        AmbryHttpResponse httpResponse = httpHelper.postFile(endpoint + "/", generateHeaders(fileBytes, fileType), fileBytes);
        response.setCode(httpResponse.getCode());
        response.setStatus(httpResponse.getStatus());

        Header[] headers = httpResponse.getHeaders();
        if (headers == null) {
            response.setMessage("Upload file failure.");

            return response;
        }
        for (Header header : headers) {
            if (AmbryHttpResponse.Headers.LOCATION.equals(header.getName())) {
                response.setAmbryId(header.getValue());
                log.info("ambry post file get resource id: " + header.getValue());
            }
        }
        response.setMessage(httpResponse.getMessage());
        return response;
    }

    @Override
    public AmbryBaseResponse deleteFile(String ambryId) {
        AmbryHttpResponse httpResponse = httpHelper.delete(endpoint + "/" + ambryId);

        AmbryBaseResponse response = new AmbryBaseResponse();
        response.setCode(httpResponse.getCode());
        response.setStatus(httpResponse.getStatus());
        response.setMessage(httpResponse.getMessage());
        return response;
    }

    @Override
    public AmbryUMResponse getFileUserMetadata(String ambryId) {
        AmbryHttpResponse httpResponse = httpHelper.get(endpoint +"/"+ambryId+"/UserMetadata");
        AmbryUMResponse response = new AmbryUMResponse();
        response.setCode(httpResponse.getCode());
        response.setStatus(httpResponse.getStatus());
        response.setMessage(httpResponse.getMessage());

        Header[] headers = httpResponse.getHeaders();
        if (headers == null) {
            return response;
        }
        for (Header header : headers) {
            if ((AmbryHttpResponse.Headers.USER_META_DATA_HEADER_PREFIX + "description").equals(header.getName())) {
                response.setUmDesc(header.getValue());
            }
        }
        return response;
    }

    private byte[] readFromStream(InputStream inputStream) {
        ByteArrayOutputStream buff = new ByteArrayOutputStream();
        byte[] readBuff = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(readBuff)) != -1) {
                buff.write(readBuff, 0, len);
            }
            try {
                buff.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            log.error("Cannot read content from stream: ", e);
        } finally {
            try {
                buff.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buff.toByteArray();
    }

    private void readFromStream(InputStream inputStream, File file) {
        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            byte[] readBuff = new byte[1024];
            int len;
            while ((len = inputStream.read(readBuff)) != -1) {
                outputStream.write(readBuff, 0, len);
            }
            //FileChannel channel = new FileOutputStream(file).getChannel();
            //inputStream
        } catch (IOException e) {
            log.error("Cannot read content from stream: ", e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Map<String, String> generateHeaders(byte[] fileContent, String fileType) {
        Map<String, String> headers = new HashMap<>();
        headers.put(AmbryHttpResponse.Headers.BLOB_SIZE, fileContent.length+"");
        headers.put(AmbryHttpResponse.Headers.SERVICE_ID, "ambry-client");
        headers.put(AmbryHttpResponse.Headers.AMBRY_CONTENT_TYPE, fileType);
        return headers;
    }
}
