# stockDataCatch

@author kimi

A Light Crawler Project that get stock infomation from internet. 

这是一个简单的爬虫程序，基于spring4.2+mybatis3.2搭建

程序结构分为：任务提交Controller、任务存储队列QueueService、任务执行Catcher

项目中共有三条爬虫负责抓取上市公司的基本信息和财务信息。

项目中有一个计算器使用线程池技术计算财务数据的同比变化率
