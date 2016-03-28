$(function(){
    var shrinkHeader = 300;
    $(window).scroll(function() {
        var scroll = getCurrentScroll();
        if ( scroll >= shrinkHeader ) {
            $('.header-container').addClass('shrink');
        }
        else {
            $('.header-container').removeClass('shrink');
        }
    });
    function getCurrentScroll() {
        return window.pageYOffset || document.documentElement.scrollTop;
    }
});