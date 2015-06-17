# 基于Mongo's GridFS的分布式图片存储服务 #
## 快速入门 ##
首先你需要搭起一个mongodb（最好用64位版本）<br>
如果不知道mongodb的话，可以google一下<br>
安装好mongodb后，你可以选择自己下源码，再用maven打包成可执行的jar文件，也可以<a href='http://code.google.com/p/kissme-photo/downloads/list'>点击这里</a>下载<br>
如果你是下了源码，那么你可以：<br>
<pre><code>mvn package<br>
</code></pre>
假设，下下来（或打包后）的jar包叫photo-0.0.2.jar，并且放在D盘（生产环境下记得用别的）中<br>
执行一下命令：<br>
<pre><code>cd D:<br>
java -jar photo-0.0.2.jar -p [监听端口] -t [处理线程数] -chunksize [最大的chunksize] -dbconfpath [db配置路径]<br>
</code></pre>
或者你可以先：<br>
<pre><code>java -jar photo-0.0.2.jar -h<br>
</code></pre>

所有的选项都不是必须的，它们都有一些默认值，你可以选择性的调整它们<br>
db默认的配置是127.0.0.1，端口27017，dbname为photo<br>
如果你需要指定其它的db路径，请带上-dbconfpath参数，指定一个配置文件<br>
配置文件是一个utf-8编码的json文件<br>
里面的内容会是这样子的：<br>
<pre><code>{"address" : "db的地址","port" : 80,"dbname" : "photo","username" : "db用户","password":"db密码"...}<br>
</code></pre>

<h3>DB接受的配置参数</h3>
<table><thead><th> <b>参数名</b> </th><th> <b>参数含义</b> </th></thead><tbody>
<tr><td> address          </td><td> db地址            </td></tr>
<tr><td> port             </td><td> db监听端口      </td></tr>
<tr><td> dbname           </td><td> db名字            </td></tr>
<tr><td> username         </td><td> db用户名         </td></tr>
<tr><td> password         </td><td> db密码            </td></tr>
<tr><td> autoConnectRetry </td><td> 是否自动重新连接（默认true） </td></tr>
<tr><td> socketKeepAlive  </td><td> 是否保持连接（默认true） </td></tr>
<tr><td> connnectionsPerHost </td><td> 连接池最大数（默认100） </td></tr>
<tr><td> connectTimeout   </td><td> 连接超时时间（默认3000毫秒） </td></tr>
<tr><td> maxConnectRetryTime </td><td> 自动重新连接的次数（默认3次）</td></tr></tbody></table>

<h2>服务API</h2>
<h3>管理API:</h3>
<table><thead><th> <b>请求URL</b> </th><th> <b>请求方法</b> </th><th> <b>功能</b> </th></thead><tbody>
<tr><td> /admin/app/      </td><td> get                 </td><td> 分页查询App </td></tr>
<tr><td> /admin/app/      </td><td> post                </td><td> 创建App     </td></tr>
<tr><td> /admin/app/{id}  </td><td> delete              </td><td> 删除指定id的App </td></tr>
<tr><td> /admin/gallery/  </td><td> get                 </td><td> 分页查询图库 </td></tr>
<tr><td> /admin/gallery/{id} </td><td> delete              </td><td> 删除指定id的图库 </td></tr>
<tr><td> /admin/photo/    </td><td> get                 </td><td> 分页查询图片 </td></tr>
<tr><td> /admin/photo/{id} </td><td> delete              </td><td> 删除指定id的图片 </td></tr>
<tr><td> /admin/vm/       </td><td> get or post         </td><td> 获取当前jvm的状态（如使用了多少堆内存） </td></tr></tbody></table>

<h3>用户API：</h3>
<table><thead><th> <b>请求URL</b> </th><th> <b>请求方法</b> </th><th> <b>功能</b> </th></thead><tbody>
<tr><td> /gallery/        </td><td> get                 </td><td> 分页查询图库 </td></tr>
<tr><td> /gallery/        </td><td> post                </td><td> 创建新的图库 </td></tr>
<tr><td> /gallery/{id}    </td><td> put                 </td><td> 修改指定id的图库 </td></tr>
<tr><td> /gallery/{id}    </td><td> delete              </td><td> 删除指定id的图库 </td></tr>
<tr><td> /photo/          </td><td> get                 </td><td> 分页查询图片 </td></tr>
<tr><td> /photo/          </td><td> post                </td><td> 上传新的图片 </td></tr>
<tr><td> /photo/{id}      </td><td> delete              </td><td> 删除指定id的图片 </td></tr>
<tr><td> /photo/{id}      </td><td> get                 </td><td> 获取指定id的图片 </td></tr></tbody></table>

<h2>关于安全</h2>
既然是服务，那么我希望它应该足够的简单<br>
服务之内，不要出现类似session这样的东西<br>
毕竟，一旦有状态有session之类的东西出现，如何粘滞会话维护状态也要花一翻功夫<br>
忘掉session这东西吧，阿门<br>
取而代之的是：每次请求带上一个签名，服务端验证该请求是否有效<br>
管理API的签名会是这样子的：<br>
<pre><code>http://localhost/admin/app/?timestamp=1348904357342&amp;signature=d52533035c24082421b985c58c140aa57b21f1a5&amp;callback=123<br>
</code></pre>
用管理员的账号密码对时间戳做hmac-sha1签名，再编码为十六进制，好像是够用了<br>
用户API的请求则会是这样子：<br>
<pre><code>http://localhost/gallery/?timestamp=1348904357342&amp;signature=1094eeea17286dbfb0761149ce1236d29f9383d3&amp;appkey=R7BJVv&amp;callback=123<br>
</code></pre>
每次请求也是要带上时间戳，不同的地方在于需要提交appkey，以及签名的密钥为appsecret<br>
如果你习惯用python，签名的代码可能会是这样子：<br>
<pre><code>current = str(int(time.time() * 1000)) # 仅仅是个示例<br>
signature = hmac.new(appsecret, current, hashlib.sha1).hexdigest()<br>
</code></pre>
<h2>关于callback参数</h2>
以上所说的所有API，只要带上了callback参数，那么就是一个jsonp调用<br>
至于为什么这么做，可能是做跨域调用最简单的方法吧<br>
如果不带callback参数，那么返回的就是json格式的数据<br>
json对比xml等其它东西，可以减少大量无用的数据，对于浏览器而言，也更加友好<br>

<h2>除此之外</h2>
既然涉及到图片，总是离不开压缩，翻转，缩放等功能<br>
而这些恰好也提供了<br>
你可以在上传图片的时候，试试这样子：<br>
<pre><code>curl -d "timestamp=xxx&amp;appkey=xxx&amp;signature=xxx&amp;gallery=xxx&amp;width=320&amp;height=480" http://localhost/photo/<br>
</code></pre>
或者是在访问图片的时候，这样子：<br>
<pre><code>http://localhost/photo/xxx?width=320&amp;height=240&amp;rotate=90&amp;quality=0.8<br>
</code></pre>
嗯，慢了少许，不过效果还可以<br>
不过，url貌似好长。试试伪静态吧<br>
<h3>以下是支持的参数：</h3>
<table><thead><th> <b>参数名</b> </th><th> <b>含义</b> </th><th> <b>有效的值</b> </th></thead><tbody>
<tr><td> rotate           </td><td> 翻转        </td><td> 0-360               </td></tr>
<tr><td> quality          </td><td> 图片质量  </td><td> 大于0并且小于1 </td></tr>
<tr><td> width            </td><td> 图片宽度  </td><td> 大于0             </td></tr>
<tr><td> height           </td><td> 图片高度  </td><td> 大于0             </td></tr>
<tr><td> cropX            </td><td> 裁剪的x坐标 </td><td> 0-width             </td></tr>
<tr><td> cropY            </td><td> 裁剪的y坐标 </td><td> 0-height            </td></tr></tbody></table>

<h2>说了那么多好的，说点不好的</h2>
在mongo gridfs中，它把文件分成了一块一块（chunk），这些chunk由mongo来管理。每次的读写都需要先找一次metadata来定位chunk，嗯，有点合乎情理。那么对大量小图片的话，好像有点代价太高，因为每次都要做query。特别是大量并发的读请求时，这部分东西，可能会大大的影响整体速度