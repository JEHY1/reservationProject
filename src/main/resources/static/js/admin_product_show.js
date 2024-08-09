$(function() {
    // 천단위 콤마
    let price = $("#showBox > div > table tr:nth-child(5) > td").text();
    let sales = $("#showBox > div > table tr:nth-child(6) > td").text();
    let count = $("#showBox > div > table tr:nth-child(7) > td").text();

    let optionPrice = $("#optionBox > table tr:nth-child(2) > td").text();
    let optionSales = $("#optionBox > table tr:nth-child(3) > td").text();

    $("#showBox > div > table tr:nth-child(5) > td").text(price.replace(/\B(?=(\d{3})+(?!\d))/g, ",") + ' 원');
    // '할인가'가 있을 때
    if(sales != '-') {
        $("#showBox > div > table tr:nth-child(6) > td").text(sales.replace(/\B(?=(\d{3})+(?!\d))/g, ",") + ' 원');
    }
    $("#showBox > div > table tr:nth-child(7) > td").text(count.replace(/\B(?=(\d{3})+(?!\d))/g, ",") + ' 개');

    for(let i = 1; i <= $("#optionBox > table").length; i++) {
        let price = $("#optionBox > table:nth-child(" + i + ") tr:nth-child(2) > td").text();
        let sales = $("#optionBox > table:nth-child(" + i + ") tr:nth-child(3) > td").text();

        $("#optionBox > table:nth-child(" + i + ") tr:nth-child(2) > td").text(price.replace(/\B(?=(\d{3})+(?!\d))/g, ",") + ' 원');
        // '할인가'가 있을 때
        if(sales != '-') {
            $("#optionBox > table:nth-child(" + i + ") tr:nth-child(3) > td").text(sales.replace(/\B(?=(\d{3})+(?!\d))/g, ",") + ' 원');
        }
    }
});