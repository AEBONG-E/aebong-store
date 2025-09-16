/** user-get.html */

$(document).ready(function () {

    // loginBtn query parameter parsing
    const params = new URLSearchParams(window.location.search);
    const userAccount = params.get("userAccount");

    if (userAccount) {
        $.ajax({
            url: "/api/v1/users/" + encodeURIComponent(userAccount),
            type: "GET",
            success: function (response) {
                if (response.code === "SUCCESS") {
                    const info = response.data;
                    $("#userId").text(info.userId);                     // hidden
                    $("#userDetailId").text(info.userDetailId);         // hidden
                    $("#userAccount").text(info.userAccount);           // hidden
                    $("#userPassword").text(info.userPassword);         // hidden
                    $("#lastName").text(info.lastName);                 // hidden
                    $("#firstName").text(info.firstName);               // hidden
                    $("#name").text(info.lastName + "" + info.firstName);
                    $("#birthDate").text(info.birthDate);
                    $("#gender").text(info.gender);
                    $("#mobileNumber").text(info.mobileNumber);
                    $("#nickName").text(info.nickName);
                    $("#email").text(info.email);
                    $("#address1").text(info.address1);
                    $("#address2").text(info.address2);
                    $("#address").text(info.address1 + "" + info.address2);
                    $("#zipcode").text(info.zipcode);
                    $("#joinDatetime").text(info.joinDatetime);
                }
            },
            error: function () {
                alert("사용자 정보를 불러오는 중 오류가 발생했습니다.");
            }
        });
    }

});