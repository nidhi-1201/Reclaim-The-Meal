package com.ReclaimTheMeal;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;

public class AdditionalFormLoginConfigurer extends AbstractHttpConfigurer<AdditionalFormLoginConfigurer, HttpSecurity> {

	private String loginPageUrl;
    private String loginProcessingUrl;

    @Override
    public void init(HttpSecurity http) throws Exception {
        DefaultLoginPageGeneratingFilter loginPageGeneratingFilter = http.getSharedObject(DefaultLoginPageGeneratingFilter.class);
        if (loginPageGeneratingFilter == null) {
            return;
        }

        loginPageGeneratingFilter.setFormLoginEnabled(true);
        loginPageGeneratingFilter.setUsernameParameter("username");
        loginPageGeneratingFilter.setPasswordParameter("password");
        loginPageGeneratingFilter.setLoginPageUrl(loginPageUrl);
        loginPageGeneratingFilter.setLogoutSuccessUrl(loginPageUrl + "?logout");
        loginPageGeneratingFilter.setFailureUrl(loginPageUrl + "?error");
        loginPageGeneratingFilter.setAuthenticationUrl(loginProcessingUrl);
    }
    
    public AdditionalFormLoginConfigurer loginPage(String loginPageUrl) {
        this.loginPageUrl = loginPageUrl;
        return this;
    }
    public AdditionalFormLoginConfigurer loginProcessingUrl(String loginProcessingUrl) {
        this.loginProcessingUrl = loginProcessingUrl;
        return this;
    }
}

