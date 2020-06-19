$( document ).ready(function(){

$('#btnRegister').click(function(){

    btnRegisterOnClick();
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

function validRepeatPassword(){
    var result = true;

    if($('#repeatPassword').val() == "" )
        result = setError('repeatPassword', 'Wartość nie może być pusta');
    else if($('#repeatPassword').val().length < 6)
        result = setError('repeatPassword', 'Hasło musi zawierać co najmniej 6 znaków');
    else if($('#repeatPassword').val() != $('#password').val())
        result = setError('repeatPassword', 'Wprowadzone hasła nie są identyczne');
    else
        removeError('repeatPassword');

    return result;

}

function validEmail(){
    var result = true;

    if($('#email').val() == "" )
        result = setError('email', 'Wartość nie może być pusta');
    else if($('#email').val().indexOf('@')== -1)
        result = setError('email', 'Niepoprawny adres e-mail');
    else
        removeError('email');

    return result;
}

function validRepeatEmail(){
    var result = true;

    if($('#repeatEmail').val() == "" )
        result = setError('repeatEmail', 'Wartość nie może być pusta');
    else if($('#repeatEmail').val().indexOf('@')== -1)
        result = setError('repeatEmail', 'Niepoprawny adres e-mail');
    else if($('#repeatEmail').val() != $('#email').val())
        result = setError('repeatEmail', 'Wprowadzone adresy e-mail nie są identyczne');
    else
        removeError('repeatEmail');

    return result;
}

function validateRegistration(){
    var result = true;

    if(!validLogin())
        result = false;
    if(!validPassword())
        result = false;
    if(!validRepeatPassword())
        result = false;
    if(!validEmail())
        result = false;
    if(!validRepeatEmail())
        result = false;

    return result;
}

function btnRegisterOnClick (){
    if(validateRegistration()){
        var login = $('#login').val();
        var password = $('#password').val();
        var email = $('#email').val();

        $.post( 'registerUser', {
            email: email,
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