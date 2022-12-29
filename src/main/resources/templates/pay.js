$("#submitPayment").on('click',function() {
    let form = new FormData();
    let creditCard = $("#creditCardNumber1").val() ;
    form.append("creditCardNumber" , creditCard);
    form.append("cvv2" , $("#cvv2").val());
    $.ajax({
        url: "http://localhost:8080/" ,
        data: form,
        type: "POST",
        contentType: false,
        dataType: 'text',
        headers: {
            "captcha-response": captchaResponse
        },
        processData: false,
        cache: false,
        success: function (msg) {
            alert("successful")
        }, error: function (request) {
        }
    });
});