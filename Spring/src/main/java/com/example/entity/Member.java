package com.example.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "member")
public class Member {
	
	@Id
	private Long id;
	
    private String nickname;
    private String profileImage;
    private String email;
    
    @OneToOne(mappedBy = "member")
    private Cart cart;

    @OneToMany(mappedBy = "member")
    private List<Order> orders;
    
    
    // 생성자
    public Member(Long id, String nickname, String profileImage, String email) {
        this.id = id;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.email = email;
    }
    protected Member() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
