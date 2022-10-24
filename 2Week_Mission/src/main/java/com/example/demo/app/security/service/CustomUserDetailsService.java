package com.example.demo.app.security.service;

import com.example.demo.app.member.entity.Member;
import com.example.demo.app.member.repository.MemberRepository;

import com.example.demo.app.security.dto.MemberContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username).get();

        List<GrantedAuthority> authorities = new ArrayList<>();

        if (member.getUsername().equals("user1")) {
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        }
        if((member.getNickname() == null || member.getNickname().trim().isEmpty())!=true){
            authorities.add(new SimpleGrantedAuthority("WRITER"));
        }

        authorities.add(new SimpleGrantedAuthority("MEMBER"));

        return new MemberContext(member, authorities);
    }
}
