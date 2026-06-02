<@layout.extends name="/inc/layout.ftl">
    <@layout.put block="title">
        <title>${year}年${month}月 - 文章归档 - ${options['site_name']}</title>
    </@layout.put>

    <@layout.put block="contents">
        <div class="row">
            <div class="col-xs-12 col-md-9 side-left">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">${year} 年 ${month} 月 的文章</h3>
                    </div>
                </div>
                <div class="posts">
                    <ul class="posts-list">
                        <@layout.extends name="/inc/posts_item.ftl" />
                        <#list posts.content as row>
                            <@posts_item row/>
                        </#list>
                        <#if posts.content?size == 0>
                            <li class="content">
                                <div class="content-box posts-aside">
                                    <div class="posts-item">该月份还没有文章!</div>
                                </div>
                            </li>
                        </#if>
                    </ul>
                </div>
                <div class="text-center">
                    <@utils.pager "${base}/archive/${year}/${month}", posts, 5/>
                </div>
            </div>
            <div class="col-xs-12 col-md-3 side-right">
                <@layout.extends name="/inc/right.ftl" />
            </div>
        </div>
    </@layout.put>
</@layout.extends>
