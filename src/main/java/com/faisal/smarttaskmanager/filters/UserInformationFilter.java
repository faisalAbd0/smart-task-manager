package com.faisal.smarttaskmanager.filters;

import com.jayway.jsonpath.JsonPath;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Component
public class UserInformationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String authorizationHeader = request.getHeader("Authorization");
        if (StringUtils.isNotBlank(authorizationHeader)) {
            fillUserInformation(authorizationHeader);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void fillUserInformation(String authorizationHeader) {
        String tokenBody = authorizationHeader.substring(7).split("\\.")[1];
        String bodyJson = new String(Base64.getDecoder().decode(tokenBody), StandardCharsets.UTF_8);
        String fullName = JsonPath.parse(bodyJson).read("$.name");
        String email = JsonPath.parse(bodyJson).read("$.email");
        List<String> roles = JsonPath.parse(bodyJson).read("$.realm_access.roles");

        List<GrantedAuthority> grantedAuthorities = roles.stream().map(role -> (GrantedAuthority) () -> role).toList();

        MyUser user = new MyUser(fullName, "", grantedAuthorities);
        user.setEmail(email);

        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }

}
