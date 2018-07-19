$(function () {
    let $navArr = [
		baseurl + 'front/home.action',
		baseurl + 'front/campManage/camp_list.action',
		baseurl + 'front/report/time.action',
		baseurl + 'front/adPlace/toList.action',
		baseurl + 'front/account/toInfo.action'
    ];
    let $left = $('.nav-hover').css('left');
    let $initialValue = $left.toString().slice(0, $left.length - 2);
    let $startSize = 155;
    let $moveSize = 194;
    let $navList = $('.nav-list');
    for (let i = 0; i < $navList.length; i++) {
        $navList.eq(i).mouseenter(function () {
            let $moveLeft = $startSize + $moveSize * i;
            // console.log($moveLeft);
            // console.log( $navArr[i]);
            $('.nav-hover').stop().animate({left: $moveLeft + 'px'}, 200);
            $('.nav-hover').html($navList.eq(i).html());
            $('.nav-hover').attr('href', $navArr[i]);
        });


    }
    $('.header').mouseleave(function () {
        $('.nav-hover').animate({left: $initialValue + 'px'}, 200).html($('.nav-list.active').html());

    });
   
});