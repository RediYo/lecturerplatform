// user
/*var user_Boolean = false;*/
var password_Boolean = false;
var varconfirm_Boolean = false;
var emaile_Boolean = false;
var Mobile_Boolean = false;
var name_boolean=false;
var profile_boolean=false;
/*$('.reg_user').blur(function(){
    if ((/^[a-z0-9_-]{4,8}$/).test($(".reg_user").val())){
        $('.user_hint').html("✔").css("color","green");
        user_Boolean = true;
    }else {
        $('.user_hint').html("X").css("color","red");
        user_Boolean = false;
    }
});*/
// password
$('.form-password').blur(function(){
    if ((/^[a-z0-9_-]{6,16}$/).test($(".form-password").val())){
        $('.password_hint').html("✔").css("color","green");
        password_Boolean = true;
    }else {
        $('.password_hint').html("X").css("color","red");
        password_Boolean = false;
    }
});


// password_confirm
$('.form-confirm').blur(function(){
    if (($(".form-password").val())===($(".form-confirm").val())){
        $('.confirm_hint').html("✔").css("color","green");
        varconfirm_Boolean = true;
    }else {
        $('.confirm_hint').html("X").css("color","red");
        varconfirm_Boolean = false;
    }
});


// Email
$('.form-email').blur(function(){
    if ((/[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/).test($(".form-email").val())){
        $('.email_hint').html("✔").css("color","green");
        emaile_Boolean = true;
    }else {
        $('.email_hint').html("X").css("color","red");
        emaile_Boolean = false;
    }
});

$('.form-name').blur(function(){
    if ((/^[\s\S]*.*[^\s][\s\S]*$/).test($(".form-name").val())){
        $('.name_hint').html("✔").css("color","green");
        name_boolean = true;
    }else {
        $('.name_hint').html("X").css("color","red");
        name_boolean = false;
    }
});

$('.form-profile').blur(function(){
    if ((/^[\s\S]*.*[^\s][\s\S]*$/).test($(".form-profile").val())){
        $('.profile_hint').html("✔").css("color","green");
        profile_boolean = true;
    }else {
        $('.profile_hint').html("X").css("color","red");
        profile_boolean = false;
    }
});


// Mobile
$('.form-mobile').blur(function(){
    if ((/^1[34578]\d{9}$/).test($(".form-mobile").val())){
        $('.mobile_hint').html("✔").css("color","green");
        Mobile_Boolean = true;
    }else {
        $('.mobile_hint').html("X").css("color","red");
        Mobile_Boolean = false;
    }
});


// click
$('#btn-submit').click(function(){
    //console.log($("#checkbox-agree").prop("checked"));
    if(name_boolean && password_Boolean && varconfirm_Boolean && emaile_Boolean && Mobile_Boolean && profile_boolean === true&&$("#checkbox-agree").prop("checked")){
        alert("申请成功！等待管理员审核");
        $("form").submit();
    }else {
        alert("请完善信息");
    }
});
