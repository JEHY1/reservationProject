$(function() {

    // submit 시 확인할 변수
    let pw1Check = false;
    let pw2Check = false;

    // 새 비밀번호
    $("#userPw1").on("input", function() {
        let userPw1 = $("#userPw1").val();
        let checkPw = RegExp(/^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[!@#$%^&*+=-_?]).{10,}$/); // 비밀번호 유효성 검사 (영문, 숫자, 특수문자 조합 10글자 이상)

        // 새 비밀번호 공백 확인
        if(userPw1 == "" || userPw1 == null) {
            $("#userPw1 + span").html("새 비밀번호를 입력해주세요.").css("color","red");
            pw1Check = false;
        }
        // 새 비밀번호 유효성 체크
        else if(!checkPw.test($.trim(userPw1))) {
            $("#userPw1 + span").html("영문, 숫자, 특수문자를 조합하여 10자리 이상 입력해주세요.").css("color","red");
            pw1Check = false;
        }
        else {
            $("#userPw1 + span").html("");
            pw1Check = true;
        }
    });

    // 새 비밀번호 확인
    $("#userPw2").on("input", function() {
        let userPw1 = $("#userPw1").val();
        let userPw2 = $("#userPw2").val();

        // 새 비밀번호 공백 확인
        if(userPw2 == "" || userPw2 == null) {
            $("#userPw2 + span").html("새 비밀번호를 다시 입력해주세요.").css("color","red");
            pw2Check = false;
        }
        // 새 비밀번호 일치 확인
        else if(userPw2 != userPw1) {
            $("#userPw2 + span").html("새 비밀번호가 일치하지 않습니다.").css("color","red");
            pw2Check = false;
        }
        else {
            $("#userPw2 + span").html("");
            pw2Check = true;
        }
    });

    // submit 됐을 때
    $("#findFinishBox").submit(function() {

        if(pw1Check == false) {
            $("#userPw1").focus();
            if($("#userPw1").val() == "" || $("#userPw1").val() == null) {
                $("#userPw1 + span").html("새 비밀번호를 입력해주세요.").css("color","red");
            }
            return false;
        }
        else if(pw2Check == false) {
            $("#userPw2").focus();
            if($("#userPw2").val() == "" || $("#userPw2").val() == null) {
                $("#userPw2 + span").html("새 비밀번호를 다시 입력해주세요.").css("color","red");
            }
            return false;
        }
        else {
            return true;
        }
    });


});