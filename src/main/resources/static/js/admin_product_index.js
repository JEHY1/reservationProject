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

    // 체크박스 체크 후 숨김 버튼 눌렀을 때
    $("#upperLine p:nth-child(2) a:nth-child(2)").on("click", function() {

        let checkArr = [];
        $(".check:checked").each(function() {
           checkArr.push($(this).val());
        });

        // 현재 페이지의 파라메타 변수를 가져옴
        let url = new URLSearchParams(window.location.search);

        $.ajax({
            url:'/seller/product/delete',
            type : "post",
            data : {
                "productIds" : checkArr
            },
            dataType : 'text',
            success : function(data) {
                if (data == "delete") {
                    // 마지막 페이지 일 때
                    if($("#pageBox > a:last-child").prop("href") == "javascript:void(0)") {
                        // 싹다 체크 됐을 때 전 페이지로
                        location.href = "/seller/product/index?isDel=1&page=" + ($(".check:checked").length == $(".check").length ? url.get("page") - 1 : url.get("page")) + "&status=" + url.get("status") + "&mainCategory=" + url.get("mainCategory") + "&subCategory=" + url.get("subCategory") + "&searchKeyword=" + url.get("searchKeyword");
                    }
                    else {
                        location.href = "/seller/product/index?isDel=1&page=" + url.get("page") + "&status=" + url.get("status") + "&mainCategory=" + url.get("mainCategory") + "&subCategory=" + url.get("subCategory") + "&searchKeyword=" + url.get("searchKeyword");
                    }
                }
                else if (data == "null") {
                    location.href = "/seller/product/index?isDel=0&page=" + url.get("page") + "&status=" + url.get("status") + "&mainCategory=" + url.get("mainCategory") + "&subCategory=" + url.get("subCategory") + "&searchKeyword=" + url.get("searchKeyword");
                }
            }
        });
    });

    let url = new URLSearchParams(window.location.search); // 현재 페이지의 파라메타 변수를 가져옴
    // 숨김창 떴을 때 페이지 갱신없이 주소 URL 변경
    if(url.get("isDel") == 1 || url.get("isDel") == 0) {
        history.pushState(null, null, "/seller/product/index?page=" + url.get("page") + "&status=" + url.get("status") + "&mainCategory=" + url.get("mainCategory") + "&subCategory=" + url.get("subCategory") + "&searchKeyword=" + url.get("searchKeyword"));
    }
    // url 파라미터 없을 때 페이지 갱신없이 주소 URL 변경
    if(url.get("page") == null) {
        history.pushState(null, null, "/seller/product/index?page=0&status=정상&mainCategory=&subCategory=&searchKeyword=");
    }
    // 검색한 searchKeyword -> URL 로 보내기
    $("#selectBox > button").on("click", function() {
        location.href = "/seller/product/index?page=0&status=" + $("#selectBox > select:nth-of-type(1)").val() + "&mainCategory=" + $("#selectBox > select:nth-of-type(2)").val() + "&subCategory=" + $("#selectBox > select:nth-of-type(3)").val() + "&searchKeyword=" + $("#selectBox > input").val();
    });
    // Enter 쳤을 때 검색 버튼 클릭 되게 하기
    $("#selectBox > input").on("keyup",function(key){
        if(key.keyCode==13) {
            $("#selectBox > button").trigger('click');
        }
    });
    // status, mainCategory, subCategory 선택 시 -> URL 로 보내기
    $("#selectBox > select").on("change", function() {
        location.href = "/seller/product/index?page=0&status=" + $("#selectBox > select:nth-of-type(1)").val() + "&mainCategory=" + $("#selectBox > select:nth-of-type(2)").val() + "&subCategory=" + $("#selectBox > select:nth-of-type(3)").val() + "&searchKeyword=" + $("#selectBox > input").val();
    });

    // 천단위 콤마
    for(let i = 1; i <= $("tr").length; i++) {
        let price = $("tr:nth-child(" + i + ") > td:nth-of-type(9)").text();
        let sales = $("tr:nth-child(" + i + ") > td:nth-of-type(10)").text();
        let count = $("tr:nth-child(" + i + ") > td:nth-of-type(11)").text();
        $("tr:nth-child(" + i + ") > td:nth-of-type(9)").text(price.replace(/\B(?=(\d{3})+(?!\d))/g, ",") + '원');
        // '할인가'가 있을 때
        if(sales != '-') {
            $("tr:nth-child(" + i + ") > td:nth-of-type(10)").text(sales.replace(/\B(?=(\d{3})+(?!\d))/g, ",") + '원');
        }
        $("tr:nth-child(" + i + ") > td:nth-of-type(11)").text(count.replace(/\B(?=(\d{3})+(?!\d))/g, ","));
    }

});


