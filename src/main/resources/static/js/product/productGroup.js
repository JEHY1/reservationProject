function getUrlParameter(name) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(name);
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
        param += getUrlParameter("searchText") !== null ? '&searchText=' + getUrlParameter("searchText") : '';
        param += getUrlParameter("mainCategory") !== null ? '&mainCategory=' + getUrlParameter("mainCategory") : '';
        param += getUrlParameter("subCategory") !== null ? '&subCategory=' + getUrlParameter("subCategory") : '';



        if(currentPageNum + 1 === currentPageGroupNum  * 5 + i + 1){
            pageButton.classList.add('selectedPage-btn');
        }
        else{
            pageButton.setAttribute('onclick', "location.href='/product?" + param + "&page=" + (currentPageGroupNum * 5 + i) + "'");
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
    param += getUrlParameter("searchText") !== null ? '&searchText=' + getUrlParameter("searchText") : '';
    param += getUrlParameter("mainCategory") !== null ? '&mainCategory=' + getUrlParameter("mainCategory") : '';
    param += getUrlParameter("subCategory") !== null ? '&subCategory=' + getUrlParameter("subCategory") : '';
    prevPageButton.setAttribute('onclick', "location.href='/product?" + param + "&page=" + (parseInt(getUrlParameter('page')) - 1) + "'");
}

const nextPageButton = document.getElementById('nextPage-btn');

if(nextPageButton){
    let param = '';
    param += getUrlParameter("searchText") !== null ? '&searchText=' + getUrlParameter("searchText") : '';
    param += getUrlParameter("mainCategory") !== null ? '&mainCategory=' + getUrlParameter("mainCategory") : '';
    param += getUrlParameter("subCategory") !== null ? '&subCategory=' + getUrlParameter("subCategory") : '';
    nextPageButton.setAttribute('onclick', "location.href='/product?" + param + "&page=" + (parseInt(getUrlParameter('page') === null ? 0 : getUrlParameter('page')) + 1) + "'");
}


//초기값 설정
//할인율 표시
if(document.getElementsByClassName('salePercent')){
    Array.from(document.getElementsByClassName('salePercent')).forEach(comp => {

        let discountPrice = parseInt(comp.previousElementSibling.textContent);
        let regularPrice = parseInt(comp.previousElementSibling.previousElementSibling.textContent);

        comp.textContent = parseInt((regularPrice - discountPrice) / regularPrice * 100) + "%";
    });
}

//원화 표시
if(document.getElementsByClassName('price')){
    Array.from(document.getElementsByClassName('price')).forEach(comp => comp.textContent = toWon(comp.textContent));
}

//이미지 없을 시 특정 이미지 표시
function checkImageExists(imgElement, fallbackSrc) {
    const img = new Image();
    img.src = imgElement.src;
    img.onload = function() {
        // 이미지가 정상적으로 로드되었으므로 아무 작업도 하지 않음
    };
    img.onerror = function() {
        // 이미지가 없으므로 대체 이미지로 설정
        imgElement.src = fallbackSrc;
    };
}

const imgs = document.getElementsByClassName('productImg');
console.log(imgs);
if(imgs){
    Array.from(imgs).forEach(img => {
        checkImageExists(img, '/images/logo_img/noimg_big.gif')
        img.classList.add('border');
    });
}

