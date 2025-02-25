package me.hanjun.springbootblogtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class memberService {

    @Autowired
    MemberRepository memberRepository;


    public void test() {

        //생성
        memberRepository.save(new Member(1L, "a"));

        //조회

        Optional<Member> member = memberRepository.findById(1L); // 단건 조회
        List<Member> allMember = memberRepository.findAll();

        //삭제

        memberRepository.deleteById(1L);

    }
}
