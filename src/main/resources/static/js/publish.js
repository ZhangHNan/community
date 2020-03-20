function selectTag(e) {
    var value = e.getAttribute("data-tag");
    var previous = $("#tag").val();
    if(previous.indexOf(value)==-1){
        if (previous){
            $("#tag").val(previous + ','+value);
        } else{
            $("#tag").val(value);
        }
    }
}
function showSelectTag() {
    $('#select-tag').show();
}