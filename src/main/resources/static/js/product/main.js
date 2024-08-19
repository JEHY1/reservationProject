$(function(){

    // slide 사진
    setInterval(autoSlide, 3000);
    function autoSlide() {
      $("#slideBox > ul").animate({ "margin-left": "-1903px" }, 800, function () {
        $("#slideBox > ul").css({ "margin-left": "0" });
        $("#slideBox > ul > li:first-child").insertAfter("#slideBox > ul > li:last-child");
      });
    };
    // slide 하단바
    autoSlideBar();
    setInterval(autoSlideBar, 9000);
    function autoSlideBar() {
      setTimeout(function(){
        $("#slideBox > div > span").animate({ "margin-left": "200px" }, 800);
      }, 3000);
      setTimeout(function(){
        $("#slideBox > div > span").animate({ "margin-left": "400px" }, 800);
      }, 6000);
      setTimeout(function(){
        $("#slideBox > div > span").animate({ "margin-left": "0px" }, 800);
      }, 9000);
    };
  
    // 신상품
    for(let i = 1; i <= $("#newBox > #grid2 > div").length; i++) {
        let price = $("#newBox > #grid2 > div:nth-child(" + i + ") > p:nth-of-type(3) > span:nth-child(1)").text();
        let sales = $("#newBox > #grid2 > div:nth-child(" + i + ") > p:nth-of-type(3) > span:nth-child(2)").text();
        let discount = Math.floor((price-sales)/price*100); // 할인률 소수점 버림
        // 할인률 계산
        $("#newBox > #grid2 > div:nth-child(" + i + ") > p:nth-of-type(3) > span:nth-child(3)").text(discount + "%");
        // 천단위 콤마
        $("#newBox > #grid2 > div:nth-child(" + i + ") > p:nth-of-type(3) > span:nth-child(1)").text(price.replace(/\B(?=(\d{3})+(?!\d))/g, ","));
        $("#newBox > #grid2 > div:nth-child(" + i + ") > p:nth-of-type(3) > span:nth-child(2)").text(sales.replace(/\B(?=(\d{3})+(?!\d))/g, ","));
    }

});
  