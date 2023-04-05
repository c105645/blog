package com.ness.userprofileservice.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Entity
@Table(name="userprofile")
@EntityListeners(AuditingEntityListener.class)
public class UserProfileEntity extends Base{
	

	private static final long serialVersionUID = 1L;
	
	@NotBlank(message="user name cannot be blank")
	@Size(message = "user name cannot be blank should be atleast 5 characters", min = 5)
	private String username;
	@Size(message = "First name cannot be blank and should be atleast 5 characters", min = 5)
	private String firstName;
	private String lastName;
	@JsonIgnore
	private String password; 
	@Email(message = "Enter a valid email")
	private String email;
	boolean isAdmin;
	private String biography; 
	private String imageUrl;
	@ManyToMany(cascade = {CascadeType.ALL })
	@JoinTable(name = "user_categories", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
	private List<Category> categories = new ArrayList<>();

	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "following")
    private List<UserProfileEntity> followers = new ArrayList<>();
    @JoinTable(name = "followers",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "follower_id")})
    @ManyToMany(cascade = CascadeType.ALL)
    private List<UserProfileEntity> following = new ArrayList<>();
    
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public String getBiography() {
		return biography;
	}
	public void setBiography(String biography) {
		this.biography = biography;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public List<UserProfileEntity> getFollowers() {
		return followers;
	}
	public void setFollowers(List<UserProfileEntity> followers) {
		this.followers = followers;
	}
	public List<UserProfileEntity> getFollowing() {
		return following;
	}
	public void setFollowing(List<UserProfileEntity> following) {
		this.following = following;
	}

	public UserProfileEntity addCategory(Category category) {
		categories.add(category);
		return this;
	}
	
	public UserProfileEntity removeCategory(Category category) {
		categories.remove(category);
		return this;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(username);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserProfileEntity other = (UserProfileEntity) obj;
		return Objects.equals(username, other.username);
	}
	
	
    public void addFollower(UserProfileEntity toFollow) {
        following.add(toFollow);
        toFollow.getFollowers().add(this);
    }
    public void removeFollower(UserProfileEntity toFollow) {
        following.remove(toFollow);
        toFollow.getFollowers().remove(this);
    }
	public List<Category> getCategories() {
		return categories;
	}
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	public void setisAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	@Override
	public String toString() {
		return "UserProfileEntity [username=" + username + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
				+ ", password=" + password + ", email=" + email + ", isAdmin=" + isAdmin + ", biography=" + biography
				+ ", imageUrl=" + imageUrl + ", categories=" + categories + ", followers=" + followers.stream().map(follower->follower.getId() + ", " + follower.getUsername()).collect(Collectors.toList()) + ", following="
				+ following.stream().map(follower->follower.getId() + ", " + follower.getUsername()).collect(Collectors.toList()) + "]";
	}
	
	
	

    
	
}
