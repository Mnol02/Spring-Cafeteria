package com.example.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final String SECRET_KEY_DEV = "DEV79215EAA49B471FB40C7659D3AA4C8CBF7F4F";

    public Map<String, String> readyPayment() {
        String urlString = "https://open-api.kakaopay.com/online/v1/payment/ready";
        HttpURLConnection httpURLConnection = null;
        Map<String, String> returnMap = new HashMap<>();

        try {
            URL url = new URL(urlString);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setRequestProperty("Authorization", "SECRET_KEY " + SECRET_KEY_DEV);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");

            // JSON Body
            String body = createPaymentRequestBody();
            httpURLConnection.setDoOutput(true);
            OutputStream bodyOutPutStream = httpURLConnection.getOutputStream();
            bodyOutPutStream.write(body.getBytes(StandardCharsets.UTF_8));
            bodyOutPutStream.close();

            // HTTP 응답 처리
            int responseCode = httpURLConnection.getResponseCode();
            InputStream in = (responseCode == HttpURLConnection.HTTP_OK) ?
                    httpURLConnection.getInputStream() : httpURLConnection.getErrorStream();

            String responseBody = readStream(in);
            System.out.println("Response: " + responseBody);

            // 추후 결제 요청시 필요한 주문 번호와 redirect URL 파싱
            String tid = extractValueFromJson(responseBody, "tid");
            String redirect_pc_url = extractValueFromJson(responseBody, "next_redirect_pc_url");

            returnMap.put("tid", tid);
            returnMap.put("redirect_pc_url", redirect_pc_url);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return returnMap;
    }

    public String approvePayment(String pg_token, String tid) {
        String urlString = "https://open-api.kakaopay.com/online/v1/payment/approve";
        HttpURLConnection httpURLConnection = null;
        String returnString = null;

        try {
            httpURLConnection = (HttpURLConnection) new URL(urlString).openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setRequestProperty("Authorization", "SECRET_KEY " + SECRET_KEY_DEV);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");

            String requestBody = createApprovalRequestBody(pg_token, tid);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(requestBody.getBytes(StandardCharsets.UTF_8));
            outputStream.close();

            int responseCode = httpURLConnection.getResponseCode();
            InputStream in = (responseCode == HttpURLConnection.HTTP_OK) ?
                    httpURLConnection.getInputStream() : httpURLConnection.getErrorStream();
            returnString = readStream(in);
            System.out.println("ResponseBody: " + returnString);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return returnString;
    }

    private String createPaymentRequestBody() {
        return "{\"cid\":\"TC0ONETIME\","
                + "\"partner_order_id\":\"1\","
                + "\"partner_user_id\":\"testUser\","
                + "\"item_name\":\"testItem\","
                + "\"quantity\":200,"
                + "\"total_amount\":200000,"
                + "\"tax_free_amount\":20000,"
                + "\"approval_url\":\"http://124.57.230.34:8080/approve\","
                + "\"cancel_url\":\"http://124.57.230.34:8080/\","
                + "\"fail_url\":\"http://124.57.230.34:8080/\"}";
    }

    private String createApprovalRequestBody(String pg_token, String tid) {
        return "{\"cid\":\"TC0ONETIME\","
                + "\"tid\":\"" + tid + "\","
                + "\"partner_order_id\":\"1\","
                + "\"partner_user_id\":\"testUser\","
                + "\"pg_token\":\"" + pg_token + "\"}";
    }

    private String readStream(InputStream in) throws IOException {
        StringBuilder responseBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
        }
        return responseBuilder.toString();
    }

    private String extractValueFromJson(String json, String key) {
        int startIndex = json.indexOf(key);
        if (startIndex == -1) return null;
        startIndex = json.indexOf(":", startIndex) + 2; // value 시작점
        int endIndex = json.indexOf("\"", startIndex);
        return json.substring(startIndex, endIndex);
    }
}