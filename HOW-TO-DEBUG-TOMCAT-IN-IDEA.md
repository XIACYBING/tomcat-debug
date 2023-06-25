# 如何在IDEA上调试Tomcat

## 如何在IDEA上运行Tomcat项目
### 第一步：clone Tomcat项目
从`github`上`clone` `tomcat`项目，项目文件夹名默认为`tomcat`

### 第二步：修改项目结构
* 新建`tomcat-debug`文件夹，将`clone`下来的`tomcat`文件夹放入其中
* 进入`tomcat-debug`文件夹，新建`catalina-home`文件夹，将`tomcat`文件夹下的`conf`和`webapps`目录复制到`catalina-home`文件夹下
* 新建一个`pom.xml`文件，放在`tomcat-debug`文件夹下，`pom.xml`文件内容如下：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">

  <modelVersion>4.0.0</modelVersion>
  <groupId>com.xiacybing</groupId>
  <artifactId>tomcat-debug</artifactId>
  <name>tomcat-debug</name>
  <version>1.0</version>
  <packaging>pom</packaging>

  <modules>
    <module>tomcat</module>
  </modules>
</project>
```
* 进入`tomcat`文件夹，新建一个`pom.xml`文件，`pom.xml`文件内容如下：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"    
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"    
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <parent>
        <groupId>com.xiacybing</groupId>
        <artifactId>tomcat-debug</artifactId>
        <version>1.0</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>tomcat</artifactId>
    <version>8.0</version>

    <name>tomcat</name>
    <url>http://localhost:8080</url>
    
    <dependencies>
        <dependency>
            <groupId>org.easymock</groupId>
            <artifactId>easymock</artifactId>
            <version>3.5</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>ant</groupId>
            <artifactId>ant</artifactId>
            <version>1.7.0</version>
        </dependency>
        <dependency>
            <groupId>wsdl4j</groupId>
            <artifactId>wsdl4j</artifactId>
            <version>1.6.2</version>
        </dependency>
        <dependency>
            <groupId>javax.xml</groupId>
            <artifactId>jaxrpc</artifactId>
            <version>1.1</version>
        </dependency>
        <dependency>
            <groupId>org.eclipse.jdt.core.compiler</groupId>
            <artifactId>ecj</artifactId>
            <version>4.6.1</version>
        </dependency>
    </dependencies>

    <build>
        <finalName>Tomcat8.0</finalName>
        <sourceDirectory>java</sourceDirectory>
        <testSourceDirectory>test</testSourceDirectory>
        <resources>
            <resource>
                <directory>java</directory>
            </resource>
        </resources>
        <testResources>
            <testResource>
                <directory>test</directory>
            </testResource>
        </testResources>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>2.0.2</version>

                <configuration>
                    <encoding>UTF-8</encoding>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
    
</project>
```

### 第三步：初始化Git配置
* 在`tomcat-debug`文件夹下，打开`git`命令行终端，确认当前所处路径是`tomcat-debug`文件夹，执行命令`git init`，初始化`tomcat-debug`文件夹作为一个仓库
* 初始化完成后，执行`git submodule add https://github.com/XIACYBING/tomcat.git tomcat`，将`tomcat`文件夹作为一个子仓库加入到当前仓库中，命令执行完成后，无报错，且`tomcat-debug`文件夹下有新增`.gitmodule`文件即可
* 给初始化的`tomcat-debug`仓库新增`.gitignore`文件，将以下内容加入其中：
```gitignore
# 配置文件排除
.idea/

# 日志排除
/catalina-home/logs

# 排除webapps文件夹下除docs、examples、host-manager、manager、META-INF和ROOT外的，其他项目文件夹
/catalina-home/webapps/*
!/catalina-home/webapps/docs
!/catalina-home/webapps/examples
!/catalina-home/webapps/host-manager
!/catalina-home/webapps/manager
!/catalina-home/webapps/META-INF
!/catalina-home/webapps/ROOT

# 任意模块下的target目录
*/target
```
* 配置`git`在当前仓库的提交用户名和提交email：`git config user.name wang.yubin && git config user.email 359474377@qq.com`
* 执行`git add . && git commit -am "初始化tomcat-debug仓库"`，提交当前仓库的所有内容
* 执行`cd tomcat && git checkout -b 8.5.20 8.5.20 && git tag 8.5.20-tag 8.5.20 && cd ..`，从`8.5.20`的`tag`上切出一条分支来，也命名为`8.5.20`，并将原来的`8.5.20`的`tag`名称修改为`8.5.20-tag`，避免后面无法提交`8.5.20`分支
* 修改`tomcat`文件夹下的`.gitignore`文件，添加以下内容：
```gitignore
# 排除编译生成的target
/target
```

### 第四步：修改Tomcat文件内容，使其运行
* 用`IDEA`将`tomcat-debug`文件夹作为项目打开，配置好`Maven`即可
* 找到`org.apache.catalina.startup.Bootstrap.main`函数，选择`Modify Run Configuration...`，编辑运行配置
* 添加`VM options`，内容如下：
```
-Dcatalina.home=catalina-home
-Dcatalina.base=catalina-home
-Djava.endorsed.dirs=catalina-home/endorsed
-Djava.io.tmpdir=catalina-home/temp
-Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager
-Djava.util.logging.config.file=catalina-home/conf/logging.properties
```
* 如果因为国际化设置而出现中文乱码问题，可以考虑修改为英文，或设置控制台输出为`UTF-8`
```
-Duser.language=en
```
* 打开`Project Structure -> Project`，确保`SDK`为`1.8`版本，此处只实验过`16`版本，会报错
```
-Djava.endorsed.dirs=catalina-home/endorsed is not supported. Endorsed standards and standalone APIs
in modular form will be supported via the concept of upgradeable modules.
Error: Could not create the Java Virtual Machine.
Error: A fatal exception has occurred. Program will exit.
```
* 找到`org.apache.catalina.startup.ContextConfig.configureStart`函数，在函数内，`webConfig();`执行完成后，添加一行`context.addServletContainerInitializer(new JasperInitializer(), null);`
* 找到`tomcat\test\util`，即`tomcat`文件夹下的`test`文件夹下的`util`目录，右键`util`目录，选择`Mark Directory as -> Excluded`，该目录下的`TestCookieFilter`类中缺少一个`CookieFilter`的引用，会导致编译报错，所以需要排除该目录，或删除该类文件
* 启动`org.apache.catalina.startup.Bootstrap.main`函数，等待`[main] org.apache.catalina.startup.Catalina.start Server startup in 683 ms`输出后，访问`localhost:8080`，有出现`tomcat`页面即可，至此，本地调试`Tomcat`的相关配置已完成

## 如何在Tomcat中动态的调试Web项目
### 第一步：新建Web项目的模块
* 用`IDEA`将`tomcat-debug`文件夹作为项目打开
* 右键`tomcat-debug`项目，选择新建模块`New -> Modules...`
* 在弹窗中选择`Maven`，勾选`Create from archetype`，选择`org.apache.maven.archetypes:maven-archetype-webapp`，点击`Next`
* 在`Name:`栏中键入模块名称`source-web-module`，点击`Next`
* 确认`Maven`配置没问题后，直接点击`Finish`
* 等待`Maven`执行完成，即可创建子模块`source-web-module`，创建成功主要体现在两方面：
  * `tomcat-debug`下的`pom`文件中，出现`source-web-module`的模块关联：
    ```xml
    <modules>
        <module>tomcat</module>
        <module>source-web-module</module>
    </modules>
    ```
  * `tomcat-debug`下出现`source-web-module`文件夹，该文件夹有以下目录：
    ```
    │  pom.xml
    └─src
        ├─main
        │  ├─java
        │  ├─resources
        │  └─webapp
        │      │  index.jsp
        │      └─WEB-INF
        │              web.xml
        └─test
            ├─java
            └─resources
    ```

### 完善项目运行配置
* 在`Project` `Structure`界面，选择`Project Settings -> Artifacts`，点击`+`号，在出现的弹窗中选择`Web Application: Exploded -> From Modules...`，在出现的弹窗`Select Modules`中选择`source-web-module`模块，会出现`source-web-module:war exploded2`的`artifact`，修改`Output directory`为`D:\codeCollection\tomcat-debug\catalina-home\webapps\source-web-module`，并修改`Name`为`source-web-module:war exploded printWebApps`
* 编辑`org.apache.catalina.startup.Bootstrap.main`函数的运行配置，选择`Modify options`，进入`Add Run Options`，在`Before Launch`下勾选`Add before launch task`
* 在配置编辑页出现的`Before launch:`栏，点击`+`号，添加以下两个任务：
  * 在出现的`Add New Task`弹窗中选择`Build Artifacts`，勾选新建好的`source-web-module:war exploded printWebApps`，该任务的目的是将在执行`org.apache.catalina.startup.Bootstrap.main`函数之前，编译`source-web-module`模块，并将生成的内容输出到`catalina-home`下的`webapps`目录下
  * 在出现的`Add New Task`弹窗中选择`Run Maven Goal`，在出现的`Select Maven Goal`弹窗中的`Command line`栏中，输入`compile -pl tomcat`，该任务的目的是重新编译修改了的`tomcat`代码（即`Tomcat`的源码），该步骤应该可以通过配置`tomcat`的`pom.xml`文件来替代，具体怎么处理还需要研究
    * 在选择`Add before launch task`时，本身会有一个`Build`任务，该任务的目的是构建当前项目，和`compile -pl tomcat`效果一致，所以按需使用即可 
* 运行`org.apache.catalina.startup.Bootstrap.main`函数，访问`http://localhost:8080/source-web-module/index.jsp` ，有出现`Hello World!`即说明`Tomcat`访问成功，项目部署成功，此时就可以通过`IDEA`的`DEBUG`模式去调试`Tomcat`的代码了

# 参考
* [【基于IntelliJ IDEA环境】Tomcat8源码的调试和项目部署](https://gongxufan.github.io/2017/10/20/tomcat-source-debug/)