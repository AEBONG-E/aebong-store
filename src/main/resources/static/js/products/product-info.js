$(document).ready(function() {

    // 이미지 모달 처리
    $('#imageModal').on('show.bs.modal', function (event) {
        const button = $(event.relatedTarget);
        const imageUrl = button.data('image-url');
        const modal = $(this);
        modal.find('#modalImage').attr('src', imageUrl);
    });

    // 삭제 버튼 클릭 이벤트
    $('#btnDelete, #btnDeleteBottom').on('click', function() {
        if (confirm('정말로 이 상품을 삭제하시겠습니까?\n삭제된 상품은 복구할 수 없습니다.')) {
            deleteProduct();
        }
    });

    // 상품 삭제 함수
    function deleteProduct() {
        axios.delete('/api/v1/products/' + productId)
            .then(function(response) {
                alert('상품이 삭제되었습니다.');
                location.href = '/products';
            })
            .catch(function(error) {
                console.error('삭제 실패:', error);
                if (error.response && error.response.data && error.response.data.message) {
                    alert('삭제 실패: ' + error.response.data.message);
                } else {
                    alert('상품 삭제 중 오류가 발생했습니다.');
                }
            });
    }

});
