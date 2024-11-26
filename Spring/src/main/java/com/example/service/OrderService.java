package com.example.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Cart;
import com.example.entity.CartItem;
import com.example.entity.Member;
import com.example.entity.Order;
import com.example.entity.OrderItem;
import com.example.repository.CartRepository;
import com.example.repository.MemberRepository;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class OrderService {
	@Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CartRepository cartRepository;
    
    private final String SECRET_KEY_DEV = "DEV79215EAA49B471FB40C7659D3AA4C8CBF7F4F";

    public Map<String, String> readyPayment(Member member) {
        String urlString = "https://open-api.kakaopay.com/online/v1/payment/ready";
        HttpURLConnection httpURLConnection = null;
        Map<String, String> returnMap = new HashMap<>();
        Order order = createOrder(member.getId());
        
        try {
            URL url = new URL(urlString);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setRequestProperty("Authorization", "SECRET_KEY " + SECRET_KEY_DEV);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            
            // JSON Body
            String body = createPaymentRequestBody(
            	    "TC0ONETIME", 
            	    order.getId(), 
            	    order.getMember().getNickname(), 
            	    "Test", 
            	    order.getTotal(), 
            	    20000, 
            	    "http://124.57.230.92:8080/approve", 
            	    "http://124.57.230.92:8080/", 
            	    "http://124.57.230.92:8080/"
            );
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

            // 추후 결제 요청시 필요한 주문 번호와  URL 파싱
            String tid = extractValueFromJson(responseBody, "tid");
            String _pc_url = extractValueFromJson(responseBody, "next_redirect_pc_url");
            String _app_url = extractValueFromJson(responseBody, "next_redirect_app_url");
            String _mobile_url = extractValueFromJson(responseBody, "next_redirect_mobile_url");

            returnMap.put("tid", tid);
            returnMap.put("_pc_url", _pc_url);
            returnMap.put("_app_url", _app_url);
            returnMap.put("_mobile_url", _mobile_url);
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

    private String createPaymentRequestBody(String cid, Long partnerOrderId, String partnerUserId, String itemName, int totalAmount, int taxFreeAmount, String approvalUrl, String cancelUrl, String failUrl) {
		return "{\"cid\":\"" + cid + "\","
				+ "\"partner_order_id\":\"" + partnerOrderId + "\","
				+ "\"partner_user_id\":\"" + partnerUserId + "\","
				+ "\"item_name\":\"" + itemName + "\","
				+ "\"quantity\":200,"
				+ "\"total_amount\":" + totalAmount + ","
				+ "\"tax_free_amount\":" + taxFreeAmount + ","
				+ "\"approval_url\":\"" + approvalUrl + "\","
				+ "\"cancel_url\":\"" + cancelUrl + "\","
				+ "\"fail_url\":\"" + failUrl + "\"}";
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
    
    // Order 생성
    public Order createOrder(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with ID: " + id));

        // 카트
        Cart cart = cartRepository.findByMemberId(id)
                .orElseThrow(() -> new RuntimeException("Cart not found for member ID: " + id));

        // OrderItem 리스트를 저장하기 위한 리스트
        List<OrderItem> orderItems = new ArrayList<>();

        // Order에 카트 아이템들을 추가
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setMenu(cartItem.getMenu());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItems.add(orderItem);
        }
        
        // Order 객체 생성
        Order order = new Order(member, orderItems);

        // Order와 OrderItem 연결
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(order); // Order 설정
        }

        // Order 저장
        orderRepository.save(order);
        
        // 최종적으로 OrderItem들을 저장
        for (OrderItem orderItem : orderItems) {
            orderItemRepository.save(orderItem); // OrderItem 저장
        }
        
        return order;
    }

	/*
	 * // 특정 멤버의 주문들 조회 public List<OrderItem> getOrdersByMember(Long Id) { return
	 * orderRepository.findByMemberId(Id); }
	 */
    
    // 지정된 주문 아이템 제거
    public void removeOrderItem(Long orderId, Long orderItemId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + orderId));

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new EntityNotFoundException("OrderItem not found with ID: " + orderItemId));

        if (!orderItem.getOrder().equals(order)) {
            throw new RuntimeException("This item does not belong to the specified order.");
        }
        orderItemRepository.delete(orderItem);
    }
    
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + id));
    }
    
    // 추가 기능 구현...
}