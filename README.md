# Failure-in-Action

> **Fail Fast, Fail Safe.**  
> 一个基于 Spring Boot 3 的实战演示项目，核心展示如何通过“快速失败”（Fail-Fast）设计原则构建健壮的业务系统，并优雅地处理参数校验与全局异常。

## 📚 项目简介

在复杂的业务系统中，参数校验和异常处理往往占据了大量样板代码。本项目通过集成自定义的 `fail-fast-spring-boot-starter`，演示了以下最佳实践：

- **统一参数校验**：摒弃散落在各处的 `if-else` 判断，采用声明式或集中式的校验逻辑。
- **全局异常处理**：统一捕获业务异常，返回标准化的 JSON 响应。
- **优雅的错误码管理**：通过枚举定义错误码，实现错误信息的统一维护。
- **安全与脱敏**：用户密码加盐存储，敏感信息自动脱敏返回。

## 🛠️ 技术栈

- **核心框架**: [Spring Boot 3.5.7](https://spring.io/projects/spring-boot) (Java 17)
- **ORM 框架**: [MyBatis Plus 3.5.5](https://baomidou.com/)
- **数据库**: MySQL 8.0
- **校验框架**: `fail-fast-spring-boot-starter` (自定义 Starter) + Hibernate Validator
- **工具库**: Lombok, Spring AOP

## ✨ 核心特性

### 1. 多样化的 Fail-Fast 校验模式

本项目演示了三种不同的校验实现方式，满足不同场景需求：

- **集中式校验 (TypedValidator)**:  
  在 [CustomValidator.java](src/main/java/com/chao/failure_in_action/validator/CustomValidator.java) 中统一注册 DTO 的校验逻辑，支持复杂的业务校验（如数据库查重）。

  ```java
  register(UserRegisterDTO.class, (dto, ctx) -> {
      Failure.with(ctx)
          .notBlank(dto.getUsername(), UserCode.USERNAME_BLANK)
          .email(dto.getEmail(), UserCode.EMAIL_INVALID)
          .verify();
      // ... 数据库查重逻辑
  });
  ```

- **注解式校验**:  
  在 Controller 或 Service 方法上使用 `@Validate` 注解自动触发校验。

- **流式 API**:  
  在业务代码中使用 `Failure.begin()...fail()` 进行链式调用，代码清晰易读。

### 2. 全局异常处理

通过继承 `DefaultExceptionHandler` 并配合 `@RestControllerAdvice`，实现零配置的异常捕获。

- [GlobalExceptionHandler.java](src/main/java/com/chao/failure_in_action/exception/GlobalExceptionHandler.java) 自动处理参数校验异常和业务异常，返回统一格式：
  ```json
  {
    "code": 40001,
    "message": "用户名不能为空",
    "data": null
  }
  ```

### 3. 用户管理功能

- **注册**: 支持用户名/邮箱/手机号唯一性校验，密码 MD5 加盐。
- **登录**: 基于 Session 的状态管理，返回脱敏后的用户信息。
- **查询**: 管理员可根据昵称搜索用户。
- **删除**: 逻辑删除机制（`is_deleted`），数据更安全。

## 🚀 快速开始

### 环境要求
- JDK 17+
- Maven 3.6+
- MySQL 8.0+

### 1. 初始化数据库

在 MySQL 中执行 [sql/db.sql](sql/db.sql) 脚本，创建 `user` 表。

```sql
create database failure_action;
use failure_action;
source sql/db.sql;
```

### 2. 配置数据库连接

修改 `src/main/resources/application.yml`，配置您的数据库账号密码：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/failure_action?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
```

### 3. 运行项目

运行 `FailureDemoApplication.java` 的 `main` 方法启动服务。

### 4. 接口测试

可以使用 Postman 或 cURL 进行测试。

**用户注册示例**:
```bash
curl -X POST http://localhost:8080/user/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "password123",
    "email": "admin@example.com",
    "nickname": "AdminUser"
  }'
```

## 📂 目录结构

```
src/main/java/com/chao/failure_in_action
├── constant       // 常量定义
├── controller     // 控制层，处理 HTTP 请求
├── exception      // 全局异常处理
├── mapper         // MyBatis Plus Mapper 接口
├── model          // 数据模型
│   ├── dto        // 数据传输对象 (Request)
│   ├── entity     // 数据库实体
│   └── enums      // 错误码枚举
├── service        // 业务逻辑层
└── validator      // 校验器实现
```

## 📄 License

MIT License
