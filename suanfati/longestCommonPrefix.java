package suanfati;

public class longestCommonPrefix {
    public String longestCommonPrefix(String[] strs) {
        // ✅ 边界处理：null / 空数组 / 单元素（单元素直接返回自身）
        if(strs==null || strs.length==0) {return "";}
        // 全部字符匹配完成，公共前缀即为第一个字符串本身
        for(int i=0;i<strs[0].length();i++){
            char c =strs[0].charAt(i);
            for(int j =1;j<strs.length;j++){
                if (j>= strs.length||strs[j].charAt(i)!=c){
                    return strs[0].substring(0, i);
                }
            }
        }
        return strs[0];
    }
}
