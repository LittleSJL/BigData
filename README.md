# BigData
2017级-软件工程-1班-孙津梁

品高企业实训5.18第一次课程

2020.5.22-学习和作业

今天主要就是关于课程作业的推进：同步本地目录的文件和S3桶内的文件（以本地为基准）

我主要分为以下步骤：（1）首先实现监控本地目录的变化（2）其次，分别在对应的监控事件中执行对应的逻辑即可：比如如果为文件的创建事件，则执行上传的逻辑；如果为文件的删除事件，则执行删除的逻辑（3）进行一些功能的完善和修补，使之更符合作业的需求：比如在每次登陆后，监听事件前，必须先同步一次本地和S3桶等。

目前，我实现了本地目录的监听，和执行监听事件中的同步前两个步骤，明天和后天再进行一些功能的完善，作为后续作业的提交。

2020.5.21-学习和作业

今天又在B站看了一遍品高大数据实训导论，我觉得还是讲得非常好的。 尤其是开头对于品高的宣传片，让我非常震撼，一是视频制作非常精良，二是确实被品高在云计算和大数据这个领域的沉淀和积累所感染（03年创立，17年的技术沉淀） 其次让我对这个行业和领域有了一个初步的认识：2006谷歌年云计算的概念；2011年国外的开源平台逐渐成熟；2013年 IBM提出它的公有云策略；2014年微软也推出云产品；2016年甲骨文也提出了它的全云战略；放眼国际，品高算是第一批拥有自主研发云平台项目经验的公司，更为自豪的是品高是一家纯国产技术研发的软件公司。

同时，我也将第一次课程中教学的基于编程实现的内容大致实践了一遍，包括： 上传文件、文件误删恢复、大文件的分片上传和下载、事件驱动的触发器、对象访问权限实时控制、属性更改、文件夹访问权限控制、文件的定期删除等。

同时对于老师上课新增的内容（实训操作手册没有的内容），我也跟着实践了一遍，包括： 查询S3桶内的所有文件、查询S3桶中文件的历史版本和ID、事件驱动中修改逻辑为图片的缩小等。

其中有遇到过两次报错的情况，分别是在（1）文件误删恢复（2）图片的缩小； 解决方法是： （1）要正确使用文件历史版本的versionId，找到你想要恢复的文件，而不是删除版本的文件； （2）使用pillow第三方库可以做到生成图片的缩略图，直接image.thumbnail即可，无需新建变量存储。
