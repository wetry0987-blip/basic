# Java 集合专题补全学习计划

> 基于韩顺平 Java 集合专题，补充 TreeMap / LinkedHashMap / HashSet 底层原理、Collections 工具类、迭代器原理、fail-fast 机制。

---

## 一、需要补全的知识点

### 1. TreeMap 底层原理
- **数据结构**：基于**红黑树**（自平衡二叉搜索树）实现，不是哈希表。
- **核心特点**：
  - 键（Key）按顺序排列，遍历时天然有序。
  - 键需要可比较：要么 Key 实现 `Comparable`，要么构造时传入 `Comparator`。
  - **Key 不能为 null**（null 无法参与比较，会抛 NPE）。
  - `put` / `get` / `remove` 时间复杂度都是 **O(log n)**。
- **常用场景**：需要排序的映射，比如成绩排名、区间查询、字典序存储。
- **和 HashMap 对比**：

  | 特性 | HashMap | TreeMap |
  |---|---|---|
  | 底层结构 | 数组+链表+红黑树 | 红黑树 |
  | 顺序 | 无序 | 按键排序 |
  | Key 是否可为 null | 可以一个 | 不可以 |
  | 时间复杂度 | 平均 O(1)，最坏 O(log n) | O(log n) |

---

### 2. LinkedHashMap 底层原理
- **数据结构**：继承自 `HashMap`，但额外维护了一条**双向链表**，把节点按插入/访问顺序串起来。
- **两种顺序模式**：
  - `accessOrder = false`（默认）：按**插入顺序**遍历。
  - `accessOrder = true`：按**访问顺序**遍历（每次 get/put 访问会把节点移到链表尾部）。
- **经典应用**：实现 **LRU 缓存**（Least Recently Used），重写 `removeEldestEntry()` 即可。
- **性能**：比 HashMap 稍慢，因为要维护链表，但差距很小；迭代时反而更快（按链表顺序，不需要遍历空桶）。

---

### 3. HashSet 底层原理
- **本质**：底层就是 `HashMap`。
  ```java
  private transient HashMap<E,Object> map;
  private static final Object PRESENT = new Object();
  ```
  你的元素作为 `HashMap` 的 Key，Value 统一是 `PRESENT` 这个占位对象。
- **去重机制**：依赖元素的 `hashCode()` 和 `equals()`。
- **特点**：
  - 无序、不重复。
  - 允许一个 `null` 元素。
  - 添加/删除/查找平均 O(1)。
- **和 HashMap 的关系**：Set 是“只有 Key 的 Map”，所以讲 HashSet 时重点回顾哈希冲突、扩容、红黑树化即可。

---

### 4. Collections 工具类
这是 Java 提供的集合**算法工具类**，类似数组的 `Arrays`。

常用方法分类：

| 类别 | 方法 | 作用 |
|---|---|---|
| 排序 | `sort(List)` / `sort(List, Comparator)` | 对 List 排序 |
| 反转/打乱 | `reverse(List)` / `shuffle(List)` | 反转、随机打乱 |
| 查找 | `binarySearch(List, key)` / `max/min` / `frequency` | 二分查找、最值、统计出现次数 |
| 替换/填充 | `replaceAll(List, oldVal, newVal)` / `fill(List, obj)` | 替换、填充 |
| 线程安全包装 | `synchronizedList/Map/Set...` | 返回同步集合 |
| 不可变包装 | `unmodifiableList/Map/Set...` | 返回只读视图 |
| 单例集合 | `singletonList/Map/Set` | 创建只含一个元素的不可变集合 |

> 注意：`Collections.sort()` 在 Java 8 后内部调用 `List.sort()`，本质都是 TimSort。

---

### 5. 迭代器（Iterator）原理
- **为什么需要迭代器**：提供统一的遍历方式，屏蔽不同集合的内部结构。
- **Iterator 接口三个核心方法**：
  - `boolean hasNext()`
  - `E next()`
  - `void remove()`（删除当前元素，比直接在 for 循环里删除更安全）
- **foreach 的底层**：编译器会把 `for (E e : collection)` 转成 `Iterator` 遍历。
- **ListIterator**：`List` 专用，支持：
  - 双向遍历：`hasPrevious()` / `previous()`
  - 修改：`add()` / `set()`
  - 获取索引：`nextIndex()` / `previousIndex()`

---

### 6. fail-fast 机制
- **含义**：快速失败。在迭代过程中，如果集合被**结构性修改**（增删元素），立即抛出 `ConcurrentModificationException`。
- **实现原理**：
  - 集合内部维护一个 `modCount`（修改次数）。
  - 迭代器创建时记录 `expectedModCount = modCount`。
  - 每次 `next()` 前检查 `modCount == expectedModCount`，不等就抛异常。
- **常见触发场景**：
  ```java
  List<String> list = new ArrayList<>();
  // ...添加元素
  for (String s : list) {
      if (s.equals("suanfati.longestCommonPrefix")) list.remove(s); // 可能抛 ConcurrentModificationException
  }
  ```
- **解决办法**：
  - 使用迭代器的 `remove()`。
  - 使用 `for` 循环倒序删除。
  - 使用并发集合：`CopyOnWriteArrayList`、`ConcurrentHashMap`（fail-safe，不会抛异常，但弱一致性）。

---

## 二、学习规划（建议 5 天）

### Day 1：Map 体系收尾（TreeMap + LinkedHashMap）
- **上午**：看韩顺平/源码中 `TreeMap` 的 put 流程、红黑树旋转概念（了解即可，不用手写红黑树）。
- **下午**：学习 `LinkedHashMap`，重点理解双向链表如何维护顺序，手写一个 LRU 缓存。
- **晚上**：对比 HashMap / TreeMap / LinkedHashMap，整理表格笔记。
- **验证任务**：写 3 个 demo：
  1. TreeMap 按学生年龄排序。
  2. LinkedHashMap 按插入顺序遍历。
  3. 用 LinkedHashMap 实现一个固定容量的 LRU 缓存。

### Day 2：Set 体系（HashSet + LinkedHashSet + TreeSet）
- **上午**：HashSet 底层就是 HashMap，重点理解去重原理。
- **下午**：LinkedHashSet（插入有序）、TreeSet（排序，底层 TreeMap）。
- **晚上**：整理 Set 家族对比表。
- **验证任务**：自定义对象去重，分别用 HashSet / TreeSet / LinkedHashSet 测试顺序。

### Day 3：Collections 工具类
- **上午**：sort / reverse / shuffle / max / min / frequency。
- **下午**：synchronizedXXX / unmodifiableXXX / singletonXXX。
- **晚上**：理解“包装视图”和原集合的关系。
- **验证任务**：
  1. 对 List 排序（自然排序 + Comparator）。
  2. 生成一个不可变的 List 视图并尝试修改，观察异常。
  3. 生成一个线程安全的 List，理解它只是在方法上加 synchronized。

### Day 4：迭代器 + fail-fast
- **上午**：Iterator / ListIterator 用法，foreach 底层原理。
- **下午**：fail-fast 机制源码级理解（modCount / expectedModCount）。
- **晚上**：fail-safe 初步了解（CopyOnWriteArrayList / ConcurrentHashMap）。
- **验证任务**：
  1. 用 Iterator 正确删除集合元素。
  2. 用增强 for 循环删除元素，触发 `ConcurrentModificationException`。
  3. 用 `ListIterator` 实现双向遍历和替换。

### Day 5：综合串联 + 输出
- **上午**：画一张 Java 集合全家脑图（List / Set / Map / Queue / 工具类）。
- **下午**：做 10 道集合高频面试题，覆盖今天补的内容。
- **晚上**：整理一份自己的“Java 集合专题总结”，至少包含：
  - HashMap vs TreeMap vs LinkedHashMap
  - HashSet 底层
  - Collections 常用工具
  - fail-fast 原理和避免方法

---

## 三、推荐学习顺序口诀

> 先 List 再 Set，然后 Map 三兄弟；  
> Hash 看桶链树，Tree 看红黑序；  
> Linked 加链表，LRU 真神器；  
> Collections 是工具，迭代器要牢记；  
> fail-fast 莫乱删，并发集合保平安。

---

## 四、后续建议

如果想进一步巩固，可以：
1. 阅读 JDK 源码：重点看 `HashMap.putVal`、`TreeMap.put`、`LinkedHashMap.afterNodeAccess`、`ArrayList.Itr`。
2. 手写 mini 版：实现一个简单的 ArrayList、LinkedList、HashMap，加深理解。
3. 刷面试题：重点练习 HashMap 扩容、并发修改异常、Comparable 与 Comparator 区别。
