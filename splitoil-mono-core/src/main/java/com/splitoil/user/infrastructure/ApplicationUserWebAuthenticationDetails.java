package com.splitoil.user.infrastructure;

import lombok.Getter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

@Getter
public class ApplicationUserWebAuthenticationDetails extends WebAuthenticationDetails {

    private String currentUserName;

    public ApplicationUserWebAuthenticationDetails(final HttpServletRequest request, final String currentUserName) {
        super(request);
        this.currentUserName = currentUserName;
    }
}
