package com.mtons.mblog.modules.template.directive;

import com.mtons.mblog.modules.service.PostService;
import com.mtons.mblog.modules.template.DirectiveHandler;
import com.mtons.mblog.modules.template.TemplateDirective;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArchiveDirective extends TemplateDirective {
    @Autowired
    private PostService postService;

    @Override
    public String getName() {
        return "archives";
    }

    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        int size = handler.getInteger("size", 12);
        handler.put(RESULTS, postService.findArchives().stream().limit(size).collect(java.util.stream.Collectors.toList()));
        handler.render();
    }
}
