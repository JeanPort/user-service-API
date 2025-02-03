package com.jean.user_service.response;

public class UserProfileGetResponse {

    public record User(Long id, String firstName){}
    public record Profile(Long id, String name){}

    private Long id;
    private User user;
    private Profile profile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
