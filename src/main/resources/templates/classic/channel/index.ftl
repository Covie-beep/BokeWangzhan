<@layout.extends name="/inc/layout.ftl">
    <@layout.put block="title">
        <title>channel.name</title>
    </@layout.put>

    <@layout.put block="contents">
        <div class="row">
            <div class="col-xs-12 col-md-9 side-left">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <ul class="list-inline topic-filter">
                            <li><a href="?order=newest" <#if order! == 'newest'>class="active"</#if>>最新</a></li>
                            <li><a href="?order=likes" <#if order! == 'likes'>class="active"</#if>>最多赞</a></li>
                            <li><a href="?order=hottest" <#if order! == 'hottest'>class="active"</#if>>热门</a></li>
                        </ul>
                    </div>
                </div>
                <@contents channelId=channel.id pageNo=pageNo order=order>
                    <div class="posts">
                        <ul class="posts-list">
                            <@layout.extends name="/inc/posts_item.ftl" />
                            <#list results.content as row>
                                <@posts_item row/>
                            </#list>
                            <#if  results.content?size == 0>
                            <li class="content">
                                <div class="content-box posts-aside">
                                    <div class="posts-item">该目录下还没有内容!</div>
                                </div>
                            </li>
                            </#if>
                        </ul>
                    </div>

                    <!-- Pager -->
                    <div class="text-center">
                        <@utils.pager request.requestURI!"", results, 5/>
                    </div>
                </@contents>

            </div>

            <div class="col-xs-12 col-md-3 side-right">
                <@layout.extends name="/inc/right.ftl" />
            </div>
        </div>
    </@layout.put>
</@layout.extends>

