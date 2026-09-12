package suanfati;

import java.util.Arrays;

public class LCP {
    public String longestCommonPrefix(String[] strs) {
        if (strs == null || strs.length == 0) {
            return "";
        }

        // 1. 排序：让最短/最不同字符的字符串排在两端
        Arrays.sort(strs);

        String first = strs[0];
        String last = strs[strs.length - 1];

        int i = 0;
        // 2. 逐字符比较首尾两个字符串
        while (i < first.length() && i < last.length() && first.charAt(i) == last.charAt(i)) {
            i++;
        }

        // 3. 截取公共部分
        return first.substring(0, i);
    }
}
