package com.jeswin8801.byteBlog.security.oauth2.dao;

import com.jeswin8801.byteBlog.util.WebUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static com.jeswin8801.byteBlog.security.oauth2.enums.OauthCookieNames.REDIRECT_URI_PARAM_COOKIE_NAME;

/**
 * <ol>
 *      <li>
 *          Flow comes here <b><i>"onAuthenticationFailure()"</i></b>, If OAuth2 Authentication Fails in <b><i>OAuth2UserService.class</i></b>
 *      </li>
 *      <ul>
 *          <li>
 *             We send authentication error response to the <b><i>redirect_uri</i></b>
 *          </li>
 *          <li>
 *             Since its failure response, (we aren't sending any tokens or data ) so, we don't need to validate the <b><i>redirect_uri</i></b> for security measures
 *          </li>
 *      </ul>
 * <p>
 *      <li>
 *          By default, OAuth2 uses Session based <b><i>AuthorizationRequestRepository</i></b>, since we are using Cookie based <b><i>AuthorizationRequestRepository</i></b>
 *      </li>
 *      <ul>
 *          <li>
 *              We clear <b><i>"authorizationRequest"</i></b> stored in our cookie, before sending redirect response
 *          </li>
 *      </ul>
 * </ol>
 */
@Slf4j
@Service
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        String targetUrl = WebUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME.getCookieName())
                .map(Cookie::getValue)
                .orElse(("/"));

        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam(
                        "error",
                        exception.getLocalizedMessage().replaceAll(
                        "[\\[\\]]", "")
                )
                .build().toUriString();

        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequest(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
