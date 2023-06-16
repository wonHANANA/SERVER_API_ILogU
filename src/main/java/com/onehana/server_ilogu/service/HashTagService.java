package com.onehana.server_ilogu.service;

import com.onehana.server_ilogu.entity.Board;
import com.onehana.server_ilogu.entity.Hashtag;
import com.onehana.server_ilogu.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
public class HashTagService {

    private final HashtagRepository hashtagRepository;

    public void createTagList(Board board) {
        Pattern tagPattern = Pattern.compile("#(\\S+)");
        Matcher mat = tagPattern.matcher(board.getContent());
        Set<String> tagList = new LinkedHashSet<>();

        while(mat.find()) {
            tagList.add(mat.group(1));
        }

        for (String tag : tagList) {
            Hashtag hashtag = hashtagRepository.findByHashtagName(tag);
            if (hashtag == null) {
                hashtag = Hashtag.of(tag);
                hashtagRepository.save(hashtag);
            }
            board.addHashtag(hashtag);
        }
    }
}
