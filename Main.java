import java.util.ArrayList;

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
    class StudentScoreManager{
            //用ArrayList存储学生信息
            private ArrayList<Student> students = new ArrayList<>();
            // 根据学号查找学生
            private Student findById(String id) {
                for (Student student : students) {
                    if (student.getId().equals(id)) {
                        return student;
                    }
                }
                return null;
            }

            // 添加学生信息
            public void addStudent(Student student) throws ScoreException{
                if(student==null){
                throw new ScoreException("学生对象不能为空");
                };
                if(student.getId()==null || student.getId().trim().isEmpty())
                {
                    throw new ScoreException("学生学号不能为空");
                }
                if(student.getScore()<0 || student.getScore()>100){
                    throw new ScoreException("学生成绩必须在0-100之间");
                }
                // 如果学号重复，也不允许添加
                if (findById(student.getId()) != null){
                    throw new ScoreException("学号重复，不允许添加"+student.getId());
                }

            }

        }
    }


