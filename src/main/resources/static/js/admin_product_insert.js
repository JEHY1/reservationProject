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
});