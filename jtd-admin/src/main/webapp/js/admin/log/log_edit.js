$(function() {
    init();
});

function init() {

    var weekHour_flag = $("#weekHour_flag").val();
    if(weekHour_flag == "0"){
        $("#before_week").css("display","block");
        $("#after_week").css("display","block");
    }else if(weekHour_flag == "1"){
        $("#before_week").css("display","block");
    }else if(weekHour_flag == "2"){
        $("#after_week").css("display","block");
    }

    var before = $("#before").val();
    if(before != null && before!=""){
        before = JSON.parse(before);
        var weekhour = before.weekHour;
        if(weekhour != null && weekhour!=""){
            weekhour = JSON.parse(weekhour);
            $("#before_hour  tr:gt(0)").each(function(week, tr) {
                $(tr).find(".psel i").each(function(hour, li) {
                    if (weekhour[week][hour] == 1) {
                        $(li).addClass("active");
                    } else {
                        $(li).removeClass("active");
                    }
                });
            });
        }
    }

    var after = $("#after").val();
    if(after != null && after!=""){
        after = JSON.parse(after);
        var weekhour = after.weekHour;
        if(weekhour != null && weekhour!=""){
            weekhour = JSON.parse(weekhour);
            $("#after_hour  tr:gt(0)").each(function(week, tr) {
                $(tr).find(".psel i").each(function(hour, li) {
                    if (weekhour[week][hour] == 1) {
                        $(li).addClass("active");
                    } else {
                        $(li).removeClass("active");
                    }
                });
            });
        }
    }
}

