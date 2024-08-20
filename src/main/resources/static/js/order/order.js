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

function toWon(price) {
    let PriceText = '';
    price = price.toString(); // 숫자를 문자열로 변환

    while (price.length > 3) {
        // 뒤에서 세 자리씩 끊어서 콤마와 함께 추가
        PriceText = ',' + price.substring(price.length - 3) + PriceText;
        price = price.substring(0, price.length - 3);
    }

    // 남아있는 숫자 부분과 함께 "원"을 붙여 반환
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

        if(document.getElementById('bankTransferPay').checked){
            if(document.getElementById('depositor').value === ''){
                alert('입금자명을 입력해주세요.');
                // alert 창이 닫힌 후에 실행되도록 setTimeout으로 지연
                setTimeout(() => {
                    let depositorInput = document.getElementById('depositor');
                    // 스크롤을 해당 요소로 이동
                    depositorInput.scrollIntoView({ behavior: 'smooth', block: 'center' });
                    // 포커스를 입력 필드로 이동
                    depositorInput.focus();
                }, 0);
                return ;
            }
        }

        if(!document.getElementById('refundAgreement').checked){
            alert('환불 정책 약관에 동의해주세요.');
            setTimeout(() => {
                document.getElementById('refundAgreement').scrollIntoView({ behavior: 'smooth', block: 'center' });
            }, 0);
            return ;
        }
        if(!document.getElementById('personalInfoAgreement').checked){
            alert('개인정보 이용약관에 동의해주세요.');
            return ;
        }

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

//유효성 검사
if(document.getElementsByClassName('userInfo')){
    let phone2Comp = document.getElementById('userPhone2');
    let phone3Comp = document.getElementById('userPhone3');
    phone2Comp.addEventListener('input', () => {
        let regex = /^[0-9]{0,4}$/;
        if(!regex.test(phone2Comp.value)){
            phone2Comp.value = phone2Comp.value.substring(0, phone2Comp.value.length - 1);
        }
    });

    phone3Comp.addEventListener('input', () => {
        let regex = /^[0-9]{0,4}$/;
        if(!regex.test(phone3Comp.value)){
            phone3Comp.value = phone3Comp.value.substring(0, phone3Comp.value.length - 1);
        }
    });
}

//원화 설정
Array.from(document.getElementsByClassName('price')).forEach(comp => comp.textContent = toWon(comp.textContent));