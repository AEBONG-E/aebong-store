/** user-get.html */

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
                    $("#userId").text(info.userId);                     // hidden
                    $("#userDetailId").text(info.userDetailId);         // hidden
                    $("#userAccount").text(info.userAccount);           // hidden
                    $("#userPassword").text(info.userPassword);         // hidden
                    $("#lastName").text(info.lastName);                 // hidden
                    $("#firstName").text(info.firstName);               // hidden
                    $("#address1").val(info.address1);                  // hidden
                    $("#address2").val(info.address2);                  // hidden
                    $("#name").text(info.lastName + "" + info.firstName);
                    $("#birthDate").text(info.birthDate);
                    $("#gender").text(info.gender);                     // hidden
                    $("#mobileNumber").text(info.mobileNumber);
                    $("#nickName").text(info.nickName);
                    $("#email").text(info.email);
                    $("#address").val(info.address1 + ", " + info.address2);
                    $("#zipcode").val(info.zipcode);
                    $("#joinDatetime").text(info.joinDatetime);
                }
            },
            error: function () {
                alert("사용자 정보를 불러오는 중 오류가 발생했습니다.");
            }
        });
    }

    // -------------------- get user api call --------------------

    // -------------------- user modify view --------------------
    $("#modifyBtn").on("click", function () {
        if (userAccount) {
            window.location.href = "/users/modify?userAccount=" + encodeURIComponent(userAccount);
        } else {
            alert("userAccount가 없습니다.");
        }
    });
    // -------------------- user modify view --------------------

});