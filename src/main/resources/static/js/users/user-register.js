/** user-register.html */

$(document).ready(function () {

    // -------------------- check password match --------------------

    $("#passwordCheck, #userPassword").on("keyup", function () {
        const password = $("#userPassword").val();
        const passwordCheck = $("#passwordCheck").val();
        const message = $("#passwordMatchMessage");

        if (password !== "" && passwordCheck !== "") {
            if (password === passwordCheck) {
                message.css("color", "green").text("비밀번호가 일치합니다.");
            } else {
                message.css("color", "red").text("비밀번호가 일치하지 않습니다.");
            }
        } else {
            message.text("");
        }
    });

    // -------------------- check password match --------------------

    // -------------------- check email verification --------------------

    // 이메일 인증 요청
    // $("#authenticateEmail").click(function () {
    //     var email = $("input[name='email']").val(); // 사용자가 입력한 이메일 가져오기
    //
    //     $.ajax({
    //         type: "POST",
    //         url: "/kittop/emailConfirm",
    //         data: {
    //             email: email
    //         },
    //         success: function (response) {
    //             // 이메일 인증 코드 발송 성공
    //             alert("인증 메일이 발송되었습니다");
    //             console.log('ajax response:', response);
    //         },
    //         error: function (jqXHR, textStatus, errorThrown) {
    //             // 오류 처리
    //             console.error(textStatus + " " + errorThrown);
    //         }
    //     });
    // });

    // 인증 코드 검증 요청
    // $("#verifyCode").click(function () {
    //     var code = $("#authenticateEmailCode").val(); // 사용자가 입력한 인증 코드 가져오기
    //
    //     $.ajax({
    //         type: "POST",
    //         url: "/kittop/emailVerify",
    //         data: {
    //             code: code
    //         },
    //         success: function (response) {
    //             console.log(response);
    //             if (response.success) {
    //                 // 인증 성공 메시지 띄우고 모달창 닫기
    //                 alert("인증 성공");
    //                 $("#btnForm").prop("disabled", false); // 회원가입 버튼 활성화
    //             } else {
    //                 // 서버에서 전달받은 메시지를 에러 표시 영역에 표시
    //                 $("#codeError").text(response.message);
    //                 $("#btnForm").prop("disabled", true); // 회원가입 버튼 비활성화
    //             }
    //         },
    //         error: function (jqXHR, textStatus, errorThrown) {
    //             // 오류 처리
    //             console.error(textStatus + " " + errorThrown);
    //         }
    //     });
    // });
    <!-- 이메일 인증 끝 -->

    // -------------------- check email verification --------------------

    // -------------------- zipcode kakao api --------------------

    function execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function (data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var fullAddr = ''; // 최종 주소 변수
                var extraAddr = ''; // 조합형 주소 변수

                // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    fullAddr = data.roadAddress;
                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    fullAddr = data.jibunAddress;
                }

                // 사용자가 선택한 주소가 도로명 타입일때 조합한다.
                if (data.userSelectedType === 'R') {
                    //법정동명이 있을 경우 추가한다.
                    if (data.bname !== '') {
                        extraAddr += data.bname;
                    }
                    // 건물명이 있을 경우 추가한다.
                    if (data.buildingName !== '') {
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    // 조합형주소의 유무에 따라 양쪽에 괄호를 추가하여 최종 주소를 만든다.
                    fullAddr += (extraAddr !== '' ? ' (' + extraAddr + ')' : '');
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.querySelector('input[name=zipcode]').value = data.zonecode; //5자리 새우편번호 사용
                document.querySelector('input[name=addr1]').value = fullAddr;

                // 커서를 상세주소 필드로 이동한다.
                document.querySelector('input[name=addr2]').focus();
            }
        }).open();
    }

    // -------------------- zipcode kakao api --------------------

    // ------------------- user register form submit -------------------
    $("#btnForm").click(function (e) {

        e.preventDefault(); // 폼 기본 전송 막기

        const formObj = document.formObj;
        let serverCode = "";

        // check the required value
        let isValid = true;
        $(".needs-validation input[required]").each(function () {
            if ($(this).val().trim() === "") {
                alert("필수 입력값이 비어있습니다: " + $(this).attr("name"));
                isValid = false;
                return false; // break
            }
        });
        if (!isValid) return; // invalid value

        const rawBirth = $("#birthDate").val(); // 예: 900101
        const formattedBirth = formatBirthDate(rawBirth); // ex: 900101 -> 1990-01-01

        // mobile number formatting
        const phone1 = document.querySelector("input[name='phone1']").value;
        const phone2 = document.querySelector("input[name='phone2']").value;
        const phone3 = document.querySelector("input[name='phone3']").value;
        $("#mobileNumber").val(phone1 + phone2 + phone3); // // setting hidden input
        const mobileNumber = $("#mobileNumber").val();    // 문자열 값 가져오기

        const gender = $("input[name='gender']:checked").val();

        // email verification number
        // const userCode = formObj.authenticateEmailCode;
        // if (userCode.value.length > 0 && (!userCode || userCode.trim() === "")) {
        //     alert("이메일 인증 코드를 입력해주세요.");
        //     return;
        // }

        $.ajax({
            type: "POST",
            url: "/api/v1/users/sign-up",
            contentType: "application/json; charset=utf-8",   // JSON 전송 명시
            data: JSON.stringify({
                userAccount: $("#userAccount").val(),         // 폼 input 값
                userPassword: $("#userPassword").val(),
                firstName: $("#firstName").val(),
                lastName: $("#lastName").val(),
                birthDate: formattedBirth,                    // 예: 1990-01-01
                gender: gender,                               // 예: MALE, FEMALE
                mobileNumber: mobileNumber,
                nickName: $("#nickName").val(),
                telNumber: $("#telNumber").val(),
                address1: $("#address1").val(),
                address2: $("#address2").val(),
                zipcode: $("#zipcode").val()
            }),
            success: function (response) {
                console.log('ajax response:', response);
                serverCode = response;

                // TODO: 서버 응답 데이터(response.data)로 환영 메시지 or 안내 페이지 표시 가능
                alert("회원가입이 완료되었습니다!");

                // main view redirect
                window.location.href = "/";

                // let next = true;
                // if (next) {
                //     console.log(userCode.value, serverCode);
                //     formObj.action = "/api/v1/users/sign-up";
                //     formObj.method = "post";
                //     console.log(formObj);
                //     formObj.submit();
                // }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                // 오류 처리
                console.error(textStatus + " " + errorThrown);
                alert("회원가입 중 오류가 발생했습니다. 다시 시도해주세요.");
            }
        });
    });

    // ------------------- user register form submit -------------------

    function formatBirthDate(yyMMdd) {

        if (!/^\d{6}$/.test(yyMMdd)) {
            alert("생년월일 형식이 잘못되었습니다. (예: 900101)");
            return null;
        }

        let yy = parseInt(yyMMdd.substring(0, 2), 10);
        let mm = yyMMdd.substring(2, 4);
        let dd = yyMMdd.substring(4, 6);

        // 00 ~ 24 -> 2000s, Others -> 1900s
        let yyyy = (yy <= 24 ? 2000 : 1900) + yy;

        return `${yyyy}-${mm}-${dd}`; // yyyy-MM-dd
    }

});