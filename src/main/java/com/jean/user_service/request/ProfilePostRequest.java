package com.jean.user_service.request;

import jakarta.validation.constraints.NotBlank;

public class ProfilePostRequest {

    @NotBlank(message = "The 'name' field cannot be empty or null")
    private String name;
    @NotBlank(message = "The 'description' field cannot be empty or null")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
