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
            pageButton.setAttribute('onclick', "location.href='/orderList?" + param + "&page=" + (currentPageGroupNum * 5 + i) + "'");
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
    prevPageButton.setAttribute('onclick', "location.href='/orderList?" + param + "&page=" + (parseInt(getUrlParameter('page')) - 1) + "'");
}

const nextPageButton = document.getElementById('nextPage-btn');

if(nextPageButton){
    let param = '';
//    param += getUrlParameter("unitPeriod") !== null ? '&unitPeriod=' + getUrlParameter("unitPeriod") : '';
//    param += getUrlParameter("startDate") !== null ? '&startDate=' + getUrlParameter("startDate") : '';
//    param += getUrlParameter("endDate") !== null ? '&endDate=' + getUrlParameter("endDate") : '';
    nextPageButton.setAttribute('onclick', "location.href='/orderList?" + param + "&page=" + (parseInt(getUrlParameter('page') === null ? 0 : parseInt(getUrlParameter('page'))) + 1) + "'");
}

//넘버링
if(document.getElementsByClassName('num')){
    let pageNum = getUrlParameter('page');
    pageNum === null ? 0 : pageNum;
    Array.from(document.getElementsByClassName('num')).forEach((comp, index) => {
        comp.textContent = pageNum * 10 + index + 1;
    });
}

//원화표시
if(document.getElementsByClassName('price')){
    Array.from(document.getElementsByClassName('price')).forEach(comp => comp.textContent = toWon(comp.textContent));
}
