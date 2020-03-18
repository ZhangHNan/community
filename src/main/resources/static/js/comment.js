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
                alert(response.message);
            }
        },
        dataType:"json"
    })
}