package com.aebong.store.controller;

import com.aebong.store.common.enums.CustomErrorType;
import com.aebong.store.common.enums.user.Gender;
import com.aebong.store.common.enums.user.UserAccountType;
import com.aebong.store.common.enums.user.UserStatus;
import com.aebong.store.common.enums.user.UserType;
import com.aebong.store.common.exceptions.UserApplicationException;
import com.aebong.store.controller.api.UserController;
import com.aebong.store.controller.req.UserLoginRequest;
import com.aebong.store.controller.req.UserModifyRequest;
import com.aebong.store.controller.req.UserRegisterRequest;
import com.aebong.store.controller.res.UserGetResponse;
import com.aebong.store.domain.entity.Address;
import com.aebong.store.service.user.UserService;
import com.aebong.store.service.user.dto.UserGetInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private UserService userService;

    @Test
    void 사용자등록_실패() throws Exception {

        // given
        UserRegisterRequest registerRequest = createUserRegisterInfo();

        doThrow(new UserApplicationException(CustomErrorType.IS_EXIST_USER, CustomErrorType.IS_EXIST_USER.getMessageKr()))
                .when(userService).registerUser(any(UserRegisterRequest.class));

        // when & then
        mockMvc.perform(post("/api/v1/users/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerRequest)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("IS_EXIST_USER"))
                .andExpect(jsonPath("$.message").value("존재하는 사용자입니다."))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andDo(document("user-register-fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userAccount").type(JsonFieldType.STRING).description("사용자계정(이메일)"),
                                fieldWithPath("userPassword").type(JsonFieldType.STRING).description("비밀번호"),
                                fieldWithPath("lastName").type(JsonFieldType.STRING).description("성"),
                                fieldWithPath("firstName").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("birthDate").type(JsonFieldType.STRING).description("생년월일"),
                                fieldWithPath("gender").type(JsonFieldType.STRING).description("성별 (예: MALE, FEMALE, NON_BINARY, OTHER"),
                                fieldWithPath("mobileNumber").type(JsonFieldType.STRING).description("휴대폰번호"),
                                fieldWithPath("nickName").type(JsonFieldType.STRING).description("별칭(닉네임)"),
                                fieldWithPath("telNumber").type(JsonFieldType.STRING).description("전화번호").optional(),
                                fieldWithPath("address1").type(JsonFieldType.STRING).description("주소(도로명주소|지번주소)"),
                                fieldWithPath("address2").type(JsonFieldType.STRING).description("상세주소").optional(),
                                fieldWithPath("zipcode").type(JsonFieldType.STRING).description("우편번호")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메시지"),
                                subsectionWithPath("data").type(JsonFieldType.OBJECT).description("error 발생시 null").optional()
                        ),
                        requestBody(),
                        responseBody()
                ));
    }

    @Test
    void 사용자등록_성공() throws Exception {

        // given
        UserRegisterRequest registerRequest = createUserRegisterInfo();

        doNothing().when(userService).registerUser(any(UserRegisterRequest.class));

        // when & then
        mockMvc.perform(post("/api/v1/users/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-register",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userAccount").type(JsonFieldType.STRING).description("사용자계정(이메일)"),
                                fieldWithPath("userPassword").type(JsonFieldType.STRING).description("비밀번호"),
                                fieldWithPath("lastName").type(JsonFieldType.STRING).description("성"),
                                fieldWithPath("firstName").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("birthDate").type(JsonFieldType.STRING).description("생년월일"),
                                fieldWithPath("gender").type(JsonFieldType.STRING).description("성별 (예: MALE, FEMALE, NON_BINARY, OTHER"),
                                fieldWithPath("mobileNumber").type(JsonFieldType.STRING).description("휴대폰번호"),
                                fieldWithPath("nickName").type(JsonFieldType.STRING).description("별칭(닉네임)"),
                                fieldWithPath("telNumber").type(JsonFieldType.STRING).description("전화번호").optional(),
                                fieldWithPath("address1").type(JsonFieldType.STRING).description("주소(도로명주소|지번주소)"),
                                fieldWithPath("address2").type(JsonFieldType.STRING).description("상세주소").optional(),
                                fieldWithPath("zipcode").type(JsonFieldType.STRING).description("우편번호")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.STRING).description("응답데이터").optional()
                        ),
                        requestBody(),
                        responseBody()
                ));
    }

    @Test
    void 로그인_실패() throws Exception {

        // given
        UserLoginRequest userLoginInfo = createUserLoginInfo();

        doThrow(new UserApplicationException(CustomErrorType.NOT_FOUND_USER, CustomErrorType.NOT_FOUND_USER.getMessageKr()))
                .when(userService).loginUser(any(UserLoginRequest.class));

        // when & then
        mockMvc.perform(post("/api/v1/users/sign-in")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userLoginInfo)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("NOT_FOUND_USER"))
                .andExpect(jsonPath("$.message").value("사용자를 찾을 수 없습니다."))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andDo(document("user-login-fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userAccount").type(JsonFieldType.STRING).description("사용자계정(이메일)"),
                                fieldWithPath("userPassword").type(JsonFieldType.STRING).description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답메시지").optional(),
                                subsectionWithPath("data").type(JsonFieldType.OBJECT).description("사용자 데이터 객체").optional()
                        ),
                        requestBody(),
                        responseBody()
                ));


    }

    @Test
    void 로그인_성공() throws Exception {

        // given
        UserLoginRequest userLoginInfo = createUserLoginInfo();

        UserGetInfo userGetInfo = createUserGetInfo();
        UserGetResponse response = UserGetResponse.to(userGetInfo);

        given(userService.loginUser(userLoginInfo)).willReturn(userGetInfo);

        // when & then
        mockMvc.perform(post("/api/v1/users/sign-in")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userLoginInfo)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userAccount").type(JsonFieldType.STRING).description("사용자계정(이메일)"),
                                fieldWithPath("userPassword").type(JsonFieldType.STRING).description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답메시지").optional(),
                                subsectionWithPath("data").type(JsonFieldType.OBJECT).description("사용자 데이터 객체"),
                                fieldWithPath("data.userId").type(JsonFieldType.NUMBER).description("사용자고유번호"),
                                fieldWithPath("data.userDetailId").type(JsonFieldType.NUMBER).description("사용자상세고유번호"),
                                fieldWithPath("data.userAccount").type(JsonFieldType.STRING).description("사용자계정"),
                                fieldWithPath("data.userPassword").type(JsonFieldType.STRING).description("비밀번호"),
                                fieldWithPath("data.lastName").type(JsonFieldType.STRING).description("성"),
                                fieldWithPath("data.firstName").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("data.birthDate").type(JsonFieldType.STRING).description("생년월일 (yyyy-MM-dd)"),
                                fieldWithPath("data.gender").type(JsonFieldType.STRING).description("성별 (예: MALE, FEMALE, NON_BINARY, OTHER)"),
                                fieldWithPath("data.mobileNumber").type(JsonFieldType.STRING).description("휴대폰번호"),
                                fieldWithPath("data.nickName").type(JsonFieldType.STRING).description("별칭(닉네임)"),
                                fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("data.address1").type(JsonFieldType.STRING).description("주소(도로명주소|지번주소)"),
                                fieldWithPath("data.address2").type(JsonFieldType.STRING).description("상세주소").optional(),
                                fieldWithPath("data.zipcode").type(JsonFieldType.STRING).description("우편번호")
                        ),
                        requestBody(),
                        responseBody()
                ));


    }

    @Test
    void 사용자수정_실패() throws Exception {

        // given
        String requestUserAccount = "test@gmail.com";
        UserModifyRequest modifyRequest = createUserModifyInfo();

        doThrow(new UserApplicationException(CustomErrorType.NOT_FOUND_USER, CustomErrorType.NOT_FOUND_USER.getMessageKr()))
                .when(userService).modifyUser(any(String.class), any(UserModifyRequest.class));

        // when & then
        mockMvc.perform(patch("/api/v1/users/{userAccount}", requestUserAccount)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(modifyRequest)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("NOT_FOUND_USER"))
                .andExpect(jsonPath("$.message").value("사용자를 찾을 수 없습니다."))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andDo(document("user-modify-fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userAccount").description("조회할 사용자계정(이메일)")
                        ),
                        requestFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("사용자 고유번호"),
                                fieldWithPath("userDetailId").type(JsonFieldType.NUMBER).description("사용자상세 고유번호"),
                                fieldWithPath("userAccount").type(JsonFieldType.STRING).description("사용자계정(이메일)"),
                                fieldWithPath("userPassword").type(JsonFieldType.STRING).description("비밀번호"),
                                fieldWithPath("lastName").type(JsonFieldType.STRING).description("성"),
                                fieldWithPath("firstName").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("birthDate").type(JsonFieldType.STRING).description("생년월일"),
                                fieldWithPath("gender").type(JsonFieldType.STRING).description("성별 (예: MALE, FEMALE, NON_BINARY, OTHER"),
                                fieldWithPath("mobileNumber").type(JsonFieldType.STRING).description("휴대폰번호"),
                                fieldWithPath("nickName").type(JsonFieldType.STRING).description("별칭(닉네임)"),
                                fieldWithPath("telNumber").type(JsonFieldType.STRING).description("전화번호").optional(),
                                fieldWithPath("address1").type(JsonFieldType.STRING).description("주소(도로명주소|지번주소)"),
                                fieldWithPath("address2").type(JsonFieldType.STRING).description("상세주소").optional(),
                                fieldWithPath("zipcode").type(JsonFieldType.STRING).description("우편번호")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답메시지").optional(),
                                subsectionWithPath("data").type(JsonFieldType.OBJECT).description("사용자 데이터 객체").optional()
                        ),
                        requestBody(),
                        responseBody()
                ));


    }

    @Test
    void 사용자수정_성공() throws Exception {

        // given
        String requestUserAccount = "test@gmail.com";
        UserModifyRequest modifyRequest = createUserModifyInfo();

        doNothing().when(userService).modifyUser(any(String.class), any(UserModifyRequest.class));

        // when & then
        mockMvc.perform(patch("/api/v1/users/{userAccount}", requestUserAccount)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(modifyRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-modify",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userAccount").description("조회할 사용자계정(이메일)")
                        ),
                        requestFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("사용자 고유번호"),
                                fieldWithPath("userDetailId").type(JsonFieldType.NUMBER).description("사용자상세 고유번호"),
                                fieldWithPath("userAccount").type(JsonFieldType.STRING).description("사용자계정(이메일)"),
                                fieldWithPath("userPassword").type(JsonFieldType.STRING).description("비밀번호"),
                                fieldWithPath("lastName").type(JsonFieldType.STRING).description("성"),
                                fieldWithPath("firstName").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("birthDate").type(JsonFieldType.STRING).description("생년월일"),
                                fieldWithPath("gender").type(JsonFieldType.STRING).description("성별 (예: MALE, FEMALE, NON_BINARY, OTHER"),
                                fieldWithPath("mobileNumber").type(JsonFieldType.STRING).description("휴대폰번호"),
                                fieldWithPath("nickName").type(JsonFieldType.STRING).description("별칭(닉네임)"),
                                fieldWithPath("telNumber").type(JsonFieldType.STRING).description("전화번호").optional(),
                                fieldWithPath("address1").type(JsonFieldType.STRING).description("주소(도로명주소|지번주소)"),
                                fieldWithPath("address2").type(JsonFieldType.STRING).description("상세주소").optional(),
                                fieldWithPath("zipcode").type(JsonFieldType.STRING).description("우편번호")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답메시지").optional(),
                                subsectionWithPath("data").type(JsonFieldType.OBJECT).description("사용자 데이터 객체").optional()
                        ),
                        requestBody(),
                        responseBody()
                ));


    }

    @Test
    void 사용자삭제_실패() throws Exception {

        // given
        String requestUserAccount = "test@gmail.com";

        doThrow(new UserApplicationException(CustomErrorType.NOT_FOUND_USER, CustomErrorType.NOT_FOUND_USER.getMessageKr()))
                .when(userService).deleteUser(any(String.class));

        // when & then
        mockMvc.perform(delete("/api/v1/users/{userAccount}", requestUserAccount)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("NOT_FOUND_USER"))
                .andExpect(jsonPath("$.message").value("사용자를 찾을 수 없습니다."))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andDo(document("user-delete-fail",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("userAccount").description("조회할 사용자계정(이메일)")
                                ),
                                responseFields(
                                        fieldWithPath("code").type(JsonFieldType.STRING).description("응답코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답메시지").optional(),
                                        subsectionWithPath("data").type(JsonFieldType.OBJECT).description("사용자 데이터 객체").optional()
                                ),
                                requestBody(),
                                responseBody()
                ));


    }

    @Test
    void 사용자삭제_성공() throws Exception {

        // given
        String requestUserAccount = "test@gmail.com";

        doNothing().when(userService).deleteUser(any(String.class));

        // when & then
        mockMvc.perform(delete("/api/v1/users/{userAccount}", requestUserAccount)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-delete",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("userAccount").description("조회할 사용자계정(이메일)")
                                ),
                                responseFields(
                                        fieldWithPath("code").type(JsonFieldType.STRING).description("응답코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답메시지").optional(),
                                        subsectionWithPath("data").type(JsonFieldType.OBJECT).description("사용자 데이터 객체").optional()
                                ),
                                requestBody(),
                                responseBody()
                ));


    }

    private UserRegisterRequest createUserRegisterInfo() {
        return UserRegisterRequest.builder()
//                .userType(UserType.REGULAR_MEMBER)
                .userAccount("aebong@gmail.com")
//                .userAccountType(UserAccountType.EMAIL)
                .userPassword("nonencodepassword")
//                .userStatus(UserStatus.ACTIVATED)
//                .requiredPasswordChangeDatetime(LocalDateTime.now().plusDays(90))
                .lastName("ae")
                .firstName("bong")
                .birthDate(LocalDate.of(1990, 1,1))
                .gender(Gender.MALE)
                .mobileNumber("01011111234")
                .nickName("aebong")
//                .email("aebong@gmail.com")
                .address1("경기도 성남시 분당구 판교역로 166 (백현동)")
                .address2("카카오 판교 아지트")
                .zipcode("13529")
                .build();
    }

    private UserGetInfo createUserGetInfo() {
        return UserGetInfo.builder()
                .userId(0L)
                .userDetailId(0L)
                .userType(UserType.REGULAR_MEMBER)
                .userAccount("aebong@gmail.com")
                .userAccountType(UserAccountType.EMAIL)
                .userPassword("nonencodepassword")
                .userStatus(UserStatus.ACTIVATED)
                .passwordInitYn(Boolean.FALSE)
                .failPasswordCount(0)
                .accountLockedDatetime(null)
                .lastLoginDatetime(null)
                .lastPasswordChangeDatetime(LocalDateTime.now())
                .requiredPasswordChangeDatetime(LocalDateTime.now().plusMonths(6))
                .loginAvailableDate(null)
                .lastName("ae")
                .firstName("bong")
                .birthDate(LocalDate.of(1990, 1,1))
                .gender(Gender.MALE)
                .mobileNumber("01011111234")
                .nickName("aebong")
                .email("aebong@gmail.com")
                .address(Address.builder()
                                 .address1("경기도 성남시 분당구 판교역로 166 (백현동)")
                                 .address2("카카오 판교 아지트")
                                 .zipcode("13529")
                                 .build())
                .joinDatetime(LocalDateTime.now())
                .activatedDatetime(LocalDateTime.now())
                .inactivatedDatetime(null)
                .withdrawalDatetime(null)
                .dormantDatetime(null)
                .build();
    }

    private UserModifyRequest createUserModifyInfo() {
        return UserModifyRequest.builder()
                .userId(0L)
                .userDetailId(0L)
                .userAccount("aebong@gmail.com")
                .userPassword("nonencodepassword")
                .lastName("ae")
                .firstName("bong")
                .birthDate(LocalDate.of(1990, 1, 1))
                .gender(Gender.MALE)
                .mobileNumber("01022221234")
                .nickName("aebong1")
                .address1("테스트시 테스트구 테스트로 2")
                .address2("테스트")
                .zipcode("00001")
                .build();
    }

    private UserLoginRequest createUserLoginInfo() {
        return UserLoginRequest.builder()
                .userAccount("aebong@gmail.com")
                .userPassword("nonencodepassword")
                .build();
    }


}