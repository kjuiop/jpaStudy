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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    @ResponseBody
    public Result memberV2() {
        List<Member> findMembers = memberService.findMembers();

        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(collect);
    }

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
    @AllArgsConstructor
    class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    class MemberDto {
        private String name;
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
