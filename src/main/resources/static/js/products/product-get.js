/** product-get.html */

$(document).ready(function () {

    // URL에서 productId 파라미터 추출
    const params = new URLSearchParams(window.location.search);
    const productId = params.get("productId");

    // -------------------- get product info from sessionStorage --------------------

    const info = JSON.parse(sessionStorage.getItem("productInfo"));

    if (info) {
        // 기본 정보
        $("#productId").text(info.productId);
        $("#productDetailId").text(info.productDetailId);
        $("#productCode").text(info.productCode);
        $("#productType").text(info.productType);
        $("#productName").text(info.productName);
        $("#productEnglishName").text(info.productEnglishName || "-");
        $("#productShortName").text(info.productShortName || "-");
        $("#basicDescription").text(info.basicDescription || "-");
        $("#detailDescription").text(info.detailDescription || "-");
        $("#manufacturerCountry").text(info.manufacturerCountry || "-");
        $("#releaseDatetime").text(info.releaseDatetime || "-");

        // 가격 정보 테이블 렌더링
        if (info.priceList && info.priceList.length > 0) {
            renderPriceTable(info.priceList);
        }

        // 이미지 목록 렌더링
        if (info.imageList && info.imageList.length > 0) {
            renderImageGallery(info.imageList);
        }
    }

    // -------------------- get product info from sessionStorage --------------------

    // -------------------- 가격 테이블 렌더링 --------------------
    function renderPriceTable(priceList) {
        const $tbody = $("#priceTableBody");
        $tbody.empty();

        priceList.forEach(function (price, index) {
            const row = `
                <tr>
                    <td class="text-center">${index + 1}</td>
                    <td>${price.applyStartDate || "-"}</td>
                    <td>${price.applyEndDate || "-"}</td>
                    <td class="text-end fw-bold">${formatNumber(price.salesAmount)}원</td>
                    <td class="text-end">${formatNumber(price.purchaseAmount)}원</td>
                    <td class="text-center">
                        <span class="badge ${price.discountType === 'NONE' ? 'bg-secondary' : 'bg-success'}">
                            ${price.discountType}
                        </span>
                    </td>
                    <td class="text-end">${formatNumber(price.discountAmount)}원</td>
                    <td class="text-end">${price.discount || 0}%</td>
                </tr>
            `;
            $tbody.append(row);
        });
    }
    // -------------------- 가격 테이블 렌더링 --------------------

    // -------------------- 이미지 갤러리 렌더링 --------------------
    function renderImageGallery(imageList) {
        const $gallery = $("#imageGallery");
        $gallery.empty();

        imageList.forEach(function (image) {
            const card = `
                <div class="col-auto">
                    <div class="card" style="width: 150px;">
                        <img src="${image.imageFileUrl}"
                             alt="${image.imageFileName}"
                             class="image-thumbnail card-img-top"
                             data-bs-toggle="modal"
                             data-bs-target="#imageModal"
                             data-image-url="${image.imageFileUrl}">
                        <div class="card-body p-2">
                            <p class="card-text small text-truncate mb-1">${image.originalImageFileName}</p>
                            <span class="badge bg-info">${image.imageType}</span>
                            <small class="text-muted d-block">${image.width} x ${image.height}</small>
                        </div>
                    </div>
                </div>
            `;
            $gallery.append(card);
        });
    }
    // -------------------- 이미지 갤러리 렌더링 --------------------

    // -------------------- 숫자 포맷팅 --------------------
    function formatNumber(num) {
        if (num === null || num === undefined) return "0";
        return Number(num).toLocaleString("ko-KR");
    }
    // -------------------- 숫자 포맷팅 --------------------

    // -------------------- 이미지 모달 처리 --------------------
    $("#imageModal").on("show.bs.modal", function (event) {
        const button = $(event.relatedTarget);
        const imageUrl = button.data("image-url");
        $(this).find("#modalImage").attr("src", imageUrl);
    });
    // -------------------- 이미지 모달 처리 --------------------

    // -------------------- 수정 버튼 클릭 --------------------
    $("#modifyBtn").on("click", function () {
        if (productId) {
            window.location.href = "/products/edit/" + encodeURIComponent(productId);
        } else {
            alert("상품 ID가 없습니다.");
        }
    });
    // -------------------- 수정 버튼 클릭 --------------------

    // -------------------- 삭제 버튼 클릭 --------------------
    $("#deleteBtn, #btnDelete, #btnDeleteBottom").on("click", function () {
        if (!productId) {
            alert("상품 ID가 없습니다.");
            return;
        }

        if (confirm("정말로 이 상품을 삭제하시겠습니까?\n삭제된 상품은 복구할 수 없습니다.")) {
            deleteProduct(productId);
        }
    });

    function deleteProduct(productId) {
        $.ajax({
            type: "DELETE",
            url: `/api/v1/products/${productId}`,
            contentType: "application/json; charset=utf-8",
            success: function (response) {
                if (response.code === "SUCCESS") {
                    alert("상품이 삭제되었습니다.");
                    sessionStorage.removeItem("productInfo");
                    window.location.href = "/products";
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error("AJAX Error:", textStatus, errorThrown);

                try {
                    const errorResponse = JSON.parse(jqXHR.responseText);
                    if (errorResponse && errorResponse.message) {
                        alert("삭제 실패: " + errorResponse.message);
                    } else {
                        alert("알 수 없는 오류가 발생했습니다.");
                    }
                } catch (e) {
                    alert(jqXHR.responseText || "서버 오류가 발생했습니다.");
                }
            }
        });
    }
    // -------------------- 삭제 버튼 클릭 --------------------

    // -------------------- 목록 버튼 클릭 --------------------
    $("#listBtn").on("click", function () {
        sessionStorage.removeItem("productInfo");
        window.location.href = "/products";
    });
    // -------------------- 목록 버튼 클릭 --------------------

});
