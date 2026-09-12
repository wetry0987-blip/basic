# Java 中级开发工程师面试题参考答案

> 岗位：北京华胜天成 Java 中级开发工程师  
> 关键词：Java、JVM、SpringBoot、SpringCloud、MySQL、PostgreSQL、Redis、Elasticsearch、Linux

---

## 一、Java 基础与集合

### 1. HashMap 的 put 流程，1.7 和 1.8 有什么差异？

- 计算 key 的 hash 值，定位到数组下标；
- 数组位置为空则直接插入；不为空则遍历链表/红黑树，key 存在则覆盖，不存在则尾插；
- 链表长度 ≥ 8 且数组长度 ≥ 64 时转红黑树，≤ 6 时退化为链表；
- 扩容阈值默认 0.75，扩容时 1.7 头插法可能产生循环链表，1.8 改为尾插法避免并发下死循环。

### 2. ConcurrentHashMap 怎么保证线程安全？

- JDK 1.7：分段锁 Segment，每段独立加锁，默认 16 段；
- JDK 1.8：取消 Segment，采用 CAS + synchronized 锁住链表头节点，读操作基本无锁，使用 volatile 保证可见性。

### 3. ArrayList 和 LinkedList 的使用场景和扩容机制

- ArrayList：基于数组，随机访问 O(1)，尾部插入快，中间插入慢；扩容为原来的 1.5 倍；
- LinkedList：基于双向链表，插入删除 O(1)，随机访问 O(n)，内存开销大；
- 查询多用 ArrayList，频繁增删用 LinkedList。

### 4. String、StringBuilder、StringBuffer 的区别

- String 不可变，每次修改生成新对象；
- StringBuilder 可变、线程不安全、效率高；
- StringBuffer 可变、方法加 synchronized、线程安全但性能略低。

### 5. 为什么重写 equals 必须重写 hashCode？

- equals 相等时 hashCode 必须相等，这是 Java 对象的通用契约；
- 如果只重写 equals 不重写 hashCode，HashMap/HashSet 等基于哈希的容器会误判为不同对象。

### 6. Java 异常体系

- Throwable 分 Error 和 Exception；
- Exception 分 checked（编译期必须处理）和 unchecked（RuntimeException，运行期异常）；
- 常见：NullPointerException、IllegalArgumentException、ClassNotFoundException、IOException。

### 7. 深拷贝和浅拷贝

- 浅拷贝：复制对象本身，引用类型字段指向同一对象；
- 深拷贝：递归复制引用对象，完全独立；
- 实现方式：实现 Cloneable 重写 clone、序列化反序列化、拷贝构造器。

### 8. 接口和抽象类的区别

- 接口：多实现，JDK 8 前只能有抽象方法，JDK 8 支持 default/static 方法；
- 抽象类：单继承，可以有成员变量、构造方法、普通方法；
- 抽象类描述"是什么"，接口描述"能做什么"。

---

## 二、JVM

### 1. JVM 内存模型

- 堆：存放对象实例，分新生代（Eden、Survivor）和老年代；
- 虚拟机栈：线程私有，存储栈帧、局部变量；
- 本地方法栈：Native 方法；
- 程序计数器：当前线程执行字节码行号；
- 方法区/元空间：类信息、常量、静态变量，JDK 8 后元空间使用本地内存。

### 2. 垃圾回收算法

- 标记-清除：简单但产生碎片；
- 复制：新生代常用，Eden → Survivor；
- 标记-整理：老年代常用，整理存活对象；
- CMS：老年代并发收集，低停顿；
- G1：分 Region 管理，可预测停顿时间，JDK 9 后默认。

### 3. 怎么判断对象是否可回收

- 引用计数法：循环引用无法解决；
- 可达性分析：从 GC Roots 出发不可达则回收；
- GC Roots：栈中局部变量、静态变量、常量、JNI 引用、类加载器等。

### 4. 什么情况下会出现 OOM，怎么排查

- 堆内存不足、元空间溢出、线程栈溢出、直接内存溢出；
- 排查：jmap 导出堆快照，MAT/VisualVM 分析大对象；jstat 看 GC 频率；
- 调优：调整堆大小、优化代码避免内存泄漏、选择合适 GC。

### 5. 线上 CPU 飙高怎么定位

- `top` 找到高 CPU 进程；
- `top -Hp pid` 找到具体线程；
- `jstack pid` 导出线程栈，线程 ID 转十六进制定位到代码行；
- 常见原因：死循环、频繁 Full GC、正则回溯、大量计算。

### 6. 类加载机制

- 加载 → 验证 → 准备 → 解析 → 初始化；
- 双亲委派：Bootstrap → Extension → Application 类加载器依次向上委托；
- 打破场景：Tomcat 隔离 Web 应用、SPI 机制、OSGi 模块化。

### 7. JVM 调优参数

- `-Xms -Xmx`：堆初始和最大值，建议设为相同减少扩缩容；
- `-Xmn`：新生代大小；
- `-XX:MetaspaceSize -XX:MaxMetaspaceSize`：元空间；
- `-XX:+UseG1GC -XX:MaxGCPauseMillis=200`：G1 及目标停顿。

---

## 三、并发编程

### 1. 线程池 7 大参数

- corePoolSize、maximumPoolSize、keepAliveTime、unit、workQueue、threadFactory、handler；
- 拒绝策略：AbortPolicy、CallerRunsPolicy、DiscardPolicy、DiscardOldestPolicy。

### 2. synchronized 和 ReentrantLock 的区别

- synchronized：JVM 层面，自动加解锁，JDK 6 后优化了偏向锁、轻量级锁；
- ReentrantLock：API 层面，可中断、可超时、可公平锁、支持多条件变量。

### 3. volatile 的作用

- 保证可见性：线程修改后其他线程立即可见；
- 禁止指令重排序；
- 不保证原子性，多线程自增仍需 synchronized/Atomic。

### 4. AQS 及工具类

- AQS 是抽象队列同步器，基于 CAS + 双向队列实现锁和同步器；
- CountDownLatch：等待多个线程完成；
- CyclicBarrier：线程互相等待到达屏障；
- Semaphore：控制同时访问的线程数量。

### 5. ThreadLocal 原理与内存泄漏

- 每个线程持有 ThreadLocalMap，key 是 ThreadLocal 弱引用，value 是强引用；
- 线程池场景下线程复用，若未 remove，value 无法回收导致内存泄漏；
- 用完及时 `remove()`。

### 6. 乐观锁和 CAS

- 乐观锁：假设不冲突，提交时检查版本号；
- CAS：Compare And Swap，依赖硬件指令，ABA 问题可用 AtomicStampedReference 解决。

### 7. 分布式锁实现方式

- Redis：SETNX + 过期时间 + 看门狗续期（Redisson）；
- ZooKeeper：临时顺序节点，监听前一个节点；
- 对比：Redis 性能高但可能丢锁，ZK 可靠性高但性能较低。

---

## 四、SpringBoot / SpringCloud

### 1. SpringBoot 自动装配原理

- `@SpringBootApplication` = `@Configuration + @EnableAutoConfiguration + @ComponentScan`；
- `EnableAutoConfiguration` 通过 `META-INF/spring.factories` 读取自动配置类；
- 按条件注解 `@ConditionalOnClass`、`@ConditionalOnProperty` 决定是否生效。

### 2. Spring IOC 和 AOP

- IOC：控制反转，容器管理对象生命周期和依赖注入；
- AOP：面向切面，基于动态代理（JDK 动态代理/CGLIB），用于日志、事务、权限等；
- Spring 事务基于 AOP，默认只对 RuntimeException 回滚。

### 3. Spring 事务传播机制

- REQUIRED（默认，加入当前事务）、REQUIRES_NEW（挂起当前事务，新建事务）、
- NESTED（嵌套事务）、SUPPORTS、MANDATORY、NOT_SUPPORTED、NEVER。

### 4. SpringCloud 常用组件

- 注册中心：Eureka/Nacos；
- 负载均衡：Ribbon/LoadBalancer；
- 服务调用：OpenFeign；
- 熔断降级：Hystrix/Sentinel；
- 网关：Gateway/Zuul；
- 配置中心：Config/Nacos Config；
- 链路追踪：Sleuth + Zipkin。

### 5. 服务注册发现过程

- 服务启动时向注册中心注册自身信息；
- 消费者从注册中心拉取服务列表；
- 注册中心与服务保持心跳，失败则剔除；
- 消费者通过负载均衡选择实例发起调用。

### 6. Nacos 和 Eureka 的区别

- Nacos 同时支持注册中心和配置中心；
- Nacos 支持 AP 和 CP 模式切换；
- Nacos 支持服务健康检查、权重、命名空间、分组；
- Eureka 只支持 AP，已进入维护模式。

### 7. OpenFeign 调用流程

- 定义接口加 `@FeignClient`，声明目标服务名和接口方法；
- 启动时生成动态代理；
- 调用时根据服务名从注册中心获取实例；
- 经 LoadBalancer 选择实例，构造 HTTP 请求发送。

### 8. 熔断降级

- 熔断：失败率达到阈值后，后续请求直接失败，过一段时间进入半开状态尝试恢复；
- 降级：服务异常时返回兜底逻辑；
- Sentinel 支持慢调用比例、异常比例、异常数等策略。

### 9. Gateway 路由过滤器

- 请求进入 Gateway，先经过 GlobalFilter，再匹配路由，最后执行 GatewayFilter；
- 可用于鉴权、限流、日志、重写路径、灰度发布。

### 10. SpringCloud Alibaba

- Nacos：注册发现 + 配置；
- Sentinel：熔断限流；
- Seata：分布式事务；
- RocketMQ：消息；
- Dubbo：RPC 调用。

---

## 五、MySQL

### 1. 索引底层数据结构

- InnoDB 使用 B+ 树；
- B+ 树叶子节点通过指针相连，支持范围查询；
- 所有叶子节点在同一层，查询稳定。

### 2. 聚簇索引和非聚簇索引

- 聚簇索引：数据行和索引在一起，InnoDB 主键索引就是聚簇索引；
- 非聚簇索引：叶子节点存主键值，需要回表查数据；
- 覆盖索引：查询字段都在索引中，无需回表。

### 3. 事务 ACID 和隔离级别

- A 原子性、C 一致性、I 隔离性、D 持久性；
- 隔离级别：读未提交、读已提交、可重复读、串行化；
- 默认可重复读，可能出现幻读，InnoDB 通过 MVCC + 间隙锁一定程度避免。

### 4. MVCC 机制

- 每行记录隐藏两个字段：事务 ID（trx_id）和回滚指针（roll_pointer）；
- undo log 形成版本链；
- read view 判断当前事务可见哪个版本；
- 实现非阻塞读，避免读写冲突。

### 5. InnoDB 锁

- 行锁：锁定单行；
- 间隙锁：锁定范围但不包括记录本身；
- 临键锁：行锁 + 间隙锁；
- 意向锁：表级锁，表示事务即将加行锁。

### 6. 慢 SQL 优化

- `explain` 看 type、key、rows、Extra；
- 加合适索引，避免索引失效；
- 避免 `select *`，减少回表；
- 大分页用覆盖索引 + 子查询；
- 优化 SQL 结构，避免隐式转换、函数操作索引列。

### 7. 分库分表

- 垂直拆分：按业务拆库/拆表；
- 水平拆分：按 ID/时间/hash 分散数据；
- 主键生成：雪花算法、UUID、数据库号段；
- 常用中间件：ShardingSphere、MyCat。

### 8. 主从复制和读写分离

- 主库写 binlog，从库 IO 线程拉取 binlog 写入 relay log，SQL 线程重放；
- 读写分离通过中间件或应用层数据源切换实现；
- 主从延迟可通过并行复制、半同步复制缓解。

---

## 六、PostgreSQL

### 1. MySQL 和 PostgreSQL 的主要区别

- PG 功能更丰富：支持 JSON/JSONB、数组、窗口函数、CTE、全文检索、GIS；
- PG 严格遵循 SQL 标准；
- PG 并发控制采用 MVCC，写不阻塞读；
- MySQL 生态更广，OLTP 场景应用更多。

### 2. PG 的 MVCC 实现

- 每行记录有 xmin（插入事务 ID）和 xmax（删除事务 ID）；
- 事务通过比较自身 ID 与 xmin/xmax 判断可见性；
- 更新操作实际插入新行，旧行由 vacuum 清理。

### 3. PG 主从复制

- 流复制：主库 WAL 记录实时发送给从库；
- 支持同步和异步复制；
- 可通过 pg_basebackup 搭建从库。

### 4. 慢日志分析

- 开启 `log_min_duration_statement` 记录慢 SQL；
- 使用 `pg_stat_statements` 扩展统计；
- 结合 `EXPLAIN ANALYZE` 分析执行计划。

### 5. PG 索引类型

- B-tree：默认，适合等值和范围查询；
- Hash：等值查询；
- GIN：适合数组、JSONB、全文检索；
- GiST：适合地理空间数据。

### 6. 分区表设计

- 范围分区：按时间/ID 区间；
- 列表分区：按固定值列表；
- 哈希分区：按哈希分散。

---

## 七、Redis

### 1. Redis 数据类型及场景

- String：缓存、计数器、分布式锁；
- Hash：对象存储；
- List：队列、栈、消息流；
- Set：去重、交集并集；
- ZSet：排行榜、延时队列；
- Bitmap/HyperLogLog/Geo：位图、UV 统计、地理位置。

### 2. 缓存三问题

- 穿透：查询不存在数据，绕过缓存打到 DB；解决：布隆过滤器、缓存空值；
- 击穿：热点 key 过期瞬间大量请求打 DB；解决：互斥锁、逻辑过期；
- 雪崩：大量 key 同时过期；解决：过期时间加随机值、多级缓存、限流。

### 3. RDB 和 AOF

- RDB：定时快照，恢复快，可能丢数据；
- AOF：记录写命令，数据更安全，文件大；
- 混合持久化：RDB 全量 + AOF 增量，Redis 4.0 后支持。

### 4. 哨兵和集群

- 哨兵：监控主从、自动故障转移、通知客户端；
- 集群：数据分片到 16384 个槽，支持水平扩展；
- 集群模式通过 gossip 协议通信。

### 5. Redis 为什么快

- 纯内存操作；
- 单线程避免上下文切换和锁竞争；
- IO 多路复用；
- 高效数据结构。

### 6. 分布式锁

- `SET key value NX EX 10`；
- 释放时用 Lua 脚本保证原子性；
- Redisson 提供看门狗自动续期。

### 7. 缓存和数据库一致性

- 读：先读缓存，未命中读 DB 并写缓存；
- 写：先更新 DB，再删缓存（Cache Aside）；
- 延迟双删、消息队列补偿、Canal 监听 binlog 删缓存。

### 8. 内存淘汰策略

- noeviction：默认，内存满直接报错；
- allkeys-lru：所有 key 按 LRU 淘汰；
- volatile-lru：设置过期时间的 key 按 LRU 淘汰；
- allkeys-random、volatile-random、volatile-ttl。

---

## 八、Elasticsearch

### 1. 倒排索引

- 记录每个词项出现在哪些文档及位置；
- 查询时直接通过词项定位文档，效率高。

### 2. ES 集群节点类型

- Master：管理集群元数据；
- Data：存储数据、执行 CRUD 和聚合；
- Ingest：预处理文档；
- Coordinating：接收请求、转发、汇总结果。

### 3. 分片和副本

- 分片：数据水平拆分，提高并发；
- 副本：分片的复制，提高可用性和读性能；
- 分片数创建索引后不可变，副本数可动态调整。

### 4. 索引生命周期管理 ILM

- 热（Hot）：频繁读写；
- 温（Warm）：只读，查询减少；
- 冷（Cold）：很少访问；
- 冻结（Frozen）：归档。

### 5. 冷热分离

- 热节点用 SSD，存放近期数据；
- 冷节点用机械盘，存放历史数据；
- 通过 ILM 策略自动迁移。

### 6. ES 写入和查询流程

- 写入：请求到协调节点 → 路由到主分片 → 主分片写入后同步副本 → 返回结果；
- 查询：协调节点分发到各分片 → 各分片本地查询 → 汇总排序返回。

### 7. 中文分词器

- IK 分词器：ik_smart 粗粒度、ik_max_word 细粒度；
- 可配置自定义词典和热更新。

### 8. ES 调优

- 合理设置分片数和副本数；
- 控制字段数量，避免深度分页；
- 使用 filter 缓存；
- 慢查询日志分析；
- 批量写入用 Bulk API。

---

## 九、Linux

### 1. 查看进程占用 CPU/内存

- `top`、`htop`、`ps aux --sort=-%mem`、`pidstat`。

### 2. 查看端口占用和网络连接

- `netstat -tunlp`、`ss -tunlp`、`lsof -i:port`。

### 3. 查找大文件和日志

- `du -sh *`、`find / -size +100M`、`ls -lhS`。

### 4. 常用命令

- 压缩：`tar -czvf`、`tar -xzvf`、`zip/unzip`；
- 权限：`chmod`、`chown`；
- 文本：`grep`、`awk`、`sed`、`cut`、`sort`、`uniq`。

### 5. 实时日志查看

- `tail -f logfile`；
- `tail -100f logfile | grep keyword`；
- `awk '/error/{print $0}' logfile`。

### 6. 写过的 Shell 脚本举例

- 日志切割清理脚本；
- 服务启动停止脚本；
- 定时备份数据库脚本；
- 监控 CPU/内存告警脚本。

### 7. top/ps/netstat/ss/lsof/df/du 场景

- top：实时看系统整体负载；
- ps：查看进程快照；
- netstat/ss：查看网络连接和端口；
- lsof：查看进程打开的文件；
- df：磁盘分区使用情况；
- du：目录/文件占用空间。

---

## 十、项目经验 & 场景题

### 1. 介绍一个最有成就感的项目

- 项目背景和目标；
- 你的角色和职责；
- 技术栈和架构；
- 遇到的难点及解决方案；
- 最终成果（量化数据更佳）。

### 2. 微服务怎么拆分

- 按业务领域（DDD）；
- 按功能模块；
- 拆分的粒度：高内聚低耦合，避免过细导致运维复杂度上升。

### 3. 线上故障排查

- 先看监控和告警，确认影响范围；
- 查日志定位异常堆栈；
- 线程栈分析 CPU/死锁；
- 数据库慢查询分析；
- 回滚或限流止血，事后复盘。

### 4. 高并发限流降级

- 限流： Sentinel/Guava RateLimiter/Nginx 限流，算法有令牌桶、漏桶、固定窗口、滑动窗口；
- 降级：核心接口保留，非核心功能关闭或返回兜底；
- 熔断：防止故障扩散。

### 5. 秒杀系统设计

- 前端：验证码、按钮置灰、CDN 静态化；
- 网关：限流、鉴权；
- 应用层：Redis 预减库存、异步下单；
- 消息队列：削峰填谷；
- 数据库：乐观锁/悲观锁控制超卖。

### 6. 分布式事务

- 强一致性：2PC、3PC；
- 最终一致性：TCC、 Saga、本地消息表、RocketMQ 事务消息；
- Seata AT 模式：通过 undo log 实现自动回滚。

### 7. 接口性能优化

- SQL 加索引、减少查询次数；
- 缓存热点数据；
- 异步处理非关键路径；
- 批量操作替代循环；
- 连接池、线程池参数调优。

### 8. 接口幂等性

- 数据库唯一索引；
- Token 机制：先申请 token，执行时校验并删除；
- 分布式锁；
- 状态机幂等。

---

## 十一、Spring MVC 原理

### 常见问法

- Spring MVC 的请求处理流程是什么？
- Spring MVC 的核心组件有哪些？
- DispatcherServlet 的作用是什么？

### 参考答案

Spring MVC 是基于 Servlet 的 Web 框架，核心入口是 `DispatcherServlet`。请求处理流程如下：

1. 客户端请求进入 `DispatcherServlet`；
2. `DispatcherServlet` 调用 `HandlerMapping`，根据请求 URL 找到对应的处理器 `Handler`；
3. 通过 `HandlerAdapter` 适配器调用具体的处理器方法；
4. `Controller` 处理业务逻辑，返回 `ModelAndView`；
5. `ViewResolver` 解析视图名称，定位到具体视图；
6. 视图渲染后把响应结果返回给客户端。

核心组件包括：**DispatcherServlet、HandlerMapping、HandlerAdapter、Controller、ViewResolver、HandlerInterceptor**。其中拦截器可以在请求前后做日志记录、权限校验、限流等通用处理。

---

## 十二、SQL 优化

### 常见问法

- 你平时怎么优化慢 SQL？
- 索引失效的场景有哪些？
- 如何查看 SQL 执行计划？

### 参考答案

排查和优化慢 SQL 我通常按以下步骤：

1. **查看执行计划**：用 `EXPLAIN` 分析 `type`、`key`、`rows`、`Extra`，确认是否走了索引；
2. **避免 SELECT ***：只查询必要字段，减少回表和 IO；
3. **建立合适索引**：根据 WHERE、ORDER BY、JOIN 条件设计联合索引，遵循最左前缀原则；
4. **避免索引失效**：如索引列上做函数运算、隐式类型转换、`LIKE '%xxx'`、用 `OR` 连接不同字段、`NOT IN` 等；
5. **优化大分页**：深分页用覆盖索引加子查询，或记录上次查询位置做游标分页；
6. **拆分复杂 SQL**：将大 SQL 拆成多个小 SQL，减少临时表和排序开销；
7. **优化表结构**：字段类型尽量精简，避免大量 NULL，必要时做冗余或反范式设计。

---

## 十三、数据量较大表的处理方式

### 常见问法

- 表数据量很大怎么优化查询？
- 分库分表怎么做？
- 大数据量迁移怎么处理？

### 参考答案

大表优化我一般从多个层面考虑：

1. **索引层面**：先确认索引是否合理，避免全表扫描；
2. **SQL 层面**：优化慢查询，减少锁时间和扫描行数；
3. **表结构层面**：做垂直拆分，将大字段或不常用字段拆分到扩展表；
4. **分区表**：按时间或范围做表分区，提升查询和维护效率；
5. **读写分离**：主库负责写，从库负责读，减轻主库压力；
6. **分库分表**：水平拆分数据，常用拆分键有用户 ID、订单 ID、时间等；
7. **冷热分离**：热数据放高性能存储，冷数据归档到低频存储；
8. **搜索引擎/缓存**：复杂检索走 Elasticsearch，热点数据走 Redis。

如果是数据迁移，我会采用 **双写 + 校验 + 灰度切换** 的方案：先同步数据，再逐步切流，过程中持续对账，确保业务不中断。

---

## 面试应答策略

1. **先讲概念，再讲原理，最后带一句实践**，避免只背书；
2. **不会的问题**直接说"这个我没深入用过，但了解大致思路"，再给出合理推测；
3. **项目题用 STAR 法则**：背景、任务、行动、结果；
4. **遇到连环追问要稳住**，把你知道的边界说清楚即可。
