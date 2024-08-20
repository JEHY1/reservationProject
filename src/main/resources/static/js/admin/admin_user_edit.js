$(function() {

    // submit 시 확인할 변수
    let phoneCheck = true;
    let emailCheck = true;

    // 연락처
    $("#userPhone").on("input", function() {
        // 자동 하이픈(-) 추가
        let val = $("#userPhone").val().replace(/[^0-9]/g, ''); // 숫자가 아닌 문자 공백으로
        if(val.length > 3 && val.length < 6) {
            let tmp = val.substring(0,2)
            if(tmp == "02"){
                $("#userPhone").val(val.substring(0,2) + "-" + val.substring(2));
            }
            else {
                $("#userPhone").val(val.substring(0,3) + "-" + val.substring(3));
            }
        }
        else if (val.length > 6) {
            let tmp = val.substring(0,2)
            let tmp2 = val.substring(0,4)
            if(tmp == "02"){
                if(val.length == "10") {
                    $("#userPhone").val(val.substring(0,2) + "-" + val.substring(2, 6) + "-" + val.substring(6));
                }
                else {
                    $("#userPhone").val(val.substring(0,2) + "-" + val.substring(2, 5) + "-" + val.substring(5));
                }
            }
            else if(tmp2 == "0505") {
                if(val.length == "12") {
                    $("#userPhone").val(val.substring(0,4) + "-" + val.substring(4, 8) + "-" + val.substring(8));
                }
                else {
                    $("#userPhone").val(val.substring(0,4) + "-" + val.substring(4, 7) + "-" + val.substring(7));
                }
            }
            else {
                if(val.length == "11") {
                    $("#userPhone").val(val.substring(0,3) + "-" + val.substring(3, 7) + "-" + val.substring(7));
                }
                else {
                    $("#userPhone").val(val.substring(0,3) + "-" + val.substring(3, 6) + "-" + val.substring(6));
                }
            }
        }

        let userPhone = $("#userPhone").val();
        let checkPhone = RegExp( /^(02|0505|0[1-9]{1}[0-9]{1})-[0-9]{3,4}-[0-9]{4}$/); // 연락처 유효성 검사

        // 연락처 공백 확인
        if(userPhone == "" || userPhone == null) {
            $("#userPhone + p").html("");
            phoneCheck = false;
        }
        // 연락처 유효성 체크
        else if(!checkPhone.test($.trim(userPhone))) {
            $("#userPhone + p").html("전화번호 형식에 맞게 입력해주세요.").css("color","red");
            phoneCheck = false;
        }
        // 연락처 중복 체크
        else {
            $.ajax({
                url:'/admin/phone/check',
                type : "post",
                data : {
                    "userPhone" : userPhone,
                    "userId" : $("input[type=hidden]").val()
                },
                dataType : 'text',
                success : function(data) {
                    if (data == "duplicated") {
                        $("#userPhone + p").html("중복된 연락처입니다.").css("color","red");
                        phoneCheck = false;
                    }
                    else if (data == "equal") {
                        $("#userPhone + p").html("현재 사용 중인 연락처입니다.").css("color","green");
                        phoneCheck = true;
                    }
                    else if (data == "available") {
                        $("#userPhone + p").html("");
                        phoneCheck = true;
                    }
                }
            });
        }

    });

    // 이메일
    $("#userEmail").on("input", function() {
        let userEmail = $("#userEmail").val();
        let checkEmail = RegExp(/^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i); // 이메일 유효성 검사

        // 이메일 공백 확인
        if(userEmail == "" || userEmail == null) {
            $("#userEmail + p").html("");
            emailCheck = false;
        }
        // 이메일 유효성 체크
        else if(!checkEmail.test($.trim(userEmail))){
            $("#userEmail + p").html("이메일 형식에 맞게 입력해주세요.").css("color","red");
            emailCheck = false;
        }
        // 이메일 중복 체크
        else {
            $.ajax({
                url:'/admin/email/check',
                type : "post",
                data : {
                    "userEmail" : userEmail,
                    "userId" : $("input[type=hidden]").val()
                },
                dataType : 'text',
                success : function(data) {
                    if (data == "duplicated") {
                        $("#userEmail + p").html("중복된 이메일입니다.").css("color","red");
                        emailCheck = false;
                    }
                    else if (data == "equal") {
                        $("#userEmail + p").html("현재 사용 중인 이메일입니다.").css("color","green");
                        emailCheck = true;
                    }
                    else if (data == "available") {
                        $("#userEmail + p").html("");
                        emailCheck = true;
                    }
                }
            });
        }
    });
    // 수정 submit 됐을 때
    $(".buttonBox button:nth-child(1)").on("click", function() {
        if(phoneCheck == false) {
            $("#userPhone").focus();
            return false;
        }
        else if(emailCheck == false) {
            $("#userEmail").focus();
            return false;
        }
        else {
            return true;
        }
    });

});