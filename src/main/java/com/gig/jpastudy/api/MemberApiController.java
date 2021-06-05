package com.gig.jpastudy.api;

import com.gig.jpastudy.model.Member;
import com.gig.jpastudy.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public CreateMemberResponse saveMemberV2(
            @RequestBody @Valid CreateMemberRequest request) {

        Member member = new Member();
        member.setName(request.getName());
        Long savedId = memberService.join(member);
        return new CreateMemberResponse(savedId);
    }

    @PutMapping("/api/v2/member/{memberId}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("memberId") Long memberId,
            @RequestBody @Valid UpdatedMemberRequest request) {

        memberService.update(memberId, request.getName());
        Member findMember = memberService.findOne(memberId);
        return new UpdateMemberResponse(findMember.getMemberId(), findMember.getName());
    }

    @Data
    static class UpdatedMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long memberId;
        private String name;
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
