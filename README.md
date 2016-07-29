# stockDataCatch

@author kimi

A Light Crawler Project that get stock infomation from internet. 

这是一个简单的爬虫程序，基于spring4.2+mybatis3.2搭建

程序结构分为：入口控制器Controller、任务存储队列TaskQueueService，任务生成器TaskGenerateService,爬虫管理器CatcherManageService,任务执行Catcher

项目中共有四条爬虫负责抓取上市公司的基本信息和财务信息。

数据表机构保存在db_structure文件下。
