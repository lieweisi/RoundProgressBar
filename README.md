# RoundProgressBar
带进度圆环，中间可添加正副标题
<br>##效果预览
![image](https://github.com/lieweisi/RoundProgressBar/blob/master/roundProgressBar.gif)
### 引入方式：

    1.在项目bulid.gradle中添加
    allprojects {
    repositories {
        jcenter()
        maven { url 'https://jitpack.io' }
    }
    2.在app的bulid.gradle中添加引用  
    compile 'com.github.lieweisi:SelectPhotos:v1.0'
    
### 具体使用步骤：
    1.在布局添加：
    <com.liluo.library.RoundProgressBar
        android:id="@+id/rb_test"
        android:layout_width="100dp"
        android:layout_height="100dp"/>
    2.在activity或fragment里面进行初始化
      rb_test= (RoundProgressBar) findViewById(R.id.rb_test);
      rb_test.setAnimProgress(50);//设置进度
      rb_test.setCenterText("50");
      rb_test.setSubText("完成度");
      rb_test.setTextSize(25);
      rb_test.setTextSubSize(30);
      rb_test.setTextColor(Color.parseColor("#3F51B5"));
      rb_test.setTextSubColor(Color.parseColor("#FF4081"));
      rb_test.setCricleColor(Color.parseColor("#3F51B5"));
      rb_test.setCricleProgressColor(Color.parseColor("#FF4081"))

