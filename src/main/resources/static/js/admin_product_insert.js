$(function() {
    $("#add").on("click", function() {
        var clone = $(".option").clone();  // 객체 복사
        clone.find("input").val(""); // 복사된 객체에서 input의 value를 공백으로 바꿈
        clone.appendTo("#optionBox");  // 초기화된 상태로 붙여넣기
    });
});