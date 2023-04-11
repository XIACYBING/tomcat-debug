# DEBUG过程中的一些QA

## Q1：为什么在source-web-module模块中会引入tomcat模块？
## A1：为了DEBUG方便，如果引入servlet-api模块，在进行源码跳转的时候，经常会跳转到servlet-api的代码中，我们期望的是跳转到tomcat模块中的对应源码。

## Q2：引入slf4j和logback时，报错ClassNotFoundException，这是怎么回事？
## A2：暂时不清楚具体原因，解决方式是，在对应的artifact（比如：source-web-module:war exploded printWebApps）配置中，对应的lib文件夹下操作：Add copy of -> Library files，添加所有当前模块的Library file，即可解决问题。