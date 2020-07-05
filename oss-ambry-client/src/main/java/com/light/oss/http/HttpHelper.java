package com.light.oss.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.light.oss.response.AmbryHttpResponse;

import java.io.IOException;
import java.util.Map;

public class HttpHelper {

    private CloseableHttpClient httpClient = HttpClients.createDefault();

    public AmbryHttpResponse get(String url) {
        return request(url, "GET");
    }

    public AmbryHttpResponse delete(String url) {
        return request(url, "DELETE");
    }

    public AmbryHttpResponse postFile(String url, Map<String, String> headers, byte[] content) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ByteArrayEntity entity = new ByteArrayEntity(content);

        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);

        // set headers
        setHeaders(httpPost, headers);

        AmbryHttpResponse ambryResponse = new AmbryHttpResponse();

        CloseableHttpResponse response;
        try {
            response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            ambryResponse.setCode(response.getStatusLine().getStatusCode());
            ambryResponse.setStatus(response.getStatusLine().getReasonPhrase());
            ambryResponse.setContent(responseEntity.getContent());
            ambryResponse.setHeaders(response.getAllHeaders());
            ambryResponse.setMessage("Success");
        } catch (IOException e) {
            e.printStackTrace();
            ambryResponse.setCode(404);
            ambryResponse.setMessage("Request error.");
        }
        return ambryResponse;
    }

    private AmbryHttpResponse request(String url, String methodType) {
        AmbryHttpResponse ambryResponse = new AmbryHttpResponse();

        HttpRequestBase httpRequest;
        switch (methodType) {
            case "HEAD":
                httpRequest = new HttpHead(url);
                break;
            case "GET":
                httpRequest = new HttpGet(url);
                break;
            case "DELETE":
                httpRequest = new HttpDelete(url);
                break;
            default:
                ambryResponse.setCode(500);
                ambryResponse.setMessage("Http request method error.");
                return ambryResponse;
        }

        CloseableHttpResponse response;
        try {
            // not close the stream
            response = httpClient.execute(httpRequest);
            HttpEntity entity = response.getEntity();

            ambryResponse.setCode(response.getStatusLine().getStatusCode());
            ambryResponse.setStatus(response.getStatusLine().getReasonPhrase());
            ambryResponse.setContent(entity.getContent());
            ambryResponse.setHeaders(response.getAllHeaders());
            ambryResponse.setMessage("Success");
            return ambryResponse;
        } catch (IOException e) {
            e.printStackTrace();
            ambryResponse.setCode(404);
            ambryResponse.setMessage("Request error.");
        }
        return ambryResponse;
    }

    private void setHeaders(HttpMessage httpMessage, Map<String, String> headers) {
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpMessage.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }
}
