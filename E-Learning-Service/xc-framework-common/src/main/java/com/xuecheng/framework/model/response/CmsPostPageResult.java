package com.xuecheng.framework.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CmsPostPageResult extends ResponseResult {
    String pageUrl;
    public CmsPostPageResult(ResultCode resultCode,String pageUrl) { super(resultCode);
        this.pageUrl = pageUrl;
    }
}

