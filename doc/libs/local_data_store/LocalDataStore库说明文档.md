# LocalDataStore库说明文档

为啥会有写这个库的想法呢？主要问题有：

1、项目中到处都是SharedPreferences的调用，要统一处理很麻烦。

2、项目长期开发维护下，有必要分SharedPreferences文件来存储不同的数据了。

3、为项目架构设计而言，我们应该对接口编程，像这种本地数据持久化的方式，应该向上提供一个统一的接口，上层不管底层实现。

4、官方已经不推荐使用SharedPreferences了。



相关文档：
[DataStore-SharedPreference知识点](../../knowledges/DataStore-SharedPreference知识点.md)

# 用法
此库无需初始化，直接调用即可

```
        lifecycleScope.launch {
            val any = Any()
            LocalDataStore.saveObj(this@MainActivity,"objDb","objKey",any)
            LocalDataStore.saveInt(this@MainActivity,"intDb","intKey",100)
            LocalDataStore.getObg(this@MainActivity,"objDb","objKey",Any::class.java)
        }
```




# 实现思路

既然是提供给上层调用，我们先提供开放接口类出去。这里我底层使用的DataStore实现的，你们可以使用SharedPreferences来实现都可以。主要是讨论思路。

## 定义对外接口

```
object LocalDataStore {

    suspend fun saveString(context: Context, storeName: String, key: String, value: String) {}

    suspend fun saveFloat(context: Context, storeName: String, key: String, value: Float) {}

    suspend fun saveBoolean(context: Context, storeName: String, key: String, value: Boolean) {}

    suspend fun saveLong(context: Context, storeName: String, key: String, value: Long) {}

    suspend fun saveInt(context: Context, storeName: String, key: String, value: Int) {}

    suspend fun saveStringSet(
        context: Context,
        storeName: String,
        key: String,
        value: Set<String>
    ) {}

    suspend fun saveObj(context: Context, storeName: String, key: String, value: Any) {}

    suspend fun getString(
        context: Context,
        storeName: String,
        key: String,
        default: String
    ): String {}

    suspend fun getFloat(context: Context, storeName: String, key: String, default: Float): Float {}

    suspend fun getBoolean(
        context: Context,
        storeName: String,
        key: String,
        default: Boolean
    ): Boolean {}

    suspend fun getLong(context: Context, storeName: String, key: String, default: Long): Long {}

    suspend fun getInt(context: Context, storeName: String, key: String, default: Int): Int {}

    suspend fun getStringSet(
        context: Context,
        storeName: String,
        key: String,
        default: Set<String>
    ): Set<String> {}

    suspend fun <T> getObg(
        context: Context,
        storeName: String,
        key: String,
        type: TypeToken<T>
    ): T? {}

    suspend fun <T> getObg(
        context: Context,
        storeName: String,
        key: String,
        cls: Class<T>
    ): T? {}

    suspend fun removeDataByStringKey(context: Context, storeName: String, key: String) {}

    suspend fun removeDataByFloatKey(context: Context, storeName: String, key: String) {}

    suspend fun removeDataByBooleanKey(context: Context, storeName: String, key: String) {}

    suspend fun removeDataByLongKey(context: Context, storeName: String, key: String) {}

    suspend fun removeDataByIntKey(context: Context, storeName: String, key: String) {}

    suspend fun removeDataByStringSetKey(context: Context, storeName: String, key: String) {}

    suspend fun removeDataByObjKey(context: Context, storeName: String, key: String) {}

    suspend fun clearAllData(context: Context, storeName: String) {}
}
```

这是我目前定义的对外接口。



## DataStore的限制

如果你用过DataStore，你会发现它的实例对象的创建都是需要基于Context的扩展来实现的，这样的话，我们怎么去传入我们的name属性呢？

```
    private val Context.dataStore: DataStore<Preferences> by androidx.datastore.preferences.preferencesDataStore(
        name = "settings"
    )
```

直接实例化它呢？我们直接通过preferencesDataStore方法实例化试试，这样倒是可以动态传入name属性。

```
public fun preferencesDataStore(
    	name: String,
    	corruptionHandler: ReplaceFileCorruptionHandler<Preferences>? = null,
    	produceMigrations: (Context) -> List<DataMigration<Preferences>> = { listOf() },
    	scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
	): ReadOnlyProperty<Context, DataStore<Preferences>> {
    	return PreferenceDataStoreSingletonDelegate(name, corruptionHandler, produceMigrations, scope)
	}
```

结果给我们返回的是 ReadOnlyProperty\<Context, DataStore\<Preferences>> 的实例对象，而我们需要的是DataStore\<Preferences>的实例对象。那通过 ReadOnlyProperty\<Context, DataStore\<Preferences>> 的实例对象去取？

```
public fun interface ReadOnlyProperty<in T, out V> {
    public operator fun getValue(thisRef: T, property: KProperty<*>): V
}
```

有倒是有个getValue方法，但是property参数的实现把我人整麻了，所以DataStore卡在这里了。

我倒要看看这个参数是干嘛的？



## preferencesDataStore的底层实现

```
public fun preferencesDataStore(
    name: String,
    corruptionHandler: ReplaceFileCorruptionHandler<Preferences>? = null,
    produceMigrations: (Context) -> List<DataMigration<Preferences>> = { listOf() },
    scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
): ReadOnlyProperty<Context, DataStore<Preferences>> {
    return PreferenceDataStoreSingletonDelegate(name, corruptionHandler, produceMigrations, scope)
}

internal class PreferenceDataStoreSingletonDelegate internal constructor(
    private val name: String,
    private val corruptionHandler: ReplaceFileCorruptionHandler<Preferences>?,
    private val produceMigrations: (Context) -> List<DataMigration<Preferences>>,
    private val scope: CoroutineScope
) : ReadOnlyProperty<Context, DataStore<Preferences>> {

    private val lock = Any()

    @GuardedBy("lock")
    @Volatile
    private var INSTANCE: DataStore<Preferences>? = null


    /**
     * Gets the instance of the DataStore.
     *
     * @param thisRef must be an instance of [Context]
     * @param property not used
     */
    override fun getValue(thisRef: Context, property: KProperty<*>): DataStore<Preferences> {
        return INSTANCE ?: synchronized(lock) {
            if (INSTANCE == null) {
                val applicationContext = thisRef.applicationContext

                INSTANCE = PreferenceDataStoreFactory.create(
                    corruptionHandler = corruptionHandler,
                    migrations = produceMigrations(applicationContext),
                    scope = scope
                ) {
                    applicationContext.preferencesDataStoreFile(name)
                }
            }
            INSTANCE!!
        }
    }
}
```

代码不多，整体来说就是通过preferencesDataStore返回了一个PreferenceDataStoreSingletonDelegate的实例，而PreferenceDataStoreSingletonDelegate实现了ReadOnlyProperty接口。而我们调用的就是它的getValue方法。

而它的这个方法的注释上写的property没有使用，我人麻了。那怎么搞？它又不用，我们没办法实现！！！

## 自定义preferencesDataStore方法

既然它用不到，我们能不能直接给它干掉，说做就做！！

```
fun preferencesDataStore(
    context: Context,
    name: String,
    corruptionHandler: ReplaceFileCorruptionHandler<Preferences>? = null,
    produceMigrations: (Context) -> List<DataMigration<Preferences>> = { listOf() },
    scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
): DataStore<Preferences> {
    return MyPreferenceDataStoreSingletonDelegate(
        name,
        corruptionHandler,
        produceMigrations,
        scope
    ).getValue(context)
}

class MyPreferenceDataStoreSingletonDelegate internal constructor(
    private val name: String,
    private val corruptionHandler: ReplaceFileCorruptionHandler<Preferences>?,
    private val produceMigrations: (Context) -> List<DataMigration<Preferences>>,
    private val scope: CoroutineScope
) {

    private val lock = Any()

    @GuardedBy("lock")
    @Volatile
    private var instance: DataStore<Preferences>? = null

    fun getValue(thisRef: Context): DataStore<Preferences> {
        return instance ?: synchronized(lock) {
            if (instance == null) {
                val applicationContext = thisRef.applicationContext

                instance = PreferenceDataStoreFactory.create(
                    corruptionHandler = corruptionHandler,
                    migrations = produceMigrations(applicationContext),
                    scope = scope
                ) {
                    applicationContext.preferencesDataStoreFile(name)
                }
            }
            instance!!
        }
    }
}
```

这样改完之后，我们就可以通过这样的方式获取到dataStore对象，并且发起相关操作。

```
        val dataStore = preferencesDataStore(context = context, name = storeName)
        dataStore.edit { it[stringPreferencesKey(key)] = value }
```

这样，我们就解决了如何通过参数初始化dataStore的问题。





## 实现存取基本类型值的方法

那在上面的基础上面，我们就可以实现基本数据类型的存取值的方式

```
class BaseTypeDataStorageUtils {

    /********************* String *********************/
    suspend fun saveString(context: Context, storeName: String, key: String, value: String) {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        dataStore.edit { it[stringPreferencesKey(key)] = value }
    }

    suspend fun getString(context: Context, storeName: String, key: String, default: String) : String {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        return dataStore.data.map { it[stringPreferencesKey(key)] ?: default }.first()
    }

    /********************* Float *********************/
    suspend fun saveFloat(context: Context, storeName: String, key: String, value: Float) {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        dataStore.edit { it[floatPreferencesKey(key)] = value }
    }

    suspend fun getFloat(context: Context, storeName: String, key: String, default: Float) : Float {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        return dataStore.data.map { it[floatPreferencesKey(key)] ?: default }.first()
    }

    /********************* Boolean *********************/
    suspend fun saveBoolean(context: Context, storeName: String, key: String, value: Boolean) {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        dataStore.edit { it[booleanPreferencesKey(key)] = value }
    }

    suspend fun getBoolean(context: Context, storeName: String, key: String, default: Boolean) : Boolean {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        return dataStore.data.map { it[booleanPreferencesKey(key)] ?: default }.first()
    }

    /********************* Long *********************/
    suspend fun saveLong(context: Context, storeName: String, key: String, value: Long) {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        dataStore.edit { it[longPreferencesKey(key)] = value }
    }

    suspend fun getLong(context: Context, storeName: String, key: String, default: Long) : Long {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        return dataStore.data.map { it[longPreferencesKey(key)] ?: default }.first()
    }

    /********************* Int *********************/
    suspend fun saveInt(context: Context, storeName: String, key: String, value: Int) {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        dataStore.edit { it[intPreferencesKey(key)] = value }
    }

    suspend fun getInt(context: Context, storeName: String, key: String, default: Int) : Int {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        return dataStore.data.map { it[intPreferencesKey(key)] ?: default }.first()
    }

    /********************* StringSet *********************/
    suspend fun saveStringSet(context: Context, storeName: String, key: String, value: Set<String>) {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        dataStore.edit { it[stringSetPreferencesKey(key)] = value }
    }

    suspend fun getStringSet(context: Context, storeName: String, key: String, default: Set<String>) : Set<String> {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        return dataStore.data.map { it[stringSetPreferencesKey(key)] ?: default }.first()
    }

    /********************* remove key *********************/
    suspend fun <T> removeDataByKey(context: Context, storeName: String, key: Preferences.Key<T>) {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        dataStore.edit { it.remove(key) }
    }

    /********************* remove data store by name *********************/
    suspend fun clearAllData(context: Context, storeName: String) {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        dataStore.edit { it.clear() }
    }
}
```



## 实现存取引用类型指的方法

那这里我们还提供了个引用类型存取值的方法，那这个该这么实现呢？

其实我们可以将引用类型，格式化为jason字符串，当成String存取。

```
class RefTypeDataStorageUtils {

    /**
     * json格式数据转换器
     */
    private val gson = Gson()

    private fun <T> fromJson(jsonString: String, cls: Class<T>): T? {
        return gson.fromJson(jsonString, cls)
    }

    private fun <T> fromJson(jsonString: String, type: TypeToken<T>): T {
        val resultType = type.type
        return gson.fromJson<T>(jsonString, resultType)
    }

    private fun toJson(any: Any): String {
        return gson.toJson(any)
    }

    suspend fun saveObj(context: Context, storeName: String, key: String, value: Any) {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        val realValue = toJson(value)
        dataStore.edit { it[stringPreferencesKey(key)] = realValue }
    }

    suspend fun <T> getObj(
        context: Context,
        storeName: String,
        key: String,
        type: TypeToken<T>
    ): T? {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        val default = null
        val jsonStr = dataStore.data.map { it[stringPreferencesKey(key)] ?: default }.first()
        if (TextUtils.isEmpty(jsonStr)) {
            return null
        }
        return fromJson(jsonStr!!, type)
    }

    suspend fun <T> getObj(
        context: Context,
        storeName: String,
        key: String,
        cls: Class<T>
    ): T? {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        val default = null
        val jsonStr = dataStore.data.map { it[stringPreferencesKey(key)] ?: default }.first()
        if (TextUtils.isEmpty(jsonStr)) {
            return null
        }
        return fromJson(jsonStr!!, cls)
    }

    suspend fun <T> removeDataByObjKey(context: Context, storeName: String, key: String) {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        dataStore.edit { it.remove(stringPreferencesKey(key)) }
    }
}
```

## 整体实现

```
object LocalDataStore {

    private val baseTypeDataStorage = BaseTypeDataStorageUtils()

    private val refTypeDataStorage = RefTypeDataStorageUtils()

    suspend fun saveString(context: Context, storeName: String, key: String, value: String) {
        baseTypeDataStorage.saveString(context, storeName, key, value)
    }

    suspend fun saveFloat(context: Context, storeName: String, key: String, value: Float) {
        baseTypeDataStorage.saveFloat(context, storeName, key, value)
    }

    suspend fun saveBoolean(context: Context, storeName: String, key: String, value: Boolean) {
        baseTypeDataStorage.saveBoolean(context, storeName, key, value)
    }

    suspend fun saveLong(context: Context, storeName: String, key: String, value: Long) {
        baseTypeDataStorage.saveLong(context, storeName, key, value)
    }

    suspend fun saveInt(context: Context, storeName: String, key: String, value: Int) {
        baseTypeDataStorage.saveInt(context, storeName, key, value)
    }

    suspend fun saveStringSet(
        context: Context,
        storeName: String,
        key: String,
        value: Set<String>
    ) {
        baseTypeDataStorage.saveStringSet(context, storeName, key, value)
    }

    suspend fun saveObj(context: Context, storeName: String, key: String, value: Any) {
        refTypeDataStorage.saveObj(context, storeName, key, value)
    }

    suspend fun getString(
        context: Context,
        storeName: String,
        key: String,
        default: String
    ): String {
        return baseTypeDataStorage.getString(context, storeName, key, default)
    }

    suspend fun getFloat(context: Context, storeName: String, key: String, default: Float): Float {
        return baseTypeDataStorage.getFloat(context, storeName, key, default)
    }

    suspend fun getBoolean(
        context: Context,
        storeName: String,
        key: String,
        default: Boolean
    ): Boolean {
        return baseTypeDataStorage.getBoolean(context, storeName, key, default)
    }

    suspend fun getLong(context: Context, storeName: String, key: String, default: Long): Long {
        return baseTypeDataStorage.getLong(context, storeName, key, default)
    }

    suspend fun getInt(context: Context, storeName: String, key: String, default: Int): Int {
        return baseTypeDataStorage.getInt(context, storeName, key, default)
    }

    suspend fun getStringSet(
        context: Context,
        storeName: String,
        key: String,
        default: Set<String>
    ): Set<String> {
        return baseTypeDataStorage.getStringSet(context, storeName, key, default)
    }

    suspend fun <T> getObg(
        context: Context,
        storeName: String,
        key: String,
        type: TypeToken<T>
    ): T? {
        return refTypeDataStorage.getObj(context, storeName, key, type)
    }

    suspend fun <T> getObg(
        context: Context,
        storeName: String,
        key: String,
        cls: Class<T>
    ): T? {
        return refTypeDataStorage.getObj(context, storeName, key, cls)
    }

    suspend fun removeDataByStringKey(context: Context, storeName: String, key: String) {
        baseTypeDataStorage.removeDataByKey(context, storeName, stringPreferencesKey(key))
    }

    suspend fun removeDataByFloatKey(context: Context, storeName: String, key: String) {
        baseTypeDataStorage.removeDataByKey(context, storeName, floatPreferencesKey(key))
    }

    suspend fun removeDataByBooleanKey(context: Context, storeName: String, key: String) {
        baseTypeDataStorage.removeDataByKey(context, storeName, booleanPreferencesKey(key))
    }

    suspend fun removeDataByLongKey(context: Context, storeName: String, key: String) {
        baseTypeDataStorage.removeDataByKey(context, storeName, longPreferencesKey(key))
    }

    suspend fun removeDataByIntKey(context: Context, storeName: String, key: String) {
        baseTypeDataStorage.removeDataByKey(context, storeName, intPreferencesKey(key))
    }

    suspend fun removeDataByStringSetKey(context: Context, storeName: String, key: String) {
        baseTypeDataStorage.removeDataByKey(context, storeName, stringSetPreferencesKey(key))
    }

    suspend fun removeDataByObjKey(context: Context, storeName: String, key: String) {
        baseTypeDataStorage.removeDataByKey(context, storeName, stringSetPreferencesKey(key))
    }

    suspend fun clearAllData(context: Context, storeName: String) {
        baseTypeDataStorage.clearAllData(context, storeName)
    }
}
```

这里的优化点有很多，比如：

- 使用Kotlin扩展方便调用
- 存储引用类型的方式可以换位 Proto DataStore
- 更加细致化的封装

等等。





# 更新

## 2025/03/07

在实际使用过程中发现会直接崩溃。

```
            val storeName = "localDataStore"
            val stringKey = "stringKey"
            LocalDataStore.saveString(
                this@LocalDataStoreActivity,
                storeName,
                stringKey,
                "我是一个值"
            )
            val stringValue = LocalDataStore.getString(
                this@LocalDataStoreActivity,
                storeName,
                stringKey,
                "我是一个默认值"
            )
            Log.d(TAG, "testLocalDataStoreFunction: stringValue = $stringValue")
```

错误日志为：

```
Process: cc.fastcv.libs.app, PID: 23664
java.lang.IllegalStateException: There are multiple DataStores active for the same file:
   /data/user/0/cc.fastcv.libs.app/files/datastore/localDataStore.preferences_pb. You should either maintain your DataStore as a singleton or confirm that there is no two DataStore's active on the same file (by confirming that the scope is cancelled).
			at androidx.datastore.core.okio.OkioStorage.createConnection(OkioStorage.kt:65)
      at androidx.datastore.core.DataStoreImpl$storageConnectionDelegate$1.invoke(DataStoreImpl.kt:189)
      at androidx.datastore.core.DataStoreImpl$storageConnectionDelegate$1.invoke(DataStoreImpl.kt:188)
      at kotlin.SynchronizedLazyImpl.getValue(LazyJVM.kt:74)
      at androidx.datastore.core.DataStoreImpl.getStorageConnection$datastore_core_release(DataStoreImpl.kt:191)
      at androidx.datastore.core.DataStoreImpl$coordinator$2.invoke(DataStoreImpl.kt:192)
      at androidx.datastore.core.DataStoreImpl$coordinator$2.invoke(DataStoreImpl.kt:192)
      at kotlin.SynchronizedLazyImpl.getValue(LazyJVM.kt:74)
```

总的意思就是说，不能给同一个存储文件创建两个实例对象去持有。那为什么会有这个问题呢？我们看下创建的地方

```
fun preferencesDataStore(
    context: Context,
    name: String,
    corruptionHandler: ReplaceFileCorruptionHandler<Preferences>? = null,
    produceMigrations: (Context) -> List<DataMigration<Preferences>> = { listOf() },
    scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
): DataStore<Preferences> {
    return MyPreferenceDataStoreSingletonDelegate(
            name,
            corruptionHandler,
            produceMigrations,
            scope
        ).getValue(context)
}
```

原来每次我们都是创建了一个新的MyPreferenceDataStoreSingletonDelegate对象，而这个对象内部持有一个DataStore对象，这样每次都是重新创建了，所以会报这个错。

既然找到问题了，那解决起来也就很简单了。稍微修改

```
private val dataStoreMap = mutableMapOf<String,DataStore<Preferences>>()

fun preferencesDataStore(
    context: Context,
    name: String,
    corruptionHandler: ReplaceFileCorruptionHandler<Preferences>? = null,
    produceMigrations: (Context) -> List<DataMigration<Preferences>> = { listOf() },
    scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
): DataStore<Preferences> {
    var dataStore = dataStoreMap[name]
    if (dataStore == null) {
        dataStore = MyPreferenceDataStoreSingletonDelegate(
            name,
            corruptionHandler,
            produceMigrations,
            scope
        ).getValue(context)
        dataStoreMap[name] = dataStore
    }
    return dataStore
}
```

这样就OK了。
