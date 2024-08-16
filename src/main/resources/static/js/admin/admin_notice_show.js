$(function() {
    // 천단위 콤마
    let count = $("#registerInfo > p:nth-child(1) > span").text();
    $("#registerInfo > p:nth-child(1) > span").text(count.replace(/\B(?=(\d{3})+(?!\d))/g, ",") + ' 회');
});