$(function() {
    // submit 됐을 때
    $("#loginBox").submit(function() {

        let userId = $("#userId").val();
        let userPw = $("#userPw").val();

        if(userId == "" || userId == null) {
            $("#userId").focus();
            $("#loginBox > p:nth-of-type(1)").html("아이디를 입력해주세요.").css("color","red");
            return false;
        }
        else if(userPw == "" || userPw == null) {
            $("#userPw").focus();
            $("#loginBox > p:nth-of-type(1)").html("비밀번호를 입력해주세요.").css("color","red");
            return false;
        }
        else {
            let returnVal = false; // form 전송여부 변수
            $.ajax({
                url:'/login/check',
                type : "post",
                data : {
                    "username" : userId,
                    "userPassword" : userPw
                },
                dataType : 'text',
                async : false, // 비동기식 처리
                success : function(data) {

                    if (data == "error_id") {
                        $("#loginBox > p:nth-of-type(1)").html("아이디 또는 비밀번호가 올바르지 않습니다. 다시 확인해주세요.").css("color","red");
                        $("#userId").val("");
                        $("#userPw").val("");
                        $("#userId").focus();
                        returnVal = false;
                    }
                    else if (data == "error_pw") {
                        $("#loginBox > p:nth-of-type(1)").html("아이디 또는 비밀번호가 올바르지 않습니다. 다시 확인해주세요.").css("color","red");
                        $("#userPw").val("");
                        $("#userPw").focus();
                        returnVal = false;
                    }
                    else if (data == "success_login") {
                        returnVal = true;

                    }
                }
            });
            return returnVal;
        }
    });
});







