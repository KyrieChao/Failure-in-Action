# Failure in Action

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-green.svg)](https://spring.io/projects/spring-boot)
[![Failure](https://img.shields.io/badge/Failure-1.0.2-orange.svg)](https://github.com/KyrieChao/Failure)

> **Fail Fast, Fail Safe.**
>
> 一个基于 **Spring Boot 3 + Failure Framework** 的最佳实践演示项目。本项目展示了如何通过“声明式校验”与“函数式编程”思想，将复杂的业务校验逻辑从 Controller/Service 中剥离，构建高内聚、低耦合的健壮系统。

## 📚 场景痛点 vs 解决方案

| 传统痛点 | Failure 方案 |
| :--- | :--- |
| **代码臃肿**: 大量 `if (obj == null) throw ...` | **流式 API**: `Failure.with(ctx).notNull(obj).verify()` |
| **逻辑耦合**: 校验逻辑散落在业务代码中 | **策略分离**: 独立的 `FastValidator` 或 `TypedValidator` |
| **反馈延迟**: 等到入库时才报错 | **Fail-Fast**: 参数错误立即返回，阻断昂贵的数据库操作 |
| **维护困难**: 错误码散落，前端提示不一致 | **统一管理**: 强类型 `ResponseCode` 枚举与自动 I18n |

## 🛠️ 技术栈

*   **核心框架**: Spring Boot 3.2.0 (Java 17)
*   **校验框架**: `failure-spring-boot-starter` 1.0.2 (核心依赖)
*   **ORM**: MyBatis Plus 3.5.5
*   **数据库**: MySQL 8.0
*   **工具**: Lombok, Hutool

## ✨ 核心代码演示

### 📐 分层校验策略

我们推荐在不同层级采用不同的校验策略，以实现最佳的性能与代码组织：

| 层级 | 校验方式 | 注解组合 |
| :--- | :--- | :--- |
| **Controller** | JSR-303 字段校验（支持分组） | `@Validate(fast = false)` + `@Validated(Group.class)` |
| **Service** | 业务逻辑校验（数据库查重等） | `@Validate(value = CustomValidator.class, fast = false)` |

### 1. Controller 层：JSR-303 字段校验
使用原生注解（如 `@NotBlank`）配合 Group 分组，处理格式校验。通过 `@Validate(fast = false)` 确保一次性返回所有格式错误。

**DTO 定义：**
```java
@Data
public class UserDTO {
    @NotBlank(groups = Create.class, message = "用户名不能为空")
    private String username;

    @NotBlank(groups = Create.class, message = "密码不能为空")
    @Length(min = 6, groups = Create.class, message = "密码长度不能少于6位")
    private String password;

    // 定义分组接口
    public interface Create {}
    public interface Update {}
}
```

**Controller 实现：**
```java
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    // fast=false: 收集所有 JSR-303 错误（如用户名为空且密码过短）
    @Validate(fast = false)
    public Result<Void> register(@RequestBody @Validated(UserDTO.Create.class) UserDTO dto) {
        userService.register(dto);
        return Result.success();
    }
}
```

### 2. Service 层：业务逻辑校验
使用 `CustomValidator` 处理复杂的业务规则（如数据库查重、状态检查）。

**Service 实现：**
```java
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    // 指定校验器，fast=false: 收集所有业务错误
    @Validate(value = UserRegisterValidator.class, fast = false)
    public void register(UserDTO dto) {
        // 校验通过后执行业务逻辑
        User user = new User();
        BeanUtils.copyProperties(dto, user);
        userMapper.insert(user);
    }
}
```

**Validator 实现：**
```java
@Component
@RequiredArgsConstructor
public class UserRegisterValidator implements FastValidator<UserDTO> {

    private final UserMapper userMapper;

    @Override
    public void validate(UserDTO dto, ValidationContext context) {
        // 链式调用：检查用户名、邮箱、手机号是否已存在
        Failure.with(context)
                .isFalse(exists(User::getUsername, dto.getUsername()), UserCode.USERNAME_EXIST)
                .isFalse(exists(User::getEmail, dto.getEmail()), UserCode.EMAIL_EXIST)
                .verify();
    }

    private boolean exists(SFunction<User, ?> field, String value) {
        return userMapper.exists(new LambdaQueryWrapper<User>().eq(field, value));
    }
}
```

## 🚀 快速开始

### 第一步：引入依赖
在 `pom.xml` 中添加 Starter：

```xml
<dependency>
    <groupId>io.github.kyriechao</groupId>
    <artifactId>failure-spring-boot-starter</artifactId>
    <version>1.0.2</version>
</dependency>
```

### 第二步：定义错误码
实现 `ResponseCode` 接口，统一管理错误信息：

```java
@Getter
@AllArgsConstructor
public enum UserCode implements ResponseCode {
    USERNAME_BLANK(1001, "用户名不能为空"),
    PASSWORD_BLANK(1002, "密码不能为空"),
    USER_NOT_FOUND(1003, "用户不存在"),
    // ...
    ;

    private final int code;
    private final String message;
}
```

### 第三步：配置全局异常处理
继承 `FailFastExceptionHandler`，实现零配置异常捕获：

```java
@RestControllerAdvice
public class GlobalExceptionHandler extends FailFastExceptionHandler {
    // 框架会自动处理 BusinessException 和 ValidationException
    // 你可以在此覆盖方法以自定义响应格式
}
```

## 📂 项目结构

```text
src/main/java/com/chao/failure_in_action
├── config             // 全局配置
├── controller         // 控制层 (集成 @Validate)
├── exception          // 全局异常处理 (GlobalExceptionHandler)
├── model
│   ├── dto            // 数据传输对象
│   ├── entity         // 数据库实体
│   └── enums          // 错误码 (UserCode)
├── repository         // 数据库访问层
├── service            // 业务逻辑层
└── validator          // 校验层
    ├── GlobalValidator.java      // 集中式校验
    └── UserRegisterValidator.java // 独立校验器
```

## 🔗 相关资源

*   **框架源码**: [Failure Framework](https://github.com/KyrieChao/Failure)
*   **作者博客**: [Kyrie's Blog](https://kyriechao.github.io)

---
*Developed with ❤️ by Kyrie Chao*
