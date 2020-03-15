package com.splitoil

import org.junit.experimental.categories.Category
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@Category(IntegrationTest)
@SpringBootTest
class SpringBootExampleTest extends Specification {

    def "Context up"() {
        expect:
            1 + 1 == 2
    }

}
