# storm 

## Introduction

### Storm 是什么？

Storm 是一个分布式实时大数据处理系统，可以帮助我们方便地处理海量数据，具有高可靠、高容错、高扩展的特点。

Storm 是流式框架，有很高的数据吞吐能力，Strom 本身是无状态的，通过 ZooKeeper 管理分布式集群环境和集群状态。

### 应用场景

应用 Storm 的场景例如：

* 日志处理

监控系统中的事件日志，使用 Storm 检查每条日志信息，把符合匹配规则的消息保存到数据库。

* 电商商品推荐

后台需要维护每个用户的兴趣点，主要基于用户的历史行为、查询、点击、地理信息等信息获得，其中有很多实时数据，可以使用 Storm 进行处理，在此基础上进行精准的商品推荐和放置广告。

### Storm与Hadoop的关系


Storm 与 Hadoop 都用来处理大数据，那么它们的关系是怎样的呢？

 
Hadoop 是强大的大数据处理系统，但是在实时计算方面不够擅长；Storm的核心功能就是提供强大的实时处理能力，但没有涉及存储；所以 Storm 与 Hadoop 即不同也互补。


它们的最主要的区别例如：
 

1. Storm 是实时流处理模式，Hadoop 是批处理模式；

2. Storm 就像一条川流不息的河流，只要不是意外或者人为停止，它就会一直运行，Hadoop 是在需要时执行 MapReduce 任务，执行完成后停止；

3. 在处理时间上，Storm 每秒可以处理数万条消息，HDFS+MapReduce 处理大量数据时通常需要几分钟到几小时。

## 知识点


### 组件关系

一个storm topology运行起来之后, 会在supervisor 机器上启动一些进程来运行spout和bolt实例. 

如果一个topology里面一共有一个spout, 一个bolt。 其中spout的parallelism是2, bolt的parallelism是4, 那么我们可以把这个topology的总工作量看成是6， 那么一共有6个task，那么/tasks/{topology-id}下面一共会有6个以task-id命名的文件，其中两个文件的内容是spout的id, 其它四个文件的内容是bolt的id。 

task->node+port, 它其实就是从task-id到supervisor-id+port的映射， 也就是把这个task分配给某台机器的某个端口来做。 

topology里面的组件(spout/bolt)都根据parallelism被分成多个task, 而这些task被分配给supervisor的多个worker来执行。 

task都会跟一个componment-id关联, componment是spout和bolt的一个统称. 

对于每一个component在部署的时候都会指定使用的数量, 在storm-user中有一个讨论说明了这个问题: 
里面的大意是说, 通过设置parallelism来指定执行spout/bolt的线程数量. 而在配置中还有另外一个地方(backtype.storm.Config.setNumWorkers(int))来指定一个storm集群中执行topolgy的进程数量, 所有的线程将在这些指定的worker进程中运行. 比如说一个topology中要启动300个线程来运行spout/bolt, 而指定的worker进程数量是60个, 那么storm将会给每个worker分配5个线程来跑spout/bolt, 如果要对一个topology进行调优, 可以调整worker数量和spout/bolt的parallelism数量(调整参数之后要记得重新部署topology. 后续会为该操作提供一个swapping的功能来减小重新部署的时间). 

对于worker和task之间的比例, nathan也给出了参考, 即1个worker包含10~15个左右, 当然这个参考, 实际情况还是要根据配置和测试情况。 


![基础组件之间的关系][7]

1. worker是一个进程，由supervisor启动，并只负责处理一个topology，所以不会同时处理多个topology.
2. executor是一个线程，由worker启动，是运行task的物理容器，其和task是1 -> N关系.
3. component是对spout/bolt/acker的抽象.
4. task也是对spout/bolt/acker的抽象，不过是计算了并行度之后。component和task是1 -> N 的关系.

supervisor会定时从zookeeper获取topologies、已分配的任务分配信息assignments及各类心跳信息，以此为依据进行任务分配。

在supervisor周期性地进行同步时，会根据新的任务分配来启动新的worker或者关闭旧的worker，以响应任务分配和负载均衡。

worker通过定期的更新connections信息，来获知其应该通讯的其它worker。

worker启动时，会根据其分配到的任务启动一个或多个executor线程。这些线程仅会处理唯一的topology。

executor线程负责处理多个spouts或者多个bolts的逻辑，这些spouts或者bolts，也称为tasks。

参考：

1. [storm基础框架分析][12]


### 并行度

1. [理解Storm中的拓扑的并发度][11]
2. [并行度调度算法][16]
3. [Storm如何分配任务和负载均衡？][18]
4. [Strom并行度详解][19]

### 常见模式

1. [流聚合][13]


### DRPC

1. [DRPC简介][14]


### Trident

1. [Storm Trident 详细介绍][15]


### Ack机制

作者是一拍脑袋想到了用20个字节来追踪每条Spout出来的消息被处理的情况，原理是XOR的时候，N XOR N=0，同一个值以任意次序XOR两次会归0，如A XOR B XOR B XOR C XOR A XOR C =0， 在发出Tuple时，就用随机产生的Tuple Id XOR一下。等接收的Bolt ack时，再XOR一下，就会归0。所以当消息以任意的顺序会流经很多节点，产生很多新Tuple，如果都被成功处理，即所有Tuple id都被以任意顺序执行了两次XOR，则这20个字节最后应该重新归0，就可判断全部ack完毕。

另外，重发是从最上游的Spout开始，如果某个bolt的操作是非幂等的，还要想想怎么自己去实现去重。

1. [Storm的ack机制在项目应用中的坑][17]


## 工具

1. [YAML 语法校验][2]
2. [storm-monitor][10]


## 参考文档

1. [Storm中文文档][1]
2. [Storm中文文档Github地址][4]
3. [Storm Github][3]
4. [Apache Storm 官方文档中文版-并发编程网][5]
5. [Storm 从入门到放弃][6]
6. [庄周梦蝶][9]
7. [疯狂的菠菜macrochen的博客][8]
8. [江南白衣][20] 


## 项目介绍


### storm-demo

一系列storm demo


### storm-flux 

简单的word count程序，使用YMAL DSL定义storm拓扑。


    
 [1]: http://storm.apachecn.org/releases/cn/1.1.0/
 [2]: http://www.yamllint.com/
 [3]: https://github.com/apache/storm
 [4]: https://github.com/apachecn/storm-doc-zh
 [5]: http://ifeve.com/apache-storm/
 [6]: http://www.cnblogs.com/intsmaze/p/7274361.html
 [7]: http://olml6iu96.bkt.clouddn.com/storm/%E5%9F%BA%E7%A1%80%E7%BB%84%E4%BB%B6%E4%B9%8B%E9%97%B4%E7%9A%84%E5%85%B3%E7%B3%BB.png
 [8]: http://macrochen.iteye.com/category/202803
 [9]: http://blog.fnil.net/
 [10]: https://github.com/killme2008/storm-monitor
 [11]: http://damacheng009.iteye.com/blog/2076758
 [12]: http://www.cnblogs.com/foreach-break/p/storm_worker_executor_spout_bolt_simbus_supervisor_mk-assignments.html
 [13]: http://www.aboutyun.com/thread-7381-1-1.html
 [14]: https://www.cnblogs.com/hd3013779515/p/6999952.html
 [15]: http://www.aboutyun.com/thread-9217-1-1.html
 [16]: https://www.cnblogs.com/shijianlvzhe/p/6057174.html
 [17]: http://www.cnblogs.com/intsmaze/p/5918087.html
 [18]: https://www.linuxidc.com/Linux/2015-07/120466.htm
 [19]: http://www.it610.com/article/2146630.htm#_motz_
 [20]: http://calvin1978.blogcn.com/articles/stormnotes.html