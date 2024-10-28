package com.example.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class OrderService {
	/*
	 * @Autowired private OrderRepository orderRepository;
	 * 
	 * @Autowired private OrderItemRepository orderItemRepository;
	 * 
	 * @Autowired private CartRepository cartRepository;
	 */
	// 개발용 시크릿키(추후 시크릿 키는 별도 관리)
    private final String SECRET_KEY_DEV = "DEV79215EAA49B471FB40C7659D3AA4C8CBF7F4F";
	/**
     * 카카오 결제 준비 메소드
     */
    public Map<String,String> readyPayment(){
        // 요청할 url준비
        String urlString = "https://open-api.kakaopay.com/online/v1/payment/ready";
        // http 요청객체 초기화
        HttpURLConnection httpURLConnection = null;
        // return객체 초기화
        Map<String, String> returnMap = new HashMap<>();
        String tid = null;
        String redirect_pc_url = null;

        try {
            //java.net 패키지의 url클래스 호출
            URL url = new URL(urlString);
            // Casting처리
            httpURLConnection = (HttpURLConnection) url.openConnection();
            // 메소드 SET
            httpURLConnection.setRequestMethod("POST");
            // 연결 TIME OUT(5초)
            httpURLConnection.setConnectTimeout(5000);
            // 읽기 TIME OUT(5초)
            httpURLConnection.setReadTimeout(5000);

            // header값 set
            httpURLConnection.setRequestProperty("Host","open-api.kakaopay.com");
            // TEST SECRET KEY
            httpURLConnection.setRequestProperty("Authorization","SECRET_KEY DEV79215EAA49B471FB40C7659D3AA4C8CBF7F4F");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");

            // post요청의 바디에 담아보낼 데이터jsonBody
            // TEST : Body(아래는 필수값들이며 추가적인 파라미터는 사이트 참고)
            String body = "{\"cid\" : \"TC0ONETIME\"," // TEST 가맹 ID
                    + "\"partner_order_id\":\"1\"," // 가맹 주문 ID
                    + "\"partner_user_id\":\"testUser\"," // 가맹 고객 회원 ID
                    + "\"item_name\":\"testItem\"," // 상품명
                    + "\"quantity\":200," // 상품량
                    + "\"total_amount\":200000," // 총 결제금액
                    + "\"tax_free_amount\":20000," // 공제금액
                    + "\"approval_url\":\"http://localhost:8080/api/postReadyKakaoPayment\"," // 결제 성공 redirect_url -> 결제 승인 api 호출
                    + "\"cancel_url\":\"http://localhost:8080/\"," // 결제 취소 redirect_url
                    + "\"fail_url\":\"http://localhost:8080/\"" // 결제 실패 redirect_url
                    + "}";
            // Post요청을 보내기 위한 출력스트림 사용
            httpURLConnection.setDoOutput(true);
            // body를 담을 outputStream
            OutputStream bodyOutPutStream = httpURLConnection.getOutputStream();
            // body를 UTF-8로 인코딩 된 바이너리를 출력한다
            bodyOutPutStream.write(body.getBytes("UTF-8"));
            // 아웃스트림 닫기
            bodyOutPutStream.close();

            // 상태코드(실행시점)
            int responseCode = httpURLConnection.getResponseCode();
            // 응답라인
            String inputLine;
            // 스트림
            InputStream in= null;
            if (responseCode == HttpURLConnection.HTTP_OK){
                // 정상적인 상태시 스트림
                in = httpURLConnection.getInputStream();
            }else{
                // 에러발생시 스트림
                in = httpURLConnection.getErrorStream();
            }

            System.out.println("Response : " + responseCode);
            // 응답을 읽기 위한 BufferedReader 생성
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            // 응답 내용
            StringBuilder sb = new StringBuilder();
            // 응답 내용 읽기
            while((inputLine = reader.readLine()) != null){
                sb.append(inputLine);
            }
            // 스트림 닫기
            reader.close();
            // 응답 내용 출력
            System.out.println("Response : " + sb.toString());

            // 추후 결제 요청시 필요한 주문 번호
            tid = getVersion(sb.toString(),"tid");
            // redirect될 url
            redirect_pc_url = getVersion(sb.toString(), "next_redirect_pc_url");
            // return객체에 SET
            returnMap.put("tid", tid);
            returnMap.put("redirect_pc_url", redirect_pc_url);

        } catch (IOException e) {
            // 스택추적 출력
            e.printStackTrace();
        } finally {
            if(httpURLConnection != null){
                // http 요청 객체 종료
                httpURLConnection.disconnect();
            }
        }
        return returnMap;
    }

    /**
     * 카카오페이 승인요청 메소드
     * @param pg_token : 사용자 최종단계(비밀번호 입력) 후 받는 토큰
     * @param tid : 결제 준비 API의 응답값으로 나온 결제 번호
     * @return
     */
    public String approvePayment(String pg_token, String tid){
        // 승인 요청 url
        String urlString = "https://open-api.kakaopay.com/online/v1/payment/approve";

        // http 연결용 객체 초기화
        HttpURLConnection httpURLConnection = null;
        // return 값
        String returnString = null;
        try {
            // URL객체 생성
            httpURLConnection = (HttpURLConnection) new URL(urlString).openConnection();

            // http요청 메소드
            httpURLConnection.setRequestMethod("POST");

            // 요청 제한 시간 5초
            httpURLConnection.setConnectTimeout(5000);

            // 읽는 제한 시간 5초
            httpURLConnection.setReadTimeout(5000);

            // 요청에 필요한 헤더값 SET(카카오 페이 API 문서 참고)
            httpURLConnection.setRequestProperty("Host","open-api.kakaopay.com");
            httpURLConnection.setRequestProperty("Authorization","SECRET_KEY " + SECRET_KEY_DEV); // 공통으로 사용해야할 인가 헤더
            httpURLConnection.setRequestProperty("SECRET_KEY","SECRET_KEY " + SECRET_KEY_DEV);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");

            // POST요청을 위한 아웃풋 가능 여부 설정
            httpURLConnection.setDoOutput(true);

            // 요청에 쓰일 값 //TODO 후에 파라미터 로 받을 수 있게
            String requestBody =  "{\"cid\" : \"TC0ONETIME\"," // TEST 가맹 ID
                    + "\"tid\":\""+ tid +"\","
                    + "\"partner_order_id\":\"1\"," // 가맹 주문 ID(결제 준비 API에서 사용했던 주문ID와같아야함)
                    + "\"partner_user_id\":\"testUser\"," // 가맹 고객 회원 ID(결제 준비 API에서 사용했던 회원ID와 같아야함)
                    + "\"pg_token\":\"" + pg_token +"\"" // 결제 승인 요청 인증 토큰(사용자가 결제 완료시 approval_url로 redirect시 queryString으로 전달
                    + "}";

            // RequestBody 출력
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(requestBody.getBytes("UTF-8"));
            // 닫기
            outputStream.close();

            // 요청시점
            int responseCode = httpURLConnection.getResponseCode();
            System.out.println("ResponseCode : " + responseCode);

            // 입력받을 라인
            String inputLine = null;

            BufferedReader bufferedReader = null;
            // 상태코드가 200이 아닐경우 에러스트림
            if(responseCode == HttpURLConnection.HTTP_OK){
                bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            }else{
                bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
            }


            // 응답 바디
            StringBuilder sb = new StringBuilder();
            while((inputLine = bufferedReader.readLine()) != null){
                sb.append(inputLine);
            }
            // Body출력
            System.out.println("ResponseBody : " + sb.toString());
            returnString = sb.toString();
            // 스트림닫기
            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            // http요청객체 종료
            httpURLConnection.disconnect();
        }
        return returnString;
    }

    // 간단한 json에서 파라미터 key에 대한 value를 찾기 위함
    private String getVersion(String json, String key){
        int keyIndex = json.indexOf(key);
        if(keyIndex == -1){
            return null;
        }

        // keyIndex로부터 : 찾기
        int startIndex = json.indexOf(":", keyIndex)+2; // value시작점
        // value이후 있을 쌍따움표 찾기
        int endIndex = json.indexOf("\"",startIndex);

        // substring은 endIndex-1 까지 보여주지만 위에서 찾은 endIndex는 쌍따옴표 위치이므로 그냥 실행
        return json.substring(startIndex,endIndex);

    }
    
}