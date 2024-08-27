$(function() {
    $("#detailImg").on("change", function(event) {

        var files = event.target.files;
        if(files.length != 0) {
            $(".file-list").remove();
            for(let i = 0; i < files.length; i++) {
                var reader = new FileReader();
                reader.onload = function(e) {
                    $(".insert").append($("<img>", {"src" : e.target.result, "class" : "file-list"}));
                }
                reader.readAsDataURL(files[i]);
            }
        }
        else {
            $(".file-list").remove();
        }
    });
});

function getUrlParameter(name) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(name);
}

const cancelButton = document.getElementById('cancel-btn');

if(cancelButton){
    cancelButton.addEventListener('click', () => location.href = '/myPage/reviewList' + (getUrlParameter('reviewId') === null ? '' : '?tab=1'))
}