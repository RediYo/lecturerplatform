$(document).ready(function () {

    $.ajax({
        url: "/category",
        type: "GET",
        datatype: "json",/*返回的就是一个json对象*/
        success: function (data) {
            let dataObj = data;
            let divHead = " <div style='display: inline-block; width: 100px;'> ";
            let divEnd = " </div> ";
            let divAll;
            $(dataObj).each(function () {
                divAll = divHead + "<a href=" + "'/student/category?id=" + this.id + "'>" + this.category + "</a>" + divEnd;
                $("#category").append(divAll);
            })
        }
    });

    $.ajax({
        url: "/message-num",
        type: "GET",
        datatype: "text",
        success: function (data) {
           $("#badge").text(data);
        }
    });


    $("#dropdownMenu1").hover(
        function () {
            $("#dropDownCur").show();
        });//为了鼠标可以进入下拉框
    $("#dropDownCur").hover(function () {
        $(this).show();//鼠标进入下拉框
    }, function () {
        $(this).hide();//鼠标离开下拉框后，就会消失
    });


//表单验证js
    $('form').validate({

        onFocus: function() {

            this.parent().addClass('active');

            return false;

        },

        onBlur: function() {

            var $parent = this.parent();

            var _status = parseInt(this.attr('data-status'));

            $parent.removeClass('active');

            if (!_status) {

                $parent.addClass('error');

            }

            return false;

        }

    });


    $('form').on('submit', function(event) {

        //event.preventDefault();

       let result=$(this).validate('submitValidate'); //return boolean;
        console.log(result);
        if(result){
            return true;
        }else {
            return  false;
        }

    });

});
function courseClick(id) {
    window.location.href = "/student/course/view?id=" + id;
}
/*function check(event) {

    event.preventDefault();
    let result=$(this).validate('submitValidate'); //return boolean;
    if(result){
        return true;    //return true; 时,表单将提交
    } else {
        return false;   //return false; 时,表单不提交
    }
}*/
