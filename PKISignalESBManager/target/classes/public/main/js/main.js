var WSM = {};
WSM.main = {};
WSM.Utils = {};
WSM.Utils.Date = {};

WSM.main.init = function(element){
	var $tab = $(".content_left_nav dd").eq(0).children('a'),
		url = $tab.attr('_href'),
		title = $tab.text(),
		tabId = $tab.attr('tabId');
		WSM.Utils.Tab('#WSMTabContent','WSMTabContent',element).tabAdd(title, url, tabId);
		WSM.Utils.Tab('#WSMTabContent','WSMTabContent',element).tabChange(tabId);
};
/**
 * 本地存储 localStorage
 * 为了保持统一，将sessionStorage更换为存储周期更长的localStorage
 * tab页面都存在sessionStorage menu中了
 */
//本地存储记录所有打开的窗口
var setStorageMenu=function(title, url, id) {
	var menu = JSON.parse(sessionStorage.getItem('menu'));
	if(menu) {
		var deep = false;
		for(var i = 0; i < menu.length; i++) {
			if(menu[i].id == id) {
				deep = true;
				menu[i].title = title;
				menu[i].url = url;
				menu[i].id = id;
			}
		}
		if(!deep) {
			menu.push({
				title: title,
				url: url,
				id: id
			})
		}
	} else {
		var menu = [{
			title: title,
			url: url,
			id: id
		}]
	}
	sessionStorage.setItem('menu', JSON.stringify(menu));
};
//本地存储记录当前打开窗口
var setStorageCurMenu=function() {
	var curMenu = sessionStorage.getItem('curMenu');
	var text = $('.layui-tab-title').find('.layui-this').text();
	text = text.split('ဆ')[0];
	var url = $('.layui-tab-content').find('.layui-show').find('.WSMTabframe').attr('src');
	var id = $('.layui-tab-title').find('.layui-this').attr('lay-id');
	curMenu = {
		title: text,
		url: url,
		id: id
	}
	sessionStorage.setItem('curMenu', JSON.stringify(curMenu));
};
//本地存储中移除删除的元素
var removeStorageMenu=function(id) {
	var menu = JSON.parse(sessionStorage.getItem('menu'));
	if(menu) {
		var deep = false;
		for(var i = 0; i < menu.length; i++) {
			if(menu[i].id == id) {
				deep = true;
				menu.splice(i, 1);
			}
		}
	} else {
		return false;
	}
	sessionStorage.setItem('menu', JSON.stringify(menu));
};
/*
 * 监听右键事件,绑定右键菜单
 * 先取消默认的右键事件，再绑定菜单，触发不同的点击事件
 */
var CustomRightClick=function(id) {
	//取消右键 
	$('.layui-tab-title li').on('contextmenu', function() {
		return false;
	})
	$('.layui-tab-title,.layui-tab-title li').on('click', function() {
		$('.rightMenu').hide();
	});
	//桌面点击右击 
	$('.layui-tab-title li').on('contextmenu', function(e) {
		var aid = $(this).attr("lay-id"); //获取右键时li的lay-id属性
		var popupmenu = $(".rightMenu");
		popupmenu.find("li").attr("data-id", aid);
		l = ($(document).width() - e.clientX) < popupmenu.width() ? (e.clientX - popupmenu.width()) : e.clientX;
		t = ($(document).height() - e.clientY) < popupmenu.height() ? (e.clientY - popupmenu.height()) : e.clientY;
		popupmenu.css({
			left: l,
			top: t
		}).show();
		return false;
	});
};
/*
 * tab触发事件：增加、删除、切换
 * $tabId：
 * layfilter：
 * element:layui的element元素
 * WSM.Utils.Tab('#tablId','layfilter')
 */
WSM.Utils.Tab =function($tabId,layfilter,element) {
	return {
		/**
		 * title:tab名称
		 * url:跳转内容的url地址
		 * id:tab具体一个tab页面的lay-id值
		 */
		tabAdd: function(title, url, id) {
			//判断当前id的元素是否存在于tab中
			var li = $($tabId+" li[lay-id=" + id + "]").length;
			if(li > 0) {
				//tab已经存在，直接切换到指定Tab项
				element.tabChange(layfilter, id); //切换到：用户管理
			} else {
				//该id不存在，新增一个Tab项
				element.tabAdd(layfilter, {
					title: title,
					content: '<iframe width="100%" height="100%" tab-id="' + id + '" frameborder="0" src="' + url + '" scrolling="yes" class="WSMTabframe"></iframe>',
					id: id
				});
				//当前窗口内容
				setStorageMenu(title, url, id);

			}
			CustomRightClick(id); //绑定右键菜单

		},
		tabDelete: function(id) {
			element.tabDelete(layfilter, id); //删除
			removeStorageMenu(id);

		},
		tabChange: function(id) {
			//切换到指定Tab项
			element.tabChange(layfilter, id);
		},
		tabDeleteAll: function(ids) { //删除所有
			$.each(ids, function(i, item) {
				element.tabDelete(layfilter, item);
			})
			sessionStorage.removeItem('menu');
		}
	}
};
WSM.main.bind = function(element,layer){
	//编辑用户信息
	$("#editUserInfo").click(function(){
		//打开编辑窗口
		layer.open({
			type:1,
			content:$("#operate"),
			title:"编辑",
			btn:["提交","取消"],
			btnAlign:'c',
			yes:function(index,layero){//点击确认的回调函数
				//获得用户权限
				var roleIds=new Array(),
					roleNames = new Array(),
					roleuser = new Array();
				$(".forroles").each(function(){
					var roleId=$(this).attr('value');
					roleIds.push(roleId);
				});
				$(".roleName").each(function(){
					var roleName=$(this).text();
					roleNames.push(roleName);
				});
				for (var i = 0; i < roleIds.length; i++) {
					var ele = roleIds[i]+":"+roleNames[i];
					roleuser.push(ele);
				}
				roleIds=roleIds.join(",");
				roleNames=roleNames.join(",");
				roleuser=roleuser.join(",");
				var user={
					UserName:$("#UserName").val(),
					PassWord:$("#PassWord").val(),
					CellPhone:$("#CellPhone").val(),
					Email:$("#Email").val(),
					RoleIds:roleIds,
					isModify:$("#PassWord").data("ismodify")
				};
				layer.close(index);
				//提交信息
				$.ajax({
			    	 type:'post',
			    	 url:'../user.do?editUserById&Id='+id,
			    	 data:user,
			    	 async:true,
			    	 dataType:'json',
			    	 success:function(res){
			    		 if(res.code==0){
			    			layer.msg("操作成功");
			    			//同步更新缓存对应的值
		    			    obj.update({
		    			    	username:user.UserName,
		    					password:user.PassWord,
		    					cellphone:user.CellPhone,
		    					email:user.Email,
		    					roleuser:roleuser,
		    					roleNames:roleNames,
		    					roleIds:roleIds
		    			    });
			    		 }else{
			    			 layer.alert(res.msg);
			    		 }
			    		//清除操作框内容
						$("#UserName").val("");
						$("#PassWord").val("");
						$("#CellPhone").val("");
						$("#Email").val("");
						layer.close(index);
			    	 },
			    	 error:function(){
			    		 layer.alert("数据更新失败！")
			    	 }
			      });
			},
			btn2:function(index,layero){//点击取消的回调函数
				//清除操作框内容
				$("#UserName").val("");
				$("#PassWord").val("");
				$("#CellPhone").val("");
				$("#Email").val("");
				layer.close(index);
			}
		})
	});
	$("#collapsing").click(function(){
		$(".content_left_nav").toggle();
		if($(".content_left_nav").css("display")=='none'){
			$(".content_right").css("width","100%");
		}else{
			$(".content_right").css("width","85%");
		}
	});
	/**
	 *@todo tab切换监听
	 * tab切换监听不能写字初始化加载$(function())方法内，否则不执行
	 */
	element.on('tab(WSMTabContent)', function(data) {
		//console.log(this); //当前Tab标题所在的原始DOM元素
		setStorageCurMenu();
	});
	/*
	 * @todo 监听layui Tab项的关闭按钮，改变本地存储
	 */
	element.on('tabDelete(WSMTabContent)', function(data) {
		var layId = $(this).parent('li').attr('lay-id');
		//console.log(layId);
		removeStorageMenu(layId);
	});
	$("#rightMenu li").click(function() {
		var type = $(this).attr("data-type");
		var layId = $(this).attr("data-id")
		if(type == "current") {
			WSM.Utils.Tab('#WSMTabContent','WSMTabContent',element).tabDelete(layId);
		} else if(type == "all") {
			var tabtitle = $(".layui-tab-title li");
			var ids = new Array();
			$.each(tabtitle, function(i) {
				ids[i] = $(this).attr("lay-id");
			})
			WSM.Utils.Tab('#WSMTabContent','WSMTabContent',element).tabDeleteAll(ids);
		} else if(type == "fresh") {
			WSM.Utils.Tab('#WSMTabContent','WSMTabContent',element).tabChange($(this).attr("data-id"));
			var othis = $('.layui-tab-title').find('>li[lay-id="' + layId + '"]'),
				index = othis.parent().children('li').index(othis),
				parents = othis.parents('.layui-tab').eq(0),
				item = parents.children('.layui-tab-content').children('.layui-tab-item'),
				src = item.eq(index).find('iframe').attr("src");
			item.eq(index).find('iframe').attr("src", src);
		} else if(type == "other") {
			var thisId = layId;
			$('.layui-tab-title').find('li').each(function(i, o) {
				var layId = $(o).attr('lay-id');
				if(layId != thisId && layId != 0) {
					WSM.Utils.Tab('#WSMTabContent','WSMTabContent',element).tabDelete(layId);
				}
			});
		}
		$('.rightMenu').hide();
	});
	
	/*
	 * @todo 左侧菜单事件
	 * 如果有子级就展开，没有就打开frame
	 */
	$('.content_left_nav dd').click(function(event) {
		var url = $(this).children('a').attr('_href');
		var title = $(this).children('a').text();
		var tabId = $(this).children('a').attr('tabId');

		for(var i = 0; i < $('.WSMTabframe').length; i++) {
			if($('.WSMTabframe').eq(i).attr('tab-id') == tabId) {
				WSM.Utils.Tab('#WSMTabContent','WSMTabContent',element).tabChange(tabId);
				event.stopPropagation();
				return;
			}
		};
		WSM.Utils.Tab('#WSMTabContent','WSMTabContent',element).tabAdd(title, url, tabId);
		WSM.Utils.Tab('#WSMTabContent','WSMTabContent',element).tabChange(tabId);
		event.stopPropagation(); //不触发任何前辈元素上的事件处理函数
	});
	//监听导航点击
	element.on('nav(demo)', function(elem){
	   layer.msg(elem.text());
	});
};
WSM.main.initlayui = function(){
	layui.use('element', function(){
		  var element = layui.element; //导航的hover效果、二级菜单等功能，需要依赖element模块
		  //监听导航点击
		  element.on('nav(demo)', function(elem){
		    layer.msg(elem.text());
		  });
		  //tab操作
		});
}
WSM.initUtils = function(layer){
	WSM.Utils.layer = layui.layer;
	//1.暴露出layer工具
	//时间戳的处理
	WSM.Utils.Date.toDateString = function(d, format){
	  var date = new Date(d || new Date())
	  ,ymd = [
	    this.digit(date.getFullYear(), 4)
	    ,this.digit(date.getMonth() + 1)
	    ,this.digit(date.getDate())
	  ]
	  ,hms = [
	    this.digit(date.getHours())
	    ,this.digit(date.getMinutes())
	    ,this.digit(date.getSeconds())
	    ,date.getMilliseconds()
	  ];

	  format = format || 'yyyy-MM-dd HH:mm:ss.ms';

	  return format.replace(/yyyy/g, ymd[0])
	  .replace(/MM/g, ymd[1])
	  .replace(/dd/g, ymd[2])
	  .replace(/HH/g, hms[0])
	  .replace(/mm/g, hms[1])
	  .replace(/ss/g, hms[2])
	  .replace(/ms/g, hms[3]);
	};
	 
	//数字前置补零
	WSM.Utils.Date.digit = function(num, length, end){
	  var str = '';
	  num = String(num);
	  length = length || 2;
	  for(var i = num.length; i < length; i++){
	    str += '0';
	  }
	  return num < Math.pow(10, length) ? str + (num|0) : num;
	};
};
(function(){
	layui.use(['element','layer','form'], function(){
		var element=layui.element;
		var layer = layui.layer;
		WSM.initUtils(layer);
		WSM.main.init(element);
		WSM.main.bind(element,layer);
	})
}());
