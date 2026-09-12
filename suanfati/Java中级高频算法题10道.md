# Java 中级开发高频算法题 10 道

> 针对华胜天成 Java 中级开发岗位整理，难度以简单到中等为主，覆盖链表、字符串、数组、二叉树、排序、查找和动态规划。

---

## 1. 反转链表

### 题目
给定单链表的头节点 `head`，请反转链表，并返回反转后的头节点。

### 思路
使用三个指针：`prev` 指向前一个节点，`cur` 指向当前节点，`next` 暂存当前节点的下一个节点。遍历链表时，将当前节点指向 `prev`，然后三个指针依次后移。

### 代码

```java
/**
 * 反转单链表
 * 时间复杂度：O(n)，空间复杂度：O(1)
 */
class ListNode {
    int val;
    ListNode next;
    ListNode(int x) { val = x; }
}

public ListNode reverseList(ListNode head) {
    ListNode prev = null; // 前一个节点，初始为 null
    ListNode cur = head;  // 当前节点
    while (cur != null) {
        ListNode next = cur.next; // 暂存下一个节点，防止断链
        cur.next = prev;          // 当前节点指向前一个节点，完成反转
        prev = cur;               // prev 后移
        cur = next;               // cur 后移
    }
    return prev; // prev 最终指向新的头节点
}
```

---

## 2. 合并两个有序链表

### 题目
将两个升序链表合并为一个新的升序链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。

### 思路
使用一个虚拟头节点 `dummy` 简化边界处理。比较两个链表当前节点的值，将较小者接入新链表，指针后移。最后将未遍历完的链表直接接上。

### 代码

```java
/**
 * 合并两个升序链表
 * 时间复杂度：O(m+n)，空间复杂度：O(1)
 */
public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
    ListNode dummy = new ListNode(0); // 虚拟头节点，统一边界处理
    ListNode cur = dummy;
    while (l1 != null && l2 != null) {
        if (l1.val <= l2.val) {
            cur.next = l1;
            l1 = l1.next;
        } else {
            cur.next = l2;
            l2 = l2.next;
        }
        cur = cur.next;
    }
    // 剩余部分直接拼接
    cur.next = (l1 != null) ? l1 : l2;
    return dummy.next;
}
```

---

## 3. 无重复字符的最长子串

### 题目
给定一个字符串 `s`，请你找出其中不含有重复字符的最长子串的长度。

### 思路
滑动窗口。用左右指针维护窗口，用 HashSet 判断字符是否重复。右指针不断右扩，遇到重复字符则左指针右移收缩窗口，直到无重复。过程中记录最大窗口长度。

### 代码

```java
/**
 * 无重复字符的最长子串（滑动窗口）
 * 时间复杂度：O(n)，空间复杂度：O(min(m,n))，m 为字符集大小
 */
import java.util.HashSet;
import java.util.Set;

public int lengthOfLongestSubstring(String s) {
    Set<Character> window = new HashSet<>();
    int left = 0;
    int maxLen = 0;
    for (int right = 0; right < s.length(); right++) {
        char c = s.charAt(right);
        // 如果字符已在窗口中，收缩左边界直到该字符被移除
        while (window.contains(c)) {
            window.remove(s.charAt(left));
            left++;
        }
        window.add(c);
        maxLen = Math.max(maxLen, right - left + 1);
    }
    return maxLen;
}
```

---

## 4. 两数之和

### 题目
给定一个整数数组 `nums` 和一个整数目标值 `target`，请你在该数组中找出和为目标值的那两个整数，并返回它们的数组下标。

### 思路
使用 HashMap 存储已经遍历过的值及其下标。遍历数组时，计算 `target - nums[i]`，如果该值在 map 中，说明找到了答案。

### 代码

```java
/**
 * 两数之和（哈希表一次遍历）
 * 时间复杂度：O(n)，空间复杂度：O(n)
 */
import java.util.HashMap;
import java.util.Map;

public int[] twoSum(int[] nums, int target) {
    Map<Integer, Integer> map = new HashMap<>();
    for (int i = 0; i < nums.length; i++) {
        int complement = target - nums[i];
        if (map.containsKey(complement)) {
            return new int[]{map.get(complement), i};
        }
        map.put(nums[i], i);
    }
    return new int[0];
}
```

---

## 5. 有效的括号

### 题目
给定一个只包括 `'('`，`')'`，`'{'`，`'}'`，`'['`，`']'` 的字符串 `s`，判断字符串是否有效。

### 思路
使用栈。遇到左括号入栈，遇到右括号则与栈顶元素匹配。如果栈顶不是对应的左括号，则无效。遍历结束后栈为空才有效。

### 代码

```java
/**
 * 有效的括号（栈）
 * 时间复杂度：O(n)，空间复杂度：O(n)
 */
import java.util.Stack;

public boolean isValid(String s) {
    Stack<Character> stack = new Stack<>();
    for (char c : s.toCharArray()) {
        if (c == '(' || c == '[' || c == '{') {
            stack.push(c);
        } else {
            if (stack.isEmpty()) return false;
            char top = stack.pop();
            // 检查右括号是否与栈顶左括号匹配
            if ((c == ')' && top != '(') ||
                (c == ']' && top != '[') ||
                (c == '}' && top != '{')) {
                return false;
            }
        }
    }
    return stack.isEmpty();
}
```

---

## 6. 二叉树层序遍历

### 题目
给你二叉树的根节点 `root`，返回其节点值的层序遍历。（即逐层地、从左到右访问所有节点）。

### 思路
使用队列实现广度优先搜索。每次取出当前层的所有节点，记录它们的值，并将下一层节点入队。

### 代码

```java
/**
 * 二叉树层序遍历（BFS）
 * 时间复杂度：O(n)，空间复杂度：O(n)
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode(int x) { val = x; }
}

public List<List<Integer>> levelOrder(TreeNode root) {
    List<List<Integer>> result = new ArrayList<>();
    if (root == null) return result;
    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);
    while (!queue.isEmpty()) {
        int size = queue.size(); // 当前层的节点数量
        List<Integer> level = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            TreeNode node = queue.poll();
            level.add(node.val);
            if (node.left != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
        }
        result.add(level);
    }
    return result;
}
```

---

## 7. 快速排序

### 题目
给定整数数组 `nums`，请你将该数组升序排列。

### 思路
分治法。选一个基准元素，将数组分成两部分：左边都比基准小，右边都比基准大。然后递归处理左右两部分。

### 代码

```java
/**
 * 快速排序
 * 平均时间复杂度：O(n log n)，最坏 O(n²)；空间复杂度：O(log n)
 */
public void quickSort(int[] nums, int left, int right) {
    if (left >= right) return;
    int pivotIndex = partition(nums, left, right);
    quickSort(nums, left, pivotIndex - 1);
    quickSort(nums, pivotIndex + 1, right);
}

private int partition(int[] nums, int left, int right) {
    int pivot = nums[left]; // 选择最左边元素作为基准
    int i = left, j = right;
    while (i < j) {
        // 从右向左找第一个小于基准的元素
        while (i < j && nums[j] >= pivot) j--;
        // 从左向右找第一个大于基准的元素
        while (i < j && nums[i] <= pivot) i++;
        if (i < j) swap(nums, i, j);
    }
    // 将基准元素放到最终位置
    nums[left] = nums[i];
    nums[i] = pivot;
    return i;
}

private void swap(int[] nums, int i, int j) {
    int temp = nums[i];
    nums[i] = nums[j];
    nums[j] = temp;
}
```

---

## 8. 二分查找

### 题目
给定一个 `n` 个元素有序的（升序）整型数组 `nums` 和一个目标值 `target`，写一个函数搜索 `nums` 中的 `target`，如果目标值存在返回下标，否则返回 -1。

### 思路
在有序数组中，每次取中间元素与目标值比较，缩小一半搜索范围，直到找到目标或区间为空。

### 代码

```java
/**
 * 二分查找
 * 时间复杂度：O(log n)，空间复杂度：O(1)
 */
public int binarySearch(int[] nums, int target) {
    int left = 0, right = nums.length - 1;
    while (left <= right) {
        int mid = left + (right - left) / 2; // 防止 (left + right) 溢出
        if (nums[mid] == target) {
            return mid;
        } else if (nums[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    return -1;
}
```

---

## 9. 爬楼梯

### 题目
假设你正在爬楼梯。需要 `n` 阶你才能到达楼顶。每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶？

### 思路
动态规划。`dp[i]` 表示爬到第 `i` 阶的方法数，状态转移方程为 `dp[i] = dp[i-1] + dp[i-2]`。初始条件 `dp[0]=1, dp[1]=1`。由于只依赖前两个状态，可优化到 O(1) 空间。

### 代码

```java
/**
 * 爬楼梯（动态规划，空间优化版）
 * 时间复杂度：O(n)，空间复杂度：O(1)
 */
public int climbStairs(int n) {
    if (n <= 1) return 1;
    int prev2 = 1; // dp[i-2]
    int prev1 = 1; // dp[i-1]
    for (int i = 2; i <= n; i++) {
        int cur = prev1 + prev2; // dp[i] = dp[i-1] + dp[i-2]
        prev2 = prev1;
        prev1 = cur;
    }
    return prev1;
}
```

---

## 10. 三数之和

### 题目
给你一个整数数组 `nums`，判断是否存在三元组 `[nums[i], nums[j], nums[k]]` 满足 `i != j、i != k 且 j != k`，同时还满足 `nums[i] + nums[j] + nums[k] == 0`。请你返回所有和为 0 且不重复的三元组。

### 思路
先排序，固定第一个数，然后用双指针在剩余区间找两数之和为第一个数的相反数。注意跳过重复元素以避免结果重复。

### 代码

```java
/**
 * 三数之和（排序 + 双指针）
 * 时间复杂度：O(n²)，空间复杂度：O(1)，不计结果存储空间
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public List<List<Integer>> threeSum(int[] nums) {
    List<List<Integer>> result = new ArrayList<>();
    if (nums == null || nums.length < 3) return result;
    Arrays.sort(nums);
    for (int i = 0; i < nums.length - 2; i++) {
        // 跳过重复的第一个数
        if (i > 0 && nums[i] == nums[i - 1]) continue;
        int left = i + 1;
        int right = nums.length - 1;
        while (left < right) {
            int sum = nums[i] + nums[left] + nums[right];
            if (sum == 0) {
                result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                // 跳过重复的左指针元素
                while (left < right && nums[left] == nums[left + 1]) left++;
                // 跳过重复的右指针元素
                while (left < right && nums[right] == nums[right - 1]) right--;
                left++;
                right--;
            } else if (sum < 0) {
                left++;
            } else {
                right--;
            }
        }
    }
    return result;
}
```

---

## 面试手撕代码建议

1. **先讲思路再写代码**，面试官更看重你的分析过程；
2. **边界条件主动处理**，比如空指针、数组长度为 0、重复元素；
3. **写完主动报时间复杂度和空间复杂度**；
4. **用笔或白板模拟一遍关键用例**，证明代码正确性；
5. 如果面试官问"还能不能优化"，可以从空间换时间、原地操作等角度思考。
