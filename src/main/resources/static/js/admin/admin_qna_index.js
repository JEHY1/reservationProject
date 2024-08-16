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

    // 현재 페이지의 파라메타 변수를 가져옴
    let url = new URLSearchParams(window.location.search);
    // 유저의 권한 가져옴
    let auth = $("#auth").val();

    // 체크박스 체크 후 삭제 버튼 눌렀을 때
    $("#upperLine p:nth-child(2) a:last-child").on("click", function() {

        let checkArr = [];
        $(".check:checked").each(function() {
           checkArr.push($(this).val());
        });

        $.ajax({
            url: '/' + auth + '/qna/delete',
            type : "post",
            data : {
                "qnaIds" : checkArr
            },
            dataType : 'text',
            success : function(data) {
                if (data == "delete") {
                    // 마지막 페이지 일 때
                    if($("#pageBox > a:last-child").prop("href") == "javascript:void(0)") {
                        // 싹다 체크 됐을 때 전 페이지로
                        location.href = "/" + auth + "/qna/index?isDel=1&page=" + ($(".check:checked").length == $(".check").length ? url.get("page") - 1 : url.get("page")) + "&group=" + url.get("group") + "&category=" + url.get("category") + "&searchKeyword=" + url.get("searchKeyword");
                    }
                    else {
                        location.href = "/" + auth + "/qna/index?isDel=1&page=" + url.get("page") + "&group=" + url.get("group") + "&category=" + url.get("category") + "&searchKeyword=" + url.get("searchKeyword");
                    }
                }
                else if (data == "null") {
                    location.href = "/" + auth + "/qna/index?isDel=0&page=" + url.get("page") + "&group=" + url.get("group") + "&category=" + url.get("category") + "&searchKeyword=" + url.get("searchKeyword");
                }
            }
        });
    });

    // 삭제창 떴을 때 페이지 갱신없이 주소 URL 변경
    if(url.get("isDel") == 1 || url.get("isDel") == 0) {
        history.pushState(null, null, "/" + auth + "/qna/index?page=" + url.get("page") + "&group=" + url.get("group") + "&category=" + url.get("category") + "&searchKeyword=" + url.get("searchKeyword"));
    }
    // url 파라미터 없을 때 페이지 갱신없이 주소 URL 변경
    if(url.get("page") == null) {
        history.pushState(null, null, "/" + auth + "/qna/index?page=0&group=1&category=1&searchKeyword=");
    }
    // 검색한 searchKeyword -> URL 로 보내기
    $("#selectBox > button").on("click", function() {
        location.href = "/" + auth + "/qna/index?page=0&group=" + $("#selectBox > select:nth-of-type(1)").val() + "&category=" + $("#selectBox > select:nth-of-type(2)").val() + "&searchKeyword=" + $("#selectBox > input").val();
    });
    // Enter 쳤을 때 검색 버튼 클릭 되게 하기
    $("#selectBox input").on("keyup",function(key){
        if(key.keyCode==13) {
            $("#selectBox > button").trigger('click');
        }
    });
    // group 선택 시 -> URL 로 보내기
    $("#selectBox > select:nth-of-type(1)").on("change", function() {
        location.href = "/" + auth + "/qna/index?page=0&group=" + $("#selectBox > select:nth-of-type(1)").val() + "&category=" + $("#selectBox > select:nth-of-type(2)").val() + "&searchKeyword=" + $("#selectBox > input").val();
    });


});


