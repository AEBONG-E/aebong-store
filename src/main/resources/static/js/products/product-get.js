$(document).ready(function () {
    const productId = resolveProductId();

    if (!productId) {
        showError("상품 ID가 없어 조회를 진행할 수 없습니다.");
        return;
    }

    loadProduct(productId);

    function resolveProductId() {
        const fromDom = $("#productView").data("product-id");
        if (fromDom) {
            return fromDom;
        }

        const pathParts = window.location.pathname.split("/").filter(Boolean);
        if (pathParts.length >= 3 && pathParts[0] === "products" && pathParts[1] === "info") {
            return pathParts[2];
        }

        const params = new URLSearchParams(window.location.search);
        return params.get("productId");
    }

    function loadProduct(productId) {
        $.ajax({
            type: "GET",
            url: `/api/v1/products/${productId}`,
            contentType: "application/json; charset=utf-8",
            success: function (response) {
                if (response.code !== "SUCCESS" || !response.data) {
                    showError(response.message || "상품 조회 응답이 올바르지 않습니다.");
                    return;
                }
                renderProduct(response.data);
            },
            error: function (jqXHR) {
                const fallbackMessage = "상품 조회 중 오류가 발생했습니다.";
                try {
                    const errorResponse = JSON.parse(jqXHR.responseText);
                    showError(errorResponse.message || fallbackMessage);
                } catch (e) {
                    showError(fallbackMessage);
                }
            }
        });
    }

    function renderProduct(info) {
        setText("#productId", info.productId);
        setText("#productCode", info.productCode);
        setText("#productType", info.productType);
        setText("#productName", info.productName);
        setText("#productEnglishName", info.productEnglishName);
        setText("#productShortName", info.productShortName);
        setText("#manufacturerCountry", info.manufacturerCountry);
        setText("#releaseDatetime", info.releaseDatetime);
        setText("#basicDescription", info.basicDescription);
        setText("#detailDescription", info.detailDescription);

        renderPriceTable(info.priceList || []);
        renderImageGallery(info.imageList || []);
    }

    function renderPriceTable(priceList) {
        const $tbody = $("#priceTableBody");
        $tbody.empty();

        if (priceList.length === 0) {
            $tbody.append('<tr><td colspan="8" class="text-center text-muted py-4">등록된 가격 정보가 없습니다.</td></tr>');
            return;
        }

        priceList.forEach(function (price, index) {
            const row = `
                <tr>
                    <td class="text-center">${index + 1}</td>
                    <td>${valueOrDash(price.applyStartDate)}</td>
                    <td>${valueOrDash(price.applyEndDate)}</td>
                    <td class="text-end">${formatNumber(price.salesAmount)}원</td>
                    <td class="text-end">${formatNumber(price.purchaseAmount)}원</td>
                    <td class="text-center">${valueOrDash(price.discountType)}</td>
                    <td class="text-end">${formatNumber(price.discountAmount)}원</td>
                    <td class="text-end">${valueOrDash(price.discount)}%</td>
                </tr>
            `;
            $tbody.append(row);
        });
    }

    function renderImageGallery(imageList) {
        const $gallery = $("#imageGallery");
        $gallery.empty();

        if (imageList.length === 0) {
            $gallery.append('<div class="col-12 text-center text-muted py-4">등록된 이미지가 없습니다.</div>');
            return;
        }

        imageList.forEach(function (image) {
            const imageUrl = valueOrDash(image.imageFileUrl);
            const card = `
                <div class="col-auto">
                    <div class="card" style="width: 150px;">
                        <img src="${imageUrl}" alt="${valueOrDash(image.imageFileName)}" class="image-thumbnail card-img-top">
                        <div class="card-body p-2">
                            <p class="card-text small text-truncate mb-1">${valueOrDash(image.originalImageFileName)}</p>
                            <span class="badge bg-info">${valueOrDash(image.imageType)}</span>
                        </div>
                    </div>
                </div>
            `;
            $gallery.append(card);
        });
    }

    function setText(selector, value) {
        $(selector).text(valueOrDash(value));
    }

    function valueOrDash(value) {
        if (value === null || value === undefined || value === "") {
            return "-";
        }
        return value;
    }

    function formatNumber(num) {
        if (num === null || num === undefined || num === "") {
            return "0";
        }
        return Number(num).toLocaleString("ko-KR");
    }

    function showError(message) {
        $("#errorBox").removeClass("d-none").text(message);
    }
});
