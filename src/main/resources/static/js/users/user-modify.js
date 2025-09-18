/** user-modify.html */

$(document).ready(function () {

    // loginBtn query parameter parsing
    const params = new URLSearchParams(window.location.search);
    const userAccount = params.get("userAccount");

    // -------------------- get user api call --------------------

    if (userAccount) {
        $.ajax({
            url: "/api/v1/users/" + encodeURIComponent(userAccount),
            type: "GET",
            success: function (response) {
                if (response.code === "SUCCESS") {
                    const info = response.data;
                    $("#userId").val(info.userId);                  // hidden
                    $("#userDetailId").val(info.userDetailId);      // hidden
                    $("#userAccount").val(info.userAccount);
                    $("#lastName").val(info.lastName);
                    $("#firstName").val(info.firstName);
                    $("#birthDate").val(info.birthDate);
                    $("#gender").val(info.gender);
                    $("#mobileNumber").val(info.mobileNumber);      // hidden
                    $("#nickName").val(info.nickName);
                    $("#address1").val(info.address1);
                    $("#address2").val(info.address2);
                    $("#zipcode").val(info.zipcode);

                    checkGender(info.gender);
                    mobileNumberSubstring(info.mobileNumber);
                }
            },
            error: function () {
                alert("사용자 정보를 불러오는 중 오류가 발생했습니다.");
            }
        });
    }

    // -------------------- get user api call --------------------

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

    // ------------------- check gender -------------------
    function checkGender(gender) {
        if (gender === "MALE") {
            $("#genderM").prop("checked", true);
        } else if (gender === "FEMALE") {
            $("#genderF").prop("checked", true);
        }
    }
    // ------------------- check gender -------------------

    // ------------------- mobile number substring -------------------
    function mobileNumberSubstring(mobileNumber) {
        if (!mobileNumber) return;
        // 휴대폰번호 패턴: 3-4-4
        const phone1 = mobileNumber.substring(0, 3);  // 010
        const phone2 = mobileNumber.substring(3, 7);  // 2222
        const phone3 = mobileNumber.substring(7);     // 1234

        $("input[name=phone1]").val(phone1);
        $("input[name=phone2]").val(phone2);
        $("input[name=phone3]").val(phone3);
    }
    // ------------------- mobile number substring -------------------

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

    // ------------------- user modify form submit -------------------
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

        // const rawBirth = $("#birthDate").val(); // 예: 900101
        // const formattedBirth = formatBirthDate(rawBirth); // ex: 900101 -> 1990-01-01

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
            type: "PATCH",
            url: "/api/v1/users/" + encodeURIComponent(userAccount),
            contentType: "application/json; charset=utf-8",   // JSON 전송 명시
            data: JSON.stringify({
                userAccount: $("#userAccount").val(),
                userPassword: $("#userPassword").val(),
                firstName: $("#firstName").val(),
                lastName: $("#lastName").val(),
                birthDate: $("#birthDate").val(),
                gender: gender,
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

                // TODO: 서버 응답 데이터(response.data)로 환영 메시지이or 안내 페이지 표시 가능
                alert("정보수정 완료되었습니다!");

                // user get view redirect
                window.location.href = "/users/info?userAccount=" + encodeURIComponent(userAccount);

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
                console.error("AJAX Error:", textStatus, errorThrown);

                try {
                    // 서버가 JSON 형태로 내려준 경우
                    const errorResponse = JSON.parse(jqXHR.responseText);

                    // 서버 응답 구조가 { code, message, data } 형태라면
                    if (errorResponse && errorResponse.message) {
                        alert(errorResponse.message);
                    } else {
                        alert("알 수 없는 오류가 발생했습니다.");
                    }
                } catch (e) {
                    // JSON 파싱 실패한 경우 (서버가 plain text 내려줬을 때 등)
                    alert(jqXHR.responseText || "서버 오류가 발생했습니다.");
                }
            }
        });
    });

    // ------------------- user modify form submit -------------------


});

// // -------------------- delete api call --------------------
// const formObj = document.querySelector('form');
// document.querySelector("#deleteBtn").addEventListener('click', function (e) {
//     e.preventDefault();
//     e.stopPropagation();
//
//     formObj.action = `/kittop/user/delete`;
//     formObj.method = 'post';
//     formObj.submit();
//
// }, false);
// // -------------------- delete api call --------------------