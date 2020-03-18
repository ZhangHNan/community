function post(){
    var questionId = $("#questionId").val();
    var commentContent = $("#commentContent").val();
    $.ajax({
        type:"POST",
        url:"/comment",
        contentType:"application/json",
        data:JSON.stringify({
            "parentId": questionId,
            "content": commentContent,
            "type": 1
        }),
        success:function (response) {
            if(response.code == 200){
                $("#comment_area").hide();
            }else{
                if(response.code == 3005){
                    var isAccepted = confirm(response.message);
                    if(isAccepted){
                        window.open("https://github.com/login/oauth/authorize?client_id=c14b8a6a1a6b4e02b9a1&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        window.localStorage.setItem("closable","true");//设置closable，当登录成功跳转到首页时判断，如果有closable等于true则删除closable并关闭浏览器
                    }
                }else{
                    alert(response.message);
                }
            }
        },
        dataType:"json"
    })
}