$(() => {
    $('#confirmationSummary').hide();

    if (!!$('#sentEmail').length) $('#sendVisa').hide();

    $('#recipient').change(() => $('#confirmRecipient').text($('#recipient').val()));

    function toggleSendAndConfirmationVisibility() {
        $('#sendVisa').toggle();
        $('#confirmationSummary').toggle();
    }

    $('#sendButton').click(toggleSendAndConfirmationVisibility);

    $('#recipient').keypress((e) => {
        if (e.keyCode === 13) {
            e.preventDefault();
            toggleSendAndConfirmationVisibility();
        }
    });

    $('#cancelSend').click((e) => {
        e.preventDefault();
        toggleSendAndConfirmationVisibility();
        $('#recipient').focus();
    });

    $('#sendAgain').click((e) => {
        e.preventDefault();
        $('#sendVisa').toggle();
        $('#sentEmail').toggle();
        $('#recipient').focus();
    });
});