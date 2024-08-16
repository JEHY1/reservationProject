function httpRequest(url, method, body){
    return fetch(url, {
        method: method,
        headers: {
            'Content-Type' : 'application/json'
        },
        body: body
    });
}

function toEssence(price){
    return parseInt(price.replaceAll(',', '').replace('원', ''));
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

const orderCheckButton = document.getElementById('orderCheck-btn');

if(orderCheckButton){
    orderCheckButton.addEventListener('click', () => {
        let body = JSON.stringify({
            orderId: document.getElementById('orderId').value,
            paymentRefundAccount: document.getElementById('paymentRefundAccountBank').value + ' ' + document.getElementById('paymentRefundAccountNumber').value,
            paymentRefundAccountName: document.getElementById('paymentRefundAccountName').value
        });

        httpRequest(`/api/payment/deposit`, 'POST', body)
        .then(response => {
            if(response.ok){
                console.log(response);
                return response.json();
            }
            else{
                alert('error1');
            }
        })
        .then(json => {
            console.log(json);
            if(json.paymentCheck === '유효한 요청이 있음'){
                alert('이전 결제확인 요청이 아직 유효함니다. (seller 미확인)');
            }
            else if(json.paymentCheck === '재요청 성공'){
                alert('결제내역 재확인을 요청하였습니다.');
            }
            else if(json.paymentCheck === '신규 결제 성공'){
                alert('결제내역 확인을 요청하였습니다.');
                location.reload();
            }
        });
    });
}

//환불버튼
const orderCancelButton = document.getElementById('orderCancel-btn');

if(orderCancelButton){
    orderCancelButton.addEventListener('click', () => {
        let body = JSON.stringify({
            orderId : document.getElementById('orderId').value
        });

        httpRequest(`/api/orderCancel`, 'POST', body)
        .then(response => {
            if(response.ok){
                alert('주문을 취소하였습니다.');
                location.reload();
            }
            else{
                alert('error1')
            }
        });

    });
}

//원화 표시
if(document.getElementsByClassName('price')){
    Array.from(document.getElementsByClassName('price')).forEach(comp => comp.textContent = toWon(comp.textContent));
}