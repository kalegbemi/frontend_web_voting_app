package com.capstone_project.web_voting_app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

    @AllArgsConstructor
    @Builder
    @Data
    public class AuthenticationResponse {
        private String token;

        public boolean isSuccess() {
            return true;
        }
}
