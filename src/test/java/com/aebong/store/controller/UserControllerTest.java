package com.aebong.store.controller;

import com.aebong.store.common.enums.user.Gender;
import com.aebong.store.common.enums.user.UserAccountType;
import com.aebong.store.common.enums.user.UserStatus;
import com.aebong.store.common.enums.user.UserType;
import com.aebong.store.controller.api.UserController;
import com.aebong.store.controller.req.UserRegisterRequest;
import com.aebong.store.service.user.UserService;
import com.aebong.store.service.user.dto.UserRegisterInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private UserService userService;

    @Test
    void 회원등록() throws Exception {

        // given
        UserRegisterInfo registerInfo = createUserRegisterInfo();
        UserRegisterRequest request = registerInfo.toRequest(registerInfo);

        // when & then
        mockMvc.perform(post("/api/v1/users/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("user-register",
                        requestFields(
                                fieldWithPath("userType").description("회원유형 (예: ADMIN, REGULAR_MEMBER, ASSOCIATE_MEMBER, NON_MEMBER)"),
                                fieldWithPath("userAccount").description("회원계정(이메일)"),
                                fieldWithPath("userAccountType").description("회원계정유형 (예: EMAIL, SOCIAL"),
                                fieldWithPath("userPassword").description("비밀번호"),
                                fieldWithPath("firstName").description("이름"),
                                fieldWithPath("lastName").description("성"),
                                fieldWithPath("birthDate").description("생년월일"),
                                fieldWithPath("gender").description("성별 (예: MALE, FEMALE, NON_BINARY, OTHER"),
                                fieldWithPath("mobileNumber").description("휴대폰번호"),
                                fieldWithPath("nickName").description("별칭(닉네임)"),
                                fieldWithPath("telNumber").description("전화번호"),
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("address.address1").description("주소(도로명주소|지번주소)"),
                                fieldWithPath("address.address2").description("상세주소").optional(),
                                fieldWithPath("address.city").description("구|군|동").optional(),
                                fieldWithPath("address.state").description("도|시").optional(),
                                fieldWithPath("address.zipcode").description("우편번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답코드"),
                                fieldWithPath("message").description("응답메시지"),
                                fieldWithPath("data").description("응답데이터")
                        )
                ));
    }

    private UserRegisterInfo createUserRegisterInfo() {
        return UserRegisterInfo.builder()
                .userType(UserType.REGULAR_MEMBER)
                .userAccount("aebong@gmail.com")
                .userAccountType(UserAccountType.EMAIL)
                .userPassword("nonencodepassword")
                .userStatus(UserStatus.ACTIVATED)
                .requiredPasswordChangeDatetime(LocalDateTime.now().plusDays(90))
                .firstName("애봉")
                .lastName("이")
                .gender(Gender.MALE)
                .email("aebong@gmail.com")
                .address1("대구광역시 동구 동대구로 503")
                .zipcode("42176")
                .build();
    }


}