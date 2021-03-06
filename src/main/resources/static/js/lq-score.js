/**
 * 评分插件
 * @author LQresier
 */
(function ($) {
    var LQScore = function (ele, options) {
        this.$element = ele;
        this.mouseoverColor; //鼠标经过的颜色,自动生成相对于选中颜色的浅色
        this.isScoreFinish = false; //评分是否完成
        this.defaults = {
            $tipEle: null,//提示元素
            fontSize: "20px", //大小
            isReScore: false, //是否允许重新评分
            tips: null, //提示
            zeroTip: "", //无分数提示
            score: null, //分数
            callBack: null, //评分后处理
            content: "★", //内容
            defultColor: "rgb(220,220,220)", //默认颜色(未选中的颜色)
            selectColor: ["rgb(183,199,219)", "#8eb9f5", "#ffac38", "#ff8547", "#f54545"] //选中后的颜色
        };
        console.log('分数'+options.score);
        this.options = $.extend({}, this.defaults, options);
    }
    LQScore.prototype = {
        // 初始化评分插件
        init: function () {
            var obj = this;
            var $tipEle = obj.options.$tipEle;
            obj.createMouseoverColor(obj.options.selectColor);
            obj.$element.addClass("lq-score");
            obj.$element.append($("<ul></ul>"));
            var $ulEle = obj.$element.children("ul").eq(0);
            for (var i = 0; i < 5; i++) {
                $ulEle.append($("<li style='font-size:" + obj.options.fontSize + ";'>" + obj.options.content + "</li>"));
            }
            var $liEle = $ulEle.children("li");
            $liEle.css("color", obj.options.defultColor);
            if ($tipEle) {
                $tipEle.html(obj.options.zeroTip).css("color", obj.options.defultColor);
            }
            if (obj.options.score != null) {
                if (obj.options.score < 0 || obj.options.score > 5) {
                    throw new Error("score 超出范围");
                } else {
                    obj.isScoreFinish=true;
                    this.setScore();
                }
            }
            $liEle.on("mouseover", function () {
                if (!obj.options.isReScore && obj.isScoreFinish) {
                    return;
                }
                var len = $(this).prevAll("li").add(this).length;
                $(this).prevAll("li").add(this).css("color", (obj.mouseoverColor instanceof Array) ? obj.mouseoverColor[len - 1] : obj.mouseoverColor);
                $(this).nextAll("li").css("color", obj.options.defultColor);
                if ($tipEle) {
                    if (obj.options.tips === "default") {
                        $tipEle.html(len + "分").css("color", (obj.mouseoverColor instanceof Array) ? obj.mouseoverColor[len - 1] : obj.mouseoverColor);
                    } else {
                        var tip = typeof obj.options.tips == 'string' ? obj.options.tips : obj.options.tips[len - 1];
                        tip = tip.replace(/{{[\s]*lq-score[\s]*}}/g, len);
                        $tipEle.html(tip).css("color", (obj.mouseoverColor instanceof Array) ? obj.mouseoverColor[len - 1] : obj.mouseoverColor);
                    }
                }
            });
            $liEle.on("mouseout", function () {
                if (!obj.options.isReScore && obj.isScoreFinish) {
                    return;
                }
                $(this).prevAll("li").add(this).css("color", obj.options.defultColor);
                if ($tipEle) {
                    $tipEle.html(obj.options.zeroTip).css("color", obj.options.defultColor);
                }
                if (obj.options.score != null) {
                    obj.setScore();
                }
            });
            $liEle.on("click", function () {
                if (!obj.options.isReScore && obj.isScoreFinish) {
                    return;
                }
                var len = $(this).prevAll("li").add(this).length;
                $(this).prevAll("li").add(this).css("color", (obj.options.selectColor instanceof Array) ? obj.options.selectColor[len - 1] : obj.options.selectColor);
                if ($tipEle) {
                    if (obj.options.tips === "default") {
                        $tipEle.html(len + "分").css("color", (obj.mouseoverColor instanceof Array) ? obj.mouseoverColor[len - 1] : obj.mouseoverColor);
                    } else {
                        var tip = typeof obj.options.tips == 'string' ? obj.options.tips : obj.options.tips[len - 1];
                        tip = tip.replace(/{{[\s]*lq-score[\s]*}}/g, len);
                        $tipEle.html(tip).css("color", (obj.options.selectColor instanceof Array) ? obj.options.selectColor[len - 1] : obj.options.selectColor);
                    }
                }
                obj.options.score = len;
                $(this).nextAll("li").css("color", obj.options.defultColor);
                obj.isScoreFinish = true;
                if (!obj.options.isReScore) {
                    $liEle.css("cursor", "default");
                }
                if (obj.options.callBack) {
                    obj.options.callBack(obj.options.score, obj.$element);
                }
            });
            return this.$element;
        },
        createMouseoverColor: function (selectColor) {
            if (typeof selectColor == 'string' && selectColor.constructor == String) {
                this.mouseoverColor = this.changeColor(selectColor);
            } else if (typeof selectColor == 'object' && selectColor.constructor == Array) {
                this.mouseoverColor = [];
                for (var i=0;i<selectColor.length;i++) {
                    this.mouseoverColor.push(this.changeColor(selectColor[i]));
                }
            }
        },
        changeColor: function (colorStr) {
            if (/^#([0-9a-fA-F]{6}|[0-9a-fA-F]{3})$/g.test(colorStr)) {
                //十六进制
                var lowColor = colorStr.toLowerCase();
                if (lowColor.length === 4) {
                    var tempColr = "#";
                    for (var i = 1; i < 4; i++) {
                        tempColor += lowColor.slice(i, i + 1).concat(lowColor.slice(i, i + 1));
                    }
                    lowColor = tempColor;
                }
                var colorChange = [];
                var rgb = ['r', 'g', 'b'];
                for (var i = 1, k = 0; i < 7; i += 2, k++) {
                    var c = parseInt("0x" + lowColor.slice(i, i + 2));
                    c = this.shallowColor(c, rgb[k]);
                    colorChange.push(c);
                }
                return "rgba(" + colorChange.join(",") + ",1)";
            } else if (/^[rR][gG][Bb][Aa][\(]([\s]*(2[0-4][0-9]|25[0-5]|[01]?[0-9][0-9]?),){2}[\s]*(2[0-4][0-9]|25[0-5]|[01]?[0-9][0-9]?),?[\s]*(0\.\d{1,2}|1|0)?[\)]{1}$/g.test(colorStr)) {
                //rgba
                var colors = colorStr.match(/[0-9]+/g);
                var rgb = ['r', 'g', 'b'];
                for (var i = 0; i < 3; i++) {
                    colors[i] = this.shallowColor(parseInt(colors[i]), rgb[i]);
                }
                return "rgba(" + colors.join(",") + ")";
            } else if (/^[rR][gG][Bb][\(]([\s]*(2[0-4][0-9]|25[0-5]|[01]?[0-9][0-9]?),){2}[\s]*(2[0-4][0-9]|25[0-5]|[01]?[0-9][0-9]?)[\)]{1}$/g.test(colorStr)) {
                //rgb
                var colors = colorStr.match(/[0-9]+/g);
                var rgb = ['r', 'g', 'b'];
                for (var i = 0; i < 3; i++) {
                    colors[i] = this.shallowColor(parseInt(colors[i]), rgb[i]);
                }
                return "rgba(" + colors.join(",") + ",1)";
            }
        },
        //单色变浅
        shallowColor: function (colorNum, flag) {
            switch (flag) {
                case "r":
                    return colorNum + 15;
                case "g":
                    return colorNum + 20;
                case "b":
                    return colorNum + 25;
                default:
                    return colorNum;
            }
        },
        setScore: function () {
            var $liEle = this.$element.children("ul").children("li");
            if (!this.options.isReScore) {
                $liEle.css("cursor", "default");
            }
            var showLen;
            if (this.options.score < 1 && this.options.score > 0) {
                showLen = 1;
            } else {
                showLen = Math.round(this.options.score);
            }
            if (this.options.score == 0) {
                if (this.options.$tipEle != null) {
                    this.options.$tipEle.html(this.options.zeroTip).css("color", this.options.defultColor);
                }
                return;
            }
            $liEle.slice(0, showLen).css("color", this.options.selectColor[showLen - 1]);
            if (this.options.$tipEle != null) {
                if (this.options.tips === "default") {
                    this.options.$tipEle.html(this.options.score + "分").css("color", this.options.selectColor[showLen - 1]);
                } else {
                    var tip = typeof this.options.tips == 'string' ? this.options.tips : this.options.tips[showLen - 1];
                    tip = tip.replace(/{{[\s]*lq-score[\s]*}}/g, this.options.score);
                    this.options.$tipEle.html(tip).css("color", this.options.selectColor[showLen - 1]);
                }
            }
            if (!this.options.isReScore && this.options.callBack != null) {
                this.options.callBack(this.options.score, this.$element);
            }
        }
    }
    $.fn.lqScore = function (options) {
        var lqScore = new LQScore(this, options);
        return lqScore.init();
    }
})(jQuery);