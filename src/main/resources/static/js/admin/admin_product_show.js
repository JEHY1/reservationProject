$(function() {
    // 천단위 콤마
    let price = $("#showBox > div > table tr:nth-child(5) > td:nth-of-type(1)").text();
    let sales = $("#showBox > div > table tr:nth-child(5) > td:nth-of-type(2)").text();
    let count = $("#showBox > div > table tr:nth-child(6) > td:nth-of-type(2)").text();

    $("#showBox > div > table tr:nth-child(5) > td:nth-of-type(1)").text(price.replace(/\B(?=(\d{3})+(?!\d))/g, ",") + ' 원');
    // '할인가'가 있을 때
    if(sales != '-') {
        $("#showBox > div > table tr:nth-child(5) > td:nth-of-type(2)").text(sales.replace(/\B(?=(\d{3})+(?!\d))/g, ",") + ' 원');
    }
    $("#showBox > div > table tr:nth-child(6) > td:nth-of-type(2)").text(count.replace(/\B(?=(\d{3})+(?!\d))/g, ",") + ' 개');

    for(let i = 1; i <= $("#optionBox > table").length; i++) {
        let optionPrice = $("#optionBox > table:nth-child(" + i + ") tr:nth-child(2) > td:nth-of-type(1)").text();
        let optionSales = $("#optionBox > table:nth-child(" + i + ") tr:nth-child(2) > td:nth-of-type(2)").text();

        $("#optionBox > table:nth-child(" + i + ") tr:nth-child(2) > td:nth-of-type(1)").text(optionPrice.replace(/\B(?=(\d{3})+(?!\d))/g, ",") + ' 원');
        // '할인가'가 있을 때
        if(sales != '-') {
            $("#optionBox > table:nth-child(" + i + ") tr:nth-child(2) > td:nth-of-type(2)").text(optionSales.replace(/\B(?=(\d{3})+(?!\d))/g, ",") + ' 원');
        }
    }
});