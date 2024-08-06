$(function() {

    // 비밀번호
    $("#userPw").on("input", function() {
        let userPw = $("#userPw").val();

        // 비밀번호 공백 확인
        if(userPw == "" || userPw == null) {
            $("#userPw + p").html("비밀번호를 입력해주세요.").css("color","red");
        }
        else {
            $("#userPw + p").html("");
        }
    });

    // submit 됐을 때
    $("#confirmBox").submit(function() {
        let userPw = $("#userPw").val();

        if(userPw == "" || userPw == null) {
            $("#userPw").focus();
            $("#userPw + p").html("비밀번호를 입력해주세요.").css("color","red");
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
                        $("#userPw + p").html("비밀번호를 다시 확인해주세요").css("color","red");
                        $("#userPw").val("");
                        $("#userPw").focus();
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
});