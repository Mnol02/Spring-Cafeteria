package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Menu;
import com.example.repository.MenuRepository;

@Service
public class MenuService {
	
	@Autowired
    MenuRepository menuRepository;
	
	public Menu addMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    public Optional<Menu> getMenuById(Long id) {
        return menuRepository.findById(id);
    }

    public Menu updateMenu(Long id, Menu updatedMenu) {
        // 업데이트 로직 추가
        return menuRepository.save(updatedMenu);
    }

    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }
}
