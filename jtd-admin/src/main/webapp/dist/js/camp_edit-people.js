$(function () {
    $.getScript("dist/js/campaignDicData.js", function () {

        // 人群定向事件
        $(".tab-list").click(function () {
            if (!$(this).hasClass("on")) {
                $(".tab-list").removeClass("on");
                $(".tab-list").find('i').removeClass("active");
                $(".peopleContainer .crowd-select-left .Tab-Container").hide();
                $(".Tab-Content").hide();

                var id = $(this).attr("id");
                $(this).addClass("on");
                $(this).find('i').addClass("active");
                switch (id) {
                    case "peopleTab":
                        $("#peopleContainer").show();
                        $("#peopleContent").show();
                        break;
                    case "interestTab":
                        $("#interest").show();
                        $("#interestContent").show();
                        break;
                    case "personalityTab":
                        $("#personality").show();
                        $("#personalityContent").show();
                        break;
                    case "guestTab":
                        $("#guest").show();
                        $("#guestContent").show();
                        break;
                    /*  case "ipTab":
                     $("#ip").show();
                     break;*/
                }
            }
        });

        //人群全选事件
        $("#people_all").click(function () {
            var that = $(this);
            var selected = $(this).attr("selected");
            if (selected) {
                $(this).removeAttr("selected");
                $("#people :checkbox").each(function (i, obj) {
                    $(obj).prop("checked", true);
                    that.text('取消');
                })
            } else {
                $(this).attr("selected", "true");
                $("#people :checkbox").each(function (i, obj) {
                    $(obj).prop("checked", false);
                    that.text('全选');
                })
            }
            $("#peopleContainer").trigger("check");
        });

        // 人群选择 - 清空
        $("#clearPeople").click(function () {
            $("#peopleContainer input[type='checkbox']").prop("checked", false);
            $("#peopleContainer").trigger("check");
        });
        // 兴趣定向 - 清空
        $("#clearInterest").click(function () {
            $("#interest input[type='checkbox']").prop("checked", false);
            $("#interestList").trigger("check");
        });
        // 个性化人群定向 - 清空
        $("#clearPersonality").click(function () {
            $("#personality input[type='checkbox']").prop("checked", false);
            $("#personality").trigger("check");
        });
        // 访客找回 - 清空
        $("#clearGuest").click(function () {
            $("#guest input[type='checkbox']").prop("checked", false);
            $("#guest").trigger("check");
        });

        $("#crowd_subdes_more").click(function () {
            var ck = $(this).attr("selected");
            if (ck == "selected") {
                $(this).attr("selected", null);
                $(this).text("收起");
                $(".crowd-subdes-more").slideDown();
            } else {
                $(this).attr("selected", "selected");
                $(this).text("显示");
                $(".crowd-subdes-more").slideUp();
            }
        });

        /**
         * 初始化人群定向
         */
        function initPopulation() {
            var populationOrientation = campaignDicData.populations;// 地区
            for (var key in populationOrientation) {
                var content = '<div class="clearfix"></div><div class="crowd-title">' + key + '：</div><ul>';
                var population = populationOrientation[key];
                $.each(population, function (i, context) {
                    content += '<li>'
                        + ' <div class="checkboxcontainer">'
                        + '     <span class="checkboxinput">'
                        + '         <input id="cookie_item_' + context.id + '" name="cookies" dim-name="' + context.dimName + '" type="checkbox" value="' + context.id + '"/>'
                        + '         <label for="cookie_item_' + context.id + '"></label>'
                        + '     </span>'
                        + '     <span class="checkboxlabel">' + context.ckGroupName + '</span>'
                        + ' </div>'
                        + '</li>';
                });
                content += '</ul>';
                $("#people").append(content);
            }

            // 监测方法
            $("#peopleContainer").bind("check", function () {
                $("#peopleResult ul").empty();
                $("#people :checked").each(function () {
                    var dimName = $(this).attr("dim-name");
                    $("#peopleResult ul[dimName=" + dimName + "]").append("<li>" + $(this).parent().next().text() + "</li>");
                });
                var isAll = true;
                $("#people :checkbox").each(function (i, obj) {
                    if ($(obj).filter(":checked").size() == 0) {
                        isAll = false;
                    }
                })
                if (isAll) {
                    $("#people_all").html("取消");
                    $("#people_all").removeAttr("selected");
                } else {
                    $("#people_all").html("全选");
                    $("#people_all").attr("selected", "true");
                }
            });

            // 已选数据初始化
            var populations_selected = $("#populations").val(); // 人群属性定向
            // 人群属性定向赋值
            if (populations_selected) {
                populations_selected = populations_selected.split(",");
                for (var i = 0; i < populations_selected.length; i++) {
                    $("#cookie_item_" + populations_selected[i] + "").prop("checked", true);
                }
            }
            //人口定向匹配模式 (and与,or或)
            var matchType_sel = $("#matchType_sel").val();
            if (matchType_sel) {
                $("#matchType_" + matchType_sel).prop("checked", true);
            } else {
                $("matchType_or").prop("checked", true);
            }
            //初始化数据后,自动触发check事件
            $("#peopleContainer").trigger("check");


            // 人群属性定向之单选框选中事件
            $("#peopleContainer").delegate(":checkbox", "click", function () {
                $("#peopleContainer").trigger("check");
            });

            // 人群属性高级设置
            $("#crowd_subdes_more").click(function () {
                var ck = $(this).attr("selected");
                if (ck == "selected") {
                    $(this).attr("selected", null);
                    $(this).text("收起");
                    $(".crowd-subdes-more").slideDown();
                } else {
                    $(this).attr("selected", "selected");
                    $(this).text("显示");
                    $(".crowd-subdes-more").slideUp();
                }
            });
        }

        initPopulation();

        /**
         * 初始化兴趣爱好
         */
        function initInstresting() {
            var instresting = campaignDicData.instresting;// 兴趣定向
            var i = 0;
            for (var key in instresting) {
                i++;
                var content = "<li class='root'>"
                    + "<span class='toggle-button' selected='false'>+</span>"
                    + "<div class='checkboxcontainer' style='margin-left:30px;'>"
                    + "<span class='checkboxinput'>"
                    + "<input id='interest_" + i + "' type='checkbox' level='1' name='instrests'/>"
                    + "<label for='interest_" + i + "'></label>"
                    + "</span>"
                    + "<span class='checkboxlabel'>" + key + "</span>"
                    + "</div>"
                    + "<ul class='sub-menu' style='display:none;margin-left:55px;padding-top:0;'>";
                var instrestingOne = instresting[key];
                $.each(instrestingOne, function (n, instrest) {
                    content += "<li>"
                        + "<div class='checkboxcontainer'>"
                        + "<span class='checkboxinput'>"
                        + "<input id='interest_" + i + "_" + instrest.id + "' type='checkbox' name='instrests' level='2' value='" + instrest.id + "' />"
                        + "<label for='interest_" + i + "_" + instrest.id + "'></label>"
                        + "</span>"
                        + "<span class='checkboxlabel'>" + instrest.ckGroupName + "</span>"
                        + "</div>"
                        + "</li>";
                });
                content += "</ul>" + "</li>";
                $("#interestList").append(content);
            }

            //绑定check事件
            $("#interestList").bind("check", function () {
                if ($("#interest input[level=2]:checked").size() > 0) {
                    $("#interest input[level=2]:checked").each(function (i, objLevel) {
                        $(objLevel).parents("li").find("input[level=1]").prop("checked", true);
                    });
                }
                $("#interest input[level=2]").each(function (i, obj) {
                    var hasOtherChecked = $(obj).parents("ul:first").find("input[level=2]:checked").size();
                    if (hasOtherChecked <= 0) {
                        $(obj).parents("ul:first").siblings("div.checkboxcontainer").find("input[level=1]").prop("checked", false);
                    }
                });
                $("#interestResult ul").empty();
                if ($("#interest input[level=1]:checked").size() > 0) {
                    $("#interest input[level=1]:checked").each(function (i, objLevel) {
                        $("#interestResult ul").append("<li>" + $(objLevel).parent().next().text() + "</li>");
                    });
                }
                var isAll = true;
                $("#interestList :checkbox").each(function (i, obj) {
                    if (!$(obj).prop("checked")) {
                        isAll = false;
                    }
                })
                if (isAll) {
                    $("#interest_all").html("取消");
                    $("#interest_all").removeAttr("selected");
                } else {
                    $("#interest_all").html("全选");
                    $("#interest_all").attr("selected", "true");
                }
            });

            //兴趣定向初始化选择的数据
            var instresting_sel = $("#instresting_sel").val(); // 人群属性定向
            if (instresting_sel) {
                instresting_sel = instresting_sel.split(",");
                var checkboxAll = $("#interestList").find("input[type=checkbox]");
                for (var i = 0; i < checkboxAll.length; i++) {
                    for (var j = 0; j < instresting_sel.length; j++) {
                        if ($(checkboxAll[i]).val() == instresting_sel[j]) {
                            $(checkboxAll[i]).prop("checked", true);
                        }
                    }
                }
            }
            $("#interestList").trigger("check");// 根据选择项初始化页面
            // 兴趣定向
            $("#interestList span.toggle-button").click(function () {
                var selected = $(this).prop("selected");
                if (selected) {
                    $(this).prop("selected", false);
                    $(this).html("+");
                } else {
                    $(this).prop("selected", true);
                    $(this).html("-");
                }
                $(this).parent().find("ul").slideToggle();
            });
            // 兴趣定向之选择框点击事件
            $("#interestList").delegate(":checkbox[name=instrests]", "click", function () {
                var level = $(this).attr("level");
                var ck = $(this).prop("checked");
                var id = $(this).attr("id");
                if (level == 1) {
                    $("input[id^='" + id + "_']").prop("checked", ck);
                }
                $("#interestList").trigger("check");
            });

            // 兴趣定向全选
            $("#interest_all").click(function () {
                var selected = $(this).attr("selected");
                if (selected) {
                    $(this).removeAttr("selected");
                    $("#interest :checkbox").each(function (i, obj) {
                        $(obj).prop("checked", true);
                    })
                } else {
                    $(this).attr("selected", "true");
                    $("#interest :checkbox").each(function (i, obj) {
                        $(obj).prop("checked", false);
                    })
                }
                $("#interestList").trigger("check");
            });
        }

        initInstresting();

        /**
         * 初始化个性化人群包
         */
        function initCookiePacket() {

            $("#personality").bind("check", function () {
                $("#personalityResult ul").empty();
                $("#personality input[name=cookiePacket]:checked").each(function () {
                    $("#personalityResult ul").append("<li>" + $(this).parent().next().text() + "</li>");
                });
                var isAll = true;
                $("#personality input[name=cookiePacket]").each(function (i, obj) {
                    if (!$(obj).prop("checked")) {
                        isAll = false;
                    }
                });
                if (isAll) {
                    $("#personality_all").html("取消");
                    $("#personality_all").removeAttr("selected");
                } else {
                    $("#personality_all").html("全选");
                    $("#personality_all").attr("selected", "true");
                }
            });

            //初始化数据
            var cookiePacket_sel = $("#cookiePacket_sel").val(); // 人群属性定向
            if (cookiePacket_sel) {
                cookiePacket_sel = cookiePacket_sel.split(",");
                for (var index in cookiePacket_sel) {
                    var cpId = cookiePacket_sel[index];
                    $("#personality input[name=cookiePacket][value=" + cpId + "]").prop("checked", true);
                }
            }
            $("#personality").trigger("check");

            // 个性化人群定向
            $("input[name='cookiebtn']").click(function () {
                var id = $(this).attr("id");
                console.log($("#btn_private").parent());
                if (id == "btn_private") {
                    $("#btn_private").parent().addClass('active');
                    $("#btn_public").parent().removeClass('active');
                    $("#cookiePrivate").show();
                    $("#cookiePublic").hide();
                } else {
                    $("#btn_public").parent().addClass('active');
                    $("#btn_private").parent().removeClass('active');
                    $("#cookiePrivate").hide();
                    $("#cookiePublic").show();
                }
            });
            $("#personality").delegate("input[name=cookiePacket]", "click", function () {
                $("#personality").trigger("check");
            });

            // 全选
            $("#personality_all").click(function () {
                var selected = $(this).attr("selected");
                if (selected) {
                    $(this).removeAttr("selected");
                    $("#personality input[name=cookiePacket]").each(function (i, obj) {
                        $(obj).prop("checked", true);
                    })
                } else {
                    $(this).attr("selected", "true");
                    $("#personality input[name=cookiePacket]").each(function (i, obj) {
                        $(obj).prop("checked", false);
                    })
                }
                $("#personality").trigger("check");
            });
        }

        initCookiePacket();
        /**
         * 初始化访客找回
         */
        function initRetargetPacket() {

            $("#guest").bind("check", function () {
                $("#guestResult ul").empty();
                $("#guest input[name=retargetpacket]:checked").each(function () {
                    $("#guestResult ul").append("<li>" + $(this).parent().next().text() + "</li>");
                });
                var isAll = true;
                $("#guest :checkbox").each(function (i, obj) {
                    if (!$(obj).prop("checked")) {
                        isAll = false;
                    }
                })
                if (isAll) {
                    $("#retarget_all").html("取消");
                    $("#retarget_all").removeAttr("selected");
                } else {
                    $("#retarget_all").html("全选");
                    $("#retarget_all").attr("selected", "true");
                }
            });

            // 访客找回
            var retargetPacket_sel = $("#retargetPacket_sel").val(); // 人群属性定向
            if (retargetPacket_sel) {
                retargetPacket_sel = retargetPacket_sel.split(",");
                for (var i = 0; i < retargetPacket_sel.length; i++) {
                    $("#retarget_" + retargetPacket_sel[i] + "").prop("checked", true);
                }
                $("#guest").trigger("check");// 根据选中项初始化页面
            }

            $("#guest").delegate(" input[name=retargetpacket]", "click", function () {
                $("#guest").trigger("check");
            });

            // 访客找回全选
            $("#retarget_all").click(function () {
                var selected = $(this).attr("selected");
                if (selected) {
                    $(this).removeAttr("selected");
                    $("#guest input[name=retargetpacket]").each(function (i, obj) {
                        $(obj).prop("checked", true);
                    })
                } else {
                    $(this).attr("selected", "true");
                    $("#guest input[name=retargetpacket]").each(function (i, obj) {
                        $(obj).prop("checked", false);
                    })
                }
                $("#guest").trigger("check");
            });
        }

        initRetargetPacket();

        //投放频次
        function freqWindow() {
            $("input[name=freq_type]").change(function () {
                var value = $(this).attr("value");
                if (value == "0") {
                    $("#custom_freq_type").css('display', 'none');
                } else if (value == "1") {
                    $("#custom_freq_type").css('display', 'block');
                }
            });
        };
        freqWindow();

        /**
         * 地区选择
         */
        function areasClick() {

            /*   //绑定打开地域窗口事件
             $("input[name=area_type]").click(function () {
             var value = $(this).attr("value");
             if (value == "1") {
             $(this).attr('data-toggle', 'modal');
             $(this).attr('data-target', 'selectArea');
             /!*     //展示地域窗口
             var areaLayer = layer.open({
             type: 1,
             title: '',
             shadeClose: true,
             closeBtn: 0, //不显示关闭按钮
             shade: 0.4,
             area: ['800px', 'auto'],
             scrollbar: false,
             content: $('#area_select')
             });

             $('#area_select').parent().css("overflow", "visible");
             //将layer对象存在一个元素中
             $("#area_ok_btn").data("areaLayer", areaLayer);

             var selectedVal = $("#area").val();
             if (selectedVal != "") {
             var array = selectedVal.split(",");
             for (var i = 0; i < array.length; i++) {
             var cityId = array[i];
             var cityCheckbox = $("#area_select :checkbox[level=city][value=" + cityId + "]");
             if (cityCheckbox.prop("checked") == false) {
             cityCheckbox.click();
             }
             }
             }*!/
             }
             });*/

            //延迟1秒加载地域的html，避免造成页面加载缓慢
            setTimeout(areasWindow, 1000);
        }

        areasClick();
        // 显示地域，绑定地域选择事件
        function areasWindow() {

            //	if($("#area_html_created").val() == "1"){
            //		return ;
            //	}

            //遍历大区
            for (var bigAreaId in _campaignDicData.areas) {
                var bigAreaName = _campaignDicData.areas[bigAreaId].name;
                var topDiv = "#area_select .check_dress_con";
                //追加大区div
                $(topDiv).append("<div class=\"clearfix check_dress\" bigArea=\"" + bigAreaId + "\"></div>");
                //大区div选择器
                var bigAreaDiv = "#area_select .check_dress[bigArea=" + bigAreaId + "]";
                //加载大区选择框
                $(bigAreaDiv).append("<span class='area-left'><label><input type=\"checkbox\" value=\"" + bigAreaId + "\" level=\"bigArea\">" + bigAreaName);
                $(bigAreaDiv).append("<div class='area-right'></div>");
            }

            //遍历省份
            for (var provinceId in _campaignDicData.provinces) {
                var provinceName = _campaignDicData.provinces[provinceId].name;
                var bigAreaId = _campaignDicData.provinces[provinceId].parent;
                var bigAreaDivR = "#area_select .check_dress[bigArea=" + bigAreaId + "] .area-right";
                //追加省份div
                $(bigAreaDivR).append("<div class=\"check_dress_rt\" province=\"" + provinceId + "\"></div>");
                //省份div选择器
                var provinceDiv = "#area_select .check_dress_rt[province=" + provinceId + "]";
                var checkBoxHtml = "<label province=\"" + provinceId + "\">";
                checkBoxHtml += "<input type=\"checkbox\" value=\"" + provinceId + "\" level=\"province\" bigArea=\"" + bigAreaId + "\">";
                checkBoxHtml += provinceName;
                //将来显示省份选中城市的数量
                checkBoxHtml += "(<span class=\"city_sel\" province=\"" + provinceId + "\">0</span>";
                //显示省份城市的总数
                checkBoxHtml += "/<span class=\"city_cnt\" province=\"" + provinceId + "\">0</span>)";
                checkBoxHtml += "</label>";
                //加载省份选择框
                $(provinceDiv).append(checkBoxHtml);
                //加载省份城市div
                $(provinceDiv + " label").append("<div class=\"dress_city\"></div>");
            }

            //遍历城市
            for (var cityId in _campaignDicData.citys) {
                var cityName = _campaignDicData.citys[cityId].name;
                var provinceId = _campaignDicData.citys[cityId].parent;
                var provinceDivLabel = "#area_select .check_dress_rt[province=" + provinceId + "] label";
                var bigAreaId = $(provinceDivLabel).find(":checkbox").attr("bigArea");
                var cityDiv = provinceDivLabel + " .dress_city";
                //加载城市选择框
                $(cityDiv).append("<label><input type=\"checkbox\" value=\"" + cityId + "\" level=\"city\" province=\"" + provinceId + "\" bigArea=\"" + bigAreaId + "\">" + cityName + "</label>");

                //累加省份的城市总数
                var provinceCityCnt = parseInt($(provinceDivLabel).find(".city_cnt").html()) + 1;
                $(provinceDivLabel).find(".city_cnt").html(provinceCityCnt);
            }

            //鼠标移到省份弹出城市div
            $("#area_select label[province]").mouseover(function () {

                $(this).addClass("active");
                $(this).find("div.dress_city").show();

            }).mouseout(function () {
                $(this).removeClass("active");
                $(this).find("div.dress_city").hide();
            });

            //定义一个函数，用于统计省内选中的城市
            var countCitySel = function (provinceId) {
                //如果有provinceId，则查询器为指定的省，否则为所有的省
                var selector = provinceId ? "#area_select .city_sel[province=" + provinceId + "]" : "#area_select .city_sel";

                $(selector).each(function (index, obj) {
                    var provinceId = $(this).attr("province");
                    $(this).text($("#area_select :checkbox[province=" + provinceId + "]:checked").length);
                });
            };

            //勾选大区
            $("#area_select :checkbox[level=bigArea]").click(function () {
                var bigAreaId = $(this).val();

                if ($(this).prop("checked") == true) {
                    //让所有的省和市都选中
                    $("#area_select :checkbox[bigArea=" + bigAreaId + "]").prop("checked", true);
                }
                else {
                    //取消所有的省和市
                    $("#area_select :checkbox[bigArea=" + bigAreaId + "]").prop("checked", false);
                }
                //统计所有选中的城市
                countCitySel();
            });

            //勾选省份
            $("#area_select :checkbox[level=province]").click(function () {
                var provinceId = $(this).val();
                var bigAreaId = $(this).attr("bigArea");

                //查询器：所有bigArea为bigAreaId的省
                var allProvinceSelector = "#area_select :checkbox[level=province][bigArea=" + bigAreaId + "]";

                if ($(this).prop("checked") == true) {
                    //选中所有城市
                    $("#area_select :checkbox[province=" + provinceId + "]").prop("checked", true);
                    //如果其大区所有省份都选中了，让大区选中
                    if ($(allProvinceSelector + ":checked").length == $(allProvinceSelector).length) {
                        $("#area_select :checkbox[level=bigArea][value=" + bigAreaId + "]").prop("checked", true);
                    }
                }
                else {
                    //省内所有城市取消
                    $("#area_select :checkbox[province=" + provinceId + "]").prop("checked", false);
                    //取消大区
                    $("#area_select :checkbox[level=bigArea][value=" + bigAreaId + "]").prop("checked", false);
                }
                //统计省内选中的城市
                countCitySel(provinceId);
            });

            //勾选城市
            $("#area_select :checkbox[level=city]").click(function () {
                var provinceId = $(this).attr("province");
                var bigAreaId = $(this).attr("bigArea");

                //查询器：所有province为provinceId的市
                var allProvinceCitySelector = "#area_select :checkbox[level=city][province=" + provinceId + "]";
                //查询器：所有bigArea为bigAreaId的市
                var allBigAreaCitySelector = "#area_select :checkbox[level=city][bigArea=" + bigAreaId + "]";

                if ($(this).prop("checked") == true) {
                    //如果其大区所有城市都选中了，让大区选中
                    if ($(allBigAreaCitySelector + ":checked").length == $(allBigAreaCitySelector).length) {
                        $("#area_select :checkbox[level=bigArea][value=" + bigAreaId + "]").prop("checked", true);
                    }
                    //如果其省份所有城市都选中了，让省份选中
                    if ($(allProvinceCitySelector + ":checked").length == $(allProvinceCitySelector).length) {
                        $("#area_select :checkbox[level=province][value=" + provinceId + "]").prop("checked", true);
                    }
                }
                else {
                    //取消大区
                    $("#area_select :checkbox[level=bigArea][value=" + bigAreaId + "]").prop("checked", false);
                    //取消省份
                    $("#area_select :checkbox[level=province][value=" + provinceId + "]").prop("checked", false);
                }
                //统计省内选中的城市
                countCitySel(provinceId);
            });

            //全选
            $("#select_all").click(function () {
                $("#area_select :checkbox").prop("checked", true);
                countCitySel();
            });
            //清空
            $("#clean_all").click(function () {
                $("#area_select :checkbox").prop("checked", false);
                countCitySel();
            });

            // 地域保存按钮事件
            $("#area_ok_btn").click(function () {
                //关闭layer
                var areaLayer = $("#area_ok_btn").data("areaLayer");
                layer.close(areaLayer);
                //读取所有选中的城市（只要城市级别的）
                var area_arr = [];
                $("#area_select :checkbox[level=city]").each(function () {
                    if ($(this).prop("checked") == true) {
                        area_arr.push($(this).val());
                    }
                });
                //console.log("area_arr");
                //console.log(area_arr);
                //将数据放在隐藏域中
                $("#area").val(area_arr);
            });

            // 地域取消按钮事件
            $("#area_cancel_btn").click(function () {
                //关闭layer
                var areaLayer = $("#area_ok_btn").data("areaLayer");
                layer.close(areaLayer);
            });

            //在隐藏域中标识地域html已经加载完成
            //$("#area_html_created").val(1);

        }

        /*
         *  媒体定向
         * */
        //按照媒体类型投放
        function mediaTypeWindow() {

            // 初始化媒体类型数据


            // 打开窗口
            /*   $("#media_type_dirrect").click(function () {
             var mediaTypeLayer = layer.open({
             type: 1,
             title: '',
             closeBtn: 0, //不显示关闭按钮
             shadeClose: true,
             shade: 0.4,
             area: ['510px', 'auto'],
             content: $('#media_type_window')
             });
             $("#media_type_window").data("mediaTypeLayer", mediaTypeLayer);
             });*/


            // 媒体类型保存按钮
            $("#media_type_conform").click(function () {
                var mediaTypeLayer = $("#media_type_window").data("mediaTypeLayer");
                layer.close(mediaTypeLayer);
                var media_type_arr = [];

                $("input:checkbox[name='webwite_check']").each(function () {
                    if ($(this)[0].checked == true) {
                        media_type_arr.push($(this).val());
                    }
                });

                $("#media_type_data").val(media_type_arr);
                $("#media_type_flag").val("1");
                return false;
            });

            // 媒体类型取消按钮
            $("#media_type_canncel").click(function () {
                var mediaTypeLayer = $("#media_type_window").data("mediaTypeLayer");
                // $("#media_type_flag").val("0");
                layer.close(mediaTypeLayer);
                return false;
            });

            //全选
            $("#medis_type_all").click(function () {
                $("#media_type_one :checkbox").each(function (i, obj) {
                    $(obj).prop("checked", true);
                });
            });
            //取消
            $("#medis_type_empty").click(function () {
                $("#media_type_one :checkbox").each(function (i, obj) {
                    $(obj).prop("checked", false);
                });
            });
        }

        mediaTypeWindow();

        //媒体类型数据初始化
        function InitmediaTypeData(mediaTypeList) {
            //遍历媒体类型
            $("#media_type_one").empty();
            var content = "";

            // each begin
            $.each(_campaignDicData.websiteTypes, function (index, obj) {
                // 拼装树
                content += "<li>";
                content += "	<span id=\"media_" + obj.id + "\" class=\"toggle-button i_open\" " +
                    "onclick=\"spread_action(" + obj.id + ")\"></span>";
                content += "	<div class=\"type_list\">";
                content += "	<span class=\"checkbox_input\"><input type=\"checkbox\" name=\"media_father_" + obj.id + "\" " +
                    "onchange=\"media_checked_action(" + obj.id + ")\" value=\"" + obj.id + "\"><label></label></span>";
                content += "	<span class=\"checkbox_label\">" + obj.name + "</span>";
                //是否包含下一级媒体类型
                if (obj.hasOwnProperty("subWebsiteTypes")) {
                    var sub_content = "";
                    sub_content += "<ul  id=\"media_type_two_" + obj.id + "\">";

                    $.each(obj.subWebsiteTypes, function (index, sub_obj) {

                        var flag = false;
                        //mediaDomain.domain = mediaDomain.domain.replace("\"","").replace("\"","");
                        if (mediaTypeList != "0") {
                            $.each(mediaTypeList, function (index, data) {
                                if (data == sub_obj.id) {
                                    flag = true;
                                }
                            });
                        }
                        ;

                        sub_content += "<li>";
                        sub_content += "	<span class=\"checkbox_input\">";
                        if (flag) {
                            sub_content += "	<input type=\"checkbox\" checked=\"checked\" id=\"check1\" onchange=\"webwite_checked_action(" + obj.id + ")\"   name=\"webwite_check\"  value=\"" + sub_obj.id + "\">"
                        } else {
                            sub_content += "	<input type=\"checkbox\" id=\"check1\" onchange=\"webwite_checked_action(" + obj.id + ")\"   name=\"webwite_check\"  value=\"" + sub_obj.id + "\">"
                        }
                        sub_content += "		<label for=\"check1\"></label></span>";
                        sub_content += "	<span class=\"checkbox_label\">" + sub_obj.name + "</span>";
                        sub_content += "</li>";
                    });

                    sub_content += "</ul>";
                    // 加载子集数据
                    content += sub_content;
                }
                ;
                content += "	</div>";
                content += "</li>";
            });  // end each
            // console.log(content)
            $("#media_type_one").append(content);

            //判断子集是否有数据,如果有则父级为选中状态
            $.each(_campaignDicData.websiteTypes, function (index, obj) {
                //是否包含下一级媒体类型
                var father_flag = false;
                if (obj.hasOwnProperty("subWebsiteTypes")) {
                    $.each(obj.subWebsiteTypes, function (index, sub_obj) {
                        if (mediaTypeList != "0") {
                            $.each(mediaTypeList, function (index, data) {
                                if (data == sub_obj.id) {
                                    father_flag = true;
                                    return false;
                                }
                            });
                        }
                        ;
                        if (father_flag) {
                            return false;
                        }
                    });
                }
                ;
                if (father_flag) {
                    var father_select_id = "input:checkbox[name='media_father_" + obj.id + "']";
                    $(father_select_id).prop("checked", true);
                }
            });

        }

        InitmediaTypeData(1);

        // 上级复选框选中未选状态的change事件
        function media_checked_action(id) {
            //spread_action(id);
            var father_select_id = "input:checkbox[name='media_father_" + id + "']";
            var father_status = $(father_select_id).prop("checked");
            var select_id = "#media_type_two_" + id + " input:checkbox[name='webwite_check']";
            $(select_id).prop("checked", father_status);
        }

        // 下级复选框点击关联事件
        function webwite_checked_action(id) {
            var father_select_id = "input:checkbox[name='media_father_" + id + "']";
            var select_id = "#media_type_two_" + id + " input:checkbox[name='webwite_check']";

            var chkt = 0;
            $(select_id).each(function () {
                if ($(this)[0].checked == true) {
                    chkt++;
                }
            });
            if (chkt > 0) {
                $(father_select_id).prop("checked", true);
            } else {
                $(father_select_id).prop("checked", false);
            }

        }

        // 展开/收缩功能
        function spread_action(id) {
            if ($("#media_" + id).hasClass("i_open")) {
                $("#media_" + id).removeClass("i_open");
                $("#media_type_two_" + id).css('display', 'block');

            } else {
                $("#media_" + id).addClass("i_open");
                $("#media_type_two_" + id).css('display', 'none');
            }
        }
    });
})



