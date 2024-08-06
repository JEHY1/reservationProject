function httpRequest(url, method, body){
    return fetch(url, {
        method: method,
        headers: {
            "Content-Type" : "application/json"
        },
        body: body
    });
}

function removeAllChild(comp){
    while(comp.children[0]){
        comp.children[0].remove();
    }
}

function makeCalendar(year, month){
    let specificDate = new Date(year + '-' + month + '-01');
    let startWeekOfDay = specificDate.getDay();
    let startDay = parseInt(document.getElementById('startDay').value);
    let endDay = parseInt(document.getElementById('endDay').value);
    let today = parseInt(document.getElementById('today').value);

    if(month === 2){
        if((year % 4 === 0 && year % 100 !== 0) || year % 400 === 0){
            maxDay = 29;
        }
        else{
            maxDay = 28;
        }
    }
    else if(month <= 7 && month % 2 === 1 || month > 7 && month % 2 === 0){
        maxDay = 31
    }
    else{
        maxDay = 30;
    }

    let week = document.createElement('div');
    week.setAttribute('class', 'row w-100 mx-auto mt-3');

    for(let blankDay = 0; blankDay < startWeekOfDay; blankDay++){
        let blankDayComp = document.createElement('div');
        blankDayComp.setAttribute('class', 'col-auto blankDay mx-auto');
        week.append(blankDayComp);
    }

    for(let day = 1; day <= maxDay; day++){
        let dayComp = document.createElement('div');
        dayComp.setAttribute('class', 'col-auto mx-auto day text-center cursor');
        if(startWeekOfDay === 6){
            dayComp.classList.add('color-blue');
        }
        else if(startWeekOfDay === 0){
            dayComp.classList.add('color-red');
        }

        let date = parseInt(year + month + (day < 10 ? '0' + day : day));

        if(date > today && date >= startDay && date <= endDay){
            dayComp.classList.add('selectable');
            dayComp.setAttribute('onclick', 'selectDate(this)');

        }
        else{
            dayComp.classList.add('disabled');
        }

        if('' + year + month + (day < 10 ? '0' + day : day) === document.getElementById('selectedDate').value){
            dayComp.classList.add('selected');
        }

        dayComp.textContent = day;
        week.append(dayComp);
        startWeekOfDay++;

        if(startWeekOfDay === 7){
            dayField.appendChild(week);
            week = document.createElement('div');
            week.setAttribute('class', 'row w-100 mx-auto mt-3');
            startWeekOfDay = 0;
        }
    }

    if(startWeekOfDay !== 0){

        for(let blankDay = 0; blankDay < 7 - startWeekOfDay; blankDay++){
            let blankDayComp = document.createElement('div');
            blankDayComp.setAttribute('class', 'col-auto blankDay mx-auto');
            week.append(blankDayComp);
        }
        dayField.appendChild(week);
    }


}

function selectDate(comp){
    Array.from(document.getElementsByClassName('options')).forEach(option => {
        option.classList.remove('d-hidden');
    });

    if(document.getElementById('dateSelectNote')){
        document.getElementById('dateSelectNote').remove();
    }

    document.getElementById('totalPrice').textContent = '0원';

    if(comp !== document.getElementsByClassName('selected')[0]){
        if(document.getElementsByClassName('selected')[0]){
            document.getElementsByClassName('selected')[0].classList.remove('selected');
        }
        document.getElementById('selectedDate').value = document.getElementById('year').value + document.getElementById('month').value + (comp.textContent < 10 ? '0' + comp.textContent : comp.textContent);
        comp.classList.add('selected');

        if(document.getElementsByClassName('innerOptionField')[0]){
            document.getElementsByClassName('innerOptionField')[0].remove();
        }
    }

    let body = JSON.stringify({
        productId : document.getElementById('productId').value,
        year : parseInt(document.getElementById('selectedDate').value.substring(0,4)),
        month : parseInt(document.getElementById('selectedDate').value.substring(4,6)),
        day : parseInt(document.getElementById('selectedDate').value.substring(6,8))
    });

    httpRequest(`/api/getStock`, 'POST', body)
    .then(response => {
        if(response.ok){
            return response.json();
        }
        else{
            alert('error1');
        }
    })
    .then(json => {
        console.log(json);
        document.getElementById('selectedDateBox').textContent = json.date.substring(0, 10) + ' (수량 : ' +json.count + ')';
        document.getElementById('selectableCount').value = json.count;
    });
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

function toEssence(price){
    return parseInt(price.replaceAll(',', '').replace('원', ''));
}

const dayField = document.getElementById('dayField');

if(dayField){
    makeCalendar(parseInt(document.getElementById('year').value), document.getElementById('month').value);
}

const nextMonthButton = document.getElementById('nextMonth-btn');

if(nextMonthButton){
    nextMonthButton.addEventListener('click', () => {
        removeAllChild(dayField);
        let monthInput = document.getElementById('month');
        let yearInput = document.getElementById('year');

        if(month.value === '12'){
            yearInput.value = parseInt(yearInput.value) + 1;
            monthInput.value = '01';
        }
        else{
            monthInput.value = parseInt(monthInput.value) + 1 < 10 ? '0' + (parseInt(monthInput.value) + 1) : parseInt(monthInput.value) + 1;
        }

        nextMonthButton.previousElementSibling.textContent = yearInput.value + '년 ' + monthInput.value + '월';
        makeCalendar(parseInt(yearInput.value), monthInput.value);

        let time = nextMonthButton.previousElementSibling.textContent;

    });
}

const prevMonthButton = document.getElementById('prevMonth-btn');

if(prevMonthButton){
    prevMonthButton.addEventListener('click', () => {
        removeAllChild(dayField);
        let monthInput = document.getElementById('month');
        let yearInput = document.getElementById('year');

        if(month.value === '01'){
            yearInput.value = parseInt(yearInput.value) - 1;
            monthInput.value = '12';
        }
        else{
            monthInput.value = parseInt(monthInput.value) - 1 < 10 ? '0' + (parseInt(monthInput.value) - 1) : parseInt(monthInput.value) - 1;
        }

        nextMonthButton.previousElementSibling.textContent = yearInput.value + '년 ' + monthInput.value + '월';
        makeCalendar(parseInt(yearInput.value), monthInput.value);

        let time = nextMonthButton.previousElementSibling.textContent;

    });
}

const productInfoTabButton = document.getElementById('productInfoTab-btn')

if(productInfoTabButton){
    productInfoTabButton.addEventListener('click', () => {
        document.getElementsByClassName('selectedTabMenu')[0].classList.remove('selectedTabMenu');
        productInfoTabButton.classList.add('selectedTabMenu');
        document.getElementsByClassName('viewTab')[0].classList.add('d-hidden');
        document.getElementsByClassName('viewTab')[0].classList.remove('viewTab');
        document.getElementsByClassName('productInfoTab')[0].classList.remove('d-hidden');
        document.getElementsByClassName('productInfoTab')[0].classList.add('viewTab');
    });
}

const reviewTabButton = document.getElementById('reviewTab-btn');

if(reviewTabButton){
    reviewTabButton.addEventListener('click', () => {
        document.getElementsByClassName('selectedTabMenu')[0].classList.remove('selectedTabMenu');
        reviewTabButton.classList.add('selectedTabMenu');
        document.getElementsByClassName('viewTab')[0].classList.add('d-hidden');
        document.getElementsByClassName('viewTab')[0].classList.remove('viewTab');
        document.getElementsByClassName('productReviewTap')[0].classList.remove('d-hidden');
        document.getElementsByClassName('productReviewTap')[0].classList.add('viewTab');
    });
}

const qnaTabButton = document.getElementById('qnaTab-btn');

if(qnaTabButton){
    qnaTabButton.addEventListener('click', () => {
        document.getElementsByClassName('selectedTabMenu')[0].classList.remove('selectedTabMenu');
        qnaTabButton.classList.add('selectedTabMenu');
        document.getElementsByClassName('viewTab')[0].classList.add('d-hidden');
        document.getElementsByClassName('viewTab')[0].classList.remove('viewTab');
        document.getElementsByClassName('qnaTab')[0].classList.remove('d-hidden');
        document.getElementsByClassName('qnaTab')[0].classList.add('viewTab');
    });
}

const optionSelection = document.getElementById('option');

if(optionSelection){
    optionSelection.addEventListener('click', () => {

        if(document.getElementById('optionBox').classList.contains('d-hidden')){
            document.getElementById('optionBox').classList.remove('d-hidden');
        }
        else{
            document.getElementById('optionBox').classList.add('d-hidden');
        }
    });
}

const options = document.getElementsByClassName('options');

if(options){
    Array.from(options).forEach(option => {
        option.addEventListener('click', () => {

            let sum = 0;
            Array.from(document.getElementsByClassName('selectedCount')).forEach(count => sum += parseInt(count.value))
            console.log('sum : ' + sum);

            if(sum >= parseInt(document.getElementById('selectableCount').value)){
                alert('수량 초과');
                return;
            }

            if(document.getElementById('selectedOption' + option.children[0].value)){
                console.log('valid');
                let selectedOption = document.getElementById('selectedOption' + option.children[0].value);
                let countComp = selectedOption.children[1].children[0].children[0];
                countComp.textContent = parseInt(countComp.textContent) + 1;
                let priceComp = selectedOption.children[2];
                priceComp.textContent = toWon(toEssence(priceComp.textContent) + parseInt(option.children[1].value));

                let selectedOptionCountComp = document.getElementById('selectedOptionCount' + option.children[0].value);
                selectedOptionCountComp.value = parseInt(selectedOptionCountComp.value) + 1;
            }
            else{
                console.log('unValid');
                let comp1 = document.createElement('div');
                comp1.setAttribute('class', 'row w-100 mx-auto py-3');
                comp1.setAttribute('id', 'selectedOption' + option.children[0].value);

                let comp2 = document.createElement('div');
                comp2.setAttribute('class', 'col-6 d-flex align-items-center');
                comp2.textContent = option.children[2].textContent;

                let comp3 = document.createElement('div');
                comp3.setAttribute('class', 'col-3');

                let comp4 = document.createElement('div');
                comp4.setAttribute('class', 'row w-100 mx-auto');

                let comp5 = document.createElement('div');
                comp5.setAttribute('class', 'col-9 d-flex align-items-center border bg-white');
                comp5.textContent = 1;

                let comp6 = document.createElement('div');
                comp6.setAttribute('class', 'col-3 p-0 border cursor');

                let comp9 = document.createElement('div');
                comp9.setAttribute('class', 'col d-flex align-items-center');
                console.log(option.children[1].value);
                comp9.textContent = toWon(option.children[1].value);

                let comp7 = document.createElement('div');
                comp7.setAttribute('class', 'text-center');
                comp7.textContent = '▵';
                comp7.addEventListener('click', () => {
                    let sum = 0;
                    Array.from(document.getElementsByClassName('selectedCount')).forEach(count => sum += parseInt(count.value))
                    console.log('sum : ' + sum);

                    if(sum >= parseInt(document.getElementById('selectableCount').value)){
                        alert('수량 초과');
                        return;
                    }

                    comp5.textContent = parseInt(comp5.textContent) + 1;
                    comp9.textContent = toWon(toEssence(comp9.textContent) + parseInt(option.children[1].value));
                    document.getElementById('totalPrice').textContent = toWon(toEssence(document.getElementById('totalPrice').textContent) + parseInt(option.children[1].value)); //최종금액 변경

                    let selectedOptionCountComp = document.getElementById('selectedOptionCount' + option.children[0].value);
                    selectedOptionCountComp.value = parseInt(selectedOptionCountComp.value) + 1;
                });

                let comp8 = document.createElement('div');
                comp8.setAttribute('class', 'text-center');
                comp8.textContent = '▿';
                comp8.addEventListener('click', () => {
                    if(comp5.textContent !== '1'){
                        comp5.textContent = parseInt(comp5.textContent) - 1;
                        comp9.textContent = toWon(toEssence(comp9.textContent) - parseInt(option.children[1].value));
                        document.getElementById('totalPrice').textContent = toWon(toEssence(document.getElementById('totalPrice').textContent) - parseInt(option.children[1].value)); //최종금액 변경

                        let selectedOptionCountComp = document.getElementById('selectedOptionCount' + option.children[0].value);
                        selectedOptionCountComp.value = parseInt(selectedOptionCountComp.value) - 1;
                    }
                    else{
                        alert('최소 수량 1개입니다.');
                    }
                });

                let comp10 = document.createElement('div');
                comp10.setAttribute('class', 'col-auto ps-0 cursor d-flex align-items-center');
                comp10.textContent = 'X';
                comp10.addEventListener('click', () => {
                    comp10.parentElement.remove();
                    console.log(toEssence(comp10.previousElementSibling.textContent));
                    document.getElementById('totalPrice').textContent = toWon(toEssence(document.getElementById('totalPrice').textContent) - toEssence(comp9.textContent)); //최종금액 변경

                    //form에서 삭제
                    document.getElementById('selectedOption' + option.children[0].value).remove();
                    document.getElementById('selectedOptionCount' + option.children[0].value).remove();


                    if(document.getElementsByClassName('innerOptionField')[0].children.length === 0){
                        document.getElementsByClassName('innerOptionField')[0].remove();
                    }

                });

                comp1.appendChild(comp2);
                comp1.appendChild(comp3);
                comp3.appendChild(comp4);
                comp4.appendChild(comp5);
                comp4.appendChild(comp6);
                comp6.appendChild(comp7);
                comp6.appendChild(comp8);
                comp1.appendChild(comp9);
                comp1.appendChild(comp10);

                if(document.getElementsByClassName('innerOptionField').length === 0){
                    let comp11 = document.createElement('div');
                    comp11.setAttribute('class', 'innerOptionField mt-4');
                    document.getElementById('optionField').appendChild(comp11);
                }
                document.getElementsByClassName('innerOptionField')[0].appendChild(comp1);

                //구매 form 에 추가하기
                let comp12 = document.createElement('input');
                comp12.setAttribute('name', 'optionId');
                comp12.setAttribute('type', 'hidden');
                comp12.setAttribute('id', 'selectedOption' + option.children[0].value);
                comp12.value = option.children[0].value;

                let comp13 = document.createElement('input');
                comp13.setAttribute('name', 'count');
                comp13.setAttribute('type', 'hidden');
                comp13.setAttribute('id', 'selectedOptionCount' + option.children[0].value);
                comp13.setAttribute('class', 'selectedCount');
                comp13.value = 1;

                document.getElementById('orderForm').appendChild(comp12);
                document.getElementById('orderForm').appendChild(comp13);
            }
            document.getElementById('totalPrice').textContent = toWon(toEssence(document.getElementById('totalPrice').textContent) + parseInt(option.children[1].value)); //최종금액 변경
        });
    });
}

const subImg = document.getElementsByClassName('subImgList');

if(subImg){
    Array.from(subImg).forEach(img => {
        img.addEventListener('click', () => {
            console.log(img);
            document.getElementById('repImg').setAttribute('src', img.src);
        });
    });
}

const reviewContent = document.getElementsByClassName('review');

if(reviewContent){
    Array.from(reviewContent).forEach(review => {
        review.addEventListener('click', () => {
            if(review.children[1].classList.contains('d-hidden')){
                review.children[1].classList.remove('d-hidden');
                if(document.getElementsByClassName('view')[0]){
                    document.getElementsByClassName('view')[0].classList.add('d-hidden');
                    document.getElementsByClassName('view')[0].classList.remove('view');
                }
                review.children[1].classList.add('view');
            }
            else{
                review.children[1].classList.add('d-hidden');
                review.children[1].classList.remove('view');
            }
        });
    });
}

const qnaContent = document.getElementsByClassName('qna');

if(qnaContent){
    Array.from(qnaContent).forEach(qna => {
        qna.addEventListener('click', () => {
            if(qna.children[1].classList.contains('d-hidden')){
                qna.children[1].classList.remove('d-hidden');
                if(document.getElementsByClassName('view')[0]){
                    document.getElementsByClassName('view')[0].classList.add('d-hidden');
                    document.getElementsByClassName('view')[0].classList.remove('view');
                }
                qna.children[1].classList.add('view');
            }
            else{
                qna.children[1].classList.add('d-hidden');
                qna.children[1].classList.remove('view');
            }
        });
    });
}

//초기값 설정
if(document.getElementById('discountPercentage')){
    let percentage = document.getElementById('discountPercentage');
    percentage.textContent = 100 - parseInt(parseInt(percentage.previousElementSibling.textContent) / parseInt(percentage.previousElementSibling.previousElementSibling.children[0].textContent) * 100) + '%';
}
Array.from(document.getElementsByClassName('price')).forEach(price => price.textContent = toWon(price.textContent));

console.log(toEssence('1,000,000원'));
