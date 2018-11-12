$(() => {

    function validate(event) {

        event.preventDefault();

        let error_lst = $('#error-list');
        let error_sum = $('.govuk-error-summary');

        error_sum.hide();
        error_lst.empty();

        let error = false;

        if (!!!$('#your-name').val().trim()) {
            $('#your-name-group').addClass('govuk-form-group--error');
            $('#your-name').toggleClass('govuk-input--error');
            error_lst.append('<li><a href="#your-name-group">Please enter your name</a></li>');
            $('#your-name-group .error-message').show();
            error = true;
        } else {
            $('#your-name-group').removeClass('govuk-form-group--error');
            $('#your-name').removeClass('govuk-input--error');
            $('#your-name-group .error-message').hide();
        }

        if (!!!$('#your-email-address').val().trim()) {
            $('#your-email-address-group').addClass('govuk-form-group--error');
            $('#your-email-address').toggleClass('govuk-input--error');
            error_lst.append('<li><a href="#your-email-address-group">Please enter your email address</a></li>');
            $('#your-email-address-group .error-message').show();
            error = true;
        } else {
            $('#your-email-address-group').removeClass('govuk-form-group--error');
            $('#your-email-address').removeClass('govuk-input--error');
            $('#your-email-address-group .error-message').hide();
        }

        if (!!!$('#your-passport-number').val().trim()) {
            $('#your-passport-number-group').addClass('govuk-form-group--error');
            $('#your-passport-number').toggleClass('govuk-input--error');
            error_lst.append('<li><a href="#your-passport-number-group">Please enter your passport number</a></li>');
            $('#your-passport-number-group .error-message').show();
            error = true;
        } else {
            $('#your-passport-number-group').removeClass('govuk-form-group--error');
            $('#your-passport-number').removeClass('govuk-input--error');
            $('#your-passport-number-group .error-message').hide();
        }

        if (!!!$('#issue-detail').val().trim()) {
            $('#issue-detail-group').addClass('govuk-form-group--error');
            $('#issue-detail').toggleClass('govuk-input--error');
            error_lst.append('<li><a href="#issue-detail-group">Please enter your issue details</a></li>');
            $('#issue-detail-group .error-message').show();
            error = true;
        } else {
            $('#issue-detail-group').removeClass('govuk-form-group--error');
            $('#issue-detail').removeClass('govuk-input--error');
            $('#issue-detail-group .error-message').hide();
        }

        if (error) {
            $('#error-list-body').show();
            error_sum.removeAttr("style");
            error_sum.show();
        } else {
            $('#error-list-body').hide();
            $.get('/csrf',function(data) {
                console.log('Submit: ' + data.token);
                $('input[name=' + data.parameterName + ']').val(data.token);

                let form = document.getElementById('issue-form');
                form.submit();
            });
        }
    }

    $('#issueSubmit').click(validate);

});

