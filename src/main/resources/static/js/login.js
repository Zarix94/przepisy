$( document ).ready(function(){

$('#btnLogin').click(function(){

    btnLoginOnClick();
});







});



function setError(selector, error){
    $('#' + selector).addClass('input-error');
    $('#' + selector).attr('title', error);
    $('#' + selector).tooltip();
    return false;
}

function removeError(selector){
    if(selector != 'input')
        selector = '#' + selector
    $(selector).removeClass('input-error');
    $(selector).attr('title', '');
    $(selector).tooltip('disable');
}

function validLogin(){
    var result = true;

    if($('#login').val() == "")
        result = setError('login', 'Wartość nie może być pusta');
     else
        removeError('login');

    return result;
}

function validPassword(){
    var result = true;

    if($('#password').val() == "" )
        result = setError('password', 'Wartość nie może być pusta');
    else if($('#password').val().length < 6){
        result = setError('password', 'Hasło musi zawierać co najmniej 6 znaków');
    } else
        removeError('password');

    return result;
}



function validateLogin(){
    var result = true;

    if(!validLogin())
        result = false;
    if(!validPassword())
        result = false;

    return result;
}

function btnLoginOnClick (){
    if(validateLogin()){
        var login = $('#login').val();
        var password = $('#password').val();

        $.post( 'loginUser', {
            login: login,
            password: password
        }, function(resJson) {
        var res = JSON.parse(resJson);

            if(res.result == true){
                $('input').each(function(idx, elm){
                    if($(elm).attr('id')!= 'btnRegister')
                        $(elm).val('');
                });
                removeError('input');
                alert('Dziękujemy za dołączenie do naszej społeczności. Od teraz możesz tworzyć wraz z Nami kulinarny świat!');
            } else {
                $(res.errors).each(function(idx, elm){
                    result = setError(elm.field, elm.message);
                })
            }
        });
    }
}