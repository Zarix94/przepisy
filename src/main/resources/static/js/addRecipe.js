$( document ).ready(function(){
    $('#btnAddReceipt').click(function(){
        addRecipe();
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

function validateRecipe(){
    var result = true;
    $('input').each(function(idx,elm){
        if($(elm).attr('id') != 'btnAddReceipt' && $(elm).val() ==''){
            result = false;
            setError($(elm).attr('id'), "Wartość nie może być pusta");
        } else
            removeError($(elm).attr('id') );
    });

    $('textarea').each(function(idx,elm){
        if($(elm).val() ==''){
            result = false;
            setError($(elm).attr('id'))
        } else
            removeError($(elm).attr('id') );
    });

    return result;
}

function addRecipe(){
    if(validateRecipe()){
        var name = $('#name').val();
        var diff = $('#diff').val();
        var ingredients = $('#ingredients').val();
        var description = $('#steps').val();

        $.post( 'saveRecipe', {
            name: name,
            diff: diff,
            ingredients: ingredients,
            description: description
        }, function(resJson) {
        var res = JSON.parse(resJson);

            if(res.result == true){
                $('input').each(function(idx, elm){
                    if($(elm).attr('id')!= 'btnAddReceipt')
                        $(elm).val('');
                });
                $('textarea').each(function(idx,elm){
                        $(elm).val('');
                });

                removeError('input');
                alert('Dodano przepis');
            } else {
                $(res.errors).each(function(idx, elm){
                    result = setError(elm.field, elm.message);
                })


            }
        });
    }
}

