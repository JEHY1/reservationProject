function httpRequest(url, method, body){
    return fetch(url, {
        method: method,
        headers: {
            "Content-Type" : "application/json"
        },
        body: body
    });
}

function prevElementClick(comp){
    comp.previousElementSibling.click();
}

function toList(comps){
    let list = [];

    Array.from(comps).forEach(comp => {
        console.log(comp);
        let value = comp.textContent !== '' ? comp.textContent : comp.value;
        list.push(toEssence(value));
    });

    return list;
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

const cardPayCheckBox = document.getElementById('cardPay');

if(cardPayCheckBox){
    cardPayCheckBox.addEventListener('click', () => {
        document.getElementById('bankTransferInfo').classList.add('d-hidden');
    });
}

const bankTransferPayCheckBox = document.getElementById('bankTransferPay');

if(bankTransferPayCheckBox){
    bankTransferPayCheckBox.addEventListener('click', () => {
        document.getElementById('bankTransferInfo').classList.remove('d-hidden');
    });
}

//주문 버튼
const orderButton = document.getElementById('order-btn');

if(orderButton){
    orderButton.addEventListener('click', () => {
        console.log('click');
        let body = JSON.stringify({
            productId : document.getElementById('productId').value,
            orderDepartDate : document.getElementById('departDate').value,
            optionList : toList(document.getElementsByClassName('optionId')),
            countList : toList(document.getElementsByClassName('count')),
            totalOptionRegularPriceList : toList(document.getElementsByClassName('totalOptionRegularPrice')),
            totalOptionDiscountPriceList : toList(document.getElementsByClassName('totalOptionDiscountPrice')),
            totalPrice : toEssence(document.getElementById('totalPrice').textContent),
            userRealName : document.getElementById('realName').value,
            userPhone : document.getElementById('userPhone1').value + '-' + document.getElementById('userPhone2').value + '-' + document.getElementById('userPhone3').value,
            userEmail : document.getElementById('userEmail').value,
            paymentType : document.getElementById('bankTransferPay').checked ? '무통장입금' : '카드결제',
            account : document.getElementById('bankTransferPay').checked ? document.getElementById('account').value : '',
            depositor : document.getElementById('bankTransferPay').checked ? document.getElementById('depositor').value : ''
        });

        httpRequest(`/api/order`, 'POST', body)
        .then(response => {
            if(response.ok){
                return response.json();
            }
            else{
                alert('error1');
            }
        })
        .then(json => {
            alert('주문이 완료되었습니다.');
            console.log(json);
            location.replace('/orderDetail/' + json.orderId);
        })

    });
}

//원화 설정
Array.from(document.getElementsByClassName('price')).forEach(comp => comp.textContent = toWon(comp.textContent));