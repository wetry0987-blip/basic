import java.util.ArrayList;
import java.util.Comparator;

public class Main
{
 //自定义异常类
 // 解释：这是一个自定义异常类，名为 ScoreException（分数异常）。
 // 它继承自 Java 标准的 Exception 类，用于在程序中抛出与“分数”相关的特定错误。
 class ScoreException extends Exception{
     // 解释：这是该异常类的构造方法，接收一个字符串参数 message 作为异常的错误描述信息。
     public ScoreException(String message){
         // 解释：super(message) 调用了父类 Exception 的构造方法，
         // 将错误信息传递给父类保存，后续捕获异常时可以通过 e.getMessage() 获取该信息。
         super(message);
     }
 }
 //学生类
    // 解释：这是一个内部类 Student（学生类），定义在 Main 类里面。
    // 它用于描述一个学生对象，包含学号、姓名和分数三个属性。
    class Student{
        // 解释：private 表示私有成员变量，只能在 Student 类内部访问。
        // id 表示学生的学号。
        private String id;
        // name 表示学生的姓名。
        private String name;
        // score 表示学生的分数。
        private double score;

        // 解释：这是 Student 类的构造方法。
        // 当使用 new Student("001", "张三", 90.0) 创建对象时，会调用这个方法，
        // 把传入的学号、姓名、分数赋值给当前对象的成员变量。
        public Student(String id, String name, double score) {
            this.id = id;
            this.name = name;
            this.score = score;
        }

        // 解释：getId 是 getter 方法，用于获取学生的学号。
        // 因为 id 是 private 私有的，外部需要通过这个方法来读取 id 的值。
        public String getId() {
            return id;
        }

        // 解释：getName 是 getter 方法，用于获取学生的姓名。
        public String getName() {
            return name;
        }

        // 解释：getScore 是 getter 方法，用于获取学生的分数。
        public double getScore() {
            return score;
        }

        // 解释：setScore 是 setter 方法，用于修改学生的分数。
        // 注意：这里只提供了 setScore，没有提供 setId 和 setName，
        // 说明学号和姓名创建后不能修改，分数可以被修改。
        public void setScore(double score) {
            this.score = score;
        }

        // 解释：toString 方法来自 Object 类，这里使用 @Override 进行重写。
        // 它的作用是把学生对象转换成字符串格式，方便直接打印对象时查看内容。
        // 例如：System.out.println(student); 会输出 Student{id='001', name='张三', score=90.0}
        @Override
        public String toString() {
            return "Student{" +
                    "id='" + id + '\'' +
                    // "name='"  → 双引号是字符串，里面的单引号 ' 不需要转义，直接写就行
                    // name     → 变量，比如 "张三"
                    // '\''     → 单引号是 char 字符的定界符，所以里面的 ' 必须用 \ 转义
                    //            拆开看： ' 开始 → \' 转义的单引号 → ' 结束
                    //            如果写成 ''' 编译器会懵：以为前两个 '' 是空字符，第三个多余
                    // 最终拼接效果：, name='张三'
                    ", name='" + name + '\'' +
                    ", score=" + score +
                    '}';
        }
    }

    //学生成绩管理类
    class StudentScoreManager {
        //用ArrayList存储学生信息
        private ArrayList<Student> students = new ArrayList<>();
        // 根据学号查找学生
        // 这是一个根据学号（id）查找学生对象的私有方法。
        // 解释：
        // 1. 遍历 students 列表中的每一个学生对象。
        // 2. 使用 equals() 方法比较当前学生的学号与传入的 id 是否一致。
        // 3. 如果找到匹配的学号，立即返回该 Student 对象。
        // 4. 如果遍历结束仍未找到，则返回 null，表示该学号的学生不存在。


        // 添加学生信息
        public void addStudent(Student student) throws ScoreException {
            if (student == null) {
                throw new ScoreException("学生对象不能为空");
            }
            ;
            if (student.getId() == null || student.getId().trim().isEmpty()) {
                throw new ScoreException("学生学号不能为空");
            }
            if (student.getScore() < 0 || student.getScore() > 100) {
                throw new ScoreException("学生成绩必须在0-100之间");
            }
            // 如果学号重复，也不允许添加
            if (findById(student.getId()) != null) {
                throw new ScoreException("学号重复，不允许添加" + student.getId());
            }
            students.add(student);

        }

        //删除学生
        public void removeStudent(String id) throws ScoreException {
            if (id == null || id.trim().isEmpty()) {// 如果为空或者去掉前后空格为空，则认为无效
                throw new ScoreException("学生学号不能为空");
            }
            Student student = findById(id);
            if (student == null) {
                throw new ScoreException("学生不存在，不允许删除");
            }
            students.remove(student);
            students.remove(student);
        }

        //修改成绩
        public void updateScore(String id, double score) throws ScoreException {
//                    throw new ScoreException("学生学号不能为空");
            if (id == null || id.trim().isEmpty()) {
                throw new ScoreException("学生学号不能为空");
            }
            if (score < 0 || score > 100) {
                throw new ScoreException("学生成绩必须在0-100之间");
            }
            Student student = findById(id);
            if (student == null) {
                throw new ScoreException("修改失败，未找到学号" + id);
            }
            student.setScore(score);
        }

        //按学号查询
        private Student findById(String id) {
            for (Student student : students) {
                if (student.getId().equals(id)) {
                    return student;
                }
            }
            return null;
        }

        //查询所有学生
        // 解释：这是一个公共方法，名为 getAllStudents，返回类型是 ArrayList<Student>。
        // 它的作用是获取当前存储的所有学生列表，供外部调用查看。
        public ArrayList<Student> getAllStudents() {
            // 解释：return new ArrayList<>(students);
            // 这里返回的是一个新的 ArrayList 对象，它复制了当前类中 students 列表的所有学生数据。
            // 为什么不直接 return students; 呢？
            // 因为如果直接返回 students 本身，外部代码拿到引用后就可以直接修改它
            // （比如添加或删除学生），这样会破坏内部数据的封装性和安全性。
            // 创建一个新副本返回（这叫“防御性拷贝”），外部怎么改都只影响副本，不影响内部的 students。
            return new ArrayList<>(students);
        }
        //5.按成绩升序排序
        /**
         * 按成绩升序排序
         *
         * 解释：
         * 1. students.sort(...) 是 ArrayList 的排序方法，接收一个 Comparator（比较器）作为参数。
         *
         * 2. Comparator.comparingDouble(Student::getScore) 是创建比较器的简洁写法：
         *    - Comparator.comparingDouble 是专门用于比较 double 类型的比较器工厂方法。
         *    - Student::getScore 是方法引用，等价于 lambda 表达式 s -> s.getScore()，
         *      表示"用学生的 getScore() 返回值作为比较依据"。
         *
         * 3. 最终效果：将 students 列表中的学生按照 score 从小到大（升序）排列。
         *    例如：[90, 60, 75] 排序后变为 [60, 75, 90]
         */
        public void sortByScoreAsc() {
            students.sort(Comparator.comparingDouble(Student::getScore));
        }
        //6、按成绩降序排序
        /**
         * 按成绩降序排序
         *
         * 解释：
         * 1. students.sort(...) 是 ArrayList 的排序方法，接收一个 Comparator（比较器）作为参数。
         *
         * 2. (s1, s2) -> Double.compare(s2.getScore(), s1.getScore()) 是一个 Lambda 表达式，
         *    它实现了 Comparator 接口的 compare 方法：
         *    - s1 和 s2 是列表中任意两个相邻的学生对象。
         *    - Double.compare(a, b) 的比较规则：
         *      · 如果 a > b，返回正数（表示 a 应该排在 b 前面）
         *      · 如果 a < b，返回负数（表示 a 应该排在 b 后面）
         *      · 如果 a == b，返回 0（表示两者顺序不变）
         *    - 注意这里故意把 s2 放在前面、s1 放在后面（与升序相反），
         *      所以当 s2 的分数更高时返回正数，s2 就排到 s1 前面，
         *      从而实现从大到小（降序）排列。
         *
         * 3. 最终效果：将 students 列表中的学生按照 score 从大到小（降序）排列。
         *    例如：[60, 90, 75] 排序后变为 [90, 75, 60]
         *
         * 补充：也可以用更简洁的写法实现相同效果：
         *    students.sort(Comparator.comparingDouble(Student::getScore).reversed());
         *    其中 .reversed() 表示将升序比较器反转为降序比较器。
         */
        public void sortByScoreDesc() {
            students.sort((s1, s2) -> Double.compare(s2.getScore(), s1.getScore()));
        }
        //打印所有学生信息
        public void printAll(){
            System.out.println("--------------当前学生列表-------------");
            if(students.isEmpty()){
                System.out.println("没有学生信息");
            }else{
                for (Student student : students) {
                    System.out.println(student);
                }
            }
            System.out.println("------------------");
        }
        }
        //测试方法
    public static void main(String[] args) {
        Main outer = new Main();
        StudentScoreManager manager = outer.new StudentScoreManager();
        //测试添加学生
//需要修改的原因：`Main` 类的 `main` 方法是 `static` 静态方法，而 `Student` 和 `StudentScoreManager` 都是**非静态内部类**，不能直接在静态方法中通过 `outer.new` 这种方式实例化（`outer` 变量根本不存在）。
//正确做法是：**先创建外部类 `Main` 的实例**，再通过该实例创建内部类对象。
        try{
            manager.addStudent(outer.new Student("001", "张三", 90.0));
            manager.addStudent(outer.new Student("002", "张三", 60.0));
            manager.addStudent(outer.new Student("003", "张三", 80.0));
            System.out.println("添加学生成功");
            manager.addStudent(outer.new Student("002", "张三", 70.0));
        }catch (ScoreException e){
            System.out.println(e.getMessage());
        }
        //测试修改非法成绩
        try{

            manager.updateScore("002", 100.0);
            System.out.println("修改成绩成功");
            manager.updateScore("001", 1010.0);
            System.out.println("修改成绩失败");

        }catch (ScoreException e){
            System.out.println(e.getMessage());
        }
        //测试删除不存在的学生
        try{
            manager.removeStudent("001");
            System.out.println("删除学生成功");
            manager.removeStudent("005");
        }catch (ScoreException e){
            System.out.println(e.getMessage());
        }
        //测试查询学生
        Student student = manager.findById("001");
        if(student !=null){
            System.out.println("查询到的学生信息：" + student);
        }else {
            System.out.println("未找到该学生");
        }
        //打印当前所有学生
        manager.printAll();
// 按照成绩升序排序
        System.out.println("--------------按照成绩升序排序-------------");
        manager.sortByScoreAsc();
        manager.printAll();
        System.out.println("--------------按照成绩降序排序-------------");
        manager.sortByScoreDesc();
        manager.printAll();
    }
    }





