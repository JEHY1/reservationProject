$(function() {
    // 비밀번호 변경 버튼 누를 때
    $("#pwButton").on("click", function() {
        $("#pwButton").css("display", "none");
        $("#pwBox").css("display", "block");
        $("#pwBox").prev().css("display", "none");
        $("#userPw").focus();
    });
    // 취소 버튼 누를 때
    $("#pwBox > span > button:nth-child(2)").on("click", function() {
        $("#pwButton").css("display", "inline-block");
        $("#pwBox").css("display", "none");
    });
    // 배송지 목록 보기 버튼 누를 때
    $("#deliveryButton").on("click", function() {
        window.open("/user/address/list", "addressList", "width = 500, height = 800, top = 50%, left = 50%");
    });


    // submit 시 확인할 변수
    let pwCheck = false;
    let newPw1Check = false;
    let newPw2Check = false;

    // 기존 비밀번호
    $("#userPw").on("input", function() {
        let userPw = $("#userPw").val();

        // 기존 비밀번호 공백 확인
        if(userPw == "" || userPw == null) {
            $("#userPw + span").html("기존 비밀번호를 입력해주세요.").css("color","red");
            pwCheck = false;
        }
        else {
            $("#userPw + span").html("");
            pwCheck = true;
        }
    });

    // 새 비밀번호
    $("#newUserPw1").on("input", function() {
        let newUserPw1 = $("#newUserPw1").val();
        let checkPw = RegExp(/^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[!@#$%^&*+=-_?]).{10,}$/); // 비밀번호 유효성 검사 (영문, 숫자, 특수문자 조합 10글자 이상)

        // 새 비밀번호 공백 확인
        if(newUserPw1 == "" || newUserPw1 == null) {
            $("#newUserPw1 + span").html("새 비밀번호를 입력해주세요.").css("color","red");
            newPw1Check = false;
        }
        // 새 비밀번호 유효성 체크
        else if(!checkPw.test($.trim(newUserPw1))) {
            $("#newUserPw1 + span").html("영문, 숫자, 특수문자를 조합하여 10자리 이상 입력해주세요.").css("color","red");
            newPw1Check = false;
        }
        else {
            $("#newUserPw1 + span").html("");
            newPw1Check = true;
        }
    });

    // 새 비밀번호 확인
    $("#newUserPw2").on("input", function() {
        let newUserPw1 = $("#newUserPw1").val();
        let newUserPw2 = $("#newUserPw2").val();

        // 새 비밀번호 공백 확인
        if(newUserPw2 == "" || newUserPw2 == null) {
            $("#newUserPw2 + span").html("새 비밀번호를 다시 입력해주세요.").css("color","red");
            newPw2Check = false;
        }
        // 새 비밀번호 일치 확인
        else if(newUserPw2 != newUserPw1) {
            $("#newUserPw2 + span").html("새 비밀번호가 일치하지 않습니다.").css("color","red");
            newPw2Check = false;
        }
        else {
            $("#newUserPw2 + span").html("");
            newPw2Check = true;
        }
    });

    // 비밀번호 변경 확인 submit 됐을 때
    $("#pwBox > span > button:nth-child(1)").on("click", function() {
        let userPw = $("#userPw").val();
        let newUserPw1 = $("#newUserPw1").val();
        let newUserPw2 = $("#newUserPw2").val();

        if(pwCheck == false) {
            $("#userPw").focus();
            if(userPw == "" || userPw == null) {
                $("#userPw + span").html("기존 비밀번호를 입력해주세요.").css("color","red");
            }
            return false;
        }
        else if(newPw1Check == false) {
            $("#newUserPw1").focus();
            if(newUserPw1 == "" || newUserPw1 == null) {
                $("#newUserPw1 + span").html("새 비밀번호를 입력해주세요.").css("color","red");
            }
            return false;
        }
        else if(newPw2Check == false) {
            $("#newUserPw2").focus();
            if(newUserPw2 == "" || newUserPw2 == null) {
                $("#newUserPw2 + span").html("새 비밀번호를 다시 입력해주세요.").css("color","red");
            }
            return false;
        }
        else {
            let returnVal = false; // form 전송여부 변수
            $.ajax({
                url:'/password/check',
                type : "post",
                data : {
                    "userPassword" : userPw
                },
                dataType : 'text',
                async : false, // 비동기식 처리
                success : function(data) {
                    console.log(data)
                    if (data == "discord") {
                        $("#userPw + span").html("기존 비밀번호가 일치하지 않습니다.").css("color","red");
                        $("#userPw").val("");
                        $("#userId").focus();
                        returnVal = false;
                    }
                    else if (data == "accord") {
                        returnVal = true;
                    }
                }
            });
            return returnVal;
        }
    });

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
            $("#userPhone + span").html("연락처를 입력해주세요.").css("color","red");
            phoneCheck = false;
        }
        // 연락처 유효성 체크
        else if(!checkPhone.test($.trim(userPhone))) {
            $("#userPhone + span").html("전화번호 형식에 맞게 입력해주세요.").css("color","red");
            phoneCheck = false;
        }
        // 연락처 중복 체크
        else {
            $.ajax({
                url:'/phone/check',
                type : "post",
                data : {
                    "userPhone" : userPhone
                },
                dataType : 'text',
                success : function(data) {
                    if (data == "duplicated") {
                        $("#userPhone + span").html("중복된 연락처입니다.").css("color","red");
                        phoneCheck = false;
                    }
                    else if (data == "equal") {
                        $("#userPhone + span").html("현재 사용 중인 연락처입니다.").css("color","green");
                        phoneCheck = true;
                    }
                    else if (data == "available") {
                        $("#userPhone + span").html("");
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
            $("#userEmail + span").html("이메일을 입력해주세요.").css("color","red");
            emailCheck = false;
        }
        // 이메일 유효성 체크
        else if(!checkEmail.test($.trim(userEmail))){
            $("#userEmail + span").html("이메일 형식에 맞게 입력해주세요.").css("color","red");
            emailCheck = false;
        }
        // 이메일 중복 체크
        else {
            $.ajax({
                url:'/email/check',
                type : "post",
                data : {
                    "userEmail" : userEmail
                },
                dataType : 'text',
                success : function(data) {
                    if (data == "duplicated") {
                        $("#userEmail + span").html("중복된 이메일입니다.").css("color","red");
                        emailCheck = false;
                    }
                    else if (data == "equal") {
                        $("#userEmail + span").html("현재 사용 중인 이메일입니다.").css("color","green");
                        emailCheck = true;
                    }
                    else if (data == "available") {
                        $("#userEmail + span").html("");
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