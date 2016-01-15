## XDroidRequest(README正在更新中) ##

**XDroidRequest** 是一款网络请求框架,它的功能也许会适合你。这是本项目的第三版了，前两版由于扩展性问题一直不满意,思考来
思考去还是觉得Google的Volley的扩展性最强,于是借鉴了Volley的责任链模式,所以有了这个第三版.

### Provide ###
    
    1 适配 Android 6.0 ,不再使用HttpClient相关API
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
    17 支持缓存配置，可配置磁盘缓存路径，磁盘缓存最大值，磁盘缓存当前占有大小，磁盘缓存清理
    内存缓存最大值大小，内存缓存清理
    18 支持缓存管理与控制，包括本地请求缓存一系列信息查询以及对缓存的手动操作

### About cache ###
    1 XDroidRequest使用了二级缓存，内存缓存与磁盘缓存，内存缓存使用LruCache,磁盘缓存使用DiskLruCache
    2 setShouldCache(true) ，一个开关控制是否使用缓存的功能
    3 setUseCacheDataAnyway(false)， 是否总是使用缓存，这个开关开启后，将每次首先从内存和本地查找缓存，有的话直接使用缓存，
    请求会在后台执行，完成后会更新缓存。如果没有缓存将直接进行网络请求获取，完成后会更新缓存.
    4 setUseCacheDataWhenRequestFailed(true) ，是否在请求失败后使用缓存数据，无网络属于请求失败，可以保证即使没有网络，或者
    请求也有数据展示.
    5 setUseCacheDataWhenTimeout(true) ，是否在请求超时后直接使用缓存，这里的超时时间并不是网络请求的超时时间，而是我们
    设定一个时间，超过这个时间后，不管请求有没有完成都直接使用缓存，后台的请求完成后会自动更新缓存
    6 setUseCacheDataWhenUnexpired(true)，是否使用缓存当缓存未过期的时候，这个开关也是经常开启的开关，每个缓存都会对应一个过期
    时间，先从内存查找缓存，没有的话再从磁盘查找，有缓存且过期的话，将直接使用缓存数据，当过期之后会进行网络请求，请求完成后会更新
    内存缓存与磁盘。没有缓存将直接进行网络请求，请求完成后会更新内存与磁盘缓存
    7 setRetryWhenRequestFailed(true) ，是否进行重试，当请求失败的时候，默认开启，重试2次，不需要重试功能的话可关闭
    8 setNeverExpired(false); 设置缓存是否永不过期 



### Here is the sample ###

[Download demo.apk](https://github.com/robinxdroid/XDroidRequest/blob/master/XDroidRequestExample.apk?raw=true)

### Screenshot ###

![](https://raw.githubusercontent.com/robinxdroid/XDroidRequest/master/1.jpg) 
![](https://raw.githubusercontent.com/robinxdroid/XDroidRequest/master/2.jpg) 

### Usage ###

**1.初始化，应用启动的时候进行，主要初始化缓存的路径等信息**

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
② POST请求

③ 发送JSON字符串参数

④上传文件

⑤下载文件

⑥关于回调
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
			 */
			@Override
			public void onParseNetworkResponse(Request<?> request, NetworkResponse networkResponse, String result) {
				CLog.i("GET请求网络数据解析完成");
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
⑦自动解析

⑧缓存配置

⑨请求配置

⑩原始发送请求方式

⑪设置优先级

⑫自定义解析方式

⑬自定义请求方式

⑭取消请求

⑮关闭请求

⑯

⑰

⑱

⑲

⑳


