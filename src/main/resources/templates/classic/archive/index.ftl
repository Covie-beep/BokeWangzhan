<@layout.extends name="/inc/layout.ftl">
    <@layout.put block="title">
        <title>文章归档 - ${options['site_name']}</title>
    </@layout.put>

    <@layout.put block="contents">
        <div class="row">
            <div class="col-xs-12 col-md-9 side-left">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">文章归档</h3>
                    </div>
                    <div class="panel-body">
                        <ul class="list-group">
                            <#list archives as row>
                                <li class="list-group-item">
                                    <a href="${base}/archive/${row.year}/${row.month}">
                                        ${row.year} 年 ${row.month} 月
                                        <span class="badge pull-right">${row.count}</span>
                                    </a>
                                </li>
                            </#list>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="col-xs-12 col-md-3 side-right">
                <@layout.extends name="/inc/right.ftl" />
            </div>
        </div>
    </@layout.put>
</@layout.extends>
