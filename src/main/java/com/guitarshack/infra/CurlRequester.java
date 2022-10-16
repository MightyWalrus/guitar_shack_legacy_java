package com.guitarshack.infra;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CurlRequester {

    public <T> T mappedRead(CurlRequest curlRequest, Class<T> clazz) {
        HttpRequest request = createHttpRequestFrom(curlRequest);
        HttpClient httpClient = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return new Gson().fromJson(response.body(), clazz);
        } catch (Exception e) {
            throw new RuntimeException("Could not read %s".formatted(curlRequest));
        }
    }

    public Map<String, Object> read(CurlRequest curlRequest) {

        HttpRequest request = createHttpRequestFrom(curlRequest);
        HttpClient httpClient = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return new Gson().fromJson(response.body(), HashMap.class);
        } catch (Exception e) {
            throw new RuntimeException("Could not read %s".formatted(curlRequest));
        }
    }

    private static HttpRequest createHttpRequestFrom(CurlRequest curlRequest) {
        Map<String, String> params = curlRequest.getRequestParameters().stream()
                .collect(Collectors.toMap(RequestParameter::getName, RequestParameter::getValue));
        String paramString = "?";

        for (String key : params.keySet()) {
            paramString += key + "=" + params.get(key).toString() + "&";
        }
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(curlRequest.getBaseUri() + paramString))
                .build();
        return request;
    }
}
