package com.xuecheng.manage_media.service;

import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResultCode;
import com.xuecheng.manage_media.dao.MediaFileRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MediaFileService {

    @Autowired
    MediaFileRepository mediaFileRepository;

    public QueryResponseResult findList(int page, int size, QueryMediaFileRequest queryMediaFileRequest) {
        MediaFile mediaFile = new MediaFile();
        if (StringUtils.isNotBlank(queryMediaFileRequest.getFileOriginalName())) {
            mediaFile.setFileOriginalName(queryMediaFileRequest.getFileOriginalName());
        }
        if (StringUtils.isNotBlank(queryMediaFileRequest.getProcessStatus())) {
            mediaFile.setProcessStatus(queryMediaFileRequest.getProcessStatus());
        }
        if (StringUtils.isNotBlank(queryMediaFileRequest.getTag())) {
            mediaFile.setTag(queryMediaFileRequest.getTag());
        }

        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("tag", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("fileOriginalName", ExampleMatcher.GenericPropertyMatchers.contains());

        Example<MediaFile> example = Example.of(mediaFile,exampleMatcher);
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<MediaFile> mediaFiles = mediaFileRepository.findAll(example, pageable);

        List<MediaFile> mediaFileList = mediaFiles.getContent();
        QueryResult queryResult = new QueryResult();
        queryResult.setList(mediaFileList);
        queryResult.setTotal(mediaFiles.getTotalElements());
        return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
    }
}
