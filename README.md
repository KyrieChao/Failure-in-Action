# Failure-in-Action

> **Fail Fast, Fail Safe.**  
> 一个基于 Spring Boot 3.5.7 的实战演示项目，核心展示如何通过“快速失败”（Failure）设计原则构建健壮的业务系统，并优雅地处理参数校验与全局异常。

## 📚 项目简介

在复杂的业务系统中，参数校验和异常处理往往占据了大量样板代码。本项目通过集成自定义的 `fail-fast-spring-boot-starter`，演示了以下最佳实践：

- **统一参数校验**：采用 `TypedValidator` 模式，在 [CustomValidator.java](src/main/java/com/chao/failure_in_action/validator/CustomValidator.java) 中集中管理所有 DTO 的校验逻辑。
- **Fail-Fast 机制**：校验逻辑支持“快速失败”，基础格式校验不通过时立即返回，避免执行昂贵的数据库查重操作。
- **全局异常处理**：通过继承 `DefaultExceptionHandler`，零配置实现异常的统一捕获与标准化响应。
- **安全实践**：密码加盐哈希存储、敏感信息自动脱敏、基于 Session 的登录态管理。

## 🛠️ 技术栈

- **核心框架**: [Spring Boot 3.5.7](https://spring.io/projects/spring-boot) (Java 17)
- **ORM 框架**: [MyBatis Plus 3.5.5](https://baomidou.com/)
- **数据库**: MySQL 8.0
- **校验框架**: `fail-fast-spring-boot-starter 1.2.0` (自定义 Starter)
- **工具库**: Lombok, Spring AOP, Hutool (可选)

## ✨ 核心特性详解

### 1. 集中式校验 (TypedValidator)

本项目推荐使用 `TypedValidator` 模式，将校验逻辑与业务逻辑分离。在 `CustomValidator` 中：

```java
// 注册 UserRegisterDTO 的校验规则
register(UserRegisterDTO.class, (dto, ctx) -> {
    // 1. 基础格式校验（流式 API）
    Failure.with(ctx)
            .notBlank(dto.getUsername(), UserCode.USERNAME_BLANK)
            .email(dto.getEmail(), UserCode.EMAIL_INVALID)
            .verify();
    
    // 2. Fail-Fast: 如果基础校验失败，直接返回，不进行后续数据库查询
    if (ctx.isFailed()) {
        return;
    }
    
    // 3. 业务校验（数据库查重）
    checkDuplicate(dto, ctx);
});
```

## 🔌 API 接口列表

所有接口均位于 `UserController`，基础路径 `/api/user`（注：实际代码中为 `/user`，请以代码为准）：

| 接口名称 | HTTP 方法 | 路径 | 描述 | 权限 |
| :--- | :--- | :--- | :--- | :--- |
| **用户注册** | POST | `/user/register` | 用户注册，包含参数校验与查重 | 公开 |
| **用户登录** | POST | `/user/login` | 邮箱密码登录，返回脱敏用户信息 | 公开 |
| **用户注销** | POST | `/user/logout` | 清除 Session 登录态 | 需登录 |
| **获取当前用户** | GET | `/user/current` | 获取当前登录用户的详细信息（脱敏） | 需登录 |
| **搜索用户** | GET | `/user/search` | 根据用户名搜索用户列表 | **管理员** |
| **删除用户** | POST | `/user/delete` | 逻辑删除用户 | **管理员** |

## 🚀 快速开始

### 1. 环境准备
- JDK 17+
- Maven 3.6+
- MySQL 8.0+

### 2. 初始化数据库
执行 [sql/db.sql](sql/db.sql) 脚本创建数据库和表结构：

```sql
create database failure_action;
use failure_action;
-- 运行 sql/db.sql 中的建表语句
```

### 3. 修改配置
编辑 `src/main/resources/application.yml`，配置数据库连接：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/failure_action?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
```

### 4. 启动项目
运行 `FailureDemoApplication.java` 的 `main` 方法。

## 📂 项目结构

```
src/main/java/com/chao/failure_in_action
├── constant       // 常量定义 (UserConstant.java)
├── controller     // 控制层 (UserController.java)
├── exception      // 全局异常处理 (GlobalExceptionHandler.java)
├── mapper         // MyBatis Plus Mapper (UserMapper.java)
├── model          // 数据模型
│   ├── dto        // 请求参数对象 (UserRegisterDTO, UserLoginDTO...)
│   ├── entity     // 数据库实体 (User.java)
│   └── enums      // 响应状态码 (UserCode.java)
├── service        // 业务逻辑层 (UserServiceImpl.java)
└── validator      // 校验逻辑 (CustomValidator.java)
```


## 📄 License

MIT License
