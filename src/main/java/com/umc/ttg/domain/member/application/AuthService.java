package com.umc.ttg.domain.member.application;

import com.umc.ttg.domain.member.entity.Member;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface AuthService {
    List<Member> findAll();
    Member findMemberByName(String name);
    String retrieveName(HttpServletRequest request);
    String permitAllAccess(HttpServletRequest request);
    Long permitMemberOnly(HttpServletRequest request);

}
