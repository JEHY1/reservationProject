$(function() {

    // 회원가입 유효성 체크 - submit 시 확인할 변수
    let idCheck = false;
    let pw1Check = false;
    let pw2Check = false;
    let nameCheck = false;
    let phoneCheck = false;
    let emailCheck = false;
    let mustCheck = false;

    // 전체 동의하기 눌렀을 때, 다 체크
    $("#all").on("click", function() {
        if($("#all").prop("checked") == true) {
            $(".must, .select").prop("checked", true);
            $("#agreeBox > span:nth-child(2)").html("");
            mustCheck = true;
        }
        else {
            $(".must, .select").prop("checked", false);
            mustCheck = false;
        }
    });
    // 개별 다 체크되면 전체 동의하기 체크
    $(".must, .select").on("click", function() {
        if($("#must1").prop("checked") == true && $("#must2").prop("checked") == true
        && $("#select1").prop("checked") == true && $("#select2").prop("checked") == true) {
            $("#all").prop("checked", true);
            $("#agreeBox > span:nth-child(2)").html("");
            mustCheck = true;
        }
        else {
            $("#all").prop("checked", false);
            if($("#must1").prop("checked") == true && $("#must2").prop("checked") == true) { // 필수 항목만 체크 됐을 때
                $("#agreeBox > span:nth-child(2)").html("");
                mustCheck = true;
            }
            else {
                mustCheck = false;
            }
        }
    });

    // 아이디
    $("#userId").on("input", function() {
        let userId = $("#userId").val();
        let checkId = RegExp(/^(?=.*[a-zA-Z])(?=.*[0-9]).{6,20}$/); // 아이디 유효성 검사 (영문, 숫자 조합 6-20글자)

        // 아이디 공백 확인
        if(userId == "" || userId == null) {
            $("#userId + span").html("아이디를 입력해주세요.").css("color","red");
            idCheck = false;
        }
        // 아이디 유효성 체크
        else if(!checkId.test($.trim(userId))) {
            $("#userId + span").html("영문, 숫자 조합 6-20글자로 입력해주세요.").css("color","red");
            idCheck = false;
        }
        // 아이디 중복 체크
        else {
            $.ajax({
                url:'/join/idCheck',
                type : "post",
                data : {
                    "username" : userId
                },
                dataType : 'text',
                success : function(data) {
                    if (data == "duplicated") {
                        $("#userId + span").html("중복된 아이디입니다.").css("color","red");
                        idCheck = false;
                    }
                    else if (data == "available") {
                        $("#userId + span").html("사용가능한 아이디입니다.").css("color","green");
                        idCheck = true;
                    }
                }
            });
        }
    });

    // 비밀번호
    $("#userPw1").on("input", function() {
        let userPw1 = $("#userPw1").val();
        let checkPw = RegExp(/^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[!@#$%^&*+=-_?]).{10,}$/); // 비밀번호 유효성 검사 (영문, 숫자, 특수문자 조합 10글자 이상)

        // 비밀번호 공백 확인
        if(userPw1 == "" || userPw1 == null) {
            $("#userPw1 + span").html("비밀번호를 입력해주세요.").css("color","red");
            pw1Check = false;
        }
        // 비밀번호 유효성 체크
        else if(!checkPw.test($.trim(userPw1))) {
            $("#userPw1 + span").html("영문, 숫자, 특수문자를 조합하여 10자리 이상 입력해주세요.").css("color","red");
            pw1Check = false;
        }
        else {
            $("#userPw1 + span").html("");
            pw1Check = true;
        }
    });

    // 비밀번호 확인
    $("#userPw2").on("input", function() {
        let userPw1 = $("#userPw1").val();
        let userPw2 = $("#userPw2").val();

        // 비밀번호 공백 확인
        if(userPw2 == "" || userPw2 == null) {
            $("#userPw2 + span").html("비밀번호를 재입력해주세요.").css("color","red");
            pw2Check = false;
        }
        // 비밀번호 일치 확인
        else if(userPw2 != userPw1) {
            $("#userPw2 + span").html("비밀번호가 일치하지 않습니다.").css("color","red");
            pw2Check = false;
        }
        else {
            $("#userPw2 + span").html("");
            pw2Check = true;
        }
    });

    // 이름
    $("#userName").on("input", function() {
        let userName = $("#userName").val();

        // 이름 공백 확인
        if(userName == "" || userName == null) {
            $("#userName + span").html("이름을 입력해주세요.").css("color","red");
            nameCheck = false;
        }
        else {
            $("#userName + span").html("");
            nameCheck = true;
        }
    });

    // 연락처
    $("#userPhone1, #userPhone2, #userPhone3").on("input", function() {
        let userPhone =  $("#userPhone1").val() + $("#userPhone2").val() + $("#userPhone3").val();
        let userPhoneUnderBar = $("#userPhone1").val() + "-" + $("#userPhone2").val() + "-" + $("#userPhone3").val(); // "-" 포함 휴대폰 번호
        $("#userPhone1").val($("#userPhone1").val().replace(/[^0-9]/g, '')); // 숫자가 아닌 문자 공백으로
        $("#userPhone2").val($("#userPhone2").val().replace(/[^0-9]/g, '')); // 숫자가 아닌 문자 공백으로
        $("#userPhone3").val($("#userPhone3").val().replace(/[^0-9]/g, '')); // 숫자가 아닌 문자 공백으로

        // 연락처 공백 확인
        if(userPhone == "" || userPhone == null) {
            $("#userPhone1").parent().next().html("연락처를 입력해주세요.").css("color","red");
            phoneCheck = false;
        }
        // 연락처 중복 체크
        else {
            $.ajax({
                url:'/join/phoneCheck',
                type : "post",
                data : {
                    "userPhone" : userPhoneUnderBar
                },
                dataType : 'text',
                success : function(data) {
                    if (data == "duplicated") {
                        $("#userPhone1").parent().next().html("중복된 연락처입니다.").css("color","red");
                        phoneCheck = false;
                    }
                    else if (data == "available") {
                        $("#userPhone1").parent().next().html("");
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
                url:'/join/emailCheck',
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
                    else if (data == "available") {
                        $("#userEmail + span").html("");
                        emailCheck = true;
                    }
                }
            });
        }
    });
    // submit 됐을 때
    $("main form").submit(function() {
        if(idCheck == false) {
            $("#userId").focus();
            if($("#userId").val() == "" || $("#userId").val() == null) {
                $("#userId + span").html("아이디를 입력해주세요.").css("color","red");
            }
            return false;
        }
        else if(pw1Check == false) {
            $("#userPw1").focus();
            if($("#userPw1").val() == "" || $("#userPw1").val() == null) {
                $("#userPw1 + span").html("비밀번호를 입력해주세요.").css("color","red");
            }
            return false;
        }
        else if(pw2Check == false) {
            $("#userPw2").focus();
            if($("#userPw2").val() == "" || $("#userPw2").val() == null) {
                $("#userPw2 + span").html("비밀번호를 재입력해주세요.").css("color","red");
            }
            return false;
        }
        else if(nameCheck == false) {
            $("#userName").focus();
            if($("#userName").val() == "" || $("#userName").val() == null) {
                $("#userName + span").html("이름을 입력해주세요.").css("color","red");
            }
            return false;
        }
        else if(phoneCheck == false) {
            let userPhone = $("#userPhone1").val() + $("#userPhone2").val() + $("#userPhone3").val();
            $("#userPhone1").focus();
            if(userPhone == "" || userPhone == null) {
                $("#userPhone1").parent().next().html("휴대폰번호를 입력해주세요.").css("color","red");
            }
            return false;
        }
        else if(emailCheck == false) {
            $("#userEmail").focus();
            if($("#userEmail").val() == "" || $("#userEmail").val() == null) {
                $("#userEmail + span").html("이메일을 입력해주세요.").css("color","red");
            }
            return false;
        }
        else if(mustCheck == false) {
            $("#all").focus();
            $("#agreeBox > span:nth-child(2)").html("! 필수 항목에 모두 동의해주세요.").css("color","red");
            return false;
        }
        else {
            return true;
        }
    });
});

