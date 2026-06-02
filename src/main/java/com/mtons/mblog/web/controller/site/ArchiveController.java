package com.mtons.mblog.web.controller.site;

import com.mtons.mblog.base.lang.Consts;
import com.mtons.mblog.base.utils.BeanMapUtils;
import com.mtons.mblog.modules.data.PostVO;
import com.mtons.mblog.modules.service.PostService;
import com.mtons.mblog.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ArchiveController extends BaseController {
    @Autowired
    private PostService postService;

    @RequestMapping("/archive")
    public String index(ModelMap model) {
        model.put("archives", postService.findArchives());
        return view("/archive/index");
    }

    @RequestMapping("/archive/{year}/{month}")
    public String view(@PathVariable int year,
                       @PathVariable int month,
                       ModelMap model,
                       HttpServletRequest request) {
        int pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
        String order = ServletRequestUtils.getStringParameter(request, "order", Consts.order.NEWEST);
        Page<PostVO> posts = postService.pagingByArchive(
                wrapPageable(pageNo, Consts.PAGE_DEFAULT_SIZE, Sort.by(Sort.Direction.DESC, BeanMapUtils.postOrder(order))),
                year,
                month);
        model.put("year", year);
        model.put("month", month);
        model.put("order", order);
        model.put("pageNo", pageNo);
        model.put("posts", posts);
        return view("/archive/view");
    }
}
