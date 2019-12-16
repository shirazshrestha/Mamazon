$(document).ready(function() {

    $('.qtyChange').change(function () {
        const url = '/buyer/updateline/'+this.title+'/'+this.value;
        const data = this.value;
        $.ajax({
            type : 'GET',
            url : url,
            contentType: "application/json",
            dataType: "json",
            success:function(results){
                console.log(results);
            },
            error: function (e) {
                console.log(e.responseJSON);
            }
        });
    });
});