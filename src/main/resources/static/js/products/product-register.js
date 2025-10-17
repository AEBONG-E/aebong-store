/** product-register.html */

$(document).ready(function () {

    // ------------------- product register form submit -------------------

    $("#btnForm").click(function (e) {
        e.preventDefault(); // 폼 기본 전송 막기

        // 기본 상품 정보 구성
        const requestData = {
            productCode: $("#productCode").val(),
            productType: $("#productType").val(),
            productName: $("#productName").val(),
            productEnglishName: $("#productEnglishName").val(),
            productShortName: $("#productShortName").val(),
            basicDescription: $("#basicDescription").val(),
            detailDescription: $("#detailDescription").val(),
            manufacturerCountry: $("#manufacturerCountry").val(),
            releaseDatetime: $("#releaseDatetime").val() ?
                new Date($("#releaseDatetime").val()).toISOString() : null,
            price: {
                applyStartDate: $("#price\\.applyStartDate").val() || null,
                applyEndDate: $("#price\\.applyEndDate").val() || null,
                salesAmount: $("#price\\.salesAmount").val() || null,
                purchaseAmount: $("#price\\.purchaseAmount").val() || null,
                discountType: $("#price\\.discountType").val() || "NONE",
                discountAmount: $("#price\\.discountAmount").val() || null,
                discount: $("#price\\.discount").val() || null
            },
            imageList: []
        };

        // 이미지 input 에서 선택된 파일들 메타데이터 생성
        $("input[name='imageFiles']").each(function () {
            const files = this.files;
            if (files && files.length > 0) {
                for (let i = 0; i < files.length; i++) {
                    const file = files[i];
                    const imageMeta = {
                        adminImageFileName: "admin-" + Date.now() + "-" + file.name, // 임시 생성 규칙 todo: 추후 규칙 지정 필요
                        originalImageFileName: file.name,
                        imageFileName: file.name,
                        imageFileUrl: "/upload/temp/" + file.name, // 임시 URL todo: 추후 실제 업로드 경로 지정 필요
                        imageType: "URL",       // todo: 기본 타입 지정 (추후 UI 에서 선택 가능)
                        contentType: 'IMAGE',   // todo: 기본 타입 지정 (추후 UI 에서 선택 가능)
                        width: 0,       // todo: 추후 파일 실제 데이터 가져와야함
                        height: 0,      // todo: 추후 파일 실제 데이터 가져와야함
                        fileSize: 0     // todo: 추후 파일 실제 데이터 가져와야함
                    };
                    requestData.imageList.push(imageMeta);
                }
            }
        });

        console.log("요청 JSON:", requestData);

        // JSON 전송
        $.ajax({
            type: "POST",
            url: "/api/v1/products",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(requestData),
            success: function (response) {
                console.log("상품 등록 성공:", response);
                alert("상품이 등록되었습니다!");
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error("AJAX Error:", textStatus, errorThrown);
                try {
                    const errorResponse = JSON.parse(jqXHR.responseText);
                    if (errorResponse && errorResponse.message) {
                        alert(errorResponse.message);
                    } else {
                        alert("알 수 없는 오류가 발생했습니다.");
                    }
                } catch (e) {
                    alert(jqXHR.responseText || "서버 오류가 발생했습니다.");
                }
            }
        });
    });

    // ------------------- product register form submit -------------------

});