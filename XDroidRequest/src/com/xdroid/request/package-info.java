/**
 * usage
 * @author Robin
 * @since 2015-05-22 14:16:49
 *
 */
package com.xdroid.request;
/*

**XDroidRequest** 是一款网络请求框架,它的功能也许会适合你。这是本项目的第三版了，前两版由于扩展性问题一直不满意,思考来思考去还是觉得Google的Volley的扩展性最强,所以有了这个第三版

### Provide ###

- 适配 Android6.0 ,不再使用HttpClient相关API
- 一行代码发送请求，提供多种回调函数供选择,
- 支持8种网络请求方式 GET，POST，PUT，DELETE，HEAD，OPTIONS，TRACE，PATCH
- 支持请求的优先级设置，优先级高的将先于优先级低的发送请求
- 支持取消请求，可以取消当前发送请求的“请求”(可自定义取消请求的依据条件)，也可以取消请求队列中还未发送的请求
- 支持多请求并发，多个请求同时发送,底层使用固定数量线程池,可设置线程池的大小
- 支持重复请求的判断，当有重复的请求将挂起，等待第一个请求完成后，挂起的请求使用已经请求完毕的缓存，如果未开启缓存，则会继续请求网络
- 支持请求失败重试，默认重试2次，重试超时时间会递增，递增速率可设置，默认为1倍递增
- 支持多文件与大文件上传，可以与参数一起发送至服务器,提供上传进度回调
- 支持大文件下载，提供下载进度回调
- 支持发送JSON数据
- 自动网络判定，可设置此时是否显示缓存数据
- 请求结果自动解析，可泛型任何Java bean，默认实现了GSON解析，可自定义
- 多种错误类型判定
- 扩展性强，可自定义发送请求方式与解析请求结果
- 支持强大的缓存控制
- 支持缓存配置，可配置磁盘缓存路径，磁盘缓存最大值，磁盘缓存当前占有大小，磁盘缓存清理
内存缓存最大值大小，内存缓存清理
- 支持缓存管理与控制，包括本地请求缓存一系列信息查询以及对缓存的手动操作

### About cache ###
- XDroidRequest使用了二级缓存，内存缓存与磁盘缓存，内存缓存使用LruCache,磁盘缓存使用DiskLruCache
- setShouldCache(true) ，一个开关控制是否使用缓存的功能
- setUseCacheDataAnyway(false)， 是否总是使用缓存，这个开关开启后，将每次首先从内存和本地查找缓存，有的话直接使用缓存，
请求会在后台执行，完成后会更新缓存。如果没有缓存将直接进行网络请求获取，完成后会更新缓存.
- setUseCacheDataWhenRequestFailed(true) ，是否在请求失败后使用缓存数据，无网络属于请求失败，可以保证即使没有网络，或者
请求也有数据展示.
- setUseCacheDataWhenTimeout(true) ，是否在请求超时后直接使用缓存，这里的超时时间并不是网络请求的超时时间，而是我们
设定一个时间，超过这个时间后，不管请求有没有完成都直接使用缓存，后台的请求完成后会自动更新缓存
- setUseCacheDataWhenUnexpired(true)，是否使用缓存当缓存未过期的时候，这个开关也是经常开启的开关，每个缓存都会对应一个过期时间，先从内存查找缓存，没有的话再从磁盘查找，有缓存且过期的话，将直接使用缓存数据，当过期之后会进行网络请求，请求完成后会更新内存缓存与磁盘。没有缓存将直接进行网络请求，请求完成后会更新内存与磁盘缓存
- setRetryWhenRequestFailed(true) ，是否进行重试，当请求失败的时候，默认开启，重试2次，不需要重试功能的话可关闭
- setNeverExpired(false); 设置缓存是否永不过期 


多文件上传PHP代码：
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


*/