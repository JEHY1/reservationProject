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
                alert('결제 확인요청 성공');
                location.replace('/');
            }
            else{
                alert('error1');
            }
        })

    });
}

//원화 표시
if(document.getElementsByClassName('price')){
    Array.from(document.getElementsByClassName('price')).forEach(comp => comp.textContent = toWon(comp.textContent));
}