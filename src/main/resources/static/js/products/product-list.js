$(document).ready(function () {
    let currentPage = 0;
    let pageSize = 20;
    let totalPages = 0;

    loadProducts();

    $("#btnReload").on("click", function () {
        loadProducts();
    });

    $("#pageSize").on("change", function () {
        pageSize = Number($(this).val()) || 20;
        currentPage = 0;
        loadProducts();
    });

    function loadProducts() {
        showError(null);
        setLoading();

        $.ajax({
            type: "POST",
            url: `/api/v1/products/paging?page=${currentPage}&size=${pageSize}`,
            contentType: "application/json; charset=utf-8",
            success: function (response) {
                if (response.code !== "SUCCESS" || !response.data) {
                    showError(response.message || "상품 목록 조회 응답이 올바르지 않습니다.");
                    setEmpty("조회 결과가 없습니다.");
                    return;
                }

                currentPage = Number(response.data.number || 0);
                totalPages = Number(response.data.totalPages || 0);
                renderTable(response.data.content || [], currentPage, Number(response.data.size || pageSize));
                renderPagination(currentPage, totalPages);
                setText("#totalCount", response.data.totalElements || 0);
            },
            error: function (jqXHR) {
                let message = "상품 목록 조회 중 오류가 발생했습니다.";
                try {
                    const errorResponse = JSON.parse(jqXHR.responseText);
                    if (errorResponse && errorResponse.message) {
                        message = errorResponse.message;
                    }
                } catch (e) {
                    // ignore parse error
                }

                showError(message);
                setEmpty("조회 결과가 없습니다.");
            }
        });
    }

    function setLoading() {
        const row = '<tr><td colspan="8" class="table-empty">데이터를 불러오는 중입니다.</td></tr>';
        $("#productTableBody").html(row);
    }

    function setEmpty(message) {
        const row = `<tr><td colspan="8" class="table-empty">${escapeHtml(message)}</td></tr>`;
        $("#productTableBody").html(row);
        $("#pagination").empty();
        setText("#totalCount", "0");
    }

    function renderTable(products, page, size) {
        if (products.length === 0) {
            setEmpty("등록된 상품이 없습니다.");
            return;
        }

        const rows = products.map(function (product, index) {
            const price = resolveSalesAmount(product);
            const productId = valueOrDash(product.productId);
            const rowNumber = page * size + index + 1;
            return `
                <tr class="clickable" data-product-id="${productId}">
                    <td class="text-center">${rowNumber}</td>
                    <td class="product-code">${valueOrDash(product.productCode)}</td>
                    <td>${valueOrDash(product.productName)}</td>
                    <td>${valueOrDash(product.productType)}</td>
                    <td>${valueOrDash(product.manufacturerCountry)}</td>
                    <td>${valueOrDash(product.releaseDatetime)}</td>
                    <td class="text-end">${formatNumber(price)}원</td>
                    <td class="text-center">
                        <a href="/products/info/${encodeURIComponent(productId)}" class="btn btn-sm btn-outline-dark">상세보기</a>
                    </td>
                </tr>
            `;
        }).join("");

        $("#productTableBody").html(rows);

        $("#productTableBody tr").on("click", function (event) {
            if ($(event.target).closest("a").length > 0) {
                return;
            }
            const productId = $(this).data("product-id");
            if (productId) {
                window.location.href = `/products/info/${encodeURIComponent(productId)}`;
            }
        });
    }

    function renderPagination(page, total) {
        const $pagination = $("#pagination");
        $pagination.empty();

        if (total <= 0) {
            return;
        }

        $pagination.append(createPageItem("이전", page - 1, page <= 0, false));

        let startPage = Math.max(0, page - 2);
        let endPage = Math.min(total - 1, startPage + 4);
        startPage = Math.max(0, endPage - 4);

        for (let i = startPage; i <= endPage; i += 1) {
            $pagination.append(createPageItem(String(i + 1), i, false, i === page));
        }

        $pagination.append(createPageItem("다음", page + 1, page >= total - 1, false));

        $pagination.find("a[data-page]").on("click", function (event) {
            event.preventDefault();
            const targetPage = Number($(this).data("page"));
            if (Number.isNaN(targetPage) || targetPage === currentPage) {
                return;
            }
            currentPage = targetPage;
            loadProducts();
        });
    }

    function createPageItem(label, page, disabled, active) {
        const disabledClass = disabled ? " disabled" : "";
        const activeClass = active ? " active" : "";
        return `
            <li class="page-item${disabledClass}${activeClass}">
                <a class="page-link" href="#" data-page="${page}">${label}</a>
            </li>
        `;
    }

    function resolveSalesAmount(product) {
        if (!product.priceList || product.priceList.length === 0) {
            return 0;
        }
        return product.priceList[0].salesAmount || 0;
    }

    function formatNumber(value) {
        const numberValue = Number(value || 0);
        return numberValue.toLocaleString("ko-KR");
    }

    function valueOrDash(value) {
        if (value === null || value === undefined || value === "") {
            return "-";
        }
        return value;
    }

    function setText(selector, value) {
        $(selector).text(valueOrDash(value));
    }

    function showError(message) {
        const $box = $("#errorBox");
        if (!message) {
            $box.addClass("d-none").text("");
            return;
        }
        $box.removeClass("d-none").text(message);
    }

    function escapeHtml(text) {
        return String(text)
            .replaceAll("&", "&amp;")
            .replaceAll("<", "&lt;")
            .replaceAll(">", "&gt;")
            .replaceAll('"', "&quot;")
            .replaceAll("'", "&#039;");
    }
});
