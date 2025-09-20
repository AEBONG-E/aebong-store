/** main.html */

$(document).ready(function () {

    /* user register view */
    $("#registerBtn").on("click", function () {
        window.location.href = "users/sign-up";
    });

    // login button event
    $("#loginBtn").on("click", function () {
        const userAccount = $("#userAccount").val();   // input 값 읽기
        const userPassword = $("#userPassword").val(); // input 값 읽기

        if (!userAccount || !userPassword) {
            alert("아이디(이메일) 또는 비밀번호를 입력해주세요.");
            return;
        }

        // -------------------- sign-in user api call --------------------

        $.ajax({
            type: "POST",
            url: `/api/v1/users/sign-in`,
            contentType: "application/json; charset=utf-8",   // JSON 전송 명시
            data: JSON.stringify({
                userAccount: userAccount,
                userPassword: userPassword,
            }),
            success: function (response) {
                if (response.code === "SUCCESS") {
                    sessionStorage.setItem("userInfo", JSON.stringify(response.data));
                    // user info view redirect
                    window.location.href = "users/info?userAccount=" + encodeURIComponent(userAccount);
                }
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

        // -------------------- sign-in user api call --------------------

    });

});
