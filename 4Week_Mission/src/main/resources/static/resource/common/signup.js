// 닉네임 체크
function username_check() {
    let username = $("#username").val();
    console.log(username);
    $.ajax({
        url: "/member/idCheck", // 클라이언트가 HTTP 요청을 보낼 서버의 URL 주소
        data: {username: username},  // HTTP 요청과 함께 서버로 보낼 데이터
        method: "GET",   // HTTP 요청 메소드(GET, POST 등)
        dataType: "html" // 서버에서 보내줄 데이터의 타입
    })
        // HTTP 요청이 성공하면 요청한 데이터가 done() 메소드로 전달됨.
        .done(function (json) {
            console.log(json);
            if ($("#duplicate").length === 0) {
                $("#idCheck").prepend("<div id='duplicate'>" + json + "</div>");
            } else {
                $("#duplicate").text(json);
            }
            // $("<div class=\"content\">").html(json.html).appendTo("body");
        })
        // HTTP 요청이 실패하면 오류와 상태에 관한 정보가 fail() 메소드로 전달됨.
        .fail(function (xhr, status, errorThrown) {
            $("#text").html("오류가 발생했다.<br>")
                .append("오류명: " + errorThrown + "<br>")
                .append("상태: " + status);
        })
        //
        .always(function (xhr, status) {
            $("#text").html("요청이 완료되었습니다!");
        });
}



// 이메일 체크
function email_check() {
    let email = $("#email").val();
    console.log(email);
    $.ajax({
        url: "/member/emailCheck", // 클라이언트가 HTTP 요청을 보낼 서버의 URL 주소
        data: {email: email},  // HTTP 요청과 함께 서버로 보낼 데이터
        method: "GET",   // HTTP 요청 메소드(GET, POST 등)
        dataType: "html" // 서버에서 보내줄 데이터의 타입
    })
        // HTTP 요청이 성공하면 요청한 데이터가 done() 메소드로 전달됨.
        .done(function (json) {
            console.log(json);
            if ($("#duplicate").length === 0) {
                $("#emailCheck").prepend("<div id='duplicate'>" + json + "</div>");
            } else {
                $("#duplicate").text(json);
            }
            // $("<div class=\"content\">").html(json.html).appendTo("body");
        })
        // HTTP 요청이 실패하면 오류와 상태에 관한 정보가 fail() 메소드로 전달됨.
        .fail(function (xhr, status, errorThrown) {
            $("#text").html("오류가 발생했다.<br>")
                .append("오류명: " + errorThrown + "<br>")
                .append("상태: " + status);
        })
        //
        .always(function (xhr, status) {
            $("#text").html("요청이 완료되었습니다!");
        });
}