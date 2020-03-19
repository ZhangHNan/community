//提交帖子的回复（评论）
function post(){
    var questionId = $("#questionId").val();
    var commentContent = $("#commentContent").val();
    comment2target(questionId,1,commentContent)
}

function secondPost(e){
    var id = e.getAttribute("data-id");
    var contentId = "#input-"+id;
    var content = $(contentId).val();
    console.log(id);
    console.log(content);
    comment2target(id,2,content);
}

//评论功能
function comment2target(targetId, type, content){
    if (!content) {
        alert("评论不能为空！！！");
        return;
    }
    $.ajax({
        type:"POST",
        url:"/comment",
        contentType:"application/json",
        data:JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success:function (response) {
            if(response.code == 200){
                window.location.reload();
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


//展开二级评论
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-"+id);
    //获取一下二级评论的展开状态
    var collapse = e.getAttribute("data-collapse","in");
    if (collapse){
        //折叠二级评论
        comments.removeClass("in");
        //
        e.removeAttribute("data-collapse","in");
        e.classList.remove("active");
    }else{
        //展开二级评论
        comments.addClass("in");
        //标记二级评论展开状态
        e.setAttribute("data-collapse","in");
        e.classList.add("active");

    }
}