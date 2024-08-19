function httpRequest(url, method, body){
    return fetch(url, {
        method: method,
        headers: {
            "Content-Type" : "application/json"
        },
        body: body
    });
}

function getUrlParameter(name) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(name);
}

function getPath(index){
    console.log(window.location.pathname.split('/'));
    return window.location.pathname.split('/')[index];
}

function toWon(price){
    let PriceText = '';
    price += '';

    while(price.length > 3){
        console.log(price.substring(price.length - 3, price.length));
        PriceText += ',' + price.substring(price.length - 3, price.length);
        price = price.substring(0, price.length - 3);
        console.log(price);
        console.log(PriceText);
    }
    return price + PriceText + '원';
}

function prevElementClick(comp){
    comp.previousElementSibling.click();
}

const searchButton = document.getElementById('search-btn');

if(searchButton){
    searchButton.addEventListener('click', () => {
        let params = '';
        let productId = document.getElementById('productId').value;
        let orderStatus = document.getElementById('orderStatus').value;
        let orderDate1 = document.getElementById('orderDate1').value;
        let orderDate2 = document.getElementById('orderDate2').value;
        let reservationDate1 = document.getElementById('reservationDate1').value;
        let reservationDate2 = document.getElementById('reservationDate2').value;
        let paymentType = '';
        Array.from(document.getElementsByClassName('radio-btn')).forEach(button => {
            if(button.checked){
                paymentType = button.value;
            }
        });

        let depositorName = document.getElementById('depositorName').value;
        let paymentPrice1 = document.getElementById('paymentPrice1').value;
        let paymentPrice2 = document.getElementById('paymentPrice2').value;

        params += productId === '' || productId === '0' ? '' : '&productId=' + productId;
        params += orderStatus === '' || orderStatus === '0' ? '' : '&orderStatus=' + orderStatus;
        params += orderDate1 === '' ? '' : '&orderDate1=' + orderDate1;
        params += orderDate2 === '' ? '' : '&orderDate2=' + orderDate2;
        params += reservationDate1 === '' ? '' : '&reservationDate1=' + reservationDate1;
        params += reservationDate2 === '' ? '' : '&reservationDate2=' + reservationDate2;
        params += paymentType === '' ? '' : '&paymentType=' + paymentType;
        params += depositorName === '' ? '' : '&depositorName=' + depositorName;
        params += paymentPrice1 === '' ? '' : '&paymentPrice1=' + paymentPrice1;
        params += paymentPrice2 === '' ? '' : '&paymentPrice2=' + paymentPrice2;

        location.href = '/' + getPath(1) + '/orderList?' + params;
    });
}

//무통장입금 입금자명칸 표시, 미표시
if(document.getElementsByClassName('radio-btn')){
    Array.from(document.getElementsByClassName('radio-btn')).forEach(button => {
        button.addEventListener('click', () => {
            if(button.value === '무통장입금'){
                document.getElementById('depositorName').classList.remove('d-hidden');
                document.getElementById('depositorName').previousElementSibling.classList.remove('d-hidden');
            }
            else{
                document.getElementById('depositorName').classList.add('d-hidden');
                document.getElementById('depositorName').previousElementSibling.classList.add('d-hidden');
            }
        });
    });
}

//detailPage 이동
document.querySelectorAll('.order').forEach(function(orderDiv) {
    orderDiv.addEventListener('click', function() {
        let path = orderDiv.getAttribute('data-path');
        let orderId = orderDiv.getAttribute('data-order-id');
        location.href = `/${path}/orderDetail/${orderId}`;
    });
});

//페이징
const pageButtonField = document.getElementById('pageButtonField');

if(pageButtonField){
    let currentPageNum = getUrlParameter('page') === null ? 0 : parseInt(getUrlParameter("page"));
    let totalPageSize = parseInt(document.getElementById('totalPageSize').value);
    let currentPageGroupNum = parseInt(currentPageNum / 5);
    let totalPageGroupNum = parseInt(totalPageSize / 5);

    let repeat = 5;
    if(currentPageGroupNum === totalPageGroupNum){
        repeat = totalPageSize % 5;
    }

    for(let i = 0; i < repeat; i++){
        console.log(currentPageGroupNum * 5 + i + 1);

        let pageButton = document.createElement('button');
        pageButton.textContent = currentPageGroupNum * 5 + i + 1;
        pageButton.setAttribute('class', 'p-0 page-btn d-flex align-items-center justify-content-center mx-1');

        let divWrap = document.createElement('div');


        let params = '';
        let productId = getUrlParameter('productId');
        let orderStatus = getUrlParameter('orderStatus');
        let orderDate1 = getUrlParameter('orderDate1');
        let orderDate2 = getUrlParameter('orderDate2');
        let reservationDate1 = getUrlParameter('reservationDate1');
        let reservationDate2 = getUrlParameter('reservationDate2');
        let paymentType = getUrlParameter('paymentType');
        let depositorName = getUrlParameter('depositorName');
        let paymentPrice1 = getUrlParameter('paymentPrice1');
        let paymentPrice2 = getUrlParameter('paymentPrice2');

        params += productId === null || productId === '0' ? '' : '&productId=' + productId;
        params += orderStatus === null || orderStatus === '0' ? '' : '&orderStatus=' + orderStatus;
        params += orderDate1 === null ? '' : '&orderDate1=' + orderDate1;
        params += orderDate2 === null ? '' : '&orderDate2=' + orderDate2;
        params += reservationDate1 === null ? '' : '&reservationDate1=' + reservationDate1;
        params += reservationDate2 === null ? '' : '&reservationDate2=' + reservationDate2;
        params += paymentType === null ? '' : '&paymentType=' + paymentType;
        params += depositorName === null ? '' : '&depositorName=' + depositorName;
        params += paymentPrice1 === null ? '' : '&paymentPrice1=' + paymentPrice1;
        params += paymentPrice2 === null ? '' : '&paymentPrice2=' + paymentPrice2;


        if(currentPageNum + 1 === currentPageGroupNum  * 5 + i + 1){
            pageButton.classList.add('selectedPage-btn');
        }
        else{
            pageButton.setAttribute('onclick', "location.href='/" + getPath(1) + "/orderList?" + params + "&page=" + (currentPageGroupNum * 5 + i) + "'");
        }

        pageButtonField.appendChild(divWrap);
        divWrap.appendChild(pageButton);

        if(currentPageNum === 0){
            document.getElementById('prevPage-btn').setAttribute('disabled', '');
        }

        if(currentPageNum === totalPageSize - 1){
            document.getElementById('nextPage-btn').setAttribute('disabled', '');
        }
    }
}

const prevPageButton = document.getElementById('prevPage-btn');

if(prevPageButton){
    let params = '';
    let productId = getUrlParameter('productId');
    let orderStatus = getUrlParameter('orderStatus');
    let orderDate1 = getUrlParameter('orderDate1');
    let orderDate2 = getUrlParameter('orderDate2');
    let reservationDate1 = getUrlParameter('reservationDate1');
    let reservationDate2 = getUrlParameter('reservationDate2');
    let paymentType = getUrlParameter('paymentType');
    let depositorName = getUrlParameter('depositorName');
    let paymentPrice1 = getUrlParameter('paymentPrice1');
    let paymentPrice2 = getUrlParameter('paymentPrice2');

    params += productId === null || productId === '0' ? '' : '&productId=' + productId;
    params += orderStatus === null || orderStatus === '0' ? '' : '&orderStatus=' + orderStatus;
    params += orderDate1 === null ? '' : '&orderDate1=' + orderDate1;
    params += orderDate2 === null ? '' : '&orderDate2=' + orderDate2;
    params += reservationDate1 === null ? '' : '&reservationDate1=' + reservationDate1;
    params += reservationDate2 === null ? '' : '&reservationDate2=' + reservationDate2;
    params += paymentType === null ? '' : '&paymentType=' + paymentType;
    params += depositorName === null ? '' : '&depositorName=' + depositorName;
    params += paymentPrice1 === null ? '' : '&paymentPrice1=' + paymentPrice1;
    params += paymentPrice2 === null ? '' : '&paymentPrice2=' + paymentPrice2;

    prevPageButton.setAttribute('onclick', "location.href='/" + getPath(1) + "/orderList?" + params + "&page=" + (parseInt(getUrlParameter('page')) - 1) + "'");
}

const nextPageButton = document.getElementById('nextPage-btn');

if(nextPageButton){
    let params = '';
    let productId = getUrlParameter('productId');
    let orderStatus = getUrlParameter('orderStatus');
    let orderDate1 = getUrlParameter('orderDate1');
    let orderDate2 = getUrlParameter('orderDate2');
    let reservationDate1 = getUrlParameter('reservationDate1');
    let reservationDate2 = getUrlParameter('reservationDate2');
    let paymentType = getUrlParameter('paymentType');
    let depositorName = getUrlParameter('depositorName');
    let paymentPrice1 = getUrlParameter('paymentPrice1');
    let paymentPrice2 = getUrlParameter('paymentPrice2');

    params += productId === null || productId === '0' ? '' : '&productId=' + productId;
    params += orderStatus === null || orderStatus === '0' ? '' : '&orderStatus=' + orderStatus;
    params += orderDate1 === null ? '' : '&orderDate1=' + orderDate1;
    params += orderDate2 === null ? '' : '&orderDate2=' + orderDate2;
    params += reservationDate1 === null ? '' : '&reservationDate1=' + reservationDate1;
    params += reservationDate2 === null ? '' : '&reservationDate2=' + reservationDate2;
    params += paymentType === null ? '' : '&paymentType=' + paymentType;
    params += depositorName === null ? '' : '&depositorName=' + depositorName;
    params += paymentPrice1 === null ? '' : '&paymentPrice1=' + paymentPrice1;
    params += paymentPrice2 === null ? '' : '&paymentPrice2=' + paymentPrice2;

    nextPageButton.setAttribute('onclick', "location.href='/" + getPath(1) + "/orderList?" + params + "&page=" + (parseInt(getUrlParameter('page') === null ? 0 : parseInt(getUrlParameter('page'))) + 1) + "'");
}

//넘버링
if(document.getElementsByClassName('num')){
    let pageNum = getUrlParameter('page');
    pageNum === null ? 0 : pageNum;
    Array.from(document.getElementsByClassName('num')).forEach((comp, index) => {
        comp.textContent = pageNum * 10 + index + 1;
    });
}

if(document.getElementsByClassName('price')){
    Array.from(document.getElementsByClassName('price')).forEach(comp => comp.textContent = toWon(comp.textContent));
}