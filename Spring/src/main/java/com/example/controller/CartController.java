package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.entity.Cart;
import com.example.entity.CartItem;
import com.example.repository.CartRepository;
import com.example.service.CartService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {
	@Autowired
    private CartService cartService;
	@Autowired
    private CartRepository cartRepository;

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam("menuId") Long menuId, @RequestParam("quantity") int quantity, HttpSession session) {

    	Long id = (Long) session.getAttribute("id");
    	if(id == null) {
    		return "redirect:/login";
    	}
    	cartService.addItemToCart(id, menuId, quantity);

        return "redirect:/"; // 장바구니 페이지로 리다이렉트 합니다.
    }
    @GetMapping("/cart")
    public String showCart(HttpSession session, Model model) {
        Long id = (Long) session.getAttribute("id");

        if (id != null) {
            // 주어진 ID로 Cart를 찾습니다
            Optional<Cart> optionalCart = cartRepository.findByMemberId(id);
            
            if (optionalCart.isPresent()) {
                Cart cart = optionalCart.get(); // Optional에서 Cart를 가져옵니다
                List<CartItem> cartItems = cart.getCartItems(); // Cart에서 CartItem을 가져옵니다
                model.addAttribute("cartItems", cartItems); // 모델에 CartItem 추가
                //model.addAttribute("totalPrice", cart.calculateTotalPrice()); // 총 가격 추가
            } else {
                model.addAttribute("message", "장바구니가 비어 있습니다.");
            }
        } else {
            model.addAttribute("message", "로그인이 필요합니다.");
        }

        return "cart";
    }
}