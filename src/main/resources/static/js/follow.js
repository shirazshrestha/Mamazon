$(document).ready(function () {

    $("#followForm").submit(function (e) {
        e.preventDefault();
        const data = $( this ).serializeArray();
        const follow = $('#followBtn span').text().toLowerCase();
        const url = '/buyer/'+follow+'/'+data[0].value;
        console.log(url);

        $.ajax({
            type : 'GET',
            url : url,
            success:function(){
                if (follow === 'unfollow') {
                    $('#followBtn').removeClass('btn-danger');
                    $('#followBtn').addClass('btn-success');
                    $('#followBtn span').text('Follow');
                } else {
                    $('#followBtn').removeClass('btn-success');
                    $('#followBtn').addClass('btn-danger');
                    $('#followBtn span').text('Unfollow');
                }
            },
            error: function (e) {
                console.log(e.responseJSON);
            }
        });

    });



});