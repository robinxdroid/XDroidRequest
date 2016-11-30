## XDroidRequest ##
##[DEPRECATED]最新代码见[https://github.com/robinxdroid/HttpRequest](https://github.com/robinxdroid/HttpRequest)##

**XDroidRequest** 
是一款网络请求框架,它的功能也许会适合你。这是本项目的第三版了，前两版由于扩展性问题一直不满意,思考来
思考去还是觉得Google的Volley的扩展性最强,于是借鉴了Volley的责任链模式,所以有了这个第三版.

### Provide ###
    
    1 不再使用HttpClient相关API，因为Android 6.0删除Apache HttpClient相关API,虽然我们可以自己引用,但是既然谷歌删除，还是跟着老大走吧，
    所以本项目没有任何HttpClient相关API.
    2 一行代码发送请求，提供多种回调函数供选择,
    3 支持8种网络请求方式 GET，POST，PUT，DELETE，HEAD，OPTIONS，TRACE，PATCH
    4 支持请求的优先级设置，优先级高的将先于优先级低的发送请求
    5 支持取消请求，可以取消当前已发送的请求(可自定义取消请求的依据条件)，也可以取消请求队列中还未发送的请求
    6 支持多请求并发，多个请求同时发送,底层使用固定数量线程池,可设置线程池的大小
    7 支持重复请求的判断，当有重复的请求将挂起，等待第一个请求完成后，挂起的请求使用已经请求完毕的缓存，如果未开启缓存，则会继续请求网络
    8 支持请求失败重试，默认重试2次，重试超时时间会递增，递增速率可设置，默认为1倍递增
    9 支持多文件与大文件上传，可以与参数一起发送至服务器,提供上传进度回调
    10 支持大文件下载，提供下载进度回调
    11 支持发送JSON数据
    12 自动网络判定，可设置此时是否显示缓存数据
    13 请求结果自动解析，可泛型任何JAVA BEAN，默认实现了GSON解析，可自定义
    14 多种错误类型判定
    15 扩展性强，可自定义发送请求方式与解析请求结果
    16 支持强大的缓存控制
    17 支持缓存配置，可配置磁盘缓存路径，磁盘缓存最大值，磁盘缓存当前占有大小，内存缓存最大值大小
    18 支持缓存管理与控制，包括本地请求缓存一系列信息查询以及对缓存的手动操作

### About cache ###
    1 XDroidRequest使用了二级缓存，内存缓存与磁盘缓存，内存缓存使用LruCache,磁盘缓存使用DiskLruCache
    2 setShouldCache(true) ，一个开关控制是否使用缓存的功能
    3 setUseCacheDataAnyway(false)， 是否总是使用缓存，这个开关开启后，将每次首先从内存和本地查找缓存，有的话直接使用缓存，
    请求会在后台执行，完成后会更新缓存。如果没有缓存将直接进行网络请求获取，完成后会更新缓存.
    4 setUseCacheDataWhenRequestFailed(true) ，是否在请求失败后使用缓存数据，无网络属于请求失败，可以保证即使没有网络，或者
    请求失败也有数据展示.
    5 setUseCacheDataWhenTimeout(true) ，是否在请求超时后直接使用缓存，这里的超时时间并不是网络请求的超时时间，而是我们
    设定一个时间，超过这个时间后，不管请求有没有完成都直接使用缓存，后台的请求完成后会自动更新缓存
    6 setUseCacheDataWhenUnexpired(true)，是否使用缓存当缓存未过期的时候，这个开关也是经常开启的开关，每个缓存都会对应一个过期
    时间，先从内存查找缓存，没有的话再从磁盘查找，有缓存且未过期的话，将直接使用缓存数据，当过期之后会进行网络请求，请求完成后会更新
    内存缓存与磁盘。没有缓存将直接进行网络请求，请求完成后会更新内存与磁盘缓存
    7 setRetryWhenRequestFailed(true) ，是否进行重试，当请求失败的时候，默认开启，重试2次，不需要重试功能的话可关闭
    8 setNeverExpired(false); 设置缓存是否永不过期 



### Here is the sample ###

[Download demo.apk](https://github.com/robinxdroid/XDroidRequest/blob/master/XDroidRequestExample.apk?raw=true)

### Screenshot ###

![](https://raw.githubusercontent.com/robinxdroid/XDroidRequest/master/1.jpg) 
![](https://raw.githubusercontent.com/robinxdroid/XDroidRequest/master/2.jpg) 

### Usage ###

**1.初始化，应用启动的时候进行，主要初始化缓存的路径等信息.有同学认为使用框架在Application中初始化是个
不太好的实现，会有种麻烦多余的感觉，此框架设计之初并不需要这样的操作，因为框架内部涉及到网络判定此类功能
需要传入Context，在“每发起一个请求传入Context”与“初始化时传入Context”两种方式中，选择了后者**

```java
XRequest.initXRequest(getApplicationContext());
```

**2.发起请求**

① GET请求
```java
	    /**
         * 简单的Get请求
		 * @param mRequestTag 请求的tag，可根据此tag取消请求
		 * @param url 请求地址
		 * @param OnRequestListener 请求结果回调
		 */
		XRequest.getInstance().sendGet(mRequestTag, url, new OnRequestListenerAdapter<String>() {
			@Override
			public void onDone(Request<?> request, Map<String, String> headers, String result, DataType dataType) {
				super.onDone(request, headers, result, dataType);
			}
		});
		 
```

请求的发起流程就这两步，真正的项目中可能会有许多其他的设置，请见下方

**3.请求回调**：
  回调Callback:OnRequestListener,此回调包含了很多监听，有些回调可能你不要重写，那么只需传入OnRequestListenerAdapter
  选择性复写需要的回调函数即可，这里是OnRequestListener的每个回调函数的注释：
  
  ```java
XRequest.getInstance().sendGet(mRequestTag, url, cacheKey, params, new OnRequestListener<String>() {

			/**
			 * 请求前准备回调
			 * 运行线程：主线程
			 * @param request 当前请求对象
			 */
			@Override
			public void onRequestPrepare(Request<?> request) {
				Toast.makeText(context, "GET请求准备", Toast.LENGTH_SHORT).show();
				
				CLog.i("GET请求准备");
			}

			/**
			 * 请求完成回调
			 * 运行线程：主线程
			 * @param request 当前请求对象
			 * @param headers 请求结果头文件Map集合
			 * @param result 请求结果泛型对象
			 */
			@Override
			public void onRequestFinish(Request<?> request, Map<String, String> headers, String result) {
				Toast.makeText(context, "GET请求结果获取成功", Toast.LENGTH_SHORT).show();
				CLog.i("GET请求结果获取成功");
			}

			/**
			 * 请求失败回调
			 * 运行线程：主线程
			 * @param request 当前请求对象
			 * @param httpException 错误类对象，包含错误码与错误描述
			 */
			@Override
			public void onRequestFailed(Request<?> request, HttpException httpException) {
				Toast.makeText(context, "GET请求结果失败", Toast.LENGTH_SHORT).show();
				CLog.i("GET请求结果失败");
			}

			/**
			 * 请求失败重试回调
			 * 运行线程：主线程
			 * @param request 当前请求对象
			 * @param currentRetryCount 当前重试次数
			 * @param previousError 上一个错误类对象，包含错误码与错误描述
			 */
			@Override
			public void onRequestRetry(Request<?> request, int currentRetryCount, HttpException previousError) {
				Toast.makeText(context, "获取信息失败，系统已经为您重试" + currentRetryCount+"次", Toast.LENGTH_SHORT).show();
				
				CLog.i("GET请求结果失败，正在重试,当前重试次数：" + currentRetryCount);
			}
			
			/**
			 * 下载进度回调
			 * 运行线程：子线程
			 * @param request 当前请求对象
			 * @param transferredBytesSize 当前下载大小
			 * @param totalSize 总大小
			 * 
			 */
			@Override
			public void onRequestDownloadProgress(Request<?> request, long transferredBytesSize, long totalSize) {
				CLog.i("onRequestDownloadProgress current：%d , total : %d" ,transferredBytesSize,totalSize);
			}
			
			/**
			 * 上传进度回调
			 * 运行线程：子线程
			 * @param request 当前请求对象
			 * @param transferredBytesSize 当前写入进度
			 * @param totalSize 总进度
			 * @param currentFileIndex 当前正在上传的是第几个文件
			 * @param currentFile 当前正在上传的文件对象
			 * 
			 */
			@Override
			public void onRequestUploadProgress(Request<?> request, long transferredBytesSize, long totalSize, int currentFileIndex,
					File currentFile) {
				CLog.i("onRequestUploadProgress current：%d , total : %d" ,transferredBytesSize,totalSize);
			}

			/**
			 * 缓存数据加载完成回调
			 * 运行线程：主线程
			 * @param request 当前请求对象
			 * @param headers 缓存的头信息Map集合
			 * @param result 缓存的数据结果对象
			 */
			@Override
			public void onCacheDataLoadFinish(Request<?> request, Map<String, String> headers, String result) {
				Toast.makeText(context, "GET请求缓存加载成功", Toast.LENGTH_SHORT).show();
				CLog.i("GET请求缓存加载成功");
			}
			
			/**
			 * 解析网络数据回调，请求完成后，如果需要做耗时操作（比如写入数据库）可在此回调中进行，不会阻塞UI
			 * 运行线程：子线程
			 * @param request 当前请求对象
			 * @param networkResponse 网络请求结果对象，包含byte数据流与头信息等
			 * @param result 解析byte数据流构建的对象
			 * @return 是否允许缓存，“true”允许缓存 “fale”反之，默认为true，请求解析完成后，可以根据情况自己指定缓存条件
			 */
			@Override
			public boolean onParseNetworkResponse(Request<?> request, NetworkResponse networkResponse, String result) {
				CLog.i("GET请求网络数据解析完成");
			        return true
			}

			/**
			 * 此请求最终完成回调，每次请求只会调用一次，无论此请求走的缓存数据还是网络数据，最后交付的结果走此回调
			 * 运行线程：主线程
			 * @param request 当前请求对象
			 * @param headers 最终交付数据的头信息
			 * @param result 最终交付的请求结果对象
			 * @param dataType 最终交付的数据类型枚举，网络数据/缓存数据
			 */
			@Override
			public void onDone(Request<?> request, Map<String, String> headers, String result, DataType dataType) {
				Toast.makeText(context, "GET请求完成", Toast.LENGTH_SHORT).show();
			}

		});
```

**4.缓存配置**：


(1)初始化的时候如果想要指定缓存路径，大小等信息，可参照如下代码
```java
public class App extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		configXReqeustCache();
		
	}

	@SuppressLint("SdCardPath")
	private void configXReqeustCache() {
		//磁盘缓存路径
		File DISK_CACHE_DIR_PATH = new File("/sdcard/xrequest/diskcache");
		//磁盘缓存最大值
		int DISK_CACHE_MAX_SIZE = 30*1024*1024;
		
		//XRequest.initXRequest(getApplicationContext());
		
		XRequest.initXRequest(getApplicationContext(), DISK_CACHE_MAX_SIZE, DISK_CACHE_DIR_PATH);
	}
}
```
(2)查找当前缓存数据占用的空间
```java
long diskCacheCurrentSize = RequestCacheManager.getInstance().getAllDiskCacheSize();
```
(3)查找缓存路径
```java
String diskCacheDir = RequestCacheManager.getInstance().getDiskCacheDirectory().getPath();
```
(4)查询当前缓存最大值
```java
long diskCacheMaxSize = RequestCacheManager.getInstance().getDiskCacheMaxSize();
```
(5)清除所有缓存
```java
RequestCacheManager.getInstance().deleteAllDiskCacheData();
```

**5.请求配置**：

在发送请求的时候，有的重载函数需要传入一个RequestCacheConfig对象（见Demo项目），不需要传入此对象的重载函数内部传入的是默认的
RequestCacheConfig对象，通过RequestCacheConfig对象控制缓存于网络数据等，下面是默认的RequestCacheConfig配置
```java
public static RequestCacheConfig buildDefaultCacheConfig() {
RequestCacheConfig cacheConfig=new RequestCacheConfig();
	cacheConfig.setShouldCache(true);  //开启缓存
	cacheConfig.setUseCacheDataAnyway(false);  //关闭总是优先使用缓存
	cacheConfig.setUseCacheDataWhenRequestFailed(true); //开启请求失败使用缓存
	cacheConfig.setUseCacheDataWhenTimeout(false); //关闭超时使用缓存
	cacheConfig.setUseCacheDataWhenUnexpired(true);  //开启当缓存未过期时使用缓存
	cacheConfig.setRetryWhenRequestFailed(true); //开启请求失败重试
	cacheConfig.setNeverExpired(false); //关闭缓存永不过期
		
	TimeController timeController=new TimeController();
	timeController.setExpirationTime(DEFAULT_EXPIRATION_TIME); //设置缓存的过期时间
	timeController.setTimeout(DEFAULT_TIMEOUT); //设置缓存超时时间，对应“setUseCacheDataWhenTimeout”函数的超时时间
	cacheConfig.setTimeController(timeController); //把时间控制器设置给RequestCacheConfig
		
	return cacheConfig;
}

```
每次请求如果需要重新指定配置，自己构造这样一个对象传入即可


**6.原始发送请求方式**：

XRequest其实是使用装饰者模式，对一系列请求步骤进行了封装，目的是为了更简单的使用，如果有复杂的需求，需要更高的自由度的话，
可以参考如下发送请求代码
```java
MultipartGsonRequest<Bean> request = new MultipartGsonRequest<T>(cacheConfig, url, cacheKey,onRequestListener);
request.setRequestParams(params);
request.setHttpMethod(HttpMethod.POST);
request.setTag(tag);

XRequest.getInstance().addToRequestQueue(request);
```
**7.设置优先级**：

优先级分为4档，IMMEDIATE > HIGH > NORMAL > LOW ,优先级越高的请求优先进行
```java
request.setPriority(Priority.NORMAL);
```

**8.自定义解析方式**：

如果需要对请求的结果进行自定义，只需继承MultipartRequest<T>，重写parseNetworkResponse函数即可
如下是把请求结果转换成String字符串
```java
public class StringRequest extends MultipartRequest<String> {
	
	public StringRequest() {
		super();
	}

	public StringRequest(RequestCacheConfig cacheConfig, String url, String cacheKey,
			OnRequestListener<String> onRequestListener) {
		super(cacheConfig, url, cacheKey, onRequestListener);
	}

	@Override
	public Response<String> parseNetworkResponse(NetworkResponse response) {
		   return Response.success(new String(response.data), response.headers);
	}

}
```


**9.自定义请求方式**：

自定义请求方式，这个需要你自己构造请求体，以及怎么传入参数相关逻辑，只需继承Request<T>，重写buildBody(HttpURLConnection connection)

可参照MultipartRequest<T>实现

有的兄弟喜欢okhttp,如果你想传输层使用okhttp等请求框架实现，可以参照HurlStack实现HttpStack，然后在初始化Network对象的地方传入你自定义的HttpStack
实现类，不过这个暂时要求你自行修改源码,后面在维护中将会加入HttpStack切换功能

**10.其他设置**：


(1).取消请求
```java
// 取消指定请求(两种方式都可以)
 // request.cancel();
 XRequest.getInstance().cancelRequest(request);

// 取消队列中的所有相同tag请求(两种方式都可以)
 //request.getRequestQueue().cancelAll(mRequestTag);
 XRequest.getInstance().cancelAllRequestInQueueByTag(mRequestTag);
```
(2).关闭请求
```java
XRequest.getInstance().shutdown(); 
```
(3).Log控制

开启Log:
```java
Clog.openLog();
```
关闭Log:
```java
Clog.closeLog();
```

**11.请求示例**：

(1)POST请求
```java
String url = "http://apis.baidu.com/heweather/weather/free";
RequestParams params = new RequestParams();
params.putHeaders("apikey", "可以到apistore申请");
params.putParams("city", "hefei");

XRequest.getInstance().sendPost(mRequestTag, url,  params, new OnRequestListenerAdapter<String>() {

	@Override
	public void onDone(Request<?> request, Map<String, String> headers, String result, DataType dataType) {
		super.onDone(request, headers, result, dataType);
	}
});
```

(2)发送JSON字符串参数
```java
RequestParams params = new RequestParams();
params.putParams(
		"{\"uid\":863548,\"stickys\":[{\"id\":29058,\"iid\":0,\"content\":\"内容\",\"color\":\"green\",\"createtime\":\"2015-04-16 16:26:17\",\"updatetime\":\"2015-04-16 16:26:17\"}]}");
XRequest.getInstance().sendPost(mRequestTag, url, params, new OnRequestListenerAdapter<String>() {
	@Override
	public void onDone(Request<?> request, Map<String, String> headers, String result, DataType dataType) {
		super.onDone(request, headers, result, dataType);
	}
});
```
(3)上传文件
```java
String url = "http://192.168.1.150/upload_multi.php";
RequestParams params = new RequestParams();
params.put("file[0]", new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "app-debug.apk"));
params.put("file[1]", new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "photoview.apk"));
params.putParams("file_name", "上传的文件名称");

XRequest.getInstance().upload(mRequestTag, url,  params, new OnRequestListenerAdapter<String>() {

	
			
	@Override
	public void onRequestUploadProgress(Request<?> request, long transferredBytesSize, long totalSize, int currentFileIndex,
			File currentFile) {
		CLog.i("正在上传第%s个文件,当前进度：%d , 总大小 : %d" ,currentFileIndex,transferredBytesSize,totalSize);
		
		mUploadProgressBar.setMax((int) totalSize);
		mUploadProgressBar.setProgress((int) transferredBytesSize);
	}
	@Override
	public void onDone(Request<?> request, Map<String, String> headers, String response, DataType dataType) {
		Toast.makeText(context, "请求完成", Toast.LENGTH_SHORT).show();
	}


});
```
测试了上传百兆以上文件无压力，如果你想测试多文件上传，下面的PHP多文件上传代码供参考。要注意的是PHP默认上传2M以内文件，需要自己改下
配置文件，网上很多，搜索即可
```java
<?php
 foreach($_FILES['file']['error'] as $k=>$v)
 {
    $uploadfile = './upload/'. basename($_FILES['file']['name'][$k]);
    if (move_uploaded_file($_FILES['file']['tmp_name'][$k], $uploadfile)) 
    {
        echo "File : ", $_FILES['file']['name'][$k] ," is valid, and was successfully uploaded.\n";
    }

    else 
    {
        echo "Possible file : ", $_FILES['file']['name'][$k], " upload attack!\n";
    }   

 }

 echo "成功接收附加字段:". $_POST['file_name'];

?>
```
(4)下载文件
```java
String url = "http://192.168.1.150/upload/xiaokaxiu.apk";
String downloadPath = "/sdcard/xrequest/download";
String fileName = "test.apk";
XRequest.getInstance().download(mRequestTag, url, downloadPath,fileName, new OnRequestListenerAdapter<File>() {
	@Override
	public void onRequestDownloadProgress(Request<?> request, long transferredBytesSize, long totalSize) {
		CLog.i("正在下载， 当前进度：%d , 总大小 : %d" ,transferredBytesSize,totalSize);
		mDownloadProgressBar.setMax((int) totalSize);
		mDownloadProgressBar.setProgress((int) transferredBytesSize);
	}
	@Override
	public void onDone(Request<?> request, Map<String, String> headers, File result, DataType dataType) {
		CLog.i("下载完成 : %s",result != null?result.toString():"获取File为空");
	}
});
```

(5)自动解析
```java
String url = "http://apis.baidu.com/apistore/aqiservice/citylist";
RequestParams params = new RequestParams();
params.putHeaders("apikey", "可以到apistore申请");
XRequest.getInstance().sendPost(mRequestTag, url, params, new OnRequestListenerAdapter<CityRootBean<CityBean>>() {

	@Override
	public void onDone(Request<?> request, Map<String, String> headers, CityRootBean<CityBean> result,
			DataType dataType) {
		CLog.i("Bean信息:" + (result == null ? "null" : result.toString()));
	}
});
```



**12.更多**：

欢迎自行探索Y(^_^)Y

#Thanks
[DiskLruCache](https://github.com/JakeWharton/DiskLruCache)<br>
[android-volley](https://github.com/mcxiaoke/android-volley)
#About me
Email:735506404@robinx.net<br>
Blog:[www.robinx.net](http://www.robinx.net)


