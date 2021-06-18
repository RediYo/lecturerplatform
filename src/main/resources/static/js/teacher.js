$(document).ready(function () {



    $("#dropdownMenu1").hover(
        function () {
            $("#dropDownCur").show();
        });//为了鼠标可以进入下拉框
    $("#dropDownCur").hover(function () {
        $(this).show();//鼠标进入下拉框
    }, function () {
        $(this).hide();//鼠标离开下拉框后，就会消失
    });

    $.ajax({
        url: "/message-num",
        type: "GET",
        datatype: "text",
        success: function (data) {
            $("#badge").text(data);
        }
    });

});