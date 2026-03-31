package com.social.application.DTOs;
import org.springframework.web.multipart.MultipartFile;

public class UserRequestDTO {


    private String username;
    private String email;

    private String bio;
    private long follower;
    private long following;
    private String password;

    // for image posting
    private MultipartFile profileImage;
    private MultipartFile coverImage;


    public UserRequestDTO(String username, String bio, long follower, long following, MultipartFile profileImage , String password , MultipartFile coverImage) {
        this.username = username;
        this.bio = bio;
        this.follower = follower;
        this.following = following;
        this.profileImage = profileImage;
        this.password = password;
        this.coverImage = coverImage;
    }

    public UserRequestDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public long getFollower() {
        return follower;
    }

    public void setFollower(long follower) {
        this.follower = follower;
    }

    public long getFollowing() {
        return following;
    }

    public void setFollowing(long following) {
        this.following = following;
    }

    public MultipartFile getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(MultipartFile profileImage) {
        this.profileImage = profileImage;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public MultipartFile getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(MultipartFile coverImage) {
        this.coverImage = coverImage;
    }
}
