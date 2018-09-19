$(() => {

    function validate(event) {

        event.preventDefault();

        $('#error-list').empty();
        let passportError = false;
        let dobError = false;

        const day = $('#dob-day').val();
        const month = $('#dob-month').val();
        const year = $('#dob-year').val();
        if (!!!$('#passport-number').val().trim()) {
            $('#passport-group').addClass('govuk-form-group--error');
            $('#passport-number').toggleClass('govuk-input--error');
            $('#error-list').append('<li><a href="#passport-group">Please enter your passport number</a></li>');
            $('#passport-group .error-message').show();
            passportError = true;
        } else {
            $('#passport-group').removeClass('govuk-form-group--error');
            $('#passport-number').removeClass('govuk-input--error');
            $('#passport-group .error-message').hide();
        }

        if (!!!day || isNaN(day) || +day < 1 || +day > 31) {
            $('#dob-day').addClass('govuk-input--error');
            dobError = true;
        }

        if (!!!month || isNaN(month) || +month < 1 || +month > 12) {
            $('#dob-month').addClass('govuk-input--error');
            dobError = true;
        }

        if (!!!year || isNaN(year) || +year < 1900 || +year > (new Date()).getFullYear()) {
            $('#dob-year').addClass('govuk-input--error');
            dobError = true;
        }

        if (dobError) {
            $('#dob-group .error-message').show();
            $('#dob-group').addClass('govuk-form-group--error');

            $('ul.govuk-error-summary__list').append('<li><a href="#dob-group">Please enter your date of birth, as written on your passport</a></li>');
        } else {
            $('#dob-group .error-message').hide();
            $('#dob-group').removeClass('govuk-form-group--error');

            $('#dob-day').removeClass('govuk-input--error');
            $('#dob-month').removeClass('govuk-input--error');
            $('#dob-year').removeClass('govuk-input--error');
        }

        if (passportError || dobError) {
            $('#error-list-body').show();
            $('.govuk-error-summary').removeAttr("style");
            $('.govuk-error-summary').show();
        } else {
            $('#error-list-body').hide();

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

