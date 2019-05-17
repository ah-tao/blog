## 打造功能完整的博客系统: Spring MVC实战入门

### 1. 创建Spring Boot项目

#### 第一个Spring Boot应用

以[Maven](https://maven.apache.org/)项目为例，项目结构如下：

```
.
├── pom.xml
├── src
│   ├── main
│   │   └── java
│   │       └── com
│   │           └── taotao
│   │               └── BlogApplication.java
```

首先引入Spring Boot的开发依赖，在`pom.xml`中写入如下内容：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.taotao</groupId>
  <artifactId>blog</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <packaging>jar</packaging>

  <name>blog</name>
  <description>Blog</description>

  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>1.4.0.RELEASE</version>
  </parent>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <java.version>1.8</java.version>
  </properties>

  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <configuration>
          <fork>true</fork>
        </configuration>
      </plugin>
    </plugins>
  </build>

</project>
```

编写一个类包含处理HTTP请求的方法以及一个`main()`函数:

```java
package com.taotao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@Controller
public class BlogApplication {

    @RequestMapping("/")
    @ResponseBody
    String index() {
        return "Hello World";
    }

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }
}

```

有两种方法可以启动应用：

1. 在IDE（或者命令行工具中的`java`）启动main函数，IDE中一般都自带Maven，能够帮助我们下载安装Maven依赖。
2. 在命令行工具中运行`mvn spring-boot:run`，但是此种方法要求你在本地环境中必须安装Maven

在控制台中可以发现启动了一个Tomcat容器，一个基于Spring MVC的应用也同时启动起来，这时访问`http://localhost:8080`就可以看到`Hello World!`出现在浏览器中了。

##### 代码详解

上述代码就是一个Java Web项目的骨架，在接下来的课程中我们会将其逐步完善并开发一个真正可用的网站。

- `main()`方法启动了一个HTTP服务器程序，这个程序默认监听`8080`端口，并将HTTP请求转发给我们的应用来处理
- `@Controller`标注表示`Application`类是一个处理HTTP请求的控制器，该类中所有被`@RequestMapping`标注的方法都会用来处理对应URL的请求，`@ResponseBody`标注告诉Spring MVC直接将字符串作为Web响应（Reponse Body）返回，如果`@ResponseBody`标注的方法返回一个对象，则会自动将该对象转换为JSON字符串返回
- `index()`方法上包含`@GetMapping("/")`标注，意味着在浏览器中访问`http://localhost:8080/`（不考虑协议、host和port信息后的路径为`"/"`）后浏览器发送的请求会交给该方法进行处理
- `index()`方法直接返回一个字符串，那么相当于直接将字符串`"Hello World"`作为HTTP请求的响应返回给浏览器，于是我们在浏览器中可以看到相应的返回值。

如果我们现在已经有一个用HTML语言编写的网页想让互联网上的其他人也能够通过这样的方式访问，那么最简单的办法就是把网页的内容作为`index()`方法的返回值，例如：

```java
@RequestMapping("/")
@ResponseBody
public String index() {
    return "<html><head><title>Hello World!</title></head><body><h1>Hello World!</h1><p>This is my first web site</p></body></html>";
}
```

<br/>

### 2. URL路由

#### 设计网站的URL

我们创建的个人博客网站如果需要将他们放在Internet中让其他人可以访问，就必须为它们设计一套URL，现在假设我们的网站通过`http://localhost:8080/`可以访问：

- `http://localhost:8080/`是网站首页，同时也应该对应当前的文章列表
- `http://localhost:8080/blogs/create`，创建新文章页面
- `http://localhost:8080/blogs/{id}/`，显示某一篇文章页面
- `http://localhost:8080/blogs/{id}/edit`，编辑某一篇文章页面

上面的URL设计都是符合REST风格的，参考[REST介绍](https://www.tianmaying.com/qa/134)。

#### 定义URL处理方法：@Controller和@RequestMapping

`@Controller`标注的类表示是一个处理HTTP请求的控制器(即MVC中的C)，该类中所有被`@RequestMapping`标注的方法都会用来处理对应URL的请求。

在Spring MVC框架中，使用`@RequestMapping`标注可以将URL与处理方法绑定起来，例如：

```java
@Controller
public class IndexController {
    
    @RequestMapping("/")
    @ResponseBody
    public String index() {
        return "index";
    }

    @RequestMapping("/hello")
    @ResponseBody
    public String hello() {
        return "hello";
    }
}
```

`IndexController`类被`@Controller`标注，其中的两个方法都被`@RequestMapping`标注，当应用程序运行后，在浏览器中访问`http://localhost:8080/`，请求会被Spring MVC框架分发到`index()`方法进行处理。同理，`http://localhost:8080/hello`会交给`hello()`方法进行处理。

`@ResponseBody`标注表示处理函数直接将函数的返回值传回到浏览器端显示。

`@RequestMapping`标注同样可以加在类上：

```java
@Controller
@RequestMapping("/blogs")
public class AppController {

    @RequestMapping("/create")
    @ResponseBody
    public String create() {
        return "mapping url is /blogs/create";
    }
}
```

`create()`绑定的URL路径是`/blogs/create`。

每一个类中都可以包含一个或多个`@RequestMapping`标注的方法，通常我们会将业务逻辑相近的URL（例如上例中的文章列表、创建文章）放在同一个Controller中进行处理。

#### @RequestMapping的简写形式

在Web应用中常用的HTTP方法有四种：

- PUT方法用来添加的资源
- GET方法用来获取已有的资源
- POST方法用来对资源进行状态转换
- DELETE方法用来删除已有的资源

这四个方法可以对应到CRUD操作（Create、Read、Update和Delete），比如博客的创建操作，按照REST风格设计URL就应该使用PUT方法，读取博客使用GET方法，更新博客使用POST方法，删除博客使用DELETE方法。

每一个Web请求都是属于其中一种，在Spring MVC中如果不特殊指定的话，默认是GET请求。

比如`@RequestMapping("/")`和`@RequestMapping("/hello")`和对应的Web请求是：

- **GET** `/`
- **GET** `/hello`

实际上`@RequestMapping("/")`是`@RequestMapping("/", method = RequestMethod.GET)`的简写，即可以通过`method`属性，设置请求的HTTP方法。

比如**PUT** `/hello`请求，对应于`@RequestMapping("/hello", method = RequestMethod.PUT)`

Spring MVC最新的版本中提供了一种更加简洁的配置HTTP方法的方式，增加了四个标注：

- `@PutMapping`
- `@GetMapping`
- `@PostMapping`
- `@DeleteMapping`

基于新的标注`@RequestMapping("/hello", method = RequestMethod.PUT)`可以简写为`@PutMapping("/hello")`。`@RequestMapping("/hello")`与`GetMapping("/hello")`等价。

#### 返回HTML

在之前所有的Controller方法中，返回值字符串都被直接传送到浏览器端并显示给用户。但是为了能够呈现更加丰富、美观的页面，我们需要将HTML代码返回给浏览器，浏览器再进行页面的渲染、显示。 一种很直观的方法是在处理请求的方法中，直接返回HTML代码：

```java
@GetMapping("/blog")
public String blog() {
    return "<html><head><title>Title</title></head><body><h2>This is a blog</h2><p>This is content of the blog.</p></body></html>";
}
```

显然，这样做的问题在于——一个复杂的页面HTML代码往往也非常复杂，并且嵌入在Java代码中十分不利于维护。

更好的做法是将页面的HTML代码写在模板文件中，然后读取该文件并返回。Spring天然支持这种非常常见的场景，需要先在`pom.xml`引入Thymeleaf依赖：

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

将HTML文本保存在`src/main/resources/templates/blog.html`下：

```html
<html>
  <head>
    <title>Title</title>
  </head>
  <body>
    <h1>This is title</h1>
    <p> This is Content</h2>
  </body>
</html>
```

`Controller`中可以去掉`@ResponseBody`标注(表示不是直接返回字符串，而是返回渲染的HTML模板)，并将URL处理函数的返回值设为刚刚保存在`templates/`文件夹中的文件名（不需要扩展名）：

```java
@GetMapping("/blogs")
public String blog() {
    return "blog";
}
```

Spring框架在发现返回值是`"blog"`后，就会去`src/main/resources/templates/`目录下读取`blog.html`文件并将它的内容返回给浏览器。

另外为了保证Thymeleaf能够正确的识别HTML5，还需要添加Maven依赖：

```xml
<dependency>
  <groupId>net.sourceforge.nekohtml</groupId>
  <artifactId>nekohtml</artifactId>
  <version>1.9.22</version>
</dependency>
```

在编写HTML代码时，请务必保证每一个标签都是闭合的，容易忽略的标签包括`<meta/>`, `<link/>`, `<br/>`, `<hr/>`, `<img/>`, `<input/>`等等。

但是在HTML5中，有些标签并不要求闭合，Thymeleaf遇到这样的HTML文件会报错。为了支持HTML5，你可以在Spring Boot的配置文件中增加一行配置：`spring.thymeleaf.mode=LEGACYHTML5`

Spring Boot的配置文件通常在`/resources`根目录下，以`application.properties`命名，没有这个这个文件则创建一个。

#### 静态资源处理

##### 外部资源文件

在编写HTML代码的过程中，我们会遇到几类外部静态资源：

- CSS文件：`<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css"/>`
- JavaScript文件：`<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>`
- 图像：`<img src="http://www.example.com/img/ios.png"/>`

这些外部资源都是通过HTTP协议访问得到——也就是说，当我们用浏览器打开我们编写的HTML页面（无论是通过本地文件直接打开，还是访问Spring Boot服务器），在获取页面内容本身之外，还需要向外部服务器（例如`maxcdn.bootstrapcdn.com`）发起HTTP请求以获取我们需要的CSS/JavaScript资源。

但是在我们开发过程中，如果某个时刻不能访问Internet，那我们的页面也就无法正确的展现出它应有的样式。另一方面，除了使用第三方库，我们自己还会编写大量的CSS/JavaScript文件，这就要求我们必须有一种很快的方式能够在修改之后立马在本地看到结果。

##### 本地资源文件组织

首先我们抛开本地HTTP服务器，简单来看在本地编写一个HTML文件以及使用CSS资源，那么我们可以这样组织项目结构：

```
.
├── index.html
├── css
    └── style.css
└── js
    └── main.js
```

在`index.html`文件中可以这样引用它们：

```html
<link rel="stylesheet" href="css/style.css"/>
<script src="js/main.js"></script>
```

`css/style.css`和`js/main.js`都是使用相对路径描述，当我们在浏览器中打开`index.html`，URL应该类似`file:///Users/tianmaying/app/index.html`，此时当浏览器解释到上述引用外部资源的代码，会以当前访问的URL为基准，根据相对路径计算出完整的HTTP请求地址：

```
Base: file:///Users/taotao/app/index.html
CSS: file:///Users/taotao/app/css/style.css
JavaScript: file:///Users/taotao/app/js/main.js
```

##### 服务器中的静态资源文件

如果我们需要将`index.html`放在服务器中呢？`index.html`位于`templates`目录下，通过`http://localhost:8080/`可以访问首页内容，但是CSS和JavaScript外部资源呢？因为我们的HTTP服务器根本没有处理它们，所以不可能通过类似`http://localhost:8080/css/style.css`这样的方式来访问它们使得我们的页面正确显示。

默认情况下，Spring Boot会将类路径上的`/static/`目录的内容Serve起来，意思就是对静态资源的请求，都会返回`/static/`目录中对应路径的文件内容，于是我们可以这样组织文件目录结构来处理静态资源（以下是`src/main/resources`目录结构，这个目录经过编译后会被添加到类路径上）：

```
.
├── static
    ├── css
        └── style.css
    └── js
        └── main.js
└── templates
    └── index.html
```

这样，当我们经过以上布局，重启应用后，就可以通过访问`http://localhost:8080/css/style.css`和`http://localhost:8080/js/main.js`来获取CSS和JavaScript资源了。

##### 在HTML中引入资源

最后我们将静态资源引入到HTML页面中，我们往往需要一种介于相对路径(如`css/style.css`)和绝对路径(如`http://www.example.com/img/ios.png`)之间的资源访问方式——**Context路径**：

```html
<link rel="stylesheet" href="/css/style.css"/>
<script src="/js/main.js"></script>
```

这里只是简单的在相对路径URL的最前面加上了`/`（`/css/style.css`），但是意义和相对路径就完全不同了，此时服务器会将其视为访问当前host中的“绝对路径”——也就是自动在这个路径之前添加上协议、主机名和端口（都是当前服务器的相同信息），那么无论我们访问的是当前网站下的任何路径，它都会给出统一的结果，从而正确引用到外部资源，即`http://localhost:8080/css/style.css`。

<br/>

### 3. @PathVariable

#### URL变量

对于相同模式的URL，我们可以在`@RequestMapping`注解里用`{}`来表明它的变量部分，例如:

```java
@GetMapping("/users/{username}")
```

这里`{username}`就是我们定义的变量规则，`username`是变量的名字。那么这个URL路由可以匹配下列任意URL并进行处理：

- `/users/taotao`
- `/users/ricky`
- `/users/tmy1234`

> 需要注意的是，在默认情况下，变量中不可以包含URL的分隔符`/`（Slash），例如上述路由不能匹配`/users/tianmaying/ricky`，即使你认为`tianmaying/ricky`是一个存在的用户名

在路由中定义变量规则后，通常我们需要在处理方法（也就是`@RequestMapping`注解的方法）中获取这个URL变量的具体值，并根据这个值（例如用户名）做相应的操作，Spring MVC提供的`@PathVariable`可以帮助我们：

```java
@GetMapping("/users/{username}")
public String userProfile(@PathVariable String username) {
    return String.format("user %s", username);
}
```

在上述例子中，当`@Controller`处理HTTP请求时，`userProfile`的参数`username`会被自动设置为URL中的对应变量`username`（同名赋值）的值，例如当HTTP请求为：`/users/taotao`，URL变量`username`的值`taotao`会被赋给函数参数`username`，函数的返回值自然也就是：`String.format("user %s", username);`，即`user taotao`。

在默认情况下，Spring会对`@PathVariable`注解的变量进行自动赋值，当然你也可以指定`@PathVariable`使用哪一个URL中的变量：

```java
@GetMapping("/users/{username}")
public String userProfile(@PathVariable("username") String user) {
    return String.format("user %s", user);
}
```

这时，由于注解`String user`的`@PathVariable`的参数值为`username`，所以仍然会将URL变量`username`的值赋给变量`user`。

可以定义URL路由，其中包含多个URL变量：

```java
@GetMapping("/users/{username}/blogs/{blogId}")
public String getUserBlog(@PathVariable String username, @PathVariable int blogId) {
    //具体实现，略
}
```

这种情况下，Spring能够根据名字自动赋值对应的函数参数值，当然也可以在`@PathVariable`中显式地表明具体的URL变量值。

在默认情况下，`@PathVariable`注解的参数可以是一些基本的简单类型：`int`，`long`，`Date`，`String`等，Spring能够根据URL变量的具体值以及函数参数的类型来进行转换，例如`/users/taotao/blogs/12`，会将`taotao`赋值给String变量`username`，而`12`赋值给`int`变量`blogId`。

#### 匹配正则表达式

很多时候，我们需要对URL变量进行更加精确的定义，例如——用户名只可能包含小写字母、数字和下划线，我们希望：

- `/users/tianmaying`是一个合法的URL
- `/users/#$$*#*#*`则不是一个合法的URL

除了简单地定义`{username}`变量，还可以定义正则表达式进行更精确的控制，定义语法是：`{变量名:正则表达式}`

```java
@GetMapping("/users/{username:[a-z0-9_]+}")
```

`[a-z0-9_]+`是一个正则表达式，表示只能包含小写字母、数字和下划线。如此设置URL变量规则后，不合法的URL则不会被处理，直接由Spring MVC框架返回`404 Not Found`。

#### 博客文章页面

每一篇博客文章都会有一个自己独有的标识符（identity，也就是我们常说的ID），那么我们会这样设计URL：

```java
@GetMapping("/blogs/{blogId}")
@ResponseBody
public String getBlog(@PathVariable int blogId) {
    Blog blog = blogService.findBlog(blogId);
    if (blog == null) {
        //异常处理，一般来说是抛出404错误
    }
    return blog.getTitle();
}

```

这里假设Blog的ID是`int`类型，例如：

- `/blogs/1`
- `/blogs/12`

`blogService`则是封装的博客的业务逻辑，例如根据ID获取博客内容、添加博客、删除博客等。在这个函数中我们只是简单把博客标题返回，那么浏览器中只会显示标题文字。在以后的学习中，我们会使用模板渲染技术显示更加美观、信息更多的博客页面。

<br/>

### 4. @RequestParam

#### Request参数

我们在访问各种各样的网站时，经常会发现网站的URL的最后一部分形如：`?xxxx=yyyy&zzzz=wwww`。这就是HTTP协议中的Request参数，它有什么用呢？我们先来看一个例子：

- 在知乎中搜索`web`
- 浏览器跳转到新页面后，URL变为`https://www.zhihu.com/search?type=content&q=web`

这里`type=content&q=web`就是搜索请求的参数，不同的参数之间用`&`分隔，每个参数形如`name=value`形式，分别表示参数名字和参数值。在这个例子中，我们输入不同的搜索关键词，在搜索结果页面的URL的`q`参数是不同的，也就是说，HTTP参数实际上可以认为是一种用户的**输入**，根据不同的用户输入，服务器经过处理后返回不同的**输出**（例如我搜索`奥运会`和搜索`世界杯`，显示的结果是不一样的）。

在Spring MVC框架中，现在我们已经可以通过定义`@RequestMapping`来处理URL请求了，和`@PathVariable`一样，我们也需要在处理URL的函数中获取URL中的参数——也就是`?key1=value1&key2=value2`这样的参数列表。通过注解`@RequestParam`可以轻松的将URL中的参数绑定到处理函数方法的变量中：

```java
@RestController
public class EditPetForm {

    @GetMapping("/blogs")
    public String setupForm(@RequestParam("id") int blogId) {
        return String.format("blog id = %d", blogId);
    }

}
```

这样当我们访问`/blogs?id=1`时，Spring MVC帮助我们将Request参数`id`的值绑定到了处理函数的参数`blogId`上。这样我们就能够轻松获取用户输入，并且根据它的值进行计算并返回了。

一旦我们在方法中定义了`@RequestParam`变量，如果访问的URL中不带有相应参数，就会抛出异常——这是显然的，Spring尝试帮我们进行绑定，然而没有成功。但是有的时候，参数确实不一定永远都存在，这时我们可以通过定义`required`属性：

```java
@RequestParam(name="id", required=false)//在默认情况下，required=true
```

当然，在参数不存在的情况下，我们可能希望变量有一个默认值：

```java
@RequestParam(name="id", required=false, defaultValue="0")
```

#### 分页

随着时间的流逝，我们的博客文章可能越来越多，博客列表页面如果一下显示几百甚至数千篇文章，首先是不方便查找，其次返回的文章过多也影响网页的加载效率。所以，对文章列表进行分页是非常有必要的，假设现在我们每页显示10篇文章，如果一共1000篇文章，那么就可以被分成100页。

访问`/blogs`时，我们需要通过告知服务器，我们需要第几页的文章，这里正是`@RequestParam`发挥用武之地的地方：

```
http://localhost:8080/blogs?page=1
```

相比于`/blogs/pages/1`，明显上面这个URl更加优雅、易懂。这时我们的处理函数可以这样定义：

```java
private static final int PAGE_SIZE = 10;

@GetMapping("/blogs")
//如果URL没有带page参数，我们默认显示第一页
public List<Blog> blogs(@RequestParam(name="page", required=false, defaultValue="1") int page) {
    int start = (page - 1) * PAGE_SIZE;//找到起始点
    List<blog> blogs = blogService.findBlogs().subList(start, start + PAGE_SIZE);
  
    return blogs;
}
```

当然我们完全也可以把每一页的大小`size`也作为参数放在URL中。

#### @RequestParam V.S @PathVariable

`@RequestParam`和`@PathVariable`都能够完成类似的功能——因为本质上，他们都是用户的输入，只不过输入的部分不同，一个在URL路径部分，另一个在参数部分。我们要访问一篇博客文章，这两种URL设计都是可以的：

- 通过`@PathVariable`，例如`/blogs/1`
- 通过`@RequestParam`，例如`blogs?blogId=1`

如何选择：

1. 当URL指向的是某一具体业务资源（或者资源列表），例如博客、用户时，使用`@PathVariable`
2. 当URL需要对资源或者资源列表进行过滤、筛选时，用`@RequestParam`

例如我们会这样设计URL：

- `/blogs/{blogId}`
- `/blogs?state=publish`而不是`/blogs/state/publish`来表示处于发布状态的博客文章

<br/>

### 5. 模板渲染

#### 模板

开发Web站点的本质，其实就是根据浏览器发起的请求（输入），生成HTML代码返回给浏览器（输出）。博客站点是动态的，即不同的请求返回的内容可能不同。但是对于同一类请求，例如访问`id`分别为`1`和`2`的两篇文章，对应的URL为`/blogs/1`, `/blogs/2`，他们返回的HTML代码片段的**结构**几乎是一样的：

```html
<div class="col-sm-8">
  <div class="page-header">
    <h2>Cum sociis（博客标题）</h2>
    <p class="blog-post-meta">2015年2月3日 标签：<a href="#">Web开发</a></p>
  </div>

  <div class="blog-post-content">
    ...
    （这里是博客内容）
  </div>
</div>
```

除了文章的时间、分组以及文章内容不同，其他的HTML部分都是一样的，这也就是**模板**提出的动机——抽取出公共部分的内容作为模板，然后根据不同的数据进行渲染，得到不同的输出值。渲染工作就是由**模板引擎**来完成的。

Spring Boot默认使用[Thymeleaf](https://thymeleaf.org/)作为模板引擎（已经配置过`spring-boot-starter-thymeleaf`依赖）。基于Thymeleaf提供的标签，我们可以将之前编写的静态HTML内容改写为动态的：

```html
<div class="col-sm-8">
  <div class="page-header">
    <h2 th:text="${title}">Cum sociis（博客标题）</h2>
    <p class="blog-post-meta"><span th:text="${createdTime}">2015年2月3日</span> 标签：<a href="#">Web开发</a></p>
  </div>

  <div class="blog-post-content" th:text="${content}">
    ...
    （这里是博客内容）
  </div>
</div>
```

`th:text="${title}"`就是告诉模板引擎，用`title`变量作为`<h2>`标签的内容（`createdTIme`, `content`也是一样）。注意为了显示博客创建时间，我们将时间放入了一个`<span>`标签中，用于和其它文字区分开。

使用Thymeleaf的标签，为了避免在IDE中出现错误提示，可以在HTML文件中增加命名空间的定义：

```html
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org">
    ...
</html>
```

#### Model

为了让模板引擎知道这些变量的值，我们需要在`@Controller`做一些工作：

```java
import org.springframework.ui.Model;

@RequestMapping("/blogs/{id}")
public String index(@PathVariable("id") long id, Model model) {
    // 这里我们模拟一些数据
    model.addAttribute("title", "This is a blog with id = " + id);
    model.addAttribute("createdTime", "2015-08-11");
    model.addAttribute("content", "This is content");
    return "index";
}
```

在上面的代码中，`index()`方法增加了一个`Model`类型的参数。通过Spring MVC框架提供的`Model`，可以调用其`addAttribute`方法，这样Thymeleaf可以访问Model中的变量从而进行模板渲染。上述例子中可以看到，`title`变量的值是根据URL中的`@PathVariable`来确定的，虽然简单，但是这已经是一个动态页面了。

在Servlet编程中，如果希望在页面中动态渲染信息，一般需要往`HttpRequest`中添加属性，然后在JSP中获取。其实`Model`的属性实际上也是放在`HttpRequest`的属性中，但是Spring MVC提供了更高层的抽象，帮你屏蔽了`HttpRequest`，你看到的只有直接以MVC中M（即Model）。

当我们真正拥有数百篇博文时，并且时常还会添加（或者删除、更新），显然不能够直接在`@Controller`方法中这样来填充`Model`。另外如果需要渲染文章列表，那么这种方法显然也是不可行的。为了解决这个问题，我们需要使用参考代码JAR包中提供的`Blog`类：

```java
public class Blog {
    private Long id;
    private String title;
    private String content;
    private Date createdTime;
    //Getter/Setter方法略
}
```

在单篇文章页面里，对于每一个属性，都需要调用一次`Model.addAttribute()`方法，属性如果很多就会很不方便。现在我们有了`Blog`对象，可以将它放入`Model`:

```java
@RequestMapping("/blogs/{id}")
public String blog(@PathVariable("id") int id, Model model) {
	  Blog blog = blogService.findBlog(id);
    model.addAttribute("blog", blog);
    return "item";
}
```

根据URL中的id获取对应的`Blog`对象，然后交给模板引擎渲染`blog`，相应地在模板中的变量表达式也要发生变化：

```html
<div class="col-sm-8">
  <div class="page-header">
    <h2 th:text="${blog.title}">Cum sociis（博客标题）</h2>
    <p class="blog-post-meta"><span th:text="${blog.createdTime}">2015年2月3日</span> 标签：<a href="#">Web开发</a></p>
  </div>

  <div class="blog-post-content" th:text="${blog.content}">
    ...
    （这里是博客内容）
  </div>
</div>
```

往Model中添加对象有两种方式：

1. `model.addAttribute("blog", blog);`
2. `model.addAttribute(blog);`

使用第二种时，对象在Model中的命名默认为类名的首字母小写形式，任何时候对于同一种类型，只可能存在一个这样的“匿名”对象

#### 日期格式化

文章页面经过模板渲染处理后，还存在一个小问题：日期格式。现在对于`${blog.created}`的渲染结果是`Tue Aug 25 19:28:49 CST 2015`，这是因为这个`${blog.created}`是一个`Date`对象，模板引擎在渲染时会直接调用它的`toString()`方法。

格式化日期是一个非常常见的任务，为此Thymeleaf提供了内置的支持：

```html
 <p class="blog-post-meta"><span th:text="${#dates.format(blog.createdTime, 'yyyy-MM-dd')}">2015年2月3日</span> 标签：<a href="#">Web开发</a></p>
```

[`#dates`](https://www.thymeleaf.org/doc/tutorials/2.1/usingthymeleaf.html#dates)是Thymeleaf内置的一个工具类，`format()`方法可以指定日期的格式。

#### 渲染文章列表页面

现在如果要渲染文章列表页面呢？文章数量是不定的，所以需要借助Thymeleaf中循环的概念，首先将文章集合放入`Model`：

```java
@RequestMapping("/")
public String index(Model model) {
    List<Blog> blogs = blogService.findBlogs();
    model.addAttribute("blogs", blogs);
    return "index";
}
```

在模板引擎中，可以通过`th:each`属性对指定集合进行遍历：

```html
<div class="blog-post" th:each="blog: ${blogs}">
  <h3 class="blog-post-title"><a th:text="${blog.title}" th:href="@{'/blogs/' + ${blog.id}}">Lorem ipsum dolor sit amet</a></h3>
  ...
</div>
```

`th:each="blog : ${blogs}"`表示对`blogs`变量进行遍历，循环变量为`blog`，那么在循环体中我们就可以通过`${blog.title}`这样的表达式来访问每一个对象的属性了。另外上述代码实际上生成的是多个`<div class="blog-post">`，也就是`th:each`所在的元素。

对于静态模板其他的`<div class="blog-post">...</div>`，添加`th:remove="all"`属性，渲染时就会自动忽略，即：

```html
<div class="blog-post" th:remove="all">
 ...
</div>
```

我们希望能够在文章列表页面中点击每一篇文章的标题可以进入到相应的文章页面，这就需要修改`<a>`的`href`属性，在Thymeleaf中支持使用`th:href`来动态渲染URL路径，但是和`th:text`稍有不同的是，渲染URL时需要使用`@`标识符：

```html
<a href="#" th:href="@{'/blogs/'+${blog.id}}" th:text="${blog.title}"></a>
```

#### Thymeleaf

[Thymeleaf](http://www.thymeleaf.org/)是一款用于渲染XML/XHTML/HTML5内容的模板引擎。类似JSP，Velocity，FreeMaker等，它也可以轻易的与Spring MVC等Web框架进行集成作为Web应用的模板引擎。与其它模板引擎相比，Thymeleaf能够直接在浏览器中打开并正确显示模板页面，而不需要启动整个Web应用。

相比于其他的模板引擎，Thymeleaf最大的特点是通过HTML的标签属性渲染标签内容，以下是一个Thymeleaf模板例子：

```html
<!DOCTYPE html SYSTEM "http://www.thymeleaf.org/dtd/xhtml1-strict-thymeleaf-4.dtd">

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org">

  <head>
    <title>Good Thymes Virtual Grocery</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" media="all" 
          href="../../css/gtvg.css" th:href="@{/css/gtvg.css}" />
  </head>

  <body>

    <p th:text="#{home.welcome}">Welcome to our grocery store!</p>

  </body>

</html>
```

这是一段标准的HTML代码，这也就意味着通过浏览器直接打开它是可以正确解析它的结构并看到页面的样子。Thymeleaf是一种针对HTML/XML定制的模板语言（当然它可以被扩展），它通过标签中的`th:text`属性来填充该标签的一段内容。上例中，`<p th:text="#{home.welcome}">Welcome to our grocery store!</p>`意味着`<p>`标签中的内容会被表达式`#{home.welcome}`的值所替代，无论模板中它的内容是什么，之所以在模板中“多此一举“地填充它的内容，完全是为了它能够作为原型在浏览器中直接显示出来。

##### 标准表达式语法

###### 变量

Thymeleaf模板引擎在进行模板渲染时，还会附带一个Context存放进行模板渲染的变量，在模板中定义的表达式本质上就是从Context中获取对应的变量的值：

```html
<p>Today is: <span th:text="${today}">13 february 2011</span>.</p>
```

假设`today`的值为`2015年8月14日`，那么渲染结果为：`<p>Today is: 2015年8月14日.</p>`。可见Thymeleaf的基本变量和JSP一样，都使用`${.}`表示获取变量的值。

###### URL

URL在Web应用模板中占据着十分重要的地位，**需要特别注意的是**Thymeleaf对于URL的处理是通过语法`@{...}`来处理的。Thymeleaf支持绝对路径URL：

```html
<a th:href="@{http://www.thymeleaf.org}">Thymeleaf</a>
```

同时也能够支持相对路径URL：

- 当前页面相对路径URL——`user/login.html`，通常不推荐这样写。
- Context相关URL——`/static/css/style.css`

另外，如果需要Thymeleaf对URL进行渲染，那么务必使用`th:href`，`th:src`等属性，下面是一个例子

```html
<!-- Will produce 'http://localhost:8080/gtvg/order/details?orderId=3' (plus rewriting) -->
<a href="details.html" 
   th:href="@{http://localhost:8080/gtvg/order/details(orderId=${o.id})}">view</a>

<!-- Will produce '/gtvg/order/details?orderId=3' (plus rewriting) -->
<a href="details.html" th:href="@{/order/details(orderId=${o.id})}">view</a>

<!-- Will produce '/gtvg/order/3/details' (plus rewriting) -->
<a href="details.html" th:href="@{/order/{orderId}/details(orderId=${o.id})}">view</a>
```

几点说明：

- 上例中URL最后的(orderId=${o.id})表示将括号内的内容作为URL参数处理，该语法避免使用字符串拼接，大大提高了可读性
- `@{...}`表达式中可以通过`{orderId}`访问Context中的`orderId`变量
- `@{/order}`是Context相关的相对路径，在渲染时会自动添加上当前Web应用的Context名字，假设context名字为app，那么结果应该是`/app/order`

###### 字符串替换

很多时候可能我们只需要对一大段文字中的某一处地方进行替换，可以通过字符串拼接操作完成：

```html
<span th:text="'Welcome to our application, ' + ${user.name} + '!'">
```

一种更简洁的方式是：

```html
<span th:text="|Welcome to our application, ${user.name}!|">
```

当然这种形式限制比较多，`|...|`中只能包含变量表达式`${...}`，不能包含其他常量、条件表达式等。

###### 运算符

在表达式中可以使用各类算术运算符，例如+, -, *, /, %

```html
th:with="isEven=(${prodStat.count} % 2 == 0)"
```

逻辑运算符`>`, `<`, `<=`,`>=`，`==`,`!=`都可以使用，唯一需要注意的是使用`<`,`>`时需要用它的HTML转义符：

```html
th:if="${prodStat.count} &gt; 1"
th:text="'Execution mode is ' + ( (${execMode} == 'dev')? 'Development' : 'Production')"
```

##### 循环

渲染列表数据是一种非常常见的场景，例如现在有n条记录需要渲染成一个表格`<table>`，该数据集合必须是可以遍历的，使用`th:each`标签：

```html
<body>
  <h1>Product list</h1>

  <table>
    <tr>
      <th>NAME</th>
      <th>PRICE</th>
      <th>IN STOCK</th>
    </tr>
    <tr th:each="prod : ${prods}">
      <td th:text="${prod.name}">Onions</td>
      <td th:text="${prod.price}">2.41</td>
      <td th:text="${prod.inStock}? #{true} : #{false}">yes</td>
    </tr>
  </table>

  <p>
    <a href="../home.html" th:href="@{/}">Return to home</a>
  </p>
</body>
```

可以看到，需要在被循环渲染的元素（这里是`<tr>`）中加入`th:each`标签，其中`th:each="prod : ${prods}"`意味着对集合变量`prods`进行遍历，循环变量是`prod`在循环体中可以通过表达式访问。

##### 条件求值

###### If/Unless

Thymeleaf中使用`th:if`和`th:unless`属性进行条件判断，下面的例子中，`<a>`标签只有在`th:if`中条件成立时才显示：

```
<a th:href="@{/login}" th:unless=${session.user != null}>Login</a>
```

`th:unless`于`th:if`恰好相反，只有表达式中的条件不成立，才会显示其内容。

###### Switch

Thymeleaf同样支持多路选择Switch结构：

```
<div th:switch="${user.role}">
  <p th:case="'admin'">User is an administrator</p>
  <p th:case="#{roles.manager}">User is a manager</p>
</div>
```

默认属性default可以用`*`表示：

```
<div th:switch="${user.role}">
  <p th:case="'admin'">User is an administrator</p>
  <p th:case="#{roles.manager}">User is a manager</p>
  <p th:case="*">User is some other thing</p>
</div>
```

##### Utilities

为了模板更加易用，Thymeleaf还提供了一系列Utility对象（内置于Context中），可以通过`#`直接访问：

- `#dates`
- `#calendars`
- `#numbers`
- `#strings`
- `arrays`
- `lists`
- `sets`
- `maps`
- ...

下面用一段代码来举例一些常用的方法：

###### `#dates`

```
/*
 * Format date with the specified pattern
 * Also works with arrays, lists or sets
 */
${#dates.format(date, 'dd/MMM/yyyy HH:mm')}
${#dates.arrayFormat(datesArray, 'dd/MMM/yyyy HH:mm')}
${#dates.listFormat(datesList, 'dd/MMM/yyyy HH:mm')}
${#dates.setFormat(datesSet, 'dd/MMM/yyyy HH:mm')}

/*
 * Create a date (java.util.Date) object for the current date and time
 */
${#dates.createNow()}

/*
 * Create a date (java.util.Date) object for the current date (time set to 00:00)
 */
${#dates.createToday()}
```

###### `#strings`

```html
/*
 * Check whether a String is empty (or null). Performs a trim() operation before check
 * Also works with arrays, lists or sets
 */
${#strings.isEmpty(name)}
${#strings.arrayIsEmpty(nameArr)}
${#strings.listIsEmpty(nameList)}
${#strings.setIsEmpty(nameSet)}

/*
 * Check whether a String starts or ends with a fragment
 * Also works with arrays, lists or sets
 */
${#strings.startsWith(name,'Don')}                  // also array*, list* and set*
${#strings.endsWith(name,endingFragment)}           // also array*, list* and set*

/*
 * Compute length
 * Also works with arrays, lists or sets
 */
${#strings.length(str)}

/*
 * Null-safe comparison and concatenation
 */
${#strings.equals(str)}
${#strings.equalsIgnoreCase(str)}
${#strings.concat(str)}
${#strings.concatReplaceNulls(str)}

/*
 * Random
 */
${#strings.randomAlphanumeric(count)}
```

#### 使用JSP

Spring Boot也可以使用JSP作为模板引擎（任何常见的Java模板引擎都支持），为了使用JSP，你只需以下三部操作：

1. 添加JSP相关的依赖

```xml
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-tomcat</artifactId>
    <scope>provided</scope>
  </dependency>
  <dependency>
    <groupId>org.apache.tomcat.embed</groupId>
    <artifactId>tomcat-embed-jasper</artifactId>
    <scope>provided</scope>
  </dependency>
  <dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>jstl</artifactId>
    <scope>provided</scope>
  </dependency>
```

1. 将JSP模板文件放在目录`webapp/WEB-INF/jsp/`中（一般放在`webapp/WEB-INF`目录下，`jsp`可以换成其它命名）
2. 在Spring Boot的配置文件中添加配置：

```
spring.mvc.view.prefix: /WEB-INF/jsp/ ## 对应实际JSP模板文件的路径
spring.mvc.view.suffix: .jsp
```

接下来你就可以愉快地使用JSP了，Controller层的代码无需任何修改。

<br/>

### 6. 分页处理

#### 分页查询

Spring Data 提供了`Pageable`类来支持分页查询，在服务层的实现中我们也提供了这样的接口，比如：

```java
/**
 * 分页获取所有博客
 * @param pageable
 * @return
 */
Page<Blog> findBlogs(Pageable pageable);
```

其中：

- `Pageable`接口是所有分页相关信息（如`pageNumber`和`pageSize`）的一个抽象，Spring Data JPA能够通过`Pageable`参数来生成带分页信息的SQL语句
- `Page`接口表示包含了分页信息的查询结果

有了分页查询方法的定义后，首先需要实例化一个`Pageable`对象。`Pageable`定义了很多方法，但其核心的信息只有两个：

- 分页的信息（`page`和`size`）
- 排序的信息

Spring Data提供了`PageRequest`作为`Pageable`的具体实现，我们直接实例化`PageRequest`对象即可：

```java
Sort sort = new Sort(Direction.DESC, "id");
Pageable pageable = new PageRequest(page, size, sort);
return blogService.findAll(pageable);
```

上面的代码通过参数获得分页的信息，并通过`Sort`以及`Direction`告诉`Pageable`需要通过`id`逆序排列。

之前我们使用了`subList`方法来返回分页数据：

```java
List<blog> blogs = blogService.findBlogs().subList(start, start + PAGE_SIZE);
```

实际上，可以直接调用`BlogService`的`findBlogs(Pageable pageable)`方法：

```java
@Controller
@RequestMapping(value = "/blogs")
public class BlogController {

    @Autowired BlogService blogService;

    @GetMapping
    public String getEntryByPageable(@RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "size", defaultValue = "15") Integer size, Model model) {
        Sort sort = new Sort(Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        model.addAttribute("blogs", blogService.findBlogs(pageable));
        return "list";
    }

}
```

#### 自动绑定Page参数

其实Spring MVC对分页提供了更强大的支持，我们可以进一步简化代码。只需要在Controller方法的参数中直接定义一个`Pageble`类型的参数，Spring就会根据请求的参数来自动创建`Pageable`对象。

```java
@GetMapping
public String getEntryByPageable(@PageableDefault(value = 15, sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
    model.addAttribute("blogs", blogService.findBlogs(pageable));
    return "list";
}
```

Spring支持的请求参数名如下：

- `page`：表示第几页，从0开始，默认为第0页
- `size`：表示每一页的大小，默认为20
- `sort`：排序相关的信息，以`property,property(,ASC|DESC)`的方式组织，例如：
  - `sort=firstname&sort=lastname,desc`表示在按`firstname`正序排列基础上按`lastname`倒序排列

也就是只需要在浏览器端发送包含这些参数的请求，Controller就能够自动识别这些参数，并且生成`Pageable`对象。

也可以为`Pageable`参数设置一个默认配置，这样浏览器端的URL就可以更加简洁。Spring提供了`@PageableDefault`进行个性化的设置`Pageable`的默认配置。例如:

```java
@PageableDefault(value = 15, sort = { "id" }, direction = Sort.Direction.DESC)
```

表示默认情况下按照`id`倒序排列，每一页的大小为`15`。

`@PageableDefault`的属性包括：

- `page`
- `size`
- `value`，等同于`size`
- `sort`
- `direction`

如果将生成的`Page<Blog>`对象以JSON字符串的形式（加`@ResponseBody`即可）返回的话，输出结果看起来是这个样子：

```
{
  "content":[
    {"id":123,"title":"blog122","content":"this is blog content"},
    {"id":122,"title":"blog121","content":"this is blog content"},
    {"id":121,"title":"blog120","content":"this is blog content"},
    {"id":120,"title":"blog119","content":"this is blog content"},
    {"id":119,"title":"blog118","content":"this is blog content"},
    {"id":118,"title":"blog117","content":"this is blog content"},
    {"id":117,"title":"blog116","content":"this is blog content"},
    {"id":116,"title":"blog115","content":"this is blog content"},
    {"id":115,"title":"blog114","content":"this is blog content"},
    {"id":114,"title":"blog113","content":"this is blog content"},
    {"id":113,"title":"blog112","content":"this is blog content"},
    {"id":112,"title":"blog111","content":"this is blog content"},
    {"id":111,"title":"blog110","content":"this is blog content"},
    {"id":110,"title":"blog109","content":"this is blog content"},
    {"id":109,"title":"blog108","content":"this is blog content"}],
  "last":false,
  "totalPages":9,
  "totalElements":123,
  "size":15,
  "number":0,
  "first":true,
  "sort":[{
    "direction":"DESC",
    "property":"id",
    "ignoreCase":false,
    "nullHandling":"NATIVE",
    "ascending":false
  }],
  "numberOfElements":15
}
```

从中可以返回结果的信息：

- 以id倒序排列的10条数据
- 当前页不是最后一页，后面还有数据
- 总共有9页
- 每页大小为15
- 当前页为第0页
- 当前页是第一页
- 当前页是以id倒序排列的
- 当前页一共有15条数据

#### 下一页

利用Spring MVC对分页的支持，来实现博客列表页面“下一页”的功能。

之前已经在Controller中为Model增加`blog`属性：

```java
model.addAttribute("blogs", blogService.findBlogs(user, pageable));
```

从`blogs`的JSON表示可以知道，通过`blogs.last`可以知道是否还有下一页，因为用`th:unless`标签来控制“下一页”链接的使用：

```html
<nav>
  <ul class="pager">
    <li class="previous"><a href="#"><span aria-hidden="true">&larr;</span> 上一页</a></li>
    <li th:unless="${blogs.last}" class="next"><a th:href="@{'/'+ ${user.name} + '?page=' + ${blogs.number + 1}}">下一页 <span aria-hidden="true">&rarr;</span></a></li>
  </ul>
</nav>
```

在渲染URL时`<a th:href="@{'/'+ ${user.name} + '?page=' + ${blogs.number + 1}}">`还用到了`user`的信息，因此也需要在Controller中往Model中添加`user`属性。

<br/>

### 7. 表单提交

#### Web表单与POST

[表单](https://en.wikipedia.org/wiki/Form_(HTML))是Web页面的一部分，用来产生更为复杂的HTTP请求，它使得用户可以将数据发送至服务器端进行处理。Web表单其实是一种产生HTTP请求的Web控件。

HTML中`<form>`标签的`enctype`属性用来指定表单编码格式，默认为`application/x-www-form-urlencoded`，即以下两个表单完全等价。

```html
<form method='post'>
  <input type="text" name='title'>
  <input type="text" name='content'>
  <input type="submit">
</form>
```

```html
<form  method='post' enctype='application/x-www-form-urlencoded'>
  <input type="text" name='title'>
  <input type="text" name='content'>
  <input type="submit">
</form>
```

上述表单将会显示为两个文本框和一个提交按钮。我们在文本框中分别写入`test`和`中国`后，点击提交按钮。产生的HTTP请求可能是这样的：

请求头（这里只给出了`Content-Type`字段）：

```
POST http://www.example.com HTTP/1.1
Content-Type: application/x-www-form-urlencoded
```

请求体：

```
title=test&subtitle=%E4%B8%AD%E5%9B%BD
```

#### 显示表单页面

为了让用户可以创建新文章，首先需要提供一个输入新文章信息的页面，这是一个典型的GET请求，给用户显示文章创建表单。可以这样设计url：

```java
@GetMapping(value = "/blogs/create")
public String createPage() {
    return "create";
}
```

#### 处理POST请求与数据绑定

通过`@RequestMapping`注解来标注某一个方法用来处理POST请求：

```java
@PostMapping(value = "/blogs")
public String create(@RequestParam("title") String title,
                     @RequestParam("content") String content) {
    //save title and content to repository
}
```

和GET请求一样，`@RequestParam`注解可以解析HTTP请求中Body中的内容，自动将Body中`title`, `content`属性对应的值注入对应的变量中。

类似[分页](https://course.tianmaying.com/spring-mvc/lesson/paging#3)中讲的于自动绑定`Pageable`参数，也可以在Controller方法中直接传入`Blog`对象，Spring MVC会自动将HTTP请求内容注入到`Blog`对象的**同名**属性：

```java
@PostMapping(value = "/blogs")
public String create(Blog blog) {
    //save title and content to repository
}
```

本质上`blog`参数实际上是由Spring创建并设置相应的字段，所以需要保证**Blog类有默认构造函数**。

#### Redirect

处理POST请求除了保存博客信息之外，最后还需要做一步操作：让用户跳转到显示博客信息的页面。

在Web开发中，一种叫做[Post/Redirect/GET](https://en.wikipedia.org/wiki/Post/Redirect/Get)的模式被提出并广泛应用在Web程序开发中。和传统的表单提交方法不同，这种模式在POST请求完成后，会重定向（HTTP状态码302）到另外一个页面。这样相当于浏览器重新加载了一个页面（使用GET请求），此时用户无论怎样刷新浏览器，都是刷新当前GET请求对应的页面而不再是重复提交一次表单。

在Spring MVC中可以这样处理Redirect：

```java
@RequestMapping(value = "/blogs", method = RequestMethod.POST)
public String create(Blog blog) {
    //save title and content to repository
    return "redirect:/blogs/" + blog.getId();  // 表示重定向到博客信息页面
}
```

相反，如果表单请求不使用这种Post/Redirect/GET模式，操作完成后如果用户刷新浏览器，浏览器会提示用户是否继续提交表单数据。这样提示的原因是，这是一个POST请求，刷新相当于执行之前的一次表单提交操作，只不过这一次提交不再需要用户填写表单数据——浏览器已经将之前填写的表单数据缓存起来以便刷新。

#### 初始化表单数据

文章编辑的表单和文章创建的表单是完全一样的，可以共用一个模板。但是两者处理上有一些不同，如果是更新一篇文章，文章编辑的表单必须有初始化数据，这时需要在Controller的Model中添加博客信息。

```java
@GetMapping(value = "/blogs/{id}/edit")
public String createPage(@PathVariable long id, Model model) {
    
    Blog blog = blogService.findOne(id);
    model.addAttribute("blog", blog);

    return "create";
}
```

此时在`create.html`页面就需要获取Model的值来渲染初始数据了。

```html
<form method='post'>
  <input type="text" name='title' value="${blog.title}">
  <input type="text" name='content' value="${blog.content}">
  <input type="submit">
</form>
```

另外一种方式是通过Thymeleaf提供的表单绑定标签来处理页面。下面是简化过的示例：

```html
<form th:action="@{/blogs}" th:object="${blog}" method='post'>
  <input type="text" th:field="*{title}">
  <input type="text" th:field="*{content}">
  <input type="submit">
</form>
```

因为在`create.html`中添加了`th:object="{blog}"`，所以必须在创建表单页面请求的处理方法中加入一个`blog`对象：

```java
@GetMapping(value = "/create")
public String showCreatePage(Model model) {
    model.addAttribute("blog", new Blog());
    return "create";
}
```

即添加一个数据为空的`Blog`对象，否则显示博客创建页面的时候将出现异常。

使用表单绑定标签的一个好处是可以天然支持表单验证出错信息的显示。

<br/>

### 8. 表单验证

#### Spring数据绑定

[处理POST请求与数据绑定](https://course.tianmaying.com/spring-mvc/lesson/form#2)中已经介绍过，Spring支持在`@RequestMapping`注解的方法中使用对象来进行参数绑定：

```java
@PostMapping(value = "/blogs")
public String create(Blog blog) {
    // ...
}
```

`Blog`对象中有两个字段`title`和`content`，Spring会将HTTP请求中的数据`title`和`content`自动注入（根据名字进行匹配）到`Blog`对象中。

对于用户提交的数据，不能够直接将他们不加检验的存储起来。假设现在要求文章的标题长度必须在2-30个字符之间，文章内容不能为空。一种最直观的办法就是在Controller方法中进行判断：

```java
@PostMapping(value = "/blogs")
public String create(Blog blog) {
    if (validate(blog)) {
        // normal handling
    } else {
        //error handling
    }
    
    // return
}
```

`validate()`方法中可以进行各种检查，但是这种方法的问题在于——如果字段过多，那么需要编写很多`if-else`语句；另一方面，这一类数据校验逻辑大多数是通用的，例如创建文章的表单数据和用户注册的表单数据基本都需要验证数据长度，在每一个方法中都编写一个验证逻辑显然是非常重复的。为了解决这个问题，可以使用`Java Validation API`：

```java
import javax.validation.constraints.Size;

public class Blog {

    private Long id;

    @Size(min=2, max=30)
    private String title;

    @Size(min=1)
    private String content;

    //其余方法、字段省略
}
```

`@Size(min=2, max=30)`表示对应属性的字符串长度必须在2到30之间。

此外，可以通过`message`属性来设置验证的信息，比如`@Size(min=2, max=30, message="博客标题长度为2-30")`，那么验证失败时将可以返回`message`信息。

当然，用于描述验证的规则的标注还有很多，大家可以在[这里](https://beanvalidation.org/1.0/spec/#d0e5601)了解，今后的学习中我们还会介绍更多注解的应用场景。

但我们知道，注解仅仅是对类加上了一些元信息，如果不使用反射等API对其进行探测、处理，和不加注解没有任何区别。所以在Controller方法中需要告知Spring框架进行数据校验：

```java
@PostMapping(value = "/")
public String create(@Valid Blog blog) {
    // ...
}
```

Spring一旦发现`@Valid`注解，就会根据`Blog`类中的注解规则进行数据校验，所以现在重新启动应用，再次提交表单，如果数据不符合校验规则（比如输入长度为1的博客标题），会出现如下错误提示：

```
Whitelabel Error Page

This application has no explicit mapping for /error, so you are seeing this as a fallback.

Fri Aug 21 10:09:54 CST 2015
There was an unexpected error (type=Bad Request, status=400).
Validation failed for object='post'. Error count: 2
```

#### Form对象和Domain对象

有时表单绑定的对象可以专门创建一个类来表示，比如`UserForm`用以表示Web请求传上来的用户信息，则相关的Java验证标注应用于`UserForm`即可。

在复杂的应用中一般都会区分`UserForm`和`User`，因为两者往往不一致，比如用户注册是需要输入两次密码，则`UserForm`类中可以添加一个`repeatedPassword`属性，但是`User`类中并不需要。甚至一个Model往往对应于多个Form，比如`UserLoginForm`和`UserRegisterForm`。

另外一种需要专门的Form对象的情况就是我们做练习的这种场景。因为`Blog`在第三方的JAR中引入，我们并不能直接修改其代码。所以需要创建`BlogCreateForm`来获取表单数据：

```java
import javax.validation.constraints.Size;

public class BlogCreateForm {

    @Size(min=2, max=30)
    private String title;

    @Size(min=1)
    private String content;

    //其余方法、字段省略

    // 将BlogCreateForm转换为Blog的方法
    Blog toBlog() {
        Blog blog = new Blog();
        blog.setTitle(this.title);
        ...
        return blog;
    }
}
```

相应地，就需要在`BlogCreateForm`中提供一个将自身转换为`Blog`的方法，而这个方法在Controller中调用。

#### 错误处理

很显然，Spring发现了用户提交的错误数据，但是返回这样的错误页面给用户，会让用户一头雾水不知所云。所以一旦发现错误以后，需要重新返回创建文章的页面并把错误提示给用户：

```java
@PostMapping(value = "/")
public String create(@Valid Blog blog, BindingResult result) {

    if (result.hasErrors()) {
        return "create";
    }
    //save title and content to repository
    
    return "redirect:/blogs/" + blog.getId(); 
}
```

在模板中可以这样获取这些错误信息，下面是简化的HTML示例：

```html
<form th:action="@{/blogs}" th:object="${blog}" method='post'>
  <input type="text" th:field="*{title}">
  <p th:if="${#fields.hasErrors('title')}" th:errors="*{title}">标题长度必须在2-30之间</p>
  <input type="text" th:field="*{content}">
  <p th:if="${#fields.hasErrors('content')}" th:errors="*{content}">内容不可为空</p>
  <input type="submit">
</form>
```

`th:object="${blog}"`表示这是一个bean-backed的表单，表示在每个表单域的后面，都跟随着一个`<p>`元素来显示错误验证错误信息，比如`<p th:if="${#fields.hasErrors('title')}" th:errors="*{title}">标题长度必须在2-30之间</p>`。

经过上述处理，再次提交表单就可以发现用户的错误数据能够很好地被处理了。

<br/>

### 9. Flash Attribute

#### 在Controller中使用Flash Attribute

Spring MVC中的Model中的属性(通过`Model.attribute()`方法添加或者通过`ModelAttribute`标注），其生命周期仅限于单个Web请求中，但是`Flash Attributes`则是针对于`POST/Redirect/GET`进行设计的——在Redirect之前的POST请求中加入Flash Attributes中的数据，会被**短暂**的存储下来（一般是Session中），直到Redirect后的GET请求完成，所以在GET请求中可以获取之前POST请求中放入Flash Attributes中的数据，这样也就完美的解决了之前提到的问题。

在Spring MVC中，被`@PostMapping`注解的方法，可以有一个`RedirectAttributes`参数：

```java
@PostMapping(value = "/login")
public String login(@RequestParam("username") String username,
                    @RequestParam("password") String password,
                    HttpSession session,
                    final RedirectAttributes redirectAttributes) {
    if (!verify(username, password)) {
        redirectAttributes.addFlashAttribute("message", "username/password invalid");
        return "redirect:/login";
    }

    session.setAttribute(SESSION_LOGGED_IN, true);
    redirectAttributes.addFlashAttribute("message", "Login Success");
    return "redirect:/";
}
```

可以看到，`redirectAttributes.addFlashAttribute()`方法就是向Flash Attributes中添加一条属性，在GET请求的处理方法中，获取它也非常简单——它被放在Model中：

```java
@GetMapping
public String index(@ModelAttribute("message) String message) {
    return "index";
}
```

`@ModelAttribute`标注了`message`参数，你可以理解为这等价于在方法中增加了一句：

```
model.addAttribute("message", message);
```

`@ModelAttribute`还有其它用法，参考[关于ModelAttribute的Spring文档](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/bind/annotation/ModelAttribute.html)。

#### 在模板中访问Flash Attributes

因为对于GET请求，Flash Attributes已经被转化为Model中的数据，所以在模板中直接通过表达式访问它们即可，无需做任何新的配置：

```html
<div class="flash"
     th:unless="${#strings.isEmpty(message)}"
     th:text="${message}"></div>
```

对于GET请求来说，如果并非重定向而来，`message`必定为空，这时不会显示提示消息。

<br/>

### 10. 拦截器Interceptor

#### 使用Session

我们已经实现了用户管理和博客管理功能，但是没有做任何权限设置。权限相关的功能需要使用Session。参考[管理用户状态——Cookie与Session ](https://www.tianmaying.com/tutorial/cookie-and-session)。

解决权限问题的常用方法是：

1. 用户登录时将用户信息存储在Session中
2. 在需要权限验证的页面（或者其对应的Controller），从Session中读取用户信息，判断用户是否有操作权限

在Controller方法中使用Session极其简单，只需要在Controller方法中声明一个`HttpSession`参数即可：

```java
@GetMapping(value = "/create")
public String showCreatePage(Model model, HttpSession session) {
    // Session中没有设置用户信息，则表示用户还没有登录
    if (session.getAttribute("CURRENT_USER") == null) {
        return "redirect:/";
    }

    model.addAttribute("post", new Post());
    return "create";
}
```

如果希望使用`HttpRequest`和`HttpResponse`等Servlet原生对象，也直接在方法中声明参数即可，Spring MVC会根据请求生成相应的实例，只需在方法实现中直接使用即可。

但是，当系统复杂、页面增多，大量页面需要做类似的权限判断时，在每一个Controller方法中都加上类似的Session判断，显然会产生大量的重复代码。这时就需要Spring MVC的拦截器（Interceptor）。

#### 拦截器的作用

Spring MVC框架中的Interceptor，与Servlet API中的Filter十分类似，用于对Web请求进行预处理/后处理。通常情况下这些预处理/后处理逻辑是通用的，可以被应用于所有或多个Web请求，例如：

- 记录Web请求相关日志，可以用于做一些信息监控、统计、分析
- 检查Web请求访问权限，例如发现用户没有登录后，重定向到登录页面
- 打开/关闭数据库连接——预处理时打开，后处理关闭，可以避免在所有业务方法中都编写类似代码，也不会忘记关闭数据库连接

#### HandlerInterceptor接口

Spring MVC中拦截器是实现了`HandlerInterceptor`接口的Bean：

```java
public interface HandlerInterceptor {
    boolean preHandle(HttpServletRequest request, 
                      HttpServletResponse response, 
                      Object handler) throws Exception;

    void postHandle(HttpServletRequest request, 
                    HttpServletResponse response, 
                    Object handler, ModelAndView modelAndView) throws Exception;

    void afterCompletion(HttpServletRequest request, 
                         HttpServletResponse response, 
                         Object handler, Exception ex) throws Exception;
}
```

- `preHandle()`：预处理回调方法，若方法返回值为`true`，请求继续（调用下一个拦截器或处理器方法）；若方法返回值为`false`，请求处理流程中断，不会继续调用其他的拦截器或处理器方法，此时需要通过`response`产生响应；
- `postHandle()`：后处理回调方法，实现处理器的后处理（但在渲染视图之前），此时可以通过`ModelAndView`对模型数据进行处理或对视图进行处理
- `afterCompletion()`：整个请求处理完毕回调方法，即在视图渲染完毕时调用

`HandlerInterceptor`有三个方法需要实现，但大部分时候可能只需要实现其中的一个方法，`HandlerInterceptorAdapter`是一个实现了`HandlerInterceptor`的抽象类，它的三个实现方法都为空实现（或者返回`true`），继承该抽象类后可以仅仅实现其中的一个方法：

```java
public class Interceptor extends HandlerInterceptorAdapter {

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        // 在controller方法调用前打印信息
        System.out.println("This is interceptor.");
        // 返回true，将强求继续传递（传递到下一个拦截器，没有其它拦截器了，则传递给Controller）
        return true;
    }
}
```

#### 配置Interceptor

定义`HandlerInterceptor`后，需要创建`WebMvcConfigurerAdapter`在MVC配置中将它们应用于特定的URL中。一般一个拦截器都是拦截特定的某一部分请求，这些请求通过URL模型来指定。

下面是一个配置的例子：

```java
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LocaleInterceptor());
        registry.addInterceptor(new ThemeInterceptor()).addPathPatterns("/**").excludePathPatterns("/admin/**");
        registry.addInterceptor(new SecurityInterceptor()).addPathPatterns("/secure/*");
    }
}
```

#### 用户登录优化

之前的过程中，如果我们访问创建新文章页面，那么一旦发现Session中`CURRENT_USER`属性不存在就回直接跳转回首页。其实这个过程可以做两个优化

1. 如果`CURRENT_USER`属性不存在，就跳转到登录页面，登录成功后再跳转回来
2. 将1中的逻辑应用于所有只有登录才能访问的页面

##### Interceptor实现检查逻辑

```java
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        // 如果用户已经登录了，就会在Session中以"CURRENT_USER"属性保存当前用户对应的User对象
        if (request.getSession().getAttribute("CURRENT_USER") != null) {
            return true;
        }

        response.sendRedirect("/login?next=".concat(request.getRequestURI()));
        return false;
    }
}
```

为了实现跳转登录页面登录成功后能够返回当前页面，在`Interceptor`中将当前URL作为`/login`的参数`next`，这里调用了`request.getRequestURL()`方法获取当前请求的URL。

##### LoginController

假设我们通过`LoginController`来处理登陆，简化的示例代码如下：

```java
@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String loginPage(@RequestParam("next") Optional<String> next) {
        return "login";
    }

    @PostMapping
    public String login(@RequestParam("next") Optional<String> next, User user, HttpSession session) {
        // Get User instance
        session.setAttribute("CURRENT_USER", user);
        return "redirect:".concat(next.orElse("/"));
    }
}
```

在登录的POST方法中，除了将Session中放入user对象外，跳转到`next`以便回到登录前的页面。

`Optional<String> next`表示`next`是一个可选参数，`return "redirect:".concat(next.orElse("/"));`表示跳转到`next`表示的URL，如果`next`为`null`，则跳转到首页`/`。

##### 配置Interceptor

```java
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public voidaddInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/posts/create");
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/admin/**");
    }
}
```

可以让拦截器拦截一个特定的URL。也可以使用URL模式来配置拦截器，比如`registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/admin/**");`表示所有以`/admin/`开头的URL都需要经过`LoginInterceptor`处理。

一般情况下会使用URL模式来配置拦截器，因为只需要一份代码就能给多个Controller实现额外的逻辑，这正是拦截器的优势所在。

<br/>

### 11. 处理PUT和DELETE请求

#### REST风格的URL

HTTP协议里面，四个表示操作方式的动词：GET、POST、PUT、DELETE，它们分别对应四种基本的操作，GET用来获取资源，POST用来新建资源，PUT用来更新资源，DELETE用来删除资源。我们完全可以设计一套URL用来表示博客文章（认为是一种资源）的增删改查操作（在前面已经提及）：

- `GET /blogs`，获取所有博客文章
- `POST /blogs`，创建一篇新博客文章
- `GET /blogs/{id}`，获取一篇博客文章
- `PUT /blogs/{id}`，更新一篇博客文章
- `DELETE /blogs/{id}`，删除一篇文章

现在我们已经了解如何发送`GET`和`POST`请求到服务器，浏览器的`form`表单只支持`GET`和`POST`请求，而`DELETE`和`PUT`请求并不支持。为了解决这个问题，Spring MVC提供了一个`HiddenHttpMethodFilter`，可以将带有`_method`参数的`POST`请求转换为`PUT`或`DELETE`请求。使用Spring Boot时，它已经被默认配置生效了。

#### 编写HTML表单

通过HTML表单我们只能发送`GET`和`POST`请求

```html
<!-- 发送put请求 -->
<form action="/blogs/1" method="post">
    <input type="hidden" name="_method" value="put" >
    <input type="submit" value="更新">
</form>
    
<!-- 发送delete请求 -->
<form action="/blogs/1" method="post">
    <input type="hidden" name="_method" value="delete" >
    <input type="submit" value="删除">
</form>
```

这样的`POST`请求，经过`HiddenHttpMethodFilter`的处理后，就会转化为`PUT`和`DELETE`请求被Spring MVC处理，匹配`@PutMapping`和`@DeleteMapping`。

需要注意的是，`_method`字段只是用于提示Spring MVC框架我们发送的请求应该作为什么方法来处理，所以将其设为`hidden`，让用户不可见

<br/>

### 12. 文件上传

#### multipart 编码

处理文件的表单和普通表单的唯一区别在于设置`enctype`——multipart编码方式则需要设置`enctype`为`multipart/form-data`。

```html
<form method="post" enctype="multipart/form-data">
    <input type="text" name="title" value="value">
    <input type="file" name="avatar">
    <input type="submit">
</form>
```

该表单将会显示为一个文本框、一个文件按钮、一个提交按钮。然后我们选择一个文件：`chrome.png`，点击表单提交后产生的请求可能是这样的：

请求头：

```
POST http://www.example.com HTTP/1.1
Content-Type:multipart/form-data; boundary=----WebKitFormBoundaryrGKCBY7qhFd3TrwA
```

请求体：

```
------WebKitFormBoundaryrGKCBY7qhFd3TrwA
Content-Disposition: form-data; name="title"

tianmaying
------WebKitFormBoundaryrGKCBY7qhFd3TrwA
Content-Disposition: form-data; name="avatar"; filename="chrome.png"
Content-Type: image/png

 ... content of chrome.png ...
------WebKitFormBoundaryrGKCBY7qhFd3TrwA--
```

这便是一个multipart编码的表单。`Content-Type`中还包含了`boundary`的定义，它用来分隔请求体中的每个字段。正是这一机制，使得请求体中可以包含二进制文件（当然文件中不能包含`boundary`）。文件上传正是利用这种机制来完成的。

如果不设置`<form>`的`enctype`编码，同样可以在表单中设置`type=file`类型的输入框，但是请求体和传统的表单一样，这样服务器程序无法获取真正的文件内容。

#### 配置Spring支持文件上传

上述描述是在客户端Web表单层面对文件上传的支持。在服务端，为了支持文件上传还需要进行一些配置。

对于表单中的文本信息输入，我们可以通过`@RequestParam`注解获取。对于上传的二进制文件（文本文件同样会转化为`byte[]`进行传输），就需要借助Spring提供的`MultipartFile`类来获取了：

```java
@Controller
public class FileUploadController {

    @PostMapping("/upload")
    @ResponseBody
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        byte[] bytes = file.getBytes();

        return "file uploaded successfully."
    }
}
```

通过`MultipartFile`的`getBytes()`方法即可以得到上传的文件内容（`<form>`中定义了一个`type="file"`的，在这里我们可以将它保存到本地磁盘。另外，在默认的情况下Spring仅仅支持大小为128KB的文件，为了调整它，我们可以修改Spring的配置文件`src/main/resources/application.properties`：

```
multipart.maxFileSize: 128KB
multipart.maxRequestSize: 128KB
```

修改上述数值即可完成配置。

#### HTML表单

HTML中支持文件上传的表单元素仍然是`<input>`，只不过它的类型是`file`：

```html
<html>
<body>
  <form method="POST" enctype="multipart/form-data" action="/upload">
    File to upload: <input type="file" name="file"><br />
    Name: <input type="text" name="name"><br /> <br />
    <input type="submit" value="Upload"> Press here to upload the file!
  </form>
</body>
</html>
```

#### 修改头像

把通过表单上传的文件存储在文件系统的指定的位置，同时每一个用户上传的文件需要区分，可以用用户唯一的标识符`id`作为文件系统的名字，例如现在有两个用户，我们将他们的头像存储在`/data/avatars/`目录下：

- 用户`id=1`，对应头像文件：`/data/avatar/1.jpg`
- 用户`id=2`，对应头像文件：`/data/avatar/2.jpg`

##### 显示用户头像

先假设`/data/avatar/`目录下已经存在所有的用户头像信息，并且是以`{id}.jpg`的形式命名。下一步工作就是使得这些图片能够通过url的形式访问，例如`http://localhost:8080/avatar/1.jpg`能够获取头像的图片，那么在渲染博客页面时，侧边栏的`<img>`标签就应该由一个静态文件变成动态渲染：

```html
<img th:src="@{'/avatar/'+${user.id}+'.jpg'}">
```

同时，还需要通过Spring MVC处理`/avatar/{id}.jpg`这样的请求，因为会根据不同的`id`返回不同的图片内容。之前我们已经学习过利用`@ResponseBody`注解可以返回文本内容（例如字符串、JSON），但是图片文件是二进制格式存储的，需要借助类`ResponseEntity`返回：

```java
@Controller
public class FileUploadController {
  
    public static final String ROOT = "/data/avatar/";

	private final ResourceLoader resourceLoader;

	@Autowired
	public FileUploadController(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
  
    @GetMapping(value = "/avatar/{filename:.+}")
    @ResponseBody
    public ResponseEntity<?> getFile(@PathVariable String filename) {
        try {
            return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(ROOT, filename).toString()));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
```

`ResponseEntity.ok()`和`ResponseEntity.notFound()`会返回相应的HTTP状态码（以及Body）。这里我们利用`ResourceLoader`类加载指定`Path`的文件并将其作为HTTP响应的Body返回，`ResourceLoader`由Spring MVC提供，我们无需创建，只需要用`@Autowired`注入即可。

##### 处理上传头像请求

```java
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
public class FileUploadController {

    @PostMapping("/avatar")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        String filename = currentUser.getId() + ".jpg";
        // 保存图片
        Files.copy(file.getInputStream(), Paths.get(ROOT, filename));
        return "";
    }
}
```

Spring MVC提供的`MultipartFile`类能够非常容易的获取上传文件的内容，我们需要根据当前用户的`id`生成指定的头像文件存储路径，再利用`java.nio.file.Files`提供的`copy`方法，将一个`InputStream`直接存储到`Path`中。

<br/>

### 13. 异常处理

#### 异常

在程序运行过程中，难免会出现一些异常——空指针异常、IO异常等等。在Spring MVC中，框架会创建一个`HandlerExceptionResolver`的`Bean`用来处理异常。出现异常并不可怕，重要的是我们如何处理异常。

在博客应用中，访问一个不存在的url——`http://localhost:8080/abcedfg`，会出现如下提示：

```
Whitelabel Error Page

This application has no explicit mapping for /error, so you are seeing this as a fallback.

Fri Aug 21 20:02:01 CST 2015
There was an unexpected error (type=Not Found, status=404).
No message available
```

本质上这是Spring MVC在对URL进行分发时，发现没有任何处理方法匹配这个URL，所以抛出了一个`NoSuchRequestHandlingMethodException`异常，而默认的异常处理机制则显示了这个错误页面。

#### 配置错误页面

##### 静态错误页面

在出现404错误的时候简单的返回一个静态HTML文档。有了错误页面，还需要建立`404 Not Found`错误和页面的映射关系，我们用Spring Boot开发Spring MVC应用，这一步配置非常的简单，在`src/main/resources/static`目录下放置一个`error`目录，里头存放各种错误码对应的页面：

```
src/
 +- main/
     +- java/
     |   + <source code>
     +- resources/
         +- static/
             +- error/
             |   +- 404.html
             +- <other public assets>
```

框架会帮助自动建立`{Status Code} => {Status Code}.html`的映射，那么在目录结构完成后，网站的所有404错误，都会将这个错误页面返回到浏览器。

##### 动态错误页面

访问不同的URL（为了触发404错误，不存在的URL），虽然HTTP状态码都是404，但是返回页面的内容是不同的——`url`部分需要动态化，可以借助模板引擎：

```html
<div class="error-details"     th:text="${#httpServletRequest.getAttribute('javax.servlet.error.request_uri')}">
          Sorry, an error has occured, Requested page not found!
</div>
```

使用Thymeleaf可以访问Context里的数据，并渲染相应的错误页面，同样可以这样组织模板目录：

```
src/
 +- main/
     +- java/
     |   + <source code>
     +- resources/
         +- templates/
             +- error/
             |   +- 404.html
             +- <other templates>
```

同理可以添加其他HTTP错误状态码的返回页面，通常还需要添加`403 Forbidden`, `500 Internal Server Error`。

#### 异常处理

除了简单的HTTP状态码配置，Spring MVC框架提供了多种机制用来处理异常，初次接触可能会对他们用法以及适用的场景感到困惑。现在在我们的博客应用中的一些场景来解释这些异常处理的机制。

在之前的学习中，我们设计了文章相关的URL资源：

- 获取文章列表：`GET /blogs/`
- 添加一篇文章：`POST /blogs/`
- 获取一篇文章：`GET /blogs/{id}`
- 更新一篇文章：`PUT /blogs/{id}`
- 删除一篇文章：`DELETE /blogs/{id}`

这是非常标准的复合RESTful风格的URL设计，在Spring MVC实现的应用过程中，相应也会有5个对应的用`@RequestMapping`注解的方法来处理相应的URL请求。在处理某一篇文章的请求中（获取、更新、删除），无疑需要做这样一个判断——请求URL中的文章id是否在于系统中，如果不存在需要返回`404 Not Found`。

#### 使用HTTP状态码

在默认情况下，Spring MVC处理Web请求时如果发现存在没有应用代码捕获的异常，那么会返回HTTP 500（Internal Server Error）错误。但是如果该异常是我们自己定义的并且使用`@ResponseStatus`注解进行修饰，那么Spring MVC则会返回指定的HTTP状态码：

```java
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No Such Blog")//404 Not Found
public class BlogNotFoundException extends RuntimeException {
    public BlogNotFoundException(String message) {
        super(message);
    }
}
```

在`Controller`中可以这样使用它：

```java
@GetMapping("/blogs/{id}")
public String showBlog(@PathVariable("id") long id, Model model) {
    Blog blog = blogService.get(id);
    if (blog == null) throw new BlogNotFoundException("blog not found");
    model.addAttribute("blog", blog);
    return "item";
}
```

这样如果我们访问了一个不存在的文章，那么Spring MVC会根据抛出的`BlogNotFoundException`上的注解值返回一个`404 Not Found`给浏览器。

##### 实践

除了获取一篇文章的请求，还有更新和删除一篇文章的方法中都需要判断文章id是否存在。在每一个方法中都加上`if (blog == null) throw new BlogNotFoundException("blog not found");`是一种解决方案，但如果有10个、20个包含`/blogs/{id}`的方法，虽然只有一行代码但让他们重复10次、20次也是非常不优雅的。

为了解决这个问题，可以将这个逻辑放在Service中实现：

```java
public Blog findBlog(long id) {
    Blog blog = blogRepository.findById(id);
    if (blog == null) throw new BlogNotFoundException("blog not found");
    return blog;
}
```

这样在所有的`Controller`方法中，只需要正常活取文章即可，所有的异常处理都交给了Spring MVC。

#### 在Controller中处理异常

`Controller`中的方法除了可以用于处理Web请求，还能够用于处理异常处理——为它们加上`@ExceptionHandler`即可：

```java
@Controller
public class ExceptionHandlingController {

    // @RequestHandler methods
    ...
  
    // Exception handling methods
  
    // Convert a predefined exception to an HTTP Status code
    @ResponseStatus(value=HttpStatus.CONFLICT, reason="Data integrity violation")  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public void conflict() {
      // Nothing to do
    }
  
    // Specify the name of a specific view that will be used to display the error:
    @ExceptionHandler({SQLException.class,DataAccessException.class})
    public String databaseError() {
        // Nothing to do.  Returns the logical view name of an error page, passed to
        // the view-resolver(s) in usual way.
        // Note that the exception is _not_ available to this view (it is not added to
        // the model) but see "Extending ExceptionHandlerExceptionResolver" below.
        return "databaseError";
    }

    // Total control - setup a model and return the view name yourself. Or consider
    // subclassing ExceptionHandlerExceptionResolver (see below).
    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(HttpServletRequest req, Exception exception) {
        logger.error("Request: " + req.getRequestURL() + " raised " + exception);

        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", exception);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("error");
        return mav;
    }
}
```

首先需要明确的一点是，在`Controller`方法中的`@ExceptionHandler`方法只能够处理同一个`Controller`中抛出的异常。这些方法上同时也可以继续使用`@ResponseStatus`注解用于返回指定的HTTP状态码，但同时还能够支持更加丰富的异常处理：

- 渲染特定的视图页面
- 使用`ModelAndView`返回更多的业务信息

大多数网站都会使用一个特定的页面来响应这些异常，而不是直接返回一个HTTP状态码或者显示Java异常调用栈。当然异常信息对于开发人员是非常有用的，如果想要在视图中直接看到它们可以这样渲染模板（以JSP为例）：

```html
<h1>Error Page</h1>
<p>Application has encountered an error. Please contact support on ...</p>

<!--
Failed URL: ${url}
Exception:  ${exception.message}
<c:forEach items="${exception.stackTrace}" var="ste">    ${ste} 
</c:forEach>
-->
```

#### 全局异常处理

[@ControllerAdvice](https://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#mvc-ann-controller-advice)提供了和上一节一样的异常处理能力，但是可以被应用于Spring应用上下文中的所有`@Controller`：

```java
@ControllerAdvice
class GlobalControllerExceptionHandler {
    @ResponseStatus(HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public void handleConflict() {
        // Nothing to do
    }
}
```

Spring MVC默认对于没有捕获也没有被`@ResponseStatus`以及`@ExceptionHandler`声明的异常，会直接返回500，这显然并不友好，可以在`@ControllerAdvice`中对其进行处理（例如返回一个友好的错误页面，引导用户返回正确的位置或者提交错误信息）：

```java
@ControllerAdvice
class GlobalDefaultExceptionHandler {
    public static final String DEFAULT_ERROR_VIEW = "error";

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it - like the OrderNotFoundException example
        // at the start of this post.
        // AnnotationUtils is a Spring Framework utility class.
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
            throw e;

        // Otherwise setup and send the user to a default error-view.
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;
    }
}
```

<br/>

### 14. @ResponseBody与@RequestBody

#### AJAX

在我们之前完成的博客页面里，我们都是通过url（或者是页面中的锚点）来访问新页面，每次访问新页面，都会经历如下过程：

- 浏览器根据URl向指定的主机发送HTTP请求
- 主机接收请求，经过处理后将结果（现在都是**完整**的HTML文档）返回道浏览器
- 浏览器收到返回的结果，将其**重新**渲染，显示一个新页面，旧页面就消失了

但是每一次完全刷新整个页面也是有代价的——从服务器上获得的完整页面内容会很大，传输过程需要时间，浏览器渲染也需要时间，这就会对用户体验造成巨大的影响，例如登录页面中，表单提交后，相当于浏览器发送了一个POST请求到服务器，服务器处理后发现用户名密码不匹配随之重新渲染登录页面（把错误信息也加入到页面中）并返回。但是实际上，这两个页面的差异只在于这一个消息，那么为什么不能够在登录表单的页面里，直接修改DOM来达到显示错误消息的目的呢？

AJAX（Asynchronous JavaScript and XML）表示的是异步JavaScript和XML，它并不是新的技术，而是为了解决：

- 不重新加载页面便能向服务器发送HTTP请求
- 解析XML格式的文档

最初，AJAX采用XML作为浏览器和服务端之间的数据格式，但是现在JSON由于其轻便小巧以及JavaScript的天然支持等优势逐渐成为主流。

#### 在JavaScript中使用AJAX

我们以jQuery为例，演示如何在代码中使用AJAX技术向服务器发送请求：

```js
var data = {username: "<get user name from DOM here>", password: "<get password from DOM here>"};
$.ajax({
  url: "login",
  method: "POST",
  data: data
});
```

如果服务器返回了成功的内容（HTTP状态码2XX），那么可以通过`.done()`函数来设置回调函数：

```js
var data = {username: "<get user name from DOM here>", password: "<get password from DOM here>"};
$.ajax({
  url: "login",
  method: "POST",
  data: data
}).done(function(msg){
  // Process Data and re-render DOM
});
```

这里，`data`是一个JavaScript对象，jQuery会自动将其序列化为JSON格式并发送HTTP请求。回调函数的参数msg就是服务器返回的消息，例如：

```json
{
  "status": "fail",
  "message": "登录失败，用户名或密码不正确"
}
```

那么只需要在回调函数中，将`msg.message`的值放在指定的位置（例如表单的下方）。

#### Spring MVC处理AJAX请求

除了在浏览器端添加AJAX代码，服务器端的处理逻辑也不同：

1. 浏览器POST的数据不再是表单，而是JSON字符串
2. 需要返回的数据不再是HTML，同样也是JSON字符串

当然我们可以读取请求的内容，用JSON库（例如[gson](https://github.com/google/gson)）将所需要的信息解析出来；也可以去手动生成JSON字符串，但是Spring MVC为我们提供了两个非常好用的注解`@RequestBody`和`ResponseBody`来完成这些操作：

```java
@Controller
public class LoginController {
    
    @PostMapping("/login")
    @ResponseBody
    public LoginStatus loginByAJAX(@RequestBody LoginForm form) {
        LoginStatus status = new LoginStatus();
        if (login(form.username, form.password)) {
            status.status = "success";
            status.message = "登录成功";
        } else {
            status.status = "fail";
            status.message = "登录失败，用户名或密码不正确";
        }
        return status;
    }
  
    class LoginForm {
        public String username;
        public String password;
    }
    class LoginStatus {
        public String status;
        public String message;
    }
}
```

- 当`@PostMapping`注解的处理函数的参数被`@RequestBody`注解时，Spring MVC在默认情况下可以推测HTTP请求Body的类型（例如这里是JSON）然后解析JSON（用到了Jackson库）并转化为一个`LoginForm`对象。
- 当`@PostMapping`注解的处理函数的返回值被`@ResponseBody`注解时，Spring MVC在默认情况下会将其JSON序列化并返回到浏览器

值得一提的是，`@RestController`正是为了简化代码，而将`@Controller`和`@ResponseBody`注解合并后得到的新注解，以后如果需要直接返回数据而不是渲染模板，推荐直接使用注解`@RestController`。

<br/>