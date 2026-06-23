/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/

define(function(require, exports, module) {
    var plugins = require('plugins');
	var Authc = require('authc');

    var wpexLocalize = {
    		"mobileMenuOpen" : "Click here to navigate",
    		"mobileMenuClosed" : "Close navigation",
    		"isOriginLeft" : "1"
    	};
    
    // 图片懒加载
    // var imagesLazyload = function () {
    // 	require.async('lazyload', function () {
    // 		$("img").lazyload({
	//    	   		 placeholder: _MTONS.BASE_PATH + '/dist/images/spinner.gif',
	//    	   		 effect: "fadeIn"
	//    	   	});
    //     });
    // }
    
    // 返回顶部
    var backToTop = function () {
    	var $window = $(window);
    	var $scrollTopLink = $( 'a.site-scroll-top' );
		$window.scroll(function () {
			if ($(this).scrollTop() > 100) {
				$scrollTopLink.fadeIn();
			} else {
				$scrollTopLink.fadeOut();
			}
		});		
		$scrollTopLink.on('click', function() {
			$( 'html, body' ).animate({scrollTop:0}, 400);
			return false;
		} );
    }
    
	// 绑定按钮事件
	var bindClickEvent = function () {
		// Like
		$(document).on('click', 'a[rel=like]', function (e) {
			e.preventDefault();
			var $btn = $(this);
			var id = $btn.attr('data-id');

			if (!Authc.isAuthced()) {
				Authc.showLogin();
				return false;
			}

			if (parseInt(id) > 0) {
				var liked = $btn.text().indexOf('已赞') >= 0;
				var url = liked ? '/user/unlike' : '/user/like';
				jQuery.getJSON(_MTONS.BASE_PATH + url, {'id': id}, function (ret) {
					if (ret.code >= 0 && ret.data) {
						if (ret.data.liked) {
							$btn.html('<i class="icon icon-heart"></i> 已赞 <strong class="post-like-count">' + ret.data.likes + '</strong>');
						} else {
							$btn.html('<i class="icon icon-heart"></i> 点赞 <strong class="post-like-count">' + ret.data.likes + '</strong>');
						}
					} else {
						layer.msg(ret.message || '操作失败', {icon: 5});
					}
				}).fail(function () {
					layer.msg('请求失败，请确认已登录后重试', {icon: 5});
				});
			}
			return false;
		});

		// Favor
		$(document).on('click', 'a[rel=favor]', function (e) {
			e.preventDefault();
			var $btn = $(this);
			var id = $btn.attr('data-id');

			if (!Authc.isAuthced()) {
				Authc.showLogin();
				return false;
			}

			if (parseInt(id) > 0) {
				var favored = $btn.text().indexOf('已收藏') >= 0;
				var url = favored ? '/user/unfavor' : '/user/favor';
				jQuery.getJSON(_MTONS.BASE_PATH + url, {'id': id}, function (ret) {
					if (ret.code >= 0 && ret.data) {
						if (ret.data.favored) {
							$btn.html('<i class="icon icon-star"></i> 已收藏 <strong class="post-favor-count">' + ret.data.favors + '</strong>');
						} else {
							$btn.html('<i class="icon icon-star"></i> 收藏 <strong class="post-favor-count">' + ret.data.favors + '</strong>');
						}
					} else {
						layer.msg(ret.message || '操作失败', {icon: 5});
					}
				}).fail(function () {
					layer.msg('请求失败，请确认已登录后重试', {icon: 5});
				});
			}
			return false;
		});

		// Follow
		$(document).on('click', 'a[rel=follow]', function (e) {
			e.preventDefault();
			var $btn = $(this);
			var userId = $btn.attr('data-id');

			if (!Authc.isAuthced()) {
				Authc.showLogin();
				return false;
			}

			if (parseInt(userId) > 0) {
				var following = $btn.text().indexOf('已关注') >= 0;
				var url = following ? '/user/unfollow' : '/user/follow';
				jQuery.getJSON(_MTONS.BASE_PATH + url, {'userId': userId}, function (ret) {
					if (ret.code >= 0 && ret.data) {
						$btn.text(ret.data.following ? '已关注' : '关注');
					} else {
						layer.msg(ret.message || '操作失败', {icon: 5});
					}
				}).fail(function () {
					layer.msg('请求失败，请确认已登录后重试', {icon: 5});
				});
			}
			return false;
		});

		//$(document).pjax('a[rel=pjax]', '#wrap', {
		//	fragment: '#wrap',
		//	timeout: 10000,
		//	maxCacheLength: 0
		//});
	}

    exports.init = function () {
    	// imagesLazyload();
    	backToTop();
		bindClickEvent();
        $('[data-toggle="tooltip"]').tooltip();
    };
    
});