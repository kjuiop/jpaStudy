package com.gig.jpastudy.api;

import com.gig.jpastudy.model.Member;
import com.gig.jpastudy.service.MemberService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/v1/member")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long savedId = memberService.join(member);
        return new CreateMemberResponse(savedId);
    }

    @PostMapping("/api/v2/member")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {

        Member member = new Member();
        member.setName(request.getName());
        Long savedId = memberService.join(member);
        return new CreateMemberResponse(savedId);
    }

    @Data
    static class CreateMemberRequest {

        @NotEmpty
        private String name;
    }


    @Data
    static class CreateMemberResponse {
        private Long memberId;

        public CreateMemberResponse(Long memberId) {
            this.memberId = memberId;
        }
    }

}
