$(function() {
    // notice 닫기
    $("#notice > div > a").on("click", function() {
        $("#notice").css("display","none");
    });
});

//검색기능
const searchBar = document.getElementById('search');

if(searchBar){
    searchBar.addEventListener('focus', () => document.getElementById('searchedField').classList.remove('d-hidden'));
    searchBar.addEventListener('blur', () => {
        wait(100)
        .then(() => document.getElementById('searchedField').classList.add('d-hidden'));
    });

    searchBar.addEventListener('keydown', (event) => {
        if (event.key === 'Enter') {
            // Enter 키가 눌렸을 때 수행할 동작
            event.preventDefault(); // 기본 Enter 동작을 막습니다 (필요 시)
            console.log(event.key);
            document.getElementById('productSearch-btn').click();
        }
    });

    searchBar.addEventListener('input', () => {

        if(searchBar.value !== ''){
            let body = JSON.stringify({
                productName : searchBar.value
            });

            httpRequest(`/getIncludeNameList`, 'POST', body)
            .then(response => {
                if(response.ok){
                    return response.json();
                }
            })
            .then(data => {

                removeAllChildNodes(document.getElementById('searchedField'));
                console.log(data);
                data.forEach(result => {
                    let fullText = document.createElement('div');
                    fullText.setAttribute('class', 'row mx-auto ps-2 productNameField');
                    fullText.setAttribute('onclick', 'getProductName(this)');

                    let frontText = document.createElement('div');
                    frontText.setAttribute('class', 'col-auto p-0');
                    frontText.textContent = result.frontText;

                    let searchText = document.createElement('div');
                    searchText.setAttribute('class', 'col-auto p-0 color-blue');
                    searchText.textContent = result.searchText;

                    let endText = document.createElement('div');
                    endText.setAttribute('class', 'col-auto p-0');
                    endText.textContent = result.endText;

                    document.getElementById('searchedField').appendChild(fullText);
                    fullText.appendChild(frontText);
                    fullText.appendChild(searchText);
                    fullText.appendChild(endText);

                });

            });
        }
        else{
            removeAllChildNodes(document.getElementById('searchedField'));
        }
    });
}

const productSearchButton = document.getElementById('productSearch-btn');

if(productSearchButton){
    productSearchButton.addEventListener('click', () => {
        location.href = '/product/productGroup?searchText=' + document.getElementById('search').value;
    });
}

function wait(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
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

function removeAllChildNodes(parent) {
    while (parent.firstChild) {
        parent.removeChild(parent.firstChild);
    }
}

function getProductName(Comp){
    document.getElementById('searchBar').value = Comp.children[0].textContent + Comp.children[1].textContent + Comp.children[2].textContent;
    document.getElementById('searchedField').classList.add('d-hidden');
}