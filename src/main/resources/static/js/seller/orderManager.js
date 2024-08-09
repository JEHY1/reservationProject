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
        let paymentType = document.getElementById('paymentType').value;
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

        location.href = '/seller/orderList?' + params;
    });
}

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


        let param = '';
//        param += getUrlParameter("unitPeriod") !== null ? '&unitPeriod=' + getUrlParameter("unitPeriod") : '';
//        param += getUrlParameter("startDate") !== null ? '&startDate=' + getUrlParameter("startDate") : '';
//        param += getUrlParameter("endDate") !== null ? '&endDate=' + getUrlParameter("endDate") : '';

        if(currentPageNum + 1 === currentPageGroupNum  * 5 + i + 1){
            pageButton.classList.add('selectedPage-btn');
        }
        else{
            pageButton.setAttribute('onclick', "location.href='/seller/orderList?" + param + "&page=" + (currentPageGroupNum * 5 + i) + "'");
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
    let param = '';
//    param += getUrlParameter("unitPeriod") !== null ? '&unitPeriod=' + getUrlParameter("unitPeriod") : '';
//    param += getUrlParameter("startDate") !== null ? '&startDate=' + getUrlParameter("startDate") : '';
//    param += getUrlParameter("endDate") !== null ? '&endDate=' + getUrlParameter("endDate") : '';
    prevPageButton.setAttribute('onclick', "location.href='/seller/orderList?" + param + "&page=" + (parseInt(getUrlParameter('page')) - 1) + "'");
}

const nextPageButton = document.getElementById('nextPage-btn');

if(nextPageButton){
    let param = '';
//    param += getUrlParameter("unitPeriod") !== null ? '&unitPeriod=' + getUrlParameter("unitPeriod") : '';
//    param += getUrlParameter("startDate") !== null ? '&startDate=' + getUrlParameter("startDate") : '';
//    param += getUrlParameter("endDate") !== null ? '&endDate=' + getUrlParameter("endDate") : '';
    nextPageButton.setAttribute('onclick', "location.href='/seller/orderList?" + param + "&page=" + (parseInt(getUrlParameter('page') === null ? 0 : parseInt(getUrlParameter('page'))) + 1) + "'");
}

//넘버링
if(document.getElementsByClassName('num')){
    let pageNum = getUrlParameter('page');
    pageNum === null ? 0 : pageNum;
    Array.from(document.getElementsByClassName('num')).forEach((comp, index) => {
        comp.textContent = pageNum * 10 + index + 1;
    });
}