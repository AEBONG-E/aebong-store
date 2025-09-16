/** main.html */

$(document).ready(function () {

    /* user register view */
    $("#registerBtn").on("click", function () {
        window.location.href = "users/sign-up";
    });

    /* user get view */
    $("#loginBtn").on("click", function () {
        const userAccount = $("#userAccount").val(); // input 값 읽기
        if (!userAccount) {
            alert("아이디(이메일)를 입력해주세요.");
            return;
        }
        window.location.href = "users/info?userAccount=" + encodeURIComponent(userAccount);
    });

});