let orders = [[${orders}]]
$('#ab1').html(genHtml);
function genHtml(){
for (item: orders){
    let html += `
       <!-- Item -->
                                <div class="item" >
                                    <div class="header-item">
                                        <div class="order-number-wrapper">
                                            Mã đơn <a class="order-number" href="@{/tai-khoan/lich-su-giao-dich/{id}(id=${item.id})}" th:text="'#'+${item.id}"></a>
                                        </div>
                                        <a class="full-details" href="@{/tai-khoan/lich-su-giao-dich/{id}(id=${item.id})}">Xem chi tiết</a>
                                    </div>
                                    <div class="item-info-wrapper">
                                        <div class="product-info-wrapper">
                                            <div class="thumbnails"><img class="img-fluid" src="@{${item.product.productImages[0]}}" th:alt="${item.product.name}"/></div>
                                            <div class="product-info"><span class="name" >${item.product.name}</span>
                                                <div class="size"> <span >${item.size}</span>VN | </div>
                                            </div>
                                        </div>
                                        <div class="price bid-price"><span class="text-price" >${item.totalPrice}</span> &#x20AB;</div>
                                    </div>
                                </div>
    `
    return html;
     }
    }


