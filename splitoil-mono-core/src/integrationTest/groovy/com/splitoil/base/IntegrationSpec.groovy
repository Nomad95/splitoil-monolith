package com.splitoil.base

import com.splitoil.SplitoilMonoApplication
import com.splitoil.infrastructure.json.JacksonAdapter
import groovy.transform.TypeChecked
import org.junit.Before
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity

@TypeChecked
@SpringBootTest(classes = SplitoilMonoApplication)
@ActiveProfiles("test")
@Transactional
@Rollback
@Sql(scripts = '/db/user/user_admin.sql')
class IntegrationSpec extends Specification {

    @Autowired
    protected WebApplicationContext webApplicationContext

    protected JacksonAdapter jackson = JacksonAdapter.getInstance()

    MockMvc mockMvc

    @Before
    void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build()
    }
}
