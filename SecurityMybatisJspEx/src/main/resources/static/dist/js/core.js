// 메뉴를 드래그하여 좌우로 스크롤
window.addEventListener("load", () => {
	const slider = document.querySelector('header nav>ul');
	
	if(! slider) {
		return;
	}
	
	let isDown = false;
	let isDrag = false;
	let startX;
	let scrollLeft;

    slider.addEventListener('mousedown', e => {
		isDown = true;
		isDrag = false;
		startX = e.pageX - slider.offsetLeft;
		scrollLeft = slider.scrollLeft;
    });
    slider.addEventListener('mouseleave', () => {
		isDown = false;
    });

    slider.addEventListener('mousemove', e => {
		if (! isDown ) return;
		isDrag = true;
		e.preventDefault();
        
		const x = e.pageX - slider.offsetLeft;
		const walk = x - startX;
		slider.scrollLeft = scrollLeft - walk;
    });
    
    slider.addEventListener('mouseup', e => {
		isDown = false;
		if( ! isDrag ) {
			let url = e.target.dataset.url;
			if( ! url) return;
			if(url === "#") return;
			
			location.href = url;
		}
    });
});

// 메뉴 상단 고정
$(function(){
	$(window).scroll(function() {
		if ($(this).scrollTop() > 100) {
			$('nav').addClass('fixed-top');
		} else {
			$('nav').removeClass('fixed-top');
		}
	});
	
	if ($(window).scrollTop() > 100) {
		$('nav').addClass('fixed-top');
	}
});

// 메뉴 활성화
$(function(){
    var pathname = window.location.pathname;

	$('nav ul>li>span').each(function() {
		let url = $(this).attr("data-url");
		if(url === pathname) {
			$(this).addClass('active');
			return false;
		}
	});
	if($('nav ul>li>span').hasClass("active")) return false;
	
	$('nav ul>li>span').each(function() {
		let url = $(this).attr("data-url");
		if(pathname.indexOf(url) === 0) {
			$(this).addClass('active');
			return false;
		}
	});
});
 