$(function() {
    // 중분류 열고 닫을 때
    $("#menu > ul > li > a").on("click", function() {
        if( $(this).next().css("display") == "none") {
            $(this).next().css("display", "block");
            $(this).find("span:nth-child(2)").text("︿").css("margin-top", "-5px");
        }
        else {
            $(this).next().css("display", "none");
            $(this).find("span:nth-child(2)").text("﹀").css("margin-top", "5px");
        }
    });

    // notice 닫기
    $("#notice > div > a").on("click", function() {
        $("#notice").css("display","none");
    });

});