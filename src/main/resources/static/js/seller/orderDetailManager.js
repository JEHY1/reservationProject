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

function httpRequest(url, method, body){
    return fetch(url, {
        method: method,
        headers: {
            "Content-Type" : "application/json"
        },
        body: body
    });
}

const paymentCheckButton = document.getElementById('paymentCheck-btn');

if(paymentCheckButton){
    paymentCheckButton.addEventListener('click', () => {
        let body = JSON.stringify({
            orderId : document.getElementById('orderId').value
        });

        httpRequest(`/api/paymentCheck`, 'POST', body)
        .then(response => {
            if(response.ok){
                alert('해당 주문을 입금확인 완료처리 하였습니다.');
                location.reload();
            }
            else{
                alert('error1');
            }
        });
    });
}

const refundButton = document.getElementById('refund-btn');

if(refundButton){
    refundButton.addEventListener('click', () => {
        let body = JSON.stringify({
            orderId : document.getElementById('orderId').value
        });

        httpRequest(`/api/paymentRefund`, 'POST', body)
        .then(response => {
            if(response.ok){
                alert('해당 주문을 환불완료처리 하였습니다.');
                location.reload();
            }
            else{
                alert('error1');
            }
        });
    });
}

const orderCancelButton = document.getElementById('orderCancel-btn');

if(orderCancelButton){
    orderCancelButton.addEventListener('click', () => {
        let body = JSON.stringify({
            orderId : document.getElementById('orderId').value
        });

        httpRequest(`/api/orderCancel`, 'POST', body)
        .then(response => {
            if(response.ok){
                alert('해당 주문을 주문취소처리 하였습니다.');
                location.reload();
            }
            else{
                alert('error1');
            }
        });
    })
}


//원화
if(document.getElementsByClassName('price')){
    Array.from(document.getElementsByClassName('price')).forEach(comp => comp.textContent = toWon(comp.textContent));
}

//헤더 fix처리
if(document.getElementById('menu')){
    document.getElementById('menu').setAttribute('style', 'width: 200px; position: fixed;');
}