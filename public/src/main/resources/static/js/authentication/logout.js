$(() => {

    function validate(event) {

        event.preventDefault();

        $.get('/csrf', function (data) {
            $('input[name=' + data.parameterName + ']').val(data.token);
            $('#logout-form').submit();
        });
    }

    $('#logoutSubmit').click(validate);
});

