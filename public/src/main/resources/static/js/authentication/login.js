$(() => {

    function validate(event) {

        event.preventDefault();

        $('ul.error-summary-list').empty();
        let passportError = false;
        let dobError = false;

        const day = $('#dob-day').val();
        const month = $('#dob-month').val();
        const year = $('#dob-year').val();

        if (!!!$('#passport-number').val().trim()) {
            $('#passport-group').addClass('form-group-error');
            $('#passport-number').toggleClass('invalid-input');
            $('ul.error-summary-list').append('<li><a href="#passport-group">Please enter your passport number</a></li>');
            $('#passport-group .error-message').show();
            passportError = true;
        } else {
            $('#passport-group').removeClass('form-group-error');
            $('#passport-group .error-message').hide();
        }

        if (!!!day || isNaN(day) || +day < 1 || +day > 31) {
            $('#dob-day').addClass('invalid-input');
            dobError = true;
        }

        if (!!!month || isNaN(month) || +month < 1 || +month > 12) {
            $('#dob-month').addClass('invalid-input');
            dobError = true;
        }

        if (!!!year || isNaN(year) || +year < 1900 || +year > (new Date()).getFullYear()) {
            $('#dob-year').addClass('invalid-input');
            dobError = true;
        }

        if (dobError) {
            $('#dob-group .error-message').show();
            $('#dob-group').addClass('form-group-error');

            $('ul.error-summary-list').append('<li><a href="#dob-group">Please enter your date of birth, as written on your passport</a></li>');
        } else {
            $('#dob-group .error-message').hide();
            $('#dob-group').removeClass('form-group-error');

            $('#dob-day').removeClass('invalid-input');
            $('#dob-month').removeClass('invalid-input');
            $('#dob-year').removeClass('invalid-input');
        }

        if (passportError || dobError) {
            $('.validation-summary.error-summary').show();
        } else {
            $('.validation-summary.error-summary').hide();

            $.get('/csrf',function(data) {
                $('input[name=' + data.parameterName + ']').val(data.token);
                $('#login-form').submit();
            });
        }
    }

    function acceptOnlyNumbers(event) {
        var controlKeys = [8,   // backspace
                   9,           // tab
                   35, 36,      // home,end
                   37,38,39,40, // left, up, right, down arrows
                   46];         // delete

        if (!String.fromCharCode(event.which).match(/[0-9]/) && controlKeys.indexOf(event.keyCode) === -1) {
            return false;
        }
    }

    $('#loginSubmit').click(validate);
    $('#dob-day').keypress(acceptOnlyNumbers);
    $('#dob-month').keypress(acceptOnlyNumbers);
    $('#dob-year').keypress(acceptOnlyNumbers);

});

