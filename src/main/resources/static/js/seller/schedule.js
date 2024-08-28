function toWon(price) {
    let PriceText = '';
    price = price.toString(); // 숫자를 문자열로 변환

    while (price.length > 3) {
        // 뒤에서 세 자리씩 끊어서 콤마와 함께 추가
        PriceText = ',' + price.substring(price.length - 3) + PriceText;
        price = price.substring(0, price.length - 3);
    }

    // 남아있는 숫자 부분과 함께 "원"을 붙여 반환
    return price + PriceText + '원';
}

function reverse(list){
    let reversedList = [];
    let i = list.length - 1;
    for(i; i >= 0; i--){
        reversedList.push(list[i]);
    }
    return reversedList;
}

function getLast3Month(year, month){
    let list = [];
    let i = 0;
    year = parseInt(year.value);
    month = parseInt(month.value);
    for(i; i < 3; i++){
        list.push(year + '년 ' + month + '월');
        month--;
        if(month === 0){
            month = 12;
            year--;
        }
    }

    return reverse(list);
}

function toIntList(compList){
    let list = [];
    Array.from(compList).forEach(comp => {
        list.push(parseInt(comp.textContent));
    });

    return list;
}

function toList(compList){
    let list = [];
    Array.from(compList).forEach(comp => {
        list.push(comp.value);
    });

    return list;
}

function getUrlParameter(name) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(name);
}

const prevMonthButton = document.getElementById('prevMonth-btn');

if(prevMonthButton){
    let month = parseInt(document.getElementById('scheduleMonth').value) - 1 === 0 ? 12 : parseInt(document.getElementById('scheduleMonth').value) - 1;
    let year = month === 12 ? parseInt(document.getElementById('scheduleYear').value) - 1 : parseInt(document.getElementById('scheduleYear').value);


    prevMonthButton.addEventListener('click', () => {
        location.href = '/seller/totalManager?productId=' + getUrlParameter('productId') + '&year=' + year + '&month=' + month;
    });
}

const nextMonthButton = document.getElementById('nextMonth-btn');

if(nextMonthButton){
    let month = parseInt(document.getElementById('scheduleMonth').value) + 1 === 13 ? 1 : parseInt(document.getElementById('scheduleMonth').value) + 1;
    let year = month === 1 ? parseInt(document.getElementById('scheduleYear').value) + 1 : parseInt(document.getElementById('scheduleYear').value);


    nextMonthButton.addEventListener('click', () => {
        location.href = '/seller/totalManager?productId=' + getUrlParameter('productId') + '&year=' + year + '&month=' + month;
    });
}

const statisticsTitle = document.getElementById('statisticsTitle');

if (statisticsTitle) {

    let productListField = statisticsTitle.nextElementSibling;
    let items = document.querySelectorAll(".productSel");
    statisticsTitle.addEventListener('mouseover', () => {
        productListField.classList.remove('d-hidden');

        items.forEach((item, index) => {
            setTimeout(() => {
              item.classList.add("fade-in");
            }, index * 20);
        });
    });

    statisticsTitle.addEventListener('mouseout', () => {

        productListField.classList.add('d-hidden');
    });

    productListField.addEventListener('mouseover', () => {
        productListField.classList.remove('d-hidden');

    });

    productListField.addEventListener('mouseout', () => {
        productListField.classList.add('d-hidden');
    });
}





let paymentStatistics = document.getElementById('paymentStatistics').getContext('2d');

let paymentStatisticsChart = new Chart(paymentStatistics, {
    type: 'bar',
    data: {
        labels: getLast3Month(document.getElementById('year'), document.getElementById('month')),
        datasets: [{
            type: 'bar',
            label: '막대 그래프', // This will not appear in the tooltip
            data: reverse(JSON.parse(document.getElementById('paymentHistoryList').getAttribute('data-payment-history-list'))),
            backgroundColor: [
                'rgba(75, 192, 192, 0.2)',
                'rgba(255, 206, 86, 0.2)',
                'rgba(54, 162, 235, 0.2)'
            ],
            borderColor: [
                'rgba(75, 192, 192, 1)',
                'rgba(255, 206, 86, 1)',
                'rgba(54, 162, 235, 1)'
            ],
            borderWidth: 1
        },
        {
            type: 'line',
            label: '선 그래프',
            data: reverse(JSON.parse(document.getElementById('paymentHistoryList').getAttribute('data-payment-history-list'))),
            borderColor: 'rgba(153, 102, 255, 1)',
            backgroundColor: 'rgba(153, 102, 255, 0.2)',
            borderWidth: 2,
            fill: false,
            tension: 0.4 // Smoothing of the line, adjust as needed
        }]
    },
    options: {
        interaction: {
            mode: 'index', // Keep this to show tooltip for all datasets at once
            intersect: false // Allow hover on the bar area, not just the center
        },
        scales: {
            y: {
                beginAtZero: true,
                ticks: {
                    callback: function(value) {
                        return toWon(value); // Append '원' to each tick label
                    }
                }
            }
        },
        responsive: true,
        plugins: {
            legend: {
                display: true // Show legend for both bar and line datasets
            },
            tooltip: {
                callbacks: {
                    title: function(tooltipItems) {
                        return ''; // No title in tooltip
                    },
                    label: function(context) {
                        let label = context.chart.data.labels[context.dataIndex] || '';
                        let value = Math.round(context.raw * 100) / 100;
                        return label + ' : ' + toWon(value);
                    }
                }
            }
        }
    }
});

console.log(toIntList(document.getElementsByClassName('optionCount')));
console.log('dsfasdfasdfads');

if(document.getElementById('orderStatusStatistics')){
    let orderStatusStatistics = document.getElementById('orderStatusStatistics').getContext('2d');
    // Create new chart
    let paymentPriceChart = new Chart(orderStatusStatistics, {
      type: 'pie', // 'pie' or 'doughnut'
      data: {
          labels: ['결제완료', '입금미확인', '미입금', '주문취소', '환불완료', '환불진행'],
          datasets: [{
              label: '통계',
              data: JSON.parse(document.getElementById('orderStatusStatisticsList').getAttribute('data-order-status-statistics-list')),
              backgroundColor: [
                  'rgba(54, 162, 235, 0.2)',
                  'rgba(255, 206, 86, 0.2)',
                  'rgba(255, 182, 193, 0.2)',
                  'rgba(255, 99, 132, 0.2)',
                  'rgba(255, 99, 71, 0.2)',
                  'rgba(153, 102, 255, 0.2)'
              ],
              borderColor: [
                  'rgba(54, 162, 235, 1)',
                  'rgba(255, 206, 86, 1)',
                  'rgba(255, 182, 193, 1)',
                  'rgba(255, 99, 132, 1)',
                  'rgba(255, 99, 71, 1)',
                  'rgba(153, 102, 255, 1)'
              ],
              borderWidth: 1
          }]
      },
      options: {
          responsive: true,
          plugins: {
              legend: {
                  display: false, // Disable the default legend
                  onClick: null, // Disable default click event
              },
          }
      }
    });

    // Generate custom legend
    generateCustomLegend1(paymentPriceChart);
}



if(document.getElementById('optionStatistics')){
    let optionStatistics = document.getElementById('optionStatistics').getContext('2d');
    // Create new chart
    let optionStatisticsChart = new Chart(optionStatistics, {
      type: 'pie', // 'pie' or 'doughnut'
      data: {
          labels: toList(document.getElementsByClassName('optionName')),
          datasets: [{
              label: '통계',
              data: toIntList(document.getElementsByClassName('optionCount')),
              backgroundColor: [
                  'rgba(75, 192, 192, 0.2)',
                  'rgba(255, 206, 86, 0.2)',
                  'rgba(54, 162, 235, 0.2)',
                  'rgba(255, 99, 132, 0.2)',
                  'rgba(75, 192, 192, 0.2)',
                  'rgba(255, 206, 86, 0.2)'
              ],
              borderColor: [
                  'rgba(75, 192, 192, 1)',
                  'rgba(255, 206, 86, 1)',
                  'rgba(54, 162, 235, 1)',
                  'rgba(255, 99, 132, 1)',
                  'rgba(75, 192, 192, 1)',
                  'rgba(255, 206, 86, 1)'
              ],
              borderWidth: 1
          }]
      },
      options: {
          responsive: true,
          plugins: {
              legend: {
                  display: false, // Disable the default legend
                  onClick: null, // Disable default click event
              },
          }
      }
    });

    // Generate custom legend
    generateCustomLegend2(optionStatisticsChart);
}


// Function to generate custom legend outside of the chart
function generateCustomLegend1(chart) {
  let legendContainer = document.getElementById('orderStatusStatisticsButtonField');
  legendContainer.innerHTML = ''; // Clear previous legends

  chart.data.labels.forEach((label, index) => {
      let legendItem = document.createElement('button');
      legendItem.style.backgroundColor = chart.data.datasets[0].backgroundColor[index];
      legendItem.style.border = `1px solid ${chart.data.datasets[0].borderColor[index]}`;
      legendItem.style.margin = '5px';
      legendItem.textContent = label;

      // Add initial text decoration based on visibility
      let meta = chart.getDatasetMeta(0);
      if (meta.data[index].hidden) {
          legendItem.style.textDecoration = 'line-through';
      }

      // Add click event to toggle visibility
      legendItem.addEventListener('click', () => {
          let meta = chart.getDatasetMeta(0);
          meta.data[index].hidden = !meta.data[index].hidden;
          chart.update();

          // Update text decoration based on visibility
          if (meta.data[index].hidden) {
              legendItem.style.textDecoration = 'line-through';
          } else {
              legendItem.style.textDecoration = 'none';
          }
      });

      legendContainer.appendChild(legendItem);
  });
}

if(document.getElementById('totalOptionCount')){
    let totalOptionCount = 0;
    Array.from(document.getElementsByClassName('optionCount')).forEach(comp => {
        totalOptionCount += parseInt(comp.textContent);
    });
    document.getElementById('totalOptionCount').value = totalOptionCount;
    if(totalOptionCount === 0){

    }
}

// Function to generate custom legend outside of the chart
function generateCustomLegend2(chart) {
  let legendContainer = document.getElementById('optionStatisticsButtonField');
  legendContainer.innerHTML = ''; // Clear previous legends

  chart.data.labels.forEach((label, index) => {
      let legendItem = document.createElement('button');
      legendItem.style.backgroundColor = chart.data.datasets[0].backgroundColor[index];
      legendItem.style.border = `1px solid ${chart.data.datasets[0].borderColor[index]}`;
      legendItem.style.margin = '5px';
      legendItem.style.fontSize = '13px';
      legendItem.textContent = label;

      // Add initial text decoration based on visibility
      let meta = chart.getDatasetMeta(0);
      if (meta.data[index].hidden) {
          legendItem.style.textDecoration = 'line-through';
      }

      // Add click event to toggle visibility
      legendItem.addEventListener('click', () => {
          let meta = chart.getDatasetMeta(0);
          meta.data[index].hidden = !meta.data[index].hidden;
          chart.update();

          // Update text decoration based on visibility
          if (meta.data[index].hidden) {
              legendItem.style.textDecoration = 'line-through';
          } else {
              legendItem.style.textDecoration = 'none';
          }
      });

      legendContainer.appendChild(legendItem);
  });
}

//퍼센트 계산
if(document.getElementsByClassName('percent').length !== 0){
    let totalOrderCount = parseInt(document.getElementById('totalOrderCount').value);
    Array.from(document.getElementsByClassName('percent')).forEach(comp => {
        comp.textContent = (parseInt(comp.nextElementSibling.nextElementSibling.textContent) / totalOrderCount * 100).toFixed(2);
    });
}

if(document.getElementsByClassName('percent2').length !== 0){
    let totalOptionCount = parseInt(document.getElementById('totalOptionCount').value);
    Array.from(document.getElementsByClassName('percent2')).forEach(comp => {
        comp.textContent = (parseInt(comp.nextElementSibling.nextElementSibling.textContent) / totalOptionCount * 100).toFixed(2);
    });
}

//달력 만들기
const calendar = document.getElementById('calendar');

if(calendar){
    let year = parseInt(document.getElementById('scheduleYear').value);
    let month = parseInt(document.getElementById('scheduleMonth').value);
    let startDayOfWeek = parseInt(document.getElementById('scheduleStartDayOfWeek').value);
    let lastDay = parseInt(document.getElementById('scheduleLastDay').value);
    let productStartDate = parseInt(document.getElementById('productStartDate').value);
    let productEndDate = parseInt(document.getElementById('productEndDate').value);

    if(35 < startDayOfWeek + lastDay){
        document.getElementById('calendarDateField').setAttribute('style', 'height: 720px;');
    }

    for(let i = 0; i < startDayOfWeek; i++){
        let comp = document.createElement('div');
        comp.setAttribute('class', 'col-auto p-0 border');
        comp.setAttribute('style', 'width: 230px; height: 120px;');
        document.getElementById('calendarDateField').appendChild(comp);
    }

    for(let i = 1; i <= lastDay; i ++){
        let comp1 = document.createElement('div');
        comp1.setAttribute('class', 'col-auto p-0 border');
        if((startDayOfWeek + i) % 7 === 0){
            comp1.classList.add('c-b');
        }
        else if((startDayOfWeek + i) % 7 === 1){
            comp1.classList.add('c-r');
        }
        comp1.setAttribute('style', 'width: 230px; height: 120px;');
        let comp2 = document.createElement('div');
        comp2.setAttribute('class', 'px-2');
        comp2.textContent = i;
        comp1.appendChild(comp2);
        let makeDate = '' + year + (month >= 10 ? month + '' : '0' + month) + (i >= 10 ? i + '' : '0' + i);
        console.log('makeDate : ' + makeDate);

        if(document.getElementById('productStartDate').value <= makeDate && document.getElementById('productEndDate').value >= makeDate){
            let index = JSON.parse(document.getElementById('orderDateList').getAttribute('data-order-date-list')).indexOf(parseInt(makeDate));
            console.log('index : ' + index);
            let comp3 = document.createElement('div');
            comp3.style.color = '#4b4f9f';
            comp3.setAttribute('class', 'px-3 mt-2');
            comp3.textContent = (index === -1 ? 0 : JSON.parse(document.getElementById('orderDepartureCountList').getAttribute('data-order-departure-count-list'))[index]) + ' / ' + document.getElementById('maxDepartureCount').value;
            comp1.appendChild(comp3);
        }
        document.getElementById('calendarDateField').appendChild(comp1);
    }
}

//헤더 fix처리
if(document.getElementById('menu')){
    document.getElementById('menu').setAttribute('style', 'width: 200px; position: fixed;');
}

//달력 이동시 페이지 맨 밑으로
if(getUrlParameter('year')){

    //바로 해당 스크롤위치로 이동
    window.scroll(0, document.body.scrollHeight);
}

//원화 설정
Array.from(document.getElementsByClassName('price')).forEach(comp => comp.textContent = toWon(comp.textContent));