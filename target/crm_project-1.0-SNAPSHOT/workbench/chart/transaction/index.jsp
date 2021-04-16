<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
        String base = request.getScheme()
                + "://"
                + request.getServerName()
                + ":"
                + request.getServerPort()
                + request.getContextPath()
                + "/";
%>
<html>
<head>
    <base href="<%=base%>">
    <title>Title</title>

    <script src="Echarts/echarts.min.js"></script>
    <script src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript">
        $(function () {
            // 基于准备好的dom，初始化echarts实例
            var myChart = echarts.init(document.getElementById('main'));

            $.get("tranServlet",{
                "action":"getCharts"
            },function (data) {
                //data{"total":xxx, "valueList":[{}{}]}
                // 指定图表的配置项和数据

                console.log(data)
                console.log(data.total)
                console.log(data.valueList)
                option = {
                    title: {
                        text: '漏斗图',
                        subtext: '纯属虚构'
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c}%"
                    },
                    toolbox: {
                        feature: {
                            dataView: {readOnly: false},
                            restore: {},
                            saveAsImage: {}
                        }
                    },
                    legend: {
                        data: ['展现','点击','访问','咨询','订单']
                    },

                    series: [
                        {
                            name:'漏斗图',
                            type:'funnel',
                            left: '10%',
                            top: 60,
                            //x2: 80,
                            bottom: 60,
                            width: '80%',
                            // height: {totalHeight} - y - y2,
                            min: 0,
                            max: data.total,
                            minSize: '0%',
                            maxSize: '100%',
                            sort: 'descending',
                            gap: 2,
                            label: {
                                show: true,
                                position: 'inside'
                            },
                            labelLine: {
                                length: 10,
                                lineStyle: {
                                    width: 1,
                                    type: 'solid'
                                }
                            },
                            itemStyle: {
                                borderColor: '#fff',
                                borderWidth: 1
                            },
                            emphasis: {
                                label: {
                                    fontSize: 20
                                }
                            },
                            data:data.valueList
                            // [
                            //     {value: 60, name: '访问'},
                            //     {value: 40, name: '咨询'},
                            //     {value: 20, name: '订单'},
                            //     {value: 80, name: '点击'},
                            //     {value: 100, name: '展现'}
                            // ]
                        }
                    ]
                };


                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);

            },"json")



        })
    </script>
</head>
<body>
<!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
<div id="main" style="width: 600px;height:400px;"></div>

</body>
</html>
