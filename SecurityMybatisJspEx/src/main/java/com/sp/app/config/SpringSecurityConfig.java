package com.sp.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

import com.sp.app.security.AjaxSessionTimeoutFilter;
import com.sp.app.security.LoginFailureHandler;
import com.sp.app.security.LoginSuccessHandler;

/*
- 시큐리티 자동 설정을 제거할 경우
  @SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
  public class Ss2Application {
      // ..
  }

 - WebSecurityConfigurerAdapter
   5.7에서 Deprecated
 
 - Spring Security 5.7.10
 - 참고
    antMatchers()는 5.8에서 Deprecated 되고 requestMatchers()로 변경 됨
    
 - Spring Security 6.x
   antMatchers()는 에러가 발생하고 requestMatchers()로 변경 됨

 - JDBC 연동은 UserDetailsService 구현클래스 작성

  - url?continue
    : 주소 뒤에 ?continue 붙는 경우가 발생
    : 스프링 시큐리티가 6.x로 업그레이드되면서 스프링부트 안정성을 추구하면서 발생되는 현상
  - 로그인 시 status : 999 -> AJAX 로그인 등
    : 로그인이 안된 상황에서 어떤 정적파일 또는 시큐리티에서 필터링 되지 못한 일부 자원이 요청에 포함되어 
      접근할 경우 해당 파일에 대한 인가 예외가 발생해서 /error 로 이동
    : 다시 로그인 페이지로 이동한 다음 정상적으로 로그인이 성공하면 
      로그인 이전의 경로가 캐시로 저장된 상태 
      즉 /error 경로로 다시 리다이렉트 되는 현상이 발생
    : /error 자체도 접근 권한을 체크하기 때문에 시큐리티가 필터링하지 못하도록 설정
      excludeUri 에 "/error" 가 등록된 주소가 아니므로 에러가 발생하는 경우
      500 에러가 발생함(Whitelabel Error Page)
  - 아이디 또는 패스워드 잘못으로 로그인이 실패하면 /error 를 캐시
*/

@Configuration
@EnableWebSecurity
// @ComponentScan(basePackages = { "com.sp.app" })
public class SpringSecurityConfig {
	// @Autowired
	// private DataSource dataSource;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	// configure HTTP security
    	
    	// ?continue 제거를 위해
    	HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
    	requestCache.setMatchingRequestParameterName(null);
    	
    	String[] excludeUri = {"/", "/index.jsp", "/member/login", "/member/member", "/member/logout", 
    			"/member/userIdCheck", "/member/complete", "/member/pwdFind", "/member/expired",
    			"/dist/**", "/uploads/**", "/favicon.ico", "/WEB-INF/views/**"};
    	
    	// Spring Security 6.1.0부터는 메서드 체이닝의 사용을 지양하고 
    	//     람다식을 통해 함수형으로 설정하게 지향하고 있다.
    	http.cors(Customizer.withDefaults())
    		.csrf(AbstractHttpConfigurer::disable)
    		.requestCache(request -> request.requestCache(requestCache))  // ?continue 제거
    		.headers(headers -> headers
    			.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
    	
    	/*
    	  ※ 설명
    	   - authorizeHttpRequests()
    	    : 스프링 시큐리티의 구성 메서드 내에서 사용되는 메서드로, HTTP 요청에 대한 인가 설정을 구성하는 데 사용
    	    : 다양한 인가 규칙을 정의할 수 있으며, 경로별로 다른 권한 설정이 가능하다.
    	    
    	   - requestMatchers()
    	     : authorizeHttpRequests()와 함께 사용되어 특정한 HTTP 요청 매처(Request Matcher)를 적용할 수 있게 해준다.
    	     : 요청의 종류는 HTTP 메서드(GET, POST 등)나 서블릿 경로를 기반으로 지정할 수 있다.
    	       requestMatchers(HttpMethod.GET,  "/public/**") 처럼 HTTP GET 요청 중 "/public/"으로 시작하는 URL에 대한 보안 설정  
    	*/    	
        http
        	.authorizeHttpRequests( authorize ->  authorize
        		.requestMatchers(excludeUri).permitAll()
        		.requestMatchers("/admin/**").hasRole("ADMIN")
    			.requestMatchers("/**").hasAnyRole("USER", "ADMIN")   // configurer에서 ROLE_ 붙여줌
                .anyRequest().authenticated() // 설정외 모든요청은 권한과 무관하고 로그인 유저만 사용
        	)
        	.formLogin(login -> login
                .loginPage("/member/login")
                .loginProcessingUrl("/member/login")
                .usernameParameter("userId")
                .passwordParameter("userPwd")
                .successHandler(loginSuccessHandler())
                .failureHandler(loginFailureHandler())
                .permitAll()
        	)
        	.logout(logout -> logout
        		.logoutUrl("/member/logout")
        		.invalidateHttpSession(true)
        		.deleteCookies("JSESSIONID")
        		.logoutSuccessUrl("/")
        	)
        	.addFilterAfter(ajaxSessionTimeoutFilter(), ExceptionTranslationFilter.class)
        	.sessionManagement(management -> management
        		.maximumSessions(1)
        		.expiredUrl("/member/expired"));

        // 인증 거부 관련 처리
        http.exceptionHandling((exceptionConfig)-> exceptionConfig.accessDeniedPage("/member/noAuthorized"));

        return http.build();
    }
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    LoginSuccessHandler loginSuccessHandler() {
    	LoginSuccessHandler handler = new LoginSuccessHandler();
    	handler.setDefaultUrl("/");
    	return handler;
    }

    @Bean
    LoginFailureHandler loginFailureHandler() {
    	LoginFailureHandler handler = new LoginFailureHandler();
    	handler.setDefaultFailureUrl("/member/login?login_error");
    	return handler;
    }

    @Bean
    AjaxSessionTimeoutFilter ajaxSessionTimeoutFilter() {
    	AjaxSessionTimeoutFilter filter = new AjaxSessionTimeoutFilter();
    	filter.setAjaxHeader("AJAX");
    	return filter;
    }
}
