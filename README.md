# 安卓MVVM模式



## 采用LiveData+Retrofit2+Rxjava2

## +ViewModel+Repository+Room构建



### LiveData

一个可以自己观察生命周期的数据存储工具，内部采用观察者模式，感知到数据变化时回调observe（），多用于更新UI。  


**(1)没有内存泄漏**  

	观察会绑定具有生命周期的对象，并在这个绑定的对象被销毁后自行清理。
**(2)不会因停止活动发生而崩溃**  

	如果观察的生命周期处于非活跃状态，例如在后退堆栈中的活动，就不会收到任何LiveData事件的通知。
**(3)不需要手动处理生命周期**  

	UI组件只需要去观察相关数据，不需要手动去停止或恢复观察.LiveData会进行自动管理这些事情，因为在观察时，它会感知到相应组件的生命周期变化。
**(4)始终保持最新的数据**  

	如果一个对象的生命周期变到非活跃状态，它将在再次变为活跃状态时接收最新的数据。例如，后台活动在返回到前台后立即收到最新数据。



### Retrofit2+Rxjava2

现在用的最多的异步网络请求工具，以后的博客中会单独说。



### ViewModel

用于连接View和Model，对逻辑进行管理，使View层专注于界面的处理。

**(1)viewmodel的生命周期**

viewmodel的生命周期贯彻了整个activity的生命周期，在屏幕旋转时存储在viewmodel中的数据也不会丢失。

![](https://developer.android.google.cn/images/topic/libraries/architecture/viewmodel-lifecycle.png)

**(2)用于组件间通信（和LiveData一起使用）**

viewmodel：

```java
public class CommunicateViewModel extends ViewModel {

    private static final String TAG = "CommunicateViewModel";

    private MutableLiveData<String> mNameLiveData;


	public LiveData<String> getName(){
    	if (mNameLiveData == null) {
        	mNameLiveData = new MutableLiveData<>();
    	}
    	return mNameLiveData;
	}

	public void setName(String name){
    	if (mNameLiveData != null) {
        	Log.d(TAG, "setName: " + name);
        	mNameLiveData.setValue(name);
    	}
	}

	@Override
	protected void onCleared() {
    	super.onCleared();
    	mNameLiveData = null;
	}
}
```

fragment中绑定viewmodel：

```java
mCommunicateViewModel = ViewModelProviders.of(getActivity()).get(CommunicateViewModel.class);
```

再调用observe方法更新ui，让多个fragment可以共享数据。

```java
 mCommunicateViewModel.getName().observe(this, name -> mTvName.setText(name));
```



### Room

[Room](https://developer.android.google.cn/training/data-storage/room/index.html)持久库提供了一个SQLite抽象层，让你访问数据库更加稳健，提升数据库性能。

该库帮助您在运行应用程序的设备上创建应用程序的数据缓存。这个缓存是你的应用程序唯一的真实来源，允许用户查看应用程序中关键信息的一致副本，而不管用户是否有Internet连接。

**(1)Enity**

用于定义数据的存储格式，和网络请求Retrofit的enity几乎相同，使用时在此类上注解<u>@Enity</u>即可。

```java
@Entity
public class Word {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "english")
    private String english;
    @ColumnInfo(name = "chinese")
    private String chinese;
    @ColumnInfo(name = "is_chinese")
    private boolean isChinese;

    public boolean isChinese() {
        return isChinese;
    }

    public void setIsChinese(boolean chinese) {
        isChinese = chinese;
    }

    public Word(String english, String chinese) {
        this.english = english;
        this.chinese = chinese;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }
}
```



**(2)Dao**

是一个接口，作为数据库操作方法定义的接口。

同样用<u>@Dao</u>注解

```java
@Dao
public interface GankDao {
    @Insert(onConflict = REPLACE)
    void insertGank(GankEnity... gankEnity);

    @Update
    void updateGank(GankEnity... gankEnity);

    @Delete
    void deleteGank(GankEnity... gankEnity);

    @Query("DELETE FROM GANK")
    void deleteAllGanks();

    @Query("SELECT * FROM Gank ORDER BY ID")
    LiveData<GankEnity> getAllGanks();
}
```

**(3)DataBase**

作为一个抽象类，提供数据库方法操作的接口，用于更新数据库版本。

在程序运行之前android studio会自动生成一个类去实现Database抽象类，相当消耗资源，所以要用单例模式构建Database实例。

在数据库版本迁移时，传入一个migration，自定义migration完成数据库版本的迁移。

并且在Database中需要写抽象方法获取Dao实例，如果有多个Enity则要有多个Dao。

最后在类上方添加注解<u>@Database</u>，并添加Enity和版本号，每次更新版本号要大于上一次。

```java
@Database(entities = {Word.class, GankEnity.class}, version = 4, exportSchema = false)
@TypeConverters({ResultConverter.class, ListConverter.class})
public abstract class WordDatabase extends RoomDatabase {
    public abstract WordDao getWordDao();
    public abstract GankDao getGankDao();
    private static volatile WordDatabase instance;


    public static WordDatabase getInstance(Context context){
        if (instance==null){
            synchronized (WordDatabase.class){
                if (instance==null){
                    instance= Room.databaseBuilder(context.getApplicationContext(),WordDatabase.class,"word_database")
                            .addMigrations(MIGRATION_2_3)
                            .build();
                }
            }
        }
        return instance;
    }

    private static final Migration MIGRATION_2_3=new Migration(2,3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE word ADD COLUMN  results STRING NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE word ADD COLUMN is_visible BOOLEAN NOT NULL DEFAULT 0");
        }
    };
}
```

**(4)Converter**

如果Enity中有List或者自定义的对象需要存储，则需要写Converter通知Room如何存储这个数据。



自定义Converter类，通过<u>@TypeConverter</u>注解，自定义转换类型存储的方法。

```java
public class ResultConverter {
    @TypeConverter
    public static String convert(GankEnity.ResultsBean resultbean){
        Gson gson=new Gson();
        return gson.toJson(resultbean);
    }

    @TypeConverter
    public static GankEnity.ResultsBean revert(String result){
        Gson gson=new Gson();
        return gson.fromJson(result,GankEnity.ResultsBean.class);

    }
}
```

再在Database中添加<u>@TypeConverter</u>注解，并注明自定义类。

```java
@TypeConverters({ResultConverter.class, ListConverter.class})
```

### Repository

用作对数据的管理，让viewmodel管理逻辑更方便清晰，防止viewmodel太混乱，导致view与model层没有做到完全隔离。

![](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)

通过Repository的控制，决定从本地数据库拿取数据或是从网络请求数据。



### 综合MVVM的一个例子

![](https://github.com/xuesui/MVVMTest/blob/master/Screenshot_20190921-131343.jpg)

![](https://github.com/xuesui/MVVMTest/blob/master/Screenshot_20190921-131348.jpg)



inser按钮插入网络请求的内容并存入数据库，clear清空数据库。

每次进入页面直接显示数据库内容，同时进行网络请求更新数据并存入数据库，数据库利用**Livedata**感知到数据变化，从此更新页面。



Demo代码github中可见：[github](https://github.com/xuesui/MVVMTest)



接口api：[gank](http://gank.io/api/today)
