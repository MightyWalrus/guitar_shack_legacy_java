package com.guitarshack;

import com.google.gson.Gson;
import com.guitarshack.domain.CurlRequest;
import com.guitarshack.domain.RequestParameter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CurlRequester {

    public Map<String, Object> read(CurlRequest curlRequest) {

        Map<String, String> params = curlRequest.getRequestParameters().stream()
                .collect(Collectors.toMap(RequestParameter::getName, RequestParameter::getValue));
        String paramString = "?";

        for (String key : params.keySet()) {
            paramString += key + "=" + params.get(key).toString() + "&";
        }
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(curlRequest.getBaseUri() + paramString))
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return new Gson().fromJson(response.body(), HashMap.class);
        } catch (Exception e) {
            throw new RuntimeException("Could not read %s".formatted(curlRequest));
        }
    }
}
