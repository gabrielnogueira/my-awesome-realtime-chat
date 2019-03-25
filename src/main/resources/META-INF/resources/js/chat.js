$ = jQuery;

$(document).ready(init);

function init() {
	$(".messages").animate({
		scrollTop : $(document).height()
	}, "fast");

	$(".content .chat-content").removeClass("show");
	$(".content [name= '" + $("#contacts ul li .active").attr("name") + "']")
			.addClass("show");

}

function expand() {
	$("#profile").toggleClass("expanded");
	$("#contacts").toggleClass("expanded");
}

function selectContact(el) {
	$("#contacts ul li").removeClass("active");
	addObserver();
	rcSelectContact([ {
		name : 'selectedContact',
		value : $(el).attr("name")
	} ]);
}

function scrollDown() {
	$(".messages:visible").each(function(index) {
		$(this).scrollTop($(this)[0].scrollHeight - $(this)[0].clientHeight);
	});
}

function addObserver() {
	var div = document.getElementById("frame");
	if (!div) {
		window.setTimeout(addObserver, 500);
		return;
	}
	var observer = new MutationObserver(function(event) {
		scrollDown()
	});
	observer.observe(div, {
		attributes : true,
		childList : true,
		characterData : true,
		subtree : true,
	});
}

// function addObserver() {
// var divs = $(".messages");
// if(!divs || divs.length == 0) {
// window.setTimeout(addObserver,500);
// return;
// }
// var observer = new MutationObserver(function(event){scrollDown()});
// $(".messages").each(function( index ) {
// observer.observe($(this)[0],{
// attributes: true,
// childList: true,
// characterData: true,
// subtree: true,
// });
// });
//   
// }

function capitalize() {
	$(".capitalize").each(function(index) {
		$(this).text(capitalizeFirstLetter($(this).text()))
	});
}

function capitalizeFirstLetter(string) {
	return string.charAt(0).toUpperCase() + string.slice(1).toLowerCase();
}
