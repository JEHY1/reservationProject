function getUrlParameter(name) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(name);
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

function checkedToList(compList){
    let list = [];
    Array.from(compList).forEach(comp => {
        console.log(comp);
        if(comp.checked){
            list.push(comp.value);
        }
    });

    return list;
}

const writeableReviewTab = document.getElementById('writableReviews');
const writtenReviewTab = document.getElementById('writtenReviews');

if(writeableReviewTab && writtenReviewTab){
    console.log(getUrlParameter('tab'));

    if(getUrlParameter('tab') !== '1'){
        writtenReviewTab.classList.add('d-hidden');
        document.getElementById('currentTab').value = 0;
        document.getElementById('showWrittenReview-btn').setAttribute('class', 'border-t-gray border-r-gray border-b-green py-10 color-gray cursor');
        document.getElementById('showWritableReview-btn').setAttribute('class', 'border-l-green border-t-green border-r-green py-10 color-green cursor');
    }
    else{
        writeableReviewTab.classList.add('d-hidden');
        document.getElementById('currentTab').value = 1;
        document.getElementById('showWritableReview-btn').setAttribute('class', 'border-l-gray border-t-gray border-b-green border-r-green py-10 color-gray cursor');
        document.getElementById('showWrittenReview-btn').setAttribute('class', 'border-t-green border-r-green color-green py-10 cursor');
    }
}

const showWritableReviewButton = document.getElementById('showWritableReview-btn');

if(showWritableReviewButton){
    showWritableReviewButton.addEventListener('click', () => {
        console.log('click');
        writeableReviewTab.classList.remove('d-hidden');
        writtenReviewTab.classList.add('d-hidden');
        document.getElementById('currentTab').value = 0;

        document.getElementById('showWrittenReview-btn').setAttribute('class', 'border-t-gray border-r-gray border-b-green py-10 color-gray cursor');
        document.getElementById('showWritableReview-btn').setAttribute('class', 'border-l-green border-t-green border-r-green py-10 color-green cursor');
    });
}

const showWrittenReviewButton = document.getElementById('showWrittenReview-btn');

if(showWrittenReviewButton){
    showWrittenReviewButton.addEventListener('click', () => {
        console.log('click');
        writeableReviewTab.classList.add('d-hidden');
        writtenReviewTab.classList.remove('d-hidden');
        document.getElementById('currentTab').value = 1;

        document.getElementById('showWritableReview-btn').setAttribute('class', 'border-l-gray border-t-gray border-b-green border-r-green py-10 color-gray cursor');
        document.getElementById('showWrittenReview-btn').setAttribute('class', 'border-t-green border-r-green color-green py-10 cursor');
    });
}

const deleteButtonList = document.getElementsByClassName('delete-btn');

if(deleteButtonList){
    Array.from(deleteButtonList).forEach(button => {
        button.addEventListener('click', () => {
            console.log('click');

            console.log(button.parentElement.parentElement.children[0].children[0].value);

            let body = JSON.stringify({
                reviewId : button.parentElement.parentElement.children[0].children[0].value
            });

            httpRequest(`/api/myPage/review`, 'DELETE', body)
            .then(response => {
                if(response.ok){
                    alert('리뷰가 삭제되었습니다.');
                    location.reload();
                }
                else{
                    alert('error1');
                }
            });
        });
    });
}

const selectedDeleteButton = document.getElementById('selectedDelete-btn')

if(selectedDeleteButton){
    selectedDeleteButton.addEventListener('click', () => {
        console.log(checkedToList(document.getElementsByClassName('checked')));

        let body = JSON.stringify({
            reviewIdList : checkedToList(checkedToList(document.getElementsByClassName('checked')))
        });
    });
}



//페이지
const writablePageButtonField = document.getElementById('writableReviewsPageButtonField');

if(writablePageButtonField){
    let currentPageNum = getUrlParameter('page1') === null ? 0 : parseInt(getUrlParameter("page1"));
    let totalPageSize = parseInt(document.getElementById('writableReviewTotalPageSize').value);
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
        param += '&tab=0';
        param += getUrlParameter("page2") !== null ? '&page2=' + getUrlParameter("page2") : '';

        if(currentPageNum + 1 === currentPageGroupNum  * 5 + i + 1){
            pageButton.classList.add('selectedPage-btn');
        }
        else{
            pageButton.setAttribute('onclick', "location.href='/myReviews?" + param + "&page1=" + (currentPageGroupNum * 5 + i) + "'");
        }

        writablePageButtonField.appendChild(divWrap);
        divWrap.appendChild(pageButton);

        if(currentPageNum === 0){
            document.getElementById('writablePrevPage-btn').setAttribute('disabled', '');
        }

        if(currentPageNum === totalPageSize - 1){
            document.getElementById('writableNextPage-btn').setAttribute('disabled', '');
        }
    }
}

const writablePrevPageButton = document.getElementById('writablePrevPage-btn');

if(writablePrevPageButton){
    let param = '';
    param += getUrlParameter("page2") !== null ? '&page2=' + getUrlParameter("page2") : '';
    param += '&tab=0';
    writablePrevPageButton.setAttribute('onclick', "location.href='/myReviews?" + param + "&page1=" + (parseInt(getUrlParameter('page1')) - 1) + "'");
}

const writableNextPageButton = document.getElementById('writableNextPage-btn');

if(writableNextPageButton){
    let param = '';
    param += getUrlParameter("page2") !== null ? '&page2=' + getUrlParameter("page2") : '';
    param += '&tab=0';
    writableNextPageButton.setAttribute('onclick', "location.href='/myReviews?" + param + "&page1=" + (parseInt(getUrlParameter('page1') === null ? 0 : getUrlParameter('page1')) + 1) + "'");
}

(parseInt(getUrlParameter('page2') === null ? 0 : getUrlParameter('page2')) + 1)




const writtenPageButtonField = document.getElementById('writtenReviewsPageButtonField');

if(writtenPageButtonField){
    let currentPageNum = getUrlParameter('page2') === null ? 0 : parseInt(getUrlParameter("page2"));
    let totalPageSize = parseInt(document.getElementById('writtenReviewTotalPageSize').value);
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
        param += '&tab=1';
        param += getUrlParameter("page1") !== null ? '&page1=' + getUrlParameter("page1") : '';


        if(currentPageNum + 1 === currentPageGroupNum  * 5 + i + 1){
            pageButton.classList.add('selectedPage-btn');
        }
        else{
            pageButton.setAttribute('onclick', "location.href='/myReviews?" + param + "&page2=" + (currentPageGroupNum * 5 + i) + "'");
        }

        writtenPageButtonField.appendChild(divWrap);
        divWrap.appendChild(pageButton);

        if(currentPageNum === 0){
            document.getElementById('writtenPrevPage-btn').setAttribute('disabled', '');
        }

        if(currentPageNum === totalPageSize - 1){
            document.getElementById('writtenNextPage-btn').setAttribute('disabled', '');
        }
    }
}

const writtenPrevPageButton = document.getElementById('writtenPrevPage-btn');

if(writtenPrevPageButton){
    let param = '';
    param += getUrlParameter("page1") !== null ? '&page1=' + getUrlParameter("page1") : '';
    param += '&tab=1';
    writtenPrevPageButton.setAttribute('onclick', "location.href='/myReviews?" + param + "&page2=" + (parseInt(getUrlParameter('page2')) - 1) + "'");
}

const writtenNextPageButton = document.getElementById('writtenNextPage-btn');

if(writtenNextPageButton){
    let param = '';
    param += getUrlParameter("page1") !== null ? '&page1=' + getUrlParameter("page1") : '';
    param += '&tab=1';
    writtenNextPageButton.setAttribute('onclick', "location.href='/myReviews?" + param + "&page2=" + (parseInt(getUrlParameter('page2') === null ? 0 : getUrlParameter('page2')) + 1) + "'");
}

if(document.getElementsByClassName('writableReviewNum')){
    let writableReviewGroupNum = parseInt(getUrlParameter('page1') === null ? 0 : getUrlParameter('page1'));
    Array.from(document.getElementsByClassName('writableReviewNum')).forEach((comp, index) => {comp.textContent = 10 * writableReviewGroupNum + index + 1})
}

if(document.getElementsByClassName('writtenReviewNum')){
    let writtenReviewGroupNum = parseInt(getUrlParameter('page2') === null ? 0 : getUrlParameter('page2'));
    Array.from(document.getElementsByClassName('writtenReviewNum')).forEach((comp, index) => {comp.textContent = 10 * writtenReviewGroupNum + index + 1})
}


$(function() {
    // 전체 체크 눌렀을 때, 다 체크
    $("#all").on("click", function() {
        if($("#all").prop("checked") == true) {
            $(".check").prop("checked", true);
        }
        else {
            $(".check").prop("checked", false);
        }
    });
    // 개별 다 체크되면 전체 체크
    $(".check").on("click", function() {
        if($(".check:checked").length == $(".check").length) {
            $("#all").prop("checked", true);
        }
        else {
            $("#all").prop("checked", false);
        }
    });

    // 체크박스 체크 후 삭제 버튼 눌렀을 때
//    $("#selectedDelete-btn").on("click", function() {
//
//        let checkArr = [];
//        $(".check:checked").each(function() {
//           checkArr.push($(this).val());
//        });
//
//
//        $.ajax({
//            url:'/myReview/delete',
//            type : "post",
//            data : {
//                "reviewIds" : checkArr
//            },
//            dataType : 'text',
//            success : function(data) {
//                // page2의 파라메타 변수를 가져옴
//                let page2 = new URLSearchParams(window.location.search).get("page2");
//                if (data == "delete") {
//                    location.href = "/myReviews?&tab=1&page2=0";
//                }
//                else if (data == "null") {
//                    location.href = "/myReviews?isDel=0&tab=1&page2=0";
//                }
//            }
//        });
//    });

});


