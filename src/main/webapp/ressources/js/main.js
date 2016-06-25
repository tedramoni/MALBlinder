$(function () {
    var shrinkHeader = 300;
    $(window).scroll(function () {
        var scroll = getCurrentScroll();
        if (scroll >= shrinkHeader) {
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

var myMessages = ['info', 'warning', 'error', 'success'];

function hideAllMessages() {
    var messagesHeights = new Array(); // this array will store height for each

    for (i = 0; i < myMessages.length; i++) {
        messagesHeights[i] = $('.' + myMessages[i]).outerHeight(); // fill array
        $('.' + myMessages[i]).css('top', -messagesHeights[i]); //move element outside viewport
    }
}

function showMessage(type) {
    $('.' + type + '-trigger').click(function () {
        hideAllMessages();
        $('.' + type).animate({top: "0"}, 500);
    });
}

$(document).ready(function () {

    // Initially, hide them all
    //hideAllMessages();

    // Show message
    for (var i = 0; i < myMessages.length; i++) {
        showMessage(myMessages[i]);
    }

    // When message is clicked, hide it
    $('.message').click(function () {
        $(this).animate({top: -$(this).outerHeight()}, 500);
    });

});

$(function () {
    var scntDiv = $('#p_scents');
    var i = $('#p_scents p').size() + 1;
    var j = $('#p_scents p').size();

    $('body').on('click', '#addScnt', function () {
        $('<p><label class="fieldCompte" size="20" required="required" for="p_scnt"><input type="text" id="p_scnt" name="users['+j+'].username" value="" placeholder="Pseudo myanimelist" onblur="validateUser($(this).val())?$(this).css(\'border-color\', \'green\') : $(this).css(\'border-color\', \'red\');" /></label> <a class="first after supprCompte" href="#" id="remScnt"> <i class="fa fa-minus" aria-hidden="true"></i> Supprimer</a></p>').appendTo(scntDiv);
        i++;
        j++;
        return false;
    });

    $('body').on('click', '#remScnt', function () {
        if (i > 2) {
            $(this).parents('p').remove();
            i--;
            j--;
        }
        return false;
    });
});

function validateUser(user) {
    var existe;

    function set_existe(x){
        existe = x;
    }

    $.ajax({
        type: 'GET',
        url: "https://cors-anywhere.herokuapp.com/http://myanimelist.net/malappinfo.php",
        data: {
            u: user
        },
        dataType: "xml",
        async: false,
        success: function (xml) {
            test = $(xml).find('error').first().text() == 'Invalid username' ? false : true;
            test ? set_existe(true) : set_existe(false);
            alert(test ? 'Le compte ' + user + ' existe !' : 'Le compte ' + user + ' n\'existe pas !');
        },
        error:function(exception){console.log('Exeption:'+exception);}
    });
    if(existe == true){
        return true;
    }
    else {
        return false;
    }
}
