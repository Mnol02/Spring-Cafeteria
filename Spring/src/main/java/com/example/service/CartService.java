package com.example.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Cart;
import com.example.entity.CartItem;
import com.example.entity.Member;
import com.example.entity.Menu;
import com.example.repository.CartItemRepository;
import com.example.repository.CartRepository;
import com.example.repository.MemberRepository;
import com.example.repository.MenuRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MemberRepository memberRepository;

    public Cart createCart(Long id) {
    	
    	// 멤버를 가져옵니다.
    	Member member = memberRepository.findById(id)
    	        .orElseThrow(() -> new EntityNotFoundException("Member not found with ID: " + id));

        Cart cart = new Cart();
        cart.setMember(member);
        
        // 카트를 저장하고 반환합니다.
        return cartRepository.save(cart);
    }

    public void addItemToCart(Long id, Long menuId, int quantity) {
        Cart cart = cartRepository.findByMemberId(id).orElseGet(() -> {
            // 카트가 없는 경우 새로운 카트를 생성합니다.
            return createCart(id);
        });
                
        // 메뉴 조회 (존재 여부 체크)
        Menu menu = menuRepository.findById(menuId)
                                   .orElseThrow(() -> new RuntimeException("Menu not found"));
        // CartItem 생성 및 추가
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setMenu(menu);
        cartItem.setQuantity(quantity);

        cart.addCartItem(cartItem);
        cartItemRepository.save(cartItem);
    }
    public void removeItemFromCart(Long cartId, Long cartItemId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart != null) {
            cartItemRepository.deleteById(cartItemId);
        }
    }

    public Cart getCart(Long id) {
        return cartRepository.findByMemberId(id).orElse(null);
    }

    public List<CartItem> getCartItems(Long id) {
        Cart cart = cartRepository.findByMemberId(id).orElseThrow(() -> 
            new RuntimeException("Cart not found for member ID: " + id));
        
        return new ArrayList<>(cart.getCartItems());
    }

    // 추가 기능 구현
}