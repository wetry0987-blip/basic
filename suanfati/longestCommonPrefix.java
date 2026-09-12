package suanfati;

public class longestCommonPrefix {
    public String longestCommonPrefix(String[] strs) {
        // ✅ 边界处理：null / 空数组 / 单元素（单元素直接返回自身）
        if (strs == null || strs.length == 0) return "";

        // 以第一个字符串为基准逐字符扫描
        for (int i = 0; i < strs[0].length(); i++) {
            char c = strs[0].charAt(i);

            // 横向检查其余所有字符串的第 i 位
            for (int j = 1; j < strs.length; j++) {
                // ⚠️ 越界保护：j 号串长度不足 或 字符不匹配 → 立即返回
                if (i >= strs[j].length() || strs[j].charAt(i) != c) {
                    return strs[0].substring(0, i);
                }
            }
        }

        // 全部字符匹配完成，公共前缀即为第一个字符串本身
        return strs[0];
    }
}
