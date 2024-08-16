$(function() {

    // 회원가입 유효성 체크 - submit 시 확인할 변수
    let idCheck = false;
    let nameCheck = false;
    let emailCheck = false;

    // 아이디
    $("#userId").on("input", function() {
        let userId = $("#userId").val();

        // 아이디 공백 확인
        if(userId == "" || userId == null) {
            $("#userId + span").html("아이디를 입력해주세요.").css("color","red");
            idCheck = false;
        }
        else {
            $("#userId + span").html("");
            idCheck = true;
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
        else {
            $("#userEmail + span").html("");
            emailCheck = true;
        }
    });

    // submit 됐을 때
    $("#findBox").submit(function() {

        if(idCheck == false) {
            $("#userId").focus();
            if($("#userId").val() == "" || $("#userId").val() == null) {
                $("#userId + span").html("아이디를 입력해주세요.").css("color","red");
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
        else if(emailCheck == false) {
            $("#userEmail").focus();
            if($("#userEmail").val() == "" || $("#userEmail").val() == null) {
                $("#userEmail + span").html("이메일을 입력해주세요.").css("color","red");
            }
            return false;
        }
        else {
            return true;
        }
    });

});

