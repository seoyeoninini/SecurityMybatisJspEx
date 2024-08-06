package com.sp.app.security;

import java.util.List;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sp.app.domain.Member;
import com.sp.app.service.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{
	private final MemberService memberService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member member = memberService.findById(username);
		if(member == null) {
			throw new UsernameNotFoundException("아이디가 존재하지 않습니다.");
		}
		
		List<String> authorities = memberService.listAuthority(username);
		
		return toUserDetails(member, authorities);
	}

    private UserDetails toUserDetails(Member member, List<String> authorities) {
        String[] roles = authorities.toArray(new String[authorities.size()]);
        
        return User.builder()
                .username(member.getUserId())
                .password(member.getUserPwd())
                .roles(roles) // ROLE_ 로 시작할수 없음
                .build();
    }	
}
