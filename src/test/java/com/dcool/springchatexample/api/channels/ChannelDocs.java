package com.dcool.springchatexample.api.channels;

import com.dcool.common.rest.docs.MockMvcRestDocs;
import com.dcool.common.rest.docs.RestDocumentationResultHandlerBuilder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ChannelDocs extends MockMvcRestDocs {
    public Map<String, Object> newEntity(String groupId, Object... users) {
        Map<String,Object> e = new HashMap<>();
        e.put("inviteUsers", Arrays.asList(users));
        e.put("channelName", "Channel"+super.randomInt());
        e.put("organization", groupId);
        return e;
    }

    public void search(RestDocumentationResultHandlerBuilder b) {
        b.requestFields().fieldWithPath("inviteUsers").description("채팅방 참여자 리스트").type("Object");
        b.requestFields().fieldWithPath("channelName").description("채팅방 이름");

        b.responseFields().subsectionWithPath("_embedded").description("Embedded resources containing the search results").type("Object");
        b.responseFields().fieldWithPath("_embedded.channels[]").description("List of channel").type("List");
        b.responseFields().subsectionWithPath("_links").description("Links to other resources").type("Object");
        b.responseFields().subsectionWithPath("page").description("Page Info").type("Object");
    }
}
