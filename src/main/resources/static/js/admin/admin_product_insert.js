$(function() {

    // 추가 버튼 누르면 옵션 추가
    $("#add").on("click", function() {
        var clone = $(".option").eq(0).clone();  // 객체 복사
        clone.find("input").val(""); // 복사된 객체에서 input의 value를 공백으로 바꿈
        clone.appendTo("#optionBox");  // 초기화된 상태로 붙여넣기
        clone.find(".age").attr("name", "productOptions[" + ($(".option").length-1) + "].productOptionAgeRange"); // 복사된 객체에 name 추가
        clone.find(".regular").attr("name", "productOptions[" + ($(".option").length-1) + "].productOptionRegularPrice");
        clone.find(".discount").attr("name", "productOptions[" + ($(".option").length-1) + "].productOptionDiscountPrice");

    });

    // 상품 대표사진 미리보기
    $("#mainImg").on("change", function(event) {

        var files = event.target.files;
        if(files.length != 0) {
            $(".mainImgShow").remove();
            for(let i = 0; i < files.length; i++) {
                var reader = new FileReader();
                reader.onload = function(e) {
                    $("#mainImgShowBox").append($("<img>", {"src" : e.target.result, "class" : "mainImgShow"}));
                }
                reader.readAsDataURL(files[i]);
            }
        }
        else {
            $(".mainImgShow").remove();
        }

    });

    // 상품 상세사진 미리보기
    $("#detailImg").on("change", function(event) {

        var files = event.target.files;
        if(files.length != 0) {
            $(".detailImgShow").remove();
            for(let i = 0; i < files.length; i++) {
                var reader = new FileReader();
                reader.onload = function(e) {
                    $("#detailImgShowBox").append($("<img>", {"src" : e.target.result, "class" : "detailImgShow"}));
                }
                reader.readAsDataURL(files[i]);
            }
        }
        else {
            $(".detailImgShow").remove();
        }

    });

    // 대분류 선택 시 중분류 생성
    $("select:nth-of-type(1)").on("change", function() {

        var Korea = ['서울','부산','제주도','울릉도'];
        var Japan = ['오사카','도쿄','후쿠오카','오키나와','삿포로'];
        var China = ['베이징','상하이','칭다오','광저우','하얼빈'];
        var USA = ['하와이','뉴욕','로스앤젤레스','샌프란시스코','라스베이거스','시카고'];
        var Europe = ['파리','스페인','이탈리아','런던','영국','프랑스','독일'];
        var mainCategory = $("select:nth-of-type(1)").val();
        var option;

        if (mainCategory == '국내여행') {
            option = Korea;
        }
        else if (mainCategory == '일본여행') {
            option = Japan;
        }
        else if (mainCategory == '중국여행') {
            option = China;
        }
        else if (mainCategory == '미국여행') {
            option = USA;
        }
        else if (mainCategory == '유럽여행') {
            option = Europe;
        }

        $("select:nth-of-type(2)").empty();
        $("select:nth-of-type(2)").append("<option val='' hidden>--중분류--</option>");
        for (var i = 0; i < option.length; i++) {
            $("select:nth-of-type(2)").append("<option val='" + option[i] + "'>" + option[i] + "</option>");
        }

    });

});