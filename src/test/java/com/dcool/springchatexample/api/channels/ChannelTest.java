package com.dcool.springchatexample.api.channels;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
public class ChannelTest{

    protected Log logger = LogFactory.getLog(getClass());

    private @Autowired MockMvc mvc;

    private @Autowired ChannelDocs cd;

    @Test
    void contextLoads() throws Exception {

        mvc.perform(cd.POST("test1", "/api/channels/search", cd.newEntity(null, "test2")))
            .andDo(cd.print())
            .andExpect(cd.is2xx());
    }


}
